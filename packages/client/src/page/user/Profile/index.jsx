import { useEffect, useState } from 'react';
import { useFormik } from 'formik';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Button } from '@/components/ui/button';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
} from '@/components/ui/command';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { Textarea } from '@/components/ui/textarea';
import { cn } from '@/lib/utils';
import ProfilePhotoDefault from '@assets/images/profile.png';
import { Check } from 'lucide-react';
import { FaChevronDown } from 'react-icons/fa';
import { Country, State, City } from 'country-state-city';
import Flags from 'country-flag-icons/react/3x2';
import CameraIcon from 'assets/images/camera-icon.png';
import EditIcon from 'assets/images/edit.png';
import RemoveIcon from 'assets/images/remove.png';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { toast } from 'sonner';
import { fetchProfile } from './Api/get';
import { useQuery } from '@tanstack/react-query';
import {
  adminUpdatePasswordSchema,
  profileValidationSchema,
} from 'schema/Prfile';
import { adminImgUpload } from './Api/post';
import { editProfile, updateAdminPassword } from './Api/put';
import { useLocation } from 'react-router-dom';
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';
import { HiEyeOff, HiEye } from 'react-icons/hi';
import ButtonLoading from 'components/ButtonLoading';

const Profile = () => {
  // const { state } = useLocation();
  const [collapseIsOpen, setCollapseIsOpen] = useState(false);
  const { isLoading, error, data, refetch } = useQuery({
    queryKey: ['profileDataAdmin'],
    queryFn: fetchProfile,
  });
  const profileData = data?.data?.body?.data?.userData;
  // const profileData = state?.profileDataAdmin;

  const [isCountryOpen, setIsCountryOpen] = useState(false);
  const [isStateOpen, setIsStateOpen] = useState(false);
  const [isCityOpen, setIsCityOpen] = useState(false);
  // const [isMobileCodeOpen, setIsMobileCodeOpen] = useState(false);
  const [selectedCountry, setSelectedCountry] = useState(null);
  const [selectedState, setSelectedState] = useState(null);
  const [selectedCity, setSelectedCity] = useState(null);
  // const [selectedCountryCode, setselectedCountryCode] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');

  const countries = Country.getAllCountries() || [];
  const filteredCountries = searchQuery
    ? countries.filter(c =>
        c.name.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : countries;

  const states = selectedCountry
    ? State.getStatesOfCountry(selectedCountry.isoCode)
    : [];
  const filteredStates = searchQuery
    ? states.filter(s =>
        s.name.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : states;

  const cities = selectedState
    ? City.getCitiesOfState(selectedState.countryCode, selectedState.isoCode)
    : [];
  const filteredCities = searchQuery
    ? cities.filter(c =>
        c.name.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : cities;

  const filteredCountryCode = searchQuery
    ? countries.filter(c =>
        c.name.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : countries;

  const FlagIcon = ({ countryCode }) => {
    const Flag = Flags[countryCode.toUpperCase()];
    return <Flag />;
  };

  const formik = useFormik({
    initialValues: {
      fullName: null,
      profilePic: null,
      address1: null,
      address2: null,
      city: null,
      state: null,
      country: null,
      zipCode: null,
      phoneNumber: null,
    },
    validationSchema: profileValidationSchema,
    onSubmit: values => {
      console.log('hi');
    },
  });

  useEffect(() => {
    if (profileData) {
      formik.setValues({
        fullName: profileData.fullName || null,
        profilePic: profileData?.profilePic || null,
        address1: profileData.address1 || null,
        address2: profileData.address2 || null,
        city: profileData.city || null,
        state: profileData.state || null,
        country: profileData.country || null,
        zipCode: profileData.zipCode || null,
        phoneNumber: profileData.phoneNumber || null,
      });
    }
  }, [profileData]);
  const [formChanged, setFormChanged] = useState(false);

  const handleChange = event => {
    formik.handleChange(event);
    setFormChanged(true);
  };

  const [file, setFile] = useState(profileData?.profilePic);

  const handleUploadProfilePhoto = () => {
    const fileInput = document.createElement('Input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = async event => {
      setFormChanged(true);
      toast.info('Uploading profile photo...', { autoClose: false });
      const updatedDataUrl = await adminImgUpload(event.currentTarget.files[0]);

      setFile(updatedDataUrl);
    };
    fileInput.click();
  };

  useEffect(() => {
    formik.setFieldValue('profilePic', file);
  }, [file]);

  const handleRemoveProfilePhoto = () => {
    setFormChanged(true);
    formik.setFieldValue('profilePic', null);
  };

  const handleSaveChnages = () => {
    // event.preventDefault();
    setFormChanged(false);

    editProfile(formik.values)
      .then(response => {
        if (response.data.body.status === 'success') {
          toast.success(response ? response?.data?.body?.message : '');
          refetch();
        } else {
          toast.error(response ? response?.data?.body?.message : '');
        }
      })
      .catch(error => {
        toast.error(error ? error?.data?.body?.message : 'Try again!');
      });
  };

  const [showOldPass, setShowOldPass] = useState(false);
  const [showNewPass, setShowNewPass] = useState(false);
  const [showConfirmPass, setShowConfirmPass] = useState(false);

  const formikPassword = useFormik({
    initialValues: {
      oldPassword: '',
      newPassword: '',
      confirmNewPassword: '',
    },
    validationSchema: adminUpdatePasswordSchema,
    onSubmit: async (values, { resetForm }) => {
      const { oldPassword, newPassword } = values;
      const payload = { oldPassword, newPassword };

      try {
        const response = await updateAdminPassword(payload);
        if (response?.data?.body?.status === 'success') {
          toast.success(response ? response?.data?.body?.message : '');
        } else {
          toast.error(response ? response?.data.body?.message : 'Try Again!');
        }
        resetForm();
      } catch (error) {
        toast.error(error.message);
      }
    },
  });

  return (
    <>
      {/* <h2 className="text-2xl font-bold mt-2 col-span-12">Profile Settings</h2> */}
      <div className="card-shadow gap-4 col-span-12 md:col-span-3 bg-white rounded-lg p-3 py-6 xl:px-8 px-4">
        <div className="flex gap-3 flex-col text-center justify-start">
          <div className="relative flex justify-center">
            <Avatar className="h-24 w-24">
              <AvatarImage
                src={
                  formik.values.profilePic === null
                    ? profileData?.profilePic !== null
                      ? profileData?.profilePic
                      : 'https://api.dicebear.com/8.x/initials/svg?seed=' +
                        profileData?.fullName
                    : formik.values.profilePic
                }
                alt="profile"
              />
              <AvatarFallback></AvatarFallback>
            </Avatar>
            <DropdownMenu>
              <DropdownMenuTrigger className="absolute bottom-0 h-8 w-8 ms-20 outline-none">
                <img src={CameraIcon} alt="camera" />
              </DropdownMenuTrigger>
              <DropdownMenuContent align="start">
                <DropdownMenuItem onClick={handleUploadProfilePhoto}>
                  <DropdownMenuLabel className="flex justify-start items-center gap-3">
                    <img src={EditIcon} alt="edit" />
                    Edit
                  </DropdownMenuLabel>
                </DropdownMenuItem>
                <DropdownMenuItem onClick={handleRemoveProfilePhoto}>
                  <DropdownMenuLabel className="flex justify-start items-center gap-3">
                    <img src={RemoveIcon} alt="remove" />
                    Remove
                  </DropdownMenuLabel>
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
          <div>
            <CardTitle className="text-sm 2xl:text-xl font-bold">
              {profileData?.fullName ?? ''}
            </CardTitle>
            <CardDescription className="text-xs 2xl:text-md font-semibold">
              {profileData?.role ?? ''}
            </CardDescription>
            <CardDescription className="text-xs 2xl:text-sm">
              {profileData?.email ?? ''}
            </CardDescription>
          </div>
          <Collapsible open={collapseIsOpen} onOpenChange={setCollapseIsOpen}>
            <CollapsibleTrigger asChild>
              {!collapseIsOpen ? (
                <Button
                  // onClick={handleSaveChnages}
                  className="w-full bg-solid-violet text-xs">
                  Update Password
                </Button>
              ) : (
                <h2 className="text-sm 2xl:text-md font-bold">
                  Set New Password
                </h2>
              )}
            </CollapsibleTrigger>
            <CollapsibleContent className="mt-2 CollapsibleContent">
              {/* Old Password */}
              <Card className="border-none shadow-none col-span-12 sm:col-span-6">
                <CardHeader className="flex flex-row items-center justify-between p-2">
                  <CardTitle className="text-xs 2xl:text-sm font-bold">
                    Old Password
                  </CardTitle>
                </CardHeader>
                <CardContent className="relative">
                  <Input
                    type={showOldPass ? 'text' : 'password'}
                    placeholder="Old Password"
                    className="w-full bg-slate-400/10 border border-input ps-3 pr-8 text-xs 2xl:text-sm"
                    name="oldPassword"
                    onChange={event => {
                      formikPassword.handleChange(event);
                    }}
                    onBlur={formikPassword.handleBlur}
                    value={formikPassword.values.oldPassword}
                  />
                  <span className="eye-icon-input">
                    {!showOldPass ? (
                      <HiEyeOff onClick={() => setShowOldPass(!showOldPass)} />
                    ) : (
                      <HiEye onClick={() => setShowOldPass(!showOldPass)} />
                    )}
                  </span>
                  {formikPassword.touched.oldPassword &&
                  formikPassword.errors.oldPassword ? (
                    <span className="error-message">
                      {formikPassword.errors.oldPassword}
                    </span>
                  ) : null}
                </CardContent>
              </Card>
              {/* New Password */}
              <Card className="border-none shadow-none col-span-12 sm:col-span-6">
                <CardHeader className="flex flex-row items-center justify-between p-2">
                  <CardTitle className="text-xs 2xl:text-sm font-bold">
                    New Password
                  </CardTitle>
                </CardHeader>
                <CardContent className="relative">
                  <Input
                    type={showNewPass ? 'text' : 'password'}
                    placeholder="New Password"
                    className="w-full bg-slate-400/10 border border-input ps-3 pr-8 text-xs 2xl:text-sm"
                    name="newPassword"
                    onChange={event => {
                      formikPassword.handleChange(event);
                    }}
                    onBlur={formikPassword.handleBlur}
                    value={formikPassword.values.newPassword}
                  />
                  <span className="eye-icon-input">
                    {!showNewPass ? (
                      <HiEyeOff onClick={() => setShowNewPass(!showNewPass)} />
                    ) : (
                      <HiEye onClick={() => setShowNewPass(!showNewPass)} />
                    )}
                  </span>
                  {formikPassword.touched.newPassword &&
                  formikPassword.errors.newPassword ? (
                    <span className="error-message">
                      {formikPassword.errors.newPassword}
                    </span>
                  ) : null}
                </CardContent>
              </Card>
              {/* Confirm Password */}
              <Card className="border-none shadow-none col-span-12 sm:col-span-6">
                <CardHeader className="flex flex-row items-center justify-between p-2">
                  <CardTitle className="text-xs 2xl:text-sm font-bold">
                    Confirm Password
                  </CardTitle>
                </CardHeader>
                <CardContent className="relative">
                  <Input
                    type={showConfirmPass ? 'text' : 'password'}
                    placeholder="Confirm Password"
                    className="w-full bg-slate-400/10 border border-input ps-3 pr-8 text-xs 2xl:text-sm"
                    name="confirmNewPassword"
                    onChange={event => {
                      formikPassword.handleChange(event);
                    }}
                    onBlur={formikPassword.handleBlur}
                    value={formikPassword.values.confirmNewPassword}
                  />
                  <span className="eye-icon-input">
                    {!showConfirmPass ? (
                      <HiEyeOff
                        onClick={() => setShowConfirmPass(!showConfirmPass)}
                      />
                    ) : (
                      <HiEye
                        onClick={() => setShowConfirmPass(!showConfirmPass)}
                      />
                    )}
                  </span>
                  {formikPassword.touched.confirmNewPassword &&
                  formikPassword.errors.confirmNewPassword ? (
                    <span className="error-message">
                      {formikPassword.errors.confirmNewPassword}
                    </span>
                  ) : null}
                </CardContent>
              </Card>
              <div className="my-4 flex items-center justify-center flex-wrap-reverse gap-4">
                <Button
                  className=" text-[#6A6AEC] text-xs"
                  variant="ghost"
                  onClick={() => setCollapseIsOpen(false)}>
                  Cancel
                </Button>
                <ButtonLoading
                  onClick={formikPassword.handleSubmit}
                  isLoading={formikPassword.isSubmitting}
                  className="bg-solid-violet text-xs"
                  disableButton={!formikPassword.isValid}>
                  Save New Password
                </ButtonLoading>
              </div>
            </CollapsibleContent>
          </Collapsible>
        </div>
      </div>
      <form
        onSubmit={formik.handleSubmit}
        className="card-shadow grid gap-4 2xl:gap-4 grid-cols-12 col-span-12 md:col-span-9 bg-white rounded-lg p-3 px-10 py-8">
        {/* Full Name */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-12">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Full Name
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Input
              type="text"
              placeholder="Full Name"
              className="bg-slate-400/10 ps-3 placeholder:text-slate-500/50 text-xs 2xl:text-sm"
              name="fullName"
              onChange={handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.fullName}
            />
            {formik.touched.fullName && formik.errors.fullName ? (
              <span className="error-message">{formik.errors.fullName}</span>
            ) : null}
          </CardContent>
        </Card>
        {/* Address 1 */}
        <Card className="border-none shadow-none col-span-12 md:col-span-12">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Address 1*
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Textarea
              className="bg-slate-400/10 text-xs 2xl:text-sm"
              placeholder="Enter Address 1"
              name="address1"
              onChange={handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.address1}
            />
            {formik.touched.address1 && formik.errors.address1 ? (
              <span className="error-message">{formik.errors.address1}</span>
            ) : null}
          </CardContent>
        </Card>

        {/* Address 2 */}
        <Card className="border-none shadow-none col-span-12 md:col-span-12">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Address 2*
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Textarea
              className="bg-slate-400/10 text-xs 2xl:text-sm"
              placeholder="Enter Address 2"
              name="address2"
              onChange={handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.address2}
            />
            {formik.touched.address2 && formik.errors.address2 ? (
              <span className="error-message">{formik.errors.address2}</span>
            ) : null}
          </CardContent>
        </Card>

        {/* Country Selector */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Country*
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Popover open={isCountryOpen} onOpenChange={setIsCountryOpen}>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  role="combobox"
                  aria-expanded={isCountryOpen}
                  className="w-full justify-between bg-slate-400/10 border border-input">
                  {formik?.values?.country ?? 'Select Country'}
                  <FaChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="p-0">
                <Command>
                  <CommandInput placeholder="Search Country..." />
                  <CommandEmpty>No Country found.</CommandEmpty>
                  <CommandGroup>
                    {filteredCountries.map(country => (
                      <CommandItem
                        key={country.isoCode}
                        value={country.name}
                        onSelect={() => {
                          setSelectedCountry(country);
                          setSelectedState(null);
                          setSelectedCity(null);
                          setSearchQuery('');
                          setIsCountryOpen(false);
                          formik.setFieldValue('country', country.name);
                          setFormChanged(true);
                          formik.setFieldValue('state', null);
                          formik.setFieldValue('city', null);
                        }}>
                        <Check
                          className={cn(
                            'mr-2 h-4 w-4',
                            selectedCountry === country.name
                              ? 'opacity-100'
                              : 'opacity-0'
                          )}
                        />
                        {country.name}
                      </CommandItem>
                    ))}
                  </CommandGroup>
                </Command>
              </PopoverContent>
            </Popover>
          </CardContent>
        </Card>

        {/* State Selector */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              State*
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Popover open={isStateOpen} onOpenChange={setIsStateOpen}>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  role="combobox"
                  aria-expanded={isStateOpen}
                  className="w-full justify-between bg-slate-400/10 border border-input">
                  {formik?.values?.state ?? 'Select State'}
                  <FaChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="p-0">
                <Command>
                  <CommandInput placeholder="Search State..." />
                  <CommandEmpty>No State found.</CommandEmpty>
                  <CommandGroup>
                    {filteredStates.map(state => (
                      <CommandItem
                        key={state.isoCode}
                        value={state.name}
                        onSelect={() => {
                          setSelectedState(state);
                          setSelectedCity(null);
                          setSearchQuery('');
                          setIsStateOpen(false);
                          formik.setFieldValue('state', state.name);
                          setFormChanged(true);
                          formik.setFieldValue('city', null);
                        }}>
                        <Check
                          className={cn(
                            'mr-2 h-4 w-4',
                            selectedState === state.name
                              ? 'opacity-100'
                              : 'opacity-0'
                          )}
                        />
                        {state.name}
                      </CommandItem>
                    ))}
                  </CommandGroup>
                </Command>
              </PopoverContent>
            </Popover>
          </CardContent>
        </Card>

        {/* City Selector */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6 lg:col-span-4">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              City*
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Popover open={isCityOpen} onOpenChange={setIsCityOpen}>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  role="combobox"
                  aria-expanded={open}
                  className="w-full justify-between bg-slate-400/10 border border-input">
                  {formik?.values?.city ?? 'Select City'}
                  <FaChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="p-0">
                <Command>
                  <CommandInput placeholder="Search City..." />
                  <CommandEmpty>No City found.</CommandEmpty>
                  <CommandGroup>
                    {filteredCities.map(city => (
                      <CommandItem
                        key={city.name}
                        value={city.name}
                        onSelect={() => {
                          setSelectedCity(city);
                          setSearchQuery('');
                          setIsCityOpen(false);
                          formik.setFieldValue('city', city.name);
                          setFormChanged(true);
                        }}>
                        <Check
                          className={cn(
                            'mr-2 h-4 w-4',
                            selectedCity === city.name
                              ? 'opacity-100'
                              : 'opacity-0'
                          )}
                        />
                        {city.name}
                      </CommandItem>
                    ))}
                  </CommandGroup>
                </Command>
              </PopoverContent>
            </Popover>
          </CardContent>
        </Card>

        {/* Phone Number */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Mobile Number*
            </CardTitle>
          </CardHeader>
          <CardContent className="flex justify-start items-center">
            {/* <Popover open={isMobileCodeOpen} onOpenChange={setIsMobileCodeOpen}>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  role="combobox"
                  aria-expanded={isMobileCodeOpen}
                  className="w-fit justify-between bg-slate-400/10 border border-input border-r-0 rounded-e-none">
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
            </Popover> */}
            <Input
              type="number"
              placeholder="Mobile Number"
              className="w-full bg-slate-400/10 border border-input ps-3 rounded-s-none"
              name="phoneNumber"
              onChange={handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.phoneNumber}
            />
          </CardContent>
          {formik.touched.phoneNumber && formik.errors.phoneNumber ? (
            <span className="error-message">{formik.errors.phoneNumber}</span>
          ) : null}
        </Card>

        {/* ZipCode */}
        <Card className="border-none shadow-none col-span-12 sm:col-span-6">
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-xs 2xl:text-sm font-bold">
              Zipcode*
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Input
              type="number"
              placeholder="Enter Zipcode"
              className="w-full bg-slate-400/10 border border-input ps-3"
              name="zipCode"
              onChange={e => {
                const value = e.target.value;
                formik.setFieldValue('zipCode', value.toString());
                setFormChanged(true);
              }}
              onBlur={formik.handleBlur}
              value={formik.values.zipCode}
            />
            {formik.touched.zipCode && formik.errors.zipCode ? (
              <span className="error-message">{formik.errors.zipCode}</span>
            ) : null}
          </CardContent>
        </Card>

        {/* <Button
          className="col-span-4 sm:col-start-1 md:col-start-6 md:col-span-3 xl:col-span-2 xl:col-start-8 text-[#6A6AEC]"
          variant="ghost">
          Discard
        </Button> */}
        <Button
          disabled={!formik.isValidating && !formChanged}
          onClick={() => {
            setFormChanged(false), handleSaveChnages();
          }}
          // className="col-span-8 md:col-span-4 xl:col-span-3 bg-solid-violet"
          className=" sm:col-start-1 md:col-start-6 xl:col-start-10 col-span-8 md:col-span-4 xl:col-span-3 bg-solid-violet">
          Save Changes
        </Button>
      </form>
    </>
  );
};

export default Profile;
