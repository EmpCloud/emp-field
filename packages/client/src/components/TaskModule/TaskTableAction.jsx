import deleteIcon from 'assets/images/employeeTable/delete.png';
import editIcon from 'assets/images/employeeTable/edit.png';
import { Modal } from 'components/Modal';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip';
// import AddNewClientModal from 'components/UIElements/Modals/AddNewClientModal';
import AddNewTaskModal from 'components/UIElements/Modals/AddNewTaskModal';
import AlertModal from 'components/UIElements/Modals/AlertModal';
import { useState } from 'react';
// const TaskTableAction = ({ row, taskDetail , rowData}) => {
//   return (

//     <div className="flex justify-start items-center gap-2">

// rowData ?
// <button className="action-icon" onClick={handleEditClick}>
//         <img src={editIcon} alt="edit" />
//       </button>
//       :

//       <Modal
//         title={'EditTask'}
//         triggerButtonIcon={
//           <img
//             className="w-4 h-4 2xl:w-5 2xl:h-5"
//             src={editIcon}
//             alt="Add Task"
//           />
//         }>
//         <AddNewTaskModal row={row} />
// </Modal>

const TaskTableAction = ({ row, taskDetail, responseData }) => {
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
      {/* {rowData ? (
        <button className="action-icon" onClick={handleEditClick}>
          <img src={editIcon} alt="edit" />
        </button>
      ) : ( */}
      <Modal
        title={'Edit Task'}
        titleStyles={'text-center'}
        buttonStyles={'bg-transparent p-0 '}
        triggerButtonIcon={
          <TooltipProvider delayDuration={0}>
            <Tooltip>
              <TooltipTrigger asChild>
                <img
                  className="w-4 h-4 2xl:w-5 2xl:h-5"
                  src={editIcon}
                  alt="edit Task"
                />
              </TooltipTrigger>
              <TooltipContent>Edit</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        }>
        <AddNewTaskModal row={row} responseData={responseData} />
      </Modal>
      {/* )} */}

      <button className="action-icon">
        <Modal
          title={'Delete Employee'}
          buttonStyles={'bg-transparent p-0'}
          triggerIcon={
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
            task={'task'}
            row={row}
            alertMessage={`Are you sure want to delete ${row?.task} task?`}
            // alertMessage={`Are you sure you want to delete the employee?`}
            buttonText="Delete"
            taskRetch={responseData.refetch}
          />
        </Modal>
      </button>
      {/* <AddNewClientModal
        isEditing={isEditing}
        clientData={rowData}
        onClose={handleCloseModal}
      /> */}
    </div>
  );
};
export default TaskTableAction;
