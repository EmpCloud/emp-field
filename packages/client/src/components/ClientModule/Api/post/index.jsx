import Cookies from 'js-cookie';
import axios from 'axios';
const HOST = import.meta.env.VITE_USER_API;

export const createClient = async function (clientData) {
  try {
    const response = await axios.post(
      `${HOST}/admin/createClient`,
      clientData, // Pass clientData as the body
      {
        headers: {
          'x-access-token': Cookies.get('token'),
        },
      }
    );
    return response;
  } catch (error) {
    console.error('Error creating client:', error);
    throw error;
  }
};
