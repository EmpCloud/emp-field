import { DateRangePicker } from 'rsuite';
import 'rsuite/dist/rsuite.min.css'; // Import RSuite styles
import moment from 'moment';
import Cookies from 'js-cookie';

const LibDateRange = ({ onSelect }) => {
  const createdAt = Cookies.get('createdAt');

  const minDate = moment(createdAt).toDate();
  const maxDate = moment().toDate();

  const handleDateChange = value => {
    if (value && value.length === 2) {
      const [startDate, endDate] = value;
      onSelect({
        startDate: moment(startDate).format('YYYY-MM-DD'),
        endDate: moment(endDate).format('YYYY-MM-DD'),
      });
    }
  };

  const isDisabledDate = date => {
    return date < minDate || date > maxDate;
  };

  return (
    <div className="date-range-container">
      <DateRangePicker
        className="responsive-date-range-picker"
        onChange={handleDateChange}
        shouldDisableDate={isDisabledDate}
      />
    </div>
  );
};

export default LibDateRange;
