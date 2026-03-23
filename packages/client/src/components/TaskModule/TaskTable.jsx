import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Input } from '@/components/ui/input';
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
import { ChevronDown, Search } from 'lucide-react';
import React, { useState } from 'react';
import {
  RiArrowDropLeftLine,
  RiArrowDropRightLine,
  RiArrowLeftDoubleFill,
  RiArrowRightDoubleFill,
} from 'react-icons/ri';
import TaskTableAction from './TaskTableAction';
import addTaskIcon from 'assets/images/add-task.png';
import { Modal } from 'components/Modal';
import AddNewTaskModal from 'components/UIElements/Modals/AddNewTaskModal';
import { getTaskDetails } from './Api/get';
import moment from 'moment';
import { skipToken, useQuery } from '@tanstack/react-query';

const columnHelper = createColumnHelper();

const statusColors = {
  Completed: 'text-[#4DB948]',
  Inprogress: 'text-[#6A6AEC]',
};

export function getStatusDescription(status) {
  switch (status) {
    case 0:
      return 'Pending';
    case 1:
      return 'Start';
    case 2:
      return 'Pause';
    case 3:
      return 'Resume';
    case 4:
      return 'Finish';
    case 5:
      return 'Delete';
    default:
      return 'Unknown Status';
  }
}

