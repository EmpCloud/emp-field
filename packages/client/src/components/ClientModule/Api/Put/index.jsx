const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const updateClient = async function (_id, clientData) {
  try {
    const response = await axios.put(`${HOST}/admin/updateClient`, clientData, {
      headers: {
        'x-access-token': Cookies.get('token'),
        'Content-Type': 'application/json', // Ensure correct content type
      },
      params: {
        clientId: _id,
      },
    });
    return response; // Return the response
  } catch (error) {
    console.error('Error updating client:', error);
    throw error; // Rethrow the error to be handled by the caller
  }
};
