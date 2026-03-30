const isRequiredErrorMessage = 'is required.';

export const requiredSchema = {
  presence: { allowEmpty: false, message: isRequiredErrorMessage },
};

export const emailSchema = {
  presence: { allowEmpty: false, message: isRequiredErrorMessage },
  email: { message: "Doesn't look like a valid email address" },
};

export const passwordSchema = {
  presence: { allowEmpty: false, message: isRequiredErrorMessage },
  format: {
    pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{6,40}$/,
    message:
      'must be 6-40 characters long with at least one digit, uppercase letter, lowercase letter, and special character.',
  },
};

export const confirmPasswordSchema = {
  presence: { allowEmpty: false, message: isRequiredErrorMessage },
  equality: {
    attribute: 'password',
    message: 'does not match the password.',
  },
};
