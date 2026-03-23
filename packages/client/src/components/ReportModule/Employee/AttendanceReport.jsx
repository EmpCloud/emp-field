import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { MoveDiagonal } from 'lucide-react';
import ReportDashboardIcon from '../../../assets/images/reportTable/carbon_report.png';
import AttendanceVennDiagram from './Chart/AttendanceVennDiagram';
import EmployeeAttendanceTable from './Table/EmployeeAttendanceTable';
import DashboardPopoverAttendance from './Popover/DashboardPopoverAttendance';
import { useContext, useEffect, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { employeeAttendanceDetails } from './Chart/Api/post';
import moment from 'moment';
import ChartContext from 'components/ChartContext/Context';
// import { exportPDF } from '../ConsolidatedEmployeeReporthelpers/attendanceReport';

const AttendanceReport = ({
  employeeId,
  filter,
  exportPDFattendanceReport,
}) => {
  const { data, isLoading, error } = useQuery({
    queryKey: [
      'employeeReportAttendance',
      filter?.dateRange?.startDate ||
        moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
      filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
      employeeId,
    ],
    queryFn: () =>
      employeeAttendanceDetails(
        filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
        filter?.dateRange?.startDate ||
          moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
        employeeId
      ),
  });

  let attendanceGraphDeatils = data?.body?.data;

  let attendanceDetials;
  attendanceDetials = data?.body?.data
    ? data?.body?.data[0]?.attendance
    : 'No Attendance found';
  const {
    attendanceTable,
    setattendanceTable,
    attendanceVennData,
    setattendanceVennData,
  } = useContext(ChartContext);
  useEffect(() => {
    setattendanceTable(attendanceDetials);
  }, [attendanceDetials]);

  useEffect(() => {
    if (attendanceGraphDeatils) {
      setattendanceVennData(attendanceGraphDeatils);
    }
  }, [attendanceGraphDeatils]);
  const clientsDetail =
    attendanceDetials !== 'No Attendance found' &&
    attendanceDetials?.map(attendanceDetial => {
      const isToday = moment().isSame(attendanceDetial?.date, 'day'); // Check if the date is today

      return {
        date: moment(attendanceDetial?.date).format('YYYY-MM-DD'),

        status:
          attendanceDetial?.leave_name === 'Unpaid'
            ? attendanceDetial?.day_off === false
              ? 'Week Off'
              : attendanceDetial?.start_time && attendanceDetial?.end_time
                ? 'Present'
                : 'Absent'
            : (attendanceDetial?.leave_name ?? 'Unknown'),

        checkIn: attendanceDetial?.start_time
          ? moment(attendanceDetial?.start_time).format('hh:mm A')
          : '-',

        checkOut: attendanceDetial?.end_time
          ? moment(attendanceDetial?.end_time).format('hh:mm A')
          : '-',

        requestStatus:
          attendanceDetial?.open_attendance_request !== null &&
          attendanceDetial?.open_attendance_request?.request_status === '0'
            ? 'Pending'
            : attendanceDetial?.open_attendance_request?.request_status === '1'
              ? 'Approved'
              : attendanceDetial?.open_attendance_request?.request_status ===
                  '2'
                ? 'Rejected'
                : '--',

        overriddenBy: '-',
        total:
          attendanceDetial?.start_time && attendanceDetial?.end_time
            ? moment
                .utc(
                  moment(attendanceDetial?.end_time).diff(
                    moment(attendanceDetial?.start_time)
                  )
                )
                .format('HH:mm:ss')
            : '00:00:00',

        // total:
        //   attendanceDetial?.start_time && attendanceDetial?.end_time
        //     ? moment
        //         .duration(
        //           moment(attendanceDetial?.end_time).diff(
        //             moment(attendanceDetial?.start_time)
        //           )
        //         )
        //         .hours() + ' hrs'
        //     : '0 hrs',

        // isToday: isToday ? 'Today' : 'Not Today', // Add custom field to check if it's today
      };
    });
  return (
    <>
      <div className="grid gap-4 col-span-12 xl:col-span-7 bg-white rounded-lg shadow-none border-none h-full">
        <Card className="h-full shadow-none border-none ">
          <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 py-2 2xl:h-12 min-h-10 w-full">
            <CardTitle className="text-xs 2xl:text-sm font-bold text-white flex items-center gap-2 justify-between w-full">
              <div className="left_content flex gap-2 justify-center items-center">
                Attendance
              </div>
              <div className="right_content flex justify-center items-center gap-3">
                {/* <div className="dashboard_design flex gap-1 px-2 py-1 bg-white border-2 rounded-sm border-white">
                  <img src={ReportDashboardIcon} className="h-4 w-4" alt="" />
                  <span className="text-xs  text-[#6A6AEC]">Generate Report</span>
                </div> */}
                <DashboardPopoverAttendance
                  clientsDetail={clientsDetail}
                  exportPDFattendanceReport={exportPDFattendanceReport}
                />
                {/* <MoveDiagonal className="cursor-pointer h-6 w-6" /> */}
              </div>
            </CardTitle>
          </CardHeader>
          <CardContent className=" py-3 w-full flex flex-col h-full">
            {/* <div className="flex justify-end w-full items-center gap-2"></div> */}
            {/* <div className="flex flex-col h-full"> */}
            <AttendanceVennDiagram
              filter={filter}
              attendanceGraphDeatils={attendanceGraphDeatils}
            />
            <EmployeeAttendanceTable
              attendanceDetials={attendanceDetials}
              isLoading={isLoading}
            />
            {/* </div> */}
          </CardContent>
        </Card>
      </div>
    </>
  );
};

export default AttendanceReport;
