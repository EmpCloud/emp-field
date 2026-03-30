import UserIcon from 'assets/images/employeeTable/user.png';

const TableDetail = () => {
  return (
    <div className="flex justify-start items-center gap-1">
      <button className="action-icon">
        {' '}
        <img src={UserIcon} alt="setting" />{' '}
      </button>
    </div>
  );
};

export default TableDetail;
