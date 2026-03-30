import React, { useState } from 'react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { ChevronDown, Search, Upload } from 'lucide-react';
import { Modal } from 'components/Modal';
// import AddNewTaskModal from 'components/UIElements/Modals/AddNewTaskModal';
// import AddNewStageTaskModal from 'components/UIElements/Modals/AddStageTasksModal';
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import addTaskIcon from 'assets/images/add-task.png';
import { StageModal } from 'components/StageModal';
import StageTaskTable from 'components/StageTaskModule/StageTaskTable';
import StageTaskSearch from 'components/StageTaskModule/StageTaskSearch';

const StageTasks = () => {
  const [pageSize, setPageSize] = useState(10);
  const [searchValue, setSearchValue] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const data = [
    { id: 1, name: 'John Doe', age: 28 },
    { id: 2, name: 'Jane Smith', age: 34 },
    { id: 3, name: 'Michael Brown', age: 45 },
  ];
  return (
    <div className=" col-span-12 bg-white rounded-md">
      <StageTaskSearch
        searchQuery={searchQuery}
        setSearchQuery={setSearchQuery}
        searchValue={searchValue}
        setSearchValue={setSearchValue}
      />
      <StageTaskTable searchQuery={searchQuery} searchValue={searchValue} />
    </div>
  );
};

export default StageTasks;
