import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

const DisplayTitles = () => {
  const location = useLocation();

  useEffect(() => {
    const getTitleFromPath = path => {
      switch (path) {
        case '/':
          return 'Field Force Management';
        case '/admin/google-redirect':
          return 'Google Login';
        case '/admin/twitter-redirect':
          return 'Twitter Login';
        case '/admin/facebook-redirec':
          return 'Facebook Login';
        case '/admin/forgot-password':
          return 'Forgot-Password | Field Force Management';
        case '/admin/reset-password':
          return 'Reset-Password | Field Force Management';
        case '/admin/reset-password-success':
          return 'Reset-Password-Success | Field Force Management';
        case '/admin/dashboard':
          return 'Dashboard | Field Force Management';
        case '/admin/employee':
          return 'Employee | Field Force Management';
        case '/admin/live-tracking':
          return 'Live-Tracking | Field Force Management';
        case '/admin/mobile-setting':
          return 'Geo Fencing | Field Force Management';
        case '/admin/clients':
          return 'Clients | Field Force Management';
        case '/admin/task':
          return 'Tasks | Field Force Management';
        case '/admin/task/stagetasks':
          return 'Tasks Stage | Field Force Management';
        case '/admin/report':
          return 'Report | Field Force Management';
        case '/admin/employee-report':
          return 'Employee Report | Field Force Management';
        default:
          return 'Field Force Management';
      }
    };

    const title = getTitleFromPath(location.pathname);
    document.title = title;
  }, [location.pathname]);
};

export default DisplayTitles;
