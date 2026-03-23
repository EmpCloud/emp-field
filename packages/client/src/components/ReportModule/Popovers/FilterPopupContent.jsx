import { Separator } from '@/components/ui/separator';
import { ChevronDown, ChevronUp } from 'lucide-react';
import SearchInput from './PopoverCollapsibleContent/SearchInput';
import LibDateRange from './PopoverCollapsibleContent/LibDateRange';
import { RadioButton } from '@/components/ui/radiobutton';
import * as RadioGroupPrimitive from '@radix-ui/react-radio-group';
import { useState } from 'react';
import Box from '@mui/material/Box';
import Slider from '@mui/material/Slider';

const FilterPopupContent = ({
  collpsibleOpen,
  title,
  setCollpsibleOpen,
  index,
  onStatusSelect,
  onDateSelect,
  onLocationSelect,
  onRoleSelect,
  onDepartmentSelect,
}) => {
  const taskStatusArray = [
    { displanName: 'Finished', value: 'TasksFinished' },
    { displanName: 'Pending', value: 'TasksPending' },
    { displanName: 'Paused', value: 'TasksPaused' },
    { displanName: 'Resumed', value: 'TasksResumed' },
  ];
  const [error, setError] = useState(''); // Error state
  const [showStatusRanges, setShowStatusRanges] = useState(
    Array(taskStatusArray.length).fill(false)
  );
  const [statusRange, setStatusRange] = useState({
    minValue: 0,
    maxValue: 0,
  });
  const [selectedStatus, setSelectedStatus] = useState('');
  const [sliderValue, setSliderValue] = useState([0, 0]);

  const toggleStatusRange = index => {
    const newState = showStatusRanges.map((item, i) =>
      i === index ? !item : false
    );
    setShowStatusRanges(newState);

    if (newState[index]) {
      const status = taskStatusArray[index];
      setSelectedStatus(status.value); // Use the 'value' for internal use
      onStatusSelect({ status: status.value, range: statusRange }); // Send the 'value' for selection
    } else {
      setSelectedStatus('');
      onStatusSelect({ status: '', range: { minValue: 0, maxValue: 0 } });
    }
  };

  const handleSliderChange = (event, newValue) => {
    setSliderValue(newValue);
    setStatusRange({ minValue: newValue[0], maxValue: newValue[1] });
    onStatusSelect({
      status: selectedStatus,
      range: { minValue: newValue[0], maxValue: newValue[1] },
    });
  };

  const chooseCollapsibleContent = index => {
    switch (index) {
      case 0:
        return (
          <RadioGroupPrimitive.Root className="w-full">
            {taskStatusArray.map((status, idx) => (
              <div
                key={idx}
                className="filter_content_options flex flex-col gap-[15px] pb-[15px] pl-[2rem] w-full">
                <div className="options_ flex gap-2 flex-col justify-start items-start">
                  <div
                    onClick={() => toggleStatusRange(idx)} // Handle click for both the button and label
                    className="flex gap-2 justify-start items-center cursor-pointer">
                    <RadioButton id={status.value} value={status.value} />
                    <label
                      htmlFor={status.value}
                      className="text-xs font-semibold text-[#3A3A3A]">
                      {status?.displanName ?? ''}
                    </label>
                  </div>
                  {showStatusRanges[idx] && (
                    <div className="flex w-full flex-col gap-2 relative left-1">
                      <label className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
                        Select Range{' '}
                        <span className="text-red-500 text-xl">*</span>
                      </label>
                      <Box sx={{ width: '92%' }}>
                        <Slider
                          getAriaLabel={() => 'Task Value range'}
                          value={sliderValue}
                          onChange={handleSliderChange}
                          valueLabelDisplay="auto"
                          getAriaValueText={value => `${value}%`}
                          sx={{
                            '& .MuiSlider-track': {
                              background:
                                'linear-gradient(90deg, #A45CEC 0%, #0285EC 100%)',
                            },
                            '& .MuiSlider-thumb': {
                              background:
                                'linear-gradient(90deg, #A45CEC 0%, #0285EC 100%)',
                              border: '2px solid white',
                            },
                          }}
                        />
                      </Box>
                      {/* Conditionally render error message */}
                      {error && (
                        <p className="text-red-500 text-xs mt-2">{error}</p>
                      )}
                    </div>
                  )}
                </div>
              </div>
            ))}
          </RadioGroupPrimitive.Root>
        );
      case 1:
        return <LibDateRange onSelect={onDateSelect} />;
      case 2:
        return <SearchInput dataType="location" onSelect={onLocationSelect} />;
      case 3:
        return <SearchInput dataType="role" onSelect={onRoleSelect} />;
      case 4:
        return (
          <SearchInput dataType="department" onSelect={onDepartmentSelect} />
        );
      // case 5:
      //   return (
      //     <RadioGroupPrimitive.Root>
      //       <div className="filter_content_options flex flex-col gap-[15px] pb-[15px] pt-0 pl-10 pr-[4.5rem] w-full">
      //         <div className="options_ flex gap-2 justify-start items-center">
      //           <RadioButton id="pending" value="pending" />
      //           <label
      //             htmlFor="pending"
      //             className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
      //             Pending
      //           </label>
      //         </div>
      //         <div className="options_ flex gap-2 justify-start items-center">
      //           <RadioButton id="contacted" value="contacted" />
      //           <label
      //             htmlFor="contacted"
      //             className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
      //             Contacted
      //           </label>
      //         </div>
      //         <div className="options_ flex gap-2 justify-start items-center">
      //           <RadioButton id="negotiation" value="negotiation" />
      //           <label
      //             htmlFor="negotiation"
      //             className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
      //             Negotiation
      //           </label>
      //         </div>
      //         <div className="options_ flex gap-2 justify-start items-center">
      //           <RadioButton id="finalization" value="finalization" />
      //           <label
      //             htmlFor="finalization"
      //             className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
      //             Finalization
      //           </label>
      //         </div>
      //         <div className="options_ flex gap-2 justify-start items-center">
      //           <RadioButton id="deal-closed" value="deal-closed" />
      //           <label
      //             htmlFor="deal-closed"
      //             className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
      //             Deal Closed
      //           </label>
      //         </div>
      //         <div className="options_ flex gap-2 justify-start items-center">
      //           <RadioButton id="order-processed" value="order-processed" />
      //           <label
      //             htmlFor="order-processed"
      //             className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
      //             Order Processed
      //           </label>
      //         </div>
      //       </div>
      //     </RadioGroupPrimitive.Root>
      //   );
      // case 6:
      //   return (
      //     <div className="border-none flex justify-center px-4 relative rounded-md w-full max-w-full mt-0 mb-3">
      //       <input
      //         type="text"
      //         placeholder="Enter task value"
      //         className="bg-slate-400/10 ps-3 focus:outline-none placeholder:font-extrabold placeholder:text-[#4D4C4C] font-extrabold text-xs 2xl:text-sm py-1 rounded-md w-full max-w-full"
      //         style={{
      //           padding: '10px',
      //           height: '34px',
      //           width: '180px',
      //           margin: 'auto',
      //         }}
      //       />
      //     </div>
      //   );
      // case 7:
      //   return (
      //     <div className="border-none px-4 flex justify-center relative rounded-md w-full max-w-full mt-0 mb-3">
      //       <input
      //         type="number"
      //         placeholder="Enter task volume"
      //         className="bg-slate-400/10 ps-3 focus:outline-none placeholder:font-extrabold placeholder:text-[#4D4C4C] font-extrabold text-xs 2xl:text-sm py-1 rounded-md w-full max-w-full"
      //         style={{
      //           padding: '10px',
      //           height: '34px',
      //           width: '180px',
      //           margin: 'auto',
      //         }}
      //       />
      //     </div>
      //   );
      default:
        return null;
    }
  };

  return (
    <div className="filter_content_option relative flex flex-col py-1 pb-0 justify-center items-center w-full">
      <div
        onClick={setCollpsibleOpen}
        className="heading_section_filter cursor-pointer flex justify-between items-center w-full py-[15px] px-[15px]">
        <div className="filter_content_heading flex justify-center gap-2 items-center">
          <label
            htmlFor={title}
            className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
            {title}
          </label>
        </div>
        <div className="custom-collapsible-container">
          {collpsibleOpen ? (
            <ChevronUp className="h-4 cursor-pointer" />
          ) : (
            <ChevronDown className="h-4 cursor-pointer" />
          )}
        </div>
      </div>
      {collpsibleOpen ? <>{chooseCollapsibleContent(index)}</> : null}
      <Separator className="col-span-12 w-full" />
    </div>
  );
};

export default FilterPopupContent;
