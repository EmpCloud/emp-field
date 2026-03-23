import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useEffect, useState, useRef } from 'react';
import * as am5 from '@amcharts/amcharts5';
import * as am5percent from '@amcharts/amcharts5/percent';
import am5themes_Animated from '@amcharts/amcharts5/themes/Animated';
import { fetchAllAdminTaskCount } from 'components/DashboardModule/Api/get';
import { useQuery } from '@tanstack/react-query';
import { employeeReportTaskStatus } from './Api/post';
import moment from 'moment';
import html2canvas from 'html2canvas';
import { useContext } from 'react';
import ChartContext from 'components/ChartContext/Context';

const TaskStatusPiSeries = ({ employeeId, filter /*,chartexportfunc*/ }) => {
  const chartTaskStatusRef = useRef(null);
  const [baseimg, setbaseimg] = useState('');
  const {
    taskStatusImg,
    settaskStatusImg,
    taskStatusChartData,
    settaskStatusChartData,
  } = useContext(ChartContext);
  const [bigScreen, setbigScreen] = useState('');
  const response = useQuery({
    queryKey: [
      'employeeReportTask',
      employeeId,
      filter?.dateRange?.startDate ||
        moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
      filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
    ],
    queryFn: () =>
      employeeReportTaskStatus(
        filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
        filter?.dateRange?.startDate ||
          moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
        employeeId
      ),
  });

  const TaskStatus = response?.data?.data?.body?.data;
  const taskStageDependencyarray = TaskStatus?.taskStats?.completedTask;
  const [fwidth, setfwidth] = useState(window.innerWidth);
  const handleWdSize = () => {
    setfwidth(window.innerWidth); // Update state with the new width
  };
  useEffect(() => {
    // Add resize event listener
    window.addEventListener('resize', handleWdSize);

    // Cleanup function to remove the event listener
    return () => {
      window.removeEventListener('resize', handleWdSize);
    };
  }, []);
  useEffect(() => {
    if (chartTaskStatusRef.current) {
      // Create root element
      let root = am5.Root.new(chartTaskStatusRef.current);
      root._logo.dispose();
      root.setThemes([am5themes_Animated.new(root)]);

      // Create chart
      let chart = root.container.children.push(
        am5percent.PieChart.new(root, {
          layout: root.verticalLayout,
          width: 260,
          height: 400,
          innerRadius:
            window.innerWidth < 768 ? am5.percent(120) : am5.percent(80),
          paddingTop: 20, // Add padding to avoid overlap
          paddingBottom: 20,
          paddingLeft: 20,
          paddingRight: 20,
        })
      );

      // Create series
      let series = chart.series.push(
        am5percent.PieSeries.new(root, {
          height: am5.p100,
          width: am5.p100,
          valueField: 'value',
          categoryField: 'category',
          alignLabels: false,
          radius: am5.percent(100),
          x: am5.percent(5),
          y: am5.percent(-11),
          tooltip: am5.Tooltip.new(root, {
            pointerOrientation: 'horizontal',
            labelText:
              '[fontFamily: "QuickSand" fontSize: "12px"]{category}: {value} Tasks',
          }),
          endAngle: 90,
          rotation: 270,
        })
      );
      series.labels.template.set('forceHidden', true);

      const chartData = [
        {
          value: TaskStatus?.taskStats?.pausedTask || 0,
          category: 'Paused',
        },
        {
          value: TaskStatus?.taskStats?.completedTask || 0,
          category: 'Completed',
        },
        {
          value: TaskStatus?.taskStats?.pendingTask || 0,
          category: 'Pending',
        },
        {
          value: TaskStatus?.taskStats?.deletedTask || 0,
          category: 'Deleted',
        },
      ];

      series.data.setAll(chartData);

      // Define gradients for different categories
      const gradients = {
        Completed: am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0x4db948) },
            { color: am5.color(0x4db948) },
          ],
          rotation: 90,
        }),
        Paused: am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0xff5960) },
            { color: am5.color(0xff5960) },
          ],
          rotation: 90,
        }),
        Pending: am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0xffb800) },
            { color: am5.color(0xffb800) },
          ],
          rotation: 90,
        }),
      };

      // Set gradients for slices based on category
      series.slices.each((slice, index) => {
        const category = chartData[index].category;
        if (gradients[category]) {
          slice.set('fillGradient', gradients[category]);
        }
      });

      series.ticks.setAll({
        fill: am5.color('#000'),
      });

      let strokeColor = am5.color(0xffffff);

      // Set stroke color for slices
      series.slices.template.setAll({
        stroke: strokeColor,
        strokeWidth: 0,
        cornerRadius: 4,
      });

      let legendItems = series.dataItems;

      let legendTop = chart.children.push(
        am5.Legend.new(root, {
          width: am5.percent(100),
          centerX:
            window.innerWidth < 468
              ? am5.percent(52)
              : window.innerWidth < 568
                ? am5.percent(42)
                : window.innerWidth < 768
                  ? am5.percent(32)
                  : am5.percent(50),
          // centerY: am5.percent(50),
          y: window.innerWidth < 768 ? am5.percent(65) : am5.percent(45),
          x:
            window.innerWidth < 468
              ? am5.percent(50)
              : Window.innerWidth < 768
                ? am5.percent(93)
                : am5.percent(53),
          marginTop: 15,
          layout: root.horizontalLayout,
          paddingLeft: window.innerWidth < 768 ? 0 : 0,
        })
      );

      if (fwidth >= 1600) {
        legendTop.data.setAll(legendItems);
        legendTop.itemContainers.template.setAll({
          minWidth: am5.percent(25), // Minimum width of 25% per item
          marginBottom: 2,
        });
      } else if (fwidth < 1280) {
        legendTop.data.setAll(legendItems);
        legendTop.itemContainers.template.setAll({
          minWidth: am5.percent(25), // Minimum width of 25% per item
          marginBottom: 2,
          // marginRight:am5.percent(25)
          paddingRight:
            window.innerWidth < 468 ? -3 : window.innerWidth < 768 ? 20 : 0,
        });
      } else {
        legendTop.data.setAll(legendItems.slice(0, 3));
        legendTop.itemContainers.template.setAll({
          minWidth: am5.percent(33), // Three items per row using 33% width
          marginBottom: 2,
        });
      }

      legendTop.labels.template.setAll({
        //  text: "{category} ({value})",
        fill: am5.color('#000'),
        fontSize: '10px',
        fontWeight: '600',
        fontFamily: 'QuickSand',
        textAlign: 'center',
      });

      // hide values
      legendTop.valueLabels.template.set('forceHidden', true);

      legendTop.markerRectangles.template.setAll({
        cornerRadiusTL: 2,
        cornerRadiusTR: 2,
        cornerRadiusBL: 2,
        cornerRadiusBR: 2,
      });
      //////
      if (fwidth < 1600 && fwidth > 1280) {
        let legendBtm = chart.children.push(
          am5.Legend.new(root, {
            width: am5.percent(100),
            centerX: am5.percent(50),
            // centerY: am5.percent(50),
            y: am5.percent(54),
            x: am5.percent(53),

            layout: root.horizontalLayout,
          })
        );

        legendBtm.data.setAll([legendItems[3]]);

        legendBtm.itemContainers.template.setAll({
          minWidth: am5.p100, // Set 100% width for the single fourth item
          marginBottom: 10,
        });
        legendBtm.labels.template.setAll({
          //  text: "{category} ({value})",
          fill: am5.color('#000'),
          fontSize: '10px',
          fontWeight: '600',
          fontFamily: 'QuickSand',
          textAlign: 'center',
        });

        // hide values
        legendBtm.valueLabels.template.set('forceHidden', true);

        legendBtm.markerRectangles.template.setAll({
          cornerRadiusTL: 2,
          cornerRadiusTR: 2,
          cornerRadiusBL: 2,
          cornerRadiusBR: 2,
        });
      }
      const handleResize = () => {
        let width = window.innerWidth;

        if (width < 768) {
          chart.set('width', am5.percent(100)); // Full width for mobile
          chart.set('height', 300); // Adjust height for mobile
        } else if (width < 1280) {
          chart.set('width', am5.percent(100)); // Adjust width for tablets
          chart.set('height', 350); // Adjust height for tablets
        } else if (width < 1600) {
          chart.set('width', am5.percent(100)); // Full width for large screens
          chart.set('height', 400); // Default height for large screens
        } else {
          chart.set('width', am5.percent(100)); // Full width for large screens
          chart.set('height', 400); // Default height for large screens
        }
      };
      // Initial call to set chart size
      handleResize();

      // Add event listener for window resizing
      window.addEventListener('resize', handleResize);
      // Play initial series animation
      series.appear(1000, 100);

      return () => {
        root.dispose();
        window.removeEventListener('resize', handleResize); // Cleanup on unmount
      };
    }
  }, [taskStageDependencyarray, fwidth]);
  const handleexportImgChartTaskStatus = async () => {
    const chartelmStatus = chartTaskStatusRef.current;

    const canvasStatus = await html2canvas(chartelmStatus);
    const imgdataStatus = canvasStatus.toDataURL('image/png');
    settaskStatusImg(imgdataStatus);
  };

  useEffect(() => {
    if (chartTaskStatusRef.current) {
      setTimeout(() => {
        handleexportImgChartTaskStatus();
      }, 1800); // Add a small delay (e.g., 500ms)
    }
    // chartexportfunc(handleexportImgChartTaskStatus());
  }, [chartTaskStatusRef.current, TaskStatus]);

  useEffect(() => {
    settaskStatusChartData(TaskStatus);
  }, [TaskStatus]);
  return (
    <Card
      className="flex col-span-12 md:col-span-6 bg-white rounded-lg w-full  shadow-none border-none"
      style={{
        display: 'flex',
        flexDirection: 'column',
        width: '100%',
      }}>
      <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 2xl:h-12 h-10 w-full">
        <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
          Tasks Status
        </CardTitle>
      </CardHeader>
      <CardContent className="p-3 pl-0 h-[260px] flex xl:justify-center justify-start  flex-col items-center relative">
        <div
          id="chartdiv1"
          ref={chartTaskStatusRef}
          className="h-full w-full absolute ">
          {/* sm:left-[9rem] sm:top-[40%] top-[12vw] left-[48vw] xl:left-[52%] xl:top-[37%] -translate-x-1/2 -translate-y-1/4 lg:-translate-y-[0%] */}
        </div>
        <div className="relative sm:mt-[60px] sm:ml-[42px] mt-[42px] ml-[30px] xl:ml-0 xl:mt-0 flex text-center flex-col justify-center items-center">
          {/* <h2 className=" text-6xl font-semibold font-montserat dark:text-white text-[#62329F]">{(userMatrics?.TodaysPoints ?? 0) || (userMatrics?.OverAllPoints ?? 0)}</h2> */}
          <h2 className="2xl:text-6xl text-5xl font-semibold font-montserat dark:text-white text-[#1F3A78]">
            {TaskStatus?.taskStats?.completedTask ?? 0}
          </h2>
          <p className="text-[#1F3A78] font-bold text-xs relative top-2 whitespace-nowrap uppercase">
            Out of {TaskStatus?.overAllTasks} completed
          </p>
        </div>
      </CardContent>
    </Card>
  );
};

export default TaskStatusPiSeries;
