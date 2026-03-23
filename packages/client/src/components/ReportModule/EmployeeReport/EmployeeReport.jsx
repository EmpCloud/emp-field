import { Button } from '@/components/ui/button';
import { ClipboardMinus, ListFilter, X } from 'lucide-react';
import ReportDashboardIconPink from '../../../assets/images/reportTable/carbon_report.png';
import ReportDashboardIconWhite from '../../../assets/images/reportTable/carbon_report_white.png';
import ProfileIcon from '../../../assets/images/employee.png';
import { Separator } from '@/components/ui/separator';
import { useContext, useEffect, useState } from 'react';
import * as Popover from '@radix-ui/react-popover';
// import CSVIconImage from "../../../../assets/images/reportTable/csv.png"
import CSVIconImage from '../../../assets/images/reportTable/csv.png';
import PDFIconImage from '../../../assets/images/reportTable/pdf.png';
import XLSIconImage from '../../../assets/images/reportTable/xls.png';
import ReportDashboardIcon from '../../../assets/images/reportTable/carbon_report.png';
import { useLocation } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { getReportEmployees } from '../Api/post';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import moment from 'moment';
import EmployeeReportFilter from './EmployeeReportFilter';
import { useEmployeeReportFilters } from 'context/Filters/FilterContext';
// import { useFilters } from 'context/Filters/FilterContext';
import ChartContext from 'components/ChartContext/Context';

import React, { useRef } from 'react';

// Sample Lottie animation data

