import React, { createContext, useContext, useState } from 'react';

const EmployeeReportFiltersContext = createContext();

export const useEmployeeReportFilters = () => {
  return useContext(EmployeeReportFiltersContext);
};

export const EmployeeReportFiltersProvider = ({ children }) => {
  const [selectedFilters, setSelectedFilters] = useState({
    status: '',
    stage: '',
    dateRange: { startDate: '', endDate: '' },
    taskVolume: { min: '', max: '' },
    taskValue: { min: '', max: '' },
  });

  const [tempFilters, setTempFilters] = useState({
    status: '',
    stage: '',
    dateRange: { startDate: '', endDate: '' },
    taskVolume: { min: '', max: '' },
    taskValue: { min: '', max: '' },
  });

  return (
    <EmployeeReportFiltersContext.Provider
      value={{
        selectedFilters,
        setSelectedFilters,
        tempFilters,
        setTempFilters,
      }}>
      {children}
    </EmployeeReportFiltersContext.Provider>
  );
};

// Context 2: For General Filter Popup
const FilterPopupContext = createContext();

export const useFilterPopup = () => {
  return useContext(FilterPopupContext);
};

export const FilterPopupProvider = ({ children }) => {
  const [selectedFilters, setSelectedFilterContext] = useState({
    status: { status: '', range: { minValue: '', maxValue: '' } },
    dateRange: { startDate: '', endDate: '' },
    location: '',
    role: '',
    department: '',
  });

  const [tempFilters, setTempFilters] = useState({
    status: { status: '', range: { minValue: '', maxValue: '' } },
    dateRange: { startDate: '', endDate: '' },
    location: '',
    role: '',
    department: '',
  });

  return (
    <FilterPopupContext.Provider
      value={{
        selectedFilters,
        setSelectedFilterContext,
        tempFilters,
        setTempFilters,
      }}>
      {children}
    </FilterPopupContext.Provider>
  );
};
