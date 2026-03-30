import * as Yup from 'yup';

export const profileValidationSchema = Yup.object().shape({
  fullName: Yup.string().required('Full Name is required'),
  address1: Yup.string().required('Address 1 is required'),
  address2: Yup.string().optional(),
  country: Yup.string().required('Country is required'),
  state: Yup.string().required('State is required'),
  city: Yup.string().required('City is required'),
  phoneNumber: Yup.string()
    .matches(
      /^[0-9]{10,15}$/,
      'Mobile Number must be between 10 and 15 digits including country code'
    )
    .required('Mobile Number is required'),
  zipCode: Yup.string()
    .required()
    .matches(/^[0-9]{6}$/, 'Zipcode must be 6 digits'),
});
export const validationSchema = Yup.object().shape({
  clientName: Yup.string().required('Client Name is required'),
  // emailId: Yup.string(),
  emailId: Yup.string()
    .matches(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
      'Invalid email format. Please include a valid domain like .com or .in'
    )
    .nullable(),
  // clientProfilePic: Yup.string(),
  category: Yup.string().required('Category is required'),
  address1: Yup.string().required('Address 1 is required'),
  address2: Yup.string().optional(),
  // country: Yup.string().required('Country is required'),
  // state: Yup.string().required('State is required'),
  // selectEmployee: Yup.string(),
  // city: Yup.string().required('City is required'),
  employeeIds: Yup.string().required('Employee is required'),
  countryCode: Yup.string()
    .required('Country Code is required')
    .test(
      'not-empty',
      'Country Code is required',
      value => value && value.trim() !== ''
    ),
  contactNumber: Yup.string()
    .matches(
      /^[0-9]{10,15}$/,
      'Mobile Number must be between 10 and 15 digits including country code'
    )
    .required('Mobile Number is required'),
  // zipCode: Yup.string().optional().matches(/^[0-9]{6}$/, 'Zipcode must be 6 digits'),
});

export const adminUpdatePasswordSchema = Yup.object().shape({
  oldPassword: Yup.string()
    .matches(
      /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{6,40}$/,
      'Must be 6-40 chars, with digit, upper & lower case, & special char.'
    )
    .required('Old password is required'),
  newPassword: Yup.string()
    .matches(
      /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{6,40}$/,
      'Must be 6-40 chars, with digit, upper & lower case, & special char.'
    )
    .notOneOf(
      [Yup.ref('oldPassword')],
      'New password must be different from old password'
    )
    .required('New password is required'),
  confirmNewPassword: Yup.string()
    .oneOf([Yup.ref('newPassword'), null], 'New passwords must match')
    .required('Confirm password is required'),
});
