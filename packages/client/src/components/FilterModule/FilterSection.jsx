import * as React from 'react';
import { CalendarDays } from 'lucide-react';
import { FaAngleDown } from 'react-icons/fa';

import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';

import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';

import { addDays, format } from 'date-fns';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import { useQuery } from '@tanstack/react-query';

import { Calendar as CalendarCom } from '@/components/ui/calendar';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';

import { Slider } from '@/components/ui/slider';

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import Cookies from 'js-cookie';
import TimeLineBox from 'components/TimelineModule/TimeLineBox';
import { TbTimeline } from 'react-icons/tb';
import Timeline from 'components/TimelineModule/Timeline';
import GeoLocation from 'components/GeoLocationModule/GeoLocation';
import {
  empUserById,
  userFreguencyandGeoLocationUpdate,
  empoyeeSearchedSuggestions,
} from './Api/get';
import { updateFrequencyRadius } from './Api/post';
import { updateSnapDetails } from './Api/post';

import Lottie from 'lottie-react';
import employeeLoadingAnimation from 'assets/loader/emp-animation.json';
import { RxCross1 } from 'react-icons/rx';
import { Separator } from '@/components/ui/separator';
import { toast } from 'sonner';

// if (isPending) return 'Loading...'

// if (error) return 'An error has occurred: ' + error.message

