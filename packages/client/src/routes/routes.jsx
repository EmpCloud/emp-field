import {
  createBrowserRouter,
  createRoutesFromElements,
  Navigate,
  Route,
} from 'react-router-dom';
import PrivateRoute from 'auth/PrivateRoute';
import { Layout } from 'layout/Layout';
import AuthenticationLayout from 'page/authentication/AuthenticationLayout';
import ForgotPassword from 'page/authentication/ForgotPassword';
import Login from 'page/authentication/Login';
import ResetPassword from 'page/authentication/ResetPassword';
import ResetPasswordSuccess from 'page/authentication/ResetPasswordSuccess';
import ErrorPage from 'page/ErrorPage';
import UserLayout from 'page/user';
import Attendance from 'page/user/Attendance';
import Clients from 'page/user/Clients';
import Dashboard from 'page/user/Dashboard';
import Employee from 'page/user/Employee';
import LiveTracking from 'page/user/LiveTracking';
import Profile from 'page/user/Profile';
import Status from 'page/user/Status';
import Task from 'page/user/Task';
import Report from 'page/user/Reports';
import AttendanceComp from 'components/AttendanceModule/AttendancePage';
import Holiday from 'components/AttendanceModule/HolidayPage';
import LeaveTypes from 'components/AttendanceModule/LeaveTypesPage';
import Leaves from 'components/AttendanceModule/LeavePage/index';
import Request from 'components/AttendanceModule/RequestPage';
import Settings from 'components/AttendanceModule/SettingsPage';
import MobileSetting from 'page/user/MobileSetting';
import ConsolidatedTable from 'components/ReportModule/ConsolidatedTable';
import Tasks from 'page/user/Tasks';
import StageTasks from 'page/user/StageTasks';
import { RestrictedRouteByOrg } from './PrivateRoute';
const disallowedOrgIds =
  import.meta.env.VITE_LIVE_TRACK_DISABLE_ORG_ID?.split(',') || [];

export const routes = createBrowserRouter(
  createRoutesFromElements(
    <>
      <Route
        path="/"
        element={<Navigate to="/admin/login" />}
        errorElement={<ErrorPage />}
      />

      <Route path="admin" element={<Layout />}>
        <Route element={<AuthenticationLayout />}>
          <Route path="login" element={<Login />} />
          <Route path="forgot-password" element={<ForgotPassword />} />
          <Route path="reset-password" element={<ResetPassword />} />
          <Route
            path="reset-password-success"
            element={<ResetPasswordSuccess />}
          />
        </Route>
        <Route
          element={
            <PrivateRoute>
              <UserLayout />
            </PrivateRoute>
          }>
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="employee" element={<Employee />} />
          <Route path="mobile-setting" element={<MobileSetting />} />
          <Route path="employee/attendance" element={<Attendance />}>
            <Route path="" element={<AttendanceComp />} />
            <Route path="leaves" element={<Leaves />} />
            <Route path="request" element={<Request />} />
            <Route path="leave-types" element={<LeaveTypes />} />
            <Route path="holiday" element={<Holiday />} />
            <Route path="settings" element={<Settings />} />
          </Route>
          <Route
            element={
              <RestrictedRouteByOrg disallowedOrgIds={disallowedOrgIds} />
            }>
            <Route path="live-tracking" element={<LiveTracking />} />
            <Route path="clients" element={<Clients />} />
            <Route path="task" element={<Tasks />}>
              <Route path="" element={<Task />} />
              <Route path="stagetasks" element={<StageTasks />} />
            </Route>
            <Route path="report" element={<ConsolidatedTable />} />
            <Route path="employee-report" element={<Report />} />
          </Route>
          <Route path="stats" element={<Status />} />
          <Route path="profile" element={<Profile />} />
        </Route>
      </Route>
    </>
  )
);
