// export const nameSchema = {
//     format: {
//         pattern: '^(?=.{3,19}$).*$',
//      message: ' must be 3-19 characters and alphabetic',

//     }
//   }
export const nameSchema = {
  format: {
    pattern: '^([a-zA-Z0-9]{0,19})?$',
    message: ' must be alphabetic or numeric and up to 19 character',
  },
};
