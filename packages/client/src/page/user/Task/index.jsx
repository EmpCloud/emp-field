import TaskSearch from 'components/TaskModule/TaskSearch';
import TaskTable from 'components/TaskModule/TaskTable';
import { useState } from 'react';

const Task = () => {
  const [employeeIdTask, setTaskEmployeEmpId] = useState('');
  const [clientIdTask, setClientIdTask] = useState('');
  const [dateFilterTask, setdateFilterTask] = useState(null);
  return (
    <>
      <TaskSearch
        setTaskEmployeEmpId={setTaskEmployeEmpId}
        setClientIdTask={setClientIdTask}
        setdateFilterTask={setdateFilterTask}
      />
    </>
  );
};

export default Task;
