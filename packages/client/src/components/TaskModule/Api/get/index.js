const HOST = import.meta.env.VITE_USER_API;
import Cookies from 'js-cookie';

export const clientSearchedSuggestions = async searchQuery => {
  const response = await fetch(
    `${HOST}/admin/fetchClient?limit=5000&searchClients=${searchQuery}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );

  return response.json();
};
export const getTaskDetails = async (
  generalSearch,
  searchEmployee,
  searchClients,
  date,
  limit = 10,
  skip = 0
) => {
  generalSearch == null ? '' : generalSearch;
  const response = await fetch(
    `${HOST}/admin/fetchTask?limit=${limit}&skip=${skip}&generalSearch=${generalSearch}&searchEmployee=${searchEmployee}&searchClients=${searchClients}&date=${date}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );

  return response.json();
};
