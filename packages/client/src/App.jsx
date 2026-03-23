import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import ChartProvider from 'components/ChartContext/Provider';
import { RouterProvider } from 'react-router-dom';
import { routes } from 'routes/routes';
import 'styles/global.css';
import {
  EmployeeReportFiltersProvider,
  FilterPopupProvider,
} from 'context/Filters/FilterContext'; // Import both providers

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <EmployeeReportFiltersProvider>
        <FilterPopupProvider>
          <ChartProvider>
            <RouterProvider router={routes} />
          </ChartProvider>
        </FilterPopupProvider>
      </EmployeeReportFiltersProvider>
    </QueryClientProvider>
  );
}

export default App;
