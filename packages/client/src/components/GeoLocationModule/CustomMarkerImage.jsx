import { useEffect, useState } from 'react';
import { MarkerF } from '@react-google-maps/api';
import EmpLogoMarker from '../../assets/images/emp-logo.webp';

const CustomMarkerImage = ({ livePosition, employeeProfile }) => {
  // const corsProxy = 'https://cors-anywhere.herokuapp.com/';
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
        <clipPath id="markerClip">
          <circle cx="238" cy="190" r="140" />
        </clipPath>
      </defs>
      <path
        d="M 239.998 0 C 345.59 0 430 83.301 430 187.498 C 430 223.528 419.117 251.36 400.207 279.589 L 259.982 489.549 C 255.726 495.902 248.41 500 239.998 500 C 231.59 500 224.161 495.802 220.007 489.549 L 79.781 279.589 C 60.883 251.36 50 223.528 50 187.498 C 50 83.301 134.41 0 239.998 0 Z M 239.998 291.601 C 298.286 291.601 345.59 244.92 345.59 187.3 C 345.59 129.691 298.286 83.011 239.998 83.011 C 181.714 83.011 134.41 129.691 134.41 187.3 C 134.41 244.92 181.714 291.601 239.998 291.601 Z M 239.998 124.999 C 275.03 124.999 303.332 152.93 303.332 187.498 C 303.332 222.072 275.03 249.998 239.998 249.998 C 204.969 249.998 176.668 222.072 176.668 187.498 C 176.668 152.93 204.969 124.999 239.998 124.999 Z"
        fill="#1D4381" />
      <image
        x="98"
        y="50"
        width="280"
        height="280"
        preserveAspectRatio="xMidYMid slice"
        href="${base64Image}"
        clip-path="url(#markerClip)" />
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
    <MarkerF
      position={livePosition}
      icon={{
        url: customIconUrl,
        scaledSize: { width: 80, height: 80 },
      }}
    />
  );
};

export default CustomMarkerImage;
