import { Suspense, useEffect, useState } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { Loader2 } from "lucide-react";
import { isLoggedIn, useAuthStore, extractSSOToken } from "@/lib/auth-store";
import { apiPost } from "@/api/client";
import { DashboardLayout } from "@/components/layout/DashboardLayout";
import DashboardPage from "@/pages/DashboardPage";
import ClientSitesPage from "@/pages/ClientSitesPage";
import FieldCheckInPage from "@/pages/FieldCheckInPage";
import LiveTrackingPage from "@/pages/LiveTrackingPage";
import VisitsPage from "@/pages/VisitsPage";
import RoutesPage from "@/pages/RoutesPage";
import FieldAgentsPage from "@/pages/FieldAgentsPage";
import SettingsPage from "@/pages/SettingsPage";
import LoginPage from "@/pages/LoginPage";
import AttendancePage from "@/pages/legacy/AttendancePage";
import HolidayPage from "@/pages/legacy/HolidayPage";
import LeavePage from "@/pages/legacy/LeavePage";
import {
  RolesPage,
  CategoriesPage,
  TagsPage,
  ProfilePage,
  GeoLocationPage,
  ReportsPage,
  LegacyClientsPage,
  EmployeesPage,
  LegacyTasksPage,
  TimelinePage,
} from "@/pages/legacy/SimplePages";

function PageLoader() {
  return (
    <div className="flex h-64 items-center justify-center">
      <Loader2 className="h-8 w-8 animate-spin text-brand-600" />
    </div>
  );
}

function AuthRedirect() {
  return isLoggedIn() ? <Navigate to="/dashboard" replace /> : <Navigate to="/login" replace />;
}

function SSOGate({ children }: { children: React.ReactNode }) {
  const login = useAuthStore((s) => s.login);
  const [ssoToken] = useState(() => extractSSOToken());
  const [ready, setReady] = useState(!ssoToken);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!ssoToken) return;

    let cancelled = false;

    (async () => {
      try {
        const res = await apiPost<{
          user: any;
          tokens: { accessToken: string; refreshToken: string };
        }>("/auth/sso", { token: ssoToken });

        if (cancelled) return;

        const { user, tokens } = res.data!;
        login(user, tokens);

        // Redirect to dashboard after SSO login
        if (window.location.pathname === "/" || window.location.pathname === "/login") {
          window.location.replace("/dashboard");
          return;
        }
        setReady(true);
      } catch (err: any) {
        if (cancelled) return;
        console.error("SSO exchange failed:", err);
        setError("SSO login failed. Please try logging in manually.");
        setReady(true);
      }
    })();

    return () => {
      cancelled = true;
    };
  }, [ssoToken, login]);

  if (!ready) return <PageLoader />;
  if (error) {
    return (
      <div className="flex h-screen items-center justify-center">
        <div className="text-center">
          <p className="text-red-600 mb-4">{error}</p>
          <a href="/login" className="text-brand-600 underline">
            Go to login
          </a>
        </div>
      </div>
    );
  }
  return <>{children}</>;
}

export default function App() {
  return (
    <SSOGate>
      <Suspense fallback={<PageLoader />}>
        <Routes>
          <Route path="/" element={<AuthRedirect />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/field/checkin" element={<FieldCheckInPage />} />
          <Route element={<DashboardLayout />}>
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/client-sites" element={<ClientSitesPage />} />
            <Route path="/field-agents" element={<FieldAgentsPage />} />
            <Route path="/visits" element={<VisitsPage />} />
            <Route path="/routes" element={<RoutesPage />} />
            <Route path="/live-tracking" element={<LiveTrackingPage />} />
            <Route path="/tracking" element={<LiveTrackingPage />} />
            <Route path="/settings" element={<SettingsPage />} />
            {/* Legacy modules ported from commit 3409d88 */}
            <Route path="/attendance" element={<AttendancePage />} />
            <Route path="/holidays" element={<HolidayPage />} />
            <Route path="/leaves" element={<LeavePage />} />
            <Route path="/roles" element={<RolesPage />} />
            <Route path="/categories" element={<CategoriesPage />} />
            <Route path="/tags" element={<TagsPage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/geo-location" element={<GeoLocationPage />} />
            <Route path="/reports" element={<ReportsPage />} />
            <Route path="/legacy-clients" element={<LegacyClientsPage />} />
            <Route path="/employees" element={<EmployeesPage />} />
            <Route path="/legacy-tasks" element={<LegacyTasksPage />} />
            <Route path="/timeline" element={<TimelinePage />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Suspense>
    </SSOGate>
  );
}
