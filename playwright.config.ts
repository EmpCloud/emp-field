import { defineConfig, devices } from "@playwright/test";

/**
 * Playwright config for emp-field E2E tests.
 *
 * Targets:
 *   - API: http://localhost:6013 (set BASE_API_URL to override)
 *   - Web: http://localhost:5180 (set BASE_WEB_URL to override)
 */
export default defineConfig({
  testDir: "./e2e",
  timeout: 30_000,
  expect: { timeout: 5_000 },
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  reporter: [["list"], ["html", { open: "never" }]],
  use: {
    baseURL: process.env.BASE_WEB_URL || "http://localhost:5180",
    trace: "on-first-retry",
    screenshot: "only-on-failure",
    video: "retain-on-failure",
  },
  projects: [
    {
      name: "desktop",
      use: { ...devices["Desktop Chrome"] },
    },
    {
      name: "mobile",
      use: { ...devices["Pixel 7"] },
    },
  ],
});
