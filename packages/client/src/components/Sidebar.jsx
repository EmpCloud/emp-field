import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';

import { Link, useLocation } from 'react-router-dom';

import EMPLogo from 'assets/images/emp-logo.webp';
import EMPLogoFull from 'assets/images/emp-icon-full.png';
import tasklnonsel from 'assets/images/tasklnonsel.png';
import tasklsel from 'assets/images/tasklsel.png';
import stagetsnonsec from 'assets/images/stagetsnonsec.png';
import stagetssec from 'assets/images/stagetssec.png';
import { useUIController } from 'context/context';
import { IoIosArrowDown } from 'react-icons/io';
import { useState } from 'react';
import { DecodeJWTToken } from 'context/Filters/util.token';
import Cookies from 'js-cookie';

const Sidebar = () => {
  const location = useLocation();
  const [controller] = useUIController();
  const { isSidebarOpen } = controller;
  const [taskopen, settaskopen] = useState(false);
  const [employeeopen, setemployeeopen] = useState(false);
  const handletaskchange = () => {
    settaskopen(!taskopen);
  };
  const orgId = DecodeJWTToken(Cookies.get('token'));
  const disabledOrgIds =
    import.meta.env.VITE_LIVE_TRACK_DISABLE_ORG_ID?.split(',') || [];
  const hiddingNavbarFeature = orgId && !disabledOrgIds.includes(String(orgId));
  const handleemployeechange = () => {
    setemployeeopen(!employeeopen);
  };
  return (
    <>
      <TooltipProvider delayDuration={100}>
        <aside
          className={`sidebar ${isSidebarOpen ? 'w-[230px] lg:w-[150px] 2xl:w-[250px]' : 'lg:flex w-0 lg:w-[60px] 2xl:w-[100px]'}`}>
          <Link to="/admin/dashboard" className="sidebar-logo">
            <img className="emp-logo" src={EMPLogo} alt="emp-logo" />
            {isSidebarOpen && (
              <img
                className="emp-logo-full fade-in"
                src={EMPLogoFull}
                alt="emp-logo-full"
              />
            )}
          </Link>

          <nav>
            <Tooltip>
              <TooltipTrigger asChild>
                <Link to="dashboard" className="navbar-item mb-6">
                  <div className="sidebar-icon dashboard-icon"></div>
                  {isSidebarOpen && (
                    <span className="fade-in text-xxs 2xl:text-base">
                      Dashboard
                    </span>
                  )}
                  {location.pathname === '/admin/dashboard' && (
                    <div className="sidebar-active"></div>
                  )}
                </Link>
              </TooltipTrigger>
              <TooltipContent side="right">Dashboard</TooltipContent>
            </Tooltip>
            {/* <Tooltip>
              <TooltipTrigger asChild>
                <Link to="employee" className="navbar-item mb-6">
                  <div className="sidebar-icon employee-icon"></div>
                  {isSidebarOpen && (
                    <span className="fade-in text-xxs 2xl:text-base">
                      Employee
                    </span>
                  )}
                  {location.pathname === '/admin/employee' && (
                    <div className="sidebar-active"></div>
                  )}
                </Link>
              </TooltipTrigger>
              <TooltipContent side="right">Employee</TooltipContent>
            </Tooltip> */}
            <Tooltip>
              <TooltipTrigger asChild>
                <Link
                  to="employee"
                  className={`navbar-item relative  ${isSidebarOpen && employeeopen ? 'mb-3' : 'mb-6'}`}
                  onClick={handleemployeechange}>
                  <div className="sidebar-icon employee-icon"></div>

                  {isSidebarOpen && (
                    <span className="fade-in text-xxs 2xl:text-base">
                      Employee
                    </span>
                  )}
                  <IoIosArrowDown
                    className={`absolute  ${isSidebarOpen ? 'right-[24px]' : 'right-[6px]'} ${employeeopen ? '' : '-rotate-90'}`}
                  />
                  {location.pathname === '/admin/employee' && (
                    <div className="sidebar-active"></div>
                  )}
                </Link>
              </TooltipTrigger>

              {isSidebarOpen && employeeopen && (
                <div className="flex flex-col 2xl:text-base text-[10px] stagetask w-full pl-[32px]">
                  <Link
                    to="employee"
                    className={`mb-3 flex items-center ${
                      location.pathname === '/admin/employee'
                        ? '!text-[#1f3a78]'
                        : ''
                    }`}>
                    {location.pathname === '/admin/employee' ? (
                      <img
                        src={tasklsel}
                        alt=""
                        className="w-[15px] h-[15px] mr-4"
                      />
                    ) : (
                      <img
                        src={tasklnonsel}
                        alt=""
                        className="w-[15px] h-[15px] mr-4"
                      />
                    )}
                    Employee Details
                  </Link>

                  <Link
                    to="employee/attendance"
                    className={`flex  items-center mb-3 ${
                      location.pathname === '/admin/employee/attendance'
                        ? '!text-[#1f3a78]'
                        : ''
                    }`}>
                    {location.pathname === '/admin/employee/attendance' ? (
                      <img
                        src={stagetssec}
                        alt=""
                        className="w-[15px] h-[15px] mr-2"
                      />
                    ) : (
                      <img
                        src={stagetsnonsec}
                        alt=""
                        className="w-[15px] h-[15px] mr-2"
                      />
                    )}
                    Attendance
                  </Link>
                </div>
              )}
              <TooltipContent side="right">
                {isSidebarOpen ? (
                  <span>Employee</span>
                ) : (
                  <div className="flex flex-col">
                    <Link to="employee" className="mb-3">
                      Employee
                    </Link>
                    <Link to="employee/attendance">
                      Employee Attendance
                    </Link>{' '}
                  </div>
                )}
              </TooltipContent>
            </Tooltip>
            {hiddingNavbarFeature && (
              <Tooltip>
                <TooltipTrigger asChild>
                  <Link to="live-tracking" className="navbar-item mb-6">
                    <div className="sidebar-icon tracking-icon"></div>
                    {isSidebarOpen && (
                      <span className="fade-in text-xxs 2xl:text-base">
                        Live Tracking
                      </span>
                    )}
                    {location.pathname === '/admin/live-tracking' && (
                      <div className="sidebar-active"></div>
                    )}
                  </Link>
                </TooltipTrigger>
                <TooltipContent side="right">Live Tracking</TooltipContent>
              </Tooltip>
            )}
            <Tooltip>
              <TooltipTrigger asChild>
                <Link to="mobile-setting" className="navbar-item mb-6">
                  <div className="sidebar-icon mobile-setting-icon"></div>
                  {isSidebarOpen && (
                    <span className="fade-in text-xxs 2xl:text-base">
                      Geo Fencing
                    </span>
                  )}
                  {location.pathname === '/admin/mobile-setting' && (
                    <div className="sidebar-active"></div>
                  )}
                </Link>
              </TooltipTrigger>
              <TooltipContent side="right">Geo Fencing</TooltipContent>
            </Tooltip>
            {/* <Tooltip>
              <TooltipTrigger asChild>
                <Link to="attendance" className="navbar-item">
                  <div className="sidebar-icon attendance-icon"></div>
                  {isSidebarOpen && (
                    <span className="fade-in text-xxs 2xl:text-base">
                      Attendance
                    </span>
                  )}
                  {location.pathname === '/admin/attendance' && (
                    <div className="sidebar-active"></div>
                  )}
                </Link>
              </TooltipTrigger>
              <TooltipContent side="right">Attendance</TooltipContent>
            </Tooltip> */}
            {/* <Tooltip>
              <TooltipTrigger asChild>
                <Link to="stats" className="navbar-item">
                  <div className="sidebar-icon status-icon"></div>{' '}
                  {isSidebarOpen && (
                    <span className="fade-in text-xxs 2xl:text-base">
                      Status
                    </span>
                  )}
                  {location.pathname === '/admin/stats' && (
                    <div className="sidebar-active"></div>
                  )}
                </Link>
              </TooltipTrigger>
              <TooltipContent side="right">Stats</TooltipContent>
            </Tooltip> */}
            {hiddingNavbarFeature && (
              <Tooltip>
                <TooltipTrigger asChild>
                  <Link to="clients" className="navbar-item mb-6">
                    <div className="sidebar-icon clients-icon"></div>
                    {isSidebarOpen && (
                      <span className="fade-in text-xxs 2xl:text-base">
                        Clients
                      </span>
                    )}
                    {location.pathname === '/admin/clients' && (
                      <div className="sidebar-active"></div>
                    )}
                  </Link>
                </TooltipTrigger>
                <TooltipContent side="right">Clients</TooltipContent>
              </Tooltip>
            )}
            {hiddingNavbarFeature && (
              <Tooltip>
                <TooltipTrigger asChild>
                  <Link
                    to="task"
                    className={`navbar-item relative  ${isSidebarOpen && taskopen ? 'mb-3' : 'mb-6'}`}
                    onClick={handletaskchange}>
                    <div className="sidebar-icon task-icon"></div>

                    {isSidebarOpen && (
                      <span className="fade-in text-xxs 2xl:text-base">
                        Task
                      </span>
                    )}
                    <IoIosArrowDown
                      className={`absolute  ${isSidebarOpen ? 'right-[24px]' : 'right-[6px]'} ${taskopen ? '' : '-rotate-90'}`}
                    />

                    {location.pathname === '/admin/task' && (
                      <div className="sidebar-active"></div>
                    )}
                  </Link>
                </TooltipTrigger>

                {isSidebarOpen && taskopen && (
                  <div className="flex flex-col 2xl:text-base text-[10px] stagetask w-full pl-[28px]">
                    <Link
                      to="task"
                      className={`mb-3 flex items-center ${
                        location.pathname === '/admin/task'
                          ? '!text-[#1f3a78]'
                          : ''
                      }`}>
                      {location.pathname === '/admin/task' ? (
                        <img
                          src={tasklsel}
                          alt=""
                          className="w-[15px] h-[15px] mr-2"
                        />
                      ) : (
                        <img
                          src={tasklnonsel}
                          alt=""
                          className="w-[15px] h-[15px] mr-2"
                        />
                      )}
                      Task Lists
                    </Link>

                    <Link
                      to="task/stagetasks"
                      className={`flex  items-center mb-3 ${
                        location.pathname === '/admin/task/stagetasks'
                          ? '!text-[#1f3a78]'
                          : ''
                      }`}>
                      {location.pathname === '/admin/task/stagetasks' ? (
                        <img
                          src={stagetssec}
                          alt=""
                          className="w-[15px] h-[15px] mr-2"
                        />
                      ) : (
                        <img
                          src={stagetsnonsec}
                          alt=""
                          className="w-[15px] h-[15px] mr-2"
                        />
                      )}
                      Tasks Stage
                    </Link>
                  </div>
                )}
                <TooltipContent side="right">
                  {isSidebarOpen ? (
                    <span>Task</span>
                  ) : (
                    <div className="flex flex-col">
                      <Link to="task" className="mb-3">
                        Task Lists
                      </Link>
                      <Link to="task/stagetasks">Tasks Stage</Link>{' '}
                    </div>
                  )}
                </TooltipContent>
              </Tooltip>
            )}
            {hiddingNavbarFeature && (
              <Tooltip>
                <TooltipTrigger asChild>
                  <Link to="report" className="navbar-item">
                    <div className="sidebar-icon report-icon"></div>
                    {isSidebarOpen && (
                      <span className="fade-in text-xxs 2xl:text-base">
                        Report
                      </span>
                    )}
                    {location.pathname === '/admin/report' && (
                      <div className="sidebar-active"></div>
                    )}
                  </Link>
                </TooltipTrigger>
                <TooltipContent side="right">Report</TooltipContent>
              </Tooltip>
            )}
          </nav>
        </aside>
      </TooltipProvider>
    </>
  );
};

export default Sidebar;
