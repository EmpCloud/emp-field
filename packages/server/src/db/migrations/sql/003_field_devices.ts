import type { Knex } from "knex";

export async function up(knex: Knex): Promise<void> {
  await knex.schema.createTable("field_devices", (t) => {
    t.uuid("id").primary();
    t.bigInteger("organization_id").unsigned().notNullable();
    t.bigInteger("user_id").unsigned().notNullable();
    t.enum("platform", ["ios", "android", "web"]).notNullable();
    t.string("push_token", 512).notNullable();
    t.string("device_id", 128).nullable();
    t.string("device_model", 128).nullable();
    t.string("app_version", 32).nullable();
    t.string("os_version", 32).nullable();
    t.boolean("is_active").notNullable().defaultTo(true);
    t.timestamp("last_seen_at").nullable();
    t.timestamps(true, true);

    t.unique(["organization_id", "user_id", "push_token"]);
    t.index(["organization_id", "user_id"]);
    t.index(["organization_id", "is_active"]);
  });
}

export async function down(knex: Knex): Promise<void> {
  await knex.schema.dropTableIfExists("field_devices");
}
