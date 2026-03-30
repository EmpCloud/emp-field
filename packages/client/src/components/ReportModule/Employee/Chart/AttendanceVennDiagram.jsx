import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { useContext, useEffect, useRef, useState } from 'react';
import { employeeAttendanceDetails, employeeClientDetails } from './Api/post';
import { useQuery } from '@tanstack/react-query';
import html2canvas from 'html2canvas';
import ChartContext from 'components/ChartContext/Context';

const AttendanceVennDiagram = ({ filters, attendanceGraphDeatils }) => {
  const [IsTabTriggered, setIsTabTriggered] = useState('Weekly');
  const { attendanceVenn, setattendanceVenn } = useContext(ChartContext);
  const attendanceVennDiagramRef = useRef('');
  const handleexportImgAttendanceVenn = async () => {
    const chartelmAttendance = attendanceVennDiagramRef.current;

    const canvasAttendance = await html2canvas(chartelmAttendance);
    const imgdataAttendanceVenn = canvasAttendance.toDataURL('image/png');
    setattendanceVenn(imgdataAttendanceVenn);
  };

  useEffect(() => {
    if (attendanceVennDiagramRef.current) {
      setTimeout(() => {
        handleexportImgAttendanceVenn();
      }, 1900); // Add a small delay (e.g., 500ms)
    }
  }, [attendanceVennDiagramRef.current]);

  return (
    <div className="flex relative items-center justify-center px-4 py-6 h-[240px] ">
      <div
        className="relative  sm:right-0 w-80 h-48"
        ref={attendanceVennDiagramRef}>
        {/* Large green circle */}
        <div className="absolute w-[170px] h-[170px] rounded-full bg-green-400 flex items-center flex-col justify-center text-white font-bold text-4xl">
          <div className="flex flex-col justify-center items-center">
            <div className="text-[42px] relative top-[6px] right-[17px] font-semibold text-center">
              {(attendanceGraphDeatils &&
                attendanceGraphDeatils[0]?.presentCount) ||
                0}
            </div>
            <div className="text-[14px] relative -top-[2px] right-4 font-semibold text-center">
              Present
            </div>
          </div>
        </div>

        {/* Medium purple circle */}
        <div className="absolute top-8 right-[7.5rem] w-[88px] h-[88px] rounded-full border-2 border-white bg-purple-400 flex items-center justify-center text-white font-bold text-3xl">
          <div className="flex flex-col justify-center items-center">
            <div className="text-[26px] relative top-[7px] font-semibold text-center">
              {(attendanceGraphDeatils &&
                attendanceGraphDeatils[0]?.leaveCount) ||
                0}
            </div>
            <div className="text-[12px] relative -top-[7px] font-semibold text-center">
              Leave
            </div>
          </div>
        </div>

        {/* Small orange circle */}
        <div className="absolute bottom-[1.5rem] border-2 border-white right-[9rem] w-[62px] h-[62px] rounded-full bg-orange-400 flex items-center justify-center text-white font-bold text-xl">
          <div className="flex flex-col justify-center items-center">
            <div className="text-[16px] relative top-[6px] font-semibold text-center">
              {(attendanceGraphDeatils &&
                attendanceGraphDeatils[0]?.absentCount) ||
                0}
            </div>
            <div className="text-[9px] relative -top-[6px] font-semibold text-center">
              Absent
            </div>
          </div>
        </div>

        {/* Legend */}
        <div className="absolute right-7 top-20 transform -translate-y-1/2 space-y-4">
          <div className="flex items-center">
            <div className="w-4 h-4 bg-green-400 border rounded-sm border-green-400 mr-2"></div>
            <span className="text-xs font-semibold">Present</span>
          </div>
          <div className="flex items-center">
            <div className="w-4 h-4 bg-purple-400 border rounded-sm border-purple-400 mr-2"></div>
            <span className="text-xs font-semibold">Leaves</span>
          </div>
          <div className="flex items-center">
            <div className="w-4 h-4 bg-orange-400 border rounded-sm border-orange-400 mr-2"></div>
            <span className="text-xs font-semibold">Absent</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AttendanceVennDiagram;
{
  /* <div className="sm:relative -top-40 left-44 flex abso z-50 items-center justify-end w-full gap-3 sm:gap-10 flex-col pb-4"> */
}
{
  /* <Tabs defaultValue="Weekly">
  <TabsList
    style={{
      backgroundColor: '#d2d2fa',
      borderRadius: '17px',
      padding: '0',
      height: 'fit-content',
      zIndex: '50',
    }}>
    <TabsTrigger
      onClick={() => setIsTabTriggered('Weekly')}
      className="text-xs"
      value="Weekly"
      style={{
        background: `${IsTabTriggered === 'Weekly' ? '#6A6AEC' : 'none'}`,
        color: `${IsTabTriggered === 'Weekly' ? 'white' : 'black'}`,
      }}>
      Weekly
    </TabsTrigger>
    <TabsTrigger
      className="text-xs"
      value="Monthly"
      onClick={() => setIsTabTriggered('Monthly')}
      style={{
        background: `${IsTabTriggered === 'Monthly' ? '#6A6AEC' : 'none'}`,
        color: `${IsTabTriggered === 'Monthly' ? 'white' : 'black'}`,
      }}>
      Monthly
    </TabsTrigger>
  </TabsList>
</Tabs>  */
}
//  <div className="selection_date flex flex-col justify-center gap-1">
//   <div className="text-xs font-semibold">Select Date</div>
//   <input
//     type="date"
//     placeholder="Select date"
//     className="border-2 h-[28px] placeholder:font-bold bg-[#F7F7FA] px-2 py-1 border-[#F7F7FA] rounded-md w-[150px] text-sm"
//   />
// </div>
// </div>
