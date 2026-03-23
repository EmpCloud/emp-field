'use client';

import * as React from 'react';
import {
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useReactTable,
} from '@tanstack/react-table';
import { ChevronDown, FilePenLine, Search, Trash2 } from 'lucide-react';

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
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

const data = [
  {
    id: 'm5gr84i9',
    amount: 316,
    employee: 'Nanadan K',
    leaveType: 'Emergency Leave',
    dayType: 'Full Day',
    status: 'Approved',
    startDate: '15-05-2024',
    endDate: '15-05-2024',
  },
  {
    id: '3u1reuv4',
    amount: 242,
    employee: 'Nanadan K',
    dayType: 'Half Day',
    leaveType: 'Casual Leave',
    status: 'Approved',
    startDate: '15-05-2024',
    endDate: '15-05-2024',
  },
  {
    id: 'derv1ws0',
    amount: 837,
    leaveType: 'Emergency Leave',
    dayType: 'Full Day',
    employee: 'Nanadan K',
    status: 'Pending',
    startDate: '15-05-2024',
    endDate: '15-05-2024',
  },
  {
    id: '5kma53ae',
    amount: 874,
    leaveType: 'Casual Leave',
    dayType: 'Full Day',
    employee: 'Nanadan K',
    status: 'Approved',
    startDate: '15-05-2024',
    endDate: '15-05-2024',
  },
  {
    id: 'bhqecj4p',
    amount: 721,
    leaveType: 'Emergency Leave',
    dayType: 'Full Day',
    employee: 'Nanadan K',
    status: 'pending',
    startDate: '15-05-2024',
    endDate: '15-05-2024',
  },
];

const Action = () => {
  <div className="text-right font-medium h-full w-full">
    <FilePenLine size={30} />
    <Trash2 size={30} />
  </div>;
};

const columns = [
  {
    accessorKey: 'employee',
    header: 'Employee',
    cell: ({ row }) => (
      <div className="capitalize">{row.getValue('employee')}</div>
    ),
  },
  {
    accessorKey: 'leaveType',
    header: 'Leave Type',
    cell: ({ row }) => (
      <div className="capitalize">{row.getValue('leaveType')}</div>
    ),
  },
  {
    accessorKey: 'dayType',
    header: 'Day Type',
    cell: ({ row }) => (
      <div className="capitalize">{row.getValue('dayType')}</div>
    ),
  },
  {
    accessorKey: 'startDate',
    header: 'Start Date',
    cell: ({ row }) => (
      <div className="capitalize">{row.getValue('startDate')}</div>
    ),
  },
  {
    accessorKey: 'endDate',
    header: 'End Date',
    cell: ({ row }) => (
      <div className="capitalize">{row.getValue('endDate')}</div>
    ),
  },
  {
    accessorKey: 'status',
    header: 'Status',
    cell: ({ row }) => (
      <div className="capitalize">{row.getValue('status')}</div>
    ),
  },
  {
    accessorKey: 'amount',
    header: 'Action',
    cell: () => <Action />,
  },
];

export function LeaveTable() {
  const [sorting, setSorting] = React.useState([]);
  const [columnFilters, setColumnFilters] = React.useState([]);
  const [columnVisibility, setColumnVisibility] = React.useState({});
  const [rowSelection, setRowSelection] = React.useState({});

  const table = useReactTable({
    data,
    columns,
    onSortingChange: setSorting,
    onColumnFiltersChange: setColumnFilters,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    onColumnVisibilityChange: setColumnVisibility,
    onRowSelectionChange: setRowSelection,
    state: {
      sorting,
      columnFilters,
      columnVisibility,
      rowSelection,
    },
  });

  return (
    <div className="w-full col-span-12 card-shadow bg-white rounded-xl p-5">
      <div className="flex items-center py-4">
        <div className=" w-96 bg-slate-400/10 border-none px-4 relative rounded-md">
          <Input
            placeholder="Search..."
            value={table.getColumn('email')?.getFilterValue() ?? ''}
            className="bg-transparent"
            onChange={event =>
              table.getColumn('email')?.setFilterValue(event.target.value)
            }
          />
          <Search className="absolute top-2/4 right-2 -translate-y-2/4 w-4 2xl:w-5 h-4 2xl:h-5" />
        </div>
        <Select>
          <SelectTrigger className="bg-slate-400/10 border-none max-w-48 ml-4">
            <SelectValue placeholder="All Location" />
          </SelectTrigger>
          <SelectContent>
            <SelectGroup>
              <SelectItem value="apple">Apple</SelectItem>
              <SelectItem value="banana">Banana</SelectItem>
              <SelectItem value="blueberry">Blueberry</SelectItem>
              <SelectItem value="grapes">Grapes</SelectItem>
              <SelectItem value="pineapple">Pineapple</SelectItem>
            </SelectGroup>
          </SelectContent>
        </Select>
        <DropdownMenu>
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
        </DropdownMenu>
      </div>
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map(headerGroup => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map(header => {
                  return (
                    <TableHead key={header.id}>
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
                <TableCell
                  colSpan={columns.length}
                  className="h-24 text-center">
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div className="flex items-center justify-end space-x-2 py-4">
        <div className="flex-1 text-sm text-muted-foreground">
          {table.getFilteredSelectedRowModel().rows.length} of{' '}
          {table.getFilteredRowModel().rows.length} row(s) selected.
        </div>
        <div className="space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => table.previousPage()}
            disabled={!table.getCanPreviousPage()}>
            Previous
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => table.nextPage()}
            disabled={!table.getCanNextPage()}>
            Next
          </Button>
        </div>
      </div>
    </div>
  );
}
