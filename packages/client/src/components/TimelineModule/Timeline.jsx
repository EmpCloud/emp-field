import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import TimeLineBox from './TimeLineBox';
import SpeedIcon from '@assets/images/speed-icon.png';
import BatteryIcon from '@assets/images/battery-icon.png';
import CarIcon from '@assets/images/car-icon.png';
import BikeIcon from '@assets/images/bike-icon.png';
import AutoIcon from '@assets/images/auto.png';
import BusIcon from '@assets/images/bus.png';
import TrainIcon from '@assets/images/Train.png';
import { Progress } from '@/components/ui/progress';

import TimeStartEndBox from './TimeStartEndBox';
import moment from 'moment';
import { useQuery } from '@tanstack/react-query';
import { getLiveTrackingTimeLineData } from './Api/post';

const formatTime = dateTimeString => {
  // Check if it's a full ISO date-time string
  if (moment(dateTimeString, moment.ISO_8601, true).isValid()) {
    // Parse the date-time string as UTC and format it
    return moment.utc(dateTimeString).format('h:mm A');
  } else if (moment(dateTimeString, 'HH:mm:ss', true).isValid()) {
    // It's a time string only (assuming it's in local time)
    return moment(dateTimeString, 'HH:mm:ss').format('h:mm A');
  } else {
    // Handle invalid or empty strings
    return '';
  }
};

