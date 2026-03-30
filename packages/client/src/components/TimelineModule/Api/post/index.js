const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const getLiveTrackingTimeLineData = async function (date, empolyeeId) {
  const dateFilter = {
    date: date,
  };
  return await axios.post(
    HOST +
      `/admin/getUserTimeLine?emp_id=${empolyeeId}&orderBy=geologs.time&sort=desc`,
    dateFilter,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};
