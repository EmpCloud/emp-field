import * as am5 from '@amcharts/amcharts5';
import * as am5xy from '@amcharts/amcharts5/xy';
import am5themes_Animated from '@amcharts/amcharts5/themes/Animated';
import { useLayoutEffect, useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { fetchAverageWorkingHours } from '../Api/get';
import { useQuery } from '@tanstack/react-query';
import moment from 'moment';

export default function BarChart() {
  function formatDate(dateString) {
    const options = { day: 'numeric', month: 'short' };
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', options);
  }

  const currentDate = new Date();

  const [duration, setDuration] = useState(
    `startDate=${moment().subtract(6, 'days').format('YYYY-MM-DD')}&endDate=${moment(new Date()).format('YYYY-MM-DD')}`
  );
  const [graphData, setGraphData] = useState([
    { country: '5 Jun', value: 140 },
    { country: '6 Jun', value: 180 },
    { country: '7 Jun', value: 150 },
    { country: '8 Jun', value: 230 },
    { country: '9 Jun', value: 250 },
    { country: '10 Jun', value: 100 },
    { country: '11 Jun', value: 80 },
  ]);

  const { isLoading, error, data } = useQuery({
    queryKey: ['fetchAllUsersWorkingHours', duration],
    queryFn: () => fetchAverageWorkingHours(duration),
  });

  useEffect(() => {
    if (data?.data?.body?.data?.averageWorkingHours?.length) {
      const newTransformedData = data.data.body.data.averageWorkingHours.map(
        item => {
          return {
            country: formatDate(item.date),
            value: parseFloat(item.avgOfAllEmployeesWorkingHours.toFixed(2)), // Ensure value is a number
          };
        }
      );
      setGraphData(newTransformedData);
    }
  }, [data]);

  const handleWeeklyClick = () => {
    const currentDate = new Date();
    const endDate = currentDate.toISOString().split('T')[0];
    const startDate = new Date(currentDate.setDate(currentDate.getDate() - 6))
      .toISOString()
      .split('T')[0];
    const data = `startDate=${startDate}&endDate=${endDate}`;
    setDuration(data);
  };

  const handleMonthlyClick = () => {
    const currentDate = new Date();
    const endDate = currentDate.toISOString().split('T')[0];
    const startDate = new Date(currentDate.setDate(currentDate.getDate() - 30))
      .toISOString()
      .split('T')[0];
    const data = `startDate=${startDate}&endDate=${endDate}`;
    setDuration(data);
  };

  useLayoutEffect(() => {
    if (!isLoading) {
      let root = am5.Root.new('lineChartdiv');
      root._logo.dispose();
      root.setThemes([am5themes_Animated.new(root)]);

      let chart = root.container.children.push(
        am5xy.XYChart.new(root, {
          panX: true,
          panY: true,
          wheelX: 'panX',
          wheelY: 'zoomX',
          pinchZoomX: true,
          paddingLeft: 0,
          paddingRight: 1,
        })
      );

      let overallData = [
        { country: '5 Jun', value: 140 },
        { country: '6 Jun', value: 180 },
        { country: '7 Jun', value: 150 },
        { country: '8 Jun', value: 230 },
        { country: '9 Jun', value: 250 },
        { country: '10 Jun', value: 100 },
        { country: '11 Jun', value: 80 },
      ];

      let todayData = [
        { country: '5 Jun', value: 80 },
        { country: '6 Jun', value: 90 },
        { country: '7 Jun', value: 60 },
        { country: '8 Jun', value: 70 },
        { country: '9 Jun', value: 80 },
        { country: '10 Jun', value: 50 },
        { country: '11 Jun', value: 40 },
      ];

      let cursor = chart.set('cursor', am5xy.XYCursor.new(root, {}));
      cursor.lineY.set('visible', false);

      let xRenderer = am5xy.AxisRendererX.new(root, {
        minGridDistance: 30,
        minorGridEnabled: true,
      });

      xRenderer.labels.template.setAll({
        centerY: am5.p50,
        centerX: am5.p50,
        paddingRight: 15,
        fill: am5.color(0x000000),
        fontSize: '12px',
        fontFamily: 'QuickSand',
      });

      xRenderer.grid.template.setAll({
        location: 1,
        stroke: am5.color(0x00000),
        strokeOpacity: 0,
        strokeWidth: 1,
      });

      let xAxis = chart.xAxes.push(
        am5xy.CategoryAxis.new(root, {
          maxDeviation: 0.3,
          categoryField: 'country',
          renderer: xRenderer,
        })
      );

      let yRenderer = am5xy.AxisRendererY.new(root, {
        strokeOpacity: 0.1,
      });

      yRenderer.grid.template.setAll({
        location: 1,
        stroke: am5.color(0xcccccc),
        strokeOpacity: 0.5,
        strokeWidth: 1,
        strokeDasharray: [5, 5],
      });

      yRenderer.labels.template.setAll({
        fill: am5.color(0x000),
        fontSize: '12px',
        fontFamily: 'QuickSand',
      });

      const findGreatestValue = data => {
        if (data.length === 0) return null;

        return data.reduce((max, current) => {
          return current.value > max.value ? current : max;
        });
      };

      const greatestValue = findGreatestValue(graphData);

      let yAxis = chart.yAxes.push(
        am5xy.ValueAxis.new(root, {
          min: 0,
          max: greatestValue.value,
          maxDeviation: 0.3,
          renderer: yRenderer,
        })
      );

      let series = chart.series.push(
        am5xy.ColumnSeries.new(root, {
          name: 'Series 1',
          xAxis: xAxis,
          yAxis: yAxis,
          valueYField: 'value',
          sequencedInterpolation: true,
          categoryXField: 'country',
          tooltip: am5.Tooltip.new(root, {
            labelText:
              '[#fff fontSize: 12px fontFamily: QuickSand]{valueY} Hrs',
          }),
        })
      );

      series.columns.template.setAll({
        cornerRadiusTL: 5,
        cornerRadiusTR: 5,
        strokeOpacity: 0,
      });

      const colors = [
        am5.color(0xefc6fd),
        am5.color(0xa586ff),
        am5.color(0x79c7ff),
        am5.color(0xffa7a7),
        am5.color(0xffdb5a),
        am5.color(0xbde36d),
        am5.color(0xff7578),
      ];

      series.columns.template.adapters.add('fill', function (fill, target) {
        return colors[series.columns.indexOf(target) % colors.length];
      });

      series.columns.template.adapters.add('stroke', function (stroke, target) {
        return colors[series.columns.indexOf(target) % colors.length];
      });

      series.bullets.push(function () {
        return am5.Bullet.new(root, {
          locationY: 1,
          sprite: am5.Label.new(root, {
            fill: root.interfaceColors.get('alternativeText'),
            centerY: 0,
            centerX: am5.p50,
            populateText: true,
          }),
        });
      });

      // Example usage

      // { country: 'Jul 4', value: 31.5375 }

      xAxis.data.setAll(graphData);
      series.data.setAll(graphData);

      series.appear(1000);
      chart.appear(1000, 100);

      return () => {
        root.dispose();
      };
    }
  }, [graphData]);

  if (isLoading)
    return (
      <div className="card-shadow grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg animate-pulse">
        <Card>
          <CardHeader className="flex flex-row items-center bg-slate-300 rounded-t-lg px-4 2xl:h-12 h-10">
            <CardTitle className="text-xs 2xl:text-sm font-bold text-white"></CardTitle>
          </CardHeader>
          <CardContent className="p-3 h-[300px] 2xl:h-[500px] flex justify-center items-end gap-3">
            <div className="h-20 w-10 relative bg-slate-200 rounded-sm"></div>
            <div className="h-40 w-10 relative bg-slate-200 rounded-sm"></div>
            <div className="h-20 w-10 relative bg-slate-200 rounded-sm"></div>
            <div className="h-60 w-10 relative bg-slate-200 rounded-sm"></div>
            <div className="h-44 w-10 relative bg-slate-200 rounded-sm"></div>
            <div className="h-24 w-10 relative bg-slate-200 rounded-sm"></div>
            <div className="h-52 w-10 relative bg-slate-200 rounded-sm"></div>
            <div className="h-2 w-10 relative bg-slate-200 rounded-sm"></div>
            <div className="h-36 w-10 relative bg-slate-200 rounded-sm"></div>
            <div className="h-20 w-10 relative bg-slate-200 rounded-sm"></div>
          </CardContent>
        </Card>
      </div>
    );

  return (
    <>
      {graphData.length > 0 ? (
        <div className="card-shadow grid gap-4 col-span-12 md:col-span-6 bg-white rounded-lg">
          <Card>
            <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 2xl:h-12 h-10">
              <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
                Average Working Hours
              </CardTitle>
            </CardHeader>
            <CardContent className="p-3 h-[300px] 2xl:h-[500px] flex justify-center flex-col items-center">
              <Tabs defaultValue="overall">
                <TabsList>
                  {/* <TabsTrigger className="text-xs" value="overall">
                    <div onClick={handleMonthlyClick}>
                    Monthly
                    </div>
                  </TabsTrigger> */}
                  <TabsTrigger className="text-xs" value="today">
                    <div onClick={handleWeeklyClick}>Weekly</div>
                  </TabsTrigger>
                </TabsList>
              </Tabs>
              <div
                className="h-[calc(100%-40px)] w-full"
                id="lineChartdiv"></div>
            </CardContent>
          </Card>
        </div>
      ) : (
        <></>
      )}
    </>
  );
}
