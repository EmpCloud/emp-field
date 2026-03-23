const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const fetchAllAdminTaskCount = async function () {
  return await axios.get(HOST + '/admin/allTaskStats', {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};

// export const dashboardEmployeesTrackingData = async function (
//   employeeId,
//   data
// ) {
//   return await axios.post(
//     HOST + `/admin/allUsers-Tracking-Data?employee_id=${employeeId}`,
//     data,
//     {
//       headers: {
//         'x-access-token': Cookies.get('token'),
//       },
//     }
//   );
// };

export const dashboardEmployeesTrackingData = async function (
  pageParam,
  employeeId,
  date,
  employeeStatus
) {
  // Initial limit and skip
  const initialLimit = 10;
  const increment = 10;

  const data = {
    date: date,
  };

  if (employeeStatus == 'Check In') {
    data.checkIn = true;
  }

  if (employeeStatus == 'Check Out') {
    data.checkOut = true;
  }

  // Calculate limit and skip based on pageParam
  const limit = initialLimit + (pageParam - 1) * increment;
  const skip = (pageParam - 1) * increment;

  return await axios.post(
    HOST +
      `/admin/allUsers-Tracking-Data?employee_id=${employeeId}&limit=${limit}&skip=${skip}`,
    data,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};

export const fetchAllDashboardStats = async function () {
  return await axios.get(HOST + '/admin/get-dashboard-stats', {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
export const fetchAverageWorkingHours = async function (data) {
  return await axios.get(HOST + `/admin/average-working-hours?${data}`, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
export const usersLastLocationDetails = async function (params) {
  return await axios.post(HOST + '/admin/users-LocationDetails', params, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
