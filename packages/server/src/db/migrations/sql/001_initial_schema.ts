import { Knex } from "knex";

export async function up(knex: Knex): Promise<void> {
  // ------------------------------------------------------------------
  // field_settings
  // ------------------------------------------------------------------
  await knex.schema.createTable("field_settings", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.integer("check_in_radius_meters").notNullable().defaultTo(200);
    t.boolean("photo_required").notNullable().defaultTo(false);
    t.integer("auto_checkout_hours").notNullable().defaultTo(12);
    t.decimal("mileage_rate_per_km", 10, 2).notNullable().defaultTo(8.0);
    t.integer("tracking_interval_seconds").notNullable().defaultTo(30);
    t.timestamps(true, true);

    t.unique(["organization_id"]);
    t.index(["organization_id"]);
  });

  // ------------------------------------------------------------------
  // client_sites
  // ------------------------------------------------------------------
  await knex.schema.createTable("client_sites", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.string("name", 255).notNullable();
    t.string("address", 500).nullable();
    t.string("city", 100).nullable();
    t.string("state", 100).nullable();
    t.string("country", 100).nullable();
    t.decimal("latitude", 10, 7).notNullable();
    t.decimal("longitude", 10, 7).notNullable();
    t.integer("radius_meters").notNullable().defaultTo(200);
    t.string("contact_name", 255).nullable();
    t.string("contact_phone", 50).nullable();
    t.string("contact_email", 255).nullable();
    t.string("category", 100).nullable();
    t.boolean("is_active").notNullable().defaultTo(true);
    t.timestamps(true, true);

    t.index(["organization_id"]);
    t.index(["organization_id", "is_active"]);
    t.index(["organization_id", "category"]);
  });

  // ------------------------------------------------------------------
  // checkins
  // ------------------------------------------------------------------
  await knex.schema.createTable("checkins", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.uuid("client_site_id").nullable().references("id").inTable("client_sites").onDelete("SET NULL");
    t.datetime("check_in_time").notNullable();
    t.datetime("check_out_time").nullable();
    t.decimal("check_in_lat", 10, 7).notNullable();
    t.decimal("check_in_lng", 10, 7).notNullable();
    t.decimal("check_out_lat", 10, 7).nullable();
    t.decimal("check_out_lng", 10, 7).nullable();
    t.string("photo_url", 500).nullable();
    t.text("notes").nullable();
    t.enum("status", ["active", "completed", "auto_closed"]).notNullable().defaultTo("active");
    t.integer("duration_minutes").nullable();
    t.timestamps(true, true);

    t.index(["organization_id"]);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "user_id", "status"]);
    t.index(["organization_id", "check_in_time"]);
  });

  // ------------------------------------------------------------------
  // location_trails
  // ------------------------------------------------------------------
  await knex.schema.createTable("location_trails", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.decimal("latitude", 10, 7).notNullable();
    t.decimal("longitude", 10, 7).notNullable();
    t.decimal("accuracy", 8, 2).nullable();
    t.integer("battery_percent").nullable();
    t.datetime("timestamp").notNullable();
    t.timestamp("created_at").defaultTo(knex.fn.now());

    t.index(["organization_id"]);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "user_id", "timestamp"]);
  });

  // ------------------------------------------------------------------
  // geo_fences
  // ------------------------------------------------------------------
  await knex.schema.createTable("geo_fences", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.uuid("client_site_id").nullable().references("id").inTable("client_sites").onDelete("SET NULL");
    t.string("name", 255).notNullable();
    t.enum("type", ["circle", "polygon"]).notNullable().defaultTo("circle");
    t.decimal("center_lat", 10, 7).nullable();
    t.decimal("center_lng", 10, 7).nullable();
    t.integer("radius_meters").nullable();
    t.json("polygon_coords").nullable();
    t.boolean("is_active").notNullable().defaultTo(true);
    t.timestamps(true, true);

    t.index(["organization_id"]);
    t.index(["organization_id", "is_active"]);
  });

  // ------------------------------------------------------------------
  // geo_fence_events
  // ------------------------------------------------------------------
  await knex.schema.createTable("geo_fence_events", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.uuid("geo_fence_id").notNullable().references("id").inTable("geo_fences").onDelete("CASCADE");
    t.enum("event_type", ["entry", "exit"]).notNullable();
    t.decimal("latitude", 10, 7).notNullable();
    t.decimal("longitude", 10, 7).notNullable();
    t.datetime("timestamp").notNullable();
    t.timestamp("created_at").defaultTo(knex.fn.now());

    t.index(["organization_id"]);
    t.index(["organization_id", "user_id"]);
    t.index(["geo_fence_id"]);
  });

  // ------------------------------------------------------------------
  // work_orders
  // ------------------------------------------------------------------
  await knex.schema.createTable("work_orders", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("assigned_to").unsigned().notNullable();
    t.uuid("client_site_id").nullable().references("id").inTable("client_sites").onDelete("SET NULL");
    t.string("title", 255).notNullable();
    t.text("description").nullable();
    t.enum("priority", ["low", "medium", "high", "urgent"]).notNullable().defaultTo("medium");
    t.enum("status", ["pending", "in_progress", "completed", "cancelled"]).notNullable().defaultTo("pending");
    t.date("due_date").nullable();
    t.datetime("completed_at").nullable();
    t.bigInteger("created_by").unsigned().notNullable();
    t.timestamps(true, true);

    t.index(["organization_id"]);
    t.index(["organization_id", "assigned_to"]);
    t.index(["organization_id", "status"]);
    t.index(["organization_id", "priority"]);
  });

  // ------------------------------------------------------------------
  // expenses
  // ------------------------------------------------------------------
  await knex.schema.createTable("expenses", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.enum("expense_type", ["travel", "food", "lodging", "materials", "other"]).notNullable();
    t.decimal("amount", 12, 2).notNullable();
    t.string("currency", 3).notNullable().defaultTo("INR");
    t.text("description").nullable();
    t.string("receipt_url", 500).nullable();
    t.enum("status", ["draft", "submitted", "approved", "rejected"]).notNullable().defaultTo("draft");
    t.bigInteger("approved_by").unsigned().nullable();
    t.datetime("approved_at").nullable();
    t.timestamps(true, true);

    t.index(["organization_id"]);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "status"]);
  });

  // ------------------------------------------------------------------
  // mileage_logs
  // ------------------------------------------------------------------
  await knex.schema.createTable("mileage_logs", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.date("date").notNullable();
    t.decimal("distance_km", 10, 2).notNullable();
    t.decimal("start_lat", 10, 7).notNullable();
    t.decimal("start_lng", 10, 7).notNullable();
    t.decimal("end_lat", 10, 7).notNullable();
    t.decimal("end_lng", 10, 7).notNullable();
    t.decimal("reimbursement_amount", 12, 2).notNullable().defaultTo(0);
    t.timestamp("created_at").defaultTo(knex.fn.now());

    t.index(["organization_id"]);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "user_id", "date"]);
  });

  // ------------------------------------------------------------------
  // daily_routes
  // ------------------------------------------------------------------
  await knex.schema.createTable("daily_routes", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.date("date").notNullable();
    t.enum("status", ["planned", "in_progress", "completed"]).notNullable().defaultTo("planned");
    t.decimal("total_distance_km", 10, 2).notNullable().defaultTo(0);
    t.timestamps(true, true);

    t.index(["organization_id"]);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "user_id", "date"]);
  });

  // ------------------------------------------------------------------
  // route_stops
  // ------------------------------------------------------------------
  await knex.schema.createTable("route_stops", (t) => {
    t.uuid("id").primary();
    t.uuid("daily_route_id").notNullable().references("id").inTable("daily_routes").onDelete("CASCADE");
    t.uuid("client_site_id").nullable().references("id").inTable("client_sites").onDelete("SET NULL");
    t.integer("sequence_order").notNullable();
    t.datetime("planned_arrival").nullable();
    t.datetime("actual_arrival").nullable();
    t.enum("status", ["pending", "visited", "skipped"]).notNullable().defaultTo("pending");
    t.text("notes").nullable();
    t.timestamp("created_at").defaultTo(knex.fn.now());

    t.index(["daily_route_id"]);
    t.index(["daily_route_id", "sequence_order"]);
  });

  // ------------------------------------------------------------------
  // visit_logs
  // ------------------------------------------------------------------
  await knex.schema.createTable("visit_logs", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.uuid("client_site_id").nullable().references("id").inTable("client_sites").onDelete("SET NULL");
    t.uuid("checkin_id").nullable().references("id").inTable("checkins").onDelete("SET NULL");
    t.string("purpose", 500).nullable();
    t.string("outcome", 500).nullable();
    t.text("notes").nullable();
    t.json("photos").nullable();
    t.integer("duration_minutes").nullable();
    t.timestamp("created_at").defaultTo(knex.fn.now());

    t.index(["organization_id"]);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "client_site_id"]);
  });

  // ------------------------------------------------------------------
  // notifications
  // ------------------------------------------------------------------
  await knex.schema.createTable("notifications", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.string("title", 255).notNullable();
    t.text("body").nullable();
    t.string("type", 50).nullable();
    t.string("reference_type", 50).nullable();
    t.string("reference_id", 100).nullable();
    t.boolean("is_read").notNullable().defaultTo(false);
    t.timestamp("created_at").defaultTo(knex.fn.now());
    t.datetime("read_at").nullable();

    t.index(["organization_id"]);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "user_id", "is_read"]);
  });
}

export async function down(knex: Knex): Promise<void> {
  const tables = [
    "notifications",
    "visit_logs",
    "route_stops",
    "daily_routes",
    "mileage_logs",
    "expenses",
    "work_orders",
    "geo_fence_events",
    "geo_fences",
    "location_trails",
    "checkins",
    "client_sites",
    "field_settings",
  ];

  for (const table of tables) {
    await knex.schema.dropTableIfExists(table);
  }
}
