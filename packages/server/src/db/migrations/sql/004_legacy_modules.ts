// ============================================================================
// LEGACY MODULES MIGRATION
// ============================================================================
//
// Brings every table from the original JS+MongoDB version (commit 3409d88)
// into the TS+Knex/MySQL stack. One big migration so that a fresh `db:migrate`
// gives the full feature set.
// ============================================================================

import type { Knex } from "knex";

export async function up(knex: Knex): Promise<void> {
  // -------------------------------------------------------------------------
  // admin_users — adminSchema (HRMS admin profile, separate from EmpCloud users)
  // -------------------------------------------------------------------------
  await knex.schema.createTable("admin_users", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("full_name", 255).notNullable();
    t.integer("age").nullable();
    t.string("gender", 32).notNullable();
    t.string("email", 255).notNullable();
    t.string("password", 255).notNullable();
    t.string("profile_pic", 500).nullable();
    t.string("role", 64).nullable();
    t.string("emp_id", 64).nullable();
    t.string("address1", 500).nullable();
    t.string("address2", 500).nullable();
    t.string("latitude", 32).nullable();
    t.string("longitude", 32).nullable();
    t.string("city", 100).notNullable();
    t.string("state", 100).notNullable();
    t.string("country", 100).notNullable();
    t.string("zip_code", 16).nullable();
    t.string("phone_number", 32).notNullable();
    t.string("timezone", 64).notNullable();
    t.boolean("is_suspended").notNullable().defaultTo(false);
    t.integer("org_radius").notNullable().defaultTo(10);
    t.integer("snap_points_limit").notNullable().defaultTo(100);
    t.integer("snap_duration_limit").notNullable().defaultTo(60);
    t.string("forgot_password_token", 16).nullable();
    t.timestamp("forgot_token_expire").nullable();
    t.integer("password_email_sent_count").notNullable().defaultTo(0);
    t.boolean("two_factor_enabled").notNullable().defaultTo(false);
    t.timestamps(true, true);
    t.unique(["organization_id", "email"]);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // employee_profiles — extended user profile (profile.model.js)
  // -------------------------------------------------------------------------
  await knex.schema.createTable("employee_profiles", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.string("full_name", 255).notNullable();
    t.integer("age").nullable();
    t.string("gender", 32).nullable();
    t.string("email", 255).notNullable();
    t.string("profile_pic", 500).nullable();
    t.string("location", 255).nullable();
    t.string("department", 255).nullable();
    t.integer("status").notNullable().defaultTo(1); // 1=active, 2=suspended, 4=deleted
    t.string("role", 64).nullable();
    t.string("emp_id", 64).nullable();
    t.string("address1", 500).nullable();
    t.string("address2", 500).nullable();
    t.string("latitude", 32).nullable();
    t.string("longitude", 32).nullable();
    t.string("city", 100).nullable();
    t.string("state", 100).nullable();
    t.string("country", 100).nullable();
    t.string("zip_code", 16).nullable();
    t.string("phone_number", 32).nullable();
    t.string("timezone", 64).nullable();
    t.boolean("is_suspended").notNullable().defaultTo(false);
    t.integer("is_geo_fencing_on").notNullable().defaultTo(0);
    t.integer("is_mobile_device_enabled").notNullable().defaultTo(1);
    t.integer("is_biometric_enabled").notNullable().defaultTo(0);
    t.integer("is_web_enabled").notNullable().defaultTo(0);
    t.integer("frequency").notNullable().defaultTo(20);
    t.boolean("geo_logs_status").notNullable().defaultTo(true);
    t.integer("snap_points_limit").notNullable().defaultTo(100);
    t.integer("snap_duration_limit").notNullable().defaultTo(60);
    t.boolean("is_biometric_user").notNullable().defaultTo(false);
    t.timestamps(true, true);
    t.unique(["organization_id", "user_id"]);
    t.index(["organization_id"]);
    t.index(["organization_id", "emp_id"]);
  });

  // -------------------------------------------------------------------------
  // field_roles — role.model.js (lightweight, distinct from EmpCloud RBAC)
  // -------------------------------------------------------------------------
  await knex.schema.createTable("field_roles", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("role", 100).notNullable();
    t.timestamps(true, true);
    t.unique(["organization_id", "role"]);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // leave_types — leave.model.js (defines available leave types per org)
  // -------------------------------------------------------------------------
  await knex.schema.createTable("leave_types", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("name", 100).notNullable();
    t.integer("duration").notNullable(); // 1=yearly, 2=half, 3=quarterly, 4=monthly
    t.integer("no_of_days").notNullable();
    t.integer("carry_forward").notNullable().defaultTo(0);
    t.timestamps(true, true);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // leave_requests — actual leave applications
  // -------------------------------------------------------------------------
  await knex.schema.createTable("leave_requests", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.uuid("leave_type_id").nullable();
    t.date("start_date").notNullable();
    t.date("end_date").notNullable();
    t.integer("days").notNullable();
    t.string("reason", 1000).nullable();
    t.enum("status", ["pending", "approved", "rejected", "cancelled"])
      .notNullable()
      .defaultTo("pending");
    t.bigInteger("approved_by").unsigned().nullable();
    t.timestamp("approved_at").nullable();
    t.timestamps(true, true);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "status"]);
  });

  // -------------------------------------------------------------------------
  // holidays — holiday.model.js
  // -------------------------------------------------------------------------
  await knex.schema.createTable("holidays", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("name", 255).notNullable();
    t.date("date").notNullable();
    t.timestamps(true, true);
    t.index(["organization_id", "date"]);
  });

  // -------------------------------------------------------------------------
  // field_categories — category.model.js
  // -------------------------------------------------------------------------
  await knex.schema.createTable("field_categories", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("category_name", 255).notNullable();
    t.timestamps(true, true);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // field_tags — tag.model.js (kept distinct from any later "tags" table)
  // -------------------------------------------------------------------------
  await knex.schema.createTable("field_tags", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("tag_name", 100).notNullable();
    t.string("tag_description", 500).nullable();
    t.string("color", 32).nullable();
    t.bigInteger("created_by").unsigned().notNullable();
    t.bigInteger("updated_by").unsigned().nullable();
    t.boolean("is_active").notNullable().defaultTo(true);
    t.integer("order").notNullable().defaultTo(1);
    t.timestamps(true, true);
    t.unique(["organization_id", "tag_name"]);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // tasks — task.model.js (separate from work_orders, has recurrence + tagLogs)
  // -------------------------------------------------------------------------
  await knex.schema.createTable("tasks", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("client_id", 64).nullable();
    t.string("task_name", 255).notNullable();
    t.string("emp_id", 64).notNullable();
    t.string("date", 32).nullable();
    t.string("start_time", 32).nullable();
    t.string("end_time", 32).nullable();
    t.text("task_description").nullable();
    t.integer("task_approve_status").notNullable().defaultTo(0);
    t.string("emp_start_time", 32).nullable();
    t.string("emp_end_time", 32).nullable();
    t.json("tag_logs").nullable();
    t.string("created_by", 64).nullable();
    t.string("updated_by", 64).nullable();
    t.json("files").nullable();
    t.json("images").nullable();
    t.string("currency", 8).nullable();
    t.decimal("amount", 14, 2).nullable();
    t.decimal("converted_amount_usd", 14, 2).nullable();
    t.integer("task_volume").nullable();
    t.string("recurrence_id", 64).nullable();
    t.integer("task_cycle").notNullable().defaultTo(0); // 0=none, 1=repeating, 2=daily
    t.string("recurrence_start", 32).nullable();
    t.string("recurrence_end", 32).nullable();
    t.json("recurrence_days").nullable();
    t.timestamps(true, true);
    t.index(["organization_id"]);
    t.index(["organization_id", "task_name"]);
    t.index(["organization_id", "recurrence_id"]);
  });

  // -------------------------------------------------------------------------
  // transport_settings — transport.model.js
  // -------------------------------------------------------------------------
  await knex.schema.createTable("transport_settings", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("emp_id", 64).notNullable();
    t.integer("current_radius").notNullable().defaultTo(10);
    t.integer("current_frequency").notNullable().defaultTo(25);
    t.string("current_mode", 32).notNullable().defaultTo("bike");
    t.json("default_config").nullable();
    t.timestamps(true, true);
    t.unique(["organization_id", "emp_id"]);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // tracking_logs — track.model.js (daily geologs summary)
  // -------------------------------------------------------------------------
  await knex.schema.createTable("tracking_logs", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("emp_id", 64).notNullable();
    t.decimal("dist_travelled", 12, 2).notNullable().defaultTo(0);
    t.string("date", 16).notNullable();
    t.string("check_in", 32).nullable();
    t.string("check_out", 32).nullable();
    t.integer("current_frequency").notNullable().defaultTo(25);
    t.string("current_mode", 32).notNullable().defaultTo("bike");
    t.json("geologs").nullable();
    t.string("created_by", 64).nullable();
    t.string("updated_by", 64).nullable();
    t.timestamps(true, true);
    t.unique(["organization_id", "emp_id", "date"]);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // user_tracking_events — userTracking.model.js (login/logout/inTask)
  // -------------------------------------------------------------------------
  await knex.schema.createTable("user_tracking_events", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.string("tracking_type", 32).notNullable(); // login, logOut, InTask
    t.string("task_id", 64).nullable();
    t.timestamp("date").notNullable().defaultTo(knex.fn.now());
    t.string("latitude", 32).notNullable();
    t.string("longitude", 32).notNullable();
    t.timestamps(true, true);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "tracking_type"]);
  });

  // -------------------------------------------------------------------------
  // employee_geo_settings — employeeLocation.model.js
  // -------------------------------------------------------------------------
  await knex.schema.createTable("employee_geo_settings", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("employee_id").unsigned().notNullable();
    t.string("address", 500).nullable();
    t.decimal("latitude", 10, 7).nullable();
    t.decimal("longitude", 10, 7).nullable();
    t.integer("range").notNullable().defaultTo(10);
    t.boolean("geo_fencing").notNullable().defaultTo(false);
    t.boolean("is_mob_enabled").notNullable().defaultTo(true);
    t.string("created_by", 64).nullable();
    t.string("updated_by", 64).nullable();
    t.timestamps(true, true);
    t.unique(["organization_id", "employee_id"]);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // org_geo_locations — orgLocation.model.js
  // -------------------------------------------------------------------------
  await knex.schema.createTable("org_geo_locations", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("location_name", 255).nullable();
    t.string("address", 500).nullable();
    t.string("latitude", 32).nullable();
    t.string("longitude", 32).nullable();
    t.integer("range").notNullable().defaultTo(10);
    t.boolean("geo_fencing").notNullable().defaultTo(false);
    t.boolean("is_mob_enabled").notNullable().defaultTo(true);
    t.json("global").nullable();
    t.string("created_by", 64).nullable();
    t.string("updated_by", 64).nullable();
    t.timestamps(true, true);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // auto_email_reports — autoEmailReport.model.js
  // -------------------------------------------------------------------------
  await knex.schema.createTable("auto_email_reports", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("reports_title", 255).notNullable();
    t.json("frequency").nullable();
    t.json("recipients").nullable();
    t.json("content").nullable();
    t.json("reports_type").nullable();
    t.json("filter").nullable();
    t.boolean("send_test_mail").notNullable().defaultTo(false);
    t.timestamps(true, true);
    t.index(["organization_id"]);
  });

  // -------------------------------------------------------------------------
  // attendance_records — attendance module data
  // -------------------------------------------------------------------------
  await knex.schema.createTable("attendance_records", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.date("attendance_date").notNullable();
    t.timestamp("check_in").nullable();
    t.timestamp("check_out").nullable();
    t.decimal("check_in_lat", 10, 7).nullable();
    t.decimal("check_in_lng", 10, 7).nullable();
    t.decimal("check_out_lat", 10, 7).nullable();
    t.decimal("check_out_lng", 10, 7).nullable();
    t.enum("status", ["present", "absent", "half_day", "leave", "holiday"])
      .notNullable()
      .defaultTo("present");
    t.string("source", 32).notNullable().defaultTo("field");
    t.string("notes", 500).nullable();
    t.timestamps(true, true);
    t.unique(["organization_id", "user_id", "attendance_date"]);
    t.index(["organization_id", "attendance_date"]);
  });

  // -------------------------------------------------------------------------
  // attendance_requests — manual attendance correction requests
  // -------------------------------------------------------------------------
  await knex.schema.createTable("attendance_requests", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.date("request_date").notNullable();
    t.string("type", 32).notNullable(); // missed_checkin, wrong_time, etc.
    t.string("reason", 1000).notNullable();
    t.enum("status", ["pending", "approved", "rejected"])
      .notNullable()
      .defaultTo("pending");
    t.bigInteger("approved_by").unsigned().nullable();
    t.timestamp("approved_at").nullable();
    t.timestamps(true, true);
    t.index(["organization_id", "user_id"]);
  });

  // -------------------------------------------------------------------------
  // legacy_clients — old client.model.js (kept alongside client_sites)
  // -------------------------------------------------------------------------
  await knex.schema.createTable("legacy_clients", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("client_name", 255).notNullable();
    t.string("emp_id", 64).nullable();
    t.string("email_id", 255).nullable();
    t.string("contact_number", 32).nullable();
    t.string("client_profile_pic", 500).nullable();
    t.string("country_code", 8).nullable();
    t.string("address1", 500).notNullable();
    t.string("address2", 500).nullable();
    t.string("city", 100).nullable();
    t.string("state", 100).nullable();
    t.string("country", 100).notNullable();
    t.string("zip_code", 16).nullable();
    t.string("latitude", 32).notNullable();
    t.string("longitude", 32).notNullable();
    t.integer("client_type").notNullable().defaultTo(0); // 0=Active, 1=Suspended, 2=Expired
    t.string("category", 100).notNullable();
    t.integer("status").notNullable().defaultTo(0);
    t.bigInteger("assigned_employees").unsigned().nullable();
    t.string("created_by", 64).nullable();
    t.string("updated_by", 64).nullable();
    t.timestamps(true, true);
    t.index(["organization_id"]);
  });
}

export async function down(knex: Knex): Promise<void> {
  await knex.schema.dropTableIfExists("legacy_clients");
  await knex.schema.dropTableIfExists("attendance_requests");
  await knex.schema.dropTableIfExists("attendance_records");
  await knex.schema.dropTableIfExists("auto_email_reports");
  await knex.schema.dropTableIfExists("org_geo_locations");
  await knex.schema.dropTableIfExists("employee_geo_settings");
  await knex.schema.dropTableIfExists("user_tracking_events");
  await knex.schema.dropTableIfExists("tracking_logs");
  await knex.schema.dropTableIfExists("transport_settings");
  await knex.schema.dropTableIfExists("tasks");
  await knex.schema.dropTableIfExists("field_tags");
  await knex.schema.dropTableIfExists("field_categories");
  await knex.schema.dropTableIfExists("holidays");
  await knex.schema.dropTableIfExists("leave_requests");
  await knex.schema.dropTableIfExists("leave_types");
  await knex.schema.dropTableIfExists("field_roles");
  await knex.schema.dropTableIfExists("employee_profiles");
  await knex.schema.dropTableIfExists("admin_users");
}
