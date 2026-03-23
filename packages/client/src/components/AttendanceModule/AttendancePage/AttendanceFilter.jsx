import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { cn } from '@/lib/utils';
import { addDays, format } from 'date-fns';
import { CalendarDays } from 'lucide-react';
import { useState } from 'react';

const AttendanceFilter = ({
  date,
  setDate,
  // ,filters, setFilters
}) => {
  //  const handleFilterChange = (key, value) => {
  //     setFilters(prev => ({ ...prev, [key]: value }));
  //   };

  const hiddingFilter = false;
  return (
    <>
      <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-3">
        {/* First box for searching employee */}
        {hiddingFilter && (
          <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-3 xl:col-span-3">
            <CardHeader className="flex flex-row items-center justify-between p-2">
              <CardTitle className="text-sm font-bold">Location</CardTitle>
            </CardHeader>
            <CardContent>
              <Select
              //  value={filters.employeeLocation}
              // onValueChange={value => handleFilterChange('employeeLocation', value)}
              >
                <SelectTrigger className="bg-slate-400/10 border-none">
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
            </CardContent>
          </Card>
        )}
        {/* Date section section */}
        {hiddingFilter && (
          <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-3 xl:col-span-3">
            <CardHeader className="flex flex-row items-center justify-between p-2">
              <CardTitle className="text-sm font-bold">Departments</CardTitle>
            </CardHeader>
            <CardContent>
              <Select
              // value={filters.employeeDepartment}
              // onValueChange={value => handleFilterChange('employeeDepartment', value)}
              >
                <SelectTrigger className="bg-slate-400/10 border-none">
                  <SelectValue placeholder="All Department" />
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
            </CardContent>
          </Card>
        )}
        {/* Section for geo location setup */}
        {hiddingFilter && (
          <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-3 xl:col-span-3">
            <CardHeader className="flex flex-row items-center justify-between p-2">
              <CardTitle className="text-sm font-bold">Employee Type</CardTitle>
            </CardHeader>
            <CardContent>
              <Select
              //  value={filters.employeeDesignation}
              // onValueChange={value => handleFilterChange('employeeDesignation', value)}
              >
                <SelectTrigger className="bg-slate-400/10 border-none">
                  <SelectValue placeholder="All" />
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
            </CardContent>
          </Card>
        )}
        {/* Slider for checing timing */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-3 xl:col-span-3">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-sm font-bold">Month & Year</CardTitle>
          </CardHeader>
          <CardContent>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant={'outline'}
                  className={cn(
                    'w-full justify-between text-left font-normal bg-slate-400/10 border-none',
                    !date && 'text-muted-foreground'
                  )}>
                  {date ? (
                    format(date, 'MMMM yyyy')
                  ) : (
                    <span className="text-primary">Select Date</span>
                  )}
                  <CalendarDays className="h-6 w-6 text-primary" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="flex flex-col space-y-2 p-3 w-[200px]">
                {/* Month Select */}
                <Select
                  value={date.getMonth().toString()}
                  onValueChange={value => {
                    const selectedDate = new Date(
                      date.getFullYear(),
                      parseInt(value),
                      1
                    );
                    const isFutureMonth =
                      selectedDate.getFullYear() === new Date().getFullYear() &&
                      selectedDate.getMonth() > new Date().getMonth();
                    if (isFutureMonth)
                      return alert('This month is not yet completed.');
                    setDate(
                      prev => new Date(prev.getFullYear(), parseInt(value), 1)
                    );
                  }}>
                  <SelectTrigger>
                    <SelectValue placeholder="Select Month">
                      {format(new Date(2000, date.getMonth(), 1), 'MMMM')}
                    </SelectValue>
                  </SelectTrigger>
                  <SelectContent>
                    {Array.from({ length: 12 }).map((_, idx) => (
                      <SelectItem key={idx} value={idx.toString()}>
                        {format(new Date(2000, idx, 1), 'MMMM')}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <Select
                  value={date.getFullYear().toString()}
                  onValueChange={value =>
                    setDate(
                      prev => new Date(parseInt(value), prev.getMonth(), 1)
                    )
                  }>
                  <SelectTrigger>
                    <SelectValue placeholder="Select Year">
                      {date.getFullYear()}
                    </SelectValue>
                  </SelectTrigger>
                  <SelectContent className=" 2xl:h-[280px] md:h-[200px] h-[160px]">
                    {Array.from({
                      length: new Date().getFullYear() - 2000 + 1,
                    }).map((_, idx) => {
                      const year = 2000 + idx;
                      return (
                        <SelectItem key={year} value={year.toString()}>
                          {year}
                        </SelectItem>
                      );
                    })}
                  </SelectContent>
                </Select>
              </PopoverContent>
            </Popover>
          </CardContent>
        </Card>
      </div>
    </>
  );
};

export default AttendanceFilter;
