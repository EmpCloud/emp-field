const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const getEmployeeStatusDetails = async function (
  employeeStatus,
  searchValue,
  pageParam = 0,
  limit = 10,
  data
) {
  return await axios.post(
    `${HOST}/admin/getEmployeeDetails?employeeStatus=${employeeStatus}&search=${searchValue}&skip=${pageParam * limit}&limit=${limit}`,
    data,
    {
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
