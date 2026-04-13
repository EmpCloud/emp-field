// ============================================================================
// LEGACY PUBLIC / UNAUTHORIZED SERVICE
// ============================================================================
//
// Maps every endpoint from commit 3409d88's unauthorized.routes.js — the
// pre-auth surface used by mobile and admin clients (verify, login, OTP,
// forgot password, attendance check-in via legacy phone-number flow, imports).
// ============================================================================

import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import { v4 as uuidv4 } from "uuid";
import otpGenerator from "../../utils/otp";
import { config } from "../../config";
import { getDB } from "../../db/connection";
import { AppError, NotFoundError, ValidationError } from "../../utils/errors";

export const publicService = {
  async verifyEmail(email: string) {
    const db = getDB();
    const user = await db("employee_profiles").where({ email }).first();
    if (!user) throw new AppError(404, "EMAIL_NOT_FOUND", "Email Not Present");
    return { message: "Email Verified" };
  },

  async verifyPhone(phoneNumber: string) {
    const db = getDB();
    const cleaned = String(phoneNumber).replace(/[^0-9]/g, "");
    if (cleaned.length < 10) throw new ValidationError("Invalid phone number");
    const user = await db("employee_profiles").where({ phone_number: cleaned }).first();
    if (!user) throw new NotFoundError("phone", phoneNumber);
    const token = jwt.sign(
      {
        empcloudUserId: user.user_id,
        empcloudOrgId: user.organization_id,
        email: user.email,
        role: "employee",
        firstName: user.full_name?.split(" ")[0] || "",
        lastName: user.full_name?.split(" ").slice(1).join(" ") || "",
        orgName: "",
        fieldProfileId: user.id,
      },
      config.jwt.secret,
      { expiresIn: "24h" },
    );
    return { user, accessToken: token };
  },

  async setPassword(email: string, password: string) {
    const db = getDB();
    const user = await db("employee_profiles").where({ email }).first();
    if (!user) throw new NotFoundError("user", email);
    const hash = await bcrypt.hash(password, 10);
    // Stored separately in admin_users (employee_profiles is profile-only)
    const adminRow = await db("admin_users").where({ email }).first();
    if (adminRow) {
      await db("admin_users").where({ id: adminRow.id }).update({
        password: hash,
        updated_at: new Date(),
      });
    }
    return { ok: true };
  },

  async userLogin(email: string, password: string) {
    const db = getDB();
    const admin = await db("admin_users").where({ email }).first();
    if (!admin) throw new AppError(401, "INVALID_CREDENTIALS", "Email Does not Exist");
    if (admin.is_suspended) throw new AppError(403, "SUSPENDED", "User Suspended");
    const ok = await bcrypt.compare(password, admin.password);
    if (!ok) throw new AppError(401, "INVALID_PASSWORD", "Incorrect User password");
    const token = jwt.sign(
      {
        empcloudUserId: admin.id,
        empcloudOrgId: admin.organization_id,
        email: admin.email,
        role: "org_admin",
        firstName: admin.full_name?.split(" ")[0] || "",
        lastName: admin.full_name?.split(" ").slice(1).join(" ") || "",
        orgName: "",
        fieldProfileId: null,
      },
      config.jwt.secret,
      { expiresIn: "24h" },
    );
    return { user: { ...admin, password: undefined }, accessToken: token };
  },

  async forgotPassword(email: string) {
    const db = getDB();
    const admin = await db("admin_users").where({ email }).first();
    if (!admin) throw new NotFoundError("admin", email);
    const token = otpGenerator.generate(4);
    const expires = new Date(Date.now() + 24 * 60 * 60 * 1000);
    await db("admin_users").where({ id: admin.id }).update({
      forgot_password_token: token,
      forgot_token_expire: expires,
      password_email_sent_count: (admin.password_email_sent_count || 0) + 1,
      updated_at: new Date(),
    });
    return { token, expires };
  },

  async verifyOTP(email: string, otp: string) {
    const db = getDB();
    const admin = await db("admin_users").where({ email }).first();
    if (!admin) throw new NotFoundError("admin", email);
    if (admin.forgot_password_token !== otp) {
      throw new AppError(400, "INVALID_OTP", "Invalid OTP");
    }
    if (admin.forgot_token_expire && new Date(admin.forgot_token_expire) < new Date()) {
      throw new AppError(400, "OTP_EXPIRED", "OTP expired");
    }
    return { verified: true };
  },

  async resetPassword(email: string, token: string, newPassword: string) {
    const db = getDB();
    const admin = await db("admin_users").where({ email }).first();
    if (!admin) throw new NotFoundError("admin", email);
    if (admin.forgot_password_token !== token) {
      throw new AppError(400, "INVALID_TOKEN", "Invalid reset token");
    }
    const hash = await bcrypt.hash(newPassword, 10);
    await db("admin_users").where({ id: admin.id }).update({
      password: hash,
      forgot_password_token: null,
      updated_at: new Date(),
    });
    return { ok: true };
  },

  async generateToken(email: string) {
    return publicService.forgotPassword(email);
  },

  async generateOtp() {
    return { otp: otpGenerator.generate(6) };
  },

  // ---- post-auth (called via verifyToken middleware in old version) -----
  async updateProfile(orgId: number, userId: number, patch: any) {
    const db = getDB();
    const fields: any = { updated_at: new Date() };
    for (const k of [
      "full_name",
      "email",
      "phone_number",
      "address1",
      "city",
      "state",
      "country",
      "zip_code",
    ]) {
      if (patch[k] !== undefined) fields[k] = patch[k];
    }
    await db("employee_profiles")
      .where({ organization_id: orgId, user_id: userId })
      .update(fields);
    return db("employee_profiles")
      .where({ organization_id: orgId, user_id: userId })
      .first();
  },

  async attendanceCheckIn(orgId: number, userId: number, payload: any) {
    const db = getDB();
    const dateKey = new Date().toISOString().slice(0, 10);
    const existing = await db("attendance_records")
      .where({ organization_id: orgId, user_id: userId, attendance_date: dateKey })
      .first();
    if (existing) {
      await db("attendance_records").where({ id: existing.id }).update({
        check_out: new Date(),
        check_out_lat: payload.latitude,
        check_out_lng: payload.longitude,
        updated_at: new Date(),
      });
      return db("attendance_records").where({ id: existing.id }).first();
    }
    const id = uuidv4();
    await db("attendance_records").insert({
      id,
      organization_id: orgId,
      user_id: userId,
      attendance_date: dateKey,
      check_in: new Date(),
      check_in_lat: payload.latitude,
      check_in_lng: payload.longitude,
      status: "present",
      source: "field",
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("attendance_records").where({ id }).first();
  },

  async getAttendance(orgId: number, userId: number) {
    return getDB()("attendance_records")
      .where({ organization_id: orgId, user_id: userId })
      .orderBy("attendance_date", "desc")
      .limit(60);
  },

  async importUsers(orgId: number, users: any[]) {
    const db = getDB();
    let imported = 0;
    for (const u of users) {
      try {
        await db("employee_profiles").insert({
          id: uuidv4(),
          organization_id: orgId,
          user_id: u.user_id || 0,
          full_name: u.full_name || "",
          email: u.email || "",
          emp_id: u.emp_id || null,
          phone_number: u.phone_number || null,
          status: 1,
          created_at: new Date(),
          updated_at: new Date(),
        });
        imported++;
      } catch {
        /* skip dup */
      }
    }
    return { imported };
  },

  async adminImportUsers(orgId: number, users: any[]) {
    const db = getDB();
    let imported = 0;
    for (const u of users) {
      try {
        const hash = await bcrypt.hash(u.password || "changeme", 10);
        await db("admin_users").insert({
          id: uuidv4(),
          organization_id: orgId,
          full_name: u.full_name,
          gender: u.gender || "other",
          email: u.email,
          password: hash,
          city: u.city || "",
          state: u.state || "",
          country: u.country || "",
          phone_number: u.phone_number || "",
          timezone: u.timezone || "UTC",
          created_at: new Date(),
          updated_at: new Date(),
        });
        imported++;
      } catch {
        /* skip dup */
      }
    }
    return { imported };
  },

  async deleteUserPerm(orgId: number, userId: number) {
    const db = getDB();
    await db("employee_profiles").where({ organization_id: orgId, user_id: userId }).delete();
    return { ok: true };
  },

  // ---- EmpMonitor compatibility endpoints ----
  async getLocationList(orgId: number) {
    return getDB()("org_geo_locations").where({ organization_id: orgId });
  },
  async getGeoLocationDetails(orgId: number, employeeId: number) {
    return getDB()("employee_geo_settings")
      .where({ organization_id: orgId, employee_id: employeeId })
      .first();
  },
  async updateGeoLocationDetails(orgId: number, employeeId: number, patch: any) {
    const db = getDB();
    const existing = await db("employee_geo_settings")
      .where({ organization_id: orgId, employee_id: employeeId })
      .first();
    if (existing) {
      await db("employee_geo_settings").where({ id: existing.id }).update({
        latitude: patch.latitude,
        longitude: patch.longitude,
        range: patch.range,
        geo_fencing: patch.geo_fencing,
        updated_at: new Date(),
      });
      return db("employee_geo_settings").where({ id: existing.id }).first();
    }
    const id = uuidv4();
    await db("employee_geo_settings").insert({
      id,
      organization_id: orgId,
      employee_id: employeeId,
      latitude: patch.latitude,
      longitude: patch.longitude,
      range: patch.range || 10,
      geo_fencing: !!patch.geo_fencing,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("employee_geo_settings").where({ id }).first();
  },
  async empEmployeeImport(orgId: number, users: any[]) {
    return publicService.importUsers(orgId, users);
  },
};
