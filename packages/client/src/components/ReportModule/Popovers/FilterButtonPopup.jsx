import { Button } from '@/components/ui/button';
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs';
import * as Popover from '@radix-ui/react-popover';
import { ListFilter } from 'lucide-react';
import { useState } from 'react';
import { Separator } from '@/components/ui/separator';
import FilterPopupContent from './FilterPopupContent';
import { useFilterPopup } from 'context/Filters/FilterContext';

const filterDataMenu = [
  'Task Status',
  'Date range',
  'Location',
  'Role',
  'Department',
];

const FilterButtonPopup = ({ onApplyFilters }) => {
  const {
    tempFilters,
    setTempFilters,
    selectedFilters,
    setSelectedFilterContext,
  } = useFilterPopup();
  const [collpsibleOpen, setCollpsibleOpen] = useState(
    Array(filterDataMenu.length).fill(false)
  );
  const [popoverOpen, setPopoverOpen] = useState(false);
  const [isClicked, setIsClicked] = useState(false);

  const toggleCollapsible = index => {
    setCollpsibleOpen(prevState =>
      prevState.map((item, i) => (i === index ? !item : false))
    );
  };

  const handleStatusSelect = ({ status, range }) => {
    setTempFilters(prev => ({
      ...prev,
      status: { status, range },
    }));
  };

  const handleDateSelect = value => {
    setTempFilters(prev => ({
      ...prev,
      dateRange: value,
    }));
  };

  const handleLocationSelect = value => {
    setTempFilters(prev => ({
      ...prev,
      location: value,
    }));
  };

  const handleRoleSelect = value => {
    setTempFilters(prev => ({
      ...prev,
      role: value,
    }));
  };

  const handleDepartmentSelect = value => {
    setTempFilters(prev => ({
      ...prev,
      department: value,
    }));
  };

  const applyFilters = () => {
    setSelectedFilterContext(tempFilters);
    setIsClicked(false); // Keep the popover open
  };

  const clearFilters = () => {
    const resetFilters = {
      status: { status: '', range: { minValue: '', maxValue: '' } },
      dateRange: { startDate: '', endDate: '' },
      location: '',
      role: '',
      department: '',
    };

    setTempFilters(resetFilters);
    setSelectedFilterContext(resetFilters);
    setPopoverOpen(false); // Close the popover only on "Clear Filter"
  };

  const isApplyEnabled = Object.values(tempFilters).some(value =>
    Array.isArray(value) ? value.length : value
  );
  const isClearEnabled = isApplyEnabled;

  return (
    <Popover.Root open={popoverOpen} onOpenChange={setPopoverOpen}>
      <Popover.Trigger asChild>
        <Button
          variant="outline"
          className={`export-button flex gap-2 text-xs tracking-wide font-semibold py-3 ${
            isClicked ? 'bg-violet-500 text-white' : 'bg-white text-[#6A6AEC]'
          } border-[#6A6AEC] hover:bg-violet-500 border hover:text-white`}
          onClick={() => setIsClicked(!isClicked)}>
          <ListFilter className="w-4 h-4 2xl:w-5 2xl:h-5" />
          Filter
        </Button>
      </Popover.Trigger>
      <Popover.Content className="report-popover-content">
        <div className="filter_content_container ml-5 select-none bg-white card-shadow w-[240px] h-full rounded-md flex flex-col items-center justify-start">
          <Tabs defaultValue="Apply" className="report-tabs-design">
            <TabsList className="report-tablist-design">
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
              <FilterPopupContent
                key={index}
                collpsibleOpen={collpsibleOpen[index]}
                setCollpsibleOpen={() => toggleCollapsible(index)}
                title={title}
                index={index}
                onStatusSelect={handleStatusSelect}
                onDateSelect={handleDateSelect}
                onLocationSelect={handleLocationSelect}
                onRoleSelect={handleRoleSelect}
                onDepartmentSelect={handleDepartmentSelect}
              />
            ))}
          </div>
        </div>
      </Popover.Content>
    </Popover.Root>
  );
};

export default FilterButtonPopup;
