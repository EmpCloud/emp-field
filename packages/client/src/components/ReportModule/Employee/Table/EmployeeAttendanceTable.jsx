import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  createColumnHelper,
  flexRender,
  getCoreRowModel,
  getPaginationRowModel,
  useReactTable,
} from '@tanstack/react-table';
import ChartContext from 'components/ChartContext/Context';
import { id } from 'date-fns/locale';
import moment from 'moment';

import React, { useContext, useEffect, useRef } from 'react';
import { date } from 'yup';

const columnHelper = createColumnHelper();

const EmployeeAttendanceTable = ({ attendanceDetials, isLoading }) => {
  const { attendanceTable, setattendanceTable } = useContext(ChartContext);
  const attendancetbref = useRef('');
  // Define columns
  const columns = [
    columnHelper.accessor('date', {
      header: 'Date',
      cell: info => (
        <span className="whitespace-nowrap">{info.getValue()}</span>
      ),
      footer: info => info.column.id,
    }),
    columnHelper.accessor('status', {
      header: 'Status',
      cell: info => (
        <span
          className={`py-1 rounded capitalize ${
            info.getValue() === 'present' ? 'text-green-500' : 'text-red-500'
          }`}>
          {info.getValue()}
        </span>
      ),
      footer: info => info.column.id,
    }),
    columnHelper.accessor('checkIn', {
      header: 'Check In',
      cell: info => (
        <span className="whitespace-nowrap">{info.getValue()}</span>
      ),
      footer: info => info.column.id,
    }),
    columnHelper.accessor('checkOut', {
      header: 'Check Out',
      footer: info => info.column.id,
    }),
    columnHelper.accessor('requestStatus', {
      header: 'Request Status',
      footer: info => <span className={'text-center'}>{info.getValue()}</span>,
    }),
    columnHelper.accessor('overriddenBy', {
      header: 'Overridden By',
      footer: info => <span className={'text-center'}>{info.getValue()}</span>,
    }),
    columnHelper.accessor('total', {
      header: 'Total',
      footer: info => info.column.id,
    }),
  ];
  const handleexportimg = async () => {
    const chartelm = chartRef.current;
    const canvas = await html2canvas(chartelm);
    const imgdata = canvas.toDataURL('image/png');
    setbaseimg(imgdata);
  };

  const timestamp = '2024-08-20T13:45:00.000Z';

  // Convert the timestamp to a JavaScript Date object
  const date = new Date(timestamp);

  // Format the time (hours and minutes)
  const formattedTime = date.toLocaleTimeString('en-US', {
    hour: '2-digit',
    minute: '2-digit',
    hour12: true, // For 12-hour format (AM/PM)
  });

  // const clientsDetail =
  //   attendanceDetials !== 'No Attendance found' &&
  //   attendanceDetials?.map(attendanceDetial => {
  //     return {
  //       date: moment(attendanceDetial?.date).format('YYYY-MM-DD'),
  //       status:
  //         attendanceDetial?.leave_name == 'Unpaid'
  //           ? attendanceDetial?.day_off == false
  //             ? 'Week Off '
  //             : attendanceDetial?.start_time && attendanceDetial?.end_time
  //               ? 'Present'
  //               : 'absent'
  //           : attendanceDetial?.leave_name,
  //       checkIn: attendanceDetial?.start_time
  //         ? moment(attendanceDetial?.start_time).format('hh:mm A')
  //         : '-',
  //       checkOut: attendanceDetial?.end_time
  //         ? moment(attendanceDetial?.end_time).format('hh:mm A')
  //         : '-',
  //       requestStatus:
  //         attendanceDetial?.open_attendance_request !== null &&
  //         attendanceDetial?.open_attendance_request?.request_status == '0'
  //           ? 'Pending'
  //           : attendanceDetial?.open_attendance_request?.request_status == '1'
  //             ? 'Approved'
  //             : attendanceDetial?.open_attendance_request?.request_status == '0'
  //               ? 'Pending'
  //               : attendanceDetial?.open_attendance_request?.request_status ==
  //                   '2'
  //                 ? 'Rejected'
  //                 : '--',
  //       overriddenBy: '-',

  //       total:
  //         attendanceDetial?.start_time && attendanceDetial?.end_time
  //           ? moment.duration(
  //               moment(attendanceDetial?.end_time).diff(
  //                 moment(attendanceDetial?.start_time)
  //               )
  //             )

  //           : '0 hrs',

  //     };

  //   });
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

  // Sample data
  const clientsDetails = [
    {
      date: '2024-08-27',
      status: 'absent',
      checkIn: '-',
      checkOut: '-',
      requestStatus: '-',
      overriddenBy: '-',
      total: '-',
    },
    {
      date: '2024-08-27',
      status: 'absent',
      checkIn: '-',
      checkOut: '-',
      requestStatus: '-',
      overriddenBy: '-',
      total: '-',
    },
    {
      date: '2024-08-27',
      status: 'absent',
      checkIn: '-',
      checkOut: '-',
      requestStatus: '-',
      overriddenBy: '-',
      total: '-',
    },
    {
      date: '2024-08-26',
      status: 'present',
      checkIn: '09:00 AM',
      checkOut: '06:00 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
    {
      date: '2024-08-25',
      status: 'absent',
      checkIn: '-',
      checkOut: '-',
      requestStatus: '-',
      overriddenBy: '-',
      total: '-',
    },
    {
      date: '2024-08-24',
      status: 'present',
      checkIn: '07:30 AM',
      checkOut: '04:30 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
    {
      date: '2024-08-24',
      status: 'present',
      checkIn: '07:30 AM',
      checkOut: '04:30 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
    {
      date: '2024-08-24',
      status: 'present',
      checkIn: '07:30 AM',
      checkOut: '04:30 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
    {
      date: '2024-08-24',
      status: 'present',
      checkIn: '07:30 AM',
      checkOut: '04:30 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
    {
      date: '2024-08-24',
      status: 'present',
      checkIn: '07:30 AM',
      checkOut: '04:30 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
    {
      date: '2024-08-24',
      status: 'present',
      checkIn: '07:30 AM',
      checkOut: '04:30 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
    {
      date: '2024-08-24',
      status: 'present',
      checkIn: '07:30 AM',
      checkOut: '04:30 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
    {
      date: '2024-08-23',
      status: 'absent',
      checkIn: '-',
      checkOut: '-',
      requestStatus: '-',
      overriddenBy: '-',
      total: '-',
    },
    {
      date: '2024-08-22',
      status: 'present',
      checkIn: '08:30 AM',
      checkOut: '05:30 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
    {
      date: '2024-08-21',
      status: 'present',
      checkIn: '09:15 AM',
      checkOut: '06:15 PM',
      requestStatus: '-',
      overriddenBy: '-',
      total: '09:00',
    },
  ];

  const table = useReactTable({
    data: clientsDetail || [],
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

  useEffect(() => {
    if (clientsDetail) {
      setattendanceTable(clientsDetail);
    }
  }, []);

  return (
    <div className="grid gap-4  grid-cols-12 col-span-12 bg-white rounded-lg max-h-[390px] pb-5 ">
      <div className="col-span-12 grid gap-x-1 max-h-[325px]">
        <Table className="w-full h-full !overflow-x-auto !overflow-y-hidden">
          <TableHeader className="!sticky top-0 h-[64px]">
            {table.getHeaderGroups().map(headerGroup => (
              <TableRow key={headerGroup.id} className="bg-[#F1F1FF]">
                {headerGroup.headers.map(header => {
                  return (
                    <TableHead
                      className="whitespace-nowrap px-3 h-[64px]"
                      key={header.id}>
                      {header.isPlaceholder ? null : (
                        <div className="flex items-center font-bold h-9 text-[#1F3A78]">
                          {/* Render the header content */}
                          {flexRender(
                            header.column.columnDef.header,
                            header.getContext()
                          )}
                        </div>
                      )}
                    </TableHead>
                  );
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody className="h-[400px] overflow-auto">
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map(row => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && 'selected'}>
                  {row.getVisibleCells().map(cell => (
                    <TableCell
                      key={cell.id}
                      className="max-w-40 py-2 px-3 text-[#4D4C4C] text-xs font-medium text-left h-[64px]">
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell
                  colSpan={columns?.length}
                  className="h-24 text-center">
                  {isLoading ? '   loading....' : 'No Attendance found'}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
    </div>
  );
};

export default EmployeeAttendanceTable;
