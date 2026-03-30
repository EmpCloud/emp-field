import React, { useContext } from 'react';
import ClientChartPiSeries from './ClientChartPiSeries';
import ClientsReportTable from '../Table/ClientsReportTable';
import { useQuery } from '@tanstack/react-query';
import { employeeClientDetails } from './Api/post';
import moment from 'moment';
import { exportPDFclientReport } from '../../ConsolidatedEmployeeReporthelpers/clientReport';
import ChartContext from 'components/ChartContext/Context';

const ClientSectionChart = ({ employeeId, filter }) => {
  // const { startDate, endDate, employeeId } = filters;

  const { data, isLoading, error } = useQuery({
    queryKey: [
      'employeeReportClient',
      filter?.dateRange?.startDate ||
        moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
      filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
      employeeId,
      filter,
    ],
    queryFn: () =>
      employeeClientDetails(
        filter?.dateRange?.endDate || moment(new Date()).format('YYYY-MM-DD'),
        filter?.dateRange?.startDate ||
          moment(new Date()).subtract(1, 'months').format('YYYY-MM-DD'),
        employeeId,
        filter
      ),
  });
  const {
    taskStatusImg,
    taskStageImg,
    attendanceTable,
    setattendanceTable,
    attendanceVenn,
    attendanceVennData,
    setattendanceVenn,
    employeeReportData,
    profileUrlImg,
    employeeStatsData,
    profileImg,
    employeeClientChart,
    employeeClientTableData,
    setemployeeClientTableData,
    employeeClientChartData,
    modeBike,
    setmodeBike,
  } = useContext(ChartContext);
  const clientDetails = data?.body?.data;

  return (
    <div className="grid grid-cols-12 col-span-12 xl:col-span-6 bg-white rounded-lg gap-2 mt-5 sm:mt-0 w-full  h-full">
      <ClientChartPiSeries
        // filters={filters}
        employeeId={employeeId}
        clientDetails={clientDetails}
        exportPDFclientReport={() =>
          exportPDFclientReport(
            employeeReportData,
            employeeStatsData,
            attendanceTable,
            attendanceVennData,
            profileImg,
            employeeClientTableData,
            employeeClientChart,
            employeeClientChartData,
            modeBike
          )
        }
      />
      <ClientsReportTable clientDetails={clientDetails} isLoading={isLoading} />
    </div>
  );
};

export default ClientSectionChart;
