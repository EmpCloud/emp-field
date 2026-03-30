import DistanceTravelledChart from './Chart/DistanceTravelledChart';
import DistanceTravelledTable from './Table/DistanceTravelledTable';
import { exportPDFtravelledReport } from '../ConsolidatedEmployeeReporthelpers/travelledReport';
import { useContext } from 'react';
import ChartContext from 'components/ChartContext/Context';

const DistanceTravelledReport = ({ employeeId, filter }) => {
  const {
    taskStatusImg,
    taskStageImg,
    attendanceTable,
    setattendanceTable,
    attendanceVenn,
    attendanceVennData,
    setattendanceVenn,
    employeeReportData,
    profileUrlImg,
    employeeStatsData,
    profileImg,
    employeeTravelledChart,
    employeeClientTableData,
    setemployeeClientTableData,
    employeeClientChartData,
    employeeTravelledTableData,
    modeBike,
    setmodeBike,
    blueBike,
    taskListData,
  } = useContext(ChartContext);
  return (
    <>
      <div className="grid col-span-12 xl:col-span-5 bg-white rounded-lg gap-2 w-full">
        <DistanceTravelledChart
          employeeId={employeeId}
          filter={filter}
          exportPDFtravelledReport={() =>
            exportPDFtravelledReport(
              employeeReportData,
              employeeStatsData,
              attendanceTable,
              attendanceVennData,
              profileImg,
              employeeClientTableData,
              employeeTravelledChart,
              employeeClientChartData,
              employeeTravelledTableData,
              modeBike,
              blueBike,
              taskListData
            )
          }
        />
      </div>
    </>
  );
};

export default DistanceTravelledReport;
