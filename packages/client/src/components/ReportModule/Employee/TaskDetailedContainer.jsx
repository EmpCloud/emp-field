import ClientSectionChart from './Chart/ClientSectionChart';
import TaskStagePiSeries from './Chart/TaskStagePiSeries';
import TaskStatusPiSeries from './Chart/TaskStatusPiSeries';
import whiteBike from '../../../assets/images/reportTable/bike_white.png';
import whiteCar from '../../../assets/images/reportTable/car-white.png';
import whiteBus from '../../../assets/images/bus.png';
import whiteTrain from '../../../assets/images/Train.png';
import { employeeReportStats } from './Chart/Api/post';
import { useQuery } from '@tanstack/react-query';
import moment from 'moment';
import { useContext, useEffect, useRef } from 'react';
import ChartContext from 'components/ChartContext/Context';
import { exportPDF } from '../ConsolidatedEmployeeReporthelpers/allReport';
import html2canvas from 'html2canvas';
// import { exportPDFclientReport } from '../../../components/ReportModule/ConsolidatedEmployeeReporthelpers/clientReport';

const TaskDetailedContainer = ({ empolyeeDeatils, employeeId, filter }) => {
  const {
    employeeStatsData,
    setemployeeStatsData,
    profileImg,
    modeBike,
    setmodeBike,
    blueBike,
    setprofileImg,
    setblueBike,
    profileUrlImg,
    setprofileUrlImg,
  } = useContext(ChartContext);

  let bike = '';
  const bikeRef = useRef();
  const response = useQuery({
    queryKey: [
      'employeeReportStats',
      employeeId,
      filter?.dateRange?.startDate ||
        moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
      filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
      filter,
    ],
    queryFn: () =>
      employeeReportStats(
        employeeId,
        filter?.dateRange?.startDate ||
          moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
        filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
        filter
      ),
  });

  const employeeStats = response?.data?.data?.body?.data;
  const formatTaskCount = taskCount => {
    if (taskCount >= 1000000) {
      return Math.trunc(taskCount / 1000000) + 'ml';
    } else if (taskCount >= 1000) {
      return Math.trunc(taskCount / 1000) + 'k';
    } else {
      return taskCount;
    }
  };
  useEffect(() => {
    if (employeeStats) {
      setemployeeStatsData(employeeStats);
    }
    // if (!whiteBike) {
    //   console.error("whiteBike is null or undefined");
    //   return;
    // }
    // const fetchBikeBase64 = async () => {
    //   try {
    //     const bikeBase64 = await convertBase64(whiteBike); // Wait for the base64 conversion
    //     console.log(bikeBase64,"bikeBase64")
    //     setmodeBike(bikeBase64); // Now you can set it when the conversion is done
    //   } catch (error) {
    //     console.error("Error converting bike image to base64:", error);
    //   }
    //   console.log("whiteBike is null or undefined");
    // };

    // fetchBikeBase64()
  }, [employeeStats]);
  // useEffect(()=>{
  //   if(employeeStatsData){
  //     console.log(employeeStatsData,"hii")
  //     exportPDF(employeeStatsData)
  //   }
  // },[employeeStatsData])
  // const convertBase64 = (file) => {
  //   return new Promise((resolve, reject) => {
  //     const fileReader = new FileReader();
  //     fileReader.readAsDataURL(file);

  //     fileReader.onload = () => {
  //       resolve(fileReader.result);
  //     };

  //     fileReader.onerror = (error) => {
  //       reject(error);
  //     };
  //   });
  // };
  // console.log(modeBike,"jhs")
  //   useEffect(()=>{
  //     if(bike)
  // setmodeBike(bike)
  //   },[bike])
  const convertProfileToBase64 = async imagePath => {
    try {
      const response = await fetch(imagePath); // Fetch the image as a blob
      const blob = await response.blob(); // Convert the response to a Blob
      const reader = new FileReader();

      reader.onloadend = () => {
        setprofileUrlImg(reader.result); // Set the base64 string in state
        // console.log(reader.result, 'red');
      };

      reader.readAsDataURL(blob); // Convert blob to base64
    } catch (error) {
      console.error('Error converting image to Base64:', error);
    }
  };
  const convertImageToBase64 = async imagePath => {
    try {
      const response = await fetch(imagePath); // Fetch the image as a blob
      const blob = await response.blob(); // Convert the response to a Blob
      const reader = new FileReader();

      reader.onloadend = () => {
        setmodeBike(reader.result); // Set the base64 string in state
      };

      reader.readAsDataURL(blob); // Convert blob to base64
    } catch (error) {
      console.error('Error converting image to Base64:', error);
    }
  };

  const convertImageBikeBlue = async whiteBike => {
    try {
      const response = await fetch(whiteBike);
      const blob = await response.blob();
      const reader = new FileReader();

      reader.onloadend = () => {
        const img = new Image();
        img.src = reader.result;

        img.onload = () => {
          const canvas = document.createElement('canvas');
          const context = canvas.getContext('2d');
          canvas.width = img.width;
          canvas.height = img.height;

          // Draw the image on the canvas
          context.drawImage(img, 0, 0);

          // Apply red tint by adjusting the pixel data
          const imageData = context.getImageData(
            0,
            0,
            canvas.width,
            canvas.height
          );
          const data = imageData.data;
          for (let i = 0; i < data.length; i += 4) {
            data[i] = 106; // Red channel
            data[i + 1] = 106; // Green channel
            data[i + 2] = 236; // Blue channel
          }
          context.putImageData(imageData, 0, 0);

          // Convert the canvas to a base64 string and set it in state
          const redImageBase64 = canvas.toDataURL('image/png');
          setblueBike(redImageBase64);
        };
      };

      reader.readAsDataURL(blob);
    } catch (error) {
      console.error('Error converting image to Base64:', error);
    }
  };
  useEffect(() => {
    // Call the conversion function when the component mounts
    convertImageToBase64(whiteBike);
    convertImageBikeBlue(whiteBike);
  }, []);
  useEffect(() => {
    if (profileImg) {
      convertProfileToBase64(profileImg);
    }
  }, []);

  useEffect(() => {
    const value = formatTaskCount(employeeStats?.taskValue?.toFixed(2) ?? 0);

    const integerPart = Math.trunc(value);
    // console.log(integerPart, 'integerPart');
  }, [employeeStats]);
  // const handleexportImgChartTaskStatus = async () => {
  //   const chartelmStatus = bikeRef.current;

  //   const canvasStatus = await html2canvas(chartelmStatus);
  //   const imgdataStatus = canvasStatus.toDataURL('image/png');
  //   setmodeBike(imgdataStatus);
  // };

  // useEffect(() => {
  //   if (bikeRef.current) {
  //     setTimeout(() => {
  //       handleexportImgChartTaskStatus();
  //     }, 3000); // Add a small delay (e.g., 500ms)
  //   }
  //   // chartexportfunc(handleexportImgChartTaskStatus());
  // }, [bikeRef.current]);

  return (
    <>
      <div className="sm:grid sm:gap-4 sm:col-span-12 xl:col-span-6 sm:rounded-lg flex flex-col col-span-12">
        <div className="grid grid-cols-12 col-span-12 xl:col-span-6 bg-white rounded-lg gap-2 w-full lg:h-[112px]">
          {/* for task */}
          <div className="flex items-center flex-col col-span-6 sm:col-span-4 md:col-span-3 lg:col-span-2 bg-[#78A3FF] text-white py-3 rounded-md text-center w-full h-[112px] gap-5">
            <h2 className="font-bold text-2xl">{employeeStats?.taskCounts}</h2>
            <p
              className="text-[12px] font-medium px-2 text-center"
              style={{ lineHeight: '14px' }}>
              Tasks
            </p>
          </div>
          {/* for Clients */}
          <div className="flex items-center flex-col col-span-6 sm:col-span-4 md:col-span-3 lg:col-span-2 bg-[#FFAC64] text-white py-3 rounded-md text-center w-full h-[112px] gap-5">
            <h2 className="font-bold text-2xl">
              {employeeStats?.clientCounts}
            </h2>
            <p
              className="text-[12px] font-medium px-2 text-center"
              style={{ lineHeight: '14px' }}>
              Clients
            </p>
          </div>
          {/* for Distance Travelled */}
          <div className="flex items-center flex-col col-span-6 sm:col-span-4 md:col-span-3 lg:col-span-2 bg-[#FFA5C9] text-white py-3 rounded-md text-center w-full h-[112px] gap-[11px]">
            <h2 className="font-bold text-2xl">
              {employeeStats?.distanceTraveled?.toFixed(2) || 0}
              <span className="text-[8px]">KM</span>
            </h2>
            <p
              className="text-[12px] font-medium px-2 text-center"
              style={{ lineHeight: '14px' }}>
              Distance Travelled
            </p>
          </div>
          {/* for Net Task Value */}
          <div className="flex items-center flex-col col-span-6 sm:col-span-4 md:col-span-3 lg:col-span-2 bg-[#1AB6AE] text-white py-3 rounded-md text-center w-full h-[112px] gap-[11px]">
            <h2 className="font-bold text-2xl">
              {formatTaskCount(employeeStats?.taskValue?.toFixed(2) || 0)}
              <span className="text-[8px]">USD</span>
            </h2>
            <p
              className="text-[12px] font-medium px-2 text-center"
              style={{ lineHeight: '14px' }}>
              Net Task Value
            </p>
          </div>
          {/* for net task volume */}
          <div className="flex items-center flex-col col-span-6 sm:col-span-4 md:col-span-3 lg:col-span-2 bg-[#6E67CA] text-white py-3 rounded-md text-center w-full h-[112px] gap-4">
            <h2 className="font-bold text-2xl">
              {formatTaskCount(employeeStats?.netTaskVolume?.toFixed(2) || 0)}
            </h2>
            <p
              className="text-[12px] font-medium px-2 text-center"
              style={{ lineHeight: '14px' }}>
              Net Task Volume
            </p>
          </div>
          {/* for Mode of transport */}
          <div className="flex flex-col col-span-6 sm:col-span-4 md:col-span-3 lg:col-span-2 bg-[#63D29B] text-white py-3 rounded-md w-full h-[112px] gap-2 items-center justify-center">
            {employeeStats?.modeOfTransport === 'bike' ? (
              <img
                src={whiteBike}
                className="h-9 w-9"
                alt="bike"
                ref={bikeRef}
              />
            ) : employeeStats?.modeOfTransport === 'car' ? (
              <img src={whiteCar} className="h-9 w-9" alt="car" />
            ) : employeeStats?.modeOfTransport === 'train' ? (
              <img src={whiteTrain} className="h-9 w-9" alt="train" />
            ) : employeeStats?.modeOfTransport === 'bus' ? (
              <img src={whiteBus} className="h-9 w-9" alt="bus" />
            ) : null}
            <p
              className="text-[12px] font-medium px-2 text-center"
              style={{ lineHeight: '14px' }}>
              Mode of Transport
            </p>
          </div>
        </div>
        <div className="grid grid-cols-12 col-span-12 xl:col-span-6 rounded-lg sm:gap-2 gap-5 md:h-[319px] h-full mt-5 sm:mt-0">
          <TaskStatusPiSeries
            empolyeeDeatils={empolyeeDeatils}
            employeeId={employeeId}
            filter={filter}
            // chartexportfunc={chartexportfunc}
          />
          <TaskStagePiSeries employeeId={employeeId} filter={filter} />
        </div>
        <ClientSectionChart employeeId={employeeId} filter={filter} />
      </div>
    </>
  );
};

export default TaskDetailedContainer;
