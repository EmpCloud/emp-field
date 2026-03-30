import TaskDetailedContainer from './TaskDetailedContainer';
import TaskList from './TaskList';
import { exportPDFTaskList } from '../../../components/ReportModule/ConsolidatedEmployeeReporthelpers/taskList';
import { useContext } from 'react';
import ChartContext from 'components/ChartContext/Context';

const TaskInsigts = ({ empolyeeDeatils, employeeId, filter }) => {
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
    modeBike,
    setmodeBike,
    taskStageChartData,
    taskStatusChartData,
    taskListData,
  } = useContext(ChartContext);
  return (
    <>
      <div className="grid grid-cols-12 col-span-12 rounded-lg w-full gap-4">
        <TaskList
          filter={filter}
          employeeId={employeeId}
          exportPDFTaskList={() =>
            exportPDFTaskList(
              taskStageImg,
              taskStatusImg,
              attendanceTable,
              employeeReportData,
              employeeStatsData,
              profileImg,
              employeeTotalTaskListNum,
              employeeClientChart,
              employeeClientChartData,
              employeeClientTableData,
              attendanceVennData,
              modeBike,
              taskStageChartData,
              taskStatusChartData,
              taskListData
            )
          }
        />
        <TaskDetailedContainer
          empolyeeDeatils={empolyeeDeatils}
          employeeId={employeeId}
          filter={filter}
        />
      </div>
    </>
  );
};

export default TaskInsigts;