const FilterSection = () => {
  const [date, setDate] = React.useState(new Date());
  const [open, setOpen] = React.useState(false);
  const [searchQuery, setSearchQuery] = React.useState('');
  const [clear, setClear] = React.useState(true);
  const [employeEmpId, setEmployeEmpId] = React.useState('');
  const [taskDetails, setTaskDeatils] = React.useState(null);
  const [employee, setemployee] = React.useState(null);
  const [employeeFrequency, setemployeeFrequency] = React.useState(null);
  const [sliderValue, setSliderValue] = React.useState(employeeFrequency ?? 0);
  const [locationData, setLocationData] = React.useState(null);
  const [geolocationStatus, setGeoLoCationStatus] = React.useState(null);
  const [timeoutId, setTimeoutId] = React.useState(null);
  const [isAdvanceSettingOpen, setisAdvanceSettingOpen] = React.useState(false);
  const [transportDetails, setTransportDetails] = React.useState(null);
  const [employeeOrgId, setEmployeOrgId] = React.useState(null);
  const [employeeProfile, setEmployeProfile] = React.useState(null);
  const [frequency, setFrequency] = React.useState(
    transportDetails?.currentFrequency
  );
  const [radius, setRadius] = React.useState(transportDetails?.currentRadius);
  const [snapPointsLimit, setSnapPointsLimit] = React.useState(100);
  const [snapDurationLimit, setSnapDurationLimit] = React.useState(60);

  const handleEmpByUserId = React.useCallback(() => {
    if (employeEmpId) {
      empUserById(employeEmpId, convertDate(date)).then(response => {
        if (response.data.body.status === 'success') {
          setTaskDeatils(response?.data?.body?.data?.modifiedArray);
          setemployee(response?.data?.body?.data?.employeeDetails);
          setTransportDetails(response?.data?.body?.data?.transportDetails);
          setLocationData(response?.data?.body?.data);
          setemployeeFrequency(
            response?.data?.body?.data?.employeeDetails?.frequency
          );
          setGeoLoCationStatus(
            response?.data?.body?.data?.employeeDetails?.geoLogsStatus
          );

          // Clear existing timeout if any
          if (timeoutId) {
            clearTimeout(timeoutId);
          }

          // Filter location data for statuses 5 and 6
          const filteredData =
            response?.data?.body?.data?.trackingData[0]?.geologs?.filter(
              item => item.status === 5 || item.status === 6
            );

          // Check if there's a check-in status (5) without a checkout status (6)
          const hasCheckinWithoutCheckout =
            filteredData?.some(item => item.status === 5) &&
            !filteredData?.some(item => item.status === 6);

          // If check-in exists without checkout, set a new timeout
          if (hasCheckinWithoutCheckout && employeeFrequency) {
            const timeoutDuration =
              employeeFrequency < 5 ? 5000 : employeeFrequency * 1000;
            const newTimeoutId = setTimeout(() => {
              handleEmpByUserId();
            }, timeoutDuration);
            setTimeoutId(newTimeoutId);
          }
        }
      });
    }
  }, [employeEmpId, date, employeeFrequency, timeoutId]);

  const handleUpdateFrequency = async updatedData => {
    const data = {
      configFrequency: parseInt(frequency, 10) * 30,
      configMode: transportDetails?.currentMode,
      ...updatedData,
    };
    try {
      const response = await updateFrequencyRadius(
        employeEmpId,
        employeeOrgId,
        data
      );
      if (response?.data?.body?.status === 'success') {
        handleEmpByUserId();
        toast.success(response ? response?.data?.body?.message : '');
      }
    } catch (error) {
      console.error('Error updating frequency and radius:', error);
    }
  };
  const handleUpdateRadius = async updatedData => {
    const data = {
      configRadius: parseInt(radius, 10),
      configMode: transportDetails?.currentMode,
      ...updatedData,
    };
    try {
      const response = await updateFrequencyRadius(
        employeEmpId,
        employeeOrgId,
        data
      );
      if (response?.data?.body?.status === 'success') {
        handleEmpByUserId();
        toast.success(response ? response?.data?.body?.message : '');
      }
    } catch (error) {
      console.error('Error updating frequency and radius:', error);
    }
  };
  const handleSnapPointsChange = value => {
    setSnapPointsLimit(Number(value));
  };

  const handleSnapDurationChange = value => {
    setSnapDurationLimit(Number(value));
  };

  const handleUpdate = async (snapDurationLimit, snapPointsLimit) => {
    if (snapPointsLimit !== undefined) {
    }
    let data;
    snapPointsLimit !== undefined && snapPointsLimit
      ? (data = {
          snap_points_limit: snapPointsLimit,
          snap_duration_limit: employee?.snap_duration_limit,
        })
      : '';
    snapDurationLimit !== undefined && snapDurationLimit
      ? (data = {
          snap_points_limit: employee?.snap_points_limit,
          snap_duration_limit: snapDurationLimit,
        })
      : '';

    try {
      const response = await updateSnapDetails(
        employeEmpId,
        employeeOrgId,
        data
      );
      if (response?.data?.body?.status === 'success') {
        handleEmpByUserId();
        toast.success(response ? response?.data?.body?.message : '');
      } else {
        toast.success(response ? response?.data?.body?.message : 'Try again!');
      }
    } catch (error) {
      console.error('Error updating snap details:', error);
    }
  };

  const inputRef = React.useRef(null);
  const handleFocus = () => {
    setOpen(true);
  };

  const handleAutoFocus = e => {
    e.preventDefault();
  };

  React.useEffect(() => {
    const handleClickOutside = event => {
      if (inputRef.current && !inputRef.current.contains(event.target)) {
        setOpen(false);
      }
    };

    const handleScroll = () => {
      setOpen(false);
    };

    const pageContainer = document.querySelector('.page-container');

    document.addEventListener('mousedown', handleClickOutside);
    if (pageContainer) {
      pageContainer.addEventListener('scroll', handleScroll);
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      if (pageContainer) {
        pageContainer.removeEventListener('scroll', handleScroll);
      }
    };
  }, []);

  const handleSearchedEmployee = (id, orgId, profilePic) => {
    setEmployeEmpId(id);
    setEmployeOrgId(orgId);
    setEmployeProfile(profilePic);
    const filteredEmployee = employeeData.filter(
      employee => employee.id === id
    );
    const { label } = filteredEmployee[0];
    setSearchQuery(label);
    setClear(false);
  };

  const { isPending, error, data } = useQuery({
    queryKey: ['repoData', searchQuery],
    queryFn: () => empoyeeSearchedSuggestions(searchQuery),
  });
  // const { isPending, error, data } = useQuery(['repoData', searchQuery], () => empoyeeSearchedSuggestions(searchQuery));
  const employeeData =
    data?.body?.data?.resultData &&
    data?.body?.data?.resultData?.map(data => ({
      profilePic: data?.profilePic,
      org_id: data?.orgId,
      id: data?.emp_id,
      value: data?.fullName,
      label: data?.fullName,
    }));

  const dateRange = { date: date };

  function convertDate(inputDate) {
    const dateObj = new Date(inputDate);
    const year = dateObj.getFullYear();
    const month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
    const day = dateObj.getDate();
    return `${year}-${month}-${day.toString().padStart(2, '0')}`;
  }

  function isANumber(str) {
    return !/\D/.test(str);
  }

  React.useEffect(() => {
    if (employeEmpId && date) {
      handleEmpByUserId();
    }
  }, [employeEmpId, date]);

  const handleUserLocationandUserGeoLocationUpdate = value => {
    if (isANumber(value)) {
      let frequency = value;
      userFreguencyandGeoLocationUpdate(employeEmpId, frequency).then(
        response => {
          if (response.data.body.status === 'success') {
            handleEmpByUserId();
          }
        }
      );
    } else {
      userFreguencyandGeoLocationUpdate(employeEmpId, '', value).then(
        response => {
          if (response.data.body.status === 'success') {
            handleEmpByUserId();
          }
        }
      );
    }
  };

  const handlefreqvalue = value => {
    if (employeEmpId) {
      if (value !== undefined) {
        setSliderValue(value);
        handleUserLocationandUserGeoLocationUpdate(value[0]);
      }
    }
  };

  React.useLayoutEffect(() => {
    handlefreqvalue();
  }, []);

  // Clean up timeout when component unmounts
  // React.useEffect(() => {
  //   return () => {
  //     if (timeoutId) {
  //       clearTimeout(timeoutId);
  //     }
  //   };
  // }, [timeoutId]);

  // React.useEffect(() => {
  //   setSliderValue(employeeFrequency ?? 0);
  // }, [employeeFrequency]);

  const handleEmployeeGeoLocation = value => {
    setGeoLoCationStatus(value);
    handleUserLocationandUserGeoLocationUpdate(value);
  };

  // Clear timeout when component unmounts
  React.useEffect(() => {
    return () => {
      if (timeoutId) {
        clearTimeout(timeoutId);
      }
    };
  }, [timeoutId]);

  return (
    <>
      <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-3">
        {/* First box for searching employee */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-4">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Search Employee
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Popover open={open} className="relative">
              <PopoverTrigger asChild>
                <div ref={inputRef} onClick={handleFocus} className="relative">
                  <Input
                    type="text"
                    placeholder="Search Employee"
                    value={searchQuery}
                    className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                    onChange={e => {
                      setSearchQuery(e.target.value);
                    }}
                  />
                  {searchQuery !== '' ? (
                    <RxCross1
                      className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                      onClick={e => {
                        e.stopPropagation();
                        setClear(true);
                        setSearchQuery('');
                        setEmployeEmpId(null);
                        clearTimeout(timeoutId);
                      }}
                    />
                  ) : null}
                </div>
              </PopoverTrigger>
              <PopoverContent
                onOpenAutoFocus={handleAutoFocus}
                align="start"
                className="max-h-[200px] overflow-y-auto p-2 min-w-40">
                {searchQuery.length === '' ? (
                  employeeData &&
                  employeeData?.map((username, index) => (
                    <div
                      key={index}
                      className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer"
                      onClick={() =>
                        handleSearchedEmployee(
                          username?.id,
                          username?.org_id,
                          username?.profilePic
                        )
                      }>
                      <CardTitle
                        className="text-xs 2xl:text-sm"
                        onClick={() =>
                          handleSearchedEmployee(
                            username?.id,
                            username?.org_id,
                            username?.profilePic
                          )
                        }>
                        {username.label}
                      </CardTitle>
                    </div>
                  ))
                ) : employeeData && employeeData.length > 0 ? (
                  employeeData &&
                  employeeData
                    // .filter(username => username.value.toLowerCase().includes(searchQuery.toLowerCase()))
                    .map((username, index) => (
                      <div
                        key={index}
                        className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer"
                        onClick={() =>
                          handleSearchedEmployee(
                            username?.id,
                            username?.org_id,
                            username?.profilePic
                          )
                        }>
                        <CardTitle
                          className="text-xs 2xl:text-sm"
                          onClick={() =>
                            handleSearchedEmployee(
                              username?.id,
                              username?.org_id,
                              username?.profilePic
                            )
                          }>
                          {username.label}
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
        {/* Date section section */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-2">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Select Date
            </CardTitle>
          </CardHeader>
          <CardContent className="flex">
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant={'outline'}
                  className={cn(
                    'w-full justify-between text-left bg-slate-400/10 border-none placeholder:font-extrabold placeholder: font-extrabold',
                    !date && 'text-muted-foreground'
                  )}>
                  {date ? (
                    format(date, 'PPP')
                  ) : (
                    <span className="text-slate-500/50 text-xs 2xl:text-sm">
                      Select Date
                    </span>
                  )}
                  <CalendarDays className="h-4 2xl:h-4 2xl:w-4 w-4 text-primary" />
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
                  <CalendarCom
                    mode="single"
                    disabled={date => {
                      const currentDate = new Date();
                      const specificDate = new Date(employee?.createdAt);
                      return (
                        date > currentDate ||
                        (date < specificDate &&
                          date.toDateString() !== specificDate.toDateString())
                      );
                    }}
                    selected={date}
                    onSelect={setDate}
                  />
                </div>
              </PopoverContent>
            </Popover>
          </CardContent>
        </Card>
        {/* Section for geo location setup */}
        {employeEmpId ? (
          <>
            <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-2">
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-xs 2xl:text-sm font-bold hidden lg:block">
                  Geo Location
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex items-center justify-between lg:justify-around bg-gray-400/10 h-6 2xl:h-10 rounded-lg px-4">
                  <CardTitle className="text-xs 2xl:text-sm font-bold lg:hidden">
                    Geo Location
                  </CardTitle>
                  <Label
                    className="hidden md:block text-xs 2xl:text-sm"
                    htmlFor="location-off">
                    Disable
                  </Label>
                  <Switch
                    id="location-mode"
                    checked={geolocationStatus ?? false}
                    onCheckedChange={handleEmployeeGeoLocation}
                  />
                  <Label
                    className="hidden md:block text-xs 2xl:text-sm"
                    htmlFor="location-on">
                    Enable
                  </Label>
                </div>
              </CardContent>
            </Card>
            <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-2">
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-xs 2xl:text-sm font-bold">
                  Set Frequency
                </CardTitle>
              </CardHeader>
              <CardContent>
                <Select
                  value={frequency}
                  onValueChange={val => {
                    // Convert the selected value to seconds
                    const seconds = {
                      1: 30,
                      2: 40,
                      3: 40,
                      4: 60,
                      5: 90,
                      6: 120,
                      7: 150,
                      8: 180,
                      9: 210,
                      10: 240,
                      11: 270,
                      12: 300,
                    }[val];

                    setFrequency(val);
                    handleUpdateFrequency({
                      configFrequency: seconds,
                      // configMode: 'bike',
                    });
                  }}>
                  <SelectTrigger className="bg-gray-400/10 h-6 2xl:h-10 rounded-lg px-4 w-full border-none">
                    <SelectValue
                      placeholder={
                        (transportDetails &&
                          (transportDetails?.currentFrequency / 60 === 0.5
                            ? '30 sec'
                            : transportDetails?.currentFrequency % 60 === 0
                              ? transportDetails?.currentFrequency / 60 + ' min'
                              : transportDetails?.currentFrequency + ' sec')) ||
                        '1 min'
                      }
                    />
                  </SelectTrigger>
                  <SelectContent>
                    {/* <SelectItem value="1">30 sec</SelectItem>
                    <SelectItem value="2">40 sec</SelectItem> */}
                    <SelectItem value="3">50 sec</SelectItem>
                    <SelectItem value="4">1 min (Recommended)</SelectItem>
                    <SelectItem value="5">1.5 min</SelectItem>
                    <SelectItem value="6">2 min</SelectItem>
                    <SelectItem value="7">2.5 min</SelectItem>
                    <SelectItem value="8">3 min</SelectItem>
                    {/* <SelectItem value="9">3.5 min</SelectItem>
                    <SelectItem value="10">4 min</SelectItem>
                    <SelectItem value="11">4.5 min</SelectItem>
                    <SelectItem value="12">5 min</SelectItem> */}
                  </SelectContent>
                </Select>
              </CardContent>
            </Card>
            <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-2">
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-xs 2xl:text-sm font-bold">
                  Geofencing Radius Range
                </CardTitle>
              </CardHeader>
              <CardContent>
                <Select
                  value={radius}
                  onValueChange={val => {
                    setRadius(val);
                    handleUpdateRadius({ configRadius: parseInt(val, 10) });
                  }}>
                  <SelectTrigger className="bg-gray-400/10 h-6 2xl:h-10 rounded-lg px-4 w-full border-none">
                    <SelectValue
                      placeholder={
                        (transportDetails &&
                          transportDetails?.currentRadius + ' meters') ||
                        '10 meters'
                      }
                    />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="5">5 meters</SelectItem>
                    <SelectItem value="7">7 meters</SelectItem>
                    <SelectItem value="10">10 meters</SelectItem>
                    <SelectItem value="20">20 meters</SelectItem>
                    <SelectItem value="30">30 meters</SelectItem>
                    <SelectItem value="40">40 meters</SelectItem>
                  </SelectContent>
                </Select>
              </CardContent>
            </Card>
          </>
        ) : null}
      </div>
      {/* {employeEmpId ? (
        <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-3">
          <Collapsible
            open={isAdvanceSettingOpen}
            onOpenChange={setisAdvanceSettingOpen}
            className="col-span-12">
            <CollapsibleTrigger asChild>
              <div className="flex items-center justify-between cursor-pointer">
                <h4 className="text-sm font-semibold px-2">Advanced Setting</h4>
                <FaAngleDown
                  className={`h-4 w-4 ${isAdvanceSettingOpen ? 'rotate-180' : ''} transition-transform`}
                />
              </div>
            </CollapsibleTrigger>
            <CollapsibleContent className="CollapsibleContent">
              <div className="grid grid-cols-12 gap-3">
                <Separator className="col-span-12 mt-2" />
                <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-2">
                  <CardHeader className="flex flex-row items-center justify-between p-2">
                    <CardTitle className="text-xs 2xl:text-sm font-bold">
                      Way Points
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <Select onValueChange={e => handleUpdate('', Number(e))}>
                      <SelectTrigger className="bg-gray-400/10 h-6 2xl:h-10 rounded-lg px-4 w-full border-none">
                        <SelectValue
                          placeholder={`${employee?.snap_points_limit ?? 100}`}
                        />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="25">25</SelectItem>
                        <SelectItem value="50">50</SelectItem>
                        <SelectItem value="75">75</SelectItem>
                        <SelectItem value="100">100</SelectItem>
                      </SelectContent>
                    </Select>
                  </CardContent>
                </Card>
                <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-2">
                  <CardHeader className="flex flex-row items-center justify-between p-2">
                    <CardTitle className="text-xs 2xl:text-sm font-bold">
                      Time of Updates
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <Select onValueChange={e => handleUpdate(Number(e), '')}>
                      <SelectTrigger className="bg-gray-400/10 h-6 2xl:h-10 rounded-lg px-4 w-full border-none">
                        <SelectValue
                          placeholder={`${employee?.snap_duration_limit / 60 || 1} hr`}
                        />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="60">1 hr</SelectItem>
                        <SelectItem value="120">2 hr</SelectItem>
                        <SelectItem value="180">3 hr</SelectItem>
                      </SelectContent>
                    </Select>
                  </CardContent>
                </Card>
                {/* <div className="col-span-12 flex justify-end">
                  <button
                    onClick={handleUpdate}
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg">
                    Update
                  </button>
                </div> */}
      {/* </div>
            </CollapsibleContent>
          </Collapsible>
        </div>
      ) : null} */}
      {!clear &&
      employeEmpId !== null &&
      taskDetails &&
      taskDetails?.length !== '' > 0 &&
      locationData &&
      locationData?.trackingData?.length !== '' > 0 ? (
        <>
          <Timeline
            date={convertDate(date)}
            isPending={isPending}
            employee={employee}
            taskDetails={taskDetails}
            employeEmpId={employeEmpId}
            locationData={locationData}
            transportDetails={transportDetails}
          />
          <GeoLocation
            date={convertDate(date)}
            employeEmpId={employeEmpId}
            isPending={isPending}
            locationData={locationData}
            transportDetails={transportDetails}
            employeeProfile={employeeProfile}
          />
        </>
      ) : (
        <div className="col-span-12 w-full text-center h-80 flex flex-col justify-center items-center">
          <Lottie
            loop
            animationData={employeeLoadingAnimation}
            play
            style={{ width: 250, height: 250 }}
          />
          <span className="text-xxs 2xl:text-base">
            Initiate the search to kickstart exploring employee data
          </span>
        </div>
      )}
    </>
  );
};

export default FilterSection;
