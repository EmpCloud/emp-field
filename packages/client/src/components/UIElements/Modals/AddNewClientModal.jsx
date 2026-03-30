import { IoMdSearch } from 'react-icons/io';
import React from 'react';
import { Button } from '@/components/ui/button';
import { useFormik } from 'formik';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import {
  GoogleMap,
  Marker,
  useLoadScript,
  Autocomplete,
} from '@react-google-maps/api';
import { Search } from 'lucide-react';
import { Textarea } from '@/components/ui/textarea';
import { useEffect, useMemo, useRef, useState } from 'react';
import Flags from 'country-flag-icons/react/3x2';
import { Country, State, City } from 'country-state-city';
import { FaChevronDown } from 'react-icons/fa';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
} from '@/components/ui/command';
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

import { Check } from 'lucide-react';
import { cn } from '@/lib/utils';

import { validationSchema } from 'schema/Prfile';
import axios from 'axios';
import { createClient } from 'components/ClientModule/Api/post';
import { updateClient } from 'components/ClientModule/Api/Put';
import { toast } from 'sonner';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import CameraIcon from 'assets/images/camera-icon.png';
import addpointer from 'assets/images/addpointer.png';
import EditIcon from 'assets/images/edit.png';
import addTaskIcon from 'assets/images/add-task.png';
import addTaskIconwi from 'assets/images/pluswi.png';
import RemoveIcon from 'assets/images/remove.png';
import { useQuery } from '@tanstack/react-query';
import { adminImgUpload } from 'page/user/Profile/Api/post';
import { DialogClose } from '@/components/ui/dialog';
import LiveLocationMarkerImage from 'components/DashboardModule/LiveLocationMarkerImage';
import { fetchEmployeeDetails } from './Api/post';
import { RxCross1 } from 'react-icons/rx';

