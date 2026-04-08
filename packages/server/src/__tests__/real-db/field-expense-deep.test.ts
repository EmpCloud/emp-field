// =============================================================================
// FIELD EXPENSE DEEP — submit/approve/reject, categories, reimbursement
// =============================================================================

import { describe, it, expect, beforeAll, afterAll, afterEach } from "vitest";
import knexLib, { Knex } from "knex";
import { v4 as uuidv4 } from "uuid";

let db: Knex;
const ORG_ID = 5;
const USER_ID = 522;
const TS = Date.now();
const cleanup: { table: string; id: string }[] = [];

beforeAll(async () => {
  db = knexLib({
    client: "mysql2",
    connection: { host: "localhost", port: 3306, user: "empcloud", password: process.env.DB_PASSWORD || "", database: "emp_field" },
  });
  await db.raw("SELECT 1");
});

afterEach(async () => {
  for (const item of cleanup.reverse()) {
    try { await db(item.table).where({ id: item.id }).del(); } catch {}
  }
  cleanup.length = 0;
});

afterAll(async () => { if (db) await db.destroy(); });

// ==========================================================================
// EXPENSE CRUD
// ==========================================================================
describe("Expense CRUD", () => {
  it("should create a draft expense", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      expense_type: "travel", amount: 1500.50, currency: "INR",
      description: "Cab to client site", status: "draft",
    });
    cleanup.push({ table: "expenses", id });

    const row = await db("expenses").where({ id }).first();
    expect(row.status).toBe("draft");
    expect(Number(row.amount)).toBeCloseTo(1500.50);
    expect(row.expense_type).toBe("travel");
  });

  it("should create expenses for all categories", async () => {
    const types = ["travel", "food", "lodging", "materials", "other"] as const;
    for (const etype of types) {
      const id = uuidv4();
      await db("expenses").insert({
        id, organization_id: ORG_ID, user_id: USER_ID,
        expense_type: etype, amount: 1000, currency: "INR",
        description: `Test ${etype}`, status: "draft",
      });
      cleanup.push({ table: "expenses", id });

      const row = await db("expenses").where({ id }).first();
      expect(row.expense_type).toBe(etype);
    }
  });

  it("should attach receipt URL", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      expense_type: "food", amount: 500, currency: "INR",
      receipt_url: "/receipts/food-001.jpg", status: "draft",
    });
    cleanup.push({ table: "expenses", id });

    const row = await db("expenses").where({ id }).first();
    expect(row.receipt_url).toBe("/receipts/food-001.jpg");
  });
});

// ==========================================================================
// EXPENSE WORKFLOW
// ==========================================================================
describe("Expense Approval Workflow", () => {
  it("should submit a draft expense", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      expense_type: "travel", amount: 2000, currency: "INR",
      description: "Site visit travel", status: "draft",
    });
    cleanup.push({ table: "expenses", id });

    await db("expenses").where({ id }).update({ status: "submitted" });
    const row = await db("expenses").where({ id }).first();
    expect(row.status).toBe("submitted");
  });

  it("should approve a submitted expense", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      expense_type: "travel", amount: 3000, currency: "INR",
      status: "submitted",
    });
    cleanup.push({ table: "expenses", id });

    await db("expenses").where({ id }).update({
      status: "approved", approved_by: USER_ID, approved_at: new Date(),
    });

    const row = await db("expenses").where({ id }).first();
    expect(row.status).toBe("approved");
    expect(row.approved_by).toBe(USER_ID);
    expect(row.approved_at).toBeTruthy();
  });

  it("should reject a submitted expense", async () => {
    const id = uuidv4();
    await db("expenses").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      expense_type: "lodging", amount: 5000, currency: "INR",
      description: "Hotel stay without prior approval", status: "submitted",
    });
    cleanup.push({ table: "expenses", id });

    await db("expenses").where({ id }).update({ status: "rejected" });
    const row = await db("expenses").where({ id }).first();
    expect(row.status).toBe("rejected");
  });

  it("should list expenses by status", async () => {
    const ids: string[] = [];
    for (const status of ["draft", "submitted", "approved"] as const) {
      const id = uuidv4();
      await db("expenses").insert({
        id, organization_id: ORG_ID, user_id: USER_ID,
        expense_type: "other", amount: 100, currency: "INR", status,
      });
      ids.push(id);
      cleanup.push({ table: "expenses", id });
    }

    const submitted = await db("expenses")
      .where({ organization_id: ORG_ID, status: "submitted" })
      .whereIn("id", ids);
    expect(submitted.length).toBe(1);
  });

  it("should compute total reimbursement for approved expenses", async () => {
    const ids: string[] = [];
    for (let i = 0; i < 3; i++) {
      const id = uuidv4();
      await db("expenses").insert({
        id, organization_id: ORG_ID, user_id: USER_ID,
        expense_type: "travel", amount: 1000 * (i + 1), currency: "INR",
        status: "approved",
      });
      ids.push(id);
      cleanup.push({ table: "expenses", id });
    }

    const result = await db("expenses")
      .where({ organization_id: ORG_ID, user_id: USER_ID, status: "approved" })
      .whereIn("id", ids)
      .sum("amount as total")
      .first();
    expect(Number(result?.total)).toBe(6000); // 1000+2000+3000
  });
});

// ==========================================================================
// MILEAGE LOGS
// ==========================================================================
describe("Mileage Logs", () => {
  it("should create a mileage log with reimbursement", async () => {
    const id = uuidv4();
    const distKm = 45.5;
    const ratePerKm = 8.0;
    const reimbursement = distKm * ratePerKm;

    await db("mileage_logs").insert({
      id, organization_id: ORG_ID, user_id: USER_ID,
      date: "2026-04-04", distance_km: distKm,
      start_lat: 12.9716, start_lng: 77.5946,
      end_lat: 13.0827, end_lng: 77.5877,
      reimbursement_amount: reimbursement,
    });
    cleanup.push({ table: "mileage_logs", id });

    const row = await db("mileage_logs").where({ id }).first();
    expect(Number(row.distance_km)).toBeCloseTo(45.5);
    expect(Number(row.reimbursement_amount)).toBeCloseTo(364.0);
  });

  it("should compute total mileage for a user in a date range", async () => {
    const ids: string[] = [];
    for (let i = 1; i <= 3; i++) {
      const id = uuidv4();
      await db("mileage_logs").insert({
        id, organization_id: ORG_ID, user_id: USER_ID,
        date: `2026-04-0${i}`, distance_km: 10 * i,
        start_lat: 12.9716, start_lng: 77.5946,
        end_lat: 12.9800, end_lng: 77.6000,
        reimbursement_amount: 10 * i * 8,
      });
      ids.push(id);
      cleanup.push({ table: "mileage_logs", id });
    }

    const result = await db("mileage_logs")
      .where({ organization_id: ORG_ID, user_id: USER_ID })
      .whereIn("id", ids)
      .sum("distance_km as total_km")
      .sum("reimbursement_amount as total_reimb")
      .first();
    expect(Number(result?.total_km)).toBe(60); // 10+20+30
    expect(Number(result?.total_reimb)).toBe(480); // 80+160+240
  });
});
