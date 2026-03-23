import Cookies from 'js-cookie';
import axios from 'axios';
import { getTaskStatus } from 'components/ReportModule/Employee/TaskList';
const HOST = import.meta.env.VITE_USER_API;

export const employeeReportTaskStatus = async function (toDay, dateRange, id) {
  let data = {
    startDate: dateRange,
    endDate: toDay,
  };
  // return await axios.post(HOST + '/reports/taskStatus?employee_Id=29372&startDate=2024-02-28%20&endDate=2024-02-28%20', {
  return await axios.post(
    HOST + '/reports/taskStatus?employee_Id=' + id,

    data,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};

export const employeeReportTaskStage = async function (
  toDay,
  dateRange,
  employeeId
) {
  let data = {
    startDate: dateRange,
    endDate: toDay,
  };
  //staging-emp-api-m.empmonitor.com/v1/reports/taskStages?employee_Id=29372%20&startDate=2024-02-28&endDate=2024-09-03
  // return await axios.post(HOST + '/reports/taskStages?employee_Id=29372&startDate=2024-02-28%20&endDate=2024-02-28%20', {
  https: return await axios.post(
    HOST + '/reports/taskStages?employee_Id=' + employeeId,

    data,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};

export const employeeReportStats = async function (
  id,
  startDate,
  endDate,
  filter
) {
  // console.log(filter, 'filter==>');
  // let data = {
  //   employee_Id: id,

  // };
  let data = {
    employee_Id: id,
    taskStatus: getTaskStatus(filter?.status) || null,
    taskStage: filter?.stage || null,
    startDate: startDate,
    endDate: endDate,
    Volume: {
      minTaskVolume: filter?.taskVolume?.min || null,
      maxTaskVolume: filter?.taskVolume?.max || null,
    },
    Amount: {
      minAmount: filter?.taskValue?.min || null,
      maxAmount: filter?.taskValue?.max || null,
    },
  };
  //staging-emp-api-m.empmonitor.com/v1/reports/taskStages?employee_Id=29372%20&startDate=2024-02-28&endDate=2024-09-03
  // return await axios.post(HOST + '/reports/taskStages?employee_Id=29372&startDate=2024-02-28%20&endDate=2024-02-28%20', {
  https: return await axios.post(
    HOST +
      '/reports/userStats' +
      '?startDate=' +
      startDate +
      '&endDate=' +
      endDate,
    data,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};

// export const employeeClientDetails = async function (endDate,startDate,id) {

//   console.log(id)
//   //staging-emp-api-m.empmonitor.com/v1/reports/taskStages?employee_Id=29372%20&startDate=2024-02-28&endDate=2024-09-03
//   return await axios.post( `${HOST}/reports/clientDetails?employee_Id=${encodeURIComponent(id)}&startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`, data,{
//     headers: {
//       'x-access-token': Cookies.get('token'),
//     },
//   });
// };
export const employeeClientDetails = async (endDate, startDate, id, filter) => {
  let data = {
    employee_Id: id,
    taskStatus: getTaskStatus(filter?.status) || 0,
    taskStage: filter?.stage || '',
    startDate: startDate,
    endDate: endDate,
    Volume: {
      minTaskVolume: filter?.taskVolume?.min,
      maxTaskVolume: filter?.taskVolume?.max || '',
    },
    Amount: {
      minAmount: filter?.taskValue?.min,
      maxAmount: filter?.taskValue?.max || '',
    },
  };
  if (!endDate || !startDate || !id) throw new Error('Invalid parameters');
  const url = `${HOST}/reports/clientDetails?employee_Id=${encodeURIComponent(id)}&skip=0&limit=500&exportClient=false`;
  try {
    const response = await axios.post(url, data, {
      headers: { 'x-access-token': Cookies.get('token') },
    });
    return response.data;
  } catch (error) {
    console.error('API error:', error);
    throw error;
  }
};

export const employeeAttendanceDetails = async (endDate, startDate, id) => {
  const data = {
    empId: id,
    start_date: startDate,
    end_date: endDate,
    skip: 0,
    limit: 500,
    allData: true,
  };
  if (!endDate || !startDate || !id) throw new Error('Invalid parameters');
  const url = `${HOST}/reports/getIndividualAttendanceData`;
  try {
    const response = await axios.post(url, data, {
      headers: { 'x-access-token': Cookies.get('token') },
    });
    return response.data;
  } catch (error) {
    console.error('API error:', error);
    throw error;
  }
};

export const employeeDistanceDetails = async (endDate, startDate, id) => {
  if (!endDate || !startDate || !id) throw new Error('Invalid parameters');

  const data = {
    startDate: startDate,
    endDate: endDate,
  };
  const url = `${HOST}/reports/distanceTraveled?employee_Id=${encodeURIComponent(id)}&skip=0&limit=500&exportDistanceTraveled=false`;
  try {
    const response = await axios.post(url, data, {
      headers: { 'x-access-token': Cookies.get('token') },
    });
    return response.data;
  } catch (error) {
    console.error('API error:', error);
    throw error;
  }
};
export const employeetasksDetails = async ({
  skip = 0,
  limit = 10,
  startDate,
  endDate,
  data,
}) => {
  return await axios.post(
    `${HOST}/reports/taskListDetails?skip=${skip}&limit=${limit}&exportTaskDetails=false`,
    data,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
        'Content-Type': 'application/json',
      },
    }
  );
};
