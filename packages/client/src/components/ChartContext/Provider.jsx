import React, { useRef, useState } from 'react';
import ChartContext from './Context';

const ChartProvider = ({ children }) => {
  const [menu, setMenu] = useState('menuhs');
  const [ReSize, setReSize] = useState(false);
  const [taskStatusImg, settaskStatusImg] = useState('');
  const [employeeReportData, setemployeeReportData] = useState('');
  const [profileImg, setprofileImg] = useState('');
  const [profileUrlImg, setprofileUrlImg] = useState('');
  const [taskStageImg, settaskStageImg] = useState('');
  const [employeeGenrateReportMain, setemployeeGenrateReportMain] =
    useState(false);
  const [attendanceVenn, setattendanceVenn] = useState('');
  const [attendanceVennData, setattendanceVennData] = useState('');
  const [attendanceTable, setattendanceTable] = useState('');
  const [distanceChartImg, setdistanceChartImg] = useState('');
  const [distanceTableImg, setdistanceTableImg] = useState('');
  const taskStatusChartRef = React.createRef('');
  const taskStageChartRef = React.createRef('');
  const [employeeStatsData, setemployeeStatsData] = useState([]);
  const [employeeClientTableData, setemployeeClientTableData] = useState([]);
  const [employeeClientChart, setemployeeClientChart] = useState([]);
  const [employeeClientChartData, setemployeeClientChartData] = useState('');
  const [employeeTravelledTableData, setemployeeTravelledTableData] =
    useState('');
  const [employeeTravelledChart, setemployeeTravelledChart] = useState([]);
  const [modeBike, setmodeBike] = useState('');
  const [employeeTotalTaskListNum, setemployeeTotalTaskListNum] = useState('');
  const [taskListData, settaskListData] = useState('');
  const [clientsReportData, setclientsReportData] = useState('');
  const [blueBike, setblueBike] = useState([]);
  const [picRatio, setpicRatio] = useState('1.7');
  const [taskStageChartData, settaskStageChartData] = useState('');
  const [taskStatusChartData, settaskStatusChartData] = useState('');
  const [taskValueVolumeData, setTaskValueVolumeData] = useState({
    maxTaskVolume: 0,
    maxConvertedAmountInUSD: 0,
  });
  return (
    <ChartContext.Provider
      value={{
        taskValueVolumeData,
        setTaskValueVolumeData,
        attendanceTable,
        employeeTravelledTableData,
        setemployeeTravelledTableData,
        employeeClientChart,
        employeeTotalTaskListNum,
        clientsReportData,
        setclientsReportData,
        setemployeeTotalTaskListNum,
        setemployeeClientChart,
        employeeClientChartData,
        setemployeeClientChartData,
        setattendanceTable,
        taskStatusImg,
        attendanceVennData,
        setattendanceVennData,
        settaskStatusImg,
        taskStageImg,
        settaskStageImg,
        taskStatusChartRef,
        employeeClientTableData,
        setemployeeClientTableData,
        employeeStatsData,
        setemployeeStatsData,
        taskStageChartRef,
        employeeGenrateReportMain,
        setemployeeGenrateReportMain,
        employeeTravelledChart,
        setemployeeTravelledChart,
        attendanceVenn,
        setattendanceVenn,
        employeeReportData,
        setemployeeReportData,
        profileImg,
        setprofileImg,
        profileUrlImg,
        setprofileUrlImg,
        modeBike,
        setmodeBike,
        blueBike,
        setblueBike,
        picRatio,
        setpicRatio,
        taskStageChartData,
        settaskStageChartData,
        taskStatusChartData,
        settaskStatusChartData,
        taskListData,
        settaskListData,
      }}>
      {children}
    </ChartContext.Provider>
  );
};

export default ChartProvider;
