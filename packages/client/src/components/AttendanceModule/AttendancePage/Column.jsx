import {
  Tooltip,
  TooltipTrigger,
  TooltipContent,
} from '@/components/ui/tooltip';
import { Info } from 'lucide-react';
export const columns = (monthLength, selectedDate) => {
  const dateColumns = [];
  const year = selectedDate.getFullYear();
  const month = selectedDate.getMonth();
  for (let i = 1; i <= monthLength; i++) {
    const dateObj = new Date(year, month, i);
    const dayName = dateObj.toLocaleDateString('en-US', { weekday: 'long' });
    dateColumns.push({
      accessorKey: `date.${i}.log.marker`,
      header: () => (
        <div className="flex flex-col items-center text-xs">
          <span>{i}</span>
          <span className="2xl:text-sm text-xs text-[#1f3a78]">{dayName}</span>
        </div>
      ),
      cell: ({ row }) => {
        const noShift =
          !row.original.date ||
          typeof row.original.date === 'string' ||
          row.original.date === "Don't have shift for this employee";
        const marker = row.original?.date?.[i]?.log?.marker;
        if (noShift || !marker) {
          return <div className="text-center text-sm">--</div>;
        }
        if (typeof marker === 'object') {
          return (
            <Tooltip>
              <TooltipTrigger asChild>
                <span className="text-center text-sm cursor-help">
                  {marker.text}
                </span>
              </TooltipTrigger>
              <TooltipContent>{marker.tooltip}</TooltipContent>
            </Tooltip>
          );
        }
        const fullFormMap = {
          P: 'Present',
          A: 'Absent',
          HD: 'Half Day Present',
          LOP: 'Loss of Pay',
          WO: 'Week-Off',
        };
        const tooltipText = fullFormMap[marker] || 'Unknown Status';
        return (
          <Tooltip>
            <TooltipTrigger asChild>
              <div className="text-center text-sm cursor-help">{marker}</div>
            </TooltipTrigger>
            <TooltipContent>{tooltipText}</TooltipContent>
          </Tooltip>
        );
      },
    });
  }
  return [
    {
      accessorKey: 'name',
      header: () => <div className="whitespace-nowrap">Employee Name</div>,
      enableSorting: false,
      cell: ({ row }) => (
        <div className="capitalize">{row.getValue('name')}</div>
      ),
    },
    {
      accessorKey: 'department',
      header: () => (
        <div className="whitespace-nowrap">Employee Department</div>
      ),
      enableSorting: false,
      cell: ({ row }) => (
        <div className="capitalize">{row.getValue('department')}</div>
      ),
    },
    {
      accessorKey: 'location',
      header: () => <div className="whitespace-nowrap">Employee Location</div>,
      enableSorting: false,
      cell: ({ row }) => (
        <div className="capitalize">{row.getValue('location')}</div>
      ),
    },
    {
      accessorKey: 'emp_code',
      header: () => <div className="whitespace-nowrap">Employee Code</div>,
      cell: ({ row }) => (
        <div className="capitalize">{row.getValue('emp_code')}</div>
      ),
    },
    ...dateColumns,
    {
      accessorKey: 'P',
      header: () => (
        <Tooltip>
          <TooltipTrigger asChild>
            <div className="flex items-center gap-1 cursor-help">
              <span>P</span>
              <Info className="w-4 h-4 text-blue-900 hover:cursor-pointer" />
            </div>
          </TooltipTrigger>
          <TooltipContent>Total Present Count</TooltipContent>
        </Tooltip>
      ),
      cell: ({ row }) => <div className="capitalize">{row.getValue('P')}</div>,
    },
    {
      accessorKey: 'A',
      header: () => (
        <Tooltip>
          <TooltipTrigger asChild>
            <div className="flex items-center gap-1 cursor-help">
              <span>A</span>
              <Info className="w-4 h-4 text-blue-900 hover:cursor-pointer" />
            </div>
          </TooltipTrigger>
          <TooltipContent>Total Absent Count</TooltipContent>
        </Tooltip>
      ),
      cell: ({ row }) => <div className="capitalize">{row.getValue('A')}</div>,
    },
    {
      accessorKey: 'L',
      header: () => (
        <Tooltip>
          <TooltipTrigger asChild>
            <div className="flex items-center gap-1 cursor-help">
              <span>L</span>
              <Info className="w-4 h-4 text-blue-900 hover:cursor-pointer" />
            </div>
          </TooltipTrigger>
          <TooltipContent>Total Leave Count</TooltipContent>
        </Tooltip>
      ),
      cell: ({ row }) => <div className="capitalize">{row.getValue('L')}</div>,
    },
    {
      accessorKey: 'H',
      header: () => (
        <Tooltip>
          <TooltipTrigger asChild>
            <div className="flex items-center gap-1 cursor-help">
              <span>H</span>
              <Info className="w-4 h-4 text-blue-900 hover:cursor-pointer" />
            </div>
          </TooltipTrigger>
          <TooltipContent>Total Holiday Count</TooltipContent>
        </Tooltip>
      ),
      cell: ({ row }) => <div className="capitalize">{row.getValue('H')}</div>,
    },
    {
      accessorKey: 'O',
      // header: 'O',
      header: () => (
        <Tooltip>
          <TooltipTrigger asChild>
            <div className="flex items-center gap-1 cursor-help">
              <span>O</span>
              <Info className="w-4 h-4 text-blue-900 hover:cursor-pointer" />
            </div>
          </TooltipTrigger>
          <TooltipContent>Total Week-off Count</TooltipContent>
        </Tooltip>
      ),
      cell: ({ row }) => <div className="capitalize">{row.getValue('O')}</div>,
    },
    {
      accessorKey: 'T',
      header: () => (
        <Tooltip>
          <TooltipTrigger asChild>
            <div className="flex items-center gap-1 cursor-help">
              <span>T</span>
              <Info className="w-4 h-4 text-blue-900 hover:cursor-pointer" />
            </div>
          </TooltipTrigger>
          <TooltipContent>Total Days</TooltipContent>
        </Tooltip>
      ),
      cell: ({ row }) => <div className="capitalize">{row.getValue('T')}</div>,
    },
  ];
};
