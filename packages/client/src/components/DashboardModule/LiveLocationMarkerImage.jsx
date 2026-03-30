import { useEffect, useState } from 'react';
import { MarkerF, OverlayView } from '@react-google-maps/api';
import EmpLogoMarker from '../../assets/images/emp-logo.webp';
import { useNavigate } from 'react-router-dom';

const LiveLocationMarkerImage = ({
  livePosition,
  employeeProfile,
  employeeName,
  employeeDetails,
}) => {
  const navigate = useNavigate();
  const [customIconUrl, setCustomIconUrl] = useState('');

  useEffect(() => {
    const img = new Image();
    img.crossOrigin = 'Anonymous';
    img.onload = () => {
      const canvas = document.createElement('canvas');
      canvas.width = img.width;
      canvas.height = img.height;
      const ctx = canvas.getContext('2d');
      ctx.drawImage(img, 0, 0);
      const base64Image = canvas.toDataURL('image/png');

      const svg = `
        <svg viewBox="0 0 500 500" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <linearGradient
            id="paint0_linear_3522_1101"
            x1="0"
            y1="16.25"
            x2="29"
            y2="16.25"
            gradientUnits="userSpaceOnUse"
            gradientTransform="matrix(1, 0, 0, 1, 155.199692, 395.446991)">
            <stop stop-color="#A45CEC"></stop>
            <stop offset="1" stop-color="#0285EC"></stop>
          </linearGradient>
          <linearGradient
            id="paint1_linear_3522_1101"
            x1="0"
            y1="16.25"
            x2="29"
            y2="16.25"
            gradientUnits="userSpaceOnUse"
            gradientTransform="matrix(1, 0, 0, 1, 155.199692, 395.446991)">
            <stop stop-color="#A45CEC"></stop>
            <stop offset="1" stop-color="#0285EC"></stop>
          </linearGradient>
          <clipPath id="markerClip">
            <circle cx="169.355" cy="409.945" r="13.455"></circle>
          </clipPath>
        </defs>
        <mask id="path-1-inside-1_3522_1101" fill="white">
          <path
            fill-rule="evenodd"
            clip-rule="evenodd"
            d="M 172.687 424.139 C 179.262 422.762 184.2 416.931 184.2 409.947 C 184.2 401.939 177.708 395.447 169.7 395.447 C 161.692 395.447 155.2 401.939 155.2 409.947 C 155.2 417.318 160.701 423.405 167.821 424.326 L 169.767 427.697 C 169.959 428.03 170.441 428.03 170.633 427.697 L 172.687 424.139 Z"></path>
        </mask>
        <g transform="matrix(14.492753982543947, 0, 0, 14.492753982543947, -2209.42041015625, -5716.623046875)">
          <path
            fill-rule="evenodd"
            clip-rule="evenodd"
            d="M 172.687 424.139 C 179.262 422.762 184.2 416.931 184.2 409.947 C 184.2 401.939 177.708 395.447 169.7 395.447 C 161.692 395.447 155.2 401.939 155.2 409.947 C 155.2 417.318 160.7 423.405 167.821 424.326 L 169.767 427.697 C 169.959 428.03 170.44 428.03 170.633 427.697 L 172.687 424.139 Z"
            fill="url(#paint0_linear_3522_1101)"></path>
          <path
            d="M 172.687 424.139 L 172.482 423.16 L 172.044 423.252 L 171.821 423.639 L 172.687 424.139 Z M 167.821 424.326 L 168.687 423.826 L 168.439 423.398 L 167.949 423.335 L 167.821 424.326 Z M 169.767 427.697 L 168.901 428.197 L 169.767 427.697 Z M 170.633 427.697 L 171.499 428.197 L 170.633 427.697 Z M 183.2 409.947 C 183.2 416.448 178.603 421.878 172.482 423.16 L 172.892 425.118 C 179.921 423.646 185.2 417.414 185.2 409.947 L 183.2 409.947 Z M 169.7 396.447 C 177.155 396.447 183.2 402.491 183.2 409.947 L 185.2 409.947 C 185.2 401.387 178.26 394.447 169.7 394.447 L 169.7 396.447 Z M 156.2 409.947 C 156.2 402.491 162.244 396.447 169.7 396.447 L 169.7 394.447 C 161.139 394.447 154.2 401.387 154.2 409.947 L 156.2 409.947 Z M 167.949 423.335 C 161.321 422.477 156.2 416.809 156.2 409.947 L 154.2 409.947 C 154.2 417.827 160.08 424.333 167.692 425.318 L 167.949 423.335 Z M 170.633 427.197 L 168.687 423.826 L 166.955 424.826 L 168.901 428.197 L 170.633 427.197 Z M 169.767 427.197 C 169.959 426.864 170.44 426.864 170.633 427.197 L 168.901 428.197 C 169.478 429.197 170.921 429.197 171.499 428.197 L 169.767 427.197 Z M 171.821 423.639 L 169.767 427.197 L 171.499 428.197 L 173.553 424.639 L 171.821 423.639 Z"
            fill="url(#paint1_linear_3522_1101)"
            mask="url(#path-1-inside-1_3522_1101)"></path>
          <image
            width="26.91"
            height="26.91"
            preserveAspectRatio="xMidYMid slice"
            href="${base64Image}"
            clip-path="url(#markerClip)"
            style="transform-origin: 152.536px 394.42px;"
            y="396.49"
            x="155.9"
            transform="matrix(1.025641, 0, 0, 1.000001, -0.086191, 0.026504)"></image>
        </g>
      </svg>
      `;
      setCustomIconUrl(
        `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`
      );
    };
    img.onerror = () => {
      console.error('Error loading image');
    };
    img.src = employeeProfile;
  }, [employeeProfile]);

  if (!customIconUrl) return null;

  return (
    <>
      <MarkerF
        position={livePosition}
        icon={{
          url: customIconUrl,
          scaledSize: { width: 50, height: 50 },
        }}
        onClick={() =>
          navigate('/admin/employee-report?empId=' + employeeDetails?.emp_id)
        }
      />
      <OverlayView
        position={livePosition}
        mapPaneName={OverlayView.OVERLAY_MOUSE_TARGET}>
        <div className="tooltip-map">{employeeName ?? ''}</div>
      </OverlayView>
    </>
  );
};

export default LiveLocationMarkerImage;
