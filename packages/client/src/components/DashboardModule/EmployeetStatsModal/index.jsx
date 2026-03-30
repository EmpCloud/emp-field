import React, { useState, useMemo, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Checkbox } from '@/components/ui/checkbox';
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
import { Search } from 'lucide-react';
import { useQuery } from '@tanstack/react-query';
import { fetchEmpUsers } from 'components/UIElements/Modals/Api/get';
import {
  RiArrowDropLeftLine,
  RiArrowDropRightLine,
  RiArrowLeftDoubleFill,
  RiArrowRightDoubleFill,
} from 'react-icons/ri';
import { getEmployeeStatusDetails } from './Api/post';

const EmployeeStatsModal = ({ selectedItem }) => {
  const [searchValue, setSearchValue] = useState('');
  const [pageSize, setPageSize] = useState(10);
  const [pageIndex, setPageIndex] = useState(0);

  const {
    data: employeeData,
    error,
    isFetching,
    isLoading,
  } = useQuery({
    queryKey: [
      'getEmployeeStatusDetails',
      pageSize,
      selectedItem?.apiQuery,
      searchValue,
      pageIndex,
    ],
    queryFn: () =>
      getEmployeeStatusDetails(
        selectedItem?.apiQuery,
        searchValue,
        pageIndex,
        pageSize
      ),
    keepPreviousData: true,
  });

  const filteredData = useMemo(
    () => employeeData?.data?.body?.data?.employees || [],
    [employeeData]
  );

  const totalCount = useMemo(
    () => employeeData?.data?.body?.data?.totalCount || 0,
    [employeeData]
  );

  const totalPages = useMemo(
    () => Math.ceil(totalCount / pageSize) || 1,
    [totalCount, pageSize]
  );

  const columnHelper = createColumnHelper();

  const columns = useMemo(
    () => [
      // columnHelper.accessor('checkbox', {
      //   header: ({ table }) => (
      //     <Checkbox
      //       checked={
      //         table.getIsAllPageRowsSelected() ||
      //         (table.getIsSomePageRowsSelected() && 'indeterminate')
      //       }
      //       onCheckedChange={value => table.toggleAllPageRowsSelected(!!value)}
      //       aria-label="Select all"
      //     />
      //   ),
      //   cell: ({ row }) => (
      //     <Checkbox
      //       checked={row.getIsSelected()}
      //       onCheckedChange={value =>
      //         !row.original.importedStatus && row.toggleSelected(!!value)
      //       }
      //       aria-label="Select row"
      //       disabled={row.original.importedStatus}
      //     />
      //   ),
      // }),
      columnHelper.accessor('sNo', {
        header: 'S. No.',
        cell: info => pageIndex * pageSize + info.row.index + 1,
      }),
      columnHelper.accessor('email', {
        header: 'Email Id',
      }),
      columnHelper.accessor('fullName', {
        header: 'Full Name',
      }),
      columnHelper.accessor('emp_id', {
        header: 'Emp Id',
      }),
      columnHelper.accessor('department', {
        header: 'Department',
      }),
    ],
    [columnHelper, pageIndex, pageSize]
  );

  const table = useReactTable({
    data: filteredData,
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

  const handlePageSizeChange = event => {
    setPageSize(Number(event.target.value));
    setPageIndex(0);
  };
  const handlePageChange = newPageIndex => {
    if (newPageIndex >= 0 && newPageIndex < totalPages) {
      setPageIndex(newPageIndex);
    }
  };

  return (
    <div className="grid gap-4 grid-cols-12 col-span-12 px-2 md:px-6 2xl:p-6 overflow-auto lg:h-[300px] lg:max-h-[300px] 2xl:h-[450px] 2xl:max-h-[450px]">
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6"}`}>
        <CardHeader>
          <CardTitle className="font-bold text-xs 2xl:text-lg">
            Search Employee
          </CardTitle>
        </CardHeader>
        <CardContent className="py-4 flex flex-col justify-start items-start">
          <div className="flex flex-wrap md:flex-row justify-between items-start w-full gap-3">
            <div className="bg-slate-400/10 border-none px-3 py-2 relative rounded-md w-full lg:w-[300px]">
              <Input
                placeholder="Search Employee..."
                value={searchValue}
                className="bg-transparent text-sm h-[30px]" // Adjust the height
                onChange={event => setSearchValue(event.target.value)}
              />
              <Search className="absolute top-2/4 right-2 -translate-y-2/4 w-4 h-4" />
            </div>

            <div
              className={`w-full lg:w-[200px] ${selectedItem?.accent} text-white py-4 rounded text-center`}>
              <h2 className="font-bold text-sm 2xl:text-2xl">{totalCount}</h2>
              <p className="text-xs 2xl:text-base">
                {selectedItem?.name ?? ''}
              </p>
            </div>
          </div>
          <div className="flex items-center gap-2 ml-auto mt-5">
            <span className="text-xs 2xl:text-sm">Show</span>
            <select
              value={pageSize}
              onChange={handlePageSizeChange}
              className="bg-transparent border rounded-md px-2 py-1 text-xs 2xl:text-sm">
              <option value={10}>10</option>
              <option value={50}>50</option>
              <option value={100}>100</option>
            </select>
            <span className="text-xs 2xl:text-sm">Entries</span>
          </div>
          <Table className="my-3">
            <TableHeader>
              {table.getHeaderGroups().map(headerGroup => (
                <TableRow key={headerGroup.id}>
                  {headerGroup.headers.map(header => (
                    <TableHead
                      className="text-xs whitespace-nowrap"
                      key={header.id}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(
                            header.column.columnDef.header,
                            header.getContext()
                          )}
                    </TableHead>
                  ))}
                </TableRow>
              ))}
            </TableHeader>
            <TableBody>
              {isLoading || isFetching ? (
                <TableRow>
                  <TableCell
                    colSpan={columns.length}
                    className="h-24 text-center">
                    Loading...
                  </TableCell>
                </TableRow>
              ) : filteredData.length > 0 ? (
                table.getRowModel().rows.map(row => (
                  <TableRow
                    key={row.id}
                    data-state={row.getIsSelected() && 'selected'}>
                    {row.getVisibleCells().map(cell => (
                      <TableCell className="text-xs" key={cell.id}>
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
                    colSpan={columns.length}
                    className="h-24 text-center text-xs">
                    No data found
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>

          <div className="flex flex-wrap gap-3 items-center justify-between w-full">
            <div className="text-[10px] 2xl:text-sm">
              {/* {table.getFilteredSelectedRowModel().rows.length} of{' '}
              {table.getFilteredRowModel().rows.length} row(s) selected. */}
              Showing {pageIndex * pageSize + 1} to{' '}
              {Math.min((pageIndex + 1) * pageSize, totalCount)} of {totalCount}{' '}
              entries
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
                Page {pageIndex + 1} of {totalPages}
              </div>
              {/* <button
                className="pagination-button"
                onClick={() => {
                  setPageIndex(pageIndex + 1);
                  fetchNextPage();
                }}
                disabled={!hasNextPage || isFetchingNextPage}>
                <RiArrowDropRightLine />
              </button>
              <button
className="pagination-button"
                onClick={() => setPageIndex(table.getPageCount() - 1)}
                disabled={!hasNextPage}>
                <RiArrowRightDoubleFill />
              </button> */}
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
        </CardContent>
      </Card>
    </div>
  );
};

export default EmployeeStatsModal;