const EmployeeReport = ({
  // employeeId,
  // setFilter,
  // exportPDF,
  // employeeId,
  // setStartDate,
  // setEndDate,
  // // setSelectedFilters,
  // setIsClicked,
  empolyeeDeatils,
  employeeId,
  setStartDate,
  setEndDate,
  exportPDF,

  // setSelectedFilters={setSelectedFilters}
  filters,
  setIsClicked,
  setFilter,
}) => {
  const navigate = useNavigate();
  const [isDashboardClicked, setIsDashboardClicked] = useState(false);
  const { selectedFilters, setSelectedFilters, tempFilters, setTempFilters } =
    useEmployeeReportFilters();
  const { isLoading, isPending, error, data } = useQuery({
    queryKey: ['repoData', employeeId],
    queryFn: () => getReportEmployees(employeeId),
  });
  const [appliedFilters, setAppliedFilters] = useState([]);
  const location = useLocation();
  useEffect(() => {
    const isOnEmployeeReport = location.pathname === '/admin/employee-report';
    const empId = new URLSearchParams(location.search).get('empId');
    if (isOnEmployeeReport && empId) {
      setSelectedFilters({
        status: '',
        dateRange: { startDate: '', endDate: '' },
        stage: '',
        taskValue: { min: '', max: '' },
        taskVolume: { min: '', max: '' },
      });
      setTempFilters({
        status: '',
        dateRange: { startDate: '', endDate: '' },
        stage: '',
        taskValue: { min: '', max: '' },
        taskVolume: { min: '', max: '' },
      });
      setAppliedFilters([]);
    }
  }, [location.pathname, location.search]);
  const imgRef = useRef('');
  const {
    employeeReportData,
    setemployeeReportData,
    empProfileimg,
    setprofileImg,
    profileImg,
    profileUrlImg,
    picRatio,
    setpicRatio,
  } = useContext(ChartContext);
  useEffect(() => {
    const newFilters = [];
    if (selectedFilters.status) {
      newFilters.push(`Status: ${selectedFilters.status}`);
    }
    if (
      selectedFilters.dateRange?.startDate &&
      selectedFilters.dateRange?.endDate
    ) {
      newFilters.push(
        `Date: ${moment(selectedFilters.dateRange.startDate).format('YYYY-MM-DD')} to ${moment(selectedFilters.dateRange.endDate).format('YYYY-MM-DD')}`
      );
    }
    if (selectedFilters.stage) {
      newFilters.push(`Stage: ${selectedFilters.stage}`);
    }
    if (selectedFilters.taskValue.min || selectedFilters.taskValue.max) {
      newFilters.push(
        `Task Value: ${selectedFilters.taskValue.min} to ${selectedFilters.taskValue.max}`
      );
    }
    if (selectedFilters.taskVolume.min || selectedFilters.taskVolume.max) {
      newFilters.push(
        `Task Volume: ${selectedFilters.taskVolume.min} to ${selectedFilters.taskVolume.max}`
      );
    }

    setAppliedFilters(newFilters); // Save applied filters
    setFilter(selectedFilters); // Pass the selected filters up to the parent component
  }, [selectedFilters, setFilter]);

  const removeFilter = filter => {
    const [type] = filter.split(': ').map(part => part.trim());

    // Reset the corresponding selected filter and temp filter
    switch (type) {
      case 'Status':
        setSelectedFilters(prev => ({ ...prev, status: '' }));
        setTempFilters(prev => ({ ...prev, status: '' }));
        break;
      case 'Date':
        setSelectedFilters(prev => ({
          ...prev,
          dateRange: { startDate: '', endDate: '' },
        }));
        setTempFilters(prev => ({
          ...prev,
          dateRange: { startDate: '', endDate: '' },
        }));
        break;
      case 'Stage':
        setSelectedFilters(prev => ({ ...prev, stage: '' }));
        setTempFilters(prev => ({ ...prev, stage: '' }));
        break;
      case 'Task Value':
        setSelectedFilters(prev => ({
          ...prev,
          taskValue: { min: '', max: '' },
        }));
        setTempFilters(prev => ({
          ...prev,
          taskValue: { min: '', max: '' },
        }));
        break;
      case 'Task Volume':
        setSelectedFilters(prev => ({
          ...prev,
          taskVolume: { min: '', max: '' },
        }));
        setTempFilters(prev => ({
          ...prev,
          taskVolume: { min: '', max: '' },
        }));
        break;
      default:
        break;
    }

    // Remove the filter from appliedFilters
    setAppliedFilters(prev => prev.filter(f => f !== filter));
  };

  const employeeDetails = data?.data?.body?.data;

  if (!employeeDetails) {
    // navigate('/admin/employee')
    console.log('404');
  }

  useEffect(() => {
    setemployeeReportData(employeeDetails);

    if (data) {
      setprofileImg(data?.data?.body?.data?.profilePic);
    }
  }, [employeeDetails]);

  const GetPicwh = imgElement => {
    if (imgElement) {
      const width = imgElement.naturalWidth; // Get the natural width
      const height = imgElement.naturalHeight; // Get the natural height

      // Assuming the image is being added to a jsPDF instance
      // const doc = new jsPDF();

      // Set image width and height dynamically (e.g., scale image)
      // const scale = 47 / width;  // Scale image width to fit a specific value (47px in this case)
      // const imgWidth = width * scale;
      // const imgHeight = height * scale;
      if (width > height) {
        const ratio = Math.round((width / height) * 10) / 10;
        // console.log(ratio);
        setpicRatio(ratio);
      }
      if (width < height) {
        const ratio = Math.round((height / width) * 10) / 10;
        // console.log(ratio);
        setpicRatio(ratio);
      }

      // Add image to the PDF with calculated width and height
      // doc.addImage(imgElement.src, 'PNG', 20, 155, imgWidth, imgHeight);  // Adjust x, y, and position as needed

      // doc.save('profile.pdf');  // Save the generated PDF
    }
  };

  useEffect(() => {
    if (imgRef.current) {
      GetPicwh(imgRef.current); // Pass the image DOM element to the function
    }
  }, [data]);

  // const [canvasData, setCanvasData] = useState("");

  // useEffect(() => {
  //   const context = imgRef.current.getContext("2d");
  //   const image = new Image();
  //   image.src =
  //     "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/Picture_icon_BLACK.svg/1200px-Picture_icon_BLACK.svg.png";
  //   image.crossOrigin = "anonymous"; // Ensure CORS is handled for external images

  //   image.onload = () => {
  //     // Draw the image on the canvas
  //     context.drawImage(image, 0, 0, 500, 500);

  //     // Convert the canvas content to a Base64 string
  //     const base64String = imgRef.current.toDataURL("image/png");

  //     // Store the Base64 string in state
  //     setCanvasData(base64String);
  //   };

  //   image.onerror = (error) => {
  //     console.error("Error loading image:", error);
  //   };
  // }, [employeeDetails]);
  const playerRef = useRef(null);
  const [lotplay, setlotplay] = useState(false);
  const handleMouseEnter = () => {
    setlotplay(true);
  };

  const handleMouseLeave = () => {
    setlotplay(false);
  };
  return (
    <>
      <div className="card-shadow grid grid-cols-12 col-span-12 bg-white rounded-lg p-5 w-full gap-4">
        {isLoading ? (
          'loading....'
        ) : (
          <div className="grid grid-cols-12 col-span-12 bg-white rounded-lg w-full gap-4">
            <div className="col-span-12 sm:col-span-8 w-fit flex justify-start gap-3">
              <div
                onMouseEnter={handleMouseEnter}
                onMouseLeave={handleMouseLeave}>
                <img
                  src={
                    employeeDetails?.profilePic == null ||
                    employeeDetails?.profilePic == 'PROFILE'
                      ? 'https://api.dicebear.com/9.x/initials/svg?seed=' +
                        employeeDetails?.fullName
                      : employeeDetails?.profilePic
                  }
                  ref={imgRef}
                  className="w-[71px] h-[71px] rounded-[36px]"
                  alt="profile"
                />
              </div>
              <div className="employee_information flex flex-col justify-center">
                <div className="emp_id text-xs font-medium text-black">
                  Employee ID - {employeeDetails?.emp_id}
                  {/* <ProfileEmployee /> */}
                </div>
                <div className="emp_name text-[22px] font-semibold text-[#1F3A78]">
                  {employeeDetails?.fullName}
                </div>
                <div className="emp_designation text-[#1F3A78] text-sm font-semibold whitespace-nowrap">
                  {employeeDetails?.role} - {employeeDetails?.email}
                </div>
              </div>
            </div>
            <div className="action-button col-span-12 sm:col-span-4 flex w-fit sm:w-auto justify-end items-center gap-3">
              <EmployeeReportFilter />
              <Popover.Root>
                <Popover.Trigger asChild>
                  <Button
                    variant="outline"
                    className={`export-buttons flex gap-1 text-xs tracking-wide font-semibold h-[40px] w-[134px] ${
                      isDashboardClicked
                        ? 'bg-violet-500 text-white'
                        : 'bg-white text-[#6A6AEC]'
                    } border-[#6A6AEC] hover:bg-violet-500 border hover:text-white px-2`}
                    onClick={() => setIsDashboardClicked(!isDashboardClicked)}>
                    <ClipboardMinus className="w-4 h-4" />
                    Generate Report
                  </Button>
                </Popover.Trigger>
                <Popover.Content className="report-popover-content exppdf z-[9999]">
                  <div className="filter_content_container select-none bg-white card-shadow w-full h-full rounded-md flex flex-col items-center justify-start mt-2">
                    <Popover.Close asChild>
                      <div
                        className="export-pdf flex justify-center items-center gap-2 px-7 py-2 font-medium hover:bg-slate-100 cursor-pointer"
                        onClick={() => {
                          exportPDF();
                        }}>
                        <img
                          src={PDFIconImage}
                          className="w-[14px] 2xl:w-4 2xl:h-4"
                          alt="pdf"
                        />
                        <span className="text-xs font-bold hover:text-black text-slate-600">
                          Export PDF
                        </span>
                      </div>
                    </Popover.Close>
                  </div>
                </Popover.Content>
              </Popover.Root>
            </div>
          </div>
        )}

        <Separator className="col-span-12" />
        <div className="filter_label_container col-span-12 flex flex-wrap w-full sm:w-auto justify-start items-center gap-2">
          {appliedFilters?.map((filter, index) => (
            <div
              key={index}
              className="filter_label flex justify-start gap-2 items-center bg-[#F1F1FF] rounded-sm py-[3px] px-2">
              <span className="text-xs font-medium capitalize text-[#6A6AEC]">
                {filter}
              </span>
              <span>
                <X
                  className="w-3 h-3 cursor-pointer text-[#6A6AEC]"
                  onClick={() => removeFilter(filter)}
                />
              </span>
            </div>
          ))}
        </div>
      </div>
    </>
  );
};

export default EmployeeReport;
