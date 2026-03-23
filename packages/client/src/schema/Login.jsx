const isRequiredErrorMessage = 'is required.';

export const emailSchema = {
  presence: { allowEmpty: false, message: isRequiredErrorMessage },
  email: { message: "Doesn't look like a valid email address" },
};

export const passwordSchema = {
  presence: { allowEmpty: false, message: isRequiredErrorMessage },
};
