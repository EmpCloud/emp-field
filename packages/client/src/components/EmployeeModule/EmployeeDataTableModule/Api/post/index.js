const HOST = import.meta.env.VITE_USER_API;
import Cookies from 'js-cookie';
import axios from 'axios';

export const employeeSearchedKeyWord = async (
  searchEmployeeKeyBoard,
  selectedRole,
  selectedLocation,
  selectedDepartment,
  deletedUsers,
  { pageParam = 0, limit = 10 }
) => {
  const baseUrl = `${HOST}/admin/getAllAdminFieldTrackingUsers?skip=${pageParam}&limit=${limit}&`;
  const url = searchEmployeeKeyBoard
    ? `${baseUrl}searchQuery=${searchEmployeeKeyBoard}`
    : baseUrl;

  // Create the request body, excluding keys if their values include "All"
  const requestBody = { deleted: deletedUsers };

  if (selectedRole && !selectedRole.includes('All')) {
    requestBody.role = selectedRole;
  }

  if (selectedLocation && !selectedLocation.includes('All')) {
    requestBody.location = selectedLocation;
  }

  if (selectedDepartment && !selectedDepartment.includes('All')) {
    requestBody.department = selectedDepartment;
  }

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': Cookies.get('token'),
      },
      body: JSON.stringify(requestBody),
    });

    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    return response.json();
  } catch (error) {
    throw new Error('Error fetching data');
  }
};

export const restoreEmployees = async function (empId) {
  return await axios.post(
    HOST + '/Admin/restore-softDelete-Users',
    {
      empIds: [empId],
    },
    {
      headers: {
        'x-access-token': Cookies.get('token'),
      },
    }
  );
};

// const HOST = import.meta.env.VITE_USER_API;
// import Cookies from 'js-cookie';

// const employeeSearchedKeyWord = async (
//   searchEmployeeKeyBoard,
//   selectedRole ,
//   selectedLocation,
//   selectedDepartment
// ) => {
//   const baseUrl = `${HOST}/admin/getAllAdminFieldTrackingUsers?limit=250&`;
//   const url = searchEmployeeKeyBoard
//     ? `${baseUrl}searchQuery=${searchEmployeeKeyBoard}`
//     : baseUrl;

//   try {
//     const response = await fetch(url, {
//       method: 'POST',
//       headers: {
//         'Content-Type': 'application/json',
//         'x-access-token': Cookies.get('token'),
//       },
//       body: JSON.stringify({
//         role: selectedRole,
//         location:selectedLocation,
//         department:selectedDepartment
//       }),
//     });

//     if (!response.ok) {
//       throw new Error('Network response was not ok');
//     }

//     return response.json();
//   } catch (error) {
//     throw new Error('Error fetching data');
//   }
// };
// export default employeeSearchedKeyWord;
