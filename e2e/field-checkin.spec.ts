import { test, expect } from "@playwright/test";

// Smoke test for the mobile-first field check-in page.
// Skipped in CI unless FIELD_SMOKE_TOKEN is provided so tests are hermetic.
test.describe("Field check-in UI", () => {
  test.skip(!process.env.FIELD_SMOKE_TOKEN, "set FIELD_SMOKE_TOKEN to enable");

  test.use({
    geolocation: { latitude: 12.9716, longitude: 77.5946 },
    permissions: ["geolocation"],
  });

  test("renders GPS panel and action buttons", async ({ page }) => {
    await page.addInitScript((token) => {
      localStorage.setItem("access_token", token as string);
      localStorage.setItem(
        "user",
        JSON.stringify({
          id: 1,
          empcloudUserId: 1,
          empcloudOrgId: 1,
          orgId: 1,
          role: "employee",
          email: "field@example.com",
          firstName: "Field",
          lastName: "Worker",
          orgName: "Acme",
        }),
      );
    }, process.env.FIELD_SMOKE_TOKEN);

    await page.goto("/field/checkin");
    await expect(page.getByText("GPS position")).toBeVisible();
    await expect(page.getByRole("button", { name: /check in/i })).toBeVisible();
  });
});
