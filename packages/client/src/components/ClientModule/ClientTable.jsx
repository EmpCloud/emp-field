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
import React, { useEffect, useMemo, useState } from 'react';
import {
  RiArrowDropLeftLine,
  RiArrowDropRightLine,
  RiArrowLeftDoubleFill,
  RiArrowRightDoubleFill,
} from 'react-icons/ri';
import ClientTableAction from './ClientTableAction';
import addTaskIcon from 'assets/images/add-task.png';
import { Modal } from 'components/Modal';
import AddNewClientModal from 'components/UIElements/Modals/AddNewClientModal';
import { fetchClients } from './Api/get';
import { useInfiniteQuery } from '@tanstack/react-query';

const columnHelper = createColumnHelper();

const ClientTable = ({ searchQuery, searchValue }) => {
  const [pageSize, setPageSize] = React.useState(10);
  const [pageIndex, setPageIndex] = React.useState(0);
  const [isAddModalOpen, setIsAddModalOpen] = React.useState(false);

  const disableActions = false;

  const handleAddClientClick = () => {
    setIsAddModalOpen(true);
  };

  const handleCloseAddModal = () => {
    setIsAddModalOpen(false);
  };

  const {
    data,
    error,
    fetchNextPage,
    hasNextPage,
    isFetching,
    isFetchingNextPage,
    status,
    isLoading,
    refetch,
  } = useInfiniteQuery({
    queryKey: ['fetchClients', pageSize, searchQuery, searchValue],
    queryFn: ({ pageParam = 0 }) =>
      fetchClients({ pageParam, limit: 1000, searchQuery, searchValue }),
    getNextPageParam: (lastPage, pages) => {
      if (!lastPage || lastPage?.data?.body?.data?.data?.length < pageSize) {
        return undefined; // No more pages
      }
      return pages.length * pageSize; // Fetch next page
    },
  });

  const columnHelper = createColumnHelper();

  const columns = [
    columnHelper.accessor('clientName', {
      header: 'Client Name',
      cell: info => info.getValue(),
    }),
    // columnHelper.accessor('clientId', {
    //   header: 'Client ID',
    // }),
    columnHelper.accessor('contactNumber', {
      header: 'Client Phone No.',
      cell: info => {
        const time = info.getValue();

        return (
          <span className="text-[#1470C6] w-24 lg:w-36 block">{time}</span>
        );
      },
    }),
    columnHelper.accessor('address1', {
      header: 'Address',
    }),
    columnHelper.accessor('employees', {
      header: 'Assigned',
      cell: info => {
        const employees = info.getValue();
        const disableActions =
          info.row.original.clientName === 'Default Client';
        return disableActions ? '' : employees?.name || 'NA';
      },
    }),
    //   columnHelper.accessor('action', {
    //     header: 'Action',
    //     cell: ({ row }) => (
    //       <ClientTableAction rowData={row?.original} refetch={refetch} />
    //

    columnHelper.accessor('action', {
      header: 'Action',
      cell: ({ row }) => (
        <ClientTableAction
          rowData={row?.original}
          refetch={refetch}
          disableActions={row.original.clientName === 'Default Client'}
        />
      ),
    }),
  ];

  const totalCount = data?.pages[0]?.data?.body?.data?.totalCount || 0;
  const totalPages = Math.ceil(totalCount / pageSize);

  const ClientData = useMemo(() => {
    if (!data || data.pages.length === 0) return [];
    const allClients = data.pages.flatMap(
      page => page?.data?.body?.data?.data || []
    );
    // Use a Set to filter out duplicates based on the client ID
    const uniqueClients = Array.from(
      new Map(allClients.map(client => [client._id, client])).values()
    );
    return uniqueClients;
  }, [data]);

  // const filteredData = useMemo(
  //   () =>
  //     ClientData.filter(
  //       row =>
  //         (row.assignedMembers !== undefined &&
  //           row?.assignedMembers[0]?.fullName
  //             ?.toLowerCase()
  //             .includes(searchQuery?.toLowerCase())) ||
  //         row?.clientName?.toLowerCase().includes(searchValue?.toLowerCase())
  //     ),
  //   [ClientData, searchValue, searchQuery]
  // );
  const filteredData = useMemo(() => {
    if (!ClientData || ClientData.length === 0) return [];
    return ClientData.filter(row => {
      const matchesQuery = searchQuery
        ? row.assignedMembers?.some(member =>
            member.fullName?.toLowerCase().includes(searchQuery.toLowerCase())
          ) || row.clientName.toLowerCase().includes(searchQuery.toLowerCase())
        : true;

      const matchesValue = searchValue
        ? row.clientName.toLowerCase().includes(searchValue.toLowerCase())
        : true;

      return matchesQuery && matchesValue;
    });
  }, [ClientData, searchValue, searchQuery]);

  const table = useReactTable({
    data: filteredData,
    columns,
    initialState: { pagination: { pageIndex, pageSize } },
    state: { pagination: { pageIndex, pageSize } },
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });

  const handlePageChange = newPageIndex => {
    if (newPageIndex >= 0 && newPageIndex < totalPages) {
      setPageIndex(newPageIndex);
    }
  };

  useEffect(() => {
    if (pageIndex > 0 && hasNextPage) {
      fetchNextPage();
    }
  }, [pageIndex, fetchNextPage, hasNextPage]);

  const [showAddClientModal, setShowAddClientModal] = useState(false);
  const handleOpenAddClientModal = () => {
    setShowAddClientModal(true);
  };
  const handleCloseAddClientModal = () => {
    setShowAddClientModal(false);
  };

  useEffect(() => {
    setPageIndex(0);
    refetch();
  }, [searchQuery, searchValue, refetch]);

  return (
    <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-3 items-center">
      <div className="col-span-12 grid gap-3">
        <div className="grid grid-cols-12 gap-4 items-center">
          {/* <Modal
            triggerText={'Add New Client'}
            title={'Add New Client'}
            titleStyles={'text-center'}
            buttonStyles={
              'col-span-6 sm:col-span-2 flex gap-3 text-[#1F3A78] border border-[#1F3A78] bg-transparent hover:bg-[#1F3A78]/10'
            }
            triggerButtonIcon={
              <img className="w-4 h-4 2xl:w-5 2xl:h-5" src={addTaskIcon} />
            }>
            <AddNewClientModal
              onClose={handleCloseAddModal}
              refetch={refetch}
            />
          </Modal> */}
          <Button
            onClick={handleOpenAddClientModal}
            className="col-span-6 sm:col-span-2 flex gap-3 text-[#1F3A78] border border-[#1F3A78] bg-transparent hover:bg-[#1F3A78]/10">
            <img
              className="w-4 h-4 2xl:w-5 2xl:h-5"
              src={addTaskIcon}
              alt="Add"
            />
            Add New Client
          </Button>

          {showAddClientModal && (
            <div
              className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50"
              onClick={handleCloseAddClientModal} // Close modal on background click
            >
              <div
                className={`bg-white rounded-lg w-96 ${location.pathname === '/admin/task' || location.pathname === '/admin/clients' ? '!w-58per' : ''}`}
                onClick={e => e.stopPropagation()} // Prevent closing when clicking inside the modal
              >
                <div className="flex justify-between items-center bg-gradient-to-r from-purple-500 to-blue-600 rounded-t-md text-white p-4">
                  <h2 className="text-lg font-semibold flex-grow text-center">
                    Add New Client
                  </h2>
                  <Button
                    className="bg-transparent text-sm"
                    onClick={handleCloseAddClientModal}>
                    X
                  </Button>
                </div>
                <AddNewClientModal
                  onClose={handleCloseAddClientModal}
                  // refetch={fetchNextPage}
                  refetch={refetch}
                />
              </div>
            </div>
          )}
          <DropdownMenu>
            <div className="lg:col-start-10 col-span-6 lg:col-span-3 flex justify-end gap-2 items-center text-[10px]">
              <span>Show</span>
              <DropdownMenuTrigger asChild className="">
                <Button variant="outline" onClick={handleAddClientClick}>
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
                  className="text-[10px] 2xl:text-sm"
                  disabled={disableActions}>
                  10
                </DropdownMenuCheckboxItem>
                <DropdownMenuCheckboxItem
                  onClick={() => {
                    setPageSize(20);
                    setPageIndex(0);
                  }}
                  className="text-[10px] 2xl:text-sm"
                  disabled={disableActions}>
                  20
                </DropdownMenuCheckboxItem>
                <DropdownMenuCheckboxItem
                  onClick={() => {
                    setPageSize(30);
                    setPageIndex(0);
                  }}
                  className="text-[10px] 2xl:text-sm"
                  disabled={disableActions}>
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
            {isLoading ? (
              <TableRow>
                <TableCell
                  colSpan={columns.length}
                  className="h-24 text-center">
                  Loading...
                </TableCell>
              </TableRow>
            ) : table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map(row => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && 'selected'}>
                  {row.getVisibleCells().map(cell => (
                    <TableCell key={cell.id} className="max-w-40">
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
                  No Clients found
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
              disabled={!table.getCanPreviousPage() || disableActions}>
              <RiArrowLeftDoubleFill />
            </button>
            <button
              className="pagination-button"
              onClick={() => handlePageChange(pageIndex - 1)}
              disabled={!table.getCanPreviousPage() || disableActions}>
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
              disabled={
                pageIndex >= totalPages - 1 || filteredData.length <= pageSize
              }>
              <RiArrowDropRightLine />
            </button>
            <button
              className="pagination-button"
              onClick={() => handlePageChange(totalPages - 1)}
              disabled={
                pageIndex >= totalPages - 1 || filteredData.length <= pageSize
              }>
              <RiArrowRightDoubleFill />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ClientTable;
