import React, { useState, useMemo, useEffect } from 'react';
import { Button } from '@/components/ui/button';
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
  getPaginationRowModel,
  useReactTable,
} from '@tanstack/react-table';
import { Modal } from 'components/Modal';
import { Search } from 'lucide-react';
import {
  useInfiniteQuery,
  useMutation,
  useQueryClient,
} from '@tanstack/react-query';
import { fetchEmpUsers } from './Api/get';
import AlertModal from './AlertModal';
import {
  RiArrowDropLeftLine,
  RiArrowDropRightLine,
  RiArrowLeftDoubleFill,
  RiArrowRightDoubleFill,
} from 'react-icons/ri';
import { addEmpUsers } from './Api/post';
import { toast } from 'sonner';

const ImportEmployee = () => {
  const queryClient = useQueryClient();
  const handleRefetchEmployeesData = () => {
    queryClient.invalidateQueries(['repoDatas']);
  };
  const [searchValue, setSearchValue] = useState('');
  const [pageSize, setPageSize] = useState(10);
  const [pageIndex, setPageIndex] = useState(0);

  const {
    data,
    error,
    fetchNextPage,
    hasNextPage,
    isFetching,
    isFetchingNextPage,
    status,
    isLoading,
  } = useInfiniteQuery({
    queryKey: ['fetchEmpUsers', pageSize],
    queryFn: ({ pageParam = 0 }) =>
      fetchEmpUsers({ pageParam, limit: pageSize }),
    getNextPageParam: (lastPage, pages) => {
      // const totalCount = lastPage?.data?.body?.data[0]?.total_count || 0;
      const totalCount = lastPage?.data?.body?.data
        ? lastPage?.data?.body?.data[0]?.total_count
        : [] || 0;
      const totalPages = Math.ceil(totalCount / pageSize);
      if (pages.length < totalPages) {
        return pages.length * pageSize;
      }
      return undefined;
    },
  });

  const mutation = useMutation({
    mutationFn: data => addEmpUsers(data),
    onSuccess: response => {
      if (response?.data?.body?.status === 'success') {
        handleRefetchEmployeesData();
        toast.success(response ? response?.data?.body?.message : '');
      } else {
        toast.error(response ? response?.data?.body?.message : 'Try Again!');
      }
    },
    onError: error => {
      console.error('Failed to add employees', error);
    },
  });

  const allEmployeeData = data?.pages[0]?.data?.body?.data;

  const totalPages = useMemo(() => {
    if (allEmployeeData?.[0]?.total_count) {
      return Math.ceil(allEmployeeData[0].total_count / pageSize);
    }
    return 1;
  }, [allEmployeeData, pageSize]);

  const userData = useMemo(
    () => data?.pages.flatMap(page => page.data.body.data) || [],
    [data]
  );

  const employeeData = useMemo(
    () =>
      userData.map(user => ({
        id: user?.id,
        u_id: user?.u_id,
        first_name: user?.first_name,
        last_name: user?.last_name,
        email: user?.email,
        role: user?.roles[0]?.role,
        department: user?.department,
        importedStatus: user?.importedStatus,
        address: user?.address,
        phone: user?.phone,
        location: user?.location,
        department_id: user?.department_id,
        domain: user?.domain,
        emp_code: user?.emp_code,
        employee_unique_id: user?.employee_unique_id,
        encriptedpassword: user?.encriptedpassword,
        full_name: user?.full_name,
        location_id: user?.location_id,
        organization_id: user?.organization_id,
        password: user?.password,
        photo_path: user?.photo_path,
        project_name: user?.project_name,
        role_id: user?.role_id,
        role_type: user?.role_type,
        name: user?.name,
        date_join: user?.date_join,
        status: user?.status,
        shift_id: user?.shift_id,
        timezone: user?.timezone,
        tracking_mode: user?.tracking_mode,
        tracking_rule_type: user?.tracking_rule_type,
        total_count: user?.total_count,
        software_version: user?.software_version,
        shift_name: user?.shift_name,
        shift_data: user?.shift_data,
        computer_name: user?.computer_name,
        username: user?.username,
        assigned: user?.assigned,
      })),
    [userData]
  );

  const columnHelper = createColumnHelper();

  const columns = useMemo(
    () => [
      columnHelper.accessor('checkbox', {
        header: ({ table }) => (
          <Checkbox
            checked={
              table.getIsAllPageRowsSelected() ||
              (table.getIsSomePageRowsSelected() && 'indeterminate')
            }
            onCheckedChange={value => table.toggleAllPageRowsSelected(!!value)}
            aria-label="Select all"
          />
        ),
        cell: ({ row }) => (
          <Checkbox
            checked={row.getIsSelected()}
            onCheckedChange={value =>
              !row.original.importedStatus && row.toggleSelected(!!value)
            }
            aria-label="Select row"
            disabled={row.original.importedStatus}
          />
        ),
      }),
      columnHelper.accessor('sNo', {
        header: 'S. No.',
        cell: info => info.row.index + 1,
      }),
      columnHelper.accessor('email', {
        header: 'Email Id',
      }),
      columnHelper.accessor('full_name', {
        header: 'First Name',
      }),
      columnHelper.accessor('id', {
        header: 'Emp Code',
      }),
      columnHelper.accessor('department', {
        header: 'Department',
      }),
    ],
    [columnHelper]
  );

  const filteredData = useMemo(
    () =>
      employeeData.filter(
        row =>
          row.email?.toLowerCase().includes(searchValue.toLowerCase()) ||
          row.first_name?.toLowerCase().includes(searchValue.toLowerCase()) ||
          row.role?.toLowerCase().includes(searchValue.toLowerCase()) ||
          row.department?.toLowerCase().includes(searchValue.toLowerCase())
      ),
    [employeeData, searchValue]
  );

  const table = useReactTable({
    data: filteredData,
    columns,
    initialState: { pagination: { pageIndex, pageSize } },
    state: { pagination: { pageIndex, pageSize } },
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });

  const selectedRowData = table
    .getSelectedRowModel()
    .rows.map(row => row.original);

  const handleAddEmployees = () => {
    mutation.mutate({ usersData: selectedRowData });
  };

  const handlePageSizeChange = event => {
    setPageSize(Number(event.target.value));
    setPageIndex(0);
  };

  const handlePageChange = newPageIndex => {
    if (newPageIndex >= 0 && newPageIndex < totalPages) {
      setPageIndex(newPageIndex);
    }
  };

  useEffect(() => {
    if (pageIndex > 0) {
      fetchNextPage();
    }
  }, [pageIndex, fetchNextPage]);

  return (
    <div className="grid gap-4 grid-cols-12 col-span-12 px-2 md:px-6 2xl:p-6 overflow-auto lg:h-[300px] lg:max-h-[300px] 2xl:h-[450px] 2xl:max-h-[450px]">
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6"}`}>
        <CardHeader>
          <CardTitle className="font-bold text-xs 2xl:text-lg">
            Add EmpMonitor Employees
          </CardTitle>
        </CardHeader>
        <CardContent className="py-4 flex flex-col justify-start items-start">
          <div className="flex flex-wrap md:flex-row justify-between w-full gap-3">
            <div className="bg-slate-400/10 border-none px-4 relative rounded-md w-full lg:w-fit">
              <Input
                placeholder="Search Employee..."
                value={searchValue}
                className="bg-transparent"
                onChange={event => setSearchValue(event.target.value)}
              />
              <Search className="absolute top-2/4 right-2 -translate-y-2/4 w-5 h-5" />
            </div>
            <div className="flex justify-between lg:justify-end items-center gap-3 w-full lg:w-fit">
              <Modal
                disable={
                  !table.getIsSomePageRowsSelected() &&
                  !table.getIsAllPageRowsSelected()
                }
                triggerText={'Add'}
                title={'Add EmpMonitor Employees'}
                buttonStyles={'bg-solid-violet col-span-5 md:col-span-2'}>
                <AlertModal
                  alertMessage={`Are you sure want to add selected ${selectedRowData?.length ?? 0} employees ?`}
                  buttonText="Add"
                  handleAddEmployees={handleAddEmployees}
                />
              </Modal>
              <div className="flex items-center gap-2">
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
            </div>
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
              {isLoading || isFetching || isFetchingNextPage ? (
                <TableRow>
                  <TableCell
                    colSpan={columns.length}
                    className="h-32 lg:h-52 text-center">
                    Loading...
                  </TableCell>
                </TableRow>
              ) : filteredData.length === 0 ? (
                <TableRow>
                  <TableCell
                    colSpan={columns.length}
                    className="h-32 lg:h-52 text-center">
                    No data available
                  </TableCell>
                </TableRow>
              ) : (
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
              )}
            </TableBody>
          </Table>

          <div className="flex flex-wrap gap-3 items-center justify-between w-full">
            <div className="text-[10px] 2xl:text-sm">
              {table.getFilteredSelectedRowModel().rows.length} of{' '}
              {table.getFilteredRowModel().rows.length} row(s) selected.
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

export default ImportEmployee;
