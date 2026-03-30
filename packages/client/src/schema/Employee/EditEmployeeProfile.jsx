import * as Yup from 'yup';

export const editEmployeeProfile = Yup.object().shape({
  firstName: Yup.string().required('First Name is required'),
  lastName: Yup.string().required('First Name is required'),
});
