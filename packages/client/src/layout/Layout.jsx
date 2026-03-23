import DisplayTitles from 'hooks/UserTitles';
import { Outlet } from 'react-router-dom';

export const Layout = () => {
  DisplayTitles();
  return (
    <>
      <main>
        <Outlet />
      </main>
    </>
  );
};
