import React, { useEffect, useMemo, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
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
  useReactTable,
} from '@tanstack/react-table';
import { ChevronDown, ChevronUp, Search } from 'lucide-react';
import {
  RiArrowDropLeftLine,
  RiArrowDropRightLine,
  RiArrowLeftDoubleFill,
  RiArrowRightDoubleFill,
} from 'react-icons/ri';
import { Checkbox } from '@/components/ui/checkbox';
import ReportTableAction from './ReportTableAction';
import { getconsolidatedReports } from './Api/post';
import ReportFilter from './ReportFilter';
import { useNavigate } from 'react-router-dom';
import jsPDF from 'jspdf';
import * as XLSX from 'xlsx';
import { exportCSV, exportPDF, exportXLS } from './consolidatedReportshelpers';
import moment from 'moment';

const columnHelper = createColumnHelper();
const logoBase64 = 'data:image/png;base64,...'; // your base64 string

const start = 'text-[#3DC237] bg-[#F0FFE4] border border-[#3DC237]';
const finish = 'text-[#3DC237] bg-[#F0FFE4] border border-[#3DC237]';
const resume = 'text-[#FF9500] bg-[#FFF0CB] border border-[#FF9500]';
const pause = 'text-[#FF5960] bg-[#FFDBDC] border border-[#FF5960]';

