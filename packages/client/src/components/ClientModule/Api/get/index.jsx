const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const fetchClients = async function ({ pageParam = 0, limit = 10 }) {
  return await axios.get(
    HOST + `/admin/fetchClient?skip=${pageParam}&limit=${limit}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
