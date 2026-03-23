import moment from 'moment';

/* Here status 1 means present and 0 means absent.
Here holiday_status 1 means holiday and 0 means not holiday)
Here leave_status 1 means leave and 0 means not leave)
Here day_off true means Week-Off and false means not Week-Off */
export const TransformAttendanceData = (rawData, monthLength) => {
  return rawData.map(item => {
    let totalDays = 0,
      P = 0,
      A = 0,
      L = 0,
      H = 0,
      O = 0;
    const date = {};

    if (item.attendance !== 'No Data!') {
      item.attendance?.forEach((emp, idx) => {
        const day = idx + 1;
        const isPastDate = moment().isAfter(emp.date);
        let marker = '--';
        const cap = text =>
          text?.charAt(0)?.toUpperCase() +
          text?.charAt(text.lastIndexOf(' ') + 1)?.toUpperCase();

        totalDays++;

        if (emp.status === 1 && isPastDate) {
          marker = 'P';
          P++;
        } else if (emp.holiday_status === 1 && emp.holiday_name) {
          marker = {
            text: cap(emp.holiday_name),
            tooltip: emp.holiday_name,
          };
          H++;
        } else if (emp.day_off === false) {
          marker = 'WO';
          O++;
        } else if (emp.half_day === 1 && isPastDate) {
          marker =
            emp.half_day_status === 1 && emp.half_day_leave
              ? cap(emp.half_day_leave)
              : emp.half_day_status === 1 && emp.leave_type !== 0
                ? cap(emp.leave_name)
                : 'HD';

          if (marker === 'HD') A += 0.5;
          else L += 0.5;

          P += 0.5;
        } else if (emp.half_day_status === 1 && emp.half_day_leave) {
          marker = cap(emp.half_day_leave);
          L += 0.5;
          A += 0.5;
        } else if (
          emp.leave_type !== 0 &&
          emp.leave_type !== undefined &&
          emp.holiday_status === 0 &&
          emp.half_day_status !== 1
        ) {
          marker = cap(emp.leave_name);
          L++;
        } else if (
          item.date_join &&
          new Date(item.date_join) > new Date(emp.date)
        ) {
          marker = '--';
        } else if (moment().isBefore(emp.date)) {
          marker = '--';
        } else if (
          Number(item.is_attendance_override) === 1 &&
          Number(item.tracking_rule_type) === 3 &&
          isPastDate
        ) {
          marker = 'P';
          P++;
        } else {
          marker = 'A';
          A++;
        }

        if (!date[day]) date[day] = {};
        date[day].log = { marker };
      });
    }

    return {
      id: item.id,
      name: item.name,
      department: item.department,
      location: item.location,
      emp_code: item.emp_code,
      T: totalDays,
      P,
      A,
      L,
      H,
      O,
      date,
    };
  });
};
