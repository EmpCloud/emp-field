import { Button } from '@/components/ui/button';
import * as Popover from '@radix-ui/react-popover';
import { LogOut } from 'lucide-react';
import CSVIconImage from '../../../assets/images/reportTable/csv.png';
import PDFIconImage from '../../../assets/images/reportTable/pdf.png';
import XLSIconImage from '../../../assets/images/reportTable/xls.png';

const ExportButtonPopup = ({ exportPDF, exportCSV, exportXLS }) => {
  return (
    <Popover.Root>
      <Popover.Trigger asChild>
        <Button
          variant="outline"
          className="export-button flex gap-2 text-xs tracking-wide font-semibold text-[#6A6AEC] border-[#6A6AEC] hover:text-[#6A6AEC] py-3">
          <LogOut className="rotate-[-90deg] w-4 h-4 2xl:w-5 2xl:h-5" />
          Export
        </Button>
      </Popover.Trigger>
      <Popover.Content className="report-popover-content exppdfrep">
        <div className="filter_content_container select-none bg-white card-shadow w-full h-full rounded-md flex flex-col items-center justify-start py-1">
          <Popover.Close asChild>
            <div
              className="export-pdf flex justify-center items-center gap-2 px-3 py-2 font-medium hover:bg-slate-100 cursor-pointer"
              onClick={() => {
                exportPDF();
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
          <Popover.Close asChild>
            <div
              className="export-csv flex justify-center items-center gap-2 px-3 py-2 font-medium hover:bg-slate-100 cursor-pointer"
              onClick={() => {
                exportCSV();
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
              className="export-csv flex justify-center items-center gap-2 px-3 py-2 font-medium hover:bg-slate-100 cursor-pointer"
              onClick={() => {
                exportXLS();
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
          </Popover.Close>
        </div>
      </Popover.Content>
    </Popover.Root>
  );
};

export default ExportButtonPopup;
