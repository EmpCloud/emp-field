import deleteIcon from 'assets/images/reportTable/red-delete.png';
import eyeIcon from 'assets/images/employeeTable/eye.png';
import { Modal } from 'components/Modal';
import AlertModal from 'components/UIElements/Modals/AlertModal';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';
import { useNavigate } from 'react-router-dom';

const ReportTableAction = ({ handleOpenEmployeeReport, row }) => {
  const navigate = useNavigate();

  return (
    <div className="flex justify-start items-center gap-2">
      <button className="action-icon">
        <TooltipProvider delayDuration={0}>
          <Tooltip>
            <TooltipTrigger asChild>
              <img
                onClick={() =>
                  navigate('/admin/employee-report?empId=' + row?.emp_id, {
                    state: row,
                  })
                }
                src={eyeIcon}
                alt="eye"
              />
            </TooltipTrigger>
            <TooltipContent>View {row?.fullName ?? ''}</TooltipContent>
          </Tooltip>
        </TooltipProvider>
      </button>
      {/* <button className="action-icon">
        <Modal
          title={'Delete Employee'}
          buttonStyles={'bg-transparent p-0'}
          triggerIcon={<img src={deleteIcon} alt="delete" />}>
          <AlertModal
            alertMessage={`Are you sure want to delete employee?`}
            buttonText="Delete"
          />
        </Modal>
      </button> */}
    </div>
  );
};

export default ReportTableAction;
