import React, {
  useCallback,
  useContext,
  useEffect,
  useLayoutEffect,
  useRef,
  useState,
} from 'react';
import ReactApexChart from 'react-apexcharts';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

import ReportDashboardIcon from '../../../../assets/images/reportTable/carbon_report.png';
import { MoveDiagonal } from 'lucide-react';
import DistanceTravelledTable from '../Table/DistanceTravelledTable';
import DashboardPopoverDistTravel from '../Popover/DashboardPopoverDistTravel';
import { employeeDistanceDetails } from './Api/post';
import { useQuery } from '@tanstack/react-query';
import html2canvas from 'html2canvas';
import moment from 'moment';
import ChartContext from 'components/ChartContext/Context';

const DistanceTravelledChart = ({
  employeeId,
  filter,
  exportPDFtravelledReport,
}) => {
  const [options, setOptions] = useState({});
  const [series, setSeries] = useState([]);
  const [baseimg, setbaseimg] = useState('');
  const { employeeTravelledChart, setemployeeTravelledChart } =
    useContext(ChartContext);
  const { data, isLoading, error } = useQuery({
    queryKey: [
      'employeeReportDistance',
      employeeId,
      filter?.dateRange?.startDate ||
        moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
      filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
    ],
    queryFn: () =>
      employeeDistanceDetails(
        filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
        filter?.dateRange?.startDate ||
          moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
        employeeId
      ),
  });
  let refchart = useRef('');
  const chartsData = data?.body?.data?.newResult;
  const employeeDistanceDetail = data?.body?.data?.finalResult?.trackData;

  // useLayoutEffect(() => {
  //   console.log(data ,'chartsData')
  //   if (chartsData && Array.isArray(chartsData) && chartsData.length > 0) {
  //     const formattedData = chartsData?.map(item => ({
  //       x: item?.x,
  //       y: item?.y ,
  //     }));

  //     setSeries([
  //       {
  //         name: 'Distance Travelled (km)',
  //         data: formattedData,
  //       },
  //     ]);

  //     console.log(formattedData ,'formattedData')

  //     setOptions({
  //       chart: {
  //         width: '100%',
  //         type: 'line',
  //         zoom: {
  //           enabled: true,
  //         },
  //         toolbar: {
  //           show: false,
  //         },
  //       },
  //       colors: ['#6794dc'],
  //       legend: {
  //         position: 'top',
  //         fontFamily: 'QuickSand',
  //         labels: {
  //           colors: '#282828',
  //         },
  //       },
  //       dataLabels: {
  //         enabled: false,
  //       },
  //       stroke: {
  //         curve: 'smooth',
  //         width: 2,
  //       },
  //       xaxis: {
  //         categories: formattedData.map(item => item.x),
  //         labels: {
  //           style: {
  //             fontFamily: 'QuickSand',
  //             colors: '#282828',
  //           },
  //         },
  //         axisBorder: {
  //           show: false,
  //         },
  //         axisTicks: {
  //           show: false,
  //         },
  //       },
  //       grid: {
  //         borderColor: '#dddddd70',
  //         strokeDashArray: 0,
  //         position: 'back',
  //         xaxis: {
  //           lines: {
  //             show: false,
  //           },
  //         },
  //         yaxis: {
  //           lines: {
  //             show: true,
  //             strokeDashArray: 5,
  //           },
  //         },
  //       },
  //       yaxis: {
  //         min: 0,
  //         labels: {
  //           style: {
  //             fontFamily: 'QuickSand',
  //             colors: '#282828',
  //           },
  //           formatter: value => `${value} km`,
  //         },
  //       },
  //       tooltip: {
  //         theme: 'light',
  //         style: {
  //           fontSize: '12px',
  //           fontFamily: 'QuickSand',
  //         },
  //         y: {
  //           formatter: function (value) {
  //             return `${value} km`;
  //           },
  //         },
  //       },
  //     });
  //   }
  // }, [chartsData]);
  useLayoutEffect(() => {
    if (chartsData && Array.isArray(chartsData) && chartsData.length > 0) {
      const formattedData = chartsData?.map(item => ({
        x: item?.x,
        y: item?.y,
      }));

      // Set the series
      setSeries([
        {
          name: 'Distance Travelled (km)',
          data: formattedData,
        },
      ]);

      // Conditional chart type (scatter if only one data point)
      const chartType = formattedData.length === 1 ? 'scatter' : 'line';

      setOptions({
        chart: {
          width: '100%',
          type: chartType,
          zoom: {
            enabled: true,
          },
          toolbar: {
            show: false,
          },
        },
        colors: ['#6794dc'],
        legend: {
          position: 'top',
          fontFamily: 'QuickSand',
          labels: {
            colors: '#282828',
          },
        },
        dataLabels: {
          enabled: false,
        },
        stroke: {
          curve: 'smooth',
          width: formattedData.length === 1 ? 0 : 2, // No line if single data point
        },
        markers: {
          size: 6, // Adjust the marker size
          colors: ['#6794dc'],
          strokeColors: '#fff',
          strokeWidth: 2,
          hover: {
            size: 8,
          },
        },
        xaxis: {
          categories: formattedData.map(item => item.x),
          labels: {
            style: {
              fontFamily: 'QuickSand',
              colors: '#282828',
            },
          },
          axisBorder: {
            show: false,
          },
          axisTicks: {
            show: false,
          },
        },
        grid: {
          borderColor: '#dddddd70',
          strokeDashArray: 0,
          position: 'back',
          xaxis: {
            lines: {
              show: false,
            },
          },
          yaxis: {
            lines: {
              show: true,
              strokeDashArray: 5,
            },
          },
        },
        yaxis: {
          min: 0,
          labels: {
            style: {
              fontFamily: 'QuickSand',
              colors: '#282828',
            },
            formatter: value => `${value.toFixed(2)} km`,
          },
        },
        tooltip: {
          theme: 'light',
          style: {
            fontSize: '12px',
            fontFamily: 'QuickSand',
          },
          y: {
            formatter: function (value) {
              return `${value.toFixed(2)} km`;
            },
          },
        },
      });
    }
  }, [chartsData]);

  // const handleexportimg2 = async () => {
  //   const chartelm2 = refchart.current.chartRef.current;

  //   const canvas2 = await html2canvas(chartelm2);
  //   const imgdata2 = canvas2.toDataURL('image/png');
  //   setbaseimg(imgdata2);
  // };
  // const exportChartAsImage =()=>{
  //   const canvas = refchart.current
  //   const canvasimg= canvas.toDataURL('image/png',1.0)
  //   console.log(canvasimg)
  // }
  // const exportChartAsImage =
  // useCallback(() => {
  // const link = document.createElement("a")
  // link.download="reactapexchart.png"
  // link.href=refchart.current.toBase64Image();
  // link.click()
  // },[]);
  // useEffect=()=>{

  // }
  let distancetravelled = document.getElementById('distance-travelled-chart');
  const handleexportImgChartTravelled = async () => {
    // const chartelmTravelled = refchart.current.chartRef.current;

    const canvasStage = await html2canvas(distancetravelled);

    const imgdataStage = canvasStage.toDataURL('image/png');
    setemployeeTravelledChart(imgdataStage);
  };

  useEffect(() => {
    if (distancetravelled) {
      setTimeout(() => {
        handleexportImgChartTravelled();
      }, 2000); // Add a small delay
    }
  }, [refchart, chartsData]);

  return (
    <>
      <Card
        className="flex col-span-12 bg-white rounded-lg w-full shadow-none border-none"
        style={{
          display: 'flex',
          flexDirection: 'column',
          maxWidth: '100%',
          width: '100%',
        }}>
        <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 py-2 2xl:h-12 min-h-10 w-full">
          <CardTitle className="text-xs 2xl:text-sm font-bold text-white flex items-center gap-2 justify-between w-full">
            <div className="left_content flex gap-2 justify-center items-center">
              Distance Travelled
            </div>
            <div className="right_content flex justify-center items-center gap-3">
              {/* <div className="dashboard_design flex gap-1 px-2 py-1 bg-white border-2 rounded-sm border-white">
                <img src={ReportDashboardIcon} className="h-4 w-4" alt="" />
                <span className="text-xs  text-[#6A6AEC]">Generate Report</span>
              </div> */}
              <DashboardPopoverDistTravel
                employeeDistanceDetail={employeeDistanceDetail}
                exportPDFtravelledReport={exportPDFtravelledReport}
              />
              {/* <MoveDiagonal className="cursor-pointer h-6 w-6" /> */}
            </div>
          </CardTitle>
        </CardHeader>
        <CardContent className="flex w-full justify-center flex-col items-center ">
          <div className="line-chart-container h-[300px] w-full">
            <ReactApexChart
              options={options}
              series={series}
              type="line"
              height={290}
              style={{ width: '96%' }}
              ref={refchart}
              id="distance-travelled-chart"
            />
          </div>
          <DistanceTravelledTable
            isLoading={isLoading}
            employeeDistanceDetail={employeeDistanceDetail}
          />
        </CardContent>
      </Card>
    </>
  );
};

export default DistanceTravelledChart;
