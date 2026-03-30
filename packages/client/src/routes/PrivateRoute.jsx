import { DecodeJWTToken } from 'context/Filters/util.token';
import Cookies from 'js-cookie';
import { Navigate, Outlet } from 'react-router-dom';

// Authentication Logic Using Cookies
const isAuthenticated = () => {
  // Getting value of cookie
  return Cookies.get('token');
};

const PrivateRoute = () => {
  return isAuthenticated() ? <Outlet /> : <Navigate to="/admin/login" />;
};

export default PrivateRoute;

export const RestrictedRouteByOrg = ({ disallowedOrgIds }) => {
  const orgId = DecodeJWTToken(Cookies.get('token'));
  const isBlocked = disallowedOrgIds.includes(String(orgId));
  return isBlocked ? <Navigate to="/admin/dashboard" replace /> : <Outlet />;
};
