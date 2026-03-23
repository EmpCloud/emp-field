import Cookies from 'js-cookie';
import { Navigate, useLocation } from 'react-router-dom';
//test

const PrivateRoute = ({ children }) => {
  const accessToken = Cookies.get('token');
  const location = useLocation();
  const isAuthenticated =
    accessToken !== undefined && accessToken !== null && accessToken !== '';

  if (!isAuthenticated) {
    sessionStorage.setItem('redirectPath', location.pathname);
    return <Navigate to="/admin/login" />;
  }
  return children;
  // return isAuthenticated ? children : <Navigate to="/admin/login" />;
};

export default PrivateRoute;
