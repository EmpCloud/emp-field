import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useEffect, useState, useRef } from 'react';
import * as am5 from '@amcharts/amcharts5';
import * as am5percent from '@amcharts/amcharts5/percent';
import am5themes_Animated from '@amcharts/amcharts5/themes/Animated';
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { fetchAllAdminTaskCount } from './Api/get';
import { useQuery } from '@tanstack/react-query';

const TasksDonot = () => {
  const { isLoading, error, data } = useQuery({
    queryKey: ['fetchAllAdminTask'],
    queryFn: fetchAllAdminTaskCount,
  });
  const tasksData = data?.data?.body?.data;

  const [selectedUTasks, setSelectedTasks] = useState('Todays');
  const [filteredUTasks, setFilteredTasks] = useState(null);
  const chartRef = useRef(null);

  useEffect(() => {
    if (tasksData) {
      handleOptionTasksClick(selectedUTasks);
    }
  }, [tasksData, selectedUTasks]);

  const handleOptionTasksClick = option => {
    setSelectedTasks(option);
    let filteredTasks = null;
    if (option === 'Todays') {
      filteredTasks = tasksData?.TodaysTasks;
    } else if (option === 'Overall') {
      filteredTasks = tasksData?.allTasksStats;
    }
    setFilteredTasks(filteredTasks);
  };

  useEffect(() => {
    if (filteredUTasks && chartRef.current) {
      // Create root element
      let root = am5.Root.new(chartRef.current);
      root._logo.dispose();

      // Set themes
      root.setThemes([am5themes_Animated.new(root)]);

      // Create chart
      let chart = root.container.children.push(
        am5percent.PieChart.new(root, {
          layout: root.verticalLayout,
          innerRadius: am5.percent(80),
        })
      );

      // Create series
      let series = chart.series.push(
        am5percent.PieSeries.new(root, {
          valueField: 'value',
          categoryField: 'category',
          alignLabels: false,
          radius: am5.percent(80),
          tooltip: am5.Tooltip.new(root, {
            pointerOrientation: 'horizontal',
            labelText:
              '[fontFamily: "QuickSand" fontSize: "14px"]{category}: {value} Tasks',
          }),
        })
      );
      series.labels.template.set('forceHidden', true);

      // Set data
      const chartData = [
        {
          value:
            filteredUTasks?.todaysTaskStats?.completedTask ??
            filteredUTasks?.overAllTaskStats?.overAllCompletedTask,
          category: 'Completed',
        },
        {
          value:
            filteredUTasks?.todaysTaskStats?.startedTask ??
            filteredUTasks?.overAllTaskStats?.overAllStartedTask,
          category: 'Started',
        },
        {
          value:
            filteredUTasks?.todaysTaskStats?.pendingTask ??
            filteredUTasks?.overAllTaskStats?.overAllPendingTask,
          category: 'Pending',
        },
        {
          value:
            filteredUTasks?.todaysTaskStats?.resumedTask ??
            filteredUTasks?.overAllTaskStats?.overAllResumedTask,
          category: 'Resumed',
        },
        {
          value:
            filteredUTasks?.todaysTaskStats?.pausedTask ??
            filteredUTasks?.overAllTaskStats?.overAllPausedTask,
          category: 'Paused',
        },
      ];

      series.data.setAll(chartData);

      // Define gradients for different categories
      const gradients = {
        Completed: am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0x28a745) },
            { color: am5.color(0x28a745) },
          ],
          rotation: 90,
        }),
        Started: am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0x007bff) },
            { color: am5.color(0x007bff) },
          ],
          rotation: 90,
        }),
        Pending: am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0xffc107) },
            { color: am5.color(0xffc107) },
          ],
          rotation: 90,
        }),
        Resumed: am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0x17a2b8) },
            { color: am5.color(0x17a2b8) },
          ],
          rotation: 90,
        }),
        Paused: am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0x6c757d) },
            { color: am5.color(0x6c757d) },
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

      let legend = chart.children.push(
        am5.Legend.new(root, {
          centerX: am5.percent(50),
          x: am5.percent(50),
          marginTop: 15,
        })
      );
      legend.data.setAll(series.dataItems);

      legend.labels.template.setAll({
        fill: am5.color('#000'),
        fontSize: '12px',
        fontFamily: 'QuickSand',
      });

      legend.valueLabels.template.set('forceHidden', true);

      legend.markerRectangles.template.setAll({
        cornerRadiusTL: 10,
        cornerRadiusTR: 10,
        cornerRadiusBL: 10,
        cornerRadiusBR: 10,
      });

      series.ticks.setAll({
        fill: am5.color('#000'),
      });

      let strokeColor = am5.color(0xffffff);

      // Set stroke color for slices
      series.slices.template.setAll({
        stroke: strokeColor,
        strokeWidth: 4,
        cornerRadius: 20,
      });

      // Play initial series animation
      series.appear(1000, 100);

      return () => {
        root.dispose();
      };
    }
  }, [filteredUTasks]);

  if (isLoading)
    return (
      <div className="card-shadow grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg animate-pulse">
        <Card>
          <CardHeader className="flex flex-row items-center bg-slate-300 rounded-t-lg px-4 2xl:h-12 h-10">
            <CardTitle className="text-xs 2xl:text-sm font-bold text-white"></CardTitle>
          </CardHeader>
          <CardContent className="p-3 h-[300px] 2xl:h-[500px] flex justify-center flex-col items-center gap-3">
            <div className="flex flex-row items-center justify-between w-full gap-3">
              <p className="text-sm font-bold bg-slate-200 w-40 h-8 rounded-sm"></p>
              <div className="flex gap-1 bg-slate-200 p-1 rounded-sm">
                <div className="bg-slate-300 rounded-sm h-6 w-16"></div>
                <div className="bg-slate-300 rounded-sm h-6 w-16"></div>
              </div>
            </div>
            <div className="h-[calc(100%-60px)] w-full relative bg-slate-200 rounded-sm"></div>
          </CardContent>
        </Card>
      </div>
    );
  if (error) return <div>Error loading tasks</div>;

  return (
    <div className="card-shadow grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg">
      <Card>
        <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 2xl:h-12 h-10">
          <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
            Tasks Grid
          </CardTitle>
        </CardHeader>
        <CardContent className="p-3 h-[300px] 2xl:h-[500px] flex justify-center flex-col items-center">
          <div className="flex flex-row items-center justify-between w-full gap-3">
            <p className="text-sm font-bold">
              Detailed Analytics of tasks of Employee
            </p>
            <Tabs defaultValue="Todays">
              <TabsList>
                <TabsTrigger
                  className="text-xs"
                  value="Todays"
                  onClick={() => handleOptionTasksClick('Todays')}>
                  Todays
                </TabsTrigger>
                <TabsTrigger
                  className="text-xs"
                  value="Overall"
                  onClick={() => handleOptionTasksClick('Overall')}>
                  Overall
                </TabsTrigger>
              </TabsList>
            </Tabs>
          </div>
          <div
            id="chartdiv"
            ref={chartRef}
            className="h-[calc(100%-40px)] w-full relative">
            <div className="absolute left-[50%] top-[25%] lg:top-[37%] -translate-x-1/2 -translate-y-1/4 lg:-translate-y-[35%] flex text-center flex-col justify-center items-center">
              {/* <h2 className=" text-6xl font-semibold font-montserat dark:text-white text-[#62329F]">{(userMatrics?.TodaysPoints ?? 0) || (userMatrics?.OverAllPoints ?? 0)}</h2> */}
              <h2 className="2xl:text-6xl text-4xl font-semibold font-montserat dark:text-white text-[#1F3A78]">
                {(filteredUTasks?.overAllTodaysTasks ?? 0) ||
                  (filteredUTasks?.overAllTasks ?? 0)}
              </h2>
              <p className="text-[#1F3A78] font-semibold text-sm 2xl:text-2xl">
                {filteredUTasks?.overAllTasks ? 'Overall' : 'Todays'} Tasks
              </p>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default TasksDonot;
