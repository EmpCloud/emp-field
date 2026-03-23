const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const updateStage = async function (id, payload) {
  try {
    if (typeof id !== 'string' && typeof id !== 'number') {
      throw new Error('Invalid ID');
    }

    const response = await axios.post(
      `${HOST}/tags/updateTags`,
      {
        tagId: id,
        ...payload,
      },
      {
        headers: {
          'x-access-token': Cookies.get('token'),
          'Content-Type': 'application/json',
        },
      }
    );

    return response;
  } catch (error) {
    console.error(
      'Error updating stage:',
      error.response ? error.response.data : error.message
    );
    throw error;
  }
};
