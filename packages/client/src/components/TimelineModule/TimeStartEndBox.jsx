import { AiOutlineCheck } from 'react-icons/ai';
import { BiPause } from 'react-icons/bi';
import { MdDelete } from 'react-icons/md';
import { BiPlay } from 'react-icons/bi';
import { BiTimeFive } from 'react-icons/bi';
import React from 'react';

import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';

import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';

import { Progress } from '@/components/ui/progress';
import { LogIn, LogOut } from 'lucide-react';

const TimeStartEndBox = ({
  progress,
  checkIn,
  loginTime,
  taskName,
  taskDesc,
  clientName,
}) => {
  const [collapseIsOpen, setCollapseIsOpen] = React.useState(false);
  const [isTaskCompleted] = React.useState(progress);
  const [cardBorderColor, setCardBorderColor] = React.useState(null);

  // const [openId, setOpenId] = React.useState(null);

  // const toggle = id => {
  //   setOpenId(openId === id ? null : id);
  // };

  React.useEffect(() => {
    switch (isTaskCompleted) {
      case 'Pending':
        setCardBorderColor('border-[#FFB800]');
        break;
      case 'Start':
        setCardBorderColor('border-[#6A6AEC]');
        break;
      case 'Pause':
        setCardBorderColor('border-[#FF5960]');
        break;
      case 'Resume':
        setCardBorderColor('border-[#5190EF]');
        break;
      case 'Finish':
        setCardBorderColor('border-[#4DB948]');
        break;
      case 'Delete':
        setCardBorderColor('border-[#E9333B]');
        break;
      default:
        console.warn('Unexpected task completion state:', isTaskCompleted);
        break;
    }
  }, [isTaskCompleted]);

  const renderStatus = () => {
    switch (isTaskCompleted) {
      case 'Pending':
        return (
          <>
            <TooltipTrigger>
              <div className="bg-[#FFB800] p-1 rounded-full w-4 h-4 2xl:w-6 2xl:h-6 2xl:hidden">
                <BiTimeFive className="rounded-full text-white w-2 h-2 2xl:w-4 2xl:h-4" />
              </div>
            </TooltipTrigger>
            <div className="bg-[#FFB800] text-white text-center text-xs px-2 py-1 rounded-full hidden 2xl:block">
              Pending
            </div>
            <TooltipContent>
              <p className="text-xs">Pending</p>
            </TooltipContent>
          </>
        );
      case 'Start':
        return (
          <>
            <TooltipTrigger>
              <div className="bg-[#6A6AEC] p-1 rounded-full w-4 h-4 2xl:w-6 2xl:h-6 2xl:hidden">
                <BiPlay className="rounded-full text-white w-2 h-2 2xl:w-4 2xl:h-4" />
              </div>
            </TooltipTrigger>
            <div className="bg-[#6A6AEC] text-white text-center text-xs px-2 py-1 rounded-full hidden 2xl:block">
              Start
            </div>
            <TooltipContent>
              <p className="text-xs">Start</p>
            </TooltipContent>
          </>
        );
      case 'Pause':
        return (
          <>
            <TooltipTrigger>
              <div className="bg-[#FF5960] p-1 rounded-full w-4 h-4 2xl:w-6 2xl:h-6 2xl:hidden">
                <BiPause className="rounded-full text-white w-2 h-2 2xl:w-4 2xl:h-4" />
              </div>
            </TooltipTrigger>
            <div className="bg-[#FF5960] text-white text-center text-xs px-2 py-1 rounded-full hidden 2xl:block">
              Pause
            </div>
            <TooltipContent>
              <p className="text-xs">Pause</p>
            </TooltipContent>
          </>
        );
      case 'Resume':
        return (
          <>
            {/* <div className="bg-[#5190EF] p-1 rounded-full w-4 h-4 2xl:w-6 2xl:h-6">
                    <BiPlay className="rounded-full text-white w-2 h-2 2xl:w-4 2xl:h-4" />
                  </div> */}
            <TooltipContent>
              <p className="text-xs">Resume</p>
            </TooltipContent>
          </>
        );
      case 'Finish':
        return (
          <>
            <div className="bg-[#4DB948] p-1 rounded-full w-4 h-4 2xl:w-6 2xl:h-6">
              <AiOutlineCheck className="rounded-full text-white w-2 h-2 2xl:w-4 2xl:h-4" />
            </div>
            <TooltipContent>
              <p className="text-xs">Finish</p>
            </TooltipContent>
          </>
        );
      case 'Delete':
        return (
          <>
            <TooltipTrigger>
              <div className="bg-[#E9333B] p-1 rounded-full w-4 h-4 2xl:w-6 2xl:h-6 2xl:hidden">
                <MdDelete className="rounded-full text-white w-2 h-2 2xl:w-4 2xl:h-4" />
              </div>
            </TooltipTrigger>
            <div className="bg-[#E9333B] text-white text-center text-xs px-2 py-1 rounded-full hidden 2xl:block">
              Delete
            </div>
            <TooltipContent>
              <p className="text-xs">Delete</p>
            </TooltipContent>
          </>
        );
      default:
        return <></>;
    }
  };
  return (
    <>
      <div className="relative">
        <Progress value={100} />
        <div className=" w-full flex">
          <div className="time-container md:w-[102px] sm:w-[82px] w-[72px]  pl-[18px]">
            <span className="2xl:text-xs text-xxs font-medium flex">
              <span className="text-violet-600 font-semibold -ms-[6px]">
                <span className="flex items-center text-[9px] gap-0.5">
                  {checkIn ? (
                    <>
                      {' '}
                      <LogIn className="w-3 h-3 border p-0.5 border-violet-500 rounded-full" />
                      Check In
                    </>
                  ) : (
                    <>
                      <LogOut className="w-3 h-3 border p-0.5 border-violet-500 rounded-full" />
                      Check out
                    </>
                  )}
                </span>
                <b className="ms-2.5 text-blue-600">{loginTime ?? '-'}</b>
              </span>
            </span>
          </div>
          <div
            className={`bg-slate-400/10 hover:bg-slate-400/30 cursor-pointer  p-2 my rounded-lg border-l-[8px] timeline-card ${cardBorderColor}  md:w-[calc(100%-102px)] sm:w-[calc(100%-82px)] w-[calc(100%-72px)]`}>
            <div
              className={`grid grid-cols-6 md:grid-cols-8 justify-between ${isTaskCompleted === 'inProgress' ? 'items-center' : 'items-center'}`}>
              <div className={`flex flex-col col-span-5 md:col-span-7`}>
                <div className="font-semibold 2xl:text-base text-xs py-6">
                  {taskName}
                  {/* Meeting with client for sales and packaging */}
                </div>
                <div
                  className={`hidden 2xl:text-sm text-xxs mt-1 text-muted-foreground font-medium md:inline ${open ? 'hidden' : ''}`}>
                  {/* Schedule Time */}
                </div>
                <p className="text-blue-500 text-xs 2xl:text-sm font-semibold">
                  {/* {startTime} - {endTime} */}
                </p>
              </div>
              <div className={`col-span-1 flex justify-center items-center`}>
                <TooltipProvider delayDuration={100}>
                  <Tooltip>{renderStatus()}</Tooltip>
                </TooltipProvider>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default TimeStartEndBox;
