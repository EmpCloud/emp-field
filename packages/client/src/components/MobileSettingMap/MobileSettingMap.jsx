import React, { useRef, useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
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

const MobileSettingMap = () => {
  const [range, setRange] = useState(0);
  const [currentLat, setcurrentLong] = useState(null);
  const mapContainerStyle = {
    width: '100%',
    height: '100%',
  };
  const autocompleteRef = useRef(null);

  const onPlaceChanged = () => {
    const place = autocompleteRef.current.getPlace();
    if (place.geometry) {
      const location = place.geometry.location;
      setMarkers(currentMarkers => [
        ...currentMarkers,
        { lat: location.lat(), lng: location.lng() },
      ]);
      setCenter({
        lat: location.lat(),
        lng: location.lng(),
      });
    }
  };

  const libraries = ['places'];

  const { isLoaded } = useLoadScript({
    googleMapsApiKey: import.meta.env.VITE_SOME_KEY,
    libraries: libraries,
  });

  const [center, setCenter] = useState({
    lat: 14.477234, // default center coordinates
    lng: 78.804932,
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
    // setCenter(newMarker);
    setcurrentLong(newMarker);

    alert(`Lat, Lng : ${event.latLng.lat()}, ${event.latLng.lng()}`);
    //   console.log('Map clicked, new marker:', newMarker);
  };

  const [onChangeRange, setOnChangeRange] = useState(range);
  const [longitude, setLongitude] = useState('');
  const [latitude, setLatitude] = useState('');

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

  // useEffect(() => {
  //   console.log('Markers:', markers);
  // }, [markers]);

  // useEffect(() => {
  //   console.log('Center:', center);
  // }, [center]);

  // useEffect(() => {
  //   console.log('Latitude:', latitude, 'Longitude:', longitude);
  // }, [latitude, longitude]);

  return (
    <>
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
      />
      <div className="card-shadow grid gap-4 col-span-12 xl:col-span-6 bg-white rounded-lg">
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
                    position: window.google.maps.ControlPosition.BOTTOM_LEFT,
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
                        {/* <PopoverContent
                          onOpenAutoFocus={handleAutoFocus}
                          align="start"
                          className=" max-h-[200px] overflow-y-auto m-0 p-3">
                          <CardTitle className="text-sm font-medium pb-2 flex flex-row items-center justify-between hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                            Bella
                          </CardTitle>
                          <CardTitle className="text-sm font-medium pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                            Madeleine
                          </CardTitle>
                          <CardTitle className="text-sm font-medium pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                            Christopher
                          </CardTitle>
                          <CardTitle className="text-sm font-medium pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                            Max
                          </CardTitle>
                          <CardTitle className="text-sm font-medium pb-2 hover:bg-slate-400/10 px-4 py-1 cursor-pointer">
                            Sebastians
                          </CardTitle>
                        </PopoverContent> */}
                      </Popover>
                    </CardContent>
                  </Card>
                </Autocomplete>
                {markers.slice(-1).map((marker, index) => (
                  <React.Fragment key={index}>
                    <Marker
                      position={{
                        lat: marker.lat ?? 14.477234,
                        lng: marker.lng ?? 78.804932,
                      }}
                    />
                    <Circle
                      center={{
                        lat: marker.lat ?? 14.477234,
                        lng: marker.lng ?? 78.804932,
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
    </>
  );
};

export default MobileSettingMap;
