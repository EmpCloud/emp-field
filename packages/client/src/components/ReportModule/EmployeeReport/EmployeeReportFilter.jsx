import { Button } from '@/components/ui/button';
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs';
import * as Popover from '@radix-ui/react-popover';
import { ListFilter } from 'lucide-react';
import { useContext, useState } from 'react';
import { Separator } from '@/components/ui/separator';
import EmployeeReportPopup from './EmployeeReportPopup';
import { useEmployeeReportFilters } from 'context/Filters/FilterContext';
import ChartContext from 'components/ChartContext/Context';

const filterDataMenu = [
  'Task Status',
  'Date range',
  'Task Stage',
  'Task Value',
  'Task Volume',
];

const EmployeeReportFilter = () => {
  let { taskValueVolumeData, setTaskValueVolumeData } =
    useContext(ChartContext);
  const [collpsibleOpen, setCollpsibleOpen] = useState(
    Array(filterDataMenu.length).fill(false)
  );
  const { tempFilters, setTempFilters, selectedFilters, setSelectedFilters } =
    useEmployeeReportFilters();

  // Local state for temporary filter values
  const [popoverOpen, setPopoverOpen] = useState(false);
  const [isClicked, setIsClicked] = useState(false);

  const toggleCollapsible = index => {
    setCollpsibleOpen(prevState =>
      prevState.map((item, i) => (i === index ? !item : false))
    );
  };

  // Update local state on selection
  const handleStatusSelect = value => {
    setTempFilters(prev => ({
      ...prev,
      status: value,
    }));
  };
  const handleStageSelect = value => {
    setTempFilters(prev => ({
      ...prev,
      stage: value,
    }));
  };

  const handleDateSelect = value => {
    setTempFilters(prev => ({
      ...prev,
      dateRange: value,
    }));
  };
  const handleTaskVolumeSelect = value => {
    setTempFilters(prev => ({
      ...prev,
      taskVolume: value,
    }));
  };
  const handleTaskValueSelect = value => {
    setTempFilters(prev => ({
      ...prev,
      taskValue: value,
    }));
  };

  const applyFilters = () => {
    setSelectedFilters(tempFilters);
    setIsClicked(false); // Keep the popover open
    setPopoverOpen(false); // close the popover
  };

  // Clear filters
  const clearFilters = () => {
    const resetFilterState = {
      status: '',
      stage: '',
      dateRange: { startDate: '', endDate: '' },
      taskVolume: { min: '', max: '' },
      taskValue: { min: '', max: '' },
    };

    setTempFilters(resetFilterState);
    setSelectedFilters(resetFilterState);
    setPopoverOpen(false); // Close the popover only on "Clear Filter"
  };
  const isApplyEnabled = Object.values(tempFilters).some(value =>
    Array.isArray(value) ? value.length : value
  );
  const isClearEnabled = isApplyEnabled;

  return (
    <Popover.Root open={popoverOpen} onOpenChange={setPopoverOpen}>
      <Popover.Trigger asChild className="r">
        <Button
          variant="outline"
          className={`export-button flex gap-2 text-xs tracking-wide font-semibold py-3 w-[114px] h-[40px] ${
            isClicked ? 'bg-violet-500 text-white' : 'bg-white text-[#6A6AEC]'
          } border-[#6A6AEC] hover:bg-violet-500 border hover:text-white`}
          onClick={() => setIsClicked(!isClicked)}>
          <ListFilter className="w-4 h-4 2xl:w-5 2xl:h-5" />
          Filter
        </Button>
      </Popover.Trigger>
      <Popover.Content className="report-popover-content ">
        <div className="filter_content_container ml-5 select-none bg-white card-shadow w-[240px] h-full rounded-md flex flex-col items-center justify-start">
          <Tabs defaultValue="Apply" className="report-tabs-design">
            <TabsList className="report-tablist-design !flex !justify-between !px-[15px]">
              <TabsTrigger
                disabled={!isApplyEnabled}
                className={`text-xs font-bold px-5 ${
                  isApplyEnabled ? '' : 'opacity-50 cursor-not-allowed'
                }`}
                value="Apply"
                onClick={applyFilters}>
                Apply
              </TabsTrigger>
              <TabsTrigger
                disabled={!isClearEnabled}
                className={`text-xs font-bold px-5 text-[#1F3A78] ${
                  isClearEnabled ? '' : 'opacity-50 cursor-not-allowed'
                }`}
                value="Clear Filter"
                onClick={clearFilters}>
                Clear Filter
              </TabsTrigger>
            </TabsList>
          </Tabs>

          <Separator className="col-span-12 w-full" />
          <div className="filter_content_options_container flex flex-col items-center justify-start w-full ">
            {filterDataMenu.map((title, index) => (
              <EmployeeReportPopup
                key={index}
                collpsibleOpen={collpsibleOpen[index]}
                setCollpsibleOpen={() => toggleCollapsible(index)}
                title={title}
                index={index}
                taskValueVolumeData={taskValueVolumeData}
                onStatusSelect={handleStatusSelect}
                onStageSelect={handleStageSelect}
                onDateSelect={handleDateSelect}
                onTaskVolumeSelect={handleTaskVolumeSelect}
                onTaskValueSelect={handleTaskValueSelect}
              />
            ))}
          </div>
        </div>
      </Popover.Content>
    </Popover.Root>
  );
};

export default EmployeeReportFilter;