const ConsolidatedTable = () => {
  const navigate = useNavigate();
  const [searchEmployeeKeyBoard, setSearchEmplyeeKeyboard] = React.useState('');
  const [pageSize, setPageSize] = React.useState(10);
  const [pageIndex, setPageIndex] = React.useState(0);
  const [filters, setFilters] = React.useState({});
  const [sortState, setSortState] = useState({
    orderBy: 'fullName', // Default column to sort by
    sort: 'asc', // Default sort direction
  });

  const handleSortToggle = headerId => {
    const newSortDirection =
      sortState.orderBy === headerId && sortState.sort === 'asc'
        ? 'desc'
        : 'asc'; // Toggle sort direction
    setSortState({
      orderBy: headerId,
      sort: newSortDirection,
    });
    setPageIndex(0); // Reset page index to 0 when sorting changes
  };

  const [selectedFilters, setSelectedFilters] = useState(null);

  const customObject = {
    CountFilters: {
      fieldName: filters?.status?.status || '',
      minValue: parseInt(filters?.status?.range?.minValue) || 0,
      maxValue: parseInt(filters?.status?.range?.maxValue) || 0,
    },
    startDate:
      filters?.dateRange?.startDate ||
      moment().subtract(1, 'month').format('YYYY-MM-DD'),
    endDate: filters?.dateRange?.endDate || moment().format('YYYY-MM-DD'),
    location: filters?.location || '',
    role: filters?.role || '',
    department: filters?.department || '',
    exportReport: false,
  };

  const defaultPayload = { exportReport: true };
  let reports = 'reports';

  const {
    data: response,
    error,
    isLoading,
    isFetching,
    refetch,
  } = useQuery({
    queryKey: [
      'getconsolidatedReports',
      pageSize,
      sortState,
      pageIndex, // Keep pageIndex as part of the query key
      searchEmployeeKeyBoard,
      customObject,
    ],
    queryFn: () =>
      getconsolidatedReports({
        pageParam: pageIndex * pageSize,
        limit: pageSize,
        orderBy: sortState.orderBy, // Ensure sorting is respected
        sort: sortState.sort,
        searchQuery: searchEmployeeKeyBoard,
        data: customObject,
      }),
    keepPreviousData: true,
  });
  const reportDetails = response?.data?.body?.data?.allUsers || [];

  const handleFilterChange = newFilters => {
    setFilters(newFilters);
  };

  const handleOpenEmployeeReport = () => {
    navigate('/admin/employee-report');
  };

  const columns = [
    // columnHelper.accessor('check', {
    //   header: <Checkbox />,
    //   cell: <Checkbox />,
    // }),
    columnHelper.accessor('fullName', {
      header: 'Full Name',
      cell: info => {
        const row = info.row.original;

        return (
          <span
            className="font-bold whitespace-nowrap cursor-pointer"
            onClick={() =>
              navigate('/admin/employee-report?empId=' + row?.emp_id, {
                state: row,
              })
            }>
            {info.getValue()}
          </span>
        );
      },
    }),
    columnHelper.accessor('email', {
      header: 'Email ID',
    }),

    columnHelper.accessor('startTaskCount', {
      header: 'Tasks Started',
      cell: info => {
        const startTaskCount = info.getValue();
        return (
          <span style={{ display: 'block', textAlign: 'center' }}>
            {startTaskCount ?? 0}
          </span>
        );
      },
    }),
    columnHelper.accessor('TasksFinished', {
      header: 'Tasks Finished',
      cell: info => {
        const finishTaskCount = info.getValue();
        return (
          <span style={{ display: 'block', textAlign: 'center' }}>
            {finishTaskCount ?? 0}
          </span>
        );
      },
    }),
    columnHelper.accessor('TasksPaused', {
      header: 'Tasks Paused',
      cell: info => {
        const pauseTaskCount = info.getValue();
        return (
          <span style={{ display: 'block', textAlign: 'center' }}>
            {pauseTaskCount ?? 0}
          </span>
        );
      },
    }),
    columnHelper.accessor('TasksResumed', {
      header: 'Tasks Resumed',
      cell: info => {
        const resumeTaskCount = info.getValue();
        return (
          <span style={{ display: 'block', textAlign: 'center' }}>
            {resumeTaskCount ?? 0}
          </span>
        );
      },
    }),
    columnHelper.accessor('Clients', {
      header: 'Clients',
      cell: info => {
        const uniqueClientCount = info.getValue();
        return (
          <span style={{ display: 'block', textAlign: 'center' }}>
            {uniqueClientCount ?? 0}
          </span>
        );
      },
    }),

    columnHelper.accessor('role', {
      header: 'Role',
    }),
    columnHelper.accessor('department', {
      header: 'Department',
    }),
    columnHelper.accessor('location', {
      header: 'Location',
    }),
    columnHelper.accessor('phoneNumber', {
      header: 'Phone Number',
      cell: info => {
        const phoneNumber = info.getValue();
        return phoneNumber == '' || phoneNumber == null || phoneNumber == 'null'
          ? 'N/A'
          : phoneNumber;
      },
    }),

    columnHelper.accessor('createdAt', {
      header: 'Created At',
      cell: info => {
        return (
          <span className="whitespace-nowrap">
            {new Date(info.getValue()).toLocaleDateString()}
          </span>
        );
      },
    }),
    columnHelper.accessor('action', {
      header: 'Action',
      cell: ({ row }) => (
        <ReportTableAction
          handleOpenEmployeeReport={handleOpenEmployeeReport}
          row={row.original}
        />
      ),
    }),
  ];
  const totalCount = response?.data?.body?.data?.usersCount || 0;
  const totalPages = Math.ceil(totalCount / pageSize);

  const table = useReactTable({
    data: reportDetails || [],
    columns,
    getCoreRowModel: getCoreRowModel(),
    manualPagination: true,
  });

  const handlePageChange = newPageIndex => {
    if (newPageIndex >= 0 && newPageIndex < totalPages) {
      setPageIndex(newPageIndex);
    }
  };

  const headers = [
    [
      'Full Name',
      'Email',
      // 'Task Volume',
      'Tasks Finished',
      'Tasks Pending',
      'Tasks Paused',
      'Tasks Resumed',
      'Clients',
      'Role',
      'Department',
      'Location',
      'Created At',
    ],
  ];

  const xlsvHeaders = [
    'Full Name',
    'Email',
    // 'Task Volume',
    'Tasks Finished',
    'Tasks Pending',
    'Tasks Paused',
    'Tasks Resumed',
    'Clients',
    'Role',
    'Department',
    'Location',
    'Created At',
  ];
  return (
    <>
      <div className="grid grid-cols-12 col-span-12 w-full gap-5 mt-4">
        <ReportFilter
          exportPDF={() => exportPDF(reportDetails, selectedFilters, headers)}
          exportCSV={() =>
            exportCSV(reportDetails, headers, 'Consolidated Reports')
          }
          exportXLS={() =>
            exportXLS(reportDetails, xlsvHeaders, 'Consolidated Reports')
          }
          setSelectedFilters={setSelectedFilters}
          onFilterChange={handleFilterChange}
        />
        <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg items-center pb-5">
          <div className="col-span-12 grid gap-1">
            <div className="grid grid-cols-12 gap-4 items-center p-5">
              <div className="col-span-12 md:col-span-6 lg:col-span-4 sm:col-span-4 bg-slate-400/10 border-none px-4 relative rounded-md">
                <Input
                  placeholder="Search..."
                  value={searchEmployeeKeyBoard}
                  className="bg-transparent py-4 placeholder:text-xs"
                  onChange={event => {
                    setSearchEmplyeeKeyboard(event.target.value);
                    //   table
                    //     .getColumn('fullName')
                    //     ?.setFilterValue(event.target.value);
                    setPageIndex(0);
                    refetch();
                  }}
                />
                <Search className="absolute top-2/4 right-2 -translate-y-2/4 w-4 h-4 2xl:w-5 2xl:h-5" />
              </div>
              <DropdownMenu>
                <div className="lg:col-start-10 col-span-12 sm:col-span-6 lg:col-span-3 flex justify-end gap-2 items-center text-[10px]">
                  <span className="font-semibold text-xs">Show</span>
                  <DropdownMenuTrigger asChild>
                    <Button variant="outline">
                      <span className="text-xs font-semibold">{pageSize}</span>{' '}
                      <ChevronDown className="ml-2 h-4 w-4" />
                    </Button>
                  </DropdownMenuTrigger>
                  <span className="font-semibold text-xs">Entries</span>
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
                  <TableRow key={headerGroup.id} className="bg-[#F1F1FF]">
                    {headerGroup.headers.map(header => {
                      return (
                        <TableHead
                          className="whitespace-nowrap px-2"
                          key={header.id}>
                          {header.isPlaceholder ? null : (
                            <div className="flex items-center font-bold h-9 text-[#1F3A78] px-2">
                              {flexRender(
                                header.column.columnDef.header,
                                header.getContext()
                              )}
                              {header.id !== 'check' && (
                                <button
                                  onClick={() => handleSortToggle(header.id)}
                                  className="ml-2">
                                  {sortState.orderBy === header.id &&
                                  sortState.sort === 'desc' ? (
                                    <ChevronUp className="w-4 h-4 cursor-pointer" />
                                  ) : (
                                    <ChevronDown className="w-4 h-4 cursor-pointer" />
                                  )}
                                </button>
                              )}
                            </div>
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
                    <TableCell
                      colSpan={columns?.length}
                      className="h-24 text-center">
                      Loading...
                    </TableCell>
                  </TableRow>
                ) : reportDetails?.length > 0 ? (
                  table.getRowModel().rows.map(row => (
                    <TableRow
                      key={row.id}
                      className="h-[42px]"
                      data-state={row.getIsSelected() && 'selected'}>
                      {row.getVisibleCells().map(cell => (
                        <TableCell
                          key={cell.id}
                          className="py-1 px-4 text-[#4D4C4C] text-xs font-medium ">
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
                      {reportDetails?.length < 1
                        ? 'No employees found'
                        : 'loading....'}
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
            <div className="flex items-center justify-between p-5">
              <div className="text-[10px] 2xl:text-sm">
                Showing {totalCount === 0 ? 0 : pageIndex * pageSize + 1} to{' '}
                {Math.min((pageIndex + 1) * pageSize, totalCount)} of{' '}
                {totalCount} entries
              </div>
              <div className="flex gap-1">
                <button
                  className="pagination-button"
                  onClick={() => handlePageChange(0)}
                  disabled={pageIndex === 0}>
                  <RiArrowLeftDoubleFill />
                </button>

                <button
                  className="pagination-button"
                  onClick={() => handlePageChange(pageIndex - 1)}
                  disabled={pageIndex === 0}>
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
                <button
                  className="pagination-button"
                  onClick={() => handlePageChange(totalPages - 1)}
                  disabled={pageIndex >= totalPages - 1}>
                  <RiArrowRightDoubleFill />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default ConsolidatedTable;
