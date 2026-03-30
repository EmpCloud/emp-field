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
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Separator } from '@/components/ui/separator';
import { cn } from '@/lib/utils';
import { addDays, format } from 'date-fns';
import { CalendarDays, SquarePlus } from 'lucide-react';
import { useState } from 'react';

const LeavesFilter = () => {
  const [date, setDate] = useState();
  return (
    <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-6">
      <Button className="col-span-6 bg-[#6A6AEC] hover:bg-[#6A6AEC]/90 py-6">
        Employee Leaves List
      </Button>
      <Button className="col-span-6 bg-[#6A6AEC]/70 hover:bg-[#6A6AEC]/80 py-6">
        Balance Leave For Employee
      </Button>
      <Separator className="col-span-12 bg-[#2873D833] h-[1.4px] rounded" />
      <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-3">
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Select Month</CardTitle>
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
                  format(date, 'PPP')
                ) : (
                  <span className="text-primary">Select Date</span>
                )}
                <CalendarDays className="h-6 w-6 text-primary" />
              </Button>
            </PopoverTrigger>
            <PopoverContent className="flex w-auto flex-col space-y-2 p-2">
              <Select
                onValueChange={value =>
                  setDate(addDays(new Date(), parseInt(value)))
                }>
                <SelectTrigger>
                  <SelectValue placeholder="Select" />
                </SelectTrigger>
                <SelectContent position="popper">
                  <SelectItem value="0">Today</SelectItem>
                  <SelectItem value="1">Tomorrow</SelectItem>
                  <SelectItem value="3">In 3 days</SelectItem>
                  <SelectItem value="7">In a week</SelectItem>
                </SelectContent>
              </Select>
              <div className="rounded-md border">
                <Calendar mode="single" selected={date} onSelect={setDate} />
              </div>
            </PopoverContent>
          </Popover>
        </CardContent>
      </Card>
      <Button className="col-span-3 col-start-10 self-end bg-transparent text-[#1F3A78] border font-semibold border-[#1F3A78] hover:bg-[#6A6AEC]/80 py-6">
        <SquarePlus className="me-2" />
        Add New Leaves
      </Button>
    </div>
  );
};

export default LeavesFilter;
