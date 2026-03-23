import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import AddPictureIcon from 'assets/images/add-pic-icn.png';
import AddFileIcon from 'assets/images/add-file-icn.png';
import { RxCross1 } from 'react-icons/rx';
import { adminFetchClient } from './Api/get';
import { useQuery } from '@tanstack/react-query';
import moment from 'moment';
import { useEffect, useRef, useState } from 'react';
import { ChromePicker } from 'react-color';
import {
  fetchEmployeeDetails,
  uploadFiles,
  createTasks,
  editTasksDetails,
} from './Api/post';
import { useFormik } from 'formik';
// import { addTasksSchema } from 'schema/Tasks/AddTasks';
import { AddStageSchema } from 'schema/Tasks/AddStage';
import ButtonLoading from 'components/ButtonLoading';
import { toast } from 'sonner';
import * as DialogPrimitive from '@radix-ui/react-dialog';
import { DialogClose } from '@/components/ui/dialog';
import CurrencyDropdown from '@/components/ui/CurrencyDropdown';
import PDFIconImage from '../../../assets/images/reportTable/pdf.png';
import { updateStage } from 'components/StageTaskModule/Api/put';
import { fetchStage } from 'components/StageTaskModule/Api/get';
import { createStage } from 'components/StageTaskModule/Api/post';

