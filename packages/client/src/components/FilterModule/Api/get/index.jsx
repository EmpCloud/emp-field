const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import GeoLocation from 'components/GeoLocationModule/GeoLocation';
import Cookies from 'js-cookie';

export const fetchUsers = async function (data) {
  return await axios.get(HOST + '/user/fetch' + data, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};

export const empUserById = async function (empId, data) {
  return await axios.post(
    // HOST + '/admin/admin-task?emp_id=' + empId,
    HOST + '/admin/admin-task?orderBy=updatedAt&sort=desc&emp_id=' + empId,
    {
      date: data,
    },
    {
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};

export const userFreguencyandGeoLocationUpdate = async function (
  empId,
  frequency = '',
  geoLocation = ''
) {
  return await axios.get(
    HOST +
      '/user/user-frequency-geolocation?emp_id=' +
      empId +
      '&frequency=' +
      frequency +
      '&geoLocation=' +
      geoLocation,
    {
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
export const empoyeeSearchedSuggestions = async searchQuery => {
  const response = await fetch(
    `${HOST}/user/fetch?limit=100&fullName=${searchQuery}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );

  return response.json();
};

export const clientSearchedSuggestions = async searchValue => {
  try {
    const response = await fetch(
      `${HOST}/admin/fetchClient?searchClients=${searchValue}`,
      {
        headers: {
          'x-access-token': Cookies.get('token'),
        },
      }
    );
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Fetch error:', error);
    throw error;
  }
};

export const StrageSearchedSuggestions = async searchValue => {
  try {
    const response = await fetch(
      `${HOST}/tags/getAdminTags?searchTags=${searchValue}`,
      {
        headers: {
          'x-access-token': Cookies.get('token'),
        },
      }
    );
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Fetch error:', error);
    throw error;
  }
};
