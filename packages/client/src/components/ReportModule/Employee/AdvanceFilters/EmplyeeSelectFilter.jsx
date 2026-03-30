import React, { useRef, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

const EmplyeeSelectFilter = ({ placeholder, rolesArray }) => {
  const ref = useRef();

  return (
    <>
      <Card className="border-none w-[244px] shadow-none ">
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs font-semibold">{placeholder}</CardTitle>
        </CardHeader>
        <CardContent>
          <Select className="border-none">
            <SelectTrigger className="bg-slate-400/10 h-7 border-none font-semibold text-xs">
              <SelectValue
                placeholder={placeholder}
                className="text-[#4D4C4C] "
              />
            </SelectTrigger>
            <SelectContent>
              {/* <SelectItem value="developer">Developer</SelectItem> */}
              <div ref={ref} />
              {rolesArray &&
                rolesArray?.map(option => (
                  <SelectItem
                    className="text-xs font-semibold pb-2 hover:bg-slate-400/10 text-[#1F3A78] px-4 py-1 cursor-pointer pl-7"
                    key={option._id}
                    value={option._id}>
                    {option.role}
                  </SelectItem>
                ))}
              <div ref={ref} />
            </SelectContent>
          </Select>
        </CardContent>
      </Card>
    </>
  );
};

export default EmplyeeSelectFilter;
