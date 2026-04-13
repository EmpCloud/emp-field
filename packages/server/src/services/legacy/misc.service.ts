// ============================================================================
// LEGACY MODULE SERVICES (consolidated)
// ============================================================================
//
// One file per old module would explode the directory. These are the small
// CRUD modules from commit 3409d88; each exports a namespaced object so the
// route layer stays explicit.
//
// Modules included:
//   holiday, leaveType/leaveRequest, role, profile, category, fieldTag,
//   transport, tracking, hrmsAdmin, autoEmailReport, legacyClient,
//   legacyUser, attendance, task, reports
// ============================================================================

import { v4 as uuidv4 } from "uuid";
import { getDB } from "../../db/connection";
import { NotFoundError, ValidationError } from "../../utils/errors";

// ---------------------------------------------------------------------------
// HOLIDAY
// ---------------------------------------------------------------------------
export const holidayService = {
  async create(orgId: number, payload: { name: string; date: string }) {
    const db = getDB();
    if (!payload.name || !payload.date) throw new ValidationError("name and date required");
    const id = uuidv4();
    await db("holidays").insert({
      id,
      organization_id: orgId,
      name: payload.name,
      date: payload.date,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("holidays").where({ id }).first();
  },
  async list(orgId: number) {
    return getDB()("holidays").where({ organization_id: orgId }).orderBy("date", "asc");
  },
  async update(orgId: number, id: string, patch: { name?: string; date?: string }) {
    const db = getDB();
    const allowed: any = { updated_at: new Date() };
    if (patch.name !== undefined) allowed.name = patch.name;
    if (patch.date !== undefined) allowed.date = patch.date;
    await db("holidays").where({ id, organization_id: orgId }).update(allowed);
    return db("holidays").where({ id }).first();
  },
  async remove(orgId: number, id: string) {
    await getDB()("holidays").where({ id, organization_id: orgId }).delete();
    return { ok: true };
  },
};

// ---------------------------------------------------------------------------
// LEAVE TYPES + REQUESTS
// ---------------------------------------------------------------------------
export const leaveService = {
  async createType(orgId: number, payload: any) {
    const db = getDB();
    const id = uuidv4();
    await db("leave_types").insert({
      id,
      organization_id: orgId,
      name: payload.name,
      duration: payload.duration,
      no_of_days: payload.no_of_days,
      carry_forward: payload.carry_forward ?? 0,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("leave_types").where({ id }).first();
  },
  async listTypes(orgId: number) {
    return getDB()("leave_types").where({ organization_id: orgId });
  },
  async updateType(orgId: number, id: string, patch: any) {
    const db = getDB();
    const allowed: any = { updated_at: new Date() };
    for (const k of ["name", "duration", "no_of_days", "carry_forward"]) {
      if (patch[k] !== undefined) allowed[k] = patch[k];
    }
    await db("leave_types").where({ id, organization_id: orgId }).update(allowed);
    return db("leave_types").where({ id }).first();
  },
  async deleteType(orgId: number, id: string) {
    await getDB()("leave_types").where({ id, organization_id: orgId }).delete();
    return { ok: true };
  },
  async leaveTypeOption(orgId: number) {
    return getDB()("leave_types").where({ organization_id: orgId }).select("id", "name");
  },
  async listLeaves(orgId: number, filters: any) {
    let q = getDB()("leave_requests").where({ organization_id: orgId });
    if (filters?.user_id) q = q.andWhere("user_id", filters.user_id);
    if (filters?.status) q = q.andWhere("status", filters.status);
    return q.orderBy("created_at", "desc");
  },
  async createLeave(orgId: number, userId: number, payload: any) {
    const db = getDB();
    const id = uuidv4();
    await db("leave_requests").insert({
      id,
      organization_id: orgId,
      user_id: userId,
      leave_type_id: payload.leave_type_id || null,
      start_date: payload.start_date,
      end_date: payload.end_date,
      days: payload.days,
      reason: payload.reason || null,
      status: "pending",
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("leave_requests").where({ id }).first();
  },
  async updateLeave(orgId: number, id: string, patch: any) {
    const db = getDB();
    const allowed: any = { updated_at: new Date() };
    for (const k of ["start_date", "end_date", "days", "reason", "status"]) {
      if (patch[k] !== undefined) allowed[k] = patch[k];
    }
    await db("leave_requests").where({ id, organization_id: orgId }).update(allowed);
    return db("leave_requests").where({ id }).first();
  },
  async deleteLeave(orgId: number, id: string) {
    await getDB()("leave_requests").where({ id, organization_id: orgId }).delete();
    return { ok: true };
  },
};

// ---------------------------------------------------------------------------
// ROLES
// ---------------------------------------------------------------------------
export const roleService = {
  async create(orgId: number, role: string) {
    const db = getDB();
    if (!role) throw new ValidationError("role required");
    const id = uuidv4();
    await db("field_roles").insert({
      id,
      organization_id: orgId,
      role,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("field_roles").where({ id }).first();
  },
  async list(orgId: number) {
    return getDB()("field_roles").where({ organization_id: orgId });
  },
  async update(orgId: number, id: string, role: string) {
    const db = getDB();
    await db("field_roles").where({ id, organization_id: orgId }).update({ role, updated_at: new Date() });
    return db("field_roles").where({ id }).first();
  },
  async remove(orgId: number, id: string) {
    await getDB()("field_roles").where({ id, organization_id: orgId }).delete();
    return { ok: true };
  },
};

// ---------------------------------------------------------------------------
// PROFILE
// ---------------------------------------------------------------------------
export const profileService = {
  async fetch(orgId: number, userId: number) {
    return getDB()("employee_profiles")
      .where({ organization_id: orgId, user_id: userId })
      .first();
  },
  async update(orgId: number, userId: number, patch: any) {
    const db = getDB();
    const existing = await db("employee_profiles")
      .where({ organization_id: orgId, user_id: userId })
      .first();
    const fields: any = {};
    for (const k of [
      "full_name",
      "age",
      "gender",
      "email",
      "profile_pic",
      "location",
      "department",
      "role",
      "emp_id",
      "address1",
      "address2",
      "latitude",
      "longitude",
      "city",
      "state",
      "country",
      "zip_code",
      "phone_number",
      "timezone",
      "is_geo_fencing_on",
      "is_mobile_device_enabled",
      "is_biometric_enabled",
      "is_web_enabled",
      "frequency",
      "snap_points_limit",
      "snap_duration_limit",
    ]) {
      if (patch[k] !== undefined) fields[k] = patch[k];
    }
    if (existing) {
      fields.updated_at = new Date();
      await db("employee_profiles").where({ id: existing.id }).update(fields);
      return db("employee_profiles").where({ id: existing.id }).first();
    }
    const id = uuidv4();
    await db("employee_profiles").insert({
      id,
      organization_id: orgId,
      user_id: userId,
      full_name: fields.full_name || "",
      email: fields.email || "",
      ...fields,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("employee_profiles").where({ id }).first();
  },
  async uploadImage(orgId: number, userId: number, url: string) {
    return profileService.update(orgId, userId, { profile_pic: url });
  },
  async updateSnap(orgId: number, userId: number, points: number, duration: number) {
    return profileService.update(orgId, userId, {
      snap_points_limit: points,
      snap_duration_limit: duration,
    });
  },
};

// ---------------------------------------------------------------------------
// CATEGORY
// ---------------------------------------------------------------------------
export const categoryService = {
  async create(orgId: number, name: string) {
    const db = getDB();
    const id = uuidv4();
    await db("field_categories").insert({
      id,
      organization_id: orgId,
      category_name: name,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("field_categories").where({ id }).first();
  },
  async list(orgId: number) {
    return getDB()("field_categories").where({ organization_id: orgId });
  },
  async update(orgId: number, id: string, name: string) {
    const db = getDB();
    await db("field_categories")
      .where({ id, organization_id: orgId })
      .update({ category_name: name, updated_at: new Date() });
    return db("field_categories").where({ id }).first();
  },
  async remove(orgId: number, id: string) {
    await getDB()("field_categories").where({ id, organization_id: orgId }).delete();
    return { ok: true };
  },
};

// ---------------------------------------------------------------------------
// TAGS
// ---------------------------------------------------------------------------
export const fieldTagService = {
  async create(orgId: number, userId: number, payload: any) {
    const db = getDB();
    const id = uuidv4();
    await db("field_tags").insert({
      id,
      organization_id: orgId,
      tag_name: payload.tag_name,
      tag_description: payload.tag_description || null,
      color: payload.color || null,
      created_by: userId,
      is_active: payload.is_active ?? true,
      order: payload.order ?? 1,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("field_tags").where({ id }).first();
  },
  async update(orgId: number, userId: number, id: string, patch: any) {
    const db = getDB();
    const allowed: any = { updated_at: new Date(), updated_by: userId };
    for (const k of ["tag_name", "tag_description", "color", "is_active", "order"]) {
      if (patch[k] !== undefined) allowed[k] = patch[k];
    }
    await db("field_tags").where({ id, organization_id: orgId }).update(allowed);
    return db("field_tags").where({ id }).first();
  },
  async updateOrder(orgId: number, items: { id: string; order: number }[]) {
    const db = getDB();
    for (const item of items) {
      await db("field_tags")
        .where({ id: item.id, organization_id: orgId })
        .update({ order: item.order, updated_at: new Date() });
    }
    return { updated: items.length };
  },
  async list(orgId: number) {
    return getDB()("field_tags").where({ organization_id: orgId, is_active: true }).orderBy("order");
  },
  async listAdmin(orgId: number) {
    return getDB()("field_tags").where({ organization_id: orgId }).orderBy("order");
  },
  async remove(orgId: number, id: string) {
    await getDB()("field_tags").where({ id, organization_id: orgId }).delete();
    return { ok: true };
  },
};

// ---------------------------------------------------------------------------
// TRANSPORT
// ---------------------------------------------------------------------------
export const transportService = {
  async updateMode(orgId: number, empId: string, mode: string) {
    const db = getDB();
    const existing = await db("transport_settings")
      .where({ organization_id: orgId, emp_id: empId })
      .first();
    if (existing) {
      await db("transport_settings").where({ id: existing.id }).update({
        current_mode: mode,
        updated_at: new Date(),
      });
      return db("transport_settings").where({ id: existing.id }).first();
    }
    const id = uuidv4();
    await db("transport_settings").insert({
      id,
      organization_id: orgId,
      emp_id: empId,
      current_mode: mode,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("transport_settings").where({ id }).first();
  },
  async updateFreqRadius(orgId: number, empId: string, frequency: number, radius: number) {
    const db = getDB();
    const existing = await db("transport_settings")
      .where({ organization_id: orgId, emp_id: empId })
      .first();
    if (existing) {
      await db("transport_settings").where({ id: existing.id }).update({
        current_frequency: frequency,
        current_radius: radius,
        updated_at: new Date(),
      });
      return db("transport_settings").where({ id: existing.id }).first();
    }
    const id = uuidv4();
    await db("transport_settings").insert({
      id,
      organization_id: orgId,
      emp_id: empId,
      current_frequency: frequency,
      current_radius: radius,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("transport_settings").where({ id }).first();
  },
};

// ---------------------------------------------------------------------------
// TRACKING (legacy daily summary)
// ---------------------------------------------------------------------------
export const trackingService = {
  async getLocation(orgId: number, empId: string, date?: string) {
    const dateKey = date || new Date().toISOString().slice(0, 10);
    return getDB()("tracking_logs")
      .where({ organization_id: orgId, emp_id: empId, date: dateKey })
      .first();
  },
};

// ---------------------------------------------------------------------------
// HRMS ADMIN — geo location settings
// ---------------------------------------------------------------------------
export const hrmsAdminService = {
  async updateLocation(orgId: number, payload: any) {
    const db = getDB();
    const id = uuidv4();
    await db("org_geo_locations").insert({
      id,
      organization_id: orgId,
      location_name: payload.location_name || null,
      address: payload.address || null,
      latitude: payload.latitude || null,
      longitude: payload.longitude || null,
      range: payload.range ?? 10,
      geo_fencing: payload.geo_fencing ?? false,
      is_mob_enabled: payload.is_mob_enabled ?? true,
      global: payload.global ? JSON.stringify(payload.global) : null,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("org_geo_locations").where({ id }).first();
  },
  async getLocationDetails(orgId: number) {
    return getDB()("org_geo_locations").where({ organization_id: orgId });
  },
  async getEmployeeConf(orgId: number, employeeId: number) {
    return getDB()("employee_geo_settings")
      .where({ organization_id: orgId, employee_id: employeeId })
      .first();
  },
  async updateEmployeeConf(orgId: number, employeeId: number, patch: any) {
    const db = getDB();
    const existing = await db("employee_geo_settings")
      .where({ organization_id: orgId, employee_id: employeeId })
      .first();
    const fields: any = {};
    for (const k of [
      "address",
      "latitude",
      "longitude",
      "range",
      "geo_fencing",
      "is_mob_enabled",
    ]) {
      if (patch[k] !== undefined) fields[k] = patch[k];
    }
    if (existing) {
      fields.updated_at = new Date();
      await db("employee_geo_settings").where({ id: existing.id }).update(fields);
      return db("employee_geo_settings").where({ id: existing.id }).first();
    }
    const id = uuidv4();
    await db("employee_geo_settings").insert({
      id,
      organization_id: orgId,
      employee_id: employeeId,
      ...fields,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("employee_geo_settings").where({ id }).first();
  },
  async orgLocationList(orgId: number) {
    return getDB()("org_geo_locations").where({ organization_id: orgId });
  },
  async updateEmployeeLocation(orgId: number, employeeId: number, patch: any) {
    return hrmsAdminService.updateEmployeeConf(orgId, employeeId, patch);
  },
  async getEmployeeLocation(orgId: number, employeeId: number) {
    return hrmsAdminService.getEmployeeConf(orgId, employeeId);
  },
};

// ---------------------------------------------------------------------------
// AUTO EMAIL REPORT
// ---------------------------------------------------------------------------
export const autoEmailReportService = {
  async create(orgId: number, payload: any) {
    const db = getDB();
    const id = uuidv4();
    await db("auto_email_reports").insert({
      id,
      organization_id: orgId,
      reports_title: payload.reports_title,
      frequency: payload.frequency ? JSON.stringify(payload.frequency) : null,
      recipients: payload.recipients ? JSON.stringify(payload.recipients) : null,
      content: payload.content ? JSON.stringify(payload.content) : null,
      reports_type: payload.reports_type ? JSON.stringify(payload.reports_type) : null,
      filter: payload.filter ? JSON.stringify(payload.filter) : null,
      send_test_mail: !!payload.send_test_mail,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("auto_email_reports").where({ id }).first();
  },
  async list(orgId: number) {
    return getDB()("auto_email_reports").where({ organization_id: orgId });
  },
  async update(orgId: number, id: string, patch: any) {
    const db = getDB();
    const fields: any = { updated_at: new Date() };
    for (const k of [
      "reports_title",
      "frequency",
      "recipients",
      "content",
      "reports_type",
      "filter",
      "send_test_mail",
    ]) {
      if (patch[k] !== undefined) {
        fields[k] = typeof patch[k] === "object" && patch[k] !== null ? JSON.stringify(patch[k]) : patch[k];
      }
    }
    await db("auto_email_reports").where({ id, organization_id: orgId }).update(fields);
    return db("auto_email_reports").where({ id }).first();
  },
  async remove(orgId: number, id: string) {
    await getDB()("auto_email_reports").where({ id, organization_id: orgId }).delete();
    return { ok: true };
  },
};

// ---------------------------------------------------------------------------
// LEGACY CLIENT (separate from client_sites)
// ---------------------------------------------------------------------------
export const legacyClientService = {
  async create(orgId: number, payload: any) {
    const db = getDB();
    const id = uuidv4();
    await db("legacy_clients").insert({
      id,
      organization_id: orgId,
      client_name: payload.client_name,
      emp_id: payload.emp_id || null,
      email_id: payload.email_id || null,
      contact_number: payload.contact_number || null,
      country_code: payload.country_code || null,
      address1: payload.address1,
      address2: payload.address2 || null,
      city: payload.city || null,
      state: payload.state || null,
      country: payload.country,
      zip_code: payload.zip_code || null,
      latitude: String(payload.latitude),
      longitude: String(payload.longitude),
      category: payload.category,
      created_by: payload.created_by || null,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("legacy_clients").where({ id }).first();
  },
  async list(orgId: number) {
    return getDB()("legacy_clients").where({ organization_id: orgId }).orderBy("created_at", "desc");
  },
  async update(orgId: number, id: string, patch: any) {
    const db = getDB();
    const fields: any = { updated_at: new Date() };
    for (const k of [
      "client_name",
      "email_id",
      "contact_number",
      "country_code",
      "address1",
      "address2",
      "city",
      "state",
      "country",
      "zip_code",
      "latitude",
      "longitude",
      "category",
      "client_type",
      "status",
    ]) {
      if (patch[k] !== undefined) fields[k] = patch[k];
    }
    await db("legacy_clients").where({ id, organization_id: orgId }).update(fields);
    return db("legacy_clients").where({ id }).first();
  },
  async remove(orgId: number, id: string) {
    await getDB()("legacy_clients").where({ id, organization_id: orgId }).delete();
    return { ok: true };
  },
  async uploadProfileImage(orgId: number, id: string, url: string) {
    const db = getDB();
    await db("legacy_clients")
      .where({ id, organization_id: orgId })
      .update({ client_profile_pic: url, updated_at: new Date() });
    return db("legacy_clients").where({ id }).first();
  },
};

// ---------------------------------------------------------------------------
// LEGACY USER (general-purpose user crud + biometric flags)
// ---------------------------------------------------------------------------
export const legacyUserService = {
  async create(orgId: number, payload: any) {
    return profileService.update(orgId, payload.user_id, payload);
  },
  async fetch(orgId: number) {
    return getDB()("employee_profiles").where({ organization_id: orgId });
  },
  async update(orgId: number, userId: number, patch: any) {
    return profileService.update(orgId, userId, patch);
  },
  async remove(orgId: number, userId: number) {
    await getDB()("employee_profiles").where({ organization_id: orgId, user_id: userId }).delete();
    return { ok: true };
  },
  async fetchEmpUsers(orgId: number) {
    return getDB()("employee_profiles").where({ organization_id: orgId, status: 1 });
  },
  async addEmpUsers(orgId: number, users: any[]) {
    const db = getDB();
    let added = 0;
    for (const u of users) {
      try {
        await db("employee_profiles").insert({
          id: uuidv4(),
          organization_id: orgId,
          user_id: u.user_id,
          full_name: u.full_name || "",
          email: u.email || "",
          emp_id: u.emp_id || null,
          status: 1,
          created_at: new Date(),
          updated_at: new Date(),
        });
        added++;
      } catch {
        // skip duplicates
      }
    }
    return { added };
  },
  async setFrequencyAndGeoLoc(orgId: number, userId: number, frequency: number, geoOn: number) {
    return profileService.update(orgId, userId, {
      frequency,
      is_geo_fencing_on: geoOn,
    });
  },
  async updateBiometricConfig(orgId: number, userId: number, enabled: number) {
    return profileService.update(orgId, userId, { is_biometric_enabled: enabled });
  },
};

// ---------------------------------------------------------------------------
// ATTENDANCE (legacy module)
// ---------------------------------------------------------------------------
export const attendanceService = {
  async fetch(orgId: number, filters: any) {
    let q = getDB()("attendance_records").where({ organization_id: orgId });
    if (filters?.user_id) q = q.andWhere("user_id", filters.user_id);
    if (filters?.start) q = q.andWhere("attendance_date", ">=", filters.start);
    if (filters?.end) q = q.andWhere("attendance_date", "<=", filters.end);
    return q.orderBy("attendance_date", "desc");
  },
  async mark(orgId: number, userId: number, payload: any) {
    const db = getDB();
    const dateKey = payload.attendance_date || new Date().toISOString().slice(0, 10);
    const existing = await db("attendance_records")
      .where({ organization_id: orgId, user_id: userId, attendance_date: dateKey })
      .first();
    if (existing) {
      await db("attendance_records").where({ id: existing.id }).update({
        check_out: payload.check_out || existing.check_out,
        check_out_lat: payload.check_out_lat || existing.check_out_lat,
        check_out_lng: payload.check_out_lng || existing.check_out_lng,
        notes: payload.notes || existing.notes,
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
      check_in: payload.check_in || new Date(),
      check_in_lat: payload.check_in_lat,
      check_in_lng: payload.check_in_lng,
      status: payload.status || "present",
      source: payload.source || "field",
      notes: payload.notes || null,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("attendance_records").where({ id }).first();
  },
  async getMine(orgId: number, userId: number) {
    return getDB()("attendance_records")
      .where({ organization_id: orgId, user_id: userId })
      .orderBy("attendance_date", "desc")
      .limit(60);
  },
  async createRequest(orgId: number, userId: number, payload: any) {
    const db = getDB();
    const id = uuidv4();
    await db("attendance_requests").insert({
      id,
      organization_id: orgId,
      user_id: userId,
      request_date: payload.request_date,
      type: payload.type,
      reason: payload.reason,
      status: "pending",
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("attendance_requests").where({ id }).first();
  },
};

// ---------------------------------------------------------------------------
// TASK (legacy task module — separate from work_orders)
// ---------------------------------------------------------------------------
export const taskService = {
  async create(orgId: number, payload: any) {
    const db = getDB();
    const id = uuidv4();
    await db("tasks").insert({
      id,
      organization_id: orgId,
      client_id: payload.client_id || null,
      task_name: payload.task_name,
      emp_id: String(payload.emp_id),
      date: payload.date || null,
      start_time: payload.start_time || null,
      end_time: payload.end_time || null,
      task_description: payload.task_description || null,
      created_by: payload.created_by || null,
      files: payload.files ? JSON.stringify(payload.files) : null,
      images: payload.images ? JSON.stringify(payload.images) : null,
      currency: payload.value?.currency || null,
      amount: payload.value?.amount || null,
      task_volume: payload.task_volume || null,
      recurrence_id: payload.recurrence_id || null,
      task_cycle: payload.recurrence_details?.TaskCycle ?? 0,
      recurrence_start: payload.recurrence_details?.startDate || null,
      recurrence_end: payload.recurrence_details?.endDate || null,
      recurrence_days: payload.recurrence_details?.daysOfWeek
        ? JSON.stringify(payload.recurrence_details.daysOfWeek)
        : null,
      created_at: new Date(),
      updated_at: new Date(),
    });
    return db("tasks").where({ id }).first();
  },
  async fetch(orgId: number, filters: any) {
    let q = getDB()("tasks").where({ organization_id: orgId });
    if (filters?.emp_id) q = q.andWhere("emp_id", filters.emp_id);
    if (filters?.client_id) q = q.andWhere("client_id", filters.client_id);
    if (filters?.date) q = q.andWhere("date", filters.date);
    return q.orderBy("created_at", "desc");
  },
  async update(orgId: number, id: string, patch: any) {
    const db = getDB();
    const fields: any = { updated_at: new Date() };
    for (const k of [
      "task_name",
      "task_description",
      "date",
      "start_time",
      "end_time",
      "task_approve_status",
      "client_id",
      "emp_id",
      "task_volume",
    ]) {
      if (patch[k] !== undefined) fields[k] = patch[k];
    }
    if (patch.files) fields.files = JSON.stringify(patch.files);
    if (patch.images) fields.images = JSON.stringify(patch.images);
    await db("tasks").where({ id, organization_id: orgId }).update(fields);
    return db("tasks").where({ id }).first();
  },
  async remove(orgId: number, id: string) {
    await getDB()("tasks").where({ id, organization_id: orgId }).delete();
    return { ok: true };
  },
  async approve(orgId: number, id: string, status: number) {
    const db = getDB();
    await db("tasks")
      .where({ id, organization_id: orgId })
      .update({ task_approve_status: status, updated_at: new Date() });
    return db("tasks").where({ id }).first();
  },
  async updateStatus(orgId: number, id: string, empStartTime?: string, empEndTime?: string) {
    const db = getDB();
    const fields: any = { updated_at: new Date() };
    if (empStartTime) fields.emp_start_time = empStartTime;
    if (empEndTime) fields.emp_end_time = empEndTime;
    await db("tasks").where({ id, organization_id: orgId }).update(fields);
    return db("tasks").where({ id }).first();
  },
  async uploadTaskFiles(orgId: number, id: string, files: any[], images: any[]) {
    const db = getDB();
    const task = await db("tasks").where({ id, organization_id: orgId }).first();
    if (!task) throw new NotFoundError("task", id);
    const existingFiles = task.files ? JSON.parse(task.files) : [];
    const existingImages = task.images ? JSON.parse(task.images) : [];
    await db("tasks").where({ id }).update({
      files: JSON.stringify([...existingFiles, ...(files || [])]),
      images: JSON.stringify([...existingImages, ...(images || [])]),
      updated_at: new Date(),
    });
    return db("tasks").where({ id }).first();
  },
  async deleteDoc(orgId: number, id: string, url: string) {
    const db = getDB();
    const task = await db("tasks").where({ id, organization_id: orgId }).first();
    if (!task) throw new NotFoundError("task", id);
    const files = (task.files ? JSON.parse(task.files) : []).filter((f: any) => f.url !== url);
    const images = (task.images ? JSON.parse(task.images) : []).filter((i: any) => i.url !== url);
    await db("tasks").where({ id }).update({
      files: JSON.stringify(files),
      images: JSON.stringify(images),
      updated_at: new Date(),
    });
    return db("tasks").where({ id }).first();
  },
  async filterTask(orgId: number, filters: any) {
    return taskService.fetch(orgId, filters);
  },
  async getNotification(orgId: number) {
    return getDB()("notifications")
      .where({ organization_id: orgId, status: "pending" })
      .orderBy("created_at", "desc")
      .limit(50);
  },
};

// ---------------------------------------------------------------------------
// REPORTS (consolidated reporting from reports.service.js, ~1,353 LOC)
// ---------------------------------------------------------------------------
export const reportsService = {
  async consolidated(orgId: number, filters: any) {
    const db = getDB();
    const start = filters?.start_date || new Date().toISOString().slice(0, 10);
    const end = filters?.end_date || start;
    const [tasks, attendance, leaves, mileage, expenses] = await Promise.all([
      db("tasks").where({ organization_id: orgId }).whereBetween("date", [start, end]),
      db("attendance_records").where({ organization_id: orgId }).whereBetween("attendance_date", [start, end]),
      db("leave_requests").where({ organization_id: orgId }).whereBetween("start_date", [start, end]),
      db("mileage_logs").where({ organization_id: orgId }),
      db("expenses").where({ organization_id: orgId }),
    ]);
    return { tasks, attendance, leaves, mileage, expenses };
  },
  async updateGeoFencing(orgId: number, employeeId: number, enabled: number) {
    return profileService.update(orgId, employeeId, { is_geo_fencing_on: enabled });
  },
  async getUserDetails(orgId: number, userId: number) {
    return getDB()("employee_profiles").where({ organization_id: orgId, user_id: userId }).first();
  },
  async taskListDetails(orgId: number, filters: any) {
    return taskService.fetch(orgId, filters);
  },
  async userStats(orgId: number, userId: number) {
    const db = getDB();
    const [tasks, checkins, distance] = await Promise.all([
      db("tasks").where({ organization_id: orgId, emp_id: String(userId) }).count<{ c: number }[]>("* as c").first(),
      db("checkins").where({ organization_id: orgId, user_id: userId }).count<{ c: number }[]>("* as c").first(),
      db("tracking_logs")
        .where({ organization_id: orgId, emp_id: String(userId) })
        .sum<{ d: number }[]>("dist_travelled as d")
        .first(),
    ]);
    return {
      totalTasks: Number(tasks?.c ?? 0),
      totalCheckins: Number(checkins?.c ?? 0),
      totalDistance: Number(distance?.d ?? 0),
    };
  },
  async taskStatus(orgId: number) {
    const db = getDB();
    return db("tasks")
      .where({ organization_id: orgId })
      .select("task_approve_status")
      .count<{ task_approve_status: number; count: number }[]>("* as count")
      .groupBy("task_approve_status");
  },
  async taskStages(orgId: number) {
    return reportsService.taskStatus(orgId);
  },
  async clientDetails(orgId: number) {
    return getDB()("legacy_clients").where({ organization_id: orgId });
  },
  async distanceTraveled(orgId: number, filters: any) {
    let q = getDB()("tracking_logs").where({ organization_id: orgId });
    if (filters?.emp_id) q = q.andWhere("emp_id", filters.emp_id);
    if (filters?.start_date && filters?.end_date) q = q.whereBetween("date", [filters.start_date, filters.end_date]);
    return q;
  },
  async getIndividualAttendance(orgId: number, userId: number, filters: any) {
    let q = getDB()("attendance_records").where({ organization_id: orgId, user_id: userId });
    if (filters?.start_date) q = q.andWhere("attendance_date", ">=", filters.start_date);
    if (filters?.end_date) q = q.andWhere("attendance_date", "<=", filters.end_date);
    return q.orderBy("attendance_date", "desc");
  },
};
