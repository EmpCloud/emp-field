import { AiFillEye } from 'react-icons/ai';
import { HiInformationCircle } from 'react-icons/hi';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { Switch } from '@/components/ui/switch';
import { Textarea } from '@/components/ui/textarea';
import { useState } from 'react';
import Flags from 'country-flag-icons/react/3x2';
import { Country } from 'country-state-city';
import { FaChevronDown } from 'react-icons/fa';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
} from '@/components/ui/command';
import HoverInfo from 'components/HoverInfo';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

import { Calendar as CalendarCom } from '@/components/ui/calendar';
import { addDays, format } from 'date-fns';
import { CalendarDays } from 'lucide-react';
import { cn } from '@/lib/utils';

const RegisterEmployeeModal = () => {
  const [isMobileCodeOpen, setIsMobileCodeOpen] = useState(false);
  const [selectedCountryCode, setselectedCountryCode] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [date, setDate] = useState();

  const countries = Country.getAllCountries() || [];
  const filteredCountryCode = searchQuery
    ? countries.filter(c =>
        c.name.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : countries;

  const FlagIcon = ({ countryCode }) => {
    const Flag = Flags[countryCode.toUpperCase()];
    return <Flag />;
  };

  return (
    <div className="grid gap-4 grid-cols-12 col-span-12 p-6 overflow-auto max-h-[300px] 2xl:max-h-[500px]">
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">First Name*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="text"
            placeholder={'First Name'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
        </CardContent>
      </Card>
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Last Name*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="text"
            placeholder={'First Name'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
        </CardContent>
      </Card>
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Email Address*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="email"
            placeholder={'Email Address'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
        </CardContent>
      </Card>
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Mobile Number*</CardTitle>
        </CardHeader>
        <CardContent className="flex justify-start items-center">
          <Popover open={isMobileCodeOpen} onOpenChange={setIsMobileCodeOpen}>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                role="combobox"
                aria-expanded={isMobileCodeOpen}
                className="w-fit justify-between bg-slate-400/10 bg-violet-200 border border-input border-r-0 rounded-e-none">
                {selectedCountryCode ? (
                  <div className="flex justify-start items-center gap-3">
                    <span className="w-6 h-4">
                      <FlagIcon countryCode={selectedCountryCode.isoCode} />
                    </span>
                    <span>+{selectedCountryCode.phonecode}</span>
                  </div>
                ) : (
                  'Select Code'
                )}
                <FaChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
              </Button>
            </PopoverTrigger>
            <PopoverContent className="p-0">
              <Command>
                <CommandInput placeholder="Search City..." />
                <CommandEmpty>No Country found.</CommandEmpty>
                <CommandGroup>
                  {filteredCountryCode.length > 0 &&
                    filteredCountryCode.map(country => (
                      <CommandItem
                        className="flex justify-start items-center gap-3"
                        key={country.isoCode}
                        value={country.isoCode}
                        onSelect={() => {
                          setselectedCountryCode(country);
                          setSearchQuery('');
                          setIsMobileCodeOpen(false);
                        }}>
                        <span className="w-6 h-4">
                          <FlagIcon countryCode={country.isoCode} />
                        </span>
                        <span>
                          {country.phonecode.includes('+')
                            ? country.phonecode
                            : '+' + country.phonecode}
                        </span>
                      </CommandItem>
                    ))}
                </CommandGroup>
              </Command>
            </PopoverContent>
          </Popover>
          <Input
            type="number"
            placeholder="Mobile Number"
            className="w-full bg-slate-400/10 border border-l-0 border-input ps-3 rounded-s-none"
            name="phoneNumber"
          />
        </CardContent>
      </Card>
      {/* Password */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold flex items-center gap-1">
            Password*
            <HoverInfo
              trigger={
                <HiInformationCircle className="w-4 h-4 text-violet-500" />
              }
              content={
                <div className="space-y-1 font-normal">
                  <h4 className="text-sm">The password must contain:</h4>
                  <ul className="flex flex-col list-disc marker:text-violet-500 ps-4">
                    <li className="text-xs text-muted-foreground">
                      At least{' '}
                      <span className="text-violet-500/70">
                        one special character
                      </span>
                    </li>
                    <li className="text-xs text-muted-foreground">
                      At least{' '}
                      <span className="text-violet-500/70">one number</span>
                    </li>
                    <li className="text-xs text-muted-foreground">
                      A minimum of{' '}
                      <span className="text-violet-500/70">8 characters</span>
                    </li>
                  </ul>
                </div>
              }
            />
          </CardTitle>
        </CardHeader>
        <CardContent className="relative">
          <Input
            type="password"
            placeholder={'Enter Password'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
          <div className="absolute top-1/2 -translate-y-1/2 right-2 text-violet-500 flex items-center gap-2">
            <AiFillEye />
          </div>
        </CardContent>
      </Card>
      {/* Confirm Password */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Confirm Password*</CardTitle>
        </CardHeader>
        <CardContent className="relative">
          <Input
            type="password"
            placeholder={'Confirm Password'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
          <div className="absolute top-1/2 -translate-y-1/2 right-2 text-violet-500 flex items-center gap-2">
            <AiFillEye />
          </div>
        </CardContent>
      </Card>
      {/* Employee Code */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Employee Code*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="password"
            placeholder={'Enter employee code'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
        </CardContent>
      </Card>
      {/* Location */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Location*</CardTitle>
        </CardHeader>
        <CardContent>
          <Select>
            <SelectTrigger className="w-full bg-gray-500/10 border">
              <SelectValue placeholder="Select a fruit" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="apple">Apple</SelectItem>
                <SelectItem value="banana">Banana</SelectItem>
                <SelectItem value="blueberry">Blueberry</SelectItem>
                <SelectItem value="grapes">Grapes</SelectItem>
                <SelectItem value="pineapple">Pineapple</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </CardContent>
      </Card>
      {/* Role */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Role*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="password"
            placeholder={'Enter role'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
        </CardContent>
      </Card>
      {/* Department */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Department*</CardTitle>
        </CardHeader>
        <CardContent>
          <Select>
            <SelectTrigger className="w-full bg-gray-500/10 border">
              <SelectValue placeholder="Select Department" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="apple">Apple</SelectItem>
                <SelectItem value="banana">Banana</SelectItem>
                <SelectItem value="blueberry">Blueberry</SelectItem>
                <SelectItem value="grapes">Grapes</SelectItem>
                <SelectItem value="pineapple">Pineapple</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </CardContent>
      </Card>
      {/* Date of Joining */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Date of Joining*</CardTitle>
        </CardHeader>
        <CardContent>
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant={'outline'}
                className={cn(
                  'w-full justify-between text-left bg-slate-400/10 border-none placeholder:font-extrabold placeholder: font-extrabold',
                  !date && 'text-muted-foreground'
                )}>
                {date ? (
                  format(date, 'PPP')
                ) : (
                  <span className="text-xs 2xl:text-sm text-primary font-normal">
                    Select Date
                  </span>
                )}
                <CalendarDays className="h-4 2xl:h-4 2xl:w-4 w-4 text-primary" />
              </Button>
            </PopoverTrigger>
            <PopoverContent className="flex w-auto flex-col space-y-2 p-2">
              <Select
                onValueChange={value =>
                  setDate(addDays(new Date(), parseInt(value)))
                }>
                <SelectTrigger>
                  <SelectValue placeholder="Select" />
                </SelectTrigger>
                <SelectContent position="popper">
                  <SelectItem value="0">Today</SelectItem>
                  <SelectItem value="1">Tomorrow</SelectItem>
                  <SelectItem value="3">In 3 days</SelectItem>
                  <SelectItem value="7">In a week</SelectItem>
                </SelectContent>
              </Select>
              <div className="rounded-md border">
                <CalendarCom mode="single" selected={date} onSelect={setDate} />
              </div>
            </PopoverContent>
          </Popover>
        </CardContent>
      </Card>
      {/* Time Zone */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Time zone*</CardTitle>
        </CardHeader>
        <CardContent>
          <Select>
            <SelectTrigger className="w-full bg-gray-500/10 border">
              <SelectValue placeholder="Select Department" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="apple">Apple</SelectItem>
                <SelectItem value="banana">Banana</SelectItem>
                <SelectItem value="blueberry">Blueberry</SelectItem>
                <SelectItem value="grapes">Grapes</SelectItem>
                <SelectItem value="pineapple">Pineapple</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </CardContent>
      </Card>
      {/* Upload Profile Picture */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold flex items-center gap-1">
            Upload Profile Picture*
            <HoverInfo
              trigger={
                <HiInformationCircle className="w-4 h-4 text-violet-500" />
              }
              content={
                <div className="space-y-1 font-normal">
                  <p className="text-xs">
                    The image should be in{' '}
                    <span className="text-violet-500/70">
                      JPEG or JPG or PNG
                    </span>{' '}
                    format and maximum size of the image should be{' '}
                    <span className="text-violet-500/70">less than 500kb</span>
                  </p>
                </div>
              }
            />
          </CardTitle>
        </CardHeader>
        <CardContent className="relative">
          <Input
            type="password"
            placeholder={'Enter Password'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
          <div className="absolute top-0 right-0 text-violet-500 flex items-center gap-2">
            <Button className="bg-violet-400/30 hover:bg-violet-300 text-blue-900 font-semibold">
              Browse
            </Button>
          </div>
        </CardContent>
      </Card>
      {/* Select Shift */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Select Shift*</CardTitle>
        </CardHeader>
        <CardContent>
          <Select>
            <SelectTrigger className="w-full bg-gray-500/10 border">
              <SelectValue placeholder="Select Department" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value="apple">Apple</SelectItem>
                <SelectItem value="banana">Banana</SelectItem>
                <SelectItem value="blueberry">Blueberry</SelectItem>
                <SelectItem value="grapes">Grapes</SelectItem>
                <SelectItem value="pineapple">Pineapple</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
        </CardContent>
      </Card>
      {/* Upload Profile Picture */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">
            Enter Project Name*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="password"
            placeholder={'Enter Project Name'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
        </CardContent>
      </Card>
      {/* Mobile Tracking */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold flex items-center gap-1">
            Mobile Tracking*
            <HoverInfo
              trigger={
                <HiInformationCircle className="w-4 h-4 text-violet-500" />
              }
              content={
                <div className="space-y-1 font-normal">
                  <p className="text-xs">Only for Web History.</p>
                </div>
              }
            />
          </CardTitle>
        </CardHeader>
        <CardContent className="flex items-center gap-2 text-xs pt-2 px-3">
          Enable <Switch /> Disable
        </CardContent>
      </Card>
      {/* Address */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-12`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Address*</CardTitle>
        </CardHeader>
        <CardContent>
          <Textarea className="bg-slate-400/10" placeholder="Enter Address" />
        </CardContent>
      </Card>
      <div className="col-span-6 col-start-7 flex gap-3 justify-center items-center">
        <Button varient="ghost" className="text-violet-500 bg-transparent">
          Discard
        </Button>
        <Button className="bg-violet-500 w-72">Register Employee</Button>
      </div>
    </div>
  );
};

export default RegisterEmployeeModal;
