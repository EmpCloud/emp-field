const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const getconsolidatedReports = async ({
  pageParam = 0,
  limit = 10,
  searchQuery = '',
  orderBy = 'fullName',
  sort = 'asc',
  data = {},
}) => {
  const url = `${HOST}/reports/get-Consolidated-Reports?skip=${pageParam}&limit=${limit}&searchQuery=${encodeURIComponent(searchQuery)}&orderBy=${encodeURIComponent(orderBy)}&sort=${encodeURIComponent(sort)}`;

  return await axios.post(url, data, {
    headers: {
      'x-access-token': Cookies.get('token'),
      'Content-Type': 'application/json',
    },
  });
};

export const getReportEmployees = async function (data) {
  const employeeId = {
    employee_Id: data,
  };

  return await axios.post(HOST + '/reports/getUserDetails', employeeId, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