const Timeline = ({
  employee,
  taskDetails,
  employeEmpId,
  isPending,
  date,
  locationData,
  transportDetails,
}) => {
  const filteredData = locationData?.trackingData[0]?.geologs?.filter(
    item => item.status === 5 || item.status === 6
  );

  const modeIcons = {
    car: CarIcon,
    bike: BikeIcon,
    auto: AutoIcon,
    bus: BusIcon,
    train: TrainIcon,
  };

  const getStatusString = status => {
    switch (status) {
      case 0:
        return 'Pending';
      case 1:
        return 'Start';
      case 2:
        return 'Pause';
      case 3:
        return 'Resume';
      case 4:
        return 'Finish';
      case 5:
        return 'Delete';
      default:
        return 'unknown status';
    }
  };

  const response = useQuery({
    queryKey: ['getLiveTrackingTimeLineData', date, employeEmpId],
    queryFn: () => getLiveTrackingTimeLineData(date, employeEmpId),
  });

  // Access the data and reverse it
  const timeLineDetail = response?.data?.data.body.data;
  const timeLineDetails = timeLineDetail ? [...timeLineDetail].reverse() : []; // Reverse the array safely

  // Log the reversed timeline details

  return (
    <>
      {employeEmpId ? (
        <div className="card-shadow grid gap-4 col-span-12 xl:col-span-6 bg-white rounded-lg">
          {isPending ? (
            <Card className="animate-pulse">
              <CardHeader className="flex flex-row items-center bg-slate-300 rounded-t-lg px-4 2xl:h-12 h-10">
                <div className="flex justify-start items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-slate-200"></div>
                  <div className="flex flex-col justify-center items-center gap-1">
                    <div className="w-40 h-4 rounded-sm bg-slate-200"></div>
                    <div className="w-40 h-2 rounded-sm bg-slate-200"></div>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="h-[300px] 2xl:h-[500px] flex flex-col gap-3 justify-start items-start p-6">
                <div className="bg-slate-200 h-10 w-60 rounded-sm"></div>
                <div className="bg-slate-200 h-32 w-full rounded-sm"></div>
                <div className="bg-slate-200 h-32 w-full rounded-sm"></div>
                <div className="bg-slate-200 h-32 w-full rounded-sm"></div>
              </CardContent>
            </Card>
          ) : (
            <Card className="pb-3">
              <CardHeader className="flex flex-row items-center bg-gradient rounded-t-lg px-4 py-2 2xl:h-12 min-h-10">
                <div className="flex flex-wrap items-center justify-between gap-3 border-white w-full">
                  <div className="flex items-center justify-start gap-3">
                    <Avatar className="h-7 2xl:h-9 w-7 2xl:w-9">
                      <AvatarImage
                        src={
                          (employee?.profilePic &&
                            employee?.profilePic === 'PROFILE') ||
                          employee?.profilePic === 'profile'
                            ? 'https://api.dicebear.com/8.x/initials/svg?seed=' +
                              employee?.fullName
                            : employee?.profilePic ??
                              'https://api.dicebear.com/8.x/initials/svg?seed=' +
                                employee?.fullName
                        }
                        alt="profile"
                      />
                      <AvatarFallback></AvatarFallback>
                    </Avatar>
                    <div>
                      <CardTitle className="text-xs 2xl:text-sm font-bold text-white">
                        {employee?.fullName ?? ''}
                      </CardTitle>
                      <CardDescription className="text-white text-[0.6rem]">
                        {employee?.role ?? ''}
                      </CardDescription>
                    </div>
                  </div>
                  <div className="flex justify-center items-center gap-2">
                    {locationData &&
                    locationData?.trackingData[0]?.distTravelled > 1 ? (
                      <div className="flex justify-center items-center text-white gap-1 text-xs">
                        {' '}
                        <img
                          src={
                            modeIcons[transportDetails?.currentMode] || BikeIcon
                          }
                          alt={transportDetails?.currentMode || 'bike'}
                        />
                        <span>
                          {Math.ceil(
                            locationData?.trackingData[0]?.distTravelled
                          ) + ' km' || 0}
                        </span>
                      </div>
                    ) : null}
                    {/* <div className="flex justify-center items-center text-white gap-1 text-xs">
                      {' '}
                      <img src={SpeedIcon} alt="speed" /> <span>40 km/h</span>
                    </div>
                    <div className="flex justify-center items-center text-white gap-1 text-xs">
                      {' '}
                      <img src={BatteryIcon} alt="speed" /> <span>66%</span>
                    </div> */}
                  </div>
                </div>
              </CardHeader>
              <CardContent className="h-[300px] 2xl:h-[500px] card-container px-4">
                <h3 className="text-sm 2xl:text-lg pt-3 pl-3 py-3 font-bold sticky top-0 bg-white z-[1]">
                  Timeline
                </h3>
                <div className="timeline-list-container">
                  {taskDetails?.length || filteredData?.length > 0 ? (
                    <>
                      {filteredData === undefined ? null : filteredData &&
                        filteredData[1]?.time ? (
                        <TimeStartEndBox
                          taskName={'Logout  Successfully'}
                          progress={'Finish'}
                          loginTime={formatTime(filteredData[1]?.time) ?? ''}
                          checkIn={false}
                        />
                      ) : (
                        ''
                      )}
                      {timeLineDetails &&
                        timeLineDetails?.map(userTaskDetails => (
                          <>
                            <TimeLineBox
                              progress={getStatusString(
                                userTaskDetails?.taskDetails?.taskApproveStatus
                              )}
                              key={userTaskDetails?.taskDetails?._id}
                              value={userTaskDetails?.taskPercentage}
                              startTime={
                                formatTime(
                                  userTaskDetails?.taskDetails?.start_time
                                ) ?? ''
                              }
                              endTime={
                                formatTime(
                                  userTaskDetails?.taskDetails?.end_time
                                ) ?? ''
                              }
                              taskName={
                                userTaskDetails?.taskDetails?.taskName ?? ''
                              }
                              empStartTime={
                                formatTime(
                                  userTaskDetails?.taskDetails?.empStartTime
                                ) ?? ''
                              }
                              empEndTime={
                                userTaskDetails?.taskDetails?.empEndTime !==
                                null
                                  ? formatTime(
                                      userTaskDetails?.taskDetails?.empEndTime
                                    )
                                  : ''
                              }
                              clientName={
                                userTaskDetails?.clientData?.clientName ?? ''
                              }
                              taskDesc={
                                userTaskDetails?.taskDetails?.taskDescription ??
                                ''
                              }
                              time={formatTime(
                                userTaskDetails?.taskDetails?.time ?? ''
                              )}
                            />
                          </>
                        ))}

                      {filteredData === undefined
                        ? ''
                        : filteredData && (
                            <TimeStartEndBox
                              taskName={'Logged In Successfully'}
                              progress={'Finish'}
                              loginTime={
                                formatTime(filteredData[0]?.time) ?? '-'
                              }
                              checkIn={true}
                            />
                          )}
                    </>
                  ) : (
                    <h1 className="text-center">
                      {date === 'NaN-NaN-NaN'
                        ? 'please select date with in the Range'
                        : ' No task found'}
                    </h1>
                  )}
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      ) : (
        ''
      )}
    </>
  );
};

export default Timeline;
