import * as Yup from 'yup';

export const getAddTasksSchema = isRepeattask =>
  Yup.object().shape({
    taskName: Yup.string().required('Task Name is required'),
    clientId: Yup.string().required('Client is required'),
    taskDescription: Yup.string().required('Task Description is required'),
    startTime: Yup.date().required('Start Time is required'),
    endTime: Yup.date()
      .required('End Time is required')
      .min(Yup.ref('startTime'), 'End Time must be after Start Time'),
    taskVolume: Yup.number().max(
      9999999999,
      'Task Volume cannot exceed 10 digits'
    ),
    currency: Yup.string(),
    amount: Yup.number().max(9999999999, 'Amount cannot exceed 10 digits'),
    employeeId: Yup.string().required('Employee is required'),
    recurrence: isRepeattask
      ? Yup.object().shape({
          startDate: Yup.date().nullable().required('Start Date is required'),
          endDate: Yup.date().nullable().required('End Date is required'),
          excludedDays: Yup.array().of(Yup.number()),
        })
      : Yup.mixed().nullable(),
  });
