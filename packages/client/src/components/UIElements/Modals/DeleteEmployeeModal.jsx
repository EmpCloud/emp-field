import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Modal } from 'components/Modal';

const DeleteEmployeeModal = () => {
  return (
    <Modal
      triggerText={'Delete Employee'}
      title={'Delete Employee'}
      titleStyles={
        'bg-gradient-to-r text-center from-purple-500 to-blue-600 rounded-t-md py-4 text-white px-4'
      }>
      <div className="grid gap-4 grid-cols-12 col-span-12 pb-10 pt-2 px-8">
        <Card className={`border-none shadow-none col-span-12 sm:col-span-6"}`}>
          <CardHeader className="flex flex-row items-center justify-center p-2">
            <CardTitle className="text-xl font-semibold">
              Do you want to delete all the checked employees?
            </CardTitle>
          </CardHeader>
          <CardContent className="text-center py-4">
            <p>
              <span className="text-violet-500">Note</span>:- The data of the
              employees will get deleted. This action cannot be undone!
            </p>
          </CardContent>
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

export default DeleteEmployeeModal;
