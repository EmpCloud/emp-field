import { Separator } from '@/components/ui/separator';
import { ChevronDown, ChevronUp } from 'lucide-react';
import * as React from 'react';
import * as RadioGroupPrimitive from '@radix-ui/react-radio-group';
import { useState } from 'react';
import Box from '@mui/material/Box';
import Slider from '@mui/material/Slider';
import Individualrpdaterange from './Individualrpdaterange';
import { RadioButton } from '@/components/ui/radiobutton';
import { fetchStage } from 'components/StageTaskModule/Api/get';
import { useQuery } from '@tanstack/react-query';

function valuetext(value) {
  return `${value}°C`;
}

const EmployeeReportPopup = ({
  collpsibleOpen,
  taskValueVolumeData,
  title,
  setCollpsibleOpen,
  index,
  onStatusSelect,
  onStageSelect,
  onDateSelect,
  onTaskVolumeSelect,
  onTaskValueSelect,
}) => {
  const taskStatusArray = [
    { displanName: 'Pending', value: 'Pending' },
    { displanName: 'Start', value: 'Start' },
    { displanName: 'Pause', value: 'Pause' },
    { displanName: 'Resume', value: 'Resume' },
    { displanName: 'Finish', value: 'Finish' },
    { displanName: 'Delete', value: 'Delete' },
  ];

  const { data } = useQuery({
    queryKey: ['fetchStageforFilter'],
    queryFn: fetchStage,
  });

  const taskStageArray =
    data?.data?.body?.data?.allTags?.map(tag => ({
      displanName: tag?.tagName,
      value: tag?.tagName,
    })) || [];

  // const taskStageArray = [
  //   { displanName: 'Pending', value: 'Pending' },
  //   { displanName: 'Contacted', value: 'Contacted' },
  //   { displanName: 'Negotiation', value: 'Negotiation' },
  //   { displanName: 'Finalization', value: 'Finalization' },
  //   { displanName: 'Deal Closed', value: 'Deal Closed' },
  //   { displanName: 'Order Processed', value: 'Order Processed' },
  // ];

  const [taskstatus, settaskstatus] = useState('');
  const [taskstage, settaskstage] = useState('');
  const [value, setValue] = useState([0, 0]);
  const [volume, setVolume] = useState([0, 0]);
  const [date, setDate] = useState('');

  const handleRadioClick = e => {
    e.stopPropagation(); // Prevent the radio from interfering with the collapsible trigger
  };

  const handleChangeValue = (event, newValue) => {
    setValue(newValue);
    onTaskValueSelect({ min: newValue[0], max: newValue[1] });
  };

  const handleChangeVolume = (event, newValue) => {
    setVolume(newValue);
    onTaskVolumeSelect({ min: newValue[0], max: newValue[1] });
  };

  const handleChangeStatus = value => {
    settaskstatus(value);
    onStatusSelect(value);
  };

  const handleChangeStage = value => {
    settaskstage(value);
    onStageSelect(value);
  };

  const onDateSelectss = dates => {
    setDate(dates);
  };
  const getDynamicMax = value => {
    if (!value || value <= 0) return 100;
    return Math.ceil(value / 100) * 100;
  };
  const getDynamicMaxUSD = value => {
    if (!value || value <= 0) return 100;
    return Math.ceil(value / 100) * 100;
  };
  const dynamicMaxVolume = getDynamicMax(taskValueVolumeData?.maxTaskVolume);
  const dynamicMaxUSD = getDynamicMaxUSD(
    taskValueVolumeData?.maxConvertedAmountInUSD
  );
  console.log(dynamicMaxUSD, 'dynamicMaxUSD');

  const chooseCollapsibleContent = index => {
    switch (index) {
      case 0:
        return (
          <RadioGroupPrimitive.Root
            className="w-full"
            onValueChange={handleChangeStatus}>
            {taskStatusArray.map((status, idx) => (
              <div
                key={idx}
                className="filter_content_options flex flex-col gap-[15px] pb-[15px] pl-[2rem] w-full">
                <div className="options_ flex gap-2 flex-col justify-start items-start">
                  <button className="flex gap-2 justify-start items-center">
                    <RadioButton
                      onClick={handleRadioClick}
                      id={status.value}
                      value={status.value}
                    />
                    <label
                      htmlFor={status.value}
                      className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
                      {status?.displanName ?? ''}
                    </label>
                  </button>
                </div>
              </div>
            ))}
          </RadioGroupPrimitive.Root>
        );
      case 1:
        return (
          <Individualrpdaterange
            onSelect={onDateSelect}
            onDateSelectRange={onDateSelectss}
          />
        );
      case 2:
        return (
          <RadioGroupPrimitive.Root
            className="w-full"
            onValueChange={handleChangeStage}>
            {taskStageArray.map((stage, idx) => (
              <div
                key={idx}
                className="filter_content_options flex flex-col gap-[15px] pb-[15px] pl-[2rem] w-full">
                <div className="options_ flex gap-2 flex-col justify-start items-start">
                  <button className="flex gap-2 justify-start items-center">
                    <RadioButton
                      onClick={handleRadioClick}
                      id={stage.value}
                      value={stage.value}
                    />
                    <label
                      htmlFor={stage.value}
                      className="text-xs font-semibold cursor-pointer text-[#3A3A3A]">
                      {(stage?.displanName ?? '').replace(/\b\w/g, char =>
                        char.toUpperCase()
                      )}
                    </label>
                  </button>
                </div>
              </div>
            ))}
          </RadioGroupPrimitive.Root>
        );
      case 3:
        return (
          <Box sx={{ width: '92%' }}>
            <Slider
              getAriaLabel={() => 'Task Value range'}
              value={value}
              max={dynamicMaxUSD}
              onChange={handleChangeValue}
              valueLabelDisplay="auto"
              getAriaValueText={valuetext}
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
        );
      case 4:
        return (
          <Box sx={{ width: '92%' }}>
            <Slider
              getAriaLabel={() => 'Task Volume range'}
              value={volume}
              max={dynamicMaxVolume}
              onChange={handleChangeVolume}
              valueLabelDisplay="auto"
              getAriaValueText={valuetext}
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
        );
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
          <label>
            {title === 'Task Status'
              ? taskstatus
              : title === 'Task Stage'
                ? taskstage
                : title === 'Task Volume'
                  ? `${volume[0]} - ${volume[1]}`
                  : title === 'Task Value'
                    ? `${value[0]} - ${value[1]}`
                    : ''}
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

export default EmployeeReportPopup;
