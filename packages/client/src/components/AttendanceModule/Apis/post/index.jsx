const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';
export const getAllEmployeeAttendanceData = async ({
  date,
  page,
  limit,
  searchTerm,
}) => {
  return await axios.post(
    HOST + `/admin/allEmployeesAttendance?skip=${page * 10}&limit=${limit}`,
    { date, searchTerm },
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
