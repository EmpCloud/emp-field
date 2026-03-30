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
  StrageSearchedSuggestions,
  clientSearchedSuggestions,
  empoyeeSearchedSuggestions,
} from 'components/FilterModule/Api/get';
const StageTaskSearch = ({
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
  const employeeData = response?.data?.body?.data?.allTags?.map(data => ({
    stageName: data?.tagName,
    color: data?.color,
    // id: data?.emp_id,
    // value: data?.fullName,
    // label: data?.fullName,
  }));

  const responseData = useQuery({
    queryKey: ['StrageSearchedSuggestions', searchValue],
    queryFn: () => StrageSearchedSuggestions(searchValue),
  });

  const isLoadingstage = responseData?.isLoading;
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
  // const handleSearchedStage = id => {
  //   // console.log(filteredStage,'...filteredtage...')
  //   const filteredStage = responseData?.data?.body?.data?.allTags.filter(
  //     stage => stage._id === id
  //   );
  //   const { stageName } = filteredStage[0];
  //   setSearchValue(stageName);
  //   setOpen(false);
  //   response.refetch();
  // };
  const handleSearchedStage = id => {
    // Find the selected stage
    const selectedStage = responseData?.data?.body?.data?.allTags.find(
      stage => stage._id === id
    );
    if (selectedStage) {
      setSearchValue(selectedStage.tagName);
      setOpen(false);
    }
  };
  const filteredStageData =
    responseData?.data?.body?.data?.allTags?.filter(stage =>
      stage.tagName.toLowerCase().includes(searchValue.toLowerCase())
    ) || [];

  // useEffect(() => {
  //   console.log('ResponseData:', responseData.data?.body?.data?.allTags);
  //   console.log('EmployeeData:', employeeData);
  // }, [responseData, employeeData]);

  return (
    // <div className="card-shadow grid gap-4 grid-cols-12 col-span-12 bg-white rounded-lg p-3">
    //   <Card className="border-none shadow-none col-span-12 sm:col-span-6">
    //     <CardHeader className="flex flex-row items-center justify-between p-2">
    //       <CardTitle className="text-xs 2xl:text-sm font-bold">
    //         Search Stage
    //       </CardTitle>
    //     </CardHeader>
    //     <CardContent>
    //       <Popover className="relative">
    //         <PopoverTrigger asChild>
    //           <div className="relative">
    //             <Input
    //               type="text"
    //               value={searchValue}
    //               placeholder="Search Stage"
    //               className=" bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
    //               onFocus={handleFocus}
    //               onBlur={handleBlur}
    //               onChange={e => {
    //                 setSearchValue(e?.target?.value);
    //                 responseData.refetch();
    //               }}
    //             />
    //             {searchValue !== '' && (
    //               <RxCross1
    //                 className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
    //                 onClick={() => {
    //                   setSearchValue('');
    //                   response?.refetch();
    //                 }}
    //               />
    //             )}{' '}
    //           </div>
    //         </PopoverTrigger>
    //         <PopoverContent
    //           onOpenAutoFocus={handleAutoFocus}
    //           align="start"
    //           className="max-h-[200px] overflow-y-auto p-2 min-w-40">
    //           {filteredStageData.length > 0 ? (
    //             filteredStageData.map(stage => (
    //               <CardTitle
    //                 key={stage._id}
    //                 onClick={() => handleSearchedStage(stage._id)}
    //                 className="text-xs font-medium cursor-pointer py-1">
    //                 {stage.tagName}
    //               </CardTitle>
    //             ))
    //           ) : (
    //             <CardTitle className="text-xs font-medium cursor-pointer py-1">
    //               {isLoadingstage ? 'loading...' : 'No Stage found'}
    //             </CardTitle>
    //           )}
    //         </PopoverContent>
    //       </Popover>
    //     </CardContent>
    //   </Card>
    // </div>
    <div></div>
  );
};

export default StageTaskSearch;
