import deleteIcon from 'assets/images/employeeTable/delete.png';
import editIcon from 'assets/images/employeeTable/edit.png';
import upArrow from 'assets/images/employeeTable/up.png';
import downArrow from 'assets/images/employeeTable/down.png';
import { Modal } from 'components/Modal';
// import AddNewClientModal from 'components/UIElements/Modals/AddNewClientModal';
import AlertModal from 'components/UIElements/Modals/AlertModal';
import { useState } from 'react';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';
import AddNewStageTaskModal from 'components/UIElements/Modals/AddNewStageTasksModal';

const StageTaskAction = ({
  filteredData,
  rowData,
  refetch,
  disableActions,
}) => {
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
      <div className="flex flex-col items-center">
        {filteredData?.length == rowData.order
          ? ''
          : !disableActions && (
              <button
                className="action-icon flex items-center"
                onClick={handleEditClick}>
                <Modal
                  titleStyles={'text-center'}
                  title={'Update Stage Order'}
                  buttonStyles={'bg-transparent p-0'}
                  triggerButtonIcon={
                    <TooltipProvider delayDuration={0}>
                      <Tooltip>
                        <TooltipTrigger asChild>
                          <div className="flex flex-col items-center">
                            <img src={upArrow} alt="up" />
                          </div>
                        </TooltipTrigger>
                        <TooltipContent>Increase Stage Order</TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                  }>
                  <AlertModal
                    alertMessage={`Are you sure want to update ${rowData?.tagName} Stage Order ?`}
                    buttonText="Update"
                    rowData={rowData}
                    isEditing={true}
                    refetch={refetch}
                    stage="stageUp"
                  />
                </Modal>
              </button>
            )}
        {rowData.order == 1
          ? ''
          : !disableActions && (
              <button
                className="action-icon flex items-center"
                onClick={handleEditClick}>
                <Modal
                  titleStyles={'text-center'}
                  title={'Update Stage Order'}
                  buttonStyles={'bg-transparent p-0'}
                  triggerButtonIcon={
                    <TooltipProvider delayDuration={0}>
                      <Tooltip>
                        <TooltipTrigger asChild>
                          <div className="flex flex-col items-center">
                            <img src={downArrow} alt="down" />
                          </div>
                        </TooltipTrigger>
                        <TooltipContent>Decrease Stage Order</TooltipContent>
                      </Tooltip>
                    </TooltipProvider>
                  }>
                  <AlertModal
                    alertMessage={`Are you sure want to update ${rowData?.tagName} Stage Order ?`}
                    buttonText="Update"
                    rowData={rowData}
                    isEditing={true}
                    refetch={refetch}
                    stage="stageDown"
                  />
                </Modal>
              </button>
            )}
      </div>

      {!disableActions && (
        <button
          className="action-icon flex items-center"
          onClick={handleEditClick}>
          <Modal
            titleStyles={'text-center'}
            title={'Edit Stage Details'}
            buttonStyles={'bg-transparent p-0'}
            triggerButtonIcon={
              <TooltipProvider delayDuration={0}>
                <Tooltip>
                  <TooltipTrigger asChild>
                    <img src={editIcon} alt="edit" />
                  </TooltipTrigger>
                  <TooltipContent>Edit</TooltipContent>
                </Tooltip>
              </TooltipProvider>
            }>
            {/* <AddNewClientModal */}
            <AddNewStageTaskModal
              rowData={rowData}
              tagId={rowData?._id}
              isEditing={true}
              refetch={refetch}
            />
          </Modal>
        </button>
      )}
      {!disableActions && (
        <button
          className="action-icon flex items-center"
          onClick={handleEditClick}>
          <Modal
            titleStyles={'text-center'}
            title={'Delete Stage'}
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
              alertMessage={`Are you sure want to delete ${rowData?.tagName} Stage ?`}
              buttonText="Delete"
              rowData={rowData}
              isEditing={true}
              refetch={refetch}
              stage="stage"
            />
          </Modal>
        </button>
      )}
    </div>
  );
};

export default StageTaskAction;
