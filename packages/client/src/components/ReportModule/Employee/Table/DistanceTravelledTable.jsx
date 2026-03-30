import React, { useContext, useEffect } from 'react';
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
import blueBikeImage from '../../../../assets/images/reportTable/blue_bike.png';
import blueCarImage from '../../../../assets/images/reportTable/blue_car.png';
import ChartContext from 'components/ChartContext/Context';

const columnHelper = createColumnHelper();

const DistanceTravelledTable = ({ employeeDistanceDetail, isLoading }) => {
  const { setemployeeTravelledTableData, employeeTravelledTableData } =
    useContext(ChartContext);
  // Define columns
  const columns = [
    columnHelper.accessor('date', {
      header: 'Date',
      cell: info => info.getValue() || '-----',
      footer: info => info.column.id,
    }),
    columnHelper.accessor('distTravelled', {
      header: 'Distance Travelled (km)',
      cell: info => {
        const distance = info.getValue();
        return distance ? `${distance.toFixed(2)} km` : '-----';
      },
      footer: info => info.column.id,
    }),
    columnHelper.accessor('currentMode', {
      header: 'Mode',
      cell: info => {
        const mode = info.getValue();
        return (
          <span className="font-semibold">
            {mode === 'car' ? (
              <img
                src={blueCarImage}
                alt="Car"
                style={{ width: '18px', height: '18px' }}
              />
            ) : mode === 'bike' ? (
              <img
                src={blueBikeImage}
                alt="Bike"
                style={{ width: '18px', height: '18px' }}
              />
            ) : (
              '-----'
            )}
          </span>
        );
      },
      footer: info => info.column.id,
    }),
    columnHelper.accessor('currentFrequency', {
      header: 'Frequency (Seconds)',
      cell: info => {
        const frequency = info.getValue();
        return frequency !== null && frequency !== undefined
          ? `${frequency} sec`
          : '-----';
      },
      footer: info => info.column.id,
    }),
  ];

  const table = useReactTable({
    data: employeeDistanceDetail || [],
    columns,
    getCoreRowModel: getCoreRowModel(),
    // getPaginationRowModel: getPaginationRowModel(),
  });

  useEffect(() => {
    if (employeeDistanceDetail) {
      setemployeeTravelledTableData(employeeDistanceDetail);
    }
  }, [employeeDistanceDetail]);

  return (
    <div className="grid gap-4 grid-cols-12 col-span-12 bg-white w-full rounded-lg  pb-5 max-h-[390px]">
      <div className="col-span-12 grid gap-1 max-h-[325px]">
        <Table className=" w-full h-full !overflow-x-auto !overflow-y-hidden">
          <TableHeader className="!sticky top-0 h-[64px]">
            {table.getHeaderGroups().map(headerGroup => (
              <TableRow key={headerGroup.id} className="bg-[#F1F1FF]">
                {headerGroup.headers.map(header => (
                  <TableHead
                    className="whitespace-nowrap px-3 h-[64px]"
                    key={header.id}>
                    {header.isPlaceholder ? null : (
                      <div className="flex items-center font-bold h-9 text-[#1F3A78]">
                        {flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                      </div>
                    )}
                  </TableHead>
                ))}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow>
                <TableCell
                  colSpan={columns?.length}
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
                    <TableCell
                      key={cell.id}
                      className="max-w-40 py-2 px-3 text-[#4D4C4C] text-xs font-medium h-[64px]">
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
                  No data available
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
    </div>
  );
};

export default DistanceTravelledTable;
