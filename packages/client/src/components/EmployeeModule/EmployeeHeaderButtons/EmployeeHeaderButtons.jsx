import { Modal } from 'components/Modal';
import BulkDeleteModal from 'components/UIElements/Modals/BulkDeleteModal';
import BulkRegisterModal from 'components/UIElements/Modals/BulkRegisterModal';
import BulkUpdateModal from 'components/UIElements/Modals/BulkUpdateModal';
import ImportEmployee from 'components/UIElements/Modals/ImportEmployee';
import RegisterEmployeeModal from 'components/UIElements/Modals/RegisterEmployeeModal';

const EmployeeHeaderButtons = () => {
  return (
    <div className="card-shadow grid gap-4 grid-cols-5 sm:grid-cols-10 col-span-12 bg-white rounded-lg px-5 2xl:px-20 py-5">
      <Modal
        triggerText={'Import from EmpMonitor'}
        title={'Import from EmpMonitor'}
        buttonStyles={'bg-solid-violet col-span-5 md:col-span-2'}>
        <ImportEmployee />
      </Modal>
      <Modal
        disable={true}
        triggerText={'Register Employee'}
        title={'Register Employee'}
        buttonStyles={'bg-solid-violet col-span-5 md:col-span-2'}>
        <RegisterEmployeeModal />
      </Modal>
      <Modal
        disable={true}
        triggerText={'Bulk Register'}
        title={'Bulk Register'}
        buttonStyles={'bg-solid-violet col-span-5 md:col-span-2'}>
        <BulkRegisterModal />
      </Modal>
      <Modal
        disable={true}
        triggerText={'Bulk Update'}
        title={'Bulk Update'}
        buttonStyles={'bg-solid-violet col-span-5 md:col-span-2'}>
        <BulkUpdateModal />
      </Modal>
      <Modal
        disable={true}
        triggerText={'Bulk Delete'}
        title={'Bulk Delete'}
        buttonStyles={'bg-solid-violet col-span-5 md:col-span-2'}>
        <BulkDeleteModal />
      </Modal>
    </div>
  );
};

export default EmployeeHeaderButtons;
