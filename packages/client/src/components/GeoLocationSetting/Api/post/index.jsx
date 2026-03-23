const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const UpdateOrgLocationDetails = async function (data) {
  return await axios.post(HOST + '/hrmsAdmin/location-update', data, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
