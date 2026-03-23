import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
} from '@/components/ui/command';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';

import CameraIcon from 'assets/images/camera-icon.png';
import EditIcon from 'assets/images/edit.png';
import RemoveIcon from 'assets/images/remove.png';
import { useState } from 'react';
import { FaChevronDown } from 'react-icons/fa';

import { Country } from 'country-state-city';
import Flags from 'country-flag-icons/react/3x2';
import { AiFillEye } from 'react-icons/ai';
import HoverInfo from 'components/HoverInfo';
import { HiInformationCircle } from 'react-icons/hi';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Calendar as CalendarCom } from '@/components/ui/calendar';
import { cn } from '@/lib/utils';
import { addDays, format } from 'date-fns';
import { CalendarDays } from 'lucide-react';
import { Textarea } from '@/components/ui/textarea';
import { Switch } from '@/components/ui/switch';

const EditModal = () => {
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
    <div className="grid gap-4 grid-cols-12 col-span-12 px-2 pb-6 md:px-6 2xl:p-6 overflow-auto max-h-[600px] lg:max-h-[300px] 2xl:max-h-[450px]">
      <div className="relative flex justify-center col-span-12">
        <Avatar className="h-24 w-24">
          <AvatarImage alt="profile" />
          <AvatarFallback>RS</AvatarFallback>
        </Avatar>
        <DropdownMenu>
          <DropdownMenuTrigger className="absolute bottom-0 h-8 w-8 ms-20 outline-none">
            <img src={CameraIcon} alt="camera" />
          </DropdownMenuTrigger>
          <DropdownMenuContent align="start">
            <DropdownMenuItem className="p-1 2xl:p-2">
              <DropdownMenuLabel className="flex justify-start items-center gap-3 text-[10px] 2xl:text-sm">
                <img src={EditIcon} alt="edit" />
                Edit
              </DropdownMenuLabel>
            </DropdownMenuItem>
            <DropdownMenuItem className="p-1 2xl:p-2">
              <DropdownMenuLabel className="flex justify-start items-center gap-3 text-[10px] 2xl:text-sm">
                <img src={RemoveIcon} alt="remove" />
                Remove
              </DropdownMenuLabel>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
      {/* First Name */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            First Name*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="text"
            placeholder={'First Name'}
            className="bg-slate-400/10 border border-input ps-3"
          />
        </CardContent>
      </Card>
      {/* Last Name */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Last Name*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="text"
            placeholder={'Last Name'}
            className="bg-slate-400/10 border border-input ps-3"
          />
        </CardContent>
      </Card>
      {/* Email */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Email Address*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="email"
            placeholder={'Email Address'}
            className="bg-slate-400/10 border border-input ps-3"
          />
        </CardContent>
      </Card>
      {/* Employee Code */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Employee Code*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="password"
            placeholder={'Enter employee code'}
            className="bg-slate-400/10 border border-input ps-3"
          />
        </CardContent>
      </Card>
      {/* Password */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold flex items-center gap-1">
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
            className="bg-slate-400/10 border border-input ps-3"
          />
          <div className="absolute top-1/2 -translate-y-1/2 right-2 text-violet-500 flex items-center gap-2">
            <AiFillEye />
          </div>
        </CardContent>
      </Card>
      {/* Confirm Password */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Confirm Password*
          </CardTitle>
        </CardHeader>
        <CardContent className="relative">
          <Input
            type="password"
            placeholder={'Confirm Password'}
            className="bg-slate-400/10 border border-input ps-3"
          />
          <div className="absolute top-1/2 -translate-y-1/2 right-2 text-violet-500 flex items-center gap-2">
            <AiFillEye />
          </div>
        </CardContent>
      </Card>
      {/* Mobile Input */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Mobile Number*
          </CardTitle>
        </CardHeader>
        <CardContent className="flex justify-start items-center">
          <Popover open={isMobileCodeOpen} onOpenChange={setIsMobileCodeOpen}>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                role="combobox"
                aria-expanded={isMobileCodeOpen}
                className="w-fit justify-between bg-slate-400/10 bg-[#E8E8FF] border border-input border-r-0 rounded-e-none">
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
                <FaChevronDown className="ml-2 h-2 2xl:w-4 w-2 2xl:h-4 shrink-0 opacity-50" />
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
            className="bg-slate-400/10 border border-l-0 border-input ps-3 rounded-s-none"
            name="phoneNumber"
          />
        </CardContent>
      </Card>
      {/* Location */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Location*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Select>
            <SelectTrigger className="bg-gray-500/10 border">
              <SelectValue placeholder="Select a Location" />
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
          <CardTitle className="text-xs 2xl:text-sm font-bold">Role*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="password"
            placeholder={'Enter role'}
            className="bg-slate-400/10 border border-input ps-3"
          />
        </CardContent>
      </Card>
      {/* Department */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Department*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Select>
            <SelectTrigger className="bg-gray-500/10 border">
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
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Date of Joining*
          </CardTitle>
        </CardHeader>
        <CardContent className="flex">
          <Popover>
            <PopoverTrigger asChild>
              <Button
                variant={'outline'}
                className={cn(
                  'justify-between text-left bg-slate-400/10 border-none w-full',
                  !date && 'text-primary'
                )}>
                {date ? (
                  format(date, 'PPP')
                ) : (
                  <span className="text-[10px] 2xl:text-sm text-primary font-medium">
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
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Time zone*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Select>
            <SelectTrigger className="bg-gray-500/10 border">
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
      {/* Project Name */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Enter Project Name*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="password"
            placeholder={'Enter Project Name'}
            className="bg-slate-400/10 border border-input ps-3"
          />
        </CardContent>
      </Card>
      {/* Select Shift */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Select Shift*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Select>
            <SelectTrigger className="bg-gray-500/10 border">
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
      {/* Address */}
      <Card className={`border-none shadow-none col-span-12 sm:col-span-12`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-xs 2xl:text-sm font-bold">
            Address*
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Textarea className="bg-slate-400/10" placeholder="Enter Address" />
        </CardContent>
      </Card>
      {/* Mobile Tracking */}
      <Card className={`border-none shadow-none col-span-12`}>
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
      <div className="col-span-12 flex gap-3 justify-end items-center">
        <Button varient="ghost" className="text-violet-500 bg-transparent">
          Discard
        </Button>
        <Button className="bg-violet-500 w-72">Register Employee</Button>
      </div>
    </div>
  );
};

export default EditModal;
