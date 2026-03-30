const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const getLocations = async ({ pageParam = 0, limit = 50, data }) => {
  return await axios.get(
    `${HOST}/admin/getLocation?skip=${pageParam}&limit=${limit}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
        'Content-Type': 'application/json',
      },
    }
  );
};
export const getDepartments = async ({ pageParam = 0, limit = 50, data }) => {
  return await axios.get(
    `${HOST}/admin/getDepartment?skip=${pageParam}&limit=${limit}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
        'Content-Type': 'application/json',
      },
    }
  );
};
export const getRoles = async ({ pageParam = 0, limit = 50, data }) => {
  return await axios.get(`${HOST}/roles/get?skip=${pageParam}&limit=${limit}`, {
    headers: {
      'x-access-token': Cookies.get('token'),
      'Content-Type': 'application/json',
    },
  });
};
