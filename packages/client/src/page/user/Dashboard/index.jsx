import Insights from 'components/DashboardModule/Insights';
import LiveEmpLocation from 'components/DashboardModule/LiveEmpLocation';
import TasksDonot from 'components/DashboardModule/TasksDonot';
import BarChart from 'components/DashboardModule/Charts/BarChart';
import Employees from 'components/DashboardModule/Employees';
import { DecodeJWTToken } from 'context/Filters/util.token';
import Cookies from 'js-cookie';

const Dashboard = () => {
  const orgId = DecodeJWTToken(Cookies.get('token'));
  const disabledOrgIds =
    import.meta.env.VITE_LIVE_TRACK_DISABLE_ORG_ID?.split(',') || [];
  const hidingDahboardFeature =
    orgId && !disabledOrgIds.includes(String(orgId));
  return (
    <>
      <Insights />
      {hidingDahboardFeature && <LiveEmpLocation />}
      {hidingDahboardFeature && <TasksDonot />}
      <Employees />
      {hidingDahboardFeature && <BarChart />}
    </>
  );
};

export default Dashboard;
