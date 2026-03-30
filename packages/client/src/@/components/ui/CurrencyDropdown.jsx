import React, { useState, useMemo, useEffect } from 'react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
  SelectGroup,
} from '@/components/ui/select';
import { countries } from 'countries-list';

const CurrencyDropdown = ({ onSelect, value }) => {
  // value is inside the selectCurrency
  const [selectedCurrency, setSelectedCurrency] = useState(value || '');

  useEffect(() => {
    setSelectedCurrency(value);
  }, [value]);

  const currencyList = useMemo(() => {
    const uniqueCurrencies = new Set();
    Object.values(countries).forEach(country => {
      if (country.currency) {
        uniqueCurrencies.add(country.currency);
      }
    });
    return Array.from(uniqueCurrencies).sort();
  }, []);

  const handleSelect = value => {
    const selectedValue = Array.isArray(value) ? value[0] : value;
    setSelectedCurrency(selectedValue);
    if (onSelect) {
      onSelect(selectedValue || '');
    }
  };

  return (
    <div className="w-fit">
      <Select onValueChange={handleSelect} value={selectedCurrency}>
        <SelectTrigger
          className="border-[#6A6AEC] text-white bg-[#6A6AEC] gap-1"
          style={{
            borderRadius: '5px 0 0 5px',
          }}>
          {/* <SelectValue placeholder={selectedCurrency || 'INR'} /> */}
          <SelectValue placeholder="INR">
            {selectedCurrency ? selectedCurrency : 'INR'}
          </SelectValue>
        </SelectTrigger>
        <SelectContent className="xl:h-[220px] 2xl:h-[320px] md:h-[180px] h-[160px] w-[70px]">
          <SelectGroup>
            {currencyList.map((currencyCode, index) => (
              <SelectItem key={`${currencyCode}-${index}`} value={currencyCode}>
                {currencyCode}
              </SelectItem>
            ))}
          </SelectGroup>
        </SelectContent>
      </Select>
    </div>
  );
};

export default CurrencyDropdown;
