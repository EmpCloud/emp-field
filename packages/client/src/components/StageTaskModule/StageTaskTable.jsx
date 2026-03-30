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
import React, { useEffect, useMemo, useRef, useState } from 'react';
import {
  RiArrowDropLeftLine,
  RiArrowDropRightLine,
  RiArrowLeftDoubleFill,
  RiArrowRightDoubleFill,
} from 'react-icons/ri';
import StageTaskAction from './StageTaskAction';
import addTaskIcon from 'assets/images/add-task.png';
import { Modal } from 'components/Modal';
import AddNewClientModal from 'components/UIElements/Modals/AddNewClientModal';
// import { fetchClients } from './Api/get';
import { useInfiniteQuery } from '@tanstack/react-query';
import { StageModal } from 'components/StageModal';
import { fetchStage } from './Api/get';
import AddNewStageTaskModal from 'components/UIElements/Modals/AddNewStageTasksModal';
import { RxCross1 } from 'react-icons/rx';

const StageTaskTable = ({ searchQuery }) => {
  const [searchValue, setSearchValue] = useState('');
  const [selectedStage, setSelectedStage] = useState(null);
  const [open, setOpen] = useState(false);
  const [pageSize, setPageSize] = React.useState(10);
  const [pageIndex, setPageIndex] = React.useState(0);
  const [isAddModalOpen, setIsAddModalOpen] = React.useState(false);

  const disableActions = false;

  const handleAddStageTaskClick = () => {
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
    // queryKey: ['fetchStage', pageSize],
    // queryFn: ({ pageParam = 0 }) => fetchStage({ pageParam, limit: pageSize }),
    queryKey: ['fetchStage', pageSize, selectedStage?.id],
    queryFn: ({ pageParam = 0 }) =>
      fetchStage({ pageParam, limit: pageSize, stageId: selectedStage?.id }),
    getNextPageParam: (lastPage, pages) => {
      if (lastPage?.data?.body?.data?.data?.length < pageSize) {
        return undefined; // No more pages
      }
      return pages.length * pageSize; // Fetch next page
    },
  });
  const StageData = useMemo(
    () => data?.pages.flatMap(page => page?.data?.body?.data?.allTags) || [],
    [data]
  );
  const filteredData = useMemo(() => {
    return StageData.filter(row => {
      const matchesStage = selectedStage
        ? row.stageId === selectedStage.id
        : true; // Adjust based on your data structure
      return (
        matchesStage &&
        row.tagName.toLowerCase().includes(searchValue.toLowerCase())
      );
    });
  }, [StageData, selectedStage, searchValue]);

  const columnHelper = createColumnHelper();

  const columns = [
    columnHelper.accessor('order', {
      header: 'Stage No',
      cell: info => {
        const order = info.getValue();
        return String(order).padStart(2, '0');
      },
    }),
    columnHelper.accessor('tagName', {
      header: 'Stage Name',
      cell: info => {
        const colour = info.row.original.color || '#6B7280';
        const status = info.getValue();

        return (
          <span
            style={{
              color: colour,
              backgroundColor: `${colour}20`,
              border: `1px solid ${colour}`,
            }}
            className="py-[1px] px-2 rounded-[5px] inline-block">
            {status}
          </span>
        );
      },
    }),
    // columnHelper.accessor('color', {
    //   header: 'Selected Color',
    //   cell: info => {
    //     const color = info.getValue();

    //     return (
    //       <span className="text-[#1470C6] w-24 lg:w-36 block">{color}</span>
    //     );
    //   },
    // }),

    columnHelper.accessor('action', {
      header: 'Action',
      cell: ({ row }) => (
        <StageTaskAction
          filteredData={filteredData}
          rowData={row?.original}
          refetch={refetch}
          disableActions={row.original._id === '66a0a19df11302964104df04'}
        />
      ),
    }),
  ];

  const totalCount = data?.pages[0]?.data?.body?.data?.totalCount || 0;
  const totalPages = Math.ceil(totalCount / pageSize);

  //   const filteredData = useMemo(() => {
  //     return StageData.filter(row => {
  //       const matchesStage = selectedStage
  //         ? row.stageId === selectedStage.id // Adjust this condition based on your data structure
  //         : true;

  //       return matchesStage && row.tagName.toLowerCase().includes(searchValue.toLowerCase());
  //     });
  //   }, [StageData, selectedStage, searchValue]);

  // // Fetch suggestions based on searchValue
  // const filteredStageData = useMemo(() => {
  //   return StageData.filter(row => row.tagName.toLowerCase().includes(searchValue.toLowerCase()));
  // }, [StageData, searchValue]);

  const handleFocus = () => {
    setOpen(true);
  };

  const handleBlur = () => {
    setOpen(false);
  };

  const handleSearchedStage = stage => {
    setSearchValue(stage.tagName);
    setSelectedStage(stage);
    // setOpen(false);
    refetch();
    setOpen(false);
  };
  const catMenu = useRef(null);

  const filteredStageData = useMemo(() => {
    return StageData.filter(row =>
      row.tagName.toLowerCase().includes(searchValue.toLowerCase())
    );
  }, [StageData, searchValue]);

  const table = useReactTable({
    data: filteredData,
    columns,
    initialState: { pagination: { pageIndex, pageSize } },
    state: { pagination: { pageIndex, pageSize } },
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });

  const handlePageChange = newPageIndex => {
    setPageIndex(newPageIndex);
  };

  useEffect(() => {
    if (pageIndex > 0 && hasNextPage) {
      fetchNextPage();
    }
  }, [pageIndex, fetchNextPage, hasNextPage]);

  function useClickOutside(ref, onClickOutside) {
    useEffect(() => {
      /**
       * Invoke Function onClick outside of element
       */
      function handleClickOutside(event) {
        if (ref.current && !ref.current.contains(event.target)) {
          onClickOutside();
        }
      }
      // Bind
      document.addEventListener('mousedown', handleClickOutside);
      return () => {
        // dispose
        document.removeEventListener('mousedown', handleClickOutside);
      };
    }, [ref, onClickOutside]);
  }

  useClickOutside(catMenu, () => {
    setOpen(false);
  });

  return (
    <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-3 items-center">
      <div className="col-span-12 grid gap-3">
        <div className="grid grid-cols-12 gap-4 items-center px-4 py-4 rounded-2xl bg-white">
          <div className="col-span-8 sm:col-span-4 bg-slate-400/10 border-none px-4 relative rounded-md">
            {/* <Input placeholder="Search..." className="bg-transparent" /> */}
            <Input
              type="text"
              value={searchValue}
              placeholder="Search Stage"
              className="bg-transparent"
              onFocus={handleFocus}
              onChange={e => setSearchValue(e.target.value)}
            />
            {open && filteredStageData.length > 0 && (
              <div
                className="absolute z-10 bg-white shadow-lg rounded-md max-h-60 overflow-y-auto"
                ref={catMenu}>
                {filteredStageData.map(stage => (
                  <div
                    key={stage._id}
                    className="cursor-pointer hover:bg-gray-200 p-2"
                    onMouseDown={() => handleSearchedStage(stage)}>
                    {stage?.tagName ?? ''}
                  </div>
                ))}
              </div>
            )}
            {searchValue ? (
              <RxCross1
                className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                onClick={() => setSearchValue('')}
              />
            ) : (
              <Search className="absolute top-2/4 right-2 -translate-y-2/4 w-4 h-4 2xl:w-5 2xl:h-5" />
            )}
          </div>
          <StageModal
            triggerText={'Add Tasks Stage'}
            className="w-[200px]"
            title={'Tasks Stage'}
            buttonStyles={
              'col-span-6 sm:col-span-2 flex gap-3 text-[#1F3A78] border border-[#1F3A78] bg-transparent hover:bg-[#1F3A78]/10'
            }
            triggerButtonIcon={
              <img className="w-4 h-4 2xl:w-5 2xl:h-5" src={addTaskIcon} />
            }>
            <AddNewStageTaskModal
              onClose={handleCloseAddModal}
              refetch={refetch}
              // |={handleGetTaskDetails}
            />
          </StageModal>

          {/* <DropdownMenu>
            <div className="lg:col-start-10 col-span-6 lg:col-span-3 flex justify-end gap-2 items-center text-[10px]">
              <span>Show</span>
              <DropdownMenuTrigger asChild className="">
                <Button variant="outline" onClick={handleAddStageTaskClick}>
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
          </DropdownMenu> */}
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
                <TableRow key={row.id}>
                  {row.getVisibleCells().map(cell => (
                    <TableCell key={cell.id}>
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
                <TableCell colSpan={columns.length} className="text-center">
                  {isLoading ? 'Loading...' : 'No data found'}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
        {/* <div className="flex items-center justify-between">
          <div className="text-[10px] 2xl:text-sm">
           
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
        </div> */}
      </div>
    </div>
  );
};

export default StageTaskTable;
