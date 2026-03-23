import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { Input } from '@/components/ui/input';

const BulkUpdateModal = () => {
  return (
    <div className="grid gap-4 grid-cols-12 col-span-12 p-6 overflow-auto max-h-[300px] 2xl:max-h-[500px]">
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6"}`}>
        <CardContent className="py-4">
          <div className="flex max-w-2xl mx-auto items-center space-x-2 border rounded">
            <Input id="picture" type="file" placeholder="Bulk" />
            <Button
              type="submit"
              className="bg-[#E8E8FF] hover:bg-[#E8E8FF]/80 text-violet-500 rounded py-7 w-36">
              Browse
            </Button>
          </div>
          <p className="pt-4 text-sm pl-20 font-semibold">
            Note: Upload File Only in xlsx Format..{' '}
            <span className="text-violet-500">Download</span> From Here
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
          Update
        </Button>
      </div>
    </div>
  );
};

export default BulkUpdateModal;