const TaskTable = ({ employeeId, clientId, date }) => {
  const [searchEmployeeKeyBoard, setSearchEmplyeeKeyboard] = React.useState('');
  const [pageSize, setPageSize] = React.useState(10);
  const [pageIndex, setPageIndex] = React.useState(0);
  const [taskSearch, setTaskSearch] = useState('');

  // const handleGetTaskDetails = () => {
  //   getTaskDetails(
  //     taskSearch,
  //     employeeId,
  //     clientId,
  //     moment(date == null ? new Date() : date).format('YYYY-MM-DD'),
  //   ).then(response => {
  //     setTaskDetails(response?.body?.data?.data);
  //     setTotalTaskCount(response?.body?.data?.totalCount);
  //   });
  // };

  const responseData = useQuery({
    queryKey: [
      'getTaskDetails',
      taskSearch,
      employeeId,
      clientId,
      pageSize,
      pageIndex,
      moment(date == null ? new Date() : date).format('YYYY-MM-DD'),
    ],
    queryFn: () =>
      getTaskDetails(
        taskSearch,
        employeeId,
        clientId,
        moment(date == null ? new Date() : date).format('YYYY-MM-DD'),
        pageSize,
        pageIndex * pageSize
      ),
    keepPreviousData: true,
    refetchOnWindowFocus: false,
  });

  //   React.useEffect(() => {
  //     handleGetTaskDetails();
  //   }, [taskSearch, employeeId, clientId, date,pageIndex,pageSize]);
  const taskDetails = responseData?.data?.body?.data?.data;
  const totalTaskCount = responseData?.data?.body?.data?.totalCount;
  const totalPages = Math.ceil(totalTaskCount / pageSize);

  let FilteredEmployDetails;
  FilteredEmployDetails = React.useMemo(() => {
    return taskDetails
      ? // taskDetail
        taskDetails.map(task => ({
          taskId: task?._id,
          task: task?.taskName,
          taskStage: task?.tagLogs[0]?.tagName ?? '---',
          taskDate: task?.Date,
          taskDiscription: task?.taskDescription,
          taskVolume: task?.taskVolume,
          amount: task?.value?.amount,
          currency: task?.value?.currency,
          clientName: task?.clientName,
          time: `${task?.start_time != null ? moment(task?.start_time).format('hh:mm A') : '-'}- ${task?.end_time !== null ? moment(task?.end_time).format('hh:mm A') : '-'}`,
          assignedBy: task?.assignedBy,
          employeeName: task?.employeeName,
          files: task?.files,
          images: task?.images,
          status: getStatusDescription(task?.taskApproveStatus),
          date: task?.date,
          start_time: task?.start_time,
          end_time: task?.end_time,
          employeeId: task?.emp_id,
          clientId: task?.clientId,
          recurrenceId: task?.recurrenceId,
          recurrenceDetails: {
            daysOfWeek: task?.recurrenceDetails?.daysOfWeek,
            TaskCycle: task?.recurrenceDetails?.TaskCycle,
            endDate: task?.recurrenceDetails?.endDate,
            startDate: task?.recurrenceDetails?.startDate,
            recurrenceDetailsId: task?.recurrenceDetails?._id,
          },
        }))
      : [];
  }, [taskDetails]);

  const columns = [
    columnHelper.accessor('task', {
      header: 'Task',
    }),
    columnHelper.accessor('employeeName', {
      header: 'Employee Name',
      cell: info => info.getValue(),
    }),
    columnHelper.accessor('clientName', {
      header: 'Client Name',
    }),
    columnHelper.accessor('time', {
      header: 'Time',
      cell: info => {
        const time = info.getValue();

        return (
          <span className="text-[#1470C6] w-24 lg:w-36 block">{time}</span>
        );
      },
    }),
    columnHelper.accessor('taskStage', {
      header: 'Stage',
    }),
    // columnHelper.accessor('assignedBy', {
    //   header: 'Assigned By',
    // }),
    columnHelper.accessor('status', {
      header: 'Status',
      cell: info => {
        const status = info.getValue();
        const colorClass = statusColors[status] || 'text-gray-500';

        return <span className={colorClass}>{status}</span>;
      },
    }),
    columnHelper.accessor('action', {
      header: 'Action',
      cell: ({ row }) => (
        <TaskTableAction row={row.original} responseData={responseData} />
      ),
    }),
  ];

  const table = useReactTable({
    data: FilteredEmployDetails,
    columns,
    pageCount: totalPages,
    state: { pagination: { pageIndex, pageSize } },
    onPaginationChange: updater => {
      const newState =
        typeof updater === 'function'
          ? updater({ pageIndex, pageSize })
          : updater;
      setPageIndex(newState.pageIndex);
      setPageSize(newState.pageSize);
    },
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    manualPagination: true, // Enable manual pagination
  });

  const handlePageChange = newPageIndex => {
    table.setPageIndex(newPageIndex);
  };

  return (
    <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-3 items-center">
      <div className="col-span-12 grid gap-3">
        <div className="grid grid-cols-12 gap-4 items-center">
          <div className="col-span-8 sm:col-span-4 bg-slate-400/10 border-none px-4 relative rounded-md">
            <Input
              placeholder="Search Task..."
              value={taskSearch}
              className="bg-transparent"
              onChange={event => {
                setTaskSearch(event.target.value);
                table
                  .getColumn('employeeName')
                  ?.setFilterValue(event.target.value);
              }}
            />
            <Search className="absolute top-2/4 right-2 -translate-y-2/4 w-4 h-4 2xl:w-5 2xl:h-5" />
          </div>
          {/* <DropdownMenu>
            <div className="col-span-4 lg:col-span-1 flex justify-center gap-2 items-center text-[10px]">
              <DropdownMenuTrigger asChild>
                <Button variant="outline">
                  All <ChevronDown className="ml-2 h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="center" className="p-1">
                {columns.map(column => (
                  <DropdownMenuCheckboxItem
                    key={column.id}
                    className="text-[10px] 2xl:text-sm">
                    {column.header}
                  </DropdownMenuCheckboxItem>
                ))}
              </DropdownMenuContent>
            </div>
          </DropdownMenu> */}

          <Modal
            triggerText={'Add New Task'}
            title={'Add New Task'}
            buttonStyles={
              'col-span-6 sm:col-span-2 flex gap-3 text-[#1F3A78] border border-[#1F3A78] bg-transparent hover:bg-[#1F3A78]/10'
            }
            titleStyles={'text-center'}
            triggerButtonIcon={
              <img className="w-4 h-4 2xl:w-5 2xl:h-5" src={addTaskIcon} />
            }>
            <AddNewTaskModal responseData={responseData} />
            {/* <AddNewTaskModal /> */}
          </Modal>

          <DropdownMenu>
            <div className="lg:col-start-10 col-span-6 lg:col-span-3 flex justify-end gap-2 items-center text-[10px]">
              <span>Show</span>
              <DropdownMenuTrigger asChild className="">
                <Button variant="outline">
                  {pageSize} <ChevronDown className="ml-2 h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <span>Entries</span>
              <DropdownMenuContent align="center p-1">
                <DropdownMenuCheckboxItem
                  onClick={() => {
                    setPageSize(10);
                    setPageIndex(0);
                  }}
                  className="text-[10px] 2xl:text-sm">
                  10
                </DropdownMenuCheckboxItem>
                <DropdownMenuCheckboxItem
                  onClick={() => {
                    setPageSize(20);
                    setPageIndex(0);
                  }}
                  className="text-[10px] 2xl:text-sm">
                  20
                </DropdownMenuCheckboxItem>
              </DropdownMenuContent>
            </div>
          </DropdownMenu>
        </div>
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map(headerGroup => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map(header => {
                  return (
                    <TableHead className="whitespace-nowrap" key={header.id}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(
                            header.column.columnDef.header,
                            header.getContext()
                          )}
                    </TableHead>
                  );
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map(row => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && 'selected'}>
                  {row.getVisibleCells().map(cell => (
                    <TableCell key={cell.id} className="max-w-40 break-words">
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
                  {responseData.isLoading
                    ? 'Loading...'
                    : (taskDetails ?? 'No task found')}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
        <div className="flex items-center justify-between">
          <div className="text-[10px] 2xl:text-sm">
            {/* {table.getFilteredSelectedRowModel().rows.length} of{' '}
            {table.getFilteredRowModel().rows.length} row(s) selected. */}
          </div>
          <div className="flex gap-1">
            <button
              className="pagination-button"
              onClick={() => handlePageChange(0)}
              disabled={!table.getCanPreviousPage()}>
              <RiArrowLeftDoubleFill />
            </button>

            <button
              className="pagination-button"
              onClick={() => handlePageChange(pageIndex - 1)}
              disabled={!table.getCanPreviousPage()}>
              <RiArrowDropLeftLine />
            </button>
            <div className="flex gap-1 text-[10px] 2xl:text-sm justify-center items-center bg-[#F1F1FF] px-4 rounded-sm">
              Page
              <span>
                {pageIndex + 1} of {Boolean(totalPages) ? totalPages : 1}
              </span>
            </div>
            <button
              className="pagination-button"
              onClick={() => handlePageChange(pageIndex + 1)}
              disabled={pageIndex >= totalPages - 1 || !Boolean(totalPages)}>
              <RiArrowDropRightLine />
            </button>
            <button
              className="pagination-button"
              onClick={() => handlePageChange(totalPages - 1)}
              disabled={pageIndex >= totalPages - 1 || !Boolean(totalPages)}>
              <RiArrowRightDoubleFill />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TaskTable;
