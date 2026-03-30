import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import DataTable from '../EmployeeDataTableModule/DataTable';
import { Separator } from '@/components/ui/separator';
import { Button } from '@/components/ui/button';
import { useInfiniteQuery } from '@tanstack/react-query';
import { useEffect, useState } from 'react';
import { useInView } from 'react-intersection-observer';
import { fetechEmpoyeeRoles } from './Api/get';
import { fetechEmpoyeeDepartment } from './Api/get';
import { fetechEmpoyeeLocation } from './Api/get';

const FilterDetails = () => {
  const [deletedUsers, setDeletedUsers] = useState(false);
  const [activeEmployees, setActiveEmplyees] = useState(false);

  const {
    data,
    status,
    error,
    fetchNextPage,
    isFetchingNextPage,
    hasNextPage,
  } = useInfiniteQuery({
    queryKey: ['roles'],
    queryFn: fetechEmpoyeeRoles,
    initialPageParam: 1,
    getNextPageParam: (lastPage, allPages) => {
      const nextPage = lastPage.length ? allPages.length + 1 : undefined;
      return nextPage;
    },
  });

  const { ref, inView } = useInView();

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

  let employeeRoles = data?.pages[0]?.body?.data;

  // Check if the new role already exists in the array
  const newRole = {
    createdAt: 'dfgfg',
    orgId: '246',
    role: 'All',
    updatedAt: 'sdfdf',
    __v: 'f',
    _id: 'tgr56',
  };

  const response = useInfiniteQuery({
    queryKey: ['Department'],
    queryFn: fetechEmpoyeeDepartment,
    initialPageParam: 1,
    getNextPageParam: (lastPage, allPages) => {
      const nextPage = lastPage.length ? allPages.length + 1 : undefined;
      return nextPage;
    },
  });

  const ref1 = useInView();

  useEffect(() => {
    if (ref1.inView && response?.hasNextPage) {
      response?.fetchNextPage();
    }
  }, [ref1.inView, response?.hasNextPage, response?.fetchNextPage]);

  let employeeDepartment = response?.data?.pages[0]?.body?.data?.Department;

  const locationResponse = useInfiniteQuery({
    queryKey: ['Location'],
    queryFn: fetechEmpoyeeLocation,
    initialPageParam: 1,
    getNextPageParam: (lastPage, allPages) => {
      const nextPage = lastPage.length ? allPages.length + 1 : undefined;
      return nextPage;
    },
  });

  const ref2 = useInView();

  useEffect(() => {
    if (ref2.inView && locationResponse?.hasNextPage) {
      locationResponse?.fetchNextPage();
    }
  }, [
    ref2.inView,
    locationResponse?.hasNextPage,
    locationResponse?.fetchNextPage,
  ]);

  let employeeLocation =
    locationResponse?.data?.pages[0]?.body?.data?.Locations;

  if (status === 'loading') {
    return <p>Loading...</p>;
  }

  if (status === 'error') {
    return <p>Error: {error.message}</p>;
  }

  const roleExists = employeeRoles?.some(role => role._id === newRole._id);

  if (!roleExists) {
    employeeRoles?.unshift(newRole);
  }

  const newLocation = {
    locationName: ' All location',
  };

  const loactionExists = employeeLocation?.some(
    location => location.locationName === newLocation.locationName
  );

  if (!loactionExists) {
    employeeLocation?.unshift(newLocation);
  }
  const newDepartment = {
    department: ' All Departments',
  };

  const departmentExists = employeeDepartment?.some(
    location => location.department === newDepartment.department
  );

  if (!departmentExists) {
    employeeDepartment?.unshift(newDepartment);
  }

  const [selectedRole, setSelectedRole] = useState('');
  const [selectedLocation, setSelectedLocation] = useState('');

  const handleSelectChange = (value, option) => {
    setSelectedRole(option.role); // Assuming 'role' is the name you want to capture
  };
  const handleSelectChangeLocation = (value, option) => {
    setSelectedLocation(option.locationName);
  };

  const [selectedDepartment, setSelectedDepartment] = useState(null);

  const handleSelectChangeDepartment = (value, option) => {
    setSelectedDepartment(option.department);
  };

  return (
    <>
      <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-3 items-center">
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-4">
          <CardHeader className="flex flex-row items-center justify-between px-2">
            <CardTitle className="text-md 2xl:text-xl font-bold cursor-pointer">
              Employee Details
            </CardTitle>
          </CardHeader>
        </Card>
        <Button
          onClick={() => {
            setActiveEmplyees(true), setDeletedUsers(false);
          }}
          disabled={true}
          variant="outline"
          className="col-span-4 sm:col-start-7 sm:col-span-2 border-violet-500 hover:bg-violet-500/30 text-violet-500 hover:text-violet-500">
          Active
        </Button>
        <Button
          disabled={true}
          variant="outline"
          className="col-span-4 sm:col-span-2 border-violet-500 hover:bg-violet-500/30 text-violet-500 hover:text-white-500">
          Suspended
        </Button>
        {deletedUsers === false ? (
          <Button
            onClick={() => {
              setDeletedUsers(true), setActiveEmplyees(false);
            }}
            // disabled={true}
            variant="outline"
            className="col-span-4 sm:col-span-2 border-violet-500 hover:bg-violet-500/30 text-violet-500 hover:text-violet-500">
            Deleted Employees
          </Button>
        ) : (
          <Button
            onClick={() => {
              setDeletedUsers(false);
            }}
            // disabled={true}
            variant="outline"
            // hover:bg-violet-500/30
            className="col-span-4 sm:col-span-2 border-violet-500  bg-violet-500 hover:bg-violet-500/80 text-white">
            Deleted Employees
          </Button>
        )}

        <Card className="border-none shadow-none col-span-12 sm:col-span-6 md:col-span-4 xl:col-span-4">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Role
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Select
              onValueChange={value =>
                handleSelectChange(
                  value,
                  employeeRoles.find(role => role._id === value)
                )
              }>
              <SelectTrigger className="bg-slate-400/10">
                <SelectValue placeholder="See All" />
              </SelectTrigger>
              <SelectContent>
                {/* <SelectItem value="developer">Developer</SelectItem> */}
                <div ref={ref} />
                {employeeRoles &&
                  employeeRoles?.map(option => (
                    <SelectItem
                      className="text-xs"
                      key={option._id}
                      value={option._id}>
                      {option.role}
                    </SelectItem>
                  ))}
                {isFetchingNextPage && <h3>Loading...</h3>}
                <div ref={ref} />
              </SelectContent>
            </Select>
          </CardContent>
        </Card>
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 md:col-span-4 xl:col-span-4">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Location
            </CardTitle>
          </CardHeader>
          <CardContent>
            {/* <Select onValueChange={
              value =>
                handleSelectChangeLocation(
                  value,
                  employeeLocation.find(role =>{ console.log(role,'role'), role.locationName === value})
                )
              }>
              <SelectTrigger className="bg-slate-400/10">
                <SelectValue placeholder="All Location" />
              </SelectTrigger>
              <SelectContent>
                {employeeLocation &&
                  employeeLocation?.map(option => (
                    <SelectItem
                      className="text-xs"
                      key={option._id}
                      value={option._id}>
                      {option.locationName}
                    </SelectItem>
                  ))}
                  
                {isFetchingNextPage && <h3>Loading...</h3>}
              </SelectContent>
            </Select> */}
            <Select
              onValueChange={value => {
                const selectedLocation = employeeLocation.find(
                  location => location.locationName === value
                );
                handleSelectChangeLocation(value, selectedLocation);
              }}>
              <SelectTrigger className="bg-slate-400/10">
                <SelectValue placeholder="Select Location" />
              </SelectTrigger>
              <SelectContent>
                <div ref={ref1.ref} />
                {employeeLocation &&
                  employeeLocation.map(option => (
                    <SelectItem
                      className="text-xs"
                      key={option._id}
                      value={option.locationName}>
                      {option.locationName}
                    </SelectItem>
                  ))}
                {locationResponse?.isFetchingNextPage && <h3>Loading...</h3>}
                <div ref={ref1.ref} />
              </SelectContent>
            </Select>
          </CardContent>
        </Card>
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 md:col-span-4 xl:col-span-4">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Department
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Select
              onValueChange={value => {
                const selectedDepartment = employeeDepartment.find(
                  location => location.department === value
                );
                handleSelectChangeDepartment(value, selectedDepartment);
              }}>
              <SelectTrigger className="bg-slate-400/10">
                <SelectValue placeholder="Select Department" />
              </SelectTrigger>
              <SelectContent>
                <div ref={ref1.ref} />
                {employeeDepartment &&
                  employeeDepartment.map(option => (
                    <SelectItem
                      className="text-xs"
                      key={option._id}
                      value={option.department}>
                      {option.department}
                    </SelectItem>
                  ))}
                {response?.isFetchingNextPage && <h3>Loading...</h3>}
                <div ref={ref1.ref} />
              </SelectContent>
            </Select>
          </CardContent>
        </Card>

        <Separator className="col-span-12" />

        <DataTable
          selectedRole={selectedRole}
          selectedLocation={selectedLocation}
          selectedDepartment={selectedDepartment}
          deletedUsers={deletedUsers}
          activeEmployees={activeEmployees}
        />
      </div>
    </>
  );
};

export default FilterDetails;
