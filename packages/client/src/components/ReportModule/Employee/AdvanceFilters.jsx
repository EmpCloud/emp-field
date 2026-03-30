import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';
import { useState } from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';

import EmployeeSearchAdvanceFilter from './EmployeeSearchAdvanceFilter';
import EmplyeeSelectFilter from './AdvanceFilters/EmplyeeSelectFilter';

const AdvanceFilters = () => {
  const [collapseIsOpen, setCollapseIsOpen] = useState(false);
  const RolesArray = [
    {
      _id: 1,
      role: 'Manager',
    },
    {
      _id: 2,
      role: 'Developer',
    },
  ];
  const ManagerRolesArray = [
    {
      _id: 1,
      role: 'Manager -1',
    },
    {
      _id: 2,
      role: 'Manager -2',
    },
  ];
  const DeveloperRolesArray = [
    {
      _id: 1,
      role: 'Manager -1',
    },
    {
      _id: 2,
      role: 'Manager -2',
    },
  ];

  return (
    <>
      <div className="card-shadow grid grid-cols-12 col-span-12 bg-white rounded-lg py-3 px-4 w-full gap-4">
        <Collapsible
          open={collapseIsOpen}
          onOpenChange={setCollapseIsOpen}
          style={{ width: '100%' }}
          className="col-span-12 w-full items-center flex justify-between flex-col">
          {/* <div
            className={`bg-slate-400/10 hover:bg-slate-400/30 cursor-pointer p-3 my rounded-lg border-l-[8px] border-[#FD7F7F] w-full max-w-full`}> */}
          <CollapsibleTrigger asChild>
            <div className="col-span-12 w-full items-center flex justify-between">
              <p className="text-md 2xl:text-xl font-semibold text-[#1F3A78]">
                Advance Filters
              </p>
              {collapseIsOpen ? (
                <ChevronUp className="w-5 h-5 cursor-pointer" />
              ) : (
                <ChevronDown className="w-5 h-5 cursor-pointer" />
              )}
            </div>
          </CollapsibleTrigger>

          <CollapsibleContent className="mt-2 CollapsibleContent w-full">
            <Separator className="col-span-12" />
            <div className="filter_label_container col-span-12 flex flex-wrap w-full gap-2 gap-y-3 justify-between">
              <div className="col-span-12 flex flex-wrap gap-2">
                <EmplyeeSelectFilter
                  placeholder={'Select Role'}
                  rolesArray={RolesArray}
                />
                <EmplyeeSelectFilter
                  placeholder={'Select Manager'}
                  rolesArray={ManagerRolesArray}
                />
                <EmployeeSearchAdvanceFilter />
              </div>
              <div className="action-button col-span-12 sm:col-span-4 flex w-fit sm:w-auto justify-end items-center gap-5">
                {/* <FilterButtonPopup /> */}
                <Button
                  variant="fill"
                  className={`export-button flex gap-2 tracking-wide font-semibold py-3 h-7 border-[#1F3A78] text-white text-xs bg-[#1F3A78] border hover:text-white`}>
                  Apply
                </Button>
              </div>
            </div>
          </CollapsibleContent>
          {/* </div> */}
        </Collapsible>
      </div>
    </>
  );
};

export default AdvanceFilters;
