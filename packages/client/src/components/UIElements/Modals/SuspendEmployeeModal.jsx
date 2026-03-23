import { Button } from '@/components/ui/button';
import { Card, CardHeader, CardTitle } from '@/components/ui/card';
import { Modal } from 'components/Modal';

const SuspendEmployeeModal = () => {
  return (
    <Modal
      triggerText={'Suspend Employee'}
      title={'Suspend Employee'}
      titleStyles={
        'bg-gradient-to-r text-center from-purple-500 to-blue-600 rounded-t-md py-4 text-white px-4'
      }>
      <div className="grid gap-4 grid-cols-12 col-span-12 pb-10 pt-2 px-8">
        <Card className={`border-none shadow-none col-span-12 sm:col-span-6"}`}>
          <CardHeader className="flex flex-row items-center justify-center p-2 py-6">
            <CardTitle className="text-xl font-semibold">
              Do you want to Suspend all the checked employees?
            </CardTitle>
          </CardHeader>
        </Card>
        <div className="col-span-6 col-start-5">
          <Button
            varient="ghost"
            className="w-32 border border-violet-500 mr-4 text-violet-500 bg-transparent">
            No
          </Button>
          <Button className="bg-violet-500 hover:bg-violet-600 w-32">
            Yes
          </Button>
        </div>
      </div>
    </Modal>
  );
};

export default SuspendEmployeeModal;
