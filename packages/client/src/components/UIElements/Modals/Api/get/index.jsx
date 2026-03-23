const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const fetchEmpUsers = async function ({ pageParam = 0, limit = 10 }) {
  return await axios.post(
    HOST + '/admin/allOrgEmployee',
    {
      skip: pageParam,
      limit: limit,
    },
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
export const adminFetchClient = async function () {
  return await axios.get(HOST + `/admin/fetchClient?limit=100`, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};

export const fetchTags = async function () {
  try {
    const response = await axios.get(HOST + '/tags/getAdminTags', {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    });
    return response;
  } catch (error) {
    throw new Error('Failed to fetch tags');
  }
};

export const getEmployeeLocationById = async function (_id) {
  return await axios.get(
    HOST + `/hrmsAdmin/getEmployeeLocation?employeeId=${_id}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
