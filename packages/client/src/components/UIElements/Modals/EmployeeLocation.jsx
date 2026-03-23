import React, { useRef, useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Dialog, DialogContent } from '@/components/ui/dialog';
import {
  GoogleMap,
  LoadScript,
  Marker,
  Circle,
  useLoadScript,
  Autocomplete,
} from '@react-google-maps/api';
import GeoLocationSetting from 'components/GeoLocationSetting/GeoLocationSetting';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { Input } from '@/components/ui/input';
import { Search } from 'lucide-react';
import { getEmployeeLocationById } from './Api/get';

const AlertModal = ({ row, setOpenLocationModal }) => {
  // console.log('row:', row);

  // useEffect(() => {
  //   const fetchLocation = async () => {
  //     const response = await getEmployeeLocationById(row._id);

  //     if (response?.data?.body?.status === 'success') {
  //       console.log(
  //         'Location fetched successfully:',
  //         response?.data?.body?.data
  //       );
  //     }
  //   };
  //   fetchLocation();
  // }, [row._id]);

  const [range, setRange] = useState(5);
  const [currentLat, setcurrentLong] = useState(null);
  const mapContainerStyle = {
    width: '100%',
    height: '100%',
  };
  const autocompleteRef = useRef(null);

  const onPlaceChanged = () => {
    const place = autocompleteRef.current.getPlace();
    if (place.geometry && place.geometry.location) {
      const location = place.geometry.location;
      setMarkers(currentMarkers => [
        ...currentMarkers,
        { lat: location.lat(), lng: location.lng() },
      ]);
      setCenter({
        lat: location.lat(),
        lng: location.lng(),
      });
      setLatitude(location.lat().toString());
      setLongitude(location.lng().toString());
    }
  };

  const libraries = ['places'];

  const { isLoaded } = useLoadScript({
    googleMapsApiKey: import.meta.env.VITE_SOME_KEY,
    libraries: libraries,
  });

  const [center, setCenter] = useState({
    lat: 12.9352, // default center coordinates
    lng: 77.6245,
  });

  const [markers, setMarkers] = useState([]);

  const handleMapClick = event => {
    const newMarker = {
      lat: event.latLng.lat(),
      lng: event.latLng.lng(),
    };

    setMarkers(currentMarkers => [...currentMarkers, newMarker]);
    setcurrentLong({
      lat: event.latLng.lat(),
      lng: event.latLng.lng(),
    });
    setCenter(newMarker);
    setLatitude(event.latLng.lat().toString());
    setLongitude(event.latLng.lng().toString());

    alert(`Lat, Lng : ${event.latLng.lat()}, ${event.latLng.lng()}`);
  };

  const [onChangeRange, setOnChangeRange] = useState(range);
  const [longitude, setLongitude] = useState('');
  const [latitude, setLatitude] = useState('');
  const [hideLocation, sethideLocation] = useState(false);

  useEffect(() => {
    if (latitude && longitude) {
      const defaultMarker = {
        lat: parseFloat(latitude),
        lng: parseFloat(longitude),
      };
      setMarkers([defaultMarker]);
      setCenter(defaultMarker);
    }
  }, [latitude, longitude]);

  const handleAutoFocus = e => {
    e.preventDefault();
  };

  return (
    <Dialog open={true} modal={true} onOpenChange={() => {}} defaultOpen={true}>
      <DialogContent onInteractOutside={e => e.preventDefault()}>
        <button
          className="absolute top-2 right-2 text-gray-500 border-none outline-none hover:text-blue-500 bg-white"
          onClick={() => setOpenLocationModal(false)}>
          X
        </button>
        <div className="grid gap-4 grid-cols-12 col-span-12 p-6 overflow-auto max-h-[300px] lg:max-h-[500px]">
          <div className="col-span-12 lg:col-span-6">
            <GeoLocationSetting
              setRange={setRange}
              currentLat={markers.slice(-1)}
              onChangeRange={onChangeRange}
              setOnChangeRange={setOnChangeRange}
              range={range}
              setLongitude={setLongitude}
              longitude={longitude}
              latitude={latitude}
              setLatitude={setLatitude}
              hideLocation="hidden"
              contentmargin="mt-[18px]"
              employeeId={row._id}
              setOpenLocationModal={setOpenLocationModal}
            />
          </div>
          <div className="card-shadow grid gap-4 col-span-12 lg:col-span-6 bg-white rounded-lg">
            <Card>
              <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 2xl:h-12 h-10">
                <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
                  Map Preview
                </CardTitle>
              </CardHeader>
              <CardContent className="h-[420px] 2xl:h-[600px] card-container-no-head p-4">
                {isLoaded && (
                  <GoogleMap
                    options={{
                      mapTypeControl: true,
                      mapTypeControlOptions: {
                        position:
                          window.google.maps.ControlPosition.BOTTOM_LEFT,
                      },
                    }}
                    mapContainerStyle={mapContainerStyle}
                    zoom={19}
                    center={center}
                    onClick={handleMapClick}>
                    <Autocomplete
                      onLoad={ref => (autocompleteRef.current = ref)}
                      onPlaceChanged={onPlaceChanged}>
                      <Card className="border-none shadow-none absolute top-5 left-1/2 -translate-x-1/2 w-[60%]">
                        <CardContent>
                          <Popover className="relative">
                            <PopoverTrigger asChild className="">
                              <div className="flex justify-between items-center bg-custom-slate px-4 py-1">
                                <Search size={16} />
                                <Input
                                  type="search"
                                  placeholder="Search for a place"
                                  className="bg-transparent ps-3 text-xs 2xl:text-sm"
                                />
                              </div>
                            </PopoverTrigger>
                          </Popover>
                        </CardContent>
                      </Card>
                    </Autocomplete>
                    {markers.slice(-1).map((marker, index) => (
                      <React.Fragment key={index}>
                        <Marker
                          position={{
                            lat: marker.lat ?? 12.9352,
                            lng: marker.lng ?? 77.6245,
                          }}
                        />
                        <Circle
                          center={{
                            lat: marker.lat ?? 12.9352,
                            lng: marker.lng ?? 77.6245,
                          }}
                          radius={range} // specify the radius in meters
                          options={{
                            fillColor: 'rgba(255, 0, 0, 0.2)',
                            strokeColor: 'rgba(255, 0, 0, 0.5)',
                            strokeWeight: 2,
                          }}
                        />
                      </React.Fragment>
                    ))}
                  </GoogleMap>
                )}
              </CardContent>
            </Card>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default AlertModal;
