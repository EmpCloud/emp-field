import SettingsIcon from 'assets/images/employeeTable/setting.png';
import UpArrowWithCircleIcon from 'assets/images/employeeTable/upwcircle.png';
import DownArrowWithCircleIcon from 'assets/images/employeeTable/downwcircle.png';
import UpArrowIcon from 'assets/images/employeeTable/uparrow.png';
import UserEditIcon from 'assets/images/employeeTable/edit.png';
import EyeIcon from 'assets/images/employeeTable/eye.png';
import DeleteIcon from 'assets/images/employeeTable/delete.png';
import ion_location from 'assets/images/ion_location.png';
import GeofencOnIcon from 'assets/images/employeeTable/Geofenc-ON.png';
import GeofencOffIcon from 'assets/images/employeeTable/Geofenc-OFF.png';
import { RiUserLocationLine } from 'react-icons/ri';
import EditModal from 'components/UIElements/Modals/EditModal';
import { Modal } from 'components/Modal';
import AlertModal from 'components/UIElements/Modals/AlertModal';
import { SlLocationPin } from 'react-icons/sl';
import EmployeeLocation from 'components/UIElements/Modals/EmployeeLocation';
import { useState } from 'react';
import { restoreEmployees } from './Api/post';
import { toast } from 'sonner';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';

const TableAction = ({
  arrow = false,
  row,
  deletedUsers,
  refetchEmployess,
}) => {
  const [updategeoFecn, setUpdategeoFecn] = useState(false);
  const [openLocationModal, setOpenLocationModal] = useState(false);

  const restoreEmployee = restoreEmployeeId => {
    restoreEmployees(restoreEmployeeId).then(response => {
      if (response?.data?.statusCode == 200) {
        refetchEmployess();
        toast.success(response?.data?.body?.message);
      } else {
        toast.error(response?.data?.body?.message);
      }
    });
  };

  return (
    <div className="flex justify-start items-center gap-[11px]">
      {deletedUsers ? (
        <button
          onClick={() => {
            restoreEmployee(row.empCode);
          }}
          className="action-icon">
          <img src={UpArrowIcon} alt="uparrow" />
        </button>
      ) : (
        ''
      )}

      {/* <button className="action-icon">
        <img src={SettingsIcon} alt="setting" />
      </button>
      <button className="action-icon">
        {arrow ? (
          <img src={UpArrowWithCircleIcon} alt="circlearw" />
        ) : (
          <img src={DownArrowWithCircleIcon} alt="circlearw" />
        )}
      </button>
      <button className="action-icon">
        <img src={UpArrowIcon} alt="uparrow" />
      </button>
      <Modal
        title={'Edit Modal'}
        buttonStyles={'bg-transparent p-0'}
        triggerIcon={
          <img className="action-icon" src={UserEditIcon} alt="edit" />
        }>
        <EditModal />
      </Modal>
      <button className="action-icon">
        <img src={EyeIcon} alt="eye" />
      </button> */}

      {!deletedUsers && (
        <Modal
          open={openLocationModal}
          title={'Location'}
          buttonStyles={'bg-transparent p-0'}
          largeWidth="hello sm:max-w-lg max-w-s"
          triggerIcon={
            <TooltipProvider delayDuration={0}>
              <Tooltip>
                <TooltipTrigger asChild>
                  <img
                    className="action-icon"
                    src={ion_location}
                    onClick={() => setOpenLocationModal(true)}
                  />
                </TooltipTrigger>
                <TooltipContent>{`Location`}</TooltipContent>
              </Tooltip>
            </TooltipProvider>
          }>
          {/* <AlertModal
        
        /> */}
          <EmployeeLocation
            row={row}
            setOpenLocationModal={setOpenLocationModal}
          />
        </Modal>
      )}
      {!deletedUsers && (
        <Modal
          title={'Geo Fencing'}
          buttonStyles={'bg-transparent p-0'}
          triggerIcon={
            <TooltipProvider delayDuration={0}>
              <Tooltip>
                <TooltipTrigger asChild>
                  <img
                    className="action-icon"
                    src={
                      row?.isGeoFencingOn === 1 ? GeofencOnIcon : GeofencOffIcon
                    }
                    alt="delete"
                    onClick={() => setUpdategeoFecn(true)}
                  />
                </TooltipTrigger>
                <TooltipContent>{`Turn ${row?.isGeoFencingOn === 1 ? 'OFF' : 'ON'} Fencing`}</TooltipContent>
              </Tooltip>
            </TooltipProvider>
          }>
          <AlertModal
            updategeoFecn={updategeoFecn}
            refetchEmployess={refetchEmployess}
            row={row}
            alertMessage={`Are you sure want to turn ${row?.isGeoFencingOn === 1 ? 'OFF' : 'ON'} ${`'${row.fullName}'`} Geo fencing`}
            buttonText="Update"
          />
        </Modal>
      )}
      <Modal
        deletedUsers={deletedUsers}
        title={'Delete Employee'}
        buttonStyles={'bg-transparent p-0'}
        triggerIcon={
          <TooltipProvider delayDuration={0}>
            <Tooltip>
              <TooltipTrigger asChild>
                <img className="action-icon" src={DeleteIcon} alt="delete" />
              </TooltipTrigger>
              <TooltipContent>Delete Employee</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        }>
        <AlertModal
          deletedUsers={deletedUsers}
          refetchEmployess={refetchEmployess}
          row={row}
          alertMessage={`Are you sure want to delete ${row.fullName}  employee  ${deletedUsers ? '   ' + 'permenantly' : ''} ?`}
          buttonText="Delete"
        />
      </Modal>
    </div>
  );
};

export default TableAction;
