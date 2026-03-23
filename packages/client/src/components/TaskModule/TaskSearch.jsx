import { Button } from '@/components/ui/button';
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
import { cn } from '@/lib/utils';
import { CalendarDays } from 'lucide-react';
import { Calendar as CalendarCom } from '@/components/ui/calendar';
import React, { useState } from 'react';
import { Input } from '@/components/ui/input';
import { RxCross1 } from 'react-icons/rx';
import { addDays, format } from 'date-fns';
import { useQuery } from '@tanstack/react-query';
import { empoyeeSearchedSuggestions } from 'components/FilterModule/Api/get';
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import html2canvas from 'html2canvas';
import { clientSearchedSuggestions } from './Api/get';
import TaskTable from './TaskTable';
import { Separator } from '@radix-ui/react-dropdown-menu';
import DataTable from 'components/EmployeeModule/EmployeeDataTableModule/DataTable';
import Cookies from 'js-cookie';
import moment from 'moment';

const TaskSearch = ({
  setdateFilterTask,
  setClientIdTask,
  setTaskEmployeEmpId,
}) => {
  const [date, setDate] = React.useState(new Date());
  const handleAutoFocus = e => {
    e.preventDefault();
  };

  const [open, setOpen] = useState(false);

  const [searchQuery, setSearchQuery] = useState('');
  const [employeeId, setEmployeEmpId] = useState('');

  const response = useQuery({
    queryKey: ['searchQueryDashboard', searchQuery],
    queryFn: () => empoyeeSearchedSuggestions(searchQuery),
    enabled: true, // Always run the query, regardless of searchQuery value
  });
  const isLoadingEmployee = response.isLoading;
  const employeeData = response?.data?.body?.data?.resultData?.map(data => ({
    profilePic: data?.profilePic,
    org_id: data?.orgId,
    id: data?.fullName,
    value: data?.fullName,
    label: data?.fullName,
  }));
  const handleBlur = () => {
    // setOpen(false);
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
    setTaskEmployeEmpId(label);
  };

  const [clientSearchQuery, setClientSearchQuery] = useState('');
  const [clientId, setClientId] = useState('');

  const clientResponse = useQuery({
    queryKey: ['searchQueryClients', clientSearchQuery],
    queryFn: () => clientSearchedSuggestions(clientSearchQuery),
    enabled: true, // Always run the query, regardless of searchQuery value
  });

  const isLoadingClient = clientResponse.isLoading;
  let clientData = clientResponse?.data?.body?.data?.data?.map(data => ({
    profilePic: data?.profilePic,
    org_id: data?.orgId,
    id: data?.client_id,
    value: data?.clientName,
    label: data?.clientName,
  }));

  const [opens, setOpens] = useState(false);

  const handleClientBlur = () => {
    // setOpens(false);
  };

  const handleClientFocus = () => {
    setOpens(true);
  };

  const handleSearchedClient = id => {
    setClientId(id);
    const filteredClient = clientData?.filter(client => {
      client.label == id;
    });
    // const { label } = filteredClient[0];
    setClientIdTask(id);
    setClientSearchQuery(id);
    setOpens(false);
  };

  const orgCreatedAt = Cookies.get('createdAt');
  const formattedDate = moment(orgCreatedAt).format(
    'ddd MMM DD YYYY HH:mm:ss [GMT]ZZ [(India Standard Time)]'
  );

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
            <Popover className="relative">
              <PopoverTrigger asChild>
                <div className="relative">
                  {/* <Input
                  type="text"
                  placeholder="Search Employee"
                  className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                /> */}
                  <Input
                    type="text"
                    placeholder="Search Employee"
                    value={searchQuery}
                    className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                    onFocus={handleFocus}
                    onBlur={handleBlur}
                    onChange={e => {
                      setSearchQuery(e.target.value);
                    }}
                  />
                  {/* {true ? (
                  <RxCross1
                    className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                    onClick={e => {
                      e.stopPropagation();
                    }}
                  />
                ) : null} */}

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
              {/* <PopoverContent
              onOpenAutoFocus={handleAutoFocus}
              align="start"
              className="max-h-[200px] overflow-y-auto p-2 min-w-40">
              <div className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                <CardTitle className="text-xs 2xl:text-sm">Rohit</CardTitle>
              </div>
            </PopoverContent> */}
              {open ? (
                <PopoverContent
                  onOpenAutoFocus={handleAutoFocus}
                  align="start"
                  className="max-h-[200px] overflow-y-auto p-2 min-w-40">
                  {employeeData && employeeData.length > 0 ? (
                    <>
                      {employeeData?.map((username, index) => (
                        <CardTitle
                          key={index}
                          onClick={() => handleSearchedEmployee(username.id)}
                          className="text-xs font-medium cursor-pointer py-1">
                          {username.label}
                        </CardTitle>
                      ))}
                    </>
                  ) : (
                    <>
                      <CardTitle className="text-xs font-medium cursor-pointer py-1">
                        {isLoadingEmployee ? 'loading...' : 'No employee found'}
                      </CardTitle>
                    </>
                  )}
                </PopoverContent>
              ) : (
                ''
              )}
            </Popover>
          </CardContent>
        </Card>
        {/* Second box for searching client */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-4">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Search Client
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Popover className="relative">
              <PopoverTrigger asChild>
                {/* <div className="relative">
                <Input
                  type="text"
                  placeholder="Search Employee"
                  className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                />
                {true ? (
                  <RxCross1
                    className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                    onClick={e => {
                      e.stopPropagation();
                    }}
                  />
                ) : null}
              </div> */}
                <div className="relative">
                  <Input
                    type="text"
                    placeholder="Search Client"
                    value={clientSearchQuery}
                    className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                    onFocus={handleClientFocus}
                    onBlur={handleClientBlur}
                    onChange={e => {
                      setClientSearchQuery(e.target.value);
                    }}
                  />
                  {clientSearchQuery !== '' && (
                    <RxCross1
                      className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                      onClick={() => {
                        setClientSearchQuery('');
                        setClientId('');
                        clientResponse?.refetch();
                      }}
                    />
                  )}
                </div>
              </PopoverTrigger>
              {opens ? (
                <PopoverContent
                  onOpenAutoFocus={handleAutoFocus}
                  align="start"
                  className="max-h-[200px] overflow-y-auto p-2 min-w-40">
                  {clientData && clientData.length > 0 ? (
                    <>
                      {clientData?.map((client, index) => (
                        <CardTitle
                          key={index}
                          onClick={() => handleSearchedClient(client.label)}
                          className="text-xs font-medium cursor-pointer py-1">
                          {client.label}
                        </CardTitle>
                      ))}
                    </>
                  ) : (
                    <>
                      <CardTitle className="text-xs font-medium cursor-pointer py-1">
                        {isLoadingClient ? 'loading...' : 'No client found'}
                      </CardTitle>
                    </>
                  )}
                </PopoverContent>
              ) : (
                ''
              )}

              {/* <PopoverContent
              onOpenAutoFocus={handleAutoFocus}
              align="start"
              className="max-h-[200px] overflow-y-auto p-2 min-w-40">
              <div className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                <CardTitle className="text-xs 2xl:text-sm">Rohit</CardTitle>
              </div>
            </PopoverContent> */}
            </Popover>
          </CardContent>
        </Card>
        {/* Date section section */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4 xl:col-span-4">
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
                  onValueChange={value => {
                    setDate(addDays(new Date(), parseInt(value)));
                    // setdateFilterTask(addDays(new Date(), parseInt(value)))
                  }}>
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
                    // disabled={date => {
                    //   const today = new Date();

                    //   // Correctly parse orgCreatedAt to a Date object using moment
                    //   let monthStart = moment(orgCreatedAt)
                    //     .subtract(1, 'day')
                    //     .toDate();

                    //   // Ensure monthEnd is the last date of the current month
                    //   const monthEnd = moment(today).toDate();

                    //   // Disable dates outside the current month
                    //   return date < monthStart || date > monthEnd;
                    // }}
                    selected={date}
                    onSelect={selectedDate => {
                      setDate(selectedDate);
                    }}
                  />
                  {/* <CalendarCom
                  mode="single"
                  onSelect={(selectedDate) => {
                    setDate(selectedDate);
                  }}
                  disabled={(date) => {
                    const today = new Date();
                    // const monthStart = new Date(today.getFullYear(), today.getMonth(), 1);
                    const monthEnd = new Date();
                   let monthStart =moment(orgCreatedAt).format('ddd MMM DD YYYY HH:mm:ss [GMT]ZZ [(India Standard Time)]')
        
                    // Disable dates outside the current month
                    return date < monthStart || date > monthEnd;
                  }}
           
                /> */}
                </div>
              </PopoverContent>
            </Popover>
          </CardContent>
        </Card>
        <Separator className="col-span-12" />

        {/* <DataTable
          employeeId={employeeId}
          clientId={clientId}
          date={date}
          task={'task'}
        /> */}
        <TaskTable
          employeeId={employeeId}
          clientId={clientId}
          date={date}
          task={'task'}
        />
      </div>
    </>
  );
};

export default TaskSearch;