const AddNewStageTaskModal = ({ rowData, refetch, handleCloseAddModal }) => {
  const [colorpick, setcolorpick] = useState(false);
  const [getcolor, setgetcolor] = useState(rowData ? rowData.color : '#C91919');

  const handlechromecolor = color => {
    setgetcolor(color.hex);
    formik.setFieldValue('color', color.hex);
    // setcolorpick(false);
    // setcolorpick(false);
  };

  useEffect(() => {
    formik.setFieldValue('color', getcolor);
  }, [getcolor]);
  const handlecolorchange = e => {
    e.preventDefault(); // Prevent form submission
    setcolorpick(!colorpick);
  };

  const [selectedClient, setSelectedClient] = useState(null);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [isDialogOpen, setIsDialogOpen] = useState(true);

  const { data } = useQuery({
    queryKey: ['fetchStage'],
    queryFn: fetchStage,
  });

  // console.log(data?.data?.body?.data?.allTags)
  const clients = data?.data?.body?.data?.allTags || [];

  const { data: response } = useQuery({
    queryKey: ['fetchEmployeeDetails', searchQuery],
    queryFn: () => fetchEmployeeDetails(searchQuery),
  });
  const employees = response?.body?.data?.resultData || [];
  const [taskCreated, setTaskCreated] = useState(false);

  const formik = useFormik({
    initialValues: {
      tagName: rowData ? rowData.tagName : '',
      tagDescription: rowData ? rowData.tagDescription : null,
      color: rowData ? rowData.color : '#C91919',
      isActive: true,
    },
    validationSchema: AddStageSchema,
    onSubmit: async values => {
      const payload = {
        tagId: rowData ? rowData._id : undefined,
        tagName: rowData ? rowData.tagName : values.tagName,
        tagDescription: rowData
          ? rowData.tagDescription
          : values.tagDescription,
        color: rowData ? rowData.color : values.color,
        isActive: rowData ? rowData.isActive : values.isActive,
      };
      try {
        let response;
        if (rowData) {
          function findUpdatedValues(a, b) {
            const updatedValues = {
              tagId: rowData?._id, // Assuming rowData is defined somewhere in your component
              isActive: true,
            };

            // Iterate over keys from object 'a'
            Object.keys(a).forEach(key => {
              if (b.hasOwnProperty(key) && a[key] !== b[key]) {
                updatedValues[key] = b[key];
              }
            });

            return updatedValues;
          }

          const result = findUpdatedValues(payload, formik.values);
          // response = await updateStage(rowData._id, payload);
          response = await updateStage(payload.tagId, result);
          // console.log(response,'...resposnsee......')
          // const { status, message } = response.body;
          if (response?.data?.body?.status === 'success') {
            toast.success(
              response?.data?.body?.message || 'Stage updated successfully'
            );
            refetch();
          } else {
            toast.error(
              response?.data?.body?.message || 'Failed to update stage'
            );
          }
        } else {
          // Creating a new stage
          response = await createStage(payload);
          // console.log(response,'...resposnsee......')
          // const { status, message } = response.body;
          if (response?.body?.status === 'success') {
            toast.success(
              response?.body?.message || 'Stage created successfully'
            );
            refetch();
          } else {
            toast.error(response?.body?.message || 'Failed to create stage');
          }
        }
      } catch (error) {
        console.error('Error:', error);
        toast.error('An error occurred.');
      }
      refetch();
    },
  });

  // console.log(row, 'row');
  // console.log(selectedClient ? selectedClient.clientName : row ? row?.clientName : '' ,'ggggggggg')

  // useEffect(() => {
  //   console.log('Formik Values:', formik.values);
  // }, [formik.values]);

  const catMenu = useRef(null);

  function useClickOutside(ref, onClickOutside) {
    useEffect(() => {
      /**
       * Invoke Function onClick outside of element
       */
      function handleClickOutside(event) {
        if (ref.current && !ref.current.contains(event.target)) {
          onClickOutside();
        }
      }
      // Bind
      document.addEventListener('mousedown', handleClickOutside);
      return () => {
        // dispose
        document.removeEventListener('mousedown', handleClickOutside);
      };
    }, [ref, onClickOutside]);
  }

  useClickOutside(catMenu, () => {
    setcolorpick(false);
  });

  return (
    <div className="grid gap-4 grid-cols-12 col-span-12  px-[12px] pt-[4px] pb-[16px] w-full h-full ">
      <form
        onSubmit={formik.handleSubmit}
        onClick={event => event.stopPropagation()}
        className="card-shadow grid gap-4 2xl:gap-4 grid-cols-12 col-span-12 md:col-span-12 bg-white rounded-lg p-3 px-10 py-8 w-full">
        {/* className="card-shadow grid gap-4 2xl:gap-4 grid-cols-12 col-span-12 md:col-span-9 bg-white rounded-lg p-3 px-10 py-8"> */}
        <Card className="border-none shadow-none col-span-12">
          <CardHeader className="flex flex-row items-center justify-between p-2 pl-0">
            <CardTitle className="text-sm font-bold">Stage Name*</CardTitle>
          </CardHeader>
          <CardContent>
            <Input
              type="text"
              placeholder="Write Stage Name"
              className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700 h-[42px]"
              name="tagName"
              value={formik.values.tagName}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
            />
            {formik.touched.tagName && formik.errors.tagName ? (
              <div className="text-red-500 text-xs">
                {formik.errors.tagName}
              </div>
            ) : null}
          </CardContent>
        </Card>

        <Card className="border-none shadow-none col-span-12">
          <CardHeader className="flex flex-row items-center justify-between p-2 pl-0">
            <CardTitle className="text-sm font-bold">Select Color*</CardTitle>
          </CardHeader>
          <CardContent className="relative ">
            <Input
              type="text"
              placeholder="Select a color"
              className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700 h-[42px]"
              name="color"
              // value={getcolor}
              value={formik.values.color}
              readOnly
            />
            <button
              onClick={handlecolorchange}
              className={`absolute w-[20px] h-[22px] right-[10px] top-[10px] `}
              style={{ backgroundColor: getcolor }}></button>
            {colorpick && (
              <div ref={catMenu}>
                <ChromePicker
                  className="absolute z-10 top-[-100px]"
                  onChange={handlechromecolor}
                  color={getcolor}
                />
              </div>
            )}
          </CardContent>
          {formik.touched.color && formik.errors.color ? (
            <div className="text-red-500 text-xs">{formik.errors.color}</div>
          ) : null}
        </Card>
        <div
          className={`col-span-12 flex justify-center border-none rounded-none px-[24px] gap-6`}>
          <DialogClose asChild>
            <Button
              type="button"
              className="border-[1px] rounded-lg text-[#6A6AEC] border-[#6A6AEC] bg-white w-45per">
              Discard
            </Button>
          </DialogClose>
          <DialogClose asChild>
            <Button
              type="submit"
              // onClick={() => {
              //   formik.handleSubmit();
              //   setIsDialogOpen(false);
              // }}
              disabled={
                !(formik.isValid && formik.dirty) || formik.isSubmitting
              }
              className=" bg-[#6A6AEC] w-45per">
              {rowData ? 'Edit Stage' : ' Add Stage'}
            </Button>
          </DialogClose>
        </div>
      </form>
    </div>
  );
};

export default AddNewStageTaskModal;
