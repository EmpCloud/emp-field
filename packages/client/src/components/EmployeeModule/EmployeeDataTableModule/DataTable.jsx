import * as React from 'react';

import {
  createColumnHelper,
  flexRender,
  getCoreRowModel,
  getPaginationRowModel,
  useReactTable,
} from '@tanstack/react-table';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import TableAction from './TableAction';
import TableDetail from './TableDetails';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { ChevronDown, Search, Upload } from 'lucide-react';
import { useInfiniteQuery, useQuery } from '@tanstack/react-query';
import { employeeSearchedKeyWord } from './Api/post';
import { handleEmpolyeeReport } from 'components/Helpers/ReportPdf';
import { useState } from 'react';

import {
  RiArrowDropLeftLine,
  RiArrowLeftDoubleFill,
  RiArrowDropRightLine,
  RiArrowRightDoubleFill,
} from 'react-icons/ri';
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import AddNewTaskModal from 'components/UIElements/Modals/AddNewTaskModal';
import addTaskIcon from 'assets/images/add-task.png';
import { Modal } from 'components/Modal';

const columnHelper = createColumnHelper();

const DataTable = ({
  date,
  employeeId,
  clientId,
  task,
  selectedRole,
  selectedLocation,
  selectedDepartment,
  deletedUsers,
  activeEmployees,
}) => {
  const [searchEmployeeKeyBoard, setSearchEmplyeeKeyboard] = React.useState('');
  const [pageSize, setPageSize] = useState(10);
  const [pageIndex, setPageIndex] = useState(0);
  const [taskSearch, setTaskSearch] = useState('');
  const [taskDetail, setTaskDetails] = useState('');
  const [totalTaskCount, setTotalTaskCount] = useState(0);

  let columns = [
    columnHelper.accessor('fullName', {
      header: 'Full Name',
      cell: info => info.getValue(),
    }),
    columnHelper.accessor('emailId', {
      header: 'Email Id',
    }),
    columnHelper.accessor('location', {
      header: 'Location',
    }),
    columnHelper.accessor('department', {
      header: 'Department',
    }),
    columnHelper.accessor('role', {
      header: 'Role',
    }),
    columnHelper.accessor('empCode', {
      header: 'EMP-Code',
    }),
    columnHelper.accessor('action', {
      header: 'Action',
      cell: ({ row }) => (
        <TableAction
          row={row.original}
          deletedUsers={deletedUsers}
          refetchEmployess={refetch}
          // handleGetTaskDetails={|}
        />
      ),
    }),
  ];

  const {
    data: employeeData,
    error,
    fetchNextPage,
    hasNextPage,
    isFetching,
    isFetchingNextPage,
    status,
    isLoading,
    refetch,
  } = useInfiniteQuery({
    queryKey: [
      'fetchEmpUsers',
      searchEmployeeKeyBoard,
      selectedRole,
      selectedLocation,
      selectedDepartment,
      deletedUsers,
      pageSize,
    ],
    queryFn: ({ pageParam = 0 }) =>
      // selectedRole === 'All'
      //   ? employeeSearchedKeyWord(searchEmployeeKeyBoard, deletedUsers, {
      //       pageParam,
      //       limit: pageSize,
      //       totalTaskCount,
      //     })
      //   :
      employeeSearchedKeyWord(
        searchEmployeeKeyBoard,
        selectedRole,
        selectedLocation,
        selectedDepartment,
        deletedUsers,
        { pageParam, limit: pageSize }
      ),
    getNextPageParam: (lastPage, pages) => {
      if (lastPage?.body?.data?.allUsers?.length < pageSize) {
        return undefined; // No more pages
      }
      return pages.length * pageSize; // Fetch next page
    },
  });
  const newData = React.useMemo(
    () => employeeData?.pages.flatMap(page => page?.body?.data?.allUsers) || [],
    [employeeData]
  );

  let newTableData = React.useMemo(() => {
    return newData
      ? newData.map(employeeDetail => ({
          fullName: employeeDetail?.fullName,
          emailId: employeeDetail?.email,
          location: employeeDetail?.location,
          department: employeeDetail?.department,
          role: employeeDetail?.role,
          empCode: employeeDetail?.emp_id,
          isGeoFencingOn: employeeDetail?.isGeoFencingOn,
          _id: employeeDetail?._id,
          emp_id: employeeDetail?.emp_id,
        }))
      : [];
  }, [newData]);

  const table = useReactTable({
    data: newTableData, // Use the correct variable here
    columns,
    initialState: { pagination: { pageIndex, pageSize } },
    state: { pagination: { pageIndex, pageSize } },
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });
  const totalCount = employeeData?.pages[0]?.body?.data?.totalCount || 0;
  const totalPages = Math.ceil(totalCount / pageSize);
  // Map combined data to the format required by autoTable

  const headers = [['Full Name', 'Email', 'Role', 'Location', 'Emp-Code']];

  const employeePdfDownloadTitle = 'Employee Details Report';
  const handlePageChange = newPageIndex => {
    setPageIndex(newPageIndex);
  };

  React.useEffect(() => {
    if (pageIndex > 0 && hasNextPage) {
      fetchNextPage();
    }
  }, [pageIndex, fetchNextPage, hasNextPage]);

  return (
    <div className="col-span-12 grid gap-3">
      <div className="grid grid-cols-12 gap-4 items-center">
        <div className="col-span-8 sm:col-span-4 bg-slate-400/10 border-none px-4 relative rounded-md">
          <Input
            placeholder="Search..."
            value={
              task == 'task'
                ? table.getColumn('task')?.getFilterValue()
                : (table.getColumn('fullName')?.getFilterValue() ?? '')
            }
            className="bg-transparent"
            onChange={event => {
              task == 'task'
                ? setTaskSearch(event.target.value)
                : setSearchEmplyeeKeyboard(event.target.value);
              task == 'task'
                ? table.getColumn('task')?.setFilterValue(event.target.value)
                : table
                    .getColumn('fullName')
                    ?.setFilterValue(event.target.value);
            }}
          />
          <Search className="absolute top-2/4 right-2 -translate-y-2/4 w-4 h-4 2xl:w-5 2xl:h-5" />
        </div>

        {/* <Button
            className="col-span-4 sm:col-span-2 flex gap-3 bg-violet-500 hover:bg-violet-500/80"
            onClick={() =>
              handleEmpolyeeReport(
                headers,
                formattedData,
                employeePdfDownloadTitle
              )
            }>
            <Upload className="w-4 h-4 2xl:w-5 2xl:h-5" />
            Export
          </Button> */}

        {/* <DropdownMenu className="col-start-9 col-span-2">
          <DropdownMenuTrigger asChild>
            <Button variant="outline" className="ml-auto">
              Columns <ChevronDown className="ml-2 h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            {table
              .getAllColumns()
              .filter(column => column.getCanHide())
              .map(column => {
                return (
                  <DropdownMenuCheckboxItem
                    key={column.id}
                    className="capitalize"
                    checked={column.getIsVisible()}
                    onCheckedChange={value => column.toggleVisibility(!!value)}>
                    {column.id}
                  </DropdownMenuCheckboxItem>
                );
              })}
          </DropdownMenuContent>
        </DropdownMenu> */}

        <DropdownMenu>
          <div className="col-start-10 col-span-3 flex justify-end gap-2 items-center text-[10px]">
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
              <DropdownMenuCheckboxItem
                onClick={() => {
                  setPageSize(30);
                  setPageIndex(0);
                }}
                className="text-[10px] 2xl:text-sm">
                30
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
          {isLoading || isFetching ? (
            <TableRow>
              <TableCell colSpan={columns?.length} className="h-24 text-center">
                Loading...
              </TableCell>
            </TableRow>
          ) : newTableData?.length > 0 ? (
            table.getRowModel().rows.map(row => (
              <TableRow
                key={row.id}
                data-state={row.getIsSelected() && 'selected'}>
                {row.getVisibleCells().map(cell => (
                  <TableCell key={cell.id}>
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </TableCell>
                ))}
              </TableRow>
            ))
          ) : (
            <TableRow>
              <TableCell colSpan={columns?.length} className="h-24 text-center">
                {task == 'task'
                  ? (taskDetail ?? 'No task found')
                  : isLoading
                    ? 'loading....'
                    : 'No employees found'}
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>

      <div className="flex flex-wrap gap-3 items-center justify-between w-full">
        <div className="text-[10px] 2xl:text-sm">
          Showing {pageIndex * pageSize + 1} to{' '}
          {pageIndex * pageSize + table.getRowModel().rows.length} of{' '}
          {totalCount} entries
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
              {pageIndex + 1} of {totalPages}
            </span>
          </div>
          <button
            className="pagination-button"
            onClick={() => handlePageChange(pageIndex + 1)}
            disabled={pageIndex >= totalPages - 1}>
            <RiArrowDropRightLine />
          </button>
          {/* <button
            className="pagination-button"
            onClick={() => handlePageChange(totalPages - 1)}
            disabled={pageIndex >= totalPages - 1}>
            <RiArrowRightDoubleFill />
          </button> */}
        </div>
      </div>
    </div>
  );
};

export default DataTable;
