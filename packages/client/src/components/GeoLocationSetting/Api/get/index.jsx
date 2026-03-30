const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const orgLocationDetails = async function () {
  return await axios.get(HOST + '/hrmsAdmin/org-location-list', {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
