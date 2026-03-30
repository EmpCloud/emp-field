import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { Slider } from '@/components/ui/slider';
import { Switch } from '@/components/ui/switch';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { useEffect, useState } from 'react';
import { orgLocationDetails } from './Api/get';
import { useQuery } from '@tanstack/react-query';
import { UpdateOrgLocationDetails } from './Api/post';
import { toast } from 'sonner';
import { fetechEmpoyeeLocation } from 'components/EmployeeModule/EmployeeFilterAndTable/Api/get';
import { updateEmployeeLocationfencing } from 'components/UIElements/Modals/Api/post';
import { getEmployeeLocationById } from 'components/UIElements/Modals/Api/get';

const GeoLocationSetting = ({
  setRange,
  currentLat,
  setOnChangeRange,
  range,
  setLatitude,
  latitude,
  longitude,
  setLongitude,
  hideLocation,
  contentmargin,
  employeeId,
  setOpenLocationModal,
}) => {
  const [address, setAddress] = useState('');
  const [geoLocationEnabled, setGeoLocationEnabled] = useState(false);
  const [mobileAttendanceEnabled, setMobileAttendanceEnabled] = useState(false);
  const [selectedLocation, setSelectedLocation] = useState('');

  // Only fetch org/location data if not hiding location
  const orgLocationQuery = useQuery({
    queryKey: ['orgLocationDetails'],
    queryFn: orgLocationDetails,
    enabled: !hideLocation,
  });
  const orgLocationDetail = orgLocationQuery.data?.data?.body?.data?.[0];

  const employeeLocationQuery = useQuery({
    queryKey: ['employeeLocation'],
    queryFn: fetechEmpoyeeLocation,
    enabled: !hideLocation,
  });
  let employeeLocation = employeeLocationQuery?.data?.body?.data?.Locations;

  // Fetch employee location by id if hiding location
  useEffect(() => {
    if (hideLocation && employeeId) {
      getEmployeeLocationById(employeeId).then(res => {
        const empLoc = res?.data?.body?.data?.[0];
        if (empLoc) {
          setAddress(empLoc.address || '');
          setLatitude(empLoc.latitude || '');
          setLongitude(empLoc.longitude || '');
          setRange(empLoc.range || 0);
          setGeoLocationEnabled(empLoc.geo_fencing ?? false);
          setMobileAttendanceEnabled(empLoc.isMobEnabled ?? false);
        }
      });
    }
  }, [hideLocation, employeeId, setLatitude, setLongitude, setRange]);

  // Populate fields from org location if not hiding location
  useEffect(() => {
    if (!hideLocation && orgLocationQuery.data && range === 0) {
      setSelectedLocation(
        prev => prev || orgLocationDetail?.locationName || null
      );
      setGeoLocationEnabled(orgLocationDetail?.geo_fencing ?? false);
      setMobileAttendanceEnabled(orgLocationDetail?.isMobEnabled ?? false);
      setAddress(orgLocationDetail?.address || '');
      setLongitude(orgLocationDetail?.longitude || currentLat?.[0]?.lng || '');
      setLatitude(orgLocationDetail?.latitude || currentLat?.[0]?.lat || '');
      setRange(orgLocationDetail?.range || 0);
    }
  }, [
    orgLocationQuery.data,
    hideLocation,
    orgLocationDetail?.locationName,
    orgLocationDetail?.geo_fencing,
    orgLocationDetail?.isMobEnabled,
    orgLocationDetail?.address,
    orgLocationDetail?.longitude,
    orgLocationDetail?.latitude,
    orgLocationDetail?.range,
    setLatitude,
    setLongitude,
    setRange,
    currentLat,
  ]);

  // Handle submit for both org and employee
  const handleGeoLoactionDetailsUpdating = e => {
    e.preventDefault();
    if (hideLocation) {
      // Update employee location fencing
      const updatedDetails = {
        employeeId,
        address,
        latitude,
        longitude,
        range,
        geo_fencing: geoLocationEnabled,
        isMobEnabled: mobileAttendanceEnabled,
      };
      updateEmployeeLocationfencing(updatedDetails).then(response => {
        if (response?.data?.body?.status) {
          toast.success(response?.data?.body?.message);
          setOpenLocationModal(false);
        }
      });
    } else {
      // Update org location details
      const updatedDetails = {
        locationName: selectedLocation,
        geo_fencing: geoLocationEnabled,
        isMobEnabled: mobileAttendanceEnabled,
        address,
        longitude: currentLat[0]?.lng,
        latitude: currentLat[0]?.lat,
        range,
      };
      UpdateOrgLocationDetails(updatedDetails).then(response => {
        if (response?.data?.body?.status) {
          toast.success(response?.data?.body?.message);
          orgLocationQuery.refetch();
        }
      });
    }
  };

  return (
    <div className="card-shadow grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg">
      <Card>
        <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 2xl:h-12 h-10">
          <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
            Geolocation Setting
          </CardTitle>
        </CardHeader>
        <CardContent className="h-[420px] 2xl:h-[550px] card-container-no-head px-4">
          <form
            className="flex flex-col justify-start items-start w-full gap-5"
            onSubmit={handleGeoLoactionDetailsUpdating}>
            {/* Hide location dropdown if hideLocation is true */}
            {!hideLocation && (
              <Card className={`border-none shadow-none w-full`}>
                <CardHeader className="flex flex-row items-center justify-between p-2">
                  <CardTitle className="text-xs 2xl:text-sm font-bold">
                    Location
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <Select
                    value={selectedLocation}
                    onValueChange={value => {
                      setSelectedLocation(value);
                      const loc = employeeLocation?.find(
                        option => option.locationName === value
                      );
                      if (loc) {
                        setLatitude(loc.latitude);
                        setLongitude(loc.longitude);
                      }
                    }}>
                    <SelectTrigger className="bg-gray-400/10 h-6 2xl:h-10 rounded-lg px-4 w-full border-none">
                      <SelectValue placeholder="Select Location" />
                    </SelectTrigger>
                    <SelectContent>
                      {employeeLocation &&
                        employeeLocation.map(option => (
                          <SelectItem
                            className="text-xs"
                            key={option._id}
                            value={option.locationName}>
                            {option.locationName}
                          </SelectItem>
                        ))}
                    </SelectContent>
                  </Select>
                </CardContent>
              </Card>
            )}
            <div
              className={`flex items-center justify-between bg-gray-400/10 h-6 2xl:h-10 rounded-lg px-4 w-full ${contentmargin}`}>
              <CardTitle className="text-xs 2xl:text-sm font-bold">
                Geo Location
              </CardTitle>
              <Switch
                checked={geoLocationEnabled}
                onCheckedChange={setGeoLocationEnabled}
              />
            </div>
            <div className="flex items-center justify-between bg-gray-400/10 h-6 2xl:h-10 rounded-lg px-4 w-full">
              <CardTitle className="text-xs 2xl:text-sm font-bold">
                Mobile Attendance
              </CardTitle>
              <Switch
                checked={mobileAttendanceEnabled}
                onCheckedChange={setMobileAttendanceEnabled}
              />
            </div>
            {/* <Card className="border-none shadow-none w-full">
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-xs 2xl:text-sm font-bold">
                  Enter Location
                </CardTitle>
              </CardHeader>
              <CardContent>
                <Popover className="relative">
                  <PopoverTrigger asChild>
                    <div className="relative">
                      <Input
                        value={address}
                        onChange={e => setAddress(e.target.value)}
                        type="text"
                        placeholder="Enter Location"
                        className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                      />
                    </div>
                  </PopoverTrigger>
                  <PopoverContent
                    align="start"
                    className="max-h-[200px] overflow-y-auto p-2 min-w-40">
                    <div className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                      <CardTitle className="text-xs 2xl:text-sm"></CardTitle>
                    </div>
                  </PopoverContent>
                </Popover>
              </CardContent>
            </Card> */}
            <div className="grid grid-cols-2 w-full gap-5">
              <Card className="border-none shadow-none col-span-1">
                <CardHeader className="flex flex-row items-center justify-between p-2">
                  <CardTitle className="text-xs 2xl:text-sm font-bold">
                    Latitude
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="relative">
                    <Input
                      value={currentLat[0]?.lat || latitude}
                      onChange={e => setLatitude(e.target.value)}
                      readOnly
                      type="text"
                      placeholder="Latitude"
                      className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                    />
                  </div>
                </CardContent>
              </Card>
              <Card className="border-none shadow-none col-span-1">
                <CardHeader className="flex flex-row items-center justify-between p-2">
                  <CardTitle className="text-xs 2xl:text-sm font-bold">
                    Longitude
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="relative">
                    <Input
                      value={currentLat[0]?.lng || longitude}
                      onChange={e => setLongitude(e.target.value)}
                      readOnly
                      type="text"
                      placeholder="Longitude"
                      className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                    />
                  </div>
                </CardContent>
              </Card>
            </div>
            <Card className="border-none shadow-none w-full">
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="flex justify-between items-center w-full">
                  <h3 className="text-xs 2xl:text-sm font-bold">
                    Select Range (mts)
                  </h3>
                  <p className="text-xs font-medium justify-center items-center flex gap-1">
                    Current Range
                    <span className="text-xs 2xl:text-sm font-bold">
                      {range ?? 0}
                    </span>
                  </p>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div
                  className="flex flex-col rounded-lg px-2"
                  style={{ cursor: 'pointer' }}>
                  <Slider
                    style={{ cursor: 'pointer' }}
                    value={[range]}
                    min={5} // Set minimum value to 5
                    max={100}
                    step={1}
                    onValueChange={value => {
                      setOnChangeRange(value[0]), setRange(value[0]);
                    }}
                    frequency={range}
                  />
                  <div className="flex justify-between text-xs pt-1 2xl:pt-4">
                    <p>5 mts</p>
                    <p>100 mts</p>
                  </div>
                </div>
              </CardContent>
            </Card>
            <Button
              onClick={handleGeoLoactionDetailsUpdating}
              type="submit"
              className="bg-[#6A6AEC] hover:bg-[#6A6AEC]/80 ml-auto">
              Submit
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default GeoLocationSetting;
