import Cookies from 'js-cookie';
import axios from 'axios';

const HOST = import.meta.env.VITE_USER_API;
export const createStage = async function (clientData) {
  try {
    // console.log('Creating stage with data:', clientData);
    const response = await axios.post(`${HOST}/tags/createTags`, clientData, {
      headers: {
        'x-access-token': Cookies.get('token'),
        'Content-Type': 'application/json',
      },
    });
    // console.log('API Response:', response); // Log response
    return response.data;
  } catch (error) {
    // console.error('Error creating stage:', error.response ? error.response.data : error.message);
    throw error;
  }
};
