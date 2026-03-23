const HOST = import.meta.env.VITE_USER_API;
import Cookies from 'js-cookie';
export const fetechEmpoyeeRoles = async ({ pageParam = 1 }) => {
  const limit = 20;
  const res = await fetch(
    `${HOST}/roles/get?_page=${pageParam}&_limit=${limit}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
  return res.json();
};

export const fetechEmpoyeeDepartment = async ({ pageParam = 1 }) => {
  const limit = 20;
  const res = await fetch(
    `${HOST}/admin/getDepartment?_page=${pageParam}&_limit=${limit}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
  return res.json();
};

export const fetechEmpoyeeLocation = async ({ pageParam = 1 }) => {
  const limit = 20;
  const res = await fetch(
    `${HOST}/admin/getLocation?_page=${pageParam}&_limit=${limit}`,
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
  return res.json();
};
