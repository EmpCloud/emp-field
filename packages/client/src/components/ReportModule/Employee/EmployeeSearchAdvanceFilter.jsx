import React, { useState, useRef } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { Input } from '@/components/ui/input';
import { Search, X } from 'lucide-react';

const employeeData = [
  {
    id: 1,
    emp_name: 'Jonathan',
  },
  {
    id: 2,
    emp_name: 'Samantha',
  },
  {
    id: 3,
    emp_name: 'Elizabeth',
  },
  {
    id: 4,
    emp_name: 'Alexander',
  },
  {
    id: 5,
    emp_name: 'Michael',
  },
  {
    id: 6,
    emp_name: 'Victoria',
  },
  {
    id: 7,
    emp_name: 'Catherine',
  },
  {
    id: 8,
    emp_name: 'Sebastian',
  },
  {
    id: 9,
    emp_name: 'Margaret',
  },
  {
    id: 10,
    emp_name: 'William',
  },
];

const EmployeeSearchAdvanceFilter = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isPending, setIsPending] = useState(false); // Simulating loading state
  const [open, setOpen] = useState(false);
  const inputRef = useRef(null);

  const handleFocus = () => {
    inputRef.current?.focus();
  };

  const handleSearchedEmployee = emp_name => {
    setSearchQuery(emp_name);
    setOpen(false); // Close the popover after selection
  };

  const filteredData = searchQuery
    ? employeeData.filter(employee =>
        employee.emp_name.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : employeeData;

  return (
    <Card className="border-none sm:w-[244px] w-full shadow-none">
      <CardHeader className="flex flex-row items-center justify-between p-2">
        <CardTitle className="text-xs font-semibold">Search Employee</CardTitle>
      </CardHeader>
      <CardContent>
        <Popover open={open} onOpenChange={setOpen}>
          <PopoverTrigger asChild>
            <div
              ref={inputRef}
              onClick={handleFocus}
              className="relative w-full">
              <div className="border-none relative rounded-md w-full max-w-full">
                <Input
                  placeholder="Search Employee"
                  type="text"
                  value={searchQuery}
                  className="bg-slate-400/10 ps-3 h-7 placeholder:font-semibold placeholder:text-[#4D4C4C] text-[#4D4C4C] font-semibold text-xs 2xl:text-sm"
                  onChange={e => {
                    setSearchQuery(e.target.value);
                    setOpen(true); // Ensure popover remains open on query change
                  }}
                />
                {searchQuery !== '' ? (
                  <X
                    className="absolute top-2/4 right-2 -translate-y-2/4 w-4 h-4 cursor-pointer font-bold"
                    onClick={e => {
                      e.stopPropagation();
                      setSearchQuery('');
                      setOpen(true); // Reopen popover when clearing query
                    }}
                  />
                ) : (
                  <Search className="absolute top-2/4 right-2 -translate-y-2/4 w-4 h-4 2xl:w-5 2xl:h-5 cursor-pointer" />
                )}
              </div>
            </div>
          </PopoverTrigger>
          <PopoverContent
            align="start"
            className="max-h-[200px] overflow-y-auto p-2 min-w-40 z-50">
            {filteredData.length > 0 ? (
              filteredData.map(employee => (
                <div
                  key={employee.id}
                  className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 text-[#1F3A78] px-4 py-1 cursor-pointer"
                  onClick={() => handleSearchedEmployee(employee.emp_name)}>
                  <CardTitle className="text-xs 2xl:text-sm text-[#1F3A78] font-semibold">
                    {employee.emp_name}
                  </CardTitle>
                </div>
              ))
            ) : (
              <div className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                <CardTitle className="text-xs 2xl:text-sm">
                  {isPending ? 'loading...' : 'No employee found'}
                </CardTitle>
              </div>
            )}
          </PopoverContent>
        </Popover>
      </CardContent>
    </Card>
  );
};

export default EmployeeSearchAdvanceFilter;
