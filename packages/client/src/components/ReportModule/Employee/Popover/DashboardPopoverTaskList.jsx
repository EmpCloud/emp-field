import * as Popover from '@radix-ui/react-popover';
// import CSVIconImage from "../../../../assets/images/reportTable/csv.png"
import CSVIconImage from '../../../../assets/images/reportTable/csv.png';
import PDFIconImage from '../../../../assets/images/reportTable/pdf.png';
import XLSIconImage from '../../../../assets/images/reportTable/xls.png';
import ReportDashboardIcon from '../../../../assets/images/reportTable/carbon_report.png';
import {
  exportCSV,
  exportXLS,
} from 'components/ReportModule/consolidatedReportshelpers';
import { useContext, useEffect } from 'react';
import ChartContext from 'components/ChartContext/Context';
// import { exportPDF } from 'components/ReportModule/ConsolidatedEmployeeReporthelpers/allReport';

const DashboardPopoverTaskList = ({ taskDetails, exportPDFTaskList }) => {
  const headers = [
    [
      'Date',
      'Status',
      // 'Task Volume',
      'Check In',
      'Check Out',
      'Request Status',
      'Overridden By',
      'Total',
      // 'Role',
      // 'Department',
      // 'Location',
      // 'Created At',
    ],
  ];

  const xlsvHeaders = [
    'Date',
    'Status',
    // 'Task Volume',
    'Check In',
    'Check Out',
    'Request Status',
    'Overridden By',
    'Total',
  ];
  const { employeeTotalTaskListNum, setemployeeTotalTaskListNum } =
    useContext(ChartContext);
  useEffect(() => {
    if (taskDetails) {
      setemployeeTotalTaskListNum(taskDetails);
    }
  }, [taskDetails]);

  return (
    <Popover.Root>
      <Popover.Trigger asChild>
        <div className="dashboard_design flex gap-1 px-2 py-1 cursor-pointer bg-white border-2 rounded-sm border-white">
          <img src={ReportDashboardIcon} className="h-4 w-4" alt="" />
          <span className="text-xs  text-[#6A6AEC] select-none">
            Generate Report
          </span>
        </div>
      </Popover.Trigger>
      <Popover.Content className="report-popover-content z-[9999]">
        <div className="filter_content_container select-none  bg-white card-shadow w-full h-full rounded-sm flex flex-col items-center justify-start mt-1 ">
          <Popover.Close asChild>
            <div
              className="export-pdf flex justify-center items-center rounded-sm gap-2 px-3 py-2 font-medium hover:bg-slate-100 cursor-pointer"
              onClick={() => {
                exportPDFTaskList();
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
          {/* <Popover.Close asChild>
            <div
              className="export-csv flex justify-center items-center rounded-sm gap-2 px-3 py-2 font-medium hover:bg-slate-100 cursor-pointer"
              onClick={() => {
                exportCSV(clientsDetail, headers, 'Attendance Report');
              }}>
              <img
                src={CSVIconImage}
                className="w-[14px] 2xl:w-4 2xl:h-4"
                alt="pdf"
              />
              <span className="text-xs font-bold hover:text-black text-slate-600">
                Export CSV
              </span>
            </div>
          </Popover.Close>
          <Popover.Close asChild>
            <div
              className="export-csv flex justify-center items-center rounded-sm gap-2 px-3 py-2 font-medium hover:bg-slate-100 cursor-pointer"
              onClick={() => {
                exportXLS(clientsDetail, xlsvHeaders, 'Attendance Report');
              }}>
              <img
                src={XLSIconImage}
                className="w-[14px] 2xl:w-4 2xl:h-4"
                alt="pdf"
              />
              <span className="text-xs font-bold hover:text-black text-slate-600">
                Export XLS
              </span>
            </div>
          </Popover.Close> */}
        </div>
      </Popover.Content>
    </Popover.Root>
  );
};

export default DashboardPopoverTaskList;
