import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { Input } from '@/components/ui/input';
import { RxCross1 } from 'react-icons/rx';
import { useEffect, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import {
  clientSearchedSuggestions,
  empoyeeSearchedSuggestions,
} from 'components/FilterModule/Api/get';
const ClientSearch = ({
  searchQuery,
  setSearchQuery,
  searchValue,
  setSearchValue,
}) => {
  const handleAutoFocus = e => {
    e.preventDefault();
  };
  const [open, setOpen] = useState(false);
  const [employeeId, setEmployeEmpId] = useState('');

  const response = useQuery({
    queryKey: ['searchQueryDashboard', searchQuery],
    queryFn: () => empoyeeSearchedSuggestions(searchQuery),
  });
  const isLoadingEmployee = response.isLoading;
  const employeeData = response?.data?.body?.data?.resultData?.map(data => ({
    profilePic: data?.profilePic,
    org_id: data?.orgId,
    id: data?.emp_id,
    value: data?.fullName,
    label: data?.fullName,
  }));

  const responseData = useQuery({
    queryKey: ['clientSearchedSuggestions', searchValue],
    queryFn: () => clientSearchedSuggestions(searchValue),
  });

  const isLoadingClient = responseData?.isLoading;
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
    responseData.refetch();
  };
  const handleSearchedClient = id => {
    const filteredClient = responseData?.data?.body?.data?.data.find(
      client => client._id === id
    );
    if (filteredClient) {
      setSearchValue(filteredClient.clientName);
      setOpen(false);
      response.refetch(); // Refetch employee suggestions if needed
    }
  };

  const handleChange = e => {
    const value = e.target.value;
    setSearchValue(value);
    if (value) {
      responseData.refetch();
    }
  };

  return (
    <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-3">
      {/* Second box for searching client */}
      <Card className="border-none shadow-none col-span-12 sm:col-span-6">
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Search Client
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Popover className="relative">
            <PopoverTrigger asChild>
              <div className="relative">
                <Input
                  type="text"
                  value={searchValue}
                  placeholder="Search Client"
                  className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                  onFocus={handleFocus}
                  onBlur={handleBlur}
                  // onChange={e => {
                  //   setSearchValue(e?.target?.value);
                  //   // responseData.refetch();
                  //   refetch();
                  // }}
                  onChange={handleChange}
                />
                {searchValue !== '' && (
                  <RxCross1
                    className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                    onClick={() => {
                      setSearchValue('');
                      // response?.refetch();
                      refetch();
                    }}
                  />
                )}{' '}
              </div>
            </PopoverTrigger>
            {/* <PopoverContent
              onOpenAutoFocus={handleAutoFocus}
              align="start"
              className="max-h-[200px] overflow-y-auto p-2 min-w-40">
              {responseData?.data?.body?.data?.data &&
              responseData?.data?.body?.data?.data?.length > 0 ? (
                <>
                  {responseData?.data?.body?.data?.data?.map(
                    (username, index) => (
                      <CardTitle
                        key={index}
                        onClick={() => handleSearchedClient(username?._id)}
                        className="text-xs font-medium cursor-pointer py-1">
                        {username?.clientName}
                      </CardTitle>
                    )
                  )}
                </>
              ) : (
                <>
                  <CardTitle className="text-xs font-medium cursor-pointer py-1">
                    {isLoadingClient ? 'loading...' : 'No clients found'}
                  </CardTitle>
                </>
              )}
            </PopoverContent> */}
          </Popover>
        </CardContent>
      </Card>
    </div>
  );
};

export default ClientSearch;
