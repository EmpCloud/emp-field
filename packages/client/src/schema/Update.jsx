const isRequiredErrorMessage = 'is required.';

export const passwordSchema = {
  presence: { allowEmpty: false, message: isRequiredErrorMessage },
  format: {
    pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{6,40}$/,
    message:
      'must be 6-40 chars, with digit, upper & lower case, & special char.',
  },
};

export const confirmPasswordSchema = {
  equality: 'newPassword',
  presence: { allowEmpty: false, message: isRequiredErrorMessage },
};
