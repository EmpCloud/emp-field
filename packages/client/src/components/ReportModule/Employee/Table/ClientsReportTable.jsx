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
import ChartContext from 'components/ChartContext/Context';
import React, { useContext, useEffect } from 'react';

const columnHelper = createColumnHelper();

const ClientsReportTable = ({ clientDetails, isLoading }) => {
  const { employeeClientTableData, setemployeeClientTableData } =
    useContext(ChartContext);

  const columns = [
    columnHelper.accessor('clientName', {
      header: 'Client Name',
      cell: info => (
        <span className="font-semibold whitespace-nowrap flex items-center gap-1">
          <div className="w-3 h-3 bg-[#e1c2eb] rounded-full"></div>
          <span className="truncate">{info.getValue()}</span>
        </span>
      ),
    }),
    columnHelper.accessor('phoneNumber', {
      header: 'Phone Number',
      cell: info => <span className="truncate">{info.getValue()}</span>,
    }),
    columnHelper.accessor('address', {
      header: 'Address',
      cell: info => (
        <span className="break-words max-w-[200px]">{info.getValue()}</span>
      ),
    }),
  ];

  const client = clientDetails?.clientDetails?.map(cd => ({
    clientName: cd?.clientDetails?.clientName,
    phoneNumber: cd?.clientDetails?.contactNumber,
    address: cd?.clientDetails?.address1,
  }));

  const table = useReactTable({
    data: client || [],
    columns,
    getCoreRowModel: getCoreRowModel(),
  });

  useEffect(() => {
    if (clientDetails) {
      setemployeeClientTableData(client);
    }
  }, [clientDetails]);

  return (
    <div className="grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg pb-5 max-h-[287px] shadow-none border-none overflow-auto">
      <div className="col-span-12 bg-white rounded-lg shadow p-4 overflow-x-auto max-w-full">
        <Table className="w-full table-fixed">
          <TableHeader>
            {table.getHeaderGroups().map(headerGroup => (
              <TableRow
                key={headerGroup.id}
                className="bg-[#F1F1FF]  sticky top-0 z-30">
                {headerGroup.headers.map(header => (
                  <TableHead key={header.id} className="px-3 ">
                    <div className="font-bold text-[#1F3A78]">
                      {flexRender(
                        header.column.columnDef.header,
                        header.getContext()
                      )}
                    </div>
                  </TableHead>
                ))}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows.length ? (
              table.getRowModel().rows.map(row => (
                <TableRow key={row.id}>
                  {row.getVisibleCells().map(cell => (
                    <TableCell
                      key={cell.id}
                      className="py-2 px-3 text-xs text-[#4D4C4C] font-medium break-words max-w-[200px]">
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
                  className="text-center h-24">
                  {!isLoading && client?.length === 0
                    ? 'No data'
                    : 'Loading...'}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
    </div>
  );
};

export default ClientsReportTable;
