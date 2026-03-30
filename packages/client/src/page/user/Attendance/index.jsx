import TopTabs from 'components/AttendanceModule/TopTabs';
import { Outlet } from 'react-router-dom';

const Attendance = () => {
  return (
    <>
      <TopTabs />
      <Outlet />
    </>
  );
};

export default Attendance;
