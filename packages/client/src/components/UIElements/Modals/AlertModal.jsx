import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { DialogClose } from '@/components/ui/dialog';
import {
  deleteClient,
  deleteEmployee,
  deleteEmployeePermanently,
  deleteStage,
  deleteTask,
  updateGeoFencing,
  updateStageOrder,
} from 'components/DashboardModule/Api/delete';
import { toast } from 'sonner';

const AlertModal = ({
  alertMessage,
  buttonText,
  handleAddEmployees,
  row,
  deletedUsers,
  refetchEmployess,
  task,
  rowClient,
  client,
  stage,
  refetch,
  updategeoFecn,
  rowData,
  taskRetch,
}) => {
  const handleDeleteEmployee = () => {
    deleteEmployee(row?.empCode).then(response => {
      if (response?.statusCode === 200) {
        toast.success(response?.body?.message);
        refetchEmployess();
      } else {
        toast.error(response?.body?.message);
      }
    });
  };

  const handleTaskDelete = taskId => {
    deleteTask(taskId).then(response => {
      if (response?.statusCode === 200) {
        toast.success(response?.body?.message);
        taskRetch();
      } else {
        toast.error(response?.body?.message);
      }
    });
  };

  const handleClientDelete = clientId => {
    deleteClient(clientId).then(response => {
      if (response?.statusCode === 200) {
        toast.success(response?.body?.message);
        refetch();
      } else {
        toast.error(response?.body?.message);
      }
    });
  };

  const handleStageDelete = stageId => {
    deleteStage(stageId)
      .then(response => {
        if (response?.statusCode === 200) {
          toast.success(response?.body?.message);
          refetch();
        } else {
          toast.error(`Failed to delete stage: ${response?.body?.message}`);
        }
      })
      .catch(error => {
        toast.error('An error occurred while deleting the stage.');
      });
  };

  const handleDeleteEmployeePermanently = () => {
    deleteEmployeePermanently(row?.empCode).then(response => {
      if (response?.statusCode === 200) {
        toast.success(response?.body?.message);
        refetchEmployess();
      } else {
        toast.error(response?.body?.message);
      }
    });
  };
  const handleUpdateEmployeeGeoFecning = () => {
    updateGeoFencing(
      row?.empCode,
      row?.isGeoFencingOn === 1 ? false : true
    ).then(response => {
      if (response?.data?.body?.status === 'success') {
        toast.success(response?.data?.body?.message);
        refetchEmployess();
      } else {
        toast.error(response?.data?.body?.message);
      }
    });
  };

  const handleUpdateStage = (order, id) => {
    updateStageOrder(order, id).then(response => {
      if (response?.data?.statusCode === 200) {
        toast.success(response?.data.body?.message);
        refetch();
      } else {
        toast.error(response?.body?.message);
      }
    });
  };

  return (
    <div className="grid gap-4 grid-cols-12 col-span-12 p-6 overflow-auto max-h-[300px] 2xl:max-h-[500px]">
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6"}`}>
        <CardContent className="py-4 text-center">
          {alertMessage ?? ''}
        </CardContent>
      </Card>
      <div className="col-span-6 col-start-5">
        <DialogClose asChild>
          <Button
            varient="ghost"
            className="w-32 border border-violet-500 mr-4 text-violet-500 bg-transparent">
            No
          </Button>
        </DialogClose>

        <DialogClose asChild>
          <Button
            className="bg-violet-500 hover:bg-violet-600 w-32"
            // onClick={() => {
            //   rowData
            //     ? stage == 'stageUp'
            //       ? handleUpdateStage(rowData?.order + 1, rowData?._id)
            //       : handleUpdateStage(rowData?.order - 1, rowData?._id)
            //     : updategeoFecn === true
            //       ? handleUpdateEmployeeGeoFecning()
            //       : client == 'client'
            //         ? handleClientDelete(rowClient._id)
            //         : task == 'task'
            //           ? handleTaskDelete(row.taskId)
            //           : stage === 'stage'
            //             ? handleStageDelete(rowData?._id)
            //             : buttonText
            //               ? row
            //                 ? deletedUsers
            //                   ? handleDeleteEmployeePermanently()
            //                   : handleDeleteEmployee()
            //                 : handleAddEmployees()
            //               : handleDeleteEmployee();
            // }}
            onClick={() => {
              rowData
                ? stage === 'stageUp'
                  ? handleUpdateStage(rowData.order + 1, rowData._id)
                  : stage === 'stageDown'
                    ? handleUpdateStage(rowData.order - 1, rowData._id)
                    : stage === 'stage'
                      ? handleStageDelete(rowData._id)
                      : null
                : updategeoFecn
                  ? handleUpdateEmployeeGeoFecning()
                  : client === 'client'
                    ? handleClientDelete(rowClient._id)
                    : task === 'task'
                      ? handleTaskDelete(row.taskId)
                      : buttonText
                        ? row
                          ? deletedUsers
                            ? handleDeleteEmployeePermanently()
                            : handleDeleteEmployee()
                          : handleAddEmployees()
                        : handleDeleteEmployee();
            }}>
            {buttonText ?? 'Yes'}
          </Button>
        </DialogClose>
      </div>
    </div>
  );
};

export default AlertModal;
