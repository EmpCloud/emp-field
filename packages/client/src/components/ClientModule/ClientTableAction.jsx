import deleteIcon from 'assets/images/employeeTable/delete.png';
import editIcon from 'assets/images/employeeTable/edit.png';
import { Modal } from 'components/Modal';
import AddNewClientModal from 'components/UIElements/Modals/AddNewClientModal';
import AlertModal from 'components/UIElements/Modals/AlertModal';
import { useState } from 'react';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';

const ClientTableAction = ({ rowData, refetch, disableActions }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const handleEditClick = () => {
    setIsEditing(true);
    setIsModalOpen(true);
  };
  const handleCloseModal = () => {
    setIsModalOpen(false);
    setIsEditing(false);
  };
  return (
    <div className="flex justify-start items-center gap-2">
      {!disableActions && (
        <button
          className="action-icon flex items-center"
          onClick={handleEditClick}>
          <TooltipProvider delayDuration={0}>
            <Tooltip>
              <TooltipTrigger asChild>
                <img src={editIcon} alt="edit" />
              </TooltipTrigger>
              <TooltipContent>Edit</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </button>
      )}
      {isEditing && (
        <div
          className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50"
          onClick={handleCloseModal}>
          <div
            className="bg-white rounded-lg w-[600px] max-w-full"
            onClick={e => e.stopPropagation()}>
            <div className="flex justify-between items-center bg-gradient-to-r from-purple-500 to-blue-600 rounded-t-md text-white p-4">
              <h2 className="text-lg font-semibold flex-grow text-center">
                Edit Client
              </h2>
              <button
                className="bg-transparent text-sm"
                onClick={handleCloseModal}>
                X
              </button>
            </div>
            <AddNewClientModal
              rowData={rowData}
              clientId={rowData?._id}
              isEditing={true}
              refetch={refetch}
              onClose={handleCloseModal}
            />
          </div>
        </div>
      )}
      {!disableActions && (
        <button className="action-icon flex items-center">
          <Modal
            title={'Delete Employee'}
            buttonStyles={'bg-transparent p-0'}
            triggerButtonIcon={
              <TooltipProvider delayDuration={0}>
                <Tooltip>
                  <TooltipTrigger asChild>
                    <img src={deleteIcon} alt="delete" />
                  </TooltipTrigger>
                  <TooltipContent>Delete</TooltipContent>
                </Tooltip>
              </TooltipProvider>
            }>
            <AlertModal
              // alertMessage={`Are you sure want to delete Client?`}
              alertMessage={`Are you sure you want to delete ${rowData?.clientName} Client?`}
              buttonText="Delete"
              rowClient={rowData}
              refetch={refetch}
              client="client"
            />
          </Modal>
        </button>
      )}
    </div>
  );
};

export default ClientTableAction;
