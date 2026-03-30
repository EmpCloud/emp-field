const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const editProfile = async function (data) {
  return await axios.put(HOST + '/admin/update', data, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
export const updateAdminPassword = async function (data) {
  return await axios.put(HOST + '/admin/update-password', data, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
