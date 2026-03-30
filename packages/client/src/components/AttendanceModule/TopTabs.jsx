import { CardTitle } from '@/components/ui/card';
import { Link, useLocation } from 'react-router-dom';

const TopTabs = () => {
  const location = useLocation();
  const hideData = false;
  return (
    <div className="card-shadow grid-cols-12 col-span-12 rounded text-xs">
      <div className="flex md:flex-row flex-col gap-8 p-3 2xl:py-5 2xl:px-4">
        <div
          className={` text-nowrap col-span-1 font-semibold relative ${location.pathname.endsWith('/attendance') ? 'active-tab' : ''} `}>
          <Link to={'/admin/employee/attendance'}>
            <CardTitle className="text-md 2xl:text-xl font-bold cursor-pointer">
              Attendance
            </CardTitle>
          </Link>
        </div>
        {hideData && (
          <div>
            <div
              className={` text-nowrap col-span-1 font-semibold relative ${location.pathname.includes('/leaves') ? 'active-tab' : ''} `}>
              <Link to={'leaves'}>Leaves</Link>
            </div>
            <div
              className={` text-nowrap col-span-1 font-semibold relative ${location.pathname.includes('/request') ? 'active-tab' : ''} `}>
              <Link to={'request'}>Attendance Requests</Link>
            </div>
            <div
              className={` text-nowrap col-span-1 font-semibold relative ${location.pathname.includes('/leave-types') ? 'active-tab' : ''} `}>
              <Link to={'leave-types'}>Leave Types</Link>
            </div>
            <div
              className={` text-nowrap col-span-1 font-semibold relative ${location.pathname.includes('/holiday') ? 'active-tab' : ''} `}>
              <Link to={'holiday'}>Holiday</Link>
            </div>
            <div
              className={` text-nowrap col-span-1 font-semibold relative ${location.pathname.includes('/settings') ? 'active-tab' : ''} `}>
              <Link to={'settings'}>Settings</Link>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default TopTabs;
