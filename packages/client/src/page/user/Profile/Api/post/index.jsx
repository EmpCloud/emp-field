const HOST = import.meta.env.VITE_USER_API;
import axios from 'axios';
import Cookies from 'js-cookie';

export const adminImgUpload = async imageUrl => {
  const cookie = Cookies.get('token');

  try {
    let data = new FormData();
    data.append('files', imageUrl);

    let config = {
      method: 'post',
      maxBodyLength: Infinity,
      url: HOST + '/admin/adminUploadProfileImage',
      headers: { 'x-access-token': cookie },
      data: data,
    };

    let res = await axios.request(config);
    alert(res?.data?.body?.message);
    const returnedUrl = res?.data?.body?.data?.profileURL;
    return returnedUrl;
  } catch (error) {
    // alert(error.message);
  }
};
