const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const addEmpUsers = async function (data) {
  return await axios.post(HOST + '/open-user/import-users', data, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
export const uploadFiles = async imageUrl => {
  const cookie = Cookies.get('token');

  try {
    let data = new FormData();
    data.append('files', imageUrl);

    let config = {
      method: 'post',
      maxBodyLength: Infinity,
      url: HOST + '/task/uploadTask-files',
      headers: { 'x-access-token': cookie },
      data: data,
    };

    let res = await axios.request(config);
    const returnedUrl = res.data.data.filesUrls[0].url;
    return returnedUrl;
  } catch (error) {
    alert(error.message);
  }
};
export const fetchEmployeeDetails = async searchQuery => {
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

export const createTasks = async (employeeId, tasksData) => {
  return await axios.post(
    `${HOST}/admin/createTask?employeeId=${employeeId}`,
    tasksData,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
        'Content-Type': 'application/json',
      },
    }
  );
};
export const editTasksDetails = async function (data) {
  return await axios.put(HOST + '/admin/updateTask', data, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
export const updateEmployeeLocationfencing = async function (data) {
  return await axios.post(HOST + '/hrmsAdmin/updateEmployeeLocation', data, {
    headers: {
      'x-access-token': Cookies.get('token'),
    },
  });
};
