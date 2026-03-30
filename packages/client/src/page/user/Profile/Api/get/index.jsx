const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const fetchProfile = async function () {
  return await axios.get(HOST + '/admin/admin-fetch', {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
