import { setIsSidebarOpen, useUIController } from 'context/context';

const OverlayBackdrop = () => {
  const [controller, dispatch] = useUIController();
  const { isSidebarOpen } = controller;

  return (
    <>
      {isSidebarOpen && (
        <div
          className="absolute inset-0 bg-black/80 backdrop-blur z-[2] lg:hidden"
          onClick={() => setIsSidebarOpen(dispatch, false)}></div>
      )}
    </>
  );
};

export default OverlayBackdrop;
