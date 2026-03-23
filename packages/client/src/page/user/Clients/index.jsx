import ClientSearch from 'components/ClientModule/ClientSearch';
import ClientTable from 'components/ClientModule/ClientTable';
import { useState } from 'react';

const Clients = () => {
  const [searchValue, setSearchValue] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  return (
    <>
      <ClientSearch
        searchQuery={searchQuery}
        setSearchQuery={setSearchQuery}
        searchValue={searchValue}
        setSearchValue={setSearchValue}
      />
      <ClientTable searchQuery={searchQuery} searchValue={searchValue} />
    </>
  );
};

export default Clients;
