import type { Knex } from "knex";

export async function up(knex: Knex): Promise<void> {
  await knex.schema.createTable("audit_logs", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().nullable();
    t.string("action", 50).notNullable(); // create, update, delete
    t.string("resource", 100).notNullable(); // e.g. "work_order", "checkin"
    t.string("resource_id", 64).nullable();
    t.string("method", 10).notNullable();
    t.string("path", 500).notNullable();
    t.integer("status_code").notNullable();
    t.string("ip_address", 45).nullable();
    t.string("user_agent", 500).nullable();
    t.json("request_body").nullable();
    t.json("response_meta").nullable();
    t.timestamp("created_at").notNullable().defaultTo(knex.fn.now());

    t.index(["organization_id", "created_at"]);
    t.index(["organization_id", "resource", "resource_id"]);
    t.index(["organization_id", "user_id", "created_at"]);
  });
}

export async function down(knex: Knex): Promise<void> {
  await knex.schema.dropTableIfExists("audit_logs");
}
