import { X } from 'lucide-react';
import ExportButtonPopup from './Popovers/ExportButtonPopup';
import FilterButtonPopup from './Popovers/FilterButtonPopup';
import { Separator } from '@/components/ui/separator';
import { useEffect, useState } from 'react';
import moment from 'moment';
import { useFilterPopup } from 'context/Filters/FilterContext';

const ReportFilter = ({
  exportPDF,
  exportCSV,
  exportXLS,
  onFilterChange,
  setSelectedFilters,
}) => {
  const {
    tempFilters,
    setTempFilters,
    selectedFilters,
    setSelectedFilterContext,
  } = useFilterPopup();
  const [appliedFilters, setAppliedFilters] = useState([]);

  useEffect(() => {
    const newFilters = [];
    if (selectedFilters.status?.status) {
      const { status, range } = selectedFilters.status;
      const rangeString =
        range?.minValue || range?.maxValue
          ? ` (${range.minValue} - ${range.maxValue})`
          : '';
      newFilters.push(`Status: ${status}${rangeString}`);
    }
    if (
      selectedFilters.dateRange?.startDate &&
      selectedFilters.dateRange?.endDate
    ) {
      newFilters.push(
        `Date: ${moment(selectedFilters.dateRange.startDate).format('YYYY-MM-DD')} to ${moment(selectedFilters.dateRange.endDate).format('YYYY-MM-DD')}`
      );
    }
    if (selectedFilters.location) {
      newFilters.push(`Location: ${selectedFilters.location}`);
    }
    if (selectedFilters.role) {
      newFilters.push(`Role: ${selectedFilters.role}`);
    }
    if (selectedFilters.department) {
      newFilters.push(`Department: ${selectedFilters.department}`);
    }

    setAppliedFilters(newFilters); // Save applied filters
    setSelectedFilters(selectedFilters); // Pass the selected filters up to the parent component
    onFilterChange(selectedFilters);
  }, [selectedFilters, setSelectedFilters, onFilterChange]);

  const removeFilter = filter => {
    const [type] = filter.split(': ').map(part => part.trim());

    switch (type) {
      case 'Status':
        setSelectedFilterContext(prev => ({
          ...prev,
          status: { status: '', range: { minValue: '', maxValue: '' } },
        }));
        setTempFilters(prev => ({
          ...prev,
          status: { status: '', range: { minValue: '', maxValue: '' } },
        }));
        break;
      case 'Date':
        setSelectedFilterContext(prev => ({
          ...prev,
          dateRange: { startDate: '', endDate: '' },
        }));
        setTempFilters(prev => ({
          ...prev,
          dateRange: { startDate: '', endDate: '' },
        }));
        break;
      case 'Location':
        setSelectedFilterContext(prev => ({ ...prev, location: '' }));
        setTempFilters(prev => ({ ...prev, location: '' }));
        break;
      case 'Role':
        setSelectedFilterContext(prev => ({ ...prev, role: '' }));
        setTempFilters(prev => ({ ...prev, role: '' }));
        break;
      case 'Department':
        setSelectedFilterContext(prev => ({ ...prev, department: '' }));
        setTempFilters(prev => ({ ...prev, department: '' }));
        break;
      default:
        break;
    }

    // Remove the filter from appliedFilters
    setAppliedFilters(prev => prev.filter(f => f !== filter));
  };

  return (
    <div className="card-shadow grid grid-cols-12 col-span-12 bg-white rounded-lg p-5 w-full gap-4">
      <div className="col-span-12 sm:col-span-8 w-fit flex justify-start">
        <h3 className="text-md 2xl:text-xl font-semibold text-[#1F3A78]">
          Consolidated Report
        </h3>
      </div>
      <div className="action-button col-span-12 sm:col-span-4 flex w-fit sm:w-auto justify-end items-center gap-5">
        <FilterButtonPopup />

        <ExportButtonPopup
          exportPDF={exportPDF}
          exportCSV={exportCSV}
          exportXLS={exportXLS}
        />
      </div>
      <Separator className="col-span-12" />

      <div className="filter_label_container col-span-12 flex flex-wrap w-full sm:w-auto justify-start items-center gap-2">
        {appliedFilters?.map((filter, index) => (
          <div
            key={index}
            className="filter_label flex justify-start gap-2 items-center bg-[#F1F1FF] rounded-sm py-[3px] px-2">
            <span className="text-xs font-medium capitalize text-[#6A6AEC]">
              {filter}
            </span>
            <X
              className="w-3 h-3 cursor-pointer text-[#6A6AEC]"
              onClick={() => removeFilter(filter)}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default ReportFilter;
