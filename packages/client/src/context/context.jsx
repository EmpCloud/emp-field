import { createContext, useContext, useReducer, useMemo } from 'react';

const UIController = createContext();

function reducer(state, action) {
  switch (action.type) {
    case 'IS_SIDEBAR_OPEN': {
      return { ...state, isSidebarOpen: action.value };
    }
    default: {
      throw new Error(`Unhandled action type: ${action.type}`);
    }
  }
}

function UIControllerProvider({ children }) {
  const initialState = {
    isSidebarOpen: false,
  };

  const [controller, dispatch] = useReducer(reducer, initialState);

  const value = useMemo(() => [controller, dispatch], [controller, dispatch]);

  return (
    <UIController.Provider value={value}>{children}</UIController.Provider>
  );
}

function useUIController() {
  const context = useContext(UIController);

  if (!context) {
    throw new Error(
      'useUIController should be used inside the UIControllerProvider.'
    );
  }

  return context;
}

const setIsSidebarOpen = (dispatch, value) => {
  dispatch({ type: 'IS_SIDEBAR_OPEN', value });
};

export { UIControllerProvider, useUIController, setIsSidebarOpen };
