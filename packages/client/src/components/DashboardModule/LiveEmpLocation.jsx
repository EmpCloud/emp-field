import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { CalendarDays, Search } from 'lucide-react';
import React, { useState, useMemo } from 'react';
import { usersLastLocationDetails } from './Api/get';
import { useQuery } from '@tanstack/react-query';
import { GoogleMap, useLoadScript } from '@react-google-maps/api';
import LiveLocationMarkerImage from './LiveLocationMarkerImage';
import { Button } from '@/components/ui/button';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { cn } from '@/lib/utils';
import { addDays } from 'date-fns';
import { Calendar as CalendarCom } from '@/components/ui/calendar';
import moment from 'moment';

const LiveEmpLocation = () => {
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: import.meta.env.VITE_SOME_KEY,
  });
  const INITIALS = import.meta.env.VITE_DICE_BEAR;
  const [date, setDate] = useState(moment().startOf('day').toDate());
  const [selectedEmployee, setSelectedEmployee] = useState(null);

  const { isLoading, error, data } = useQuery({
    queryKey: ['usersLastLocationDetails', date],
    queryFn: () =>
      usersLastLocationDetails({ date: moment(date).format('YYYY-MM-DD') }),
  });

  const UsersData = data?.data?.body?.data;

  const [open, setOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const employeeData =
    UsersData?.userData?.map(user => ({
      id: user.user.emp_id,
      label: user.user.fullName,
      value: user.user.fullName.toLowerCase(),
      location: {
        lat: parseFloat(user.currentLocation.latitude),
        lng: parseFloat(user.currentLocation.longitude),
      },
    })) || [];

  const handleBlur = () => setOpen(false);
  const handleFocus = () => setOpen(true);

  const handleSearchedEmployee = id => {
    const employee = employeeData.find(e => e.id === id);
    if (employee) {
      setSearchQuery(employee.label);
      setSelectedEmployee(employee);
      setOpen(false);
    }
  };

  const handleAutoFocus = e => {
    e.preventDefault();
  };

  const defaultCenter = useMemo(() => {
    if (UsersData?.userData?.length) {
      return {
        lat:
          UsersData.userData.reduce(
            (sum, user) => sum + user.currentLocation.latitude,
            0
          ) / UsersData.userData.length,
        lng:
          UsersData.userData.reduce(
            (sum, user) => sum + user.currentLocation.longitude,
            0
          ) / UsersData.userData.length,
      };
    }
    return { lat: 12.9784, lng: 77.6408 };
  }, [UsersData]);

  const defaultZoom = 10;
  const zoomedInZoom = 15;

  if (isLoading)
    return (
      <div className="card-shadow grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg animate-pulse">
        <Card>
          <CardHeader className="flex flex-row items-center bg-slate-300 rounded-t-lg px-4 2xl:h-12 h-10">
            <CardTitle className="text-xs 2xl:text-sm font-bold text-white"></CardTitle>
          </CardHeader>
          <CardContent className="p-3 h-[300px] 2xl:h-[500px] flex justify-center flex-col items-center gap-3">
            <div className="h-[calc(100%)] w-full relative bg-slate-200 rounded-sm"></div>
          </CardContent>
        </Card>
      </div>
    );
  if (error) return <div>Error loading data</div>;

  return (
    <div className="card-shadow grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between bg-gradient rounded-t-lg px-4 2xl:h-12 h-10">
          <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
            Live Employee Location
          </CardTitle>
          <Popover>
            <PopoverTrigger asChild>
              <div className="flex justify-center items-center gap-3">
                <div className="text-white text-sm font-medium whitespace-nowrap hidden lg:block">
                  Select Date
                </div>
                <Button
                  variant={'outline'}
                  className={cn(
                    '2xl:h-8 w-full justify-between text-left border-none placeholder:font-extrabold',
                    !date && 'text-muted-foreground'
                  )}>
                  {date ? (
                    moment(date).format('MMMM D, YYYY')
                  ) : (
                    <span className="text-slate-500/50 text-xs 2xl:text-sm">
                      Select Date
                    </span>
                  )}
                  <CalendarDays className="h-4 2xl:h-4 2xl:w-4 w-4 text-primary" />
                </Button>
              </div>
            </PopoverTrigger>
            <PopoverContent className="flex w-auto flex-col space-y-2 p-2">
              <Select
                onValueChange={value => {
                  const selectedDate = addDays(new Date(), parseInt(value));
                  setDate(selectedDate);
                }}>
                <SelectTrigger>
                  <SelectValue placeholder="Select" />
                </SelectTrigger>
                <SelectContent position="popper">
                  <SelectItem value="0">Today</SelectItem>
                  <SelectItem value="-1">Yesterday</SelectItem>
                  <SelectItem value="-2">Before 2 days</SelectItem>
                  <SelectItem value="-3">Before 3 days</SelectItem>
                </SelectContent>
              </Select>
              <div className="rounded-md border">
                <CalendarCom
                  mode="single"
                  disabled={date => {
                    const currentDate = moment().startOf('day');
                    const minDate = moment(UsersData?.orgStartDate).startOf(
                      'day'
                    );
                    return (
                      moment(date).isBefore(minDate) ||
                      moment(date).isAfter(currentDate)
                    );
                  }}
                  selected={date}
                  onSelect={newDate => setDate(newDate)}
                  min={moment(UsersData?.orgStartDate).toDate()}
                  max={moment().toDate()}
                />
              </div>
            </PopoverContent>
          </Popover>
        </CardHeader>
        <CardContent className="p-3 h-[300px] 2xl:h-[500px] w-full card-container relative">
          <Card className="border-none shadow-none absolute top-5 left-1/2 -translate-x-1/2 w-[60%] z-[1]">
            <CardContent>
              <Popover onOpenChange={setOpen} className="relative">
                <PopoverTrigger asChild className="">
                  <div className="flex justify-between items-center px-4 py-1">
                    <Search size={16} />
                    <Input
                      type="search"
                      placeholder="Search for an Employee"
                      value={searchQuery}
                      className="ps-3 text-xs 2xl:text-sm"
                      onFocus={handleFocus}
                      onBlur={handleBlur}
                      onChange={e => {
                        setSearchQuery(e.target.value);
                        if (!e.target.value) {
                          setSelectedEmployee(null); // Reset selected employee
                        }
                      }}
                    />
                  </div>
                </PopoverTrigger>
                <PopoverContent
                  className="max-h-[200px] overflow-y-auto m-0 min-w-40 p-0"
                  align="start"
                  onOpenAutoFocus={handleAutoFocus}>
                  {searchQuery === '' ? (
                    employeeData.map((employee, index) => (
                      <div
                        key={index}
                        className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer"
                        onClick={() => handleSearchedEmployee(employee.id)}>
                        <CardTitle className="text-sm font-medium">
                          {employee.label}
                        </CardTitle>
                      </div>
                    ))
                  ) : employeeData.filter(employee =>
                      employee.value.includes(searchQuery.toLowerCase())
                    ).length > 0 ? (
                    employeeData
                      .filter(employee =>
                        employee.value.includes(searchQuery.toLowerCase())
                      )
                      .map(employee => (
                        <div
                          key={employee.id}
                          className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer"
                          onClick={() => handleSearchedEmployee(employee.id)}>
                          <CardTitle className="text-sm font-medium">
                            {employee.label}
                          </CardTitle>
                        </div>
                      ))
                  ) : (
                    <div className="p-4 text-sm text-center text-slate-500">
                      No employee found
                    </div>
                  )}
                </PopoverContent>
              </Popover>
            </CardContent>
          </Card>
          {isLoaded && (
            <GoogleMap
              options={{
                mapTypeControl: true,
                mapTypeControlOptions: {
                  position: window.google.maps.ControlPosition.BOTTOM_LEFT,
                },
              }}
              center={
                selectedEmployee ? selectedEmployee.location : defaultCenter
              }
              zoom={selectedEmployee ? zoomedInZoom : defaultZoom}
              mapContainerStyle={{ width: '100%', height: '100%' }}>
              {UsersData?.userData?.map(user => {
                const profilePic =
                  user?.user?.profilePic === 'PROFILE' ||
                  user?.user?.profilePic === 'profile' ||
                  user?.user?.profilePic === undefined ||
                  user?.user?.profilePic === null
                    ? `${INITIALS}${user?.user?.fullName}`
                    : user?.user?.profilePic;
                return (
                  <LiveLocationMarkerImage
                    key={user.user.id}
                    livePosition={{
                      lat: parseFloat(user?.currentLocation?.latitude ?? 0),
                      lng: parseFloat(user?.currentLocation?.longitude ?? 0),
                    }}
                    employeeProfile={profilePic}
                    employeeName={user?.user?.fullName ?? ''}
                    employeeDetails={user?.user}
                  />
                );
              })}
            </GoogleMap>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default LiveEmpLocation;
