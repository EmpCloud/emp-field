import { useState } from 'react';
import AttendanceFilter from './AttendanceFilter';
import { AttendanceTable } from './AttendanceTable';

const index = () => {
  const [selectedDate, setSelectedDate] = useState(new Date());
  // const [filters, setFilters] = useState({
  //   employeeLocation: '',
  //   employeeDepartment: '',
  //   employeeDesignation: ''
  // });

  return (
    <>
      <AttendanceFilter
        date={selectedDate}
        setDate={setSelectedDate}
        // setFilters={setFilters}
        // filters={filters}
      />
      <AttendanceTable
        selectedDate={selectedDate}
        // {...filters}
      />
    </>
  );
};

export default index;