const center = {
  lat: -3.745,
  lng: -38.523,
};
const AddNewClientModal = ({ rowData, _id, refetch, onClose = () => {} }) => {
  // const { isLoaded } = useLoadScript({
  //   googleMapsApiKey: import.meta.env.VITE_SOME_KEY,
  // });

  const [saveAddress, setSaveAddress] = useState(false);
  const conformAddress = true;

  const [isMobileCodeOpen, setIsMobileCodeOpen] = useState(false);
  const [addcompladdr, setaddcompladdr] = useState(false);
  const [isCountryOpen, setIsCountryOpen] = useState(false);
  const [isStateOpen, setIsStateOpen] = useState(false);
  const [isCityOpen, setIsCityOpen] = useState(false);
  const [selectedCountry, setSelectedCountry] = useState(null);
  const [selectedState, setSelectedState] = useState(null);
  const [selectedCity, setSelectedCity] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCountryCode, setselectedCountryCode] = useState(null);
  const [mapisopen, setmapisopen] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [isBlurred, setIsBlurred] = useState(false);
  const mapref = useRef();
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [countrySearchQuery, setCountrySearchQuery] = useState('');
  const [employeeSearchQuery, setEmployeeSearchQuery] = useState('');
  const [isInputFocused, setInputFocused] = useState(false);
  const [addCompleteAddressVisible, setAddCompleteAddressVisible] =
    useState(false);
  const [tempProfilePic, setTempProfilePic] = useState(
    rowData?.clientProfilePic || null
  );

  // const { isLoading, error, data, refetch } = useQuery({});
  const handleMap = () => {
    if (mapref.current.focus) {
      setmapisopen(true);
    }
  };

  const { data: response, isLoading } = useQuery({
    queryKey: ['fetchEmployeeDetails', searchQuery],
    queryFn: () => fetchEmployeeDetails(searchQuery),
    enabled: isInputFocused, // Only run when input is focused
  });
  const employees = response?.body?.data?.resultData || [];

  const [formChanged, setFormChanged] = useState(false);

  const [clientData, setClientData] = useState({
    clientName: '',
    clientId: '',
    clientPhoneNo: '',
    address: '',
    assigned: '',
  });

  const [formData, setFormData] = useState(clientData || {});

  const handleSubmit = async event => {
    event.preventDefault();
    await formik.handleSubmit();
    onClose();
  };
  const [searchQueryCounrty, setSearchQueryCounrty] = useState('');
  const countries = Country.getAllCountries() || [];
  const filteredCountries = useMemo(
    () =>
      searchQueryCounrty
        ? countries.filter(c =>
            c.name.toLowerCase().includes(searchQueryCounrty.toLowerCase())
          )
        : countries,
    [searchQueryCounrty, countries]
  );

  const states = selectedCountry
    ? State.getStatesOfCountry(selectedCountry.isoCode)
    : [];
  const filteredStates = useMemo(
    () =>
      searchQueryCounrty
        ? states.filter(s =>
            s.name.toLowerCase().includes(searchQueryCounrty.toLowerCase())
          )
        : states,
    [searchQueryCounrty, states]
  );

  const cities = selectedState
    ? City.getCitiesOfState(selectedState.countryCode, selectedState.isoCode)
    : [];
  const filteredCities = useMemo(
    () =>
      searchQueryCounrty
        ? cities.filter(c =>
            c.name.toLowerCase().includes(searchQueryCounrty.toLowerCase())
          )
        : cities,
    [searchQueryCounrty, cities]
  );

  const filteredCountryCode = countrySearchQuery
    ? countries.filter(c =>
        c.name.toLowerCase().includes(countrySearchQuery.toLowerCase())
      )
    : countries;

  const FlagIcon = ({ countryCode }) => {
    const Flag = Flags[countryCode.toUpperCase()];
    return <Flag />;
  };

  const formik = useFormik({
    initialValues: {
      clientProfilePic: rowData?.clientProfilePic || null,
      clientName: rowData?.clientName || '',
      emailId: rowData?.emailId || null,
      contactNumber: (rowData?.contactNumber || '').replace(/\s+/g, ''),
      countryCode: rowData?.countryCode || '',
      // clientId: rowData?.clientId || '',
      category: rowData?.category || '',
      country: rowData?.country || '',
      state: rowData?.state || '',
      city: rowData?.city || '',
      zipCode: rowData?.zipCode || null,
      address1:
        rowData?.address1 || (conformAddress == true && saveAddress)
          ? saveAddress && saveAddress?.address
          : '',
      // selectEmployee: rowData?.assigned || '',
      address2: rowData?.address2 || '',
      latitude: rowData?.latitude || '', // Use null or '' instead of 0
      longitude: rowData?.longitude || '',
      // employeeIds: rowData ? rowData.assignedMembers[0]?.fullName || [] : [],
      employeeIds: rowData?.employees ? rowData?.employees?.name : '',
    },
    validationSchema: validationSchema,
    onSubmit: values => {
      console.log(values);
    },
  });
  useEffect(() => {
    if (conformAddress && saveAddress) {
      formik.setFieldValue('address1', saveAddress?.address || '');
      formik.setFieldValue('address2', saveAddress.address2);
      formik.setFieldValue('country', saveAddress?.country || '');
      formik.setFieldValue('state', saveAddress?.state || '');
      formik.setFieldValue('city', saveAddress?.city || '');
      formik.setFieldValue('zipCode', saveAddress?.zip || null);
    }
  }, [conformAddress, saveAddress]);

  const [file, setFile] = useState(rowData?.clientProfilePic);

  const getInitials = name => {
    if (!name) return '';
    const names = name.split(' ').filter(Boolean); // Split by spaces and filter out any empty strings
    const initials = names
      .slice(0, 3)
      .map(n => n.charAt(0).toUpperCase())
      .join(''); // Get first 3 initials
    return `https://api.dicebear.com/8.x/initials/svg?seed=${initials}`;
  };

  useEffect(() => {
    setFile(tempProfilePic);
  }, [tempProfilePic]);

  const handleRemoveProfilePhoto = () => {
    setFormChanged(true);
    setTempProfilePic(''); // Clear temporary picture
    formik.setFieldValue('clientProfilePic', null); // Clear formik field
    setFile(''); // Clear file state as well

    // Set initials as the temporary profile picture
    if (rowData?.clientName) {
      const initials = getInitials(rowData.clientName);
      setTempProfilePic(initials); // Set initials as temp profile picture
    }
  };

  const handleUploadProfilePhoto = () => {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = async event => {
      setFormChanged(true);
      const selectedFile = event.currentTarget.files[0];
      if (!selectedFile) return;

      // Create a temporary URL for immediate UI feedback
      const tempDataUrl = URL.createObjectURL(selectedFile);
      setTempProfilePic(tempDataUrl); // Set temporary image URL
      setFile(tempDataUrl); // Update file state as well

      toast.info('Uploading profile photo...', { autoClose: false });

      try {
        const updatedDataUrl = await adminImgUpload(selectedFile);
        formik.setFieldValue('clientProfilePic', updatedDataUrl);
        toast.success('Profile photo uploaded successfully!');
      } catch (error) {
        console.error('Error uploading image:', error);
        toast.error('Failed to upload image. Please try again.');
      }
    };
    fileInput.click();
  };

  const handleSaveChanges = async () => {
    setFormChanged(false);
    try {
      const valuesToSubmit = {
        ...formik.values,
        emailId: formik.values.emailId || null,
        zipCode: formik.values.zipCode === null ? null : formik.values.zipCode,
        // Ensure employeeIds is a string
        employeeIds: Array.isArray(formik.values.employeeIds)
          ? formik.values.employeeIds.join(',') // Convert array to string
          : formik.values.employeeIds || '', // Default to empty string if undefined
        countryCode: formik.values.countryCode.replace(/ /g, ''),
        latitude: formik.values.latitude ? Number(formik.values.latitude) : 0, // Convert to 0 if empty or undefined
        longitude: formik.values.longitude
          ? Number(formik.values.longitude)
          : 0,
      };
      const response = rowData
        ? await updateClient(rowData._id, valuesToSubmit)
        : await createClient(valuesToSubmit);
      if (response.data.body.status === 'success') {
        toast.success(response?.data?.body?.message || 'Success');
        onClose();
        await refetch();
      } else {
        toast.error(response?.data?.body?.message || 'Error occurred');
      }
    } catch (error) {
      console.error('API Error:', error);
      // toast.error(error?.response?.data?.body?.message || 'Try again!');
    }
    closePopup();
    refetch();
  };

  useEffect(() => {
    if (rowData) {
      formik.setValues({
        clientProfilePic: rowData?.clientProfilePic || null,
        clientName: rowData.clientName || '',
        emailId: rowData.emailId || null,
        contactNumber: (rowData.contactNumber || '').replace(/\s+/g, ''),
        countryCode: rowData.countryCode || '',
        // clientId: rowData.clientId || '',
        category: rowData.category || '',
        country: rowData.country || '',
        state: rowData.state || '',
        city: rowData.city || '',
        zipCode: rowData.zipCode || null,
        address1: rowData.address1 || '',
        // selectEmployee: rowData.assigned || '',
        address2: rowData?.address2 || '',
        longitude:
          rowData?.longitude !== undefined && rowData.longitude !== ''
            ? Number(rowData.longitude)
            : 0,
        latitude:
          rowData?.latitude !== undefined && rowData.latitude !== ''
            ? Number(rowData.latitude)
            : 0,
        // employeeIds: rowData ? rowData?.assignedMembers[0]?.fullName : '',
        employeeIds: rowData?.employees?.[0]?.fullName || '',
      });
      if (rowData.countryCode) {
        const country = countries.find(
          c => c.phonecode === rowData.countryCode
        );
        setselectedCountryCode(country);
        formik.setFieldValue('countryCode', country?.phonecode || '');
      }
    }
  }, [rowData]);

  useEffect(() => {
    if (rowData) {
      setClientData(rowData);
    }
  }, [rowData]);

  const handleChange = e => {
    const { name, value } = e.target;
    formik.setFieldValue(name, value);
  };

  const [range, setRange] = useState(0);
  const [currentLat, setcurrentLong] = useState(null);
  const mapContainerStyle = {
    width: '100%',
    height: '100%',
  };
  const autocompleteRef = useRef(null);

  const handleEmployeeSelect = employee => {
    setSelectedEmployee(employee);
    setSelectedEmployee(employee?.fullName);
    // Set employeeIds to the selected employee's ID for form submission
    formik.setFieldValue('employeeIds', employee?._id || null);
    setSearchQuery(employee.fullName);
    // setInputFocused(false);
  };
  const filteredEmployees = searchQuery
    ? employees.filter(employee =>
        employee.fullName.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : employees;

  const handleInputFocus = () => {
    setInputFocused(true);
    // setIsCountryOpen(true);
  };

  const onPlaceChanged = () => {
    const place = autocompleteRef.current.getPlace();
    if (place && place.formatted_address) {
      formik.setFieldValue('address1', place.formatted_address);

      // Extract latitude and longitude from the place
      const lat = place.geometry.location.lat();
      const lng = place.geometry.location.lng();
      alert(`Lat, Lng : ${lat}, ${lng}`);

      // Set the marker position
      setMarkers([{ lat, lng }]);
      formik.setFieldValue('latitude', lat);
      formik.setFieldValue('longitude', lng);
      setCenter({ lat, lng }); // Center the map on the selected location

      // Optionally, you can extract and set more details if needed
      const addressComponents = place.address_components;

      const getComponent = type => {
        const component = addressComponents.find(comp =>
          comp.types.includes(type)
        );
        return component ? component.long_name : '';
      };

      // Update additional fields if necessary
      formik.setFieldValue('country', getComponent('country'));
      formik.setFieldValue(
        'state',
        getComponent('administrative_area_level_1')
      );
      formik.setFieldValue(
        'city',
        getComponent('locality') || getComponent('sublocality')
      );
      formik.setFieldValue('zipCode', getComponent('postal_code'));
      setAddCompleteAddressVisible(true);
    }
  };
  // Function to toggle map visibility
  const toggleMap = () => {
    setmapisopen(prev => !prev);
  };

  const libraries = ['places'];

  const { isLoaded } = useLoadScript({
    googleMapsApiKey: import.meta.env.VITE_SOME_KEY,
    libraries: libraries,
  });

  const [center, setCenter] = useState({
    lat: rowData?.latitude || 14.477234, // Set to rowData latitude if available
    lng: rowData?.longitude || 78.804932,
  });

  const [markers, setMarkers] = useState([]);

  const handleMapClick = event => {
    const newMarker = {
      lat: event.latLng.lat(),
      lng: event.latLng.lng(),
    };

    setMarkers([newMarker]);
    setcurrentLong(newMarker);
    setCenter(newMarker);
    if (newMarker.lat && newMarker.lng) {
      formik.setFieldValue('latitude', newMarker.lat);
      formik.setFieldValue('longitude', newMarker.lng);
    }
    getAddressFromCoordinatesGoogle(newMarker.lat, newMarker.lng);
    alert(`Lat, Lng : ${newMarker.lat}, ${newMarker.lng}`);
    setAddCompleteAddressVisible(true);
  };

  const [onChangeRange, setOnChangeRange] = useState(range);
  const [longitude, setLongitude] = useState('');
  const [latitude, setLatitude] = useState('');

  useEffect(() => {
    if (latitude && longitude) {
      formik.setFieldValue('latitude', latitude);
      formik.setFieldValue('longitude', longitude);
    }
  }, [latitude, longitude]);

  const handleAutoFocus = e => {
    e.preventDefault();
    // Disable map interaction
    // setMapInteractionEnabled(true);
  };
  const handleSuggestionClick = () => {
    // setMapInteractionEnabled(true);
    setIsBlurred(false);
  };

  async function getAddressFromCoordinatesGoogle(lat, lon, address) {
    const apiKey = import.meta.env.VITE_SOME_KEY;
    const url = address
      ? `https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(address)}&key=${apiKey}`
      : `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lon}&key=${apiKey}`;
    try {
      const response = await fetch(url);
      const data = await response.json();
      if (data.status !== 'OK') {
        throw new Error('Error retrieving data from the Google API');
      }
      const result = data.results[0];
      const addressComponents = result.address_components;
      const getComponent = type => {
        const component = addressComponents.find(comp =>
          comp.types.includes(type)
        );
        return component ? component.long_name : null;
      };
      const addressDetails = {
        address: result.formatted_address,
        country: getComponent('country'),
        state: getComponent('administrative_area_level_1'),
        city:
          getComponent('locality') ||
          getComponent('sublocality') ||
          getComponent('administrative_area_level_2'),
        zip: getComponent('postal_code'),
      };
      formik.setFieldValue('address1', addressDetails.address);
      formik.setFieldValue('country', addressDetails.country);
      formik.setFieldValue('state', addressDetails.state);
      formik.setFieldValue('city', addressDetails.city);
      formik.setFieldValue('zipCode', addressDetails.zip);
      setSaveAddress(addressDetails);
      setMarkers([
        {
          lat: result.geometry.location.lat,
          lng: result.geometry.location.lng,
        },
      ]);
      setCenter({
        lat: result.geometry.location.lat,
        lng: result.geometry.location.lng,
      });
      return addressDetails;
    } catch (error) {
      console.error('Error fetching address from Google Maps:', error.message);
      return null;
    }
  }

  useEffect(() => {
    const fetchCoordinates = async () => {
      try {
        if (rowData?.address1) {
          const addressDetails = await getAddressFromCoordinatesGoogle(
            null,
            null,
            rowData.address1
          );
          if (addressDetails) {
            setSaveAddress(addressDetails);
          }
        } else if (rowData?.latitude && rowData?.longitude) {
          const addressDetails = await getAddressFromCoordinatesGoogle(
            rowData.latitude,
            rowData.longitude
          );
          if (addressDetails) {
            setSaveAddress(addressDetails);
          }
        }
      } catch (error) {
        console.error('Error fetching coordinates:', error);
      }
    };
    fetchCoordinates();
  }, [rowData]);

  useEffect(() => {
    if (rowData && rowData.employees) {
      const employeeName = rowData.employees.name; // Extract the employee name
      setSelectedEmployee(employeeName); // Set the employee name in state
      // formik.setFieldValue('employeeIds', rowData.employees._id); // Set employee ID in form field
      formik.setFieldValue('employeeIds', rowData.employees?._id || '');
    }
  }, [rowData]);

  useEffect(() => {
    if (rowData && rowData.countryCode) {
      const country = countries.find(c => c.phonecode === rowData.countryCode);
      setselectedCountryCode(country);
      formik.setFieldValue('countryCode', rowData?.countryCode || '');
    }
  }, [rowData, countries]);

  const closePopup = () => {
    setIsPopupOpen(false);
  };

  return (
    // <div className="grid gap-4 grid-cols-12 col-span-12 p-4 overflow-auto max-h-[600px] 2xl:max-h-[500px]">
    <div className="grid gap-4 grid-cols-32 col-span-12 overflow-auto bg-white  lg:max-h-[400px] 2xl:max-h-[500px] max-w-4xl w-full rounded-lg shadow-lg mt-6">
      <form
        onSubmit={formik.handleSubmit}
        className="card-shadow grid gap-4 2xl:gap-4 grid-cols-12 col-span-12 md:col-span-9 bg-white rounded-lg  px-10 pb-8 pt-[10px]">
        <Card className="border-none shadow-none col-span-12  relative top-[-20px]">
          <CardHeader className="flex flex-col items-center justify-center p-4">
            <CardTitle className="text-lg font-bold mb-4">
              Profile Picture
            </CardTitle>
          </CardHeader>
          <CardContent className="flex flex-col items-center"></CardContent>
          <div className="relative flex justify-center">
            <Avatar className="h-24 w-24">
              <AvatarImage
                src={
                  tempProfilePic ||
                  formik.values.clientProfilePic ||
                  rowData?.clientProfilePic ||
                  (rowData?.clientName
                    ? getInitials(rowData.clientName)
                    : 'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/client/profile.jpg?timestamp=1727339626335')
                }
              />
              <AvatarFallback>
                <img
                  src={
                    formik.values.clientProfilePic || rowData?.clientProfilePic
                      ? undefined
                      : rowData?.clientName
                        ? `https://api.dicebear.com/8.x/initials/svg?seed=${rowData.clientName}`
                        : null
                  }
                  alt="Default"
                />
              </AvatarFallback>
            </Avatar>
            <DropdownMenu>
              <DropdownMenuTrigger className="absolute bottom-0 h-8 w-8 ms-20 outline-none">
                <img src={CameraIcon} alt="camera" />
              </DropdownMenuTrigger>
              <DropdownMenuContent align="start">
                <DropdownMenuItem onClick={handleUploadProfilePhoto}>
                  <DropdownMenuLabel className="flex justify-start items-center gap-3">
                    <img src={EditIcon} alt="edit" />
                    {tempProfilePic ? 'Edit' : 'Add'}
                  </DropdownMenuLabel>
                </DropdownMenuItem>
                {tempProfilePic && (
                  <DropdownMenuItem onClick={handleRemoveProfilePhoto}>
                    <DropdownMenuLabel className="flex justify-start items-center gap-3">
                      <img src={RemoveIcon} alt="remove" />
                      Remove
                    </DropdownMenuLabel>
                  </DropdownMenuItem>
                )}
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
          {formik.touched.clientProfilePic && formik.errors.clientProfilePic ? (
            <span className="error-message">
              {formik.errors.clientProfilePic}
            </span>
          ) : null}
        </Card>

        <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-sm font-bold">Full Name*</CardTitle>
          </CardHeader>
          <CardContent>
            <Input
              type="text"
              placeholder={'Full Name'}
              className="w-full bg-slate-400/10 border border-input ps-3 h-[42px] placeholder:text-slate-700"
              name="clientName" // Ensure name prop is set
              // onChange={handleChange}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.clientName}
            />
            {formik.touched.clientName && formik.errors.clientName ? (
              <span className="error-message">{formik.errors.clientName}</span>
            ) : null}
          </CardContent>
        </Card>

        {/* <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
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
      </Card> */}

        <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-sm font-bold">Email Address</CardTitle>
          </CardHeader>
          <CardContent>
            <Input
              type="email"
              placeholder={'Email Address'}
              className="w-full bg-slate-400/10 border border-input h-[42px] ps-3 placeholder:text-slate-700"
              name="emailId"
              onChange={formik.handleChange}
              // onChange={handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.emailId}
            />
            {formik.touched.emailId && formik.errors.emailId ? (
              <span className="error-message">{formik.errors.emailId}</span>
            ) : null}
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
                  value={formik.values.countryCode}
                  onChange={formik.handleChange}
                  onBlur={() => {
                    if (!formik?.values?.countryCode && !rowData?.countryCode) {
                      // Only mark as touched if there's no rowData countryCode
                      formik.setFieldTouched('countryCode', true);
                      formik.handleBlur('countryCode');
                    }
                  }}
                  onClick={() => {
                    setIsMobileCodeOpen(!isMobileCodeOpen); // Toggle the country code dropdown
                  }}
                  placeholder="Country Code"
                  className="w-fit justify-between h-[42px] bg-slate-400/10 bg-[#E8E8FF] border border-input border-r-0 rounded-e-none">
                  {selectedCountryCode ? (
                    <div className="flex justify-start items-center gap-3">
                      <span className="w-6 h-4">
                        <FlagIcon countryCode={selectedCountryCode.isoCode} />
                      </span>
                      <span>{selectedCountryCode?.phonecode}</span>
                    </div>
                  ) : (
                    <span>{rowData?.countryCode || 'Select Code'}</span>
                  )}
                  <FaChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                </Button>
              </PopoverTrigger>
              <PopoverContent className="p-0">
                <Command>
                  <CommandInput placeholder="Search Country..." />
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
                            formik.setFieldValue(
                              'countryCode',
                              country.phonecode.includes('+')
                                ? country.phonecode
                                : '+' + country.phonecode
                            );
                            // setSearchQuery('');
                            // formik.setFieldTouched('countryCode', true);
                            setIsMobileCodeOpen(false);
                          }}
                          onBlur={formik.handleBlur}>
                          {/* onSelect={() => handleCountrySelect(country)}> */}
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
              className="w-full bg-slate-400/10 border h-[42px] border-l-0 border-input ps-3 rounded-s-none"
              name="contactNumber"
              pattern="\d{10,15}"
              onChange={e => {
                const value = e.target.value.replace(/\s+/g, '');
                formik.setFieldValue('contactNumber', value.toString());
              }}
              onBlur={formik.handleBlur}
              value={formik.values.contactNumber}
            />
          </CardContent>
          {formik.touched.countryCode &&
            formik.errors.countryCode &&
            !rowData?.countryCode && (
              <span className="error-message">{formik.errors.countryCode}</span>
            )}
          {formik.touched.contactNumber && formik.errors.contactNumber ? (
            <span className="error-message">{formik.errors.contactNumber}</span>
          ) : null}
        </Card>

        {/* Client Code
      <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Client ID*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="text"
            placeholder={'Enter Client Code'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
          />
        </CardContent>
      </Card> */}

        {/* <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
        <CardHeader className="flex flex-row items-center justify-between p-2">
          <CardTitle className="text-sm font-bold">Client ID*</CardTitle>
        </CardHeader>
        <CardContent>
          <Input
            type="text"
            placeholder={'Enter Client Code'}
            className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
            name="clientId"
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            value={formik.values.clientId}
          />
          {formik.touched.clientId && formik.errors.clientId ? (
            <span className="error-message">{formik.errors.clientId}</span>
          ) : null}
        </CardContent>
      </Card> */}

        {/* Categories
            <Card className={`border-none shadow-none col-span-12`}>
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-sm font-bold">Categories*</CardTitle>
              </CardHeader>
              <CardContent>
                <Input
                  type="password"
                  placeholder={'Select Categories'}
                  className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700"
                />
              </CardContent>
            </Card> */}

        <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-sm font-bold">Categories*</CardTitle>
          </CardHeader>
          <CardContent>
            <Input
              type="text"
              placeholder={'Select Categories'}
              className="w-full bg-slate-400/10 border h-[42px] border-input ps-3 placeholder:text-slate-700"
              name="category"
              onChange={formik.handleChange}
              // onChange={handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.category}
            />
            {formik.touched.category && formik.errors.category ? (
              <span className="error-message">{formik.errors.category}</span>
            ) : null}
            {/* <Select>
              <SelectTrigger className="w-full bg-slate-400/10 border h-[42px] border-input ps-3 placeholder:text-slate-700">
                <SelectValue placeholder="Select Categories" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="light">Light</SelectItem>
                <SelectItem value="dark">Dark</SelectItem>
                <SelectItem value="system">System</SelectItem>
              </SelectContent>
            </Select> */}
          </CardContent>
        </Card>
        <Card className="border-none shadow-none col-span-12 pt-4">
          <CardHeader className="flex flex-row items-center justify-between p-2 pl-0">
            <CardTitle className="text-sm font-bold">
              Select Employee*
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Popover className="relative">
              <PopoverTrigger asChild>
                <div className="relative">
                  <Input
                    type="text"
                    placeholder="Select Employee"
                    value={
                      searchQuery || (selectedEmployee ? selectedEmployee : '')
                    }
                    onChange={e => {
                      setSearchQuery(e.target.value);
                      setSelectedEmployee(null); // Reset selected employee while typing
                      formik.setFieldValue('employeeIds', '');
                    }}
                    onFocus={handleInputFocus}
                    onBlur={e => {
                      formik.handleBlur(e);
                      if (!formik.values.employeeIds) {
                        formik.setFieldTouched('employeeIds', true); // Mark the field as touched
                      }
                    }}
                    className="bg-slate-400/10 ps-3 h-[42px] placeholder:font-extrabold placeholder:text-slate-500/50 font-extrabold text-xs 2xl:text-sm"
                    // disabled={!!rowData}
                  />
                  {selectedEmployee && !rowData && (
                    <RxCross1
                      className="absolute top-1/2 -translate-y-1/2 right-2 w-3 h-3 cursor-pointer"
                      onClick={() => {
                        setSearchQuery('');
                        setEmployeeSearchQuery('');
                        setSelectedEmployee(null); // Clear selected employee
                        formik.setFieldValue('employeeIds', ''); // Clear Formik fieldAdd Complete Address
                      }}
                    />
                  )}
                </div>
              </PopoverTrigger>
              <PopoverContent
                onOpenAutoFocus={e => e.preventDefault()}
                align="start"
                className="max-h-[200px] overflow-y-auto p-2 min-w-40">
                {isLoading ? (
                  <div className="p-2 text-xs">Loading...</div>
                ) : filteredEmployees.length > 0 ? (
                  filteredEmployees.map(employee => (
                    <div
                      key={employee._id}
                      className="flex flex-row items-center justify-between pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer"
                      onClick={() => {
                        setSelectedEmployee(employee); // Set selected employee
                        setSearchQuery(employee.fullName); // Set input to selected employee's name
                        formik.setFieldValue('employeeIds', employee._id); // Assuming you're using Formik
                      }}>
                      <CardTitle className="text-xs 2xl:text-sm">
                        {employee.fullName}
                      </CardTitle>
                    </div>
                  ))
                ) : (
                  // Show "No employees found" if search query is not empty and no employee is selected
                  employeeSearchQuery.length > 0 &&
                  !selectedEmployee && (
                    <div className="p-2 text-xs text-gray-500">
                      No employees found.
                    </div>
                  )
                )}
              </PopoverContent>
            </Popover>
          </CardContent>
          {formik.touched.employeeIds && formik.errors.employeeIds ? (
            <div className="text-red-500 text-xs">
              {formik.errors.employeeIds}
            </div>
          ) : null}
        </Card>
        <Card className={`border-none shadow-none col-span-12 sm:col-span-12`}>
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-sm font-bold">Add Address*</CardTitle>
          </CardHeader>
          <CardContent className="relative">
            {/* <Input
              type="text"
              placeholder={'Search for area, street name...'}
              className="w-full bg-slate-400/10 border h-[42px] border-input ps-3 placeholder:text-slate-700 !pl-[40px]"
              name="address1"
              // onChange={formik.handleChange}
              // onChange={handleChange}
              // onBlur={formik.handleBlur}
              value={formik.values.address1}
              onClick={handleMap}
              ref={mapref}
            /> */}
            {!mapisopen && !rowData && (
              <button
                type="button"
                // onClick={handleMap}
                onClick={toggleMap}
                className="w-full bg-slate-400/10 border h-[42px] border-input ps-3 text-slate-700 flex items-center !pl-[40px]"
                ref={mapref}>
                <span className="flex-1">Open Map and Search Location</span>
              </button>
            )}
            {/* <IoMdSearch className="absolute text-[22px] top-[11px] left-[12px]" /> */}
            {/* {formik.touched.address1 && formik.errors.address1 ? (
              <span className="error-message">{formik.errors.address1}</span>
            ) : null} */}
          </CardContent>
        </Card>
        {(mapisopen || rowData) && (
          <Card
            className={`border-none shadow-none col-span-12 sm:col-span-12 h-[300px] rounded-lg`}>
            {isLoaded && (
              <GoogleMap
                options={{
                  mapTypeControl: true,
                  mapTypeControlOptions: {
                    position: window.google.maps.ControlPosition.BOTTOM_LEFT,
                  },
                }}
                mapContainerStyle={mapContainerStyle}
                zoom={10}
                center={center}
                markers={markers}
                onClick={handleMapClick}>
                <Autocomplete
                  ref={autocompleteRef}
                  onLoad={ref => (autocompleteRef.current = ref)}
                  onPlaceChanged={onPlaceChanged} // Ensure this is bound correctly
                >
                  <Card
                    style={{ position: 'relative' }}
                    className="absolute border-none shadow-none top-5 left-1/2 -translate-x-1/2 w-[60%]">
                    <CardContent>
                      <Popover className="relative">
                        <PopoverTrigger asChild className="">
                          <div className="flex justify-between items-center bg-custom-slate px-4 py-1">
                            {/* <Search size={16} /> */}
                            <Input
                              type="search"
                              placeholder="Search for a place"
                              value={formik.values.address1}
                              className="bg-transparent ps-3 text-xs 2xl:text-sm"
                              name="address1"
                              onChange={formik.handleChange}
                              onFocus={handleAutoFocus}
                              // onChange={handleChange}
                              // onBlur={handleSuggestionClick} // Optional: Restore interaction on blur
                              onBlur={e => {
                                formik.handleBlur(e);
                                formik.validateForm(); // Force a validation check
                              }}
                            />
                          </div>
                        </PopoverTrigger>
                      </Popover>
                    </CardContent>
                  </Card>
                </Autocomplete>
                {/* {markers.slice(-1).map((marker, index) => (
                  <React.Fragment key={index}>
                    <Marker
                      position={{
                        lat: marker.lat ?? 14.477234,
                        lng: marker.lng ?? 78.804932,
                      }}
                    />
                  </React.Fragment>
                ))} */}
                {markers.map((marker, index) => (
                  <Marker
                    key={index}
                    position={{ lat: marker.lat, lng: marker.lng }}
                  />
                ))}
              </GoogleMap>
            )}
            {/* <div className="col-span-12 flex justify-between p-3 bg-[#F7F7FA] items-center">
              <div className="flex gap-4 justify-between">
                <img src={addpointer} alt="" className="w-[22px] h-[22px]" />
                <div>
                  <p>Bidadi</p>
                  <p>Bengaluru, India</p>
                </div>
              </div>
              <Button
                varient="ghost"
                onClick={() => {
                  console.log('wqr');
                }}
                className="w-[154px]  h-[42px]  bg-solid-violet">
                Confirm location12
              </Button>
            </div> */}
          </Card>
        )}

        <div className="col-span-12 grid grid-cols-12">
          {!rowData && (
            <button
              onClick={() => {
                setAddCompleteAddressVisible(!addCompleteAddressVisible);
                formik.validateForm();
                formik.setFieldTouched('countryCode', true);
              }}
              className={`col-span-6 sm:col-span-5 flex gap-3 text-[#1F3A78] border rounded-md border-[#1F3A78] items-center justify-center py-[10px] ${addCompleteAddressVisible ? 'bg-[#1F3A78] text-white' : 'bg-white'}`}>
              <img
                className="w-4 h-4 2xl:w-5 2xl:h-5"
                src={addCompleteAddressVisible ? addTaskIconwi : addTaskIcon}
              />
              <span>Add Complete Address</span>
            </button>
          )}
        </div>
        {(addCompleteAddressVisible || rowData) && (
          <div className="col-span-12 grid grid-cols-12 w-full gap-4">
            {/* Address */}
            <Card
              className={`border-none shadow-none col-span-12 sm:col-span-12`}>
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-sm font-bold">
                  Address Line 1*
                </CardTitle>
              </CardHeader>
              <CardContent>
                <Textarea
                  className="bg-slate-400/10 h-[42px]"
                  placeholder="Enter Address 1"
                  name="address1"
                  onChange={formik.handleChange}
                  // onChange={handleChange}
                  onBlur={formik.handleBlur}
                  value={formik.values.address1}
                />
                {formik.touched.address1 && formik.errors.address1 ? (
                  <span className="error-message">
                    {formik.errors.address1}
                  </span>
                ) : null}
              </CardContent>
            </Card>

            <Card
              className={`border-none shadow-none col-span-12 sm:col-span-12`}>
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-sm font-bold">
                  Address Line 2
                </CardTitle>
              </CardHeader>
              <CardContent>
                <Textarea
                  className="bg-slate-400/10 h-[42px]"
                  placeholder="Enter Address 2"
                  name="address2"
                  onChange={formik.handleChange}
                  // onChange={handleChange}
                  onBlur={formik.handleBlur}
                  value={
                    formik.values.address2 === null
                      ? ''
                      : formik.values.address2
                  }
                />
                {formik.touched.address2 && formik.errors.address2 ? (
                  <div className="text-red-500 mt-2">
                    {formik.errors.address2}
                  </div>
                ) : null}
              </CardContent>
            </Card>
            {/* Country Selector */}
            <Card className="border-none shadow-none col-span-12 sm:col-span-6">
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-sm 2xl:text-md font-bold">
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
                      className="w-full justify-between bg-slate-400/10 border border-input h-[42px]">
                      {/* {selectedCountry?.name || 'Select a country'} */}
                      {formik?.values?.country
                        ? formik?.values?.country
                        : 'Select Country'}
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
                              setSearchQueryCounrty('');
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
            <Card className="border-none shadow-none col-span-12 sm:col-span-6">
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-sm 2xl:text-md font-bold">
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
                      className="w-full justify-between bg-slate-400/10 border border-input h-[42px]">
                      {/* {selectedState?.name || 'Select a state'} */}
                      {formik?.values?.state
                        ? formik?.values?.state
                        : 'Select State'}
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
                              setSearchQueryCounrty('');
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
            <Card className="border-none shadow-none col-span-12 sm:col-span-6">
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-sm 2xl:text-md font-bold">
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
                      className="w-full justify-between bg-slate-400/10 border border-input h-[42px]">
                      {formik?.values?.city
                        ? formik.values.city
                        : 'Select City'}
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
                              setSearchQueryCounrty('');
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
            {/* Zip Code */}
            <Card
              className={`border-none shadow-none col-span-12 sm:col-span-6`}>
              <CardHeader className="flex flex-row items-center justify-between p-2">
                <CardTitle className="text-sm font-bold">ZipCode</CardTitle>
              </CardHeader>
              <CardContent>
                <Input
                  type="number"
                  placeholder={'Zipcode'}
                  className="w-full bg-slate-400/10 border border-input ps-3 placeholder:text-slate-700 h-[42px]"
                  name="zipCode"
                  onChange={formik.handleChange}
                  // onChange={e =>
                  //   formik.setFieldValue('zipCode', e.target.value)
                  // }
                  onBlur={formik.handleBlur}
                  value={formik.values.zipCode}
                  disabled={addCompleteAddressVisible || !!rowData}
                />
                {formik.touched.zipCode && formik.errors.zipCode ? (
                  <span className="error-message">{formik.errors.zipCode}</span>
                ) : null}
              </CardContent>
            </Card>

            {/* <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-sm font-bold">Longitude</CardTitle>
          </CardHeader>
          <CardContent>
            <Input
              type="text"
              placeholder="Longitude"
              className="w-full bg-slate-400/10 border border-input ps-3 h-[42px] placeholder:text-slate-700"
              name="longitude"
              // onChange={formik.handleChange}
              onChange={handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.longitude}
            />
            {formik.touched.longitude && formik.errors.longitude ? (
              <span className="error-message">{formik.errors.longitude}</span>
            ) : null}
          </CardContent>
        </Card>

        <Card className={`border-none shadow-none col-span-12 sm:col-span-6`}>
          <CardHeader className="flex flex-row items-center justify-between p-2">
            <CardTitle className="text-sm font-bold">Latitude</CardTitle>
          </CardHeader>
          <CardContent>
            <Input
              type="text"
              placeholder="Latitude"
              className="w-full bg-slate-400/10 border border-input ps-3 h-[42px] placeholder:text-slate-700"
              name="latitude"
              // onChange={formik.handleChange}
              onChange={handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.latitude}
            />
            {formik.touched.latitude && formik.errors.latitude ? (
              <span className="error-message">{formik.errors.latitude}</span>
            ) : null}
          </CardContent>
        </Card> */}
          </div>
        )}

        <div className="col-span-12 flex justify-end gap-4">
          {/* <DialogClose asChild> */}
          <Button
            type="button"
            variant="ghost"
            onClick={event => {
              event.preventDefault();
              event.stopPropagation();
              onClose();
            }}
            className="bg-transparent text-[#6A6AEC] col-span-2">
            Discard
          </Button>
          {/* </DialogClose> */}
          {/* <DialogClose asChild> */}
          <Button
            className=" sm:col-start-1 md:col-start-6  xl:col-start-10 col-span-8 md:col-span-4 xl:col-span-3 bg-solid-violet"
            disabled={!formik.isValid || !formik.dirty}
            onClick={async () => {
              // setFormChanged(false);
              await handleSaveChanges();
              onClose();
            }}>
            {rowData ? 'Save Changes' : 'Add Client'}
          </Button>
          {/* </DialogClose> */}
        </div>
      </form>
    </div>
  );
};

export default AddNewClientModal;
