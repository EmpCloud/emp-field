import { useQuery } from '@tanstack/react-query';
import { fetchAllDashboardStats } from './Api/get';
import { Modal } from 'components/Modal';
import EmployeeStatsModal from './EmployeetStatsModal';
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogClose,
} from '@/components/ui/dialog'; // Update with actual path
import { useState } from 'react';
import Cookies from 'js-cookie';
import { DecodeJWTToken } from 'context/Filters/util.token';

const Insights = () => {
  const { isLoading, data: response } = useQuery({
    queryKey: ['fetchAllDashboardStats'],
    queryFn: fetchAllDashboardStats,
  });

  const [selectedItem, setSelectedItem] = useState(null);
  const orgId = DecodeJWTToken(Cookies.get('token'));
  const disabledOrgIds =
    import.meta.env.VITE_LIVE_TRACK_DISABLE_ORG_ID?.split(',') || [];
  const hidingDahboardFeature =
    orgId && !disabledOrgIds.includes(String(orgId));
  if (isLoading)
    return (
      <div className="card-shadow grid gap-4 md:grid-cols-5 sm:grid-cols-2 grid-cols-1 col-span-12 bg-white rounded-lg p-3 animate-pulse">
        {Array.from({ length: 5 }).map((_, index) => (
          <div
            key={index}
            className="col-span-1 py-3 rounded text-center bg-slate-200 h-16">
            <h2 className="font-bold text-sm 2xl:text-2xl bg-slate-300 h-6 w-10 rounded-sm mx-auto"></h2>
            <p className="text-xs 2xl:text-base bg-slate-300 h-3 w-24 rounded-sm mx-auto mt-2"></p>
          </div>
        ))}
      </div>
    );

  const statsData = response?.data?.body?.data;

  let data = [
    {
      id: 1,
      name: 'Employees',
      apiQuery: 'AllEmployees',
      value: statsData?.employees || 0,
      accent: 'bg-gradient-to-br from-[#5FAEF8] to-[#5B58E8]',
    },
    {
      id: 2,
      name: 'Present Today',
      apiQuery: 'presentToday',
      value: statsData?.presentToday || 0,
      accent: 'bg-gradient-to-br from-[#FF7272] to-[#FFA5C9]',
    },
    {
      id: 3,
      name: 'On Duty',
      apiQuery: 'onDuty',
      value: statsData?.onDuty || 0,
      accent: 'bg-gradient-to-br from-[#BDE36D] to-[#5F9B22]',
    },
    {
      id: 4,
      name: 'Absent Today',
      apiQuery: 'absent',
      value: statsData?.absent || 0,
      accent: 'bg-gradient-to-br from-[#A893FF] to-[#B04FEC]',
    },
    {
      id: 5,
      name: 'Suspended',
      apiQuery: 'suspended',
      value: statsData?.suspended || 0,
      accent: 'bg-gradient-to-br from-[#FFB258] to-[#FF6636]',
    },
  ];
  // Remove "On Duty" if orgId is in disabledOrgIds
  if (!hidingDahboardFeature)
    data = data.filter(
      item => item.apiQuery !== 'onDuty' && item.apiQuery !== 'suspended'
    );

  const handleItemClick = (name, apiQuery, accent) => {
    setSelectedItem({ name, apiQuery, accent });
  };
  return (
    <div className="card-shadow grid gap-4 grid-cols-[repeat(auto-fit,minmax(180px,1fr))] col-span-12 bg-white rounded-lg p-3">
      {data &&
        data?.map(item => (
          <Dialog key={item?.id}>
            <DialogTrigger asChild>
              <div
                onClick={() =>
                  handleItemClick(item?.name, item?.apiQuery, item?.accent)
                }
                className={`col-span-1 ${item.accent} text-white py-3 rounded text-center cursor-pointer`}>
                <h2 className="font-bold text-sm 2xl:text-2xl">
                  {item?.value ?? 0}
                </h2>
                <p className="text-xs 2xl:text-base">{item?.name ?? ''}</p>
              </div>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader className="col-span-12">
                <DialogTitle className="text-center">
                  {selectedItem?.name ?? ''}
                </DialogTitle>
                <DialogClose />
              </DialogHeader>
              <EmployeeStatsModal selectedItem={selectedItem} />
            </DialogContent>
          </Dialog>
        ))}
    </div>
  );
};

export default Insights;
