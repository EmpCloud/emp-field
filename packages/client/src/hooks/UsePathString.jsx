import { useLocation } from 'react-router-dom';

const usePathString = () => {
  const location = useLocation();
  const pathname = location.pathname;

  let pathString;

  switch (pathname) {
    case '/admin/dashboard':
      pathString = 'Dashboard';
      break;
    case '/admin/employee':
      pathString = 'Employee';
      break;
    case '/admin/live-tracking':
      pathString = 'Live Tracking';
      break;
    // case '/admin/report':
    //   pathString = 'Employee Reports';
    //   break;
    case '/admin/attendance':
      pathString = 'Attendance';
      break;
    case '/admin/stats':
      pathString = 'Stats';
      break;
    case '/admin/clients':
      pathString = 'Clients';
      break;
    case '/admin/task':
      pathString = 'Task';
      break;
    case '/admin/profile':
      pathString = 'Profile Settings';
      break;
    case '/admin/report':
      pathString = 'Consolidated Reports';
      break;
    case '/admin/mobile-setting':
      pathString = 'Geo Fencing';
      break;
    case '/admin/task/stagetasks':
      pathString = 'Tasks Stage';
      break;
    default:
      pathString = '';
  }

  return pathString;
};

export default usePathString;
