import React, { useContext, useEffect, useRef } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import * as am5 from '@amcharts/amcharts5';
import * as am5percent from '@amcharts/amcharts5/percent';
import am5themes_Animated from '@amcharts/amcharts5/themes/Animated';
import html2canvas from 'html2canvas';
import { useQuery } from '@tanstack/react-query';
import { MoveDiagonal } from 'lucide-react';
import DashboardPopoverClients from '../Popover/DashboardPopoverClients';
import ChartContext from 'components/ChartContext/Context';
const ClientChartPiSeries = ({
  filters,
  clientDetails,
  exportPDFclientReport,
}) => {
  const chartRef = useRef(null);
  const {
    employeeClientChart,
    setemployeeClientChart,
    employeeClientChartData,
    setemployeeClientChartData,
  } = useContext(ChartContext);

  let clientDependencyarray = clientDetails?.totalCount;
  useEffect(() => {
    if (clientDependencyarray) {
      // Create root element
      let root = am5.Root.new(chartRef.current);
      root._logo.dispose();

      // Set themes
      root.setThemes([am5themes_Animated.new(root)]);

      // Create chart
      let chart = root.container.children.push(
        am5percent.PieChart.new(root, {
          layout: root.horizontalLayout,
          height: 300,
          innerRadius: am5.percent(70),
        })
      );

      // Create series
      let series = chart.series.push(
        am5percent.PieSeries.new(root, {
          // x: am5.percent(0),
          // y: am5.percent(0),
          centerX: am5.percent(0),
          centerY: am5.percent(0),
          valueField: 'value',
          categoryField: 'category',
          alignLabels: false,
          radius: am5.percent(100),
          tooltip: am5.Tooltip.new(root, {
            pointerOrientation: 'horizontal',
            labelText:
              '[fontFamily: "QuickSand" fontSize: "12px"] {value} Tasks are {category} to Clients  ',
          }),
          endAngle: 90,
          rotation: 270,
        })
      );
      series.labels.template.set('forceHidden', true);

      // Set data
      const chartData = [
        {
          value: clientDetails?.clientCounts?.contacted || 0,
          category: 'Connected',
        },
        {
          value: clientDetails?.clientCounts?.notContacted || 0,
          category: 'Not Connected',
        },
      ];

      series.data.setAll(chartData);

      // Define gradients for different categories
      const gradients = {
        Connected: am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0xa586ff) },
            { color: am5.color(0xa586ff) },
          ],
          rotation: 90,
        }),
        'Not Connected': am5.LinearGradient.new(root, {
          stops: [
            { color: am5.color(0xefc6fd) },
            { color: am5.color(0xefc6fd) },
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
          x: am5.percent(70),
          y: am5.percent(25),
          layout: root.verticalLayout,
          marginTop: 20,
          marginBottom: 20,
          // centerX: am5.percent(50),
          centerY: am5.percent(0),
        })
      );
      legend.data.setAll(series.dataItems);

      legend.labels.template.setAll({
        fill: am5.color('#000'),
        fontSize: '11px',
        fontWeight: '600',
        fontFamily: 'QuickSand',
      });

      legend.valueLabels.template.set('forceHidden', true);

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
  }, [clientDependencyarray]);

  const handleexportImgChartTask = async () => {
    const chartelmStage = chartRef.current;

    const canvasStage = await html2canvas(chartelmStage);
    const imgdataStage = canvasStage.toDataURL('image/png');
    setemployeeClientChart(imgdataStage);
  };

  useEffect(() => {
    if (chartRef.current) {
      setTimeout(() => {
        handleexportImgChartTask();
      }, 1500); // Add a small delay (e.g., 500ms)
    }
  }, [clientDependencyarray, chartRef.current]);

  useEffect(() => {
    if (clientDetails) {
      setemployeeClientChartData(clientDetails);
    }
  }, [clientDetails]);

  const client = clientDetails?.clientDetails?.map(clientDetails => ({
    clientName: clientDetails?.clientDetails?.clientName,
    phoneNumber: clientDetails?.clientDetails?.contactNumber,
    address: clientDetails?.clientDetails?.address1,
  }));
  useEffect(() => {
    console.log(
      employeeClientChart,
      'employeeClientChartemployeeClientChartemployeeClientChart'
    );
  }, [employeeClientChart]);
  return (
    <Card
      className="flex col-span-12 bg-white rounded-lg w-full shadow-none border-none "
      style={{
        display: 'flex',
        flexDirection: 'column',
        maxWidth: '100%',
        width: '100%',
      }}>
      <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 py-2 2xl:h-12 min-h-10 w-full">
        <CardTitle className="text-xs 2xl:text-sm font-bold text-white flex items-center gap-2 justify-between w-full">
          <div className="left_content flex gap-2 justify-center items-center">
            Clients-Wise Task Connected Status
          </div>
          <div className="right_content flex justify-center items-center gap-3">
            <DashboardPopoverClients
              client={client}
              exportPDFclientReport={exportPDFclientReport}
            />
            {/* <MoveDiagonal className="cursor-pointer h-6 w-6" /> */}
          </div>
        </CardTitle>
      </CardHeader>
      <CardContent className="relative pl-0 h-[200px] 2xl:h-[260px] flex justify-center flex-col items-center">
        <div
          id="chartdiv"
          ref={chartRef}
          className="h-[calc(100%-40px)] w-full absolute">
          {/* left-[39%] top-[47%] -translate-x-1/2 -translate-y-1/4 lg:-translate-y-[0%] */}
        </div>
        {/* inset-0 md:right-[15%] md:top-[15%] sm:right-[15%] sm:top-[15%] right-[15%] top-[15%] */}
        <div className="relative 2xl:mr-[6vw] xl:mr-[8vw] md:mr-[10vw] 2xl:mt-0 md:mt-[36px] sm:mt-[72px] sm:mr-[15vw] mr-[19vw] mt-[72px] flex text-center flex-col justify-center items-center">
          <h2 className="text-[40px] font-bold font-montserat dark:text-white text-[#1F3A78]">
            {clientDetails?.clientCounts?.contacted || 0}
          </h2>
          <p className="text-[#1F3A78] font-semibold text-xs relative top-0 whitespace-nowrap uppercase">
            Out of{' '}
            {clientDetails?.clientCounts?.contacted +
              clientDetails?.clientCounts?.notContacted}{' '}
            total
          </p>
        </div>
      </CardContent>
    </Card>
  );
};

export default ClientChartPiSeries;
