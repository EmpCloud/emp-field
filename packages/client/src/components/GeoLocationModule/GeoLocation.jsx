import { useState, useEffect } from 'react';
import {
  GoogleMap,
  MarkerF,
  PolylineF,
  useLoadScript,
  CircleF,
  OverlayView,
} from '@react-google-maps/api';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import EmpLogoMarker from '../../../src/assets/images/EMP-marker.png';
import CheckinImg from '../../assets/images/mapicons/Checkin.png';
import StartTask from '../../assets/images/mapicons/Starttask.png';
import PauseTask from '../../assets/images/mapicons/Pause.png';
import ResumeTask from '../../assets/images/mapicons/Resume.png';
import FinishTask from '../../assets/images/mapicons/Finish.png';
import CheckOutImg from '../../assets/images/mapicons/Checkout.png';
import CustomMarkerImage from './CustomMarkerImage';
import { Separator } from '@/components/ui/separator';
// import moment from 'moment';
import moment from 'moment-timezone';
import Cookies from 'js-cookie';
import { DecodeJWTToken } from '../../context/Filters/util.token';

const GeoLocation = ({
  date,
  locationData,
  employeEmpId,
  isPending,
  transportDetails,
  employeeProfile,
}) => {
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: import.meta.env.VITE_SOME_KEY,
  });

  // Get token and decode orgId
  const token = Cookies.get('token');
  const orgId = DecodeJWTToken(token);
  const hideGoogleMapsOrgIds =
    import.meta.env.VITE_HIDE_GOOGLE_MAPS_FEATURE_ORG?.split(',').map(id =>
      id.trim()
    ) || [];
  const isHideGoogleMapsOrg = hideGoogleMapsOrgIds.includes(String(orgId));

  // Extracting tracking data - flatten if needed to support both single geologs object and array format
  const { trackingData } = locationData;

  const markers =
    trackingData
      ?.map((item, itemIndex) => {
        // Handle both array of geologs and single geologs object
        const geologsData = Array.isArray(item.geologs)
          ? item.geologs
          : [item.geologs];

        return geologsData.map((geolog, geologIndex) => {
          // Get taskId from geolog first, then fall back to item properties
          const taskDetails = geolog.taskId || item.taskDetails;

          return {
            time: geolog.time || item.taskDetails?.time,
            latitude: geolog.latitude,
            longitude: geolog.longitude,
            status: geolog.status || 0,
            taskId: taskDetails,
            taskName: geolog.taskId?.taskName || item.taskDetails?.taskName,
            taskDescription:
              geolog.taskId?.taskDescription ||
              item.taskDetails?.taskDescription,
            _id: geolog._id || `${item._id}-${itemIndex}-${geologIndex}`,
            clientDetails: item.clientDetails,
            distTravelled: item.distTravelled,
          };
        });
      })
      .flat() || [];

  // State for reverse geocoding data
  const [geocodingData, setGeocodingData] = useState({});
  const [loadingGeocoding, setLoadingGeocoding] = useState(false);

  // Fetch reverse geocoding data for non-tracking markers only
  useEffect(() => {
    if (!isHideGoogleMapsOrg || markers.length === 0) return;

    setLoadingGeocoding(true);
    const fetchGeocoding = async () => {
      const geocodingResults = {};
      const apiKey = '699edfe621107149723356uyd2f9cd8';

      // Only fetch geocoding for non-tracking markers (status !== 0)
      const nonTrackingMarkers = markers.filter(marker => marker.status !== 0);

      for (const marker of nonTrackingMarkers) {
        try {
          const response = await fetch(
            `https://geocode.maps.co/reverse?lat=${marker.latitude}&lon=${marker.longitude}&api_key=${apiKey}`
          );
          if (response.ok) {
            const data = await response.json();
            geocodingResults[marker._id] = data;
          } else {
            geocodingResults[marker._id] = { error: 'Failed to fetch address' };
          }
        } catch (error) {
          console.error('Geocoding error:', error);
          geocodingResults[marker._id] = { error: error.message };
        }
      }

      setGeocodingData(geocodingResults);
      setLoadingGeocoding(false);
    };

    fetchGeocoding();
  }, [date]);

  const statusIcons = {
    5: CheckinImg, // checkin
    1: StartTask, // start
    2: PauseTask, //pause
    3: ResumeTask, //resume
    4: FinishTask, //finish
    6: CheckOutImg, //checkout
  };

  const [activeMarker, setActiveMarker] = useState(null);
  const lastStatusZeroMarker = markers
    .slice()
    .reverse()
    .find(marker => marker.status === 0);

  const lastMarkerStatus = markers[markers.length - 1]?.status;
  const shouldShowLivePosition = lastMarkerStatus === 0;

  const [defaultCenter, setDefaultCenter] = useState(null);

  const handleActiveMarker = (marker, latitude, longitude) => {
    setDefaultCenter({ lat: latitude, lng: longitude });
    setActiveMarker(marker);
  };

  const getStatusStringMaps = status => {
    switch (status) {
      case 0:
        return 'Tracking';
      case 1:
        return 'Started';
      case 2:
        return 'Paused';
      case 3:
        return 'Resumed';
      case 4:
        return 'Finished';
      case 5:
        return 'Check-In';
      case 6:
        return 'Check-Out';
      default:
        return '';
    }
  };

  const mapColor = {
    1: '#6A6AEC',
    2: '#FF5960',
    3: '#FFB800',
    4: '#4DB948',
    5: '#1181EC',
    6: '#E9333B',
  };

  let lastColor = '#6A6AEC';

  const paths = markers.reduce((acc, marker, index, arr) => {
    if (index === 0) return acc;
    const prevMarker = arr[index - 1];
    if (marker.status !== 0) {
      lastColor = mapColor[marker.status] || '#6A6AEC';
    }
    acc.push({
      start: { lat: prevMarker.latitude, lng: prevMarker.longitude },
      end: { lat: marker.latitude, lng: marker.longitude },
      color: lastColor,
    });
    return acc;
  }, []);

  const filteredMarkers = markers.filter(marker => marker.status !== 0);

  // const lastMarkerMap = filteredMarkers.reduce((acc, marker) => {
  //   if (marker.taskId && marker.taskId._id) {
  //   key = `${marker.taskId._id}-${marker.status}`;
  // } // Unique key for each combination of taskId and status
  //   acc[key] = marker; // The latest occurrence will overwrite the previous one
  //   return acc;
  // }, {});

  // const lastMarkerMap = filteredMarkers.reduce((acc, marker) => {
  //   let key = marker.status; // Default key is based on status
  //   if (marker.taskId && marker.taskId._id) {
  //     key = `${marker.taskId._id}-${marker.status}`;
  //     acc[key] = marker;
  //   } else {
  //     // Handle the case where marker.taskId or marker.taskId._id is null or undefined
  //     key = `null-${marker.status}`;
  //     acc[key] = marker; // Create a default key for markers with null taskId
  //   }
  //   return acc;
  // }, {});

  // const uniqueMarkers = Object.values(lastMarkerMap);

  const zeroStatusMarkersMap = markers.reduce((acc, marker) => {
    if (marker.status === 0) {
      const key = marker.taskId?._id;
      acc[key] = marker; // The latest occurrence will overwrite the previous one
    }
    return acc;
  }, {});
  const lastZeroStatusMarkers = Object.values(zeroStatusMarkersMap);
  // useEffect(() => {
  //   if (lastZeroStatusMarkers) {
  //     setLivePosition({
  //       lat: lastZeroStatusMarkers.slice(-1)[0]?.latitude,
  //       lng: lastZeroStatusMarkers.slice(-1)[0]?.longitude,
  //     });
  //   }
  //   // }, [lastStatusZeroMarker]);
  // }, []);

  const livePosition = {
    lat: lastZeroStatusMarkers.slice(-1)[0]?.latitude,
    lng: lastZeroStatusMarkers.slice(-1)[0]?.longitude,
  };

  return employeEmpId ? (
    <div className="card-shadow grid gap-4 col-span-12 xl:col-span-6 bg-white rounded-lg">
      {isPending ? (
        <Card className="animate-pulse">
          <CardHeader className="flex flex-row items-center bg-slate-300 rounded-t-lg px-4 2xl:h-12 h-10">
            <div className="flex justify-start items-center gap-3">
              <div className="flex flex-col justify-center items-center gap-1">
                <div className="w-40 h-4 rounded-sm bg-slate-200"></div>
              </div>
            </div>
          </CardHeader>
          <CardContent className="h-[300px] 2xl:h-[500px] flex flex-col gap-3 justify-start items-start p-6">
            <div className="bg-slate-200 h-full w-full rounded-sm"></div>
          </CardContent>
        </Card>
      ) : isHideGoogleMapsOrg ? (
        <Card>
          <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 2xl:h-12 h-10">
            <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
              Location Details
            </CardTitle>
          </CardHeader>
          <CardContent className="overflow-x-auto overflow-y-auto h-[300px] 2xl:h-[500px] bg-white p-0">
            {loadingGeocoding ? (
              <div className="flex justify-center items-center h-[300px]">
                <p className="text-sm">Loading location details...</p>
              </div>
            ) : markers.filter(marker => marker.status !== 0).length > 0 ? (
              <Table className="bg-white">
                <TableHeader>
                  <TableRow className="sticky top-0 bg-[#F1F1FF] z-10 h-[42px]">
                    <TableHead className="text-[#1F3A78] font-bold whitespace-nowrap">
                      Time
                    </TableHead>
                    <TableHead className="text-[#1F3A78] font-bold whitespace-nowrap">
                      Address
                    </TableHead>
                    <TableHead className="text-[#1F3A78] font-bold whitespace-nowrap">
                      Task Name
                    </TableHead>
                    <TableHead className="text-[#1F3A78] font-bold whitespace-nowrap">
                      Task Description
                    </TableHead>
                    <TableHead className="text-[#1F3A78] font-bold whitespace-nowrap">
                      Status
                    </TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {markers
                    .filter(marker => marker.status !== 0)
                    .reverse()
                    .map(marker => {
                      const geocode = geocodingData[marker._id];
                      const displayName =
                        geocode?.display_name ||
                        `Lat: ${marker.latitude.toFixed(4)}, Lon: ${marker.longitude.toFixed(4)}`;
                      const formatDateTime = date => {
                        if (!date) return '-';

                        return moment.utc(date).format('YYYY-MM-DD hh:mm:ss A');
                      };

                      return (
                        <TableRow key={marker._id} className="h-[42px]">
                          <TableCell className="py-1 px-4 text-[#4D4C4C] text-xs font-medium whitespace-nowrap">
                            {formatDateTime(marker.time)}
                          </TableCell>
                          <TableCell className="py-1 px-4 text-[#4D4C4C] text-xs font-medium max-w-xs">
                            <div className="flex items-center gap-2">
                              {/* <span className="truncate" title={displayName}>
                                {displayName}
                              </span> */}
                              <button
                                onClick={() =>
                                  window.open(
                                    `https://www.google.com/maps?q=${marker.latitude},${marker.longitude}`,
                                    '_blank'
                                  )
                                }
                                className="flex-shrink-0 cursor-pointer hover:opacity-70 transition-opacity"
                                title="Open in Google Maps">
                                <img
                                  src={CheckinImg}
                                  alt="Map"
                                  className="w-4 h-4"
                                />
                              </button>
                            </div>
                          </TableCell>
                          <TableCell className="py-1 px-4 text-[#4D4C4C] text-xs font-medium whitespace-nowrap">
                            {marker.taskName ?? '—'}
                          </TableCell>
                          <TableCell
                            className="py-1 px-4 text-[#4D4C4C] text-xs font-medium max-w-xs truncate"
                            title={marker.taskDescription ?? ''}>
                            {marker.taskDescription ?? '—'}
                          </TableCell>
                          <TableCell className="py-1 px-4 text-[#4D4C4C] text-xs font-medium whitespace-nowrap">
                            {getStatusStringMaps(marker.status)}
                          </TableCell>
                        </TableRow>
                      );
                    })}
                </TableBody>
              </Table>
            ) : (
              <div className="flex justify-center items-center h-[300px]">
                <h1 className="text-center text-[#4D4C4C]">
                  No tracking data found
                </h1>
              </div>
            )}
          </CardContent>
        </Card>
      ) : (
        <Card>
          <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 2xl:h-12 h-10">
            <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
              Map Preview
            </CardTitle>
          </CardHeader>
          <CardContent className="p-3 h-[300px] 2xl:h-[500px] card-container w-full flex justify-center flex-col items-center">
            {isLoaded &&
            locationData &&
            locationData?.trackingData &&
            locationData?.trackingData?.length > 0 ? (
              <GoogleMap
                center={
                  defaultCenter == null
                    ? {
                        lat: markers?.slice(-1)[0]?.latitude || 0,
                        lng: markers?.slice(-1)[0]?.longitude || 0,
                      }
                    : defaultCenter
                }
                zoom={16}
                onClick={() => setActiveMarker(null)}
                mapContainerStyle={{ width: '100%', height: '100%' }}>
                {filteredMarkers?.map(
                  ({
                    _id,
                    taskId,
                    taskName,
                    taskDescription,
                    latitude,
                    longitude,
                    status,
                    time,
                  }) => (
                    <MarkerF
                      key={_id}
                      position={{ lat: latitude, lng: longitude }}
                      onClick={() => {
                        handleActiveMarker(_id, latitude, longitude);
                        window.open(
                          `https://www.google.com/maps?q=${latitude},${longitude}`,
                          '_blank'
                        );
                      }}
                      icon={{
                        url: statusIcons[status],
                        scaledSize: { width: 50, height: 50 },
                      }}>
                      {activeMarker === _id && (
                        <OverlayView
                          position={{ lat: latitude, lng: longitude }}
                          mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}>
                          <>
                            <div className="tooltip-map flex flex-col items-start justify-center gap-1">
                              <div className="text-xs 2xl:text-sm">
                                {getStatusStringMaps(status)}
                              </div>
                              {taskId && (
                                <>
                                  <Separator />
                                  <div className="text-[#4D4C4C] font-normal text-xs 2xl:text-sm w-60">
                                    <div className="font-semibold">
                                      {time
                                        ? moment(time).format(
                                            'YYYY-MM-DD HH:mm:ss'
                                          )
                                        : '—'}
                                    </div>
                                    <div className="w-full">
                                      <span>Task Name: </span>
                                      <span className="font-semibold text-wrap">
                                        {taskName ?? ''}
                                      </span>
                                    </div>
                                    <div className="w-full">
                                      <span>Task Description: </span>
                                      <span className="font-semibold text-wrap">
                                        {taskDescription ?? ''}
                                      </span>
                                    </div>
                                  </div>
                                </>
                              )}
                            </div>
                          </>
                        </OverlayView>
                      )}
                    </MarkerF>
                  )
                )}

                {paths.map(({ start, end, color }, index) => (
                  <PolylineF
                    key={index}
                    path={[start, end]}
                    options={{
                      strokeColor: color,
                      strokeOpacity: 1.0,
                      strokeWeight: 4,
                    }}
                  />
                ))}
                {shouldShowLivePosition && (
                  <>
                    {employeeProfile && employeeProfile !== 'PROFILE' ? (
                      <CustomMarkerImage
                        livePosition={livePosition}
                        employeeProfile={employeeProfile}
                      />
                    ) : (
                      <MarkerF
                        position={livePosition}
                        icon={{
                          url: EmpLogoMarker,
                          scaledSize: { width: 50, height: 50 },
                        }}
                      />
                    )}
                    <CircleF
                      center={livePosition}
                      radius={transportDetails?.currentRadius ?? 50}
                      options={{
                        strokeColor: '#1D4381',
                        strokeOpacity: 0.4,
                        strokeWeight: 2,
                        fillColor: '#5F96F0',
                        fillOpacity: 0.35,
                      }}
                    />
                  </>
                )}
              </GoogleMap>
            ) : (
              <h1 className="text-center">No tracking data found</h1>
            )}
          </CardContent>
        </Card>
      )}
    </div>
  ) : null;
};

export default GeoLocation;
