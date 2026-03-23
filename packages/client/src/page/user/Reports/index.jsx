import ConsolidatedTable from 'components/ReportModule/ConsolidatedTable';
import AdvanceFilters from 'components/ReportModule/Employee/AdvanceFilters';
import AttendanceReport from 'components/ReportModule/Employee/AttendanceReport';
import DistanceTravelledReport from 'components/ReportModule/Employee/DistanceTravelledReport';
import EmployeeReport from 'components/ReportModule/EmployeeReport/EmployeeReport';
import DistanceTravelledTable from 'components/ReportModule/Employee/Table/DistanceTravelledTable';
import TaskInsigts from 'components/ReportModule/Employee/TaskInsigts';
import ReportFilter from 'components/ReportModule/ReportFilter';
import { fi } from 'date-fns/locale';
import React, { useContext, useMemo, useState } from 'react';
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom';
import { useInfiniteQuery, useQuery } from '@tanstack/react-query';
import { exportPDF } from '../../../components/ReportModule/ConsolidatedEmployeeReporthelpers/allReport';
import { exportPDFattendanceReport } from '../../../components/ReportModule/ConsolidatedEmployeeReporthelpers/attendanceReport';
import { useEffect } from 'react';
import { employeeReportStats } from 'components/ReportModule/Employee/Chart/Api/post';
import ChartContext from 'components/ChartContext/Context';
import check from '../../../assets/images/check.png';

import moment from 'moment/moment';
const Report = () => {
  const [filter, setFilter] = useState([]);
  const location = useLocation();
  const [searchParams] = useSearchParams();
  const [checkimg, setcheckimg] = useState();
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
    employeeTotalTaskListNum,
    setemployeeTotalTaskListNum,
    employeeClientChart,
    employeeClientChartData,
    employeeClientTableData,
    employeeTravelledChart,
    setsettaskListData,
    taskListData,
    employeeTravelledTableData,
    modeBike,
    blueBike,
    picRatio,
    setpicRatio,
    taskStageChartData,
    settaskStageChartData,
    taskStatusChartData,
    settaskStatusChartData,
  } = useContext(ChartContext);
  // To get the value of a specific query parameter, use searchParams.get()
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const employeeId = searchParams.get('empId');
  const filters = {
    employeeId: employeeId,
    startDate: startDate,
    endDate: endDate,
  };

  const [isClicked, setIsClicked] = useState([]);
  useEffect(() => {
    setcheckimg(check);
  }, []);

  return (
    <>
      <>
        <div className="grid grid-cols-12 col-span-12 w-full gap-5 mt-4">
          {/* <AdvanceFilters /> */}
          <EmployeeReport
            empolyeeDeatils={location?.state}
            employeeId={employeeId}
            setStartDate={setStartDate}
            setEndDate={setEndDate}
            exportPDF={() =>
              exportPDF(
                taskStageImg,
                taskStatusImg,
                attendanceTable,
                employeeReportData,
                employeeStatsData,
                profileImg,
                employeeTotalTaskListNum,
                taskListData,
                employeeClientChart,
                employeeClientChartData,
                employeeClientTableData,
                attendanceVennData,
                modeBike,
                employeeTravelledChart,

                employeeTravelledTableData,
                blueBike,
                picRatio,
                taskStageChartData,
                taskStatusChartData
              )
            }
            // setSelectedFilters={setSelectedFilters}
            filters={filters}
            setIsClicked={setIsClicked}
            setFilter={setFilter}
          />
          <TaskInsigts
            empolyeeDeatils={location?.state}
            employeeId={employeeId}
            filter={filter}
            filters={filters}
          />
        </div>
        <div className="grid grid-cols-12 col-span-12 w-full gap-5 mt-4">
          <AttendanceReport
            employeeId={employeeId}
            filter={filter}
            exportPDFattendanceReport={() =>
              exportPDFattendanceReport(
                employeeReportData,
                employeeStatsData,
                attendanceTable,
                attendanceVennData,
                profileImg,
                modeBike,
                taskListData
              )
            }
          />
          <DistanceTravelledReport employeeId={employeeId} filter={filter} />
        </div>
      </>
    </>
  );
};

export default Report;
