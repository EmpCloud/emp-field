import React, { useState, useRef } from 'react';
import { Card, CardContent, CardTitle } from '@/components/ui/card';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { Input } from '@/components/ui/input';
import { Search, X } from 'lucide-react';
import { getLocations, getDepartments, getRoles } from './Api/get';
import { useQuery } from '@tanstack/react-query';

const SearchInput = ({ dataType, onSelect }) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isPending, setIsPending] = useState(false);
  const [open, setOpen] = useState(false);
  const inputRef = useRef(null);

  // Fetch location, role, or department data based on dataType prop
  const { data: locationData } = useQuery({
    queryKey: 'getLocations',
    queryFn: getLocations,
    enabled: dataType === 'location',
  });
  const locations = locationData?.data?.body?.data?.Locations || [];

  const { data: roleData } = useQuery({
    queryKey: 'getRoles',
    queryFn: getRoles,
    enabled: dataType === 'role',
  });
  const roles = roleData?.data?.body?.data || [];

  const { data: departmentData } = useQuery({
    queryKey: 'getDepartments',
    queryFn: getDepartments,
    enabled: dataType === 'department',
  });
  const departments = departmentData?.data?.body?.data?.Department || [];

  const dataToDisplay =
    dataType === 'location'
      ? locations
      : dataType === 'role'
        ? roles
        : departments;

  const filteredData = searchQuery
    ? dataToDisplay.filter(
        item =>
          item.locationName
            ?.toLowerCase()
            .includes(searchQuery.toLowerCase()) ||
          item.role?.toLowerCase().includes(searchQuery.toLowerCase()) ||
          item.department?.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : dataToDisplay;

  const handleSelect = item => {
    const selectedOption =
      item.locationName || item.role || item.department || '';
    if (onSelect) {
      onSelect(selectedOption);
    }
    setSearchQuery(item.locationName || item.role || item.department || '');
    setOpen(false);
  };

  return (
    <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-4 w-full">
      <CardContent>
        <Popover open={open} onOpenChange={setOpen}>
          <PopoverTrigger asChild>
            <div
              ref={inputRef}
              onClick={() => inputRef.current?.focus()}
              className="relative w-full">
              <div className="border-none px-4 relative rounded-md w-full max-w-full mt-1 mb-4">
                <Input
                  placeholder={`Search ${dataType}`}
                  type="text"
                  value={searchQuery}
                  className="bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-[#4D4C4C] font-extrabold text-xs 2xl:text-sm"
                  onChange={e => {
                    setSearchQuery(e.target.value);
                    setOpen(true); // Ensure popover remains open on query change
                  }}
                  style={{
                    padding: '10px',
                    height: '34px',
                    width: '180px',
                    margin: 'auto',
                  }}
                />
                {searchQuery !== '' ? (
                  <X
                    className="absolute top-2/4 right-9 -translate-y-2/4 w-4 h-4 cursor-pointer font-bold"
                    onClick={e => {
                      e.stopPropagation();
                      setSearchQuery('');
                      setOpen(true); // Reopen popover when clearing query
                    }}
                  />
                ) : (
                  <Search className="absolute top-2/4 right-9 -translate-y-2/4 w-4 h-4 cursor-pointer" />
                )}
              </div>
            </div>
          </PopoverTrigger>
          <PopoverContent
            align="start"
            className="max-h-[200px] overflow-y-auto p-2 min-w-40 z-50">
            {filteredData.length > 0 ? (
              filteredData.map(item => (
                <div
                  key={item.id || item._id}
                  className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 text-[#1F3A78] px-4 py-1 cursor-pointer"
                  onClick={() => handleSelect(item)}>
                  <CardTitle
                    className="text-xs 2xl:text-sm"
                    style={{ color: '#1F3A78', fontWeight: 'bold' }}>
                    {item.locationName || item.role || item.department || ''}
                  </CardTitle>
                </div>
              ))
            ) : (
              <div className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                <CardTitle className="text-xs 2xl:text-sm">
                  {isPending ? 'loading...' : `No ${dataType} found`}
                </CardTitle>
              </div>
            )}
          </PopoverContent>
        </Popover>
      </CardContent>
    </Card>
  );
};

export default SearchInput;
