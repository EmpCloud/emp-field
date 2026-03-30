import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
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
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Search } from 'lucide-react';
import { useEffect, useState } from 'react';
import { dashboardEmployeesTrackingData } from './Api/get';
import { useInfiniteQuery, useQuery } from '@tanstack/react-query';
import moment from 'moment';
import { empoyeeSearchedSuggestions } from 'components/FilterModule/Api/get';
import { RxCross1 } from 'react-icons/rx';
import {
  RiArrowDropLeftLine,
  RiArrowDropRightLine,
  RiArrowLeftDoubleFill,
  RiArrowRightDoubleFill,
} from 'react-icons/ri';
import { useInView } from 'react-intersection-observer';
import { Button } from '@/components/ui/button';
import { cn } from '@/lib/utils';
import { Calendar as CalendarCom } from '@/components/ui/calendar';
import { CalendarDays } from 'lucide-react';
import { addDays } from 'date-fns';

const Employees = () => {
  const [date, setDate] = useState(moment().startOf('day').toDate());
  const [employeeStatus, setEmployStatus] = useState('All');

  const [employeeId, setEmployeEmpId] = useState('');
  const { ref, inView } = useInView();
  // const {
  //   data,
  //   status,
  //   isLoading,
  //   error,
  //   fetchNextPage,
  //   isFetchingNextPage,
  //   hasNextPage,
  // } = useInfiniteQuery({
  //   queryKey: ['fetchAllEmployeeData', employeeId],
  //   queryFn: ({ pageParam = 1 }) =>
  //     dashboardEmployeesTrackingData(pageParam, employeeId),
  //   getNextPageParam: (lastPage, allPages) => {
  //     return lastPage?.data?.body?.data?.userData?.length
  //       ? allPages?.length + 1
  //       : undefined;
  //   },
  // });

  const {
    data,
    status,
    isLoading,
    error,
    fetchNextPage,
    isFetchingNextPage,
    hasNextPage,
  } = useInfiniteQuery({
    queryKey: ['fetchAllEmployeeData', employeeId, date, employeeStatus],
    // queryKey: ['fetchAllEmployeeData'],

    queryFn: ({ pageParam = 1 }) =>
      dashboardEmployeesTrackingData(
        pageParam,
        employeeId,
        moment(date).format('YYYY-MM-DD'),
        employeeStatus
      ),
    // getNextPageParam: (lastPage, allPages) => {
    //   const userDataLength = lastPage?.data?.body?.data?.userData?.length || 0;
    //   return userDataLength === 10 ? allPages.length + 1 : undefined;
    // },

    getNextPageParam: (lastPage, allPages) => {
      const userData = lastPage?.data?.body?.data?.userData || [];
      const totalRecords = lastPage?.data?.body?.data?.totalCount || 0; // Assuming totalRecords is returned by API
      const currentTotalFetched = allPages.reduce(
        (acc, page) => acc + (page?.data?.body?.data?.userData?.length || 0),
        0
      );

      // Check if more pages are available based on total records and current fetched records
      return currentTotalFetched < totalRecords
        ? allPages.length + 1
        : undefined;
    },
  });

  useEffect(() => {
    if (inView && hasNextPage) {
      fetchNextPage();
    }
  }, [inView, hasNextPage, fetchNextPage]);

  if (status === 'loading') {
    return <p>Loading...</p>;
  }

  if (status === 'error') {
    return <p>Error: {error.message}</p>;
  }

  const userData =
    data?.pages?.flatMap(page => page?.data?.body?.data?.userData) || [];
  const [open, setOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const handleBlur = () => {
    setOpen(false);
  };

  const handleFocus = () => {
    setOpen(true);
  };

  const handleSearchedEmployee = id => {
    setEmployeEmpId(id);
    const filteredEmployee = employeeData.filter(
      employee => employee.id === id
    );
    const { label } = filteredEmployee[0];
    setSearchQuery(label);
    setOpen(false);
  };

  const response = useQuery({
    queryKey: ['searchQueryDashboard', searchQuery],
    queryFn: () => empoyeeSearchedSuggestions(searchQuery),
  });
  const isLoadingEmployee = response.isLoading;
  const handleAutoFocus = e => {
    e.preventDefault();
  };

  const employeeData = response?.data?.body?.data?.resultData?.map(data => ({
    profilePic: data?.profilePic,
    org_id: data?.orgId,
    id: data?.emp_id,
    value: data?.fullName,
    label: data?.fullName,
  }));

  if (isLoading)
    return (
      <div className="card-shadow grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg animate-pulse">
        <Card>
          <CardHeader className="flex flex-row items-center bg-slate-300 rounded-t-lg px-4 2xl:h-12 h-10">
            <CardTitle className="text-xs 2xl:text-sm font-bold text-white"></CardTitle>
          </CardHeader>
          <CardContent className="p-3 h-[300px] 2xl:h-[500px] flex justify-center flex-col items-center gap-3">
            <div className="h-20 w-40 relative bg-slate-200 rounded-sm mr-auto"></div>
            <div className="h-20 w-full relative bg-slate-200 rounded-sm"></div>
            <div className="h-20 w-full relative bg-slate-200 rounded-sm"></div>
            <div className="h-20 w-full relative bg-slate-200 rounded-sm"></div>
            <div className="h-20 w-full relative bg-slate-200 rounded-sm"></div>
            <div className="h-20 w-full relative bg-slate-200 rounded-sm"></div>
          </CardContent>
        </Card>
      </div>
    );

  const handleCheckInCheckout = value => {
    setEmployStatus(value);
  };

  return (
    <>
      <div className="card-shadow grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg">
        <Card className="overflow-x-auto">
          <CardHeader className="flex flex-row items-center justify-between bg-gradient rounded-t-lg px-4 2xl:h-12 h-10">
            <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
              Employees
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
                      // const minDate = moment()
                      //   .subtract( 90,'days')
                      //   .startOf('day');
                      return (
                        // moment(date).isBefore(minDate) ||
                        moment(date).isAfter(currentDate)
                      );
                    }}
                    selected={date}
                    onSelect={newDate => setDate(newDate)}
                  />
                </div>
              </PopoverContent>
            </Popover>
          </CardHeader>
          <CardContent className="p-3 h-[300px] 2xl:h-[500px] flex justify-start flex-col items-center gap-3">
            <div className="flex justify-between w-full">
              <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-6 xl:col-span-6">
                <CardContent>
                  <Popover open={open} className="relative">
                    <PopoverTrigger asChild className="">
                      <div className="flex justify-between items-center bg-slate-400/10 ps-2 pe-4 rounded relative">
                        <Search size={16} />
                        <Input
                          type="text"
                          placeholder="Search Employee"
                          value={searchQuery}
                          className="bg-transparent ps-2"
                          onFocus={handleFocus}
                          onBlur={handleBlur}
                          onChange={e => {
                            setSearchQuery(e.target.value);
                          }}
                        />
                        {searchQuery !== '' && (
                          <RxCross1
                            className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                            onClick={() => {
                              setSearchQuery('');
                              setEmployeEmpId('');
                              response?.refetch();
                            }}
                          />
                        )}
                      </div>
                    </PopoverTrigger>
                    <PopoverContent
                      onOpenAutoFocus={handleAutoFocus}
                      align="start"
                      className="max-h-[200px] overflow-y-auto p-2 min-w-40">
                      {employeeData && employeeData.length > 0 ? (
                        <>
                          {employeeData?.map((username, index) => (
                            <CardTitle
                              key={index}
                              onClick={() =>
                                handleSearchedEmployee(username.id)
                              }
                              className="text-xs font-medium cursor-pointer py-1">
                              {username.label}
                            </CardTitle>
                          ))}
                        </>
                      ) : (
                        <>
                          <CardTitle className="text-xs font-medium cursor-pointer py-1">
                            {isLoadingEmployee
                              ? 'loading...'
                              : 'No employee found'}
                          </CardTitle>
                        </>
                      )}
                    </PopoverContent>
                  </Popover>
                </CardContent>
              </Card>
              <Select onValueChange={handleCheckInCheckout}>
                <SelectTrigger
                  className="w-[180px] bg-slate-400/10"
                  // disabled={true}
                >
                  <SelectValue
                    placeholder={
                      employeeStatus == 'Check In'
                        ? 'Check In'
                        : employeeStatus == 'Check Out'
                          ? 'Check Out'
                          : 'All'
                    }
                  />
                </SelectTrigger>
                <SelectContent>
                  <SelectGroup>
                    {/* <SelectLabel>All</SelectLabel> */}
                    <SelectItem value="All">All</SelectItem>
                    <SelectItem value="Check In">Check In</SelectItem>
                    <SelectItem value="Check Out">Check Out</SelectItem>
                    {/* <SelectItem value="blueberry">Blueberry</SelectItem>
                    <SelectItem value="grapes">Grapes</SelectItem>
                    <SelectItem value="pineapple">Pineapple</SelectItem> */}
                  </SelectGroup>
                </SelectContent>
              </Select>
            </div>

            <div className="h-[calc(100%-40px)] w-full">
              <Table>
                <TableHeader className="sticky top-0">
                  <TableRow className="bg-[#e7e7ff] h-[40px]">
                    <TableHead className="text-xs font-semibold text-[#1F3A78]">
                      Employee
                    </TableHead>
                    <TableHead className="text-xs font-semibold text-[#1F3A78]">
                      Attendance
                    </TableHead>
                    <TableHead className="text-xs font-semibold text-[#1F3A78]">
                      Last Location
                    </TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {userData && userData.length <= 0 ? (
                    <>
                      <TableRow>
                        <TableCell colSpan={3} style={{ textAlign: 'center' }}>
                          No employees found
                        </TableCell>
                      </TableRow>
                    </>
                  ) : (
                    userData[0] !== undefined &&
                    userData.map((employDetails, index) => (
                      <TableRow key={index}>
                        <TableCell>
                          <div className="flex gap-3 justify-start items-center w-full">
                            <div className="img w-10 h-10 rounded-[50%] overflow-hidden">
                              {employDetails?.user?.profilePic &&
                              employDetails?.user?.profilePic !== 'PROFILE' &&
                              employDetails?.user?.profilePic !== 'profile' ? (
                                <img
                                  src={employDetails?.user?.profilePic}
                                  alt=""
                                  className="w-full"
                                />
                              ) : (
                                <img
                                  src={`${import.meta.env.VITE_DICE_BEAR}${employDetails?.fullName}`}
                                  alt=""
                                  className="w-full"
                                />
                              )}
                            </div>
                            <p className="text-xs font-semibold w-[calc(100%-2.5rem-0.75rem)]">
                              {employDetails?.fullName}
                            </p>
                          </div>
                        </TableCell>
                        <TableCell>
                          {employDetails?.checkIn ? (
                            <div className="flex flex-col gap-3 font-semibold text-[#4D4C4C] text-xs">
                              <p>
                                <span className="text-[#4DB948]">
                                  Check In :
                                </span>{' '}
                                {moment(employDetails?.checkIn).format(
                                  'hh:mm A'
                                )}
                              </p>
                              <p>
                                {employDetails?.checkOut != null ? (
                                  <>
                                    <span className="text-[#E9333B]">
                                      Check Out :
                                    </span>{' '}
                                    {moment(employDetails?.checkOut).format(
                                      'hh:mm A'
                                    )}
                                  </>
                                ) : (
                                  ''
                                )}
                              </p>
                            </div>
                          ) : (
                            '-------------'
                          )}
                        </TableCell>
                        <TableCell>
                          <div className="font-semibold text-xs">
                            {employDetails?.address || '  ___________'}
                          </div>
                        </TableCell>
                      </TableRow>
                    ))
                  )}
                  {isFetchingNextPage && (
                    <TableRow>
                      <TableCell colSpan="3">
                        {userData?.length % 2 == 0
                          ? 'Loading more...'
                          : 'No more'}
                      </TableCell>
                    </TableRow>
                  )}
                </TableBody>
                <div ref={ref} className="h-1"></div>
              </Table>
            </div>
          </CardContent>
        </Card>
      </div>
    </>
  );
};

export default Employees;
