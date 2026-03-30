import * as Yup from 'yup';

export const AddStageSchema = Yup.object().shape({
  tagName: Yup.string().required('Stage Name is required').min('1').max('50'),
  color: Yup.string().required('Color Picker is required'),
});
