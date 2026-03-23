import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Modal } from 'components/Modal';

const AssignShiftModal = () => {
  return (
    <Modal
      triggerText={'Assign Shift'}
      title={'Assign Shift'}
      titleStyles={
        'bg-gradient-to-r text-center from-purple-500 to-blue-600 rounded-t-md py-4 text-white px-4'
      }>
      <div className="grid gap-4 grid-cols-12 col-span-12 grid-rows-2 py-6 px-8">
        <Card
          className={`border-none shadow-none col-span-12 sm:col-span-6" }`}>
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-sm font-bold">Select Shift</CardTitle>
          </CardHeader>
          <CardContent>
            <Select>
              <SelectTrigger className="w-full">
                <SelectValue placeholder="Select Shift" />
              </SelectTrigger>
              <SelectContent>
                <SelectGroup>
                  <SelectItem value="no-shift">No Shift</SelectItem>
                  <SelectItem value="tester-shift">Tester Shift</SelectItem>
                  <SelectItem value="alert-shift">Alert Shift</SelectItem>
                  <SelectItem value="morning-shift">Morning Shift</SelectItem>
                  <SelectItem value="afteroon-shift">
                    Afternoon Shift
                  </SelectItem>
                  <SelectItem value="normal-shift">Normal Shift</SelectItem>
                </SelectGroup>
              </SelectContent>
            </Select>
          </CardContent>
        </Card>
        <div className="col-span-6 col-start-4">
          <Button
            varient="ghost"
            className="w-32 border border-violet-500 mr-4 text-violet-500 bg-transparent">
            Cancel
          </Button>
          <Button className="bg-violet-500 hover:bg-violet-600 w-32">
            Assign
          </Button>
        </div>
      </div>
    </Modal>
  );
};

export default AssignShiftModal;
