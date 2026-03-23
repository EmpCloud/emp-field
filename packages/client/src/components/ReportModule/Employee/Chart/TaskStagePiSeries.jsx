import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { useContext, useEffect, useRef } from 'react';
import * as am5 from '@amcharts/amcharts5';
import * as am5percent from '@amcharts/amcharts5/percent';
import am5themes_Animated from '@amcharts/amcharts5/themes/Animated';
import { useQuery } from '@tanstack/react-query';
import { employeeReportTaskStage } from './Api/post';
import moment from 'moment';
import ChartProvider from 'components/ChartContext/Provider';
import ChartContext from 'components/ChartContext/Context';
import html2canvas from 'html2canvas';

const TaskStagePiSeries = ({ employeeId, filters, filter }) => {
  const chartTaskStageRef = useRef(null);

  // const { startDate, endDate, employeeId } = filters;

  const response = useQuery({
    queryKey: [
      'employeeReportTaskStages',
      filter?.dateRange?.startDate ||
        moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
      filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
      employeeId,
    ],
    queryFn: () =>
      employeeReportTaskStage(
        filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
        filter?.dateRange?.startDate ||
          moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
        employeeId
      ),
  });

  const TaskStage = response?.data?.data?.body?.data?.tags;
  const tottalStageCount = response?.data?.data?.body?.data?.taskCount;
  const {
    taskStageImg,
    settaskStageImg,
    settaskStageChartData,
    taskStageChartData,
  } = useContext(ChartContext);
  useEffect(() => {
    if (chartTaskStageRef.current) {
      // Create root element
      let root = am5.Root.new(chartTaskStageRef.current);
      root._logo.dispose();

      // Set themes
      root.setThemes([am5themes_Animated.new(root)]);

      // Create chart
      let chart = root.container.children.push(
        am5percent.PieChart.new(root, {
          layout: root.horizontalLayout,
          width: am5.percent(110),
          height: am5.percent(110),
          innerRadius: am5.percent(75),
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
          radius: am5.percent(70),

          tooltip: am5.Tooltip.new(root, {
            pointerOrientation: 'horizontal',
            labelText:
              '[fontFamily: "QuickSand" fontSize: "12px"]{category}: {value} Tasks',
          }),
        })
      );
      series.labels.template.set('forceHidden', true);

      const chartData =
        TaskStage &&
        TaskStage !== undefined &&
        TaskStage?.map(task => ({
          value: task?.count,
          category: task?.tagName,
          color: task?.tagDetails?.color,
        }));

      series.data.setAll(chartData || []);

      // Define colors for different categories
      const colors = {
        Pending: am5.color('#FF5733'), // Custom color for Pending
        Deal_Closed: am5.color('#32CD32'), // Custom color for Deal Closed
        // Add more categories and colors if needed
      };

      // Set colors for slices based on category
      series.slices.each((slice, index) => {
        const dataItem = series.dataItems[index]; // Get data item
        if (dataItem) {
          const category = dataItem.get('category'); // Get category
          const color = dataItem?.dataContext?.color;

          if (color) {
            slice.set('fill', am5.color(color)); // Set the fill color
          } else {
            // Optional: Set a default color if none found
            slice.set('fill', am5.color('#6794DC')); // Default color
          }
        }
      });

      // Play initial series animation
      series.appear(1000, 100);

      let legendContainer = chart.children.push(
        am5.Container.new(root, {
          width: am5.percent(65),
          height: am5.percent(80), // Adjust height to fit your requirements
          layout: root.verticalLayout,
          background: am5.Rectangle.new(root, {
            fill: am5.color(0xf3f3f3), // Optional: Add a background color to the legend container
            fillOpacity: 0.1,
          }),
        })
      );

      let legend = legendContainer.children.push(
        am5.Legend.new(root, {
          layout: root.verticalLayout,
          height: am5.percent(50), // Make legend fit the container
          verticalScrollbar: am5.Scrollbar.new(root, {
            orientation: 'vertical',
          }),
        })
      );

      // Set legend data
      legend.data.setAll(series.dataItems);

      // Style legend labels (if needed)
      legend.labels.template.setAll({
        fontSize: '10px',
        fontWeight: '600',
        fontFamily: 'QuickSand',
      });

      // Hide value labels in legend
      legend.valueLabels.template.set('forceHidden', true);

      legend.data.setAll(series.dataItems);

      legend.labels.template.setAll({
        fill: am5.color('#000'),
        fontSize: '10px',
        fontWeight: '600',
        fontFamily: 'QuickSand',
      });

      legend.valueLabels.template.set('forceHidden', true);

      legend.markerRectangles.template.setAll({
        cornerRadiusTL: 2,
        cornerRadiusTR: 2,
        cornerRadiusBL: 2,
        cornerRadiusBR: 2,
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

      // Play initial series animation
      series.appear(1000, 100);

      return () => {
        root.dispose();
      };
    }
  }, [tottalStageCount]);
  const handleexportImgChartTask = async () => {
    const chartelmStage = chartTaskStageRef.current;

    const canvasStage = await html2canvas(chartelmStage);
    const imgdataStage = canvasStage.toDataURL('image/png');
    settaskStageImg(imgdataStage);
  };

  useEffect(() => {
    if (chartTaskStageRef.current) {
      setTimeout(() => {
        handleexportImgChartTask();
      }, 1600); // Add a small delay (e.g., 500ms)
    }
  }, [chartTaskStageRef.current, TaskStage]);

  useEffect(() => {
    settaskStageChartData(tottalStageCount);
  }, [tottalStageCount]);
  return (
    <>
      <Card
        className="flex col-span-12 md:col-span-6 bg-white rounded-lg w-full shadow-none border-none"
        style={{
          display: 'flex',
          flexDirection: 'column',
          width: '100%',
        }}>
        <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 2xl:h-12 h-10 w-full">
          <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
            Tasks Stage
          </CardTitle>
        </CardHeader>
        <CardContent className="p-3 pl-0 h-[260px] w-full flex justify-center   flex-col items-center relative">
          <div
            id="chartdiv"
            ref={chartTaskStageRef}
            className="h-[calc(100%-10px)] w-full absolute"></div>
          {/* inset-0 flex right-[38%] sm:right-[37%] md:right-[30%] xl:right-[30%] 2xl:right-[30%] */}
          <div className="relative xl:mr-[7vw] md:mr-[14vw] sm:mr-[28vw] mr-[30vw] text-center flex-col justify-center items-center">
            {/* <h2 className=" text-6xl font-semibold font-montserat dark:text-white text-[#62329F]">{(userMatrics?.TodaysPoints ?? 0) || (userMatrics?.OverAllPoints ?? 0)}</h2> */}
            <h2 className="2xl:text-6xl text-4xl font-semibold font-montserat dark:text-white text-[#1F3A78]">
              {/* 30 */}
              {tottalStageCount ?? 0}
            </h2>
            <p className="text-[#1F3A78] font-bold text-[11px] uppercase">
              Total Stages
            </p>
          </div>
        </CardContent>
      </Card>
    </>
  );
};

export default TaskStagePiSeries;
