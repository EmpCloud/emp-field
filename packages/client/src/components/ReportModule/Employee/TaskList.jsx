import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import TaskCollapsible from './Collapsible/TaskCollapsible';
import { ClipboardMinus, MoveDiagonal } from 'lucide-react';
import ReportDashboardIcon from '../../../assets/images/reportTable/carbon_report.png';

import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import AttendanceReport from './AttendanceReport';
import DashboardPopoverTaskList from './Popover/DashboardPopoverTaskList';
import { employeetasksDetails } from './Chart/Api/post';
import { useQuery } from '@tanstack/react-query';
import Cookies from 'js-cookie';
import moment from 'moment';
import { useContext } from 'react';
import ChartContext from 'components/ChartContext/Context';

export function getTaskStatus(statusName) {
  switch (statusName) {
    case 'Pending':
      return 0; // Pending
    case 'Start':
      return 1; // Start
    case 'Pause':
      return 2; // Pause
    case 'Resume':
      return 3; // Resume
    case 'Finish':
      return 4; // Finish
    case 'Delete':
      return 5; // Delete
    default:
      return null; // Unknown status
  }
}
const TaskList = ({ filter, employeeId, exportPDFTaskList }) => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const {
    taskListData,
    settaskListData,
    taskValueVolumeData,
    setTaskValueVolumeData,
  } = useContext(ChartContext);
  const { data, isLoading, error } = useQuery({
    queryKey: [
      'employeetasksDetails',
      employeeId,
      filter?.dateRange?.startDate ||
        moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
      filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
      getTaskStatus(filter?.status) ?? null,
      filter?.stage || '',

      filter?.taskVolume?.min,
      filter?.taskVolume?.max || '',

      filter?.taskValue?.min || '',
      filter?.taskValue?.max || '',
    ],
    queryFn: () => {
      const data = {
        employee_Id: employeeId,
        startDate:
          filter?.dateRange?.startDate ||
          moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
        endDate:
          filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
        taskStatus: filter?.status ? getTaskStatus(filter.status) : undefined,
        taskStage: filter?.stage || undefined,
        Volume:
          filter?.taskVolume?.min || filter?.taskVolume?.max
            ? {
                minTaskVolume: filter.taskVolume?.min || undefined,
                maxTaskVolume: filter.taskVolume?.max || undefined,
              }
            : undefined,
        Amount:
          filter?.taskValue?.min || filter?.taskValue?.max
            ? {
                minAmount: filter.taskValue?.min || undefined,
                maxAmount: filter.taskValue?.max || undefined,
              }
            : undefined,
      };
      const cleanedData = JSON.parse(JSON.stringify(data));
      return employeetasksDetails({
        skip: 0,
        limit: 500,
        data: cleanedData,
      });
    },
  });

  const taskDetails = data?.data?.body?.data;
  const userTaskDetails = data?.data?.body?.data?.userTaskDetails;
  const maxTaskVolume = Math.max(
    ...(Array.isArray(userTaskDetails)
      ? userTaskDetails.map(task => task.taskVolume ?? 0)
      : [0])
  );
  const maxConvertedAmountInUSD = Math.ceil(
    Math.max(
      ...(Array.isArray(userTaskDetails)
        ? userTaskDetails.map(task => task?.value?.convertedAmountInUSD ?? 0)
        : [0])
    )
  );
  useEffect(() => {
    setTaskValueVolumeData({
      maxTaskVolume: maxTaskVolume,
      maxConvertedAmountInUSD: maxConvertedAmountInUSD,
    });
  }, [maxTaskVolume, maxConvertedAmountInUSD]);

  const handleCollapseToggle = collapsed => {
    setIsCollapsed(collapsed);
  };
  // console.log(userTaskDetails);
  useEffect(() => {
    settaskListData(userTaskDetails);
  }, [userTaskDetails]);
  return (
    <>
      <div className="grid gap-4 col-span-12 xl:col-span-6 bg-white rounded-lg   sm:max-w-full w-full shadow-none border-none  ">
        <Card className="h-full shadow-none border-none overflow-hidden w-full">
          <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 py-2 2xl:h-12 min-h-10 w-full">
            <CardTitle className="text-xs 2xl:text-sm font-bold text-white flex items-center gap-2 justify-between w-full">
              <div className="left_content flex gap-2 justify-center items-center">
                Task List
                <div className="h-6 w-6 pr-0.5 rounded-full bg-[#a993f3] text-white flex justify-center items-center">
                  <span>{taskDetails?.totalCount ?? 0}</span>
                </div>
              </div>
              <div className="right_content flex justify-center items-center gap-3">
                {/* <div className="dashboard_design flex gap-1 px-2 py-1 bg-white border-2 rounded-sm border-white">
                  <img src={ReportDashboardIcon} className="h-4 w-4" alt="" />
                  <span className="text-xs  text-[#6A6AEC]">Dashboard</span>
                </div> */}
                <DashboardPopoverTaskList
                  taskDetails={taskDetails}
                  exportPDFTaskList={exportPDFTaskList}
                />
                {/* <MoveDiagonal className="cursor-pointer h-6 w-6" /> */}
              </div>
            </CardTitle>
          </CardHeader>
          {isLoading ? (
            <div className="h-24 text-center flex items-center justify-center">
              Loading...
            </div>
          ) : !userTaskDetails?.length ? (
            <div className="h-24 text-center flex items-center justify-center">
              No tasks available.
            </div>
          ) : (
            <CardContent className="2xl:h-[900px] lg:h-[900px] md:h-[1080px] sm:h-[980px] h-[1200px] px-4 py-3 w-full shadow-none border-none md:overflow-x-hidden  overflow-x-auto">
              <div className="flex justify-end w-full items-center gap-2">
                <Label
                  className={`text-xs ${isCollapsed ? 'font-medium' : 'font-bold'}`}
                  htmlFor="location-off">
                  Collapse All
                </Label>
                <Switch
                  id="location-mode"
                  onCheckedChange={checked => handleCollapseToggle(checked)}
                  checked={isCollapsed}
                />
                <Label
                  className={`text-xs ${isCollapsed ? 'font-bold' : 'font-medium'}`}
                  htmlFor="location-on">
                  Expand All
                </Label>
              </div>
              <div className="flex flex-col">
                {userTaskDetails?.map((task, index) => (
                  <TaskCollapsible
                    key={index}
                    task={task}
                    taskDetails={taskDetails}
                    isCollapsed={isCollapsed}
                  />
                ))}
              </div>
            </CardContent>
          )}
        </Card>
      </div>
      {/* <AttendanceReport /> */}
    </>
  );
};

export default TaskList;
