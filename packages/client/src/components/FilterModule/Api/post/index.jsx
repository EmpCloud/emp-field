const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const updateFrequencyRadius = async function (empId, orgId, data) {
  return await axios.post(
    `${HOST}/profile/Update-Emp-Mot-Frequency-Radius?Employee_id=${empId}&Employee_orgId=${orgId}`,
    data,
    {
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
export const updateSnapDetails = async function (empId, orgId, data) {
  return await axios.post(
    `${HOST}/profile/update-snap-details?Employee_id=${empId}&Employee_orgId=${orgId}`,
    data,
    {
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
