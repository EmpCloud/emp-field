import { test, expect } from "@playwright/test";

const API = process.env.BASE_API_URL || "http://localhost:6013";

test.describe("emp-field API health", () => {
  test("GET /health returns ok", async ({ request }) => {
    const res = await request.get(`${API}/health`);
    expect(res.status()).toBe(200);
    const body = await res.json();
    expect(body).toHaveProperty("status");
  });

  test("GET /api/v1/checkins without token is 401", async ({ request }) => {
    const res = await request.get(`${API}/api/v1/checkins`);
    expect(res.status()).toBe(401);
  });
});
