import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import AddPictureIcon from 'assets/images/add-pic-icn.png';
import { Calendar as CalendarCom } from '@/components/ui/calendar';
import { CalendarDays } from 'lucide-react';
import AddFileIcon from 'assets/images/add-file-icn.png';
import { RxCross1 } from 'react-icons/rx';
import { adminFetchClient, fetchTags } from './Api/get';
import { useQuery } from '@tanstack/react-query';
import moment from 'moment';
import { useEffect, useRef, useState } from 'react';
import { Checkbox } from '@/components/ui/checkbox';
import {
  fetchEmployeeDetails,
  uploadFiles,
  createTasks,
  editTasksDetails,
} from './Api/post';
import { useFormik } from 'formik';
import { getAddTasksSchema } from 'schema/Tasks/AddTasks';
import ButtonLoading from 'components/ButtonLoading';
import { toast } from 'sonner';
import * as DialogPrimitive from '@radix-ui/react-dialog';
import { DialogClose } from '@/components/ui/dialog';
import CurrencyDropdown from '@/components/ui/CurrencyDropdown';
import PDFIconImage from '../../../assets/images/reportTable/pdf.png';
import { clientSearchedSuggestions } from 'components/FilterModule/Api/get';

const AddNewTaskModal = ({ taskDetail, row, responseData }) => {
  const [clientSearchQuery, setClientSearchQuery] = useState('');
  const [employeeSearchQuery, setEmployeeSearchQuery] = useState('');
  const [startTime, setStartTime] = useState(row?.start_time || '');
  const [endTime, setEndTime] = useState(row?.end_time || '');
  const [isPopoverOpenClient, setIsPopoverOpenClient] = useState(false);
  const [images, setImages] = useState([]);
  const [files, setFiles] = useState([]);
  // const [selectedClient, setSelectedClient] = useState(row?.clientName);
  const [selectedClient, setSelectedClient] = useState(null);
  // const [selectedEmployee, setSelectedEmployee] = useState(row?.clientName === 'Default Client' ? row?.employeeId || null : null);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [selectedTag, setSelectedTag] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [loadingClients, setLoadingClients] = useState(false);
  const [loadingEmployees, setLoadingEmployees] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(true);
  const [isRepeattask, setisRepeattask] = useState(
    row?.recurrenceDetails?.recurrenceId ? true : false
  );
  const [isEveryDay, setisEveryDay] = useState(
    row?.recurrenceDetails?.daysOfWeek?.length === 7
  );
  const [startDate, setStartDate] = useState(
    row?.recurrenceDetails?.startDate || ''
  );
  const [endDate, setEndDate] = useState(row?.recurrenceDetails?.endDate || '');
  const [isPopoverOpen, setIsPopoverOpen] = useState(false);
  const [isCleared, setIsCleared] = useState(false);
  const [isEditingTime, setIsEditingTime] = useState(false);
  const [endTimeValue, setEndTimeValue] = useState(
    row?.end_time ? moment(row?.end_time).format('YYYY-MM-DDTHH:mm') : ''
  );

  const { data } = useQuery({
    queryKey: ['adminFetchClient'],
    queryFn: async () => {
      setLoadingClients(true); // Set loading state to true
      const result = await adminFetchClient();
      setLoadingClients(false); // Set loading state to false
      return result;
    },
  });

  const repeatTaskDaysWork = [
    { title: 'S', id: 0, state: 'false' },
    { title: 'M', id: 1, state: 'false' },
    { title: 'T', id: 2, state: 'false' },
    { title: 'W', id: 3, state: 'false' },
    { title: 'T', id: 4, state: 'false' },
    { title: 'F', id: 5, state: 'false' },
    { title: 'S', id: 6, state: 'false' },
  ];
  const [repeatTaskDaysWorkState, setrepeatTaskDaysWorkState] =
    useState(repeatTaskDaysWork);
  const clients = data?.data?.body?.data?.data || [];

  const initialExcludedDays = row?.recurrenceDetails?.daysOfWeek
    ? repeatTaskDaysWork
        .filter(day => !row.recurrenceDetails.daysOfWeek.includes(day.id))
        .map(day => day.id)
    : repeatTaskDaysWork.map(day => day.id);
  const [recurrence, setRecurrence] = useState({
    startDate: row?.recurrenceDetails?.startDate || startDate || null,
    endDate: row?.recurrenceDetails?.endDate || null,
    excludedDays: initialExcludedDays,
  });

  const { data: response, refetch: refetchEmployeeDetails } = useQuery({
    queryKey: ['fetchEmployeeDetails', employeeSearchQuery],
    queryFn: async () => {
      setLoadingEmployees(true); // Set loading state to true
      const result = await fetchEmployeeDetails(employeeSearchQuery);
      setLoadingEmployees(false); // Set loading state to false
      return result;
    },
  });

  const tagsData = useQuery({
    queryKey: ['fetchTags'],
    queryFn: () => fetchTags(),
  });

  const tagsResultedData = tagsData?.data?.data?.body?.data?.allTags || [];
  const employees = response?.body?.data?.resultData || [];
  const [taskCreated, setTaskCreated] = useState(false);
  const formik = useFormik({
    initialValues: {
      taskName: row ? row.task : '',
      clientId: row ? row.clientId : '',
      taskDescription: row ? row.taskDiscription : '',
      startTime: row ? row.start_time : '',
      endTime: row ? row.end_time : '',
      taskVolume: row ? row.taskVolume || 0 : 0,
      currency: row ? row?.currency || '' : '',
      amount: row ? row?.amount || 0 : 0,
      employeeId: row ? row.employeeId : '',
      tagId: row ? row.tagId : '',
      recurrenceId: row ? row?.recurrenceId : null,
      recurrence: isRepeattask
        ? {
            startDate: row?.recurrenceDetails?.startDate || startDate || null,
            endDate: row?.recurrenceDetails?.endDate || endDate || null,
            excludedDays: initialExcludedDays || [],
          }
        : null,
    },
    // validationSchema: addTasksSchema,
    validationSchema: getAddTasksSchema(isRepeattask),
    onSubmit: async values => {
      const updatedExcludedDays = repeatTaskDaysWorkState
        .filter(item => item.state === 'false')
        .map(item => item.id);

      values.date = row?.date;
      values.tasKId = row?.taskId;
      const updaetePayLoad = {
        clientId: values?.clientId,
        taskName: values?.taskName,
        taskId: values?.tasKId,
        start_time: moment(values.startTime).format('YYYY-MM-DD HH:mm:ss'),
        end_time: moment(values.endTime).format('YYYY-MM-DD HH:mm:ss'),
        date: moment(values.startTime).format('YYYY-MM-DD'),
        taskDescription: values.taskDescription,
        employeeId: values?.employeeId,
        files,
        images,
        value: {
          currency: values.currency || 'INR',
          amount: Number(values.amount) || 0,
        },
        taskVolume: values.taskVolume,
        tagLogs: values?.tagName
          ? [
              {
                tagName: values.tagName,
                time: moment(new Date()).format(),
              },
            ]
          : [],
        // recurrenceId: values?.recurrenceId || row?.recurrenceId, // Add recurrenceId
        recurrence: {
          startDate: recurrence?.startDate || startDate || null,
          endDate: recurrence?.endDate || endDate || null,
          excludedDays: updatedExcludedDays || [],
        },
      };
      if (row != undefined) {
        if (row) {
          const response = await editTasksDetails(updaetePayLoad);
          if (response?.data?.body?.status === 'success') {
            setTaskCreated(true);
            responseData.refetch();
            toast.success(response?.data?.body?.message || 'Success');
          } else {
            toast.error(response?.data?.body?.message || 'Error occurred');
          }
          setIsDialogOpen(false);
        }
      } else {
        const payload = {
          clientId: selectedClient?._id,
          taskName: values.taskName,
          start_time: moment(values.startTime).format('YYYY-MM-DD HH:mm:ss'),
          end_time: moment(values.endTime).format('YYYY-MM-DD HH:mm:ss'),
          date: moment(values.startTime).format('YYYY-MM-DD'),
          taskDescription: values.taskDescription,
          files,
          images,
          value: {
            currency: values.currency || 'INR',
            amount: Number(values.amount) || 0,
          },
          taskVolume: values.taskVolume,
          recurrence: {
            startDate: recurrence?.startDate || startDate || null,
            endDate: recurrence?.endDate || endDate || null,
            excludedDays: updatedExcludedDays,
          },
        };

        try {
          if (selectedEmployee?.emp_id) {
            const response = await createTasks(
              selectedEmployee.emp_id,
              payload
            );
            if (response?.data?.body?.status === 'success') {
              setTaskCreated(true);

              toast.success(
                response?.data?.body?.message || 'Task created successfully'
              );
            } else {
              toast.error(response?.data?.body?.message || 'Try again!');
            }
            responseData.refetch();
          }
        } catch (error) {
          console.error('Error creating task:', error);
        }
      }
    },
  });

  useEffect(() => {
    if (isRepeattask) {
      formik.setFieldValue('recurrence.startDate', startDate || null);
      formik.setFieldValue('recurrence.endDate', endDate || null);
    }
  }, [isRepeattask, startDate, endDate]);

  const pictureInputRef = useRef(null);
  const fileInputRef = useRef(null);
  const MAX_FILES = 5;
  const handleFileChange = async (e, type) => {
    const filesData = Array.from(e.target.files);
    const uploadedFiles = [];
    if (type === 'picture') {
      if (filesData.length + images.length > MAX_FILES)
        return alert(`You can only upload up to ${MAX_FILES} images.`);
    } else {
      if (filesData.length + files.length > MAX_FILES)
        return alert(`You can only upload up to ${MAX_FILES} files.`);
    }
    for (const file of filesData) {
      try {
        const url = await uploadFiles(file);

        // Only add a description if the file type is 'picture'
        const fileObject = {
          url,
        };

        if (type === 'picture') {
          fileObject.description = 'Picture'; // Add description only for images
        }

        uploadedFiles.push(fileObject);
      } catch (error) {
        console.error('Error uploading file:', error);
      }
    }

    if (type === 'picture') {
      setImages(prevFiles => [
        ...prevFiles,
        ...uploadedFiles.map(image => {
          const { _id, ...imageWithoutId } = image; // Remove _id field
          return imageWithoutId; // Ensure no _id is included in the image
        }),
      ]);
    } else {
      setFiles(prevFiles => [...prevFiles, ...uploadedFiles]);
    }
  };

  const handleButtonClick = type => {
    if (type === 'picture') {
      pictureInputRef.current.click();
    } else {
      fileInputRef.current.click();
    }
  };

  const handleRemoveFile = (url, type) => {
    if (type === 'picture') {
      setImages(prevFiles => prevFiles.filter(file => file.url !== url));
    } else {
      setFiles(prevFiles => prevFiles.filter(file => file.url !== url));
    }
  };

  const handleClientSelect = client => {
    setSelectedClient(client);
    formik.setFieldValue('clientId', client._id);
    formik.setFieldTouched('taskName', false);
  };
  const handleEmployeeSelect = employee => {
    setSelectedEmployee(employee);
    formik.setFieldValue('employeeId', employee.emp_id);
    formik.setFieldTouched('employeeId', true);
    setSelectedEmployee(employee);
  };

  const [date, setDate] = useState(false);

  const handleTimeChange = (e, type) => {
    const selectedTime = e.target.value; // Ensure proper datetime format

    if (type === 'start') {
      const originalDate = moment(e.target.value).format('YYYY-MM-DD'); // Ensure selected date is used

      const formattedStartTime =
        moment(selectedTime).format('YYYY-MM-DDTHH:mm'); // Ensure correct format

      setStartTime(formattedStartTime);
      formik.setFieldValue('startTime', formattedStartTime);

      // Reset end time when start time changes
      setEndTimeValue('');
      formik.setFieldValue('endTime', '');
    } else if (type === 'end') {
      const originalDate = startTime
        ? moment(startTime).format('YYYY-MM-DD') // Use the date from startTime
        : moment().format('YYYY-MM-DD');

      const formattedEndTime = `${originalDate}T${moment(e.target.value, 'HH:mm').format('HH:mm')}`;

      setEndTimeValue(formattedEndTime); // ✅ Update state immediately
      formik.setFieldValue('endTime', formattedEndTime); // ✅ Ensure Formik updates too
    }
  };

  const handleDateChange = (e, type) => {
    const value = e.target.value;

    if (type === 'start') {
      setStartDate(value);
      setRecurrence(prev => ({ ...prev, startDate: value }));
      formik.setFieldValue('recurrenceDetails.startDate', value);

      // Check if the start date is after the end date
      if (new Date(value) > new Date(endDate)) {
        setDate(true); // set error flag
        formik.setFieldError(
          'recurrenceDetails.startDate',
          'Start date cannot be after end date'
        );
      } else {
        setDate(false);
        formik.setFieldError('recurrenceDetails.startDate', ''); // clear error if valid
      }
    } else if (type === 'end') {
      setEndDate(value);
      setRecurrence(prev => ({ ...prev, endDate: value }));
      formik.setFieldValue('recurrenceDetails.endDate', value);

      // Check if the end date is before the start date
      if (new Date(value) < new Date(startDate)) {
        setDate(true); // set error flag
        formik.setFieldError(
          'recurrenceDetails.endDate',
          'End date cannot be before start date'
        );
      } else {
        setDate(false);
        formik.setFieldError('recurrenceDetails.endDate', ''); // clear error if valid
      }
    }
  };

  const filteredEmployees = searchQuery
    ? employees?.filter(employee =>
        employee.fullName.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : employees;

  const displayedEmployees =
    selectedClient?.clientName === 'Default Client'
      ? filteredEmployees || []
      : filteredEmployees || [];

  useEffect(() => {
    if (selectedClient?.clientName === 'Default Client') {
      setLoadingEmployees(true);
      fetchEmployeeDetails('').then(() => {
        setLoadingEmployees(false);
        if (employees.length > 0) {
          formik.setFieldValue('employeeId', employees[0].emp_id);
        } else {
          formik.setFieldValue('employeeId', '');
        }
        formik.setFieldTouched('employeeId', true);
      });
    }
  }, [selectedClient, employees]);

  const taskNameRef = useRef(null);
  useEffect(() => {
    if (isDialogOpen) {
      taskNameRef.current?.focus(); // Focus the task name input when the dialog opens
    }
  }, [isDialogOpen]);

  useEffect(() => {
    if (selectedClient) {
      setIsPopoverOpenClient(!isPopoverOpenClient);
    }
  }, [selectedClient]);

  const onChangeBtnState = index => {
    const updatedDays = [...repeatTaskDaysWorkState]; // Clone the current state of days
    updatedDays[index].state =
      updatedDays[index].state === 'true' ? 'false' : 'true'; // Toggle state for selected day
    setrepeatTaskDaysWorkState(updatedDays); // Update the days state

    const updatedExcludedDays = updatedDays
      .filter(item => item.state === 'false') // Get the days that are excluded
      .map(item => item.id);

    formik.setFieldValue('recurrence.excludedDays', updatedExcludedDays); // Update excludedDays in formik

    setRecurrence(prev => ({
      ...prev,
      excludedDays: updatedExcludedDays, // Update recurrence state
    }));
  };

  const handleCheckEveryDay = checked => {
    setisEveryDay(checked);

    if (checked) {
      // If checked, mark all days as selected
      const allSelected = repeatTaskDaysWork.map(day => ({
        ...day,
        state: 'true',
      }));
      setrepeatTaskDaysWorkState(allSelected);

      setRecurrence(prev => ({
        ...prev,
        excludedDays: [],
      }));

      formik.setFieldValue('recurrence.excludedDays', []);
    } else {
      // If unchecked, mark all days as unselected
      const resetDays = repeatTaskDaysWork.map(day => ({
        ...day,
        state: 'false',
      }));
      setrepeatTaskDaysWorkState(resetDays);

      const updatedExcludedDays = repeatTaskDaysWork.map(day => day.id);

      setRecurrence(prev => ({
        ...prev,
        excludedDays: updatedExcludedDays,
      }));

      formik.setFieldValue('recurrence.excludedDays', updatedExcludedDays); // Update excludedDays in formik
    }
  };
  useEffect(() => {
    if (isEveryDay) {
      const updatedDays = repeatTaskDaysWork.map(item => ({
        ...item,
        state: 'true',
      }));
      setrepeatTaskDaysWorkState(updatedDays);
      setRecurrence(prev => ({ ...prev, excludedDays: [] }));
      formik.setFieldValue('recurrence.excludedDays', []);
    }
  }, [isEveryDay]);

  useEffect(() => {
    if (row?.recurrenceId || formik?.values?.recurrenceId) {
      setisRepeattask(true);
    } else {
      setisRepeattask(false);
    }
  }, [row?.recurrenceId, formik?.values?.recurrenceId]);

  useEffect(() => {
    if (row?.recurrenceDetails?.daysOfWeek) {
      const updatedDays = repeatTaskDaysWork.map(day => ({
        ...day,
        state: row.recurrenceDetails.daysOfWeek.includes(day.id)
          ? 'true'
          : 'false',
      }));
      setrepeatTaskDaysWorkState(updatedDays);
    }
  }, [row?.recurrenceDetails?.daysOfWeek]);
  useEffect(() => {
    if (row?.images?.length > 0) {
      setImages(row.images);
    }
    if (row?.files?.length > 0) {
      setFiles(row.files);
    }
  }, [row]);
  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      if (selectedClient?.clientName === 'Default Client') {
        refetchEmployeeDetails();
      }
    }, 500);
    return () => clearTimeout(delayDebounceFn);
  }, [employeeSearchQuery, selectedClient]);

  return (
    <div className="grid gap-x-4 grid-cols-12 col-span-12 overflow-auto p-4 md:px-6 2xl:p-6 lg:max-h-[400px] 2xl:max-h-[740px]">
      <Card className="border-none shadow-none col-span-6">
        <CardHeader className="flex flex-row items-center justify-between p-2 pl-0">
          <CardTitle className="text-sm font-bold">Task Name*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            ref={taskNameRef}
            type="text"
            placeholder="Write Task Name"
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700 h-[42px]"
            name="taskName"
            value={formik.values.taskName}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            tabIndex={-1}
          />
          {formik.errors.taskName && formik.touched.taskName ? (
            <div className="text-red-500 text-xs">{formik.errors.taskName}</div>
          ) : null}
        </CardContent>
      </Card>

      <Card className={`border-none shadow-none col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2 pl-0">
          <CardTitle className="text-sm font-bold">Select Client*</CardTitle>
        </CardHeader>
        <CardContent>
          <Popover
            className="relative"
            open={isPopoverOpenClient}
            onOpenChange={setIsPopoverOpenClient}>
            <PopoverTrigger asChild>
              <div className="relative">
                <Input
                  type="text"
                  placeholder="Select Client"
                  value={
                    isCleared && !selectedClient
                      ? '' // If cleared and no client is selected, show empty
                      : selectedClient
                        ? selectedClient.clientName // Show selected client
                        : clientSearchQuery
                  }
                  onChange={e => {
                    setIsCleared(false);
                    setClientSearchQuery(e.target.value);
                    setIsPopoverOpenClient(true);
                    formik.setFieldValue('clientId', ''); // Clear clientId on input change
                  }}
                  name="clientId"
                  className="bg-slate-400/10 ps-3 placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm h-[42px]"
                />
                {/* {(row || selectedClient) && (
                      <RxCross1
                        className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                        onClick={e => {
                          e.stopPropagation();
                          if (row === undefined) {
                            setSelectedClient(null);
                            setSearchQuery('');
                            formik.setFieldValue('clientId', '');
                          } else {
                            setSelectedClient(null);
                            setSearchQuery('');
                            formik.setFieldValue('clientId', '');
                            formik.setFieldTouched('clientId', false);
                          }
                          if (
                            Object?.keys(selectedClient?.employees).length !== 0
                          ) {
                            if (row === undefined) {
                              setSelectedEmployee(null);
                              // handleEmployeeSelect(null);
                              formik.setFieldValue('employeeId', '');
                            } else {
                              formik.setFieldTouched('clientId', false);
                              setSelectedEmployee(null);
                              formik.setFieldValue('employeeId', '');
                            }
                          }
                        }}
                      />
                    )} */}
                {(row ||
                  selectedClient ||
                  (selectedClient?.clientName === '' &&
                    selectedClient?.clientName !== 'Default Client')) && (
                  <RxCross1
                    className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                    onClick={e => {
                      e.stopPropagation();

                      // Reset client selection
                      setSelectedClient(null);
                      setClientSearchQuery('');
                      setSearchQuery('');
                      setIsCleared(true);

                      formik.setFieldValue('clientId', '');
                      formik.setFieldTouched('clientId', true);

                      // Reset employee selection
                      setSelectedEmployee(null);
                      formik.setFieldValue('employeeId', '');
                      formik.setFieldTouched('employeeId', false);
                    }}
                  />
                )}
                {formik.errors.clientId && formik.touched.clientId && (
                  <div className="text-red-500 text-xs">
                    {formik.errors.clientId}
                  </div>
                )}
              </div>
            </PopoverTrigger>
            <PopoverContent
              onOpenAutoFocus={e => e.preventDefault()}
              align="start"
              className="max-h-[200px] overflow-y-auto p-2 min-w-40">
              {loadingClients ? (
                <div>Loading clients...</div>
              ) : clients.filter(client =>
                  client.clientName
                    .toLowerCase()
                    .includes(clientSearchQuery.toLowerCase())
                ).length === 0 ? (
                <div>No clients found</div>
              ) : (
                clients
                  .filter(client =>
                    client.clientName
                      .toLowerCase()
                      .includes(clientSearchQuery.toLowerCase())
                  )
                  .map(client => (
                    <div
                      key={client._id}
                      className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer"
                      onClick={() => {
                        setIsCleared(false);
                        setSelectedClient(client);
                        setClientSearchQuery(
                          client.clientName === 'Default Client'
                            ? ''
                            : client.clientName
                        );
                        formik.setFieldValue('clientId', client._id);
                        setTimeout(() => {
                          formik.setFieldTouched('clientId', true);
                        }, 0);

                        if (client.clientName === 'Default Client') {
                          setSelectedEmployee(null);
                          refetchEmployeeDetails();
                        } else if (
                          client.employees &&
                          typeof client.employees === 'object' &&
                          Object.keys(client.employees).length > 0
                        ) {
                          const firstEmployee = client.employees?.emp_id
                            ? client.employees
                            : client.employees[0];
                          setSelectedEmployee(firstEmployee);
                          formik.setFieldValue(
                            'employeeId',
                            firstEmployee.emp_id || ''
                          );
                          formik.setFieldTouched('employeeId', true);
                        } else {
                          setSelectedEmployee(null);
                          formik.setFieldValue('employeeId', '');
                          formik.setFieldTouched('employeeId', true);
                        }
                        setIsPopoverOpenClient(false);
                      }}>
                      <CardTitle className="text-xs 2xl:text-sm">
                        {client.clientName}
                      </CardTitle>
                    </div>
                  ))
              )}
            </PopoverContent>
          </Popover>
        </CardContent>
      </Card>

      {/* Task Description */}
      <Card className="border-none shadow-none col-span-12 pt-4">
        <CardHeader className="flex flex-row items-center justify-between p-2 pl-0">
          <CardTitle className="text-sm font-bold">Task Description*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="text"
            placeholder="Write Task Description"
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700 h-[42px]"
            name="taskDescription"
            value={formik.values.taskDescription}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
          {formik.touched.taskDescription && formik.errors.taskDescription ? (
            <div className="text-red-500 text-xs">
              {formik.errors.taskDescription}
            </div>
          ) : null}
        </CardContent>
      </Card>

      <Card className="border-none shadow-none col-span-12 pt-4">
        <CardHeader className="flex flex-row items-center justify-between p-2 pl-0 pb-[15px]">
          <CardTitle className="text-sm font-bold">Schedule Time*</CardTitle>
        </CardHeader>
        <CardContent className="space-y-2">
          <div className="flex flex-row gap-4">
            <div className="flex flex-col flex-1">
              <label className="text-xs 2xl:text-sm font-semibold pb-1">
                Start Time*
              </label>
              <input
                type="datetime-local"
                onChange={e => handleTimeChange(e, 'start')}
                className="bg-slate-400/10 border border-input ps-2  focus:outline-none cursor-pointer placeholder:text-slate-700 text-xs pr-1 h-[42px]"
                defaultValue={row ? row?.start_time : startTime}
              />
              {formik.touched.startTime && formik.errors.startTime ? (
                <div className="text-red-500 text-xs">
                  {formik.errors.startTime}
                </div>
              ) : null}
            </div>

            <div className="flex flex-col flex-1">
              <label className="text-xs 2xl:text-sm font-semibold pb-1">
                End Time*
              </label>
              <input
                id="endTime"
                type={isEditingTime ? 'time' : 'datetime-local'}
                // readOnly={!isEditingTime}
                onFocus={() => setIsEditingTime(true)}
                onBlur={() => setIsEditingTime(false)}
                onChange={e => handleTimeChange(e, 'end')}
                className="bg-slate-400/10 border border-input ps-2 h-[42px] focus:outline-none cursor-pointer placeholder:text-slate-700 text-xs pr-1"
                value={
                  isEditingTime
                    ? moment(endTimeValue).format('HH:mm') // Show only time while editing
                    : moment(endTimeValue).format('YYYY-MM-DD HH:mm')
                } // Show full date when not editing
              />
              {formik.touched.endTime && formik.errors.endTime ? (
                <div className="text-red-500 text-xs">
                  {date
                    ? 'please edit end date with respect to start date'
                    : formik.errors.endTime}
                </div>
              ) : null}
            </div>
          </div>
        </CardContent>
      </Card>

      <Card className="border-none shadow-none col-span-12 pt-4">
        <div
          className={`flex gap-2 items-center  ${isRepeattask ? 'mb-4' : 'mb-0'}`}>
          <Checkbox
            checked={isRepeattask}
            onCheckedChange={checked => {
              setisRepeattask(checked);
            }}
            className="w-[24px] h-[24px] "
            checkClass={' !w-[20px] !h-[20px]'}
            chckColor={'data-[state=checked]:!bg-[#6a6aec]'}
          />
          <span className="text-[16px] font-medium text-[#4d4c4c]">
            Is this a repeating task?
          </span>
        </div>
        {(isRepeattask ||
          row?.recurrenceDetails?.recurrenceId ||
          formik?.values?.recurrenceId) && (
          <div>
            <div className="flex flex-row gap-4">
              <div className="flex flex-col flex-1">
                <label className="  pb-1 text-[14px] font-semibold text-[#4d4c4c]">
                  Start Date*
                </label>
                <input
                  id="startDate"
                  type="date"
                  value={startDate || row?.recurrenceDetails?.startDate || ''}
                  onChange={e => handleDateChange(e, 'start')}
                  className="bg-slate-400/10 border border-input ps-2  focus:outline-none cursor-pointer placeholder:text-slate-700 text-xs pr-1 h-[42px] "
                  defaultValue={row ? row?.startDate : startDate}
                />
                {formik.touched.startDate && formik.errors.startDate ? (
                  <div className="text-red-500 text-xs">
                    {formik.errors.startDate}
                  </div>
                ) : null}
              </div>

              <div className="flex flex-col flex-1">
                <label className=" pb-1 text-[14px] font-semibold text-[#4d4c4c]">
                  End Date*
                </label>
                <input
                  id="endDate"
                  type="date"
                  onChange={e => handleDateChange(e, 'end')}
                  className="bg-slate-400/10 border border-input ps-2 h-[42px] focus:outline-none cursor-pointer placeholder:text-slate-700 text-xs pr-1"
                  value={endDate || row?.recurrenceDetails?.endDate || ''}
                />
                {formik.touched.endDate && formik.errors.endDate ? (
                  <div className="text-red-500 text-xs">
                    {date
                      ? 'please edit end date with respect to start date'
                      : formik.errors.endDate}
                  </div>
                ) : null}
              </div>
            </div>
            <div>
              <p className="mt-4 text-[#1f3a78] text-[16px] font-semibold">
                Repeat task on days of week:
              </p>
              <div className="flex gap-2 my-4">
                {repeatTaskDaysWorkState.map((item, index) => (
                  <button
                    key={index}
                    onClick={() => onChangeBtnState(index)}
                    className={`w-[40px] h-[40px] rounded-[5px] !px-0 flex justify-center items-center ${
                      isEveryDay || !recurrence.excludedDays.includes(index)
                        ? 'bg-[#6a6aec] text-[#ffffff] font-semibold text-[15px]'
                        : 'bg-[#f7f7fa] text-[#4d4c4c] font-semibold text-[15px]'
                    }`}>
                    {repeatTaskDaysWorkState[index]?.title}
                  </button>
                ))}
              </div>
            </div>
            <div className={`flex gap-2 items-center mb-4`}>
              <Checkbox
                checked={isEveryDay}
                // onCheckedChange={checked => {
                //   handleCheckEveryDay(checked);
                // }}
                onCheckedChange={handleCheckEveryDay}
                className="w-[24px] h-[24px] "
                checkClass={' !w-[20px] !h-[20px]'}
                chckColor={'data-[state=checked]:!bg-[#6a6aec]'}
              />
              <span className="text-[16px] font-medium text-[#4d4c4c]">
                Every Day
              </span>
            </div>
          </div>
        )}
      </Card>

      {/* Task Volume and Type */}
      <Card className="border-none shadow-none col-span-12 pt-4">
        <CardHeader className="flex flex-row items-center justify-between p-2 pl-0">
          <CardTitle className="text-sm font-bold">
            Task Volume and Value
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-2">
          <div className="flex flex-row gap-4">
            <div className="flex flex-col flex-1">
              <label className="text-xs 2xl:text-sm font-semibold pb-1 pl-0">
                Task Volume
              </label>
              <Input
                type="number"
                placeholder="Enter task volume"
                className="bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700 h-[42px]"
                name="taskVolume"
                value={Math.max(0, formik.values.taskVolume)}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              />
              {formik.touched.taskVolume && formik.errors.taskVolume ? (
                <div className="text-red-500 text-xs">
                  {formik.errors.taskVolume}
                </div>
              ) : null}
            </div>
            <div className="flex flex-col flex-1">
              <label className="text-xs 2xl:text-sm font-semibold pb-1 pl-0">
                Task Value
              </label>
              <div className="flex justify-start items-start h-[42px] taskval">
                {/* <CurrencyDropdown /> */}
                <CurrencyDropdown
                  onSelect={value => {
                    formik.setFieldValue('currency', value || '');
                  }}
                  value={formik.values.currency || 'INR'}
                  className="h-full currencdrop"
                />
                <Input
                  type="number"
                  placeholder="Enter amount"
                  className="bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700 h-[42px]"
                  name="amount"
                  value={Math.max(0, formik.values.amount)}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                />
              </div>

              {formik.touched.amount && formik.errors.amount ? (
                <div className="text-red-500 text-xs">
                  {formik.errors.amount}
                </div>
              ) : null}
            </div>
          </div>
        </CardContent>
      </Card>
      <Card className="border-none shadow-none col-span-12 pt-4">
        <CardHeader className="flex flex-row items-center justify-between p-2 pl-0">
          <CardTitle className="text-sm font-bold">Select Employee*</CardTitle>
        </CardHeader>
        <CardContent>
          <Popover className="relative">
            {/* Conditionally render the PopoverTrigger and PopoverContent */}
            {(selectedClient &&
              selectedClient.clientName === 'Default Client') ||
            (selectedClient &&
              Object?.keys(selectedClient?.employees).length === 0) ||
            selectedClient === null ? (
              <>
                <PopoverTrigger asChild>
                  <div className="relative">
                    <Input
                      type="text"
                      placeholder="Select Employee"
                      value={selectedEmployee?.fullName || employeeSearchQuery}
                      // value={row ? row.employeeName : ''}
                      disabled={selectedClient?.clientName !== 'Default Client'}
                      name="employeeId"
                      className="bg-slate-400/10 ps-3 h-[42px] placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                      onChange={e => {
                        setEmployeeSearchQuery(e.target.value);
                      }}
                    />
                    {selectedEmployee && (
                      <RxCross1
                        className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                        onClick={e => {
                          e.stopPropagation();
                          setSelectedEmployee(null);
                          setEmployeeSearchQuery('');
                          setSearchQuery(''); // Clear the search query
                          formik.setFieldValue('employeeId', ''); // Clear formik field
                        }}
                      />
                    )}
                    {formik.errors.employeeId && formik.touched.employeeId && (
                      <div className="text-red-500 text-xs">
                        {formik.errors.employeeId}
                      </div>
                    )}
                  </div>
                </PopoverTrigger>
                <PopoverContent
                  onOpenAutoFocus={e => e.preventDefault()}
                  align="start"
                  className="max-h-[200px] overflow-y-auto p-2 min-w-40">
                  {loadingEmployees ? ( // Show loading state
                    <div className="text-gray-500 text-xs">Loading...</div>
                  ) : displayedEmployees?.length > 0 ? (
                    displayedEmployees.map(employee => (
                      <div
                        key={employee._id}
                        className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer"
                        onClick={() => {
                          handleEmployeeSelect(employee);
                          setSearchQuery(employee.fullName); // Set search query to selected employee's name
                          formik.setFieldValue(
                            'employeeId',
                            employee.emp_id,
                            true
                          );
                        }}>
                        <CardTitle className="text-xs 2xl:text-sm">
                          {employee?.fullName}
                        </CardTitle>
                      </div>
                    ))
                  ) : (
                    <div className="text-gray-500 text-xs">
                      No employees found
                    </div>
                  )}
                </PopoverContent>
              </>
            ) : (
              <>
                {/* <PopoverTrigger asChild> */}
                <div className="relative">
                  <Input
                    type="text"
                    placeholder="Select Employee"
                    // value={selectedClient?.employees?.name}
                    value={
                      selectedClient
                        ? selectedClient?.employees?.name
                        : row
                          ? row.clientName
                          : searchQuery
                    }
                    disabled={selectedClient?.clientName !== 'Default Client'}
                    // readOnly={!!selectedClient?.employees?.length}
                    onChange={e => {
                      setSearchQuery(e.target.value);
                      formik.setFieldValue('clientId', ''); // Clear clientId on input change
                    }}
                    name="employeeId"
                    className="bg-slate-400/10 ps-3 h-[42px] placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                  />

                  {formik.errors.employeeId && formik.touched.employeeId && (
                    <div className="text-red-500 text-xs">
                      {formik.errors.employeeId}
                    </div>
                  )}
                </div>
                {/* </PopoverTrigger> */}
              </>
            )}
          </Popover>
        </CardContent>
      </Card>
      {row &&
      row.status !== 'Pending' &&
      row.status !== 'Finish' &&
      row !== 'undefined' ? (
        <Card className="border-none shadow-none col-span-12 pt-4">
          <CardHeader className="flex flex-row items-center justify-between p-2 pl-0">
            <CardTitle className="text-sm font-bold">Add Stages*</CardTitle>
          </CardHeader>
          <CardContent>
            <Popover
              className="relative"
              open={isPopoverOpen}
              onOpenChange={setIsPopoverOpen}>
              <PopoverTrigger asChild>
                <div className="relative">
                  <Input
                    type="text"
                    placeholder="Select Stages"
                    value={
                      selectedTag
                        ? selectedTag.tagName
                        : row
                          ? row.taskStage
                          : ''
                    }
                    // value={selectedEmployee ? selectedEmployee.fullName : '' }
                    readOnly
                    name="tagId"
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    className="bg-slate-400/10 ps-3 h-[42px] placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                  />
                  {selectedTag && (
                    <RxCross1
                      className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                      onClick={e => {
                        e.stopPropagation();
                        setSelectedTag(null);
                        // Clear the selected employee
                        formik.setFieldValue('tagId', '');
                      }}
                    />
                  )}
                  {formik.touched.tagId && formik.errors.tagId ? (
                    <div className="text-red-500 text-xs">
                      {formik.errors.tagId}
                    </div>
                  ) : null}
                </div>
              </PopoverTrigger>
              <PopoverContent
                onOpenAutoFocus={e => e.preventDefault()}
                align="start"
                className="max-h-[200px] overflow-y-auto p-2 min-w-40">
                {tagsResultedData.map(tags => (
                  <div
                    key={tags._id}
                    className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer"
                    onClick={() => {
                      setSelectedTag(tags);
                      formik.setFieldTouched('tagId', true);
                      formik.setFieldValue('tagId', tags._id);
                      formik.setFieldValue('tagName', tags.tagName);
                      setIsPopoverOpen(false);
                    }}>
                    <CardTitle className="text-xs 2xl:text-sm">
                      {tags.tagName}
                    </CardTitle>
                  </div>
                ))}
              </PopoverContent>
            </Popover>
          </CardContent>
        </Card>
      ) : null}
      <div
        className={`flex  pt-2 col-span-12 ${images.length > 0 || files?.length > 0 ? 'flex-col' : 'flex-row'}`}>
        {/* Attach Pictures */}
        <Card
          className={`border-none shadow-none col-span-3 relative flex  ${images.length > 2 || row?.images?.length > 2 ? 'lg:flex-col flex' : 'flex-row items-center'} ${images.length > 0 || row?.images?.length > 0 ? 'pt-8' : 'pt-0'} ${images.length > 4 || row?.images?.length > 4 ? '2xl:flex-col flex' : ''} `}>
          <span
            className={`text-sm font-bold absolute  ${images.length > 0 ? 'flex mt-[-190px]' : 'hidden'}  ${images.length > 2 || row?.images?.length > 2 ? 'flex mt-[-36px]' : ''} `}>
            Attach Pictures
          </span>
          <CardContent
            className={`flex gap-3 ${images.length > 4 ? 'flex-wrap' : ''}`}>
            {images.map(picture => (
              <div
                key={picture.url}
                className="flex flex-col w-44 border border-slate-100 rounded-md bg-white ">
                <img
                  src={picture.url}
                  alt={images.length > 0 ? 'Uploaded' : 'Existing'}
                  className="w-44 h-28 object-cover rounded-t-md"
                />
                <div className="description_ bg-slate-400/10 flex justify-between items-center gap-3 p-2 rounded-b-md">
                  <div className="flex justify-center items-center gap-1">
                    <img src={PDFIconImage} className="w-4 h-4" alt="pdf" />
                    {images.length > 0 && (
                      <span className="text-xs font-semibold">
                        {picture.description}
                      </span>
                    )}
                  </div>
                  <RxCross1
                    className="w-6 h-6 cursor-pointer bg-[#d6d6f0] p-[5px] rounded-full"
                    onClick={() => handleRemoveFile(picture.url, 'picture')}
                  />
                </div>
              </div>
            ))}
          </CardContent>

          <CardHeader
            className={`flex flex-row items-center justify-between  pl-0 flex-wrap gap-3 py-2 pr-2 h-[83px] ${images.length > 0 ? 'py-2 pr-2 pl-2' : ''} ${images.length > 2 ? 'py-2 pl-0 pr-2' : ''} `}>
            <Popover>
              <PopoverTrigger
                onClick={() => handleButtonClick('picture')}
                className="flex justify-center items-center gap-1 hover:bg-slate-100 p-0 w-36 rounded-sm border-[##6A6AEC] border h-full">
                <Button variant="ghost" size="sm" className="p-0">
                  <img src={AddPictureIcon} alt="Picture" className="w-7 h-7" />
                </Button>
                <span className="text-[#4F4E4E] font-medium text-xs">
                  Attach Pictures
                </span>
              </PopoverTrigger>
            </Popover>
            <input
              type="file"
              accept="image/*"
              multiple
              ref={pictureInputRef}
              style={{ display: 'none' }}
              onChange={e => handleFileChange(e, 'picture')}
            />
          </CardHeader>
        </Card>

        {/* Attach Files */}
        <Card
          className={`border-none shadow-none col-span-3 ${row?.files?.length > 3 ? 'lg:flex-col flex' : 'flex-row'} ${row?.files?.length > 0 ? 'pt-8' : 'pt-0'} ${row?.files?.length > 5 ? '2xl:flex-col flex' : ''}`}>
          {files.length > 0 && (
            <span className="text-sm font-bold mb-2">Attach Files</span>
          )}
          <CardContent
            className={`flex gap-3 w-full ${row?.files?.length > 3 ? 'flex-wrap' : ''}`}>
            {files.map(file => (
              <div
                key={file.url}
                className="flex items-center gap-2 bg-slate-100 w-fit p-1 px-2 rounded-sm">
                <span className="text-xs font-semibold">
                  {file.url.substring(file.url.lastIndexOf('/') + 1)}
                </span>
                <RxCross1
                  className="w-6 h-6 cursor-pointer p-[5px] rounded-full"
                  onClick={() => handleRemoveFile(file.url, 'file')}
                />
              </div>
            ))}
          </CardContent>
          <CardHeader
            className={`flex flex-row items-center justify-between p-2 flex-wrap gap-3 h-[83px] ${files.length > 0 || images.length > 0 ? 'py-2 pr-2 pl-0' : ''}`}>
            {/* <CardTitle className={`text-sm font-bold ${(row?.files?.length > 0)?"flex":'hidden'}`}>Attach Files</CardTitle> */}
            <Popover>
              {/* <PopoverTrigger>
              <Button
                variant="outline"
                size="sm"
                onClick={() => handleButtonClick('file')}>
                <img src={AddPictureIcon} alt="Add" className="w-4 h-4" />
              </Button>
            </PopoverTrigger> */}
              <PopoverTrigger
                onClick={() => handleButtonClick('file')}
                className="flex justify-center items-center gap-1 hover:bg-slate-100 p-0 w-36 rounded-sm border-[##6A6AEC] border h-full">
                <Button variant="ghost" size="sm" className="p-0">
                  <img src={AddFileIcon} alt="File" className="w-7 h-7" />
                </Button>
                <span className="text-[#4F4E4E] font-medium text-xs">
                  Attach File
                </span>
              </PopoverTrigger>
            </Popover>
            <input
              type="file"
              accept="application/pdf, application/vnd.ms-excel"
              multiple
              ref={fileInputRef}
              style={{ display: 'none' }}
              onChange={e => handleFileChange(e, 'file')}
            />
          </CardHeader>
        </Card>
      </div>

      <Card
        className={`border-none shadow-none col-span-12 lg:col-start-9 lg:col-span-4 grid grid-cols-5 gap-3`}>
        <DialogClose asChild>
          <Button className="bg-transparent text-[#6A6AEC] col-span-2">
            Discard
          </Button>
        </DialogClose>
        <DialogClose asChild>
          <Button
            type="submit"
            onClick={() => {
              formik.handleSubmit();
              setIsDialogOpen(false);
            }}
            // disabled={!formik.isValid}
            // disabled={!formik.isValid || formik.isSubmitting}
            disabled={
              !formik.isValid || formik.isSubmitting || !formik.values.clientId
            }
            className="bg-transparent bg-[#6A6AEC] col-span-3">
            {row ? 'Save Task' : ' Add Task'}
          </Button>
        </DialogClose>
      </Card>
    </div>
  );
};

export default AddNewTaskModal;
