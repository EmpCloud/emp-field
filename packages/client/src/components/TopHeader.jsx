import { RxDotFilled } from 'react-icons/rx';
import { BsBellFill } from 'react-icons/bs';
import { AiOutlineMenu } from 'react-icons/ai';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Link, useNavigate } from 'react-router-dom';

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';

import ProfilePhotoDefault from '@assets/images/profile.png';
import { setIsSidebarOpen, useUIController } from 'context/context';
import usePathString from 'hooks/UsePathString';
import Cookies from 'js-cookie';
import { toast } from 'sonner';
import { fetchProfile } from 'page/user/Profile/Api/get';
import { useQuery } from '@tanstack/react-query';
import { useEffect, useState } from 'react';

const TopHeader = () => {
  // const { isLoading, error, data } = useQuery({
  //   queryKey: ['profileDataAdmin'],
  //   queryFn: fetchProfile,
  // });
  const [controller, dispatch] = useUIController();
  const { isSidebarOpen } = controller;
  const headerName = usePathString();

  const [profileDataAdmin, setprofileDataAdmin] = useState(null);

  const handleProfileFetch = () => {
    fetchProfile()
      .then(res => {
        setprofileDataAdmin(res?.data?.body?.data?.userData);
      })
      .catch(error => {
        Cookies.remove('token');
        navigate('/admin/login');
      });
  };

  useEffect(() => {
    handleProfileFetch();
  }, []);

  const toggleSidebar = () => {
    setIsSidebarOpen(dispatch, !isSidebarOpen);
  };

  const navigate = useNavigate();

  const handleAdminLogout = () => {
    Cookies.remove('token');
    navigate('/admin/login');
    toast.success('Admin logged out Successfully');
  };

  function parseJwt(token) {
    if (!token) {
      console.error('Token is missing or invalid');
      return null; // Return null or handle as necessary
    }

    const parts = token.split('.');

    if (parts.length !== 3) {
      console.error('Invalid JWT token format');
      return null; // Return null if token format is not valid
    }

    const base64Url = parts[1]; // Get the payload part of the token
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');

    try {
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
          })
          .join('')
      );

      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error('Error decoding JWT', error);
      return null;
    }
  }

  // Function to check if token is expired
  function isTokenExpired(token) {
    if (!token) {
      console.error('No token provided');
      return true; // Handle error or return as expired if token is missing
    }

    const decodedToken = parseJwt(token); // Decoding the token

    if (!decodedToken) {
      console.error('Failed to decode token');
      return true; // Return true if token decoding failed
    }

    const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
    const expireDuration = decodedToken.exp - currentTime; // Time until expiration
    // Check if the token is expired
    if (expireDuration < 0) {
      setTimeout(() => {
        Cookies.remove('token'); // Remove the token
        Cookies.remove('createdAt'); // Remove any other relevant cookies
        navigate('/admin/login'); // Example: redirecting to the login page
      }, expireDuration * 1000);

      toast.info('Token is expired. Please log in again.');
      // Handle token expiration (e.g., redirect to login)
      return true; // Token is expired
    } else {
      setTimeout(() => {
        toast.info('Token is expired. Please log in again.');
        Cookies.remove('token'); // Remove the token
        Cookies.remove('createdAt'); // Remove any other relevant cookies
        navigate('/admin/login'); // Example: redirecting to the login page
      }, expireDuration * 1000); // Convert seconds to milliseconds

      return false; // Token is valid
    }
  }

  // Usage
  const token = Cookies.get('token'); // Replace with your method of obtaining the token
  isTokenExpired(token);

  useEffect(() => {
    if (isTokenExpired(Cookies.get('token'))) {
      console.log('Token is expired');
    } else {
      console.log('Token is still valid');
    }
  }, [Cookies.get('token')]);

  return (
    <>
      <div
        className={`topbar ${isSidebarOpen ? 'w-full lg:w-[calc(100%-150px)] 2xl:w-[calc(100%-250px)]' : 'w-full lg:w-[calc(100%-60px)] 2xl:w-[calc(100%-100px)]'}`}>
        <nav className="flex gap-3 justify-between items-center w-full">
          <div className="flex gap-8 items-center">
            <AiOutlineMenu
              className="text-white cursor-pointer 2xl:w-6 2xl:h-6"
              onClick={() => toggleSidebar()}
            />
            <h1 className="text-sm 2xl:text-xl font-semibold col-span-12 text-white">
              {headerName}
            </h1>
          </div>
          <div className="flex gap-4 2xl:gap-8 items-center">
            {/* <div className="relative w-8 h-8 flex justify-center items-center">
              <BsBellFill className="text-white 2xl:w-6 2xl:h-6" />
              <span className="absolute top-0 right-0 animate-pulse text-yellow-500">
                <RxDotFilled />
              </span>
            </div> */}
            <DropdownMenu>
              <DropdownMenuTrigger className="outline-none">
                <Avatar className="border-white border-2 w-8 h-8 2xl:w-10 2xl:h-10">
                  <AvatarImage
                    src={
                      profileDataAdmin?.profilePic ??
                      'https://api.dicebear.com/8.x/initials/svg?seed=' +
                        profileDataAdmin?.fullName
                    }
                    alt="profile-img"
                  />
                  <AvatarFallback>
                    <img
                      src={
                        profileDataAdmin?.profilePic ??
                        'https://api.dicebear.com/8.x/initials/svg?seed=' +
                          profileDataAdmin?.fullName
                      }
                      alt="profile-image-loader"
                    />
                  </AvatarFallback>
                </Avatar>
              </DropdownMenuTrigger>
              <DropdownMenuContent>
                {/* <Link to="/admin/profile"> */}
                <DropdownMenuItem
                  onClick={() =>
                    navigate('/admin/profile', { state: { profileDataAdmin } })
                  }>
                  <DropdownMenuLabel className="flex items-center justify-start gap-3 text-xs 2xl:text-sm">
                    <Avatar className="w-8 h-8 2xl:w-10 2xl:h-10">
                      <AvatarImage
                        src={
                          profileDataAdmin?.profilePic ??
                          'https://api.dicebear.com/8.x/initials/svg?seed=' +
                            profileDataAdmin?.fullName
                        }
                        alt="profile-img"
                      />
                      <AvatarFallback>
                        <img
                          src={
                            profileDataAdmin?.profilePic ??
                            'https://api.dicebear.com/8.x/initials/svg?seed=' +
                              profileDataAdmin?.fullName
                          }
                          alt="profile-image-loader"
                        />
                      </AvatarFallback>
                    </Avatar>
                    My Account
                  </DropdownMenuLabel>
                </DropdownMenuItem>
                {/* </Link> */}
                <DropdownMenuItem onClick={handleAdminLogout}>
                  <DropdownMenuLabel className="text-xs 2xl:text-sm">
                    Logout
                  </DropdownMenuLabel>
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </nav>
      </div>
    </>
  );
};

export default TopHeader;
