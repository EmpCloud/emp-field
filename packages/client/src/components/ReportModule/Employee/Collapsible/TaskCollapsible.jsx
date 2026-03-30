import React from 'react';
import PDFIconImage from '../../../../assets/images/reportTable/pdf.png';
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';

import { ArrowDownToLine, ChevronDown, ChevronUp } from 'lucide-react';

import { Separator } from '@/components/ui/separator';
import StatusTimeLine from '../StatusTimeLine';
import { getStatusDescription } from 'components/TaskModule/TaskTable';
import moment from 'moment';

const TaskCollapsible = ({ task, isCollapsed, taskDetails }) => {
  const status = getStatusDescription(task?.taskApproveStatus);

  const [collapseIsOpen, setCollapseIsOpen] = React.useState(isCollapsed);
  const [isTaskCompleted] = React.useState(10);

  React.useEffect(() => {
    setCollapseIsOpen(isCollapsed);
  }, [isCollapsed]);

  const handleDownload = async url => {
    try {
      const response = await fetch(url);
      const blob = await response.blob();

      const link = document.createElement('a');

      const blobUrl = URL.createObjectURL(blob);

      link.href = blobUrl;
      link.setAttribute('download', url.split('/').pop());

      document.body.appendChild(link);

      link.click();

      document.body.removeChild(link);

      URL.revokeObjectURL(blobUrl);
    } catch (error) {
      console.error('Download failed:', error);
    }
  };
  const [titleExpanded, setTitleExpanded] = React.useState(false);

  return (
    <>
      <div className="grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg py-2 w-full  ">
        <Collapsible
          open={collapseIsOpen}
          onOpenChange={setCollapseIsOpen}
          className="w-full overflow-hidden">
          <div
            className={`bg-slate-400/10 hover:bg-slate-400/30 cursor-pointer lg:py-3 lg:px-[10px] 2xl:p-3 my rounded-lg border-l-[8px] px-2 py-3 ${status === 'Pause' && 'border-[#FF5960] '}
                        ${status === 'Finish' && 'border-[#4DB948]'}
                        ${status === 'Pending' && 'border-[#FFB800]'}
                        ${status === 'Start' && 'border-[#00A8FF]'}
                        ${status === 'Resume' && 'border-[#FFA500]'}
                        ${status === 'Delete' && 'border-[#FF0000]'} w-full max-w-full overflow-hidden`}>
            <CollapsibleTrigger asChild>
              <div
                className={`grid grid-cols-6 md:grid-cols-8 justify-center ${isTaskCompleted === 'inProgress' ? 'items-center' : 'items-center'}`}>
                <div className={`flex flex-col col-span-6 md:col-span-8 gap-0`}>
                  <div className="text-xs font-medium relative flex justify-between items-center gap-2 w-full">
                    <div className="flex items-center gap-2 flex-wrap">
                      <span>
                        {moment(task?.date ?? '').format('DD MMM YYYY')}
                      </span>
                      <span className="w-[3px] h-[3px] bg-red-900 rounded-full"></span>
                      <p>
                        <span className="font-semibold">Start Time: </span>
                        <span className="">
                          {moment(task?.start_time).format('hh:mm A')}
                        </span>
                      </p>
                      <span className="w-[3px] h-[3px] bg-red-900 rounded-full"></span>
                      <p>
                        <span className="font-semibold">End Time: </span>
                        <span className="">
                          {moment(task?.end_time).format('hh:mm A')}
                        </span>
                      </p>
                    </div>
                    {(task?.images?.length > 0 || task?.files?.length > 0) && (
                      <div className="flex justify-center gap-2 items-center">
                        <span className="text-[#6A6AEC] text-xs font-bold whitespace-nowrap">
                          {collapseIsOpen ? 'View Less' : 'View More'}
                        </span>
                        {collapseIsOpen ? (
                          <ChevronUp className="w-6 h-6 rounded-full text-white bg-gradient-to-r from-[#4CB4FE] to-[#8B85FF]" />
                        ) : (
                          <ChevronDown className="w-6 h-6 rounded-full text-white bg-gradient-to-r from-[#4CB4FE] to-[#8B85FF]" />
                        )}
                      </div>
                    )}
                  </div>
                  <div
                    className={`font-semibold 2xl:text-base text-xs flex justify-start items-center gap-2 flex-wrap w-full`}>
                    <span className="text-lg font-bold break-words whitespace-normal overflow-hidden">
                      {task?.taskName && task.taskName.length > 50 ? (
                        <>
                          {titleExpanded
                            ? task.taskName
                            : `${task.taskName.slice(0, 50)}... `}
                          <button
                            className="text-blue-600 underline ml-1 text-xs px-1 py-0 leading-tight"
                            type="button"
                            onClick={() => {
                              setTitleExpanded(!titleExpanded),
                                setCollapseIsOpen(false);
                            }}>
                            {titleExpanded ? 'Read Less' : 'Read More'}
                          </button>
                        </>
                      ) : (
                        (task?.taskName ?? '')
                      )}
                    </span>
                    <span
                      className={`w-2 h-2 rounded-full 
                        ${status === 'Pause' && 'bg-[#FF5960]'}
                        ${status === 'Finish' && 'bg-[#4DB948]'}
                        ${status === 'Pending' && 'bg-[#FFB800]'}
                        ${status === 'Start' && 'bg-[#00A8FF]'}
                        ${status === 'Resume' && 'bg-[#FFA500]'}
                        ${status === 'Delete' && 'bg-[#FF0000]'}
                      `}></span>
                    <span className="font-semibold">{status ?? ''}</span>
                  </div>
                  <p className="text-black mb-1 text-[11px] font-medium gap-2 break-words whitespace-normal">
                    {task?.taskDescription ?? ''}
                  </p>
                  <div className="emp_info_box_container flex justify-start items-center gap-2">
                    <div className="name_box flex flex-col justify-center items-center py-2 px-3 bg-[#FFAC64] text-white rounded-sm h-14">
                      <p className="text-xs relative top-1 sm:text-sm font-medium">
                        {task?.clientData[0]?.clientName ?? ''}
                      </p>
                      <p className="text-xs relative -top-[2px] font-normal">
                        Client Name
                      </p>
                    </div>
                    <div className="value_box flex flex-col justify-center items-center py-2 px-3 bg-[#1AB6AE] text-white rounded-sm h-14">
                      <p className="text-xs relative top-1 sm:text-sm font-medium">
                        {`${Math.ceil(task?.value?.convertedAmountInUSD) ?? ''}  ${'USD'}`}
                      </p>
                      <p className="text-xs relative -top-[2px] font-normal">
                        Task Value
                      </p>
                    </div>
                    <div className="volume_box flex flex-col justify-center items-center py-2 px-3 bg-[#6E67CA] text-white rounded-sm h-14">
                      <p className="text-xs relative top-1 sm:text-sm font-medium">
                        {task?.taskVolume ?? ''}
                      </p>
                      <p className="text-xs relative -top-[2px] font-normal">
                        Task Volume
                      </p>
                    </div>
                  </div>

                  {/* pasted div straight timelining */}
                  <StatusTimeLine
                    stagesWithColours={task?.tagLogsWithColors ?? ''}
                    allTags={taskDetails?.overAllTags ?? []}
                  />
                </div>
              </div>
            </CollapsibleTrigger>
            <CollapsibleContent className="CollapsibleContent w-full max-w-full">
              <Separator className="col-span-12 bg-[#D2D2D2] h-[1.4px] rounded my-3 mt-5" />
              {task?.images && task?.images?.length > 0 && (
                <>
                  <h4 className="font-bold text-sm pb-2">Attached Pictures</h4>
                  <div className="images_container_attached_file flex justify-start items-center gap-3 max-w-[30rem] sm:max-w-full w-full">
                    {task?.images &&
                      task?.images?.length > 0 &&
                      task.images?.map(image => (
                        <div
                          key={image._id}
                          className="file_image_container flex flex-col min-w-[178px] gap-2 rounded-md pb-2 bg-white">
                          <img
                            src={image?.url ?? ''}
                            alt={image.description || 'Attached Image'}
                            className="w-44 h-28 object-cover rounded-t-md"
                          />
                          <div className="description_ flex justify-between items-center gap-3 px-2">
                            <div className="flex justify-center items-center gap-1">
                              <span className="text-[10px] font-semibold">
                                {image.description || 'Image'}
                              </span>
                            </div>
                            <button
                              onClick={() => handleDownload(image?.url ?? '')}
                              className="w-6 h-6 bg-[#d6d6f0] p-[5px] rounded-full flex justify-center items-center"
                              aria-label="Download Image">
                              <ArrowDownToLine className="w-4 h-4" />
                            </button>
                          </div>
                        </div>
                      ))}
                  </div>
                  <Separator className="col-span-12 bg-[#D2D2D2] h-[1.4px] rounded my-3 mt-5" />
                </>
              )}

              {task?.files && task?.files?.length > 0 ? (
                <>
                  <h4 className="font-bold text-sm pb-2">Attached Files</h4>
                  <div className="w-full overflow-x-auto">
                    <div className="images_container_attached_file flex  gap-3  w-fit ">
                      {task?.files?.map(file => (
                        <div
                          key={file._id}
                          className="file_image_container flex flex-col min-w-[178px] gap-2 rounded-md pb-2 bg-white">
                          <img
                            src={PDFIconImage}
                            alt="PDF Icon"
                            className="w-44 h-28 object-cover rounded-t-md"
                          />
                          <div className="description_ flex justify-between items-center gap-3 px-2">
                            <div className="flex justify-center items-center gap-1">
                              <img
                                src={PDFIconImage}
                                className="w-4 h-4"
                                alt="pdf"
                              />
                              <span className="text-[10px] font-semibold">
                                {file.url.split('/').pop()}
                              </span>
                            </div>
                            <button
                              onClick={() => handleDownload(file?.url ?? '')}
                              className="w-6 h-6 bg-[#d6d6f0] p-[5px] rounded-full flex justify-center items-center"
                              aria-label="Download File">
                              <ArrowDownToLine className="w-4 h-4" />
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                </>
              ) : (
                ''
              )}
            </CollapsibleContent>
          </div>
        </Collapsible>
      </div>
    </>
  );
};

export default TaskCollapsible;
