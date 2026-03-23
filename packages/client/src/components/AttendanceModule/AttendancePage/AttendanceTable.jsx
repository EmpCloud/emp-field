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
import { ChevronDown, FilePenLine, Search, Trash2, Upload } from 'lucide-react';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
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
import { getAllEmployeeAttendanceData } from '../Apis/post';
import { columns } from './Column';
import { TooltipProvider } from '@/components/ui/tooltip';
import { useState } from 'react';
import { TransformAttendanceData } from './TransformAttendanceData';
import {
  RiArrowDropLeftLine,
  RiArrowDropRightLine,
  RiArrowLeftDoubleFill,
  RiArrowRightDoubleFill,
} from 'react-icons/ri';

export function AttendanceTable({
  selectedDate,
  // employeeLocation, employeeDepartment, employeeDesignation
}) {
  const [pageSize, setPageSize] = useState(10);
  const [pageIndex, setPageIndex] = useState(0);
  const [searchTerm, setSearchTerm] = useState('');
  const [searchError, setSearchError] = useState('');
  const [loading, setLoading] = useState(false);
  const exportableFields = [
    { label: 'Emp Name', value: 'name' },
    { label: 'Emp Location', value: 'location' },
    { label: 'Emp Department', value: 'department' },
    { label: 'Emp Code', value: 'emp_code' },
    { label: 'Total Present', value: 'P' },
    { label: 'Total Leave', value: 'L' },
    { label: 'Total Absent', value: 'A' },
    { label: 'Total Holiday', value: 'H' },
    { label: 'Total Week-off', value: 'O' },
    { label: 'Total Days', value: 'T' },
  ];
  const [selectedExportFields, setSelectedExportFields] = useState(
    exportableFields.map(field => field.value)
  );
  const [sorting, setSorting] = React.useState([]);
  const [columnFilters, setColumnFilters] = React.useState([]);
  const [columnVisibility, setColumnVisibility] = React.useState({});
  const [rowSelection, setRowSelection] = React.useState({});
  const [totalCount, setTotalCount] = React.useState(0);
  const [data, setData] = React.useState([]);
  const [isExportMenuOpen, setIsExportMenuOpen] = useState(false);
  const monthLength = new Date(
    selectedDate.getFullYear(),
    selectedDate.getMonth() + 1,
    0
  ).getDate();
  const formattedDate = `${selectedDate.getFullYear()}${String(selectedDate.getMonth() + 1).padStart(2, '0')}`;
  const getEmployeeAttendance = async () => {
    try {
      setLoading(true);
      const res = await getAllEmployeeAttendanceData({
        date: formattedDate,
        page: pageIndex,
        limit: pageSize,
        searchTerm,
      });
      if (res.data.statusCode === 200) {
        const processed = TransformAttendanceData(
          res.data.body.data.data,
          monthLength
        );
        setTotalCount(res.data.body.data.totalCount);
        setData(processed);
      } else if (res.data.statusCode === 400) toast.error(res.data.message);
    } catch (error) {
      console.log('Internal Server Error', error);
    } finally {
      setLoading(false);
    }
  };
  React.useEffect(() => {
    if (searchTerm.length === 0 || searchTerm.length >= 3)
      getEmployeeAttendance();
  }, [formattedDate, pageIndex, pageSize, searchTerm]);

  const table = useReactTable({
    data,
    columns: columns(monthLength, selectedDate),
    manualPagination: true,
    pageCount: Math.ceil(totalCount / pageSize),
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
      pagination: {
        pageIndex,
        pageSize,
      },
    },
  });
  const handlePageChange = newPageIndex => {
    setPageIndex(newPageIndex);
  };
  const handleExportPDF = async () => {
    try {
      const res = await getAllEmployeeAttendanceData({
        date: formattedDate,
        page: 0,
        limit: totalCount,
        searchTerm,
      });
      if (res.data.statusCode !== 200)
        return console.error('Failed to fetch data');
      const doc = new jsPDF();
      const employeeData = TransformAttendanceData(
        res.data.body.data.data,
        monthLength
      );
      const orderedSelectedFields = exportableFields.filter(field =>
        selectedExportFields.includes(field.value)
      );
      const headers = orderedSelectedFields.map(field => field.label);
      const rows = employeeData.map(emp =>
        orderedSelectedFields.map(field => emp[field.value] ?? '--')
      );
      const pageWidth = doc.internal.pageSize.getWidth();

      const pageHeight = doc.internal.pageSize.getHeight();
      doc.addImage(
        'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/header-2.png?timestamp=1726132224089',
        'PNG',
        0,
        0,
        pageWidth,
        40
      );
      doc.addImage(
        'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/EmpMonitor-logo-tag1-white.png?timestamp=1726140771727',
        'PNG',
        16,
        18,
        pageWidth / 5,
        10
      );
      doc.setTextColor(31, 58, 120); // RGB for red
      const dateText = `Date: ${selectedDate.getFullYear()}-${String(selectedDate.getMonth() + 1).padStart(2, '0')}-01`;
      doc.text('Employee Attendance Report', 14, 46);
      const textWidth = doc.getTextWidth(dateText);
      doc.setFontSize(12);
      doc.text(dateText, pageWidth - textWidth - 14, 48);
      autoTable(doc, {
        head: [headers],
        body: rows,
        startY: 55,
        // styles: {
        //   fontSize: 8,
        //   cellPadding: 2,
        // },
        styles: {
          fontSize: 8,
          cellPadding: 2,
          // halign: 'left',
          lineColor: [255, 255, 255], // Remove border color,
          // minCellHeight: 24,
        },
        headStyles: {
          fillColor: [241, 241, 255], // Light blue color for the header background
          textColor: [31, 58, 120], // Black text color for the header
          // fontSize: 11,
          font: 'Quicksand-Bold',
          fontStyle: 'normal',
        },
        bodyStyles: {
          fillColor: [241, 241, 255], // White background for odd rows
          // fontSize: 10,
          font: 'Quicksand-Medium',
          fontStyle: 'normal',
        },
        alternateRowStyles: {
          fillColor: [255, 255, 255], // Light blue for even rows
          // fontSize: 10,
          font: 'Quicksand-Medium',
          fontStyle: 'normal',
        },
      });

      //  const imgy02 = pageHeight - imgHeight;
      doc.addImage(
        'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/header-1.png?timestamp=1726132199725',
        'PNG',
        0,
        pageHeight - 40,
        pageWidth,
        40
      );

      doc.save(`Employee-Attendance-${formattedDate}.pdf`);
    } catch (err) {
      console.error('Export failed', err);
    }
  };
  const searchHadler = e => {
    setSearchTerm(e.target.value);
    if (e.target.value.length === 0) {
      setSearchError('');
      setPageIndex(0);
    } else if (e.target.value.length < 3)
      setSearchError('Please enter at least 3 characters');
    else {
      setSearchError('');
      setPageIndex(0);
    }
  };
  return (
    <TooltipProvider>
      <div className="w-full col-span-12 card-shadow bg-white rounded-xl">
        <div className="flex sm:items-center sm:justify-between p-5 gap-4  w-full sm:flex-row flex-col">
          <div className="flex flex-col w-[70%] sm:w-[220px] md:w-96">
            <div className="bg-slate-400/10 border-none px-4 relative rounded-md">
              <Input
                placeholder="Search..."
                value={searchTerm}
                className="bg-transparent"
                onChange={searchHadler}
              />
              <Search className="absolute top-2/4 right-2 -translate-y-2/4 w-4 h-4 2xl:w-5 2xl:h-5" />
            </div>
            {searchError && (
              <div className="text-red-500 text-xs mt-1 pl-2">
                {searchError}
              </div>
            )}
          </div>
          <div className="flex items-center gap-2 text-[10px] flex-1 justify-between">
            <div>
              <DropdownMenu
                open={isExportMenuOpen}
                onOpenChange={open => {
                  setIsExportMenuOpen(open);
                  if (open)
                    setSelectedExportFields(
                      exportableFields.map(field => field.value)
                    );
                }}>
                <DropdownMenuTrigger asChild>
                  <Button className="bg-violet-500 hover:bg-violet-500/80">
                    <Upload className="me-2" />
                    Export
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent className="max-h-64 overflow-auto w-64">
                  {exportableFields.map(field => (
                    <div
                      key={field.value}
                      className="flex items-center gap-2 px-2 py-1.5 cursor-pointer hover:bg-muted"
                      onClick={e => e.stopPropagation()}>
                      <input
                        type="checkbox"
                        checked={selectedExportFields.includes(field.value)}
                        onChange={e => {
                          const checked = e.target.checked;
                          setSelectedExportFields(prev =>
                            checked
                              ? [...prev, field.value]
                              : prev.filter(val => val !== field.value)
                          );
                        }}
                        className="form-checkbox h-4 w-4"
                      />
                      <label className="text-sm">{field.label}</label>
                    </div>
                  ))}
                  <div className="mt-2 border-t pt-2">
                    <Button
                      className="w-full"
                      disabled={selectedExportFields.length === 0}
                      onClick={async () => {
                        await handleExportPDF();
                        setIsExportMenuOpen(false);
                      }}>
                      Export Selected
                    </Button>
                  </div>
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
            <div className="flex items-center gap-2">
              <span className="font-semibold text-xs">Show</span>
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="outline">
                    <span className="text-xs font-semibold">{pageSize}</span>
                    <ChevronDown className="ml-2 h-4 w-4" />
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="center" className="p-1">
                  {[10, 20, 30].map(size => (
                    <DropdownMenuCheckboxItem
                      key={size}
                      onClick={() => {
                        setPageSize(size);
                        setPageIndex(0);
                      }}
                      className="text-[10px] 2xl:text-sm">
                      {size}
                    </DropdownMenuCheckboxItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>
              <span className="font-semibold text-xs">Entries</span>
            </div>
          </div>
        </div>
        <div className="rounded-md border">
          <Table>
            <TableHeader className="bg-[#F1F1FF]">
              {table.getHeaderGroups().map(headerGroup => (
                <TableRow key={headerGroup.id}>
                  {headerGroup.headers.map(header => {
                    return (
                      <TableHead
                        key={header.id}
                        className="text-[#1f3a78] font-bold  ">
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
              {loading ? (
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
                    className="h-[42px]"
                    data-state={row.getIsSelected() && 'selected'}>
                    {row.getVisibleCells().map((cell, index) => (
                      <TableCell
                        key={cell.id}
                        className={`text-[#4d4c4c]  ${index === 0 ? 'font-bold' : ''}`}>
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
        <div className="flex flex-wrap gap-3 items-center justify-between w-full p-5">
          <div className="text-[10px] 2xl:text-sm">
            Showing {pageIndex * pageSize + 1} to {(pageIndex + 1) * pageSize}{' '}
            of {totalCount} entries
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
                {pageIndex + 1} of {Math.ceil(totalCount / pageSize)}
              </span>
            </div>
            <button
              className="pagination-button"
              onClick={() => handlePageChange(pageIndex + 1)}
              disabled={pageIndex >= Math.ceil(totalCount / pageSize) - 1}>
              <RiArrowDropRightLine />
            </button>
            <button
              className="pagination-button"
              onClick={() =>
                handlePageChange(Math.ceil(totalCount / pageSize) - 1)
              }
              disabled={pageIndex >= Math.ceil(totalCount / pageSize) - 1}>
              <RiArrowRightDoubleFill />
            </button>
          </div>
        </div>
      </div>
    </TooltipProvider>
  );
}
