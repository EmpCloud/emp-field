import axios from 'axios';
import Cookies from 'js-cookie';
const HOST = import.meta.env.VITE_USER_API;
import Clients from './../../../../page/user/Clients/index';

export const deleteEmployee = async function (empId) {
  try {
    // const response = await axios.delete(`${HOST}/admin/delete-user`, {
    const response = await axios.delete(`${HOST}/admin/soft-delete-user`, {
      data: {
        empIds: [empId],
      },
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error deleting employee:', error);
    throw error;
  }
};

export const deleteTask = async function (empId) {
  try {
    // const response = await axios.delete(`${HOST}/admin/delete-user`, {
    const response = await axios.delete(`${HOST}/admin/deleteTask`, {
      data: {
        taskId: [empId],
      },
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error deleting employee:', error);
    throw error;
  }
};

export const deleteClient = async function (ClientId) {
  try {
    const response = await axios.delete(`${HOST}/admin/deleteClient`, {
      data: {
        clientIds: [ClientId],
      },
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error deleting employee:', error);
    throw error;
  }
};

export const deleteEmployeePermanently = async function (empId) {
  try {
    const response = await axios.delete(`${HOST}/admin/permanent-delete-user`, {
      // const response = await axios.delete(`${HOST}/admin/soft-delete-user`, {
      data: {
        empIds: [empId],
      },
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error deleting employee:', error);
    throw error;
  }
};
export const updateGeoFencing = async function (empId, geoFencing, data) {
  return await axios.post(
    `${HOST}/reports/updateGeoFencing?emp_id=${empId}&geoFencing=${geoFencing}`,
    data,
    {
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
export const updateStageOrder = async function (order, id) {
  const data = {
    updatedStagesOrder: [
      {
        _id: id,
        order: order,
      },
    ],
  };
  return await axios.post(`${HOST}/tags/updateTagsOrder`, data, {
    headers: {
      'Content-Type': 'application/json',
      'x-access-token': Cookies.get('token'),
    },
  });
};
export const deleteStage = async function (stageId) {
  try {
    const response = await axios.delete(`${HOST}/tags/deleteTags`, {
      data: {
        tagIds: [stageId],
      },
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
    });
    return response.data;
  } catch (error) {
    console.error('Error deleting employee:', error);
    throw error;
  }
};
