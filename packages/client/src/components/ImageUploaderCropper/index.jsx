import React, { useState, useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import Cropper from 'react-easy-crop';
import { getCroppedImg } from './cropImage'; // Import the helper function to crop images
import { adminImgUpload } from 'page/user/Profile/Api/post'; // Import the API function for image upload

const ImageUploaderCropper = ({ setFieldValue }) => {
  const [imageSrc, setImageSrc] = useState(null);
  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const [croppedAreaPixels, setCroppedAreaPixels] = useState(null);
  const [error, setError] = useState(null);

  const onDrop = useCallback(acceptedFiles => {
    const file = acceptedFiles[0];

    if (!file.type.startsWith('image/')) {
      setError('Only image files are allowed.');
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      setImageSrc(reader.result);
      setError(null);
    };
    reader.readAsDataURL(file);
  }, []);

  const { getRootProps, getInputProps } = useDropzone({
    onDrop,
    maxSize: 1048576,
  }); // Max size 1MB

  const onCropComplete = useCallback((croppedArea, croppedAreaPixels) => {
    setCroppedAreaPixels(croppedAreaPixels);
  }, []);

  const showCroppedImage = useCallback(async () => {
    try {
      const croppedImage = await getCroppedImg(imageSrc, croppedAreaPixels);
      const uploadedUrl = await adminImgUpload(croppedImage);
      setFieldValue('profilePic', uploadedUrl);
    } catch (e) {
      console.error(e);
    }
  }, [croppedAreaPixels, imageSrc, setFieldValue]);

  return (
    <div className="container mx-auto p-4">
      <div
        {...getRootProps()}
        className="border-dashed border-2 border-gray-400 rounded p-6 text-center cursor-pointer">
        <input {...getInputProps()} />
        <p>Drag 'n' drop some files here, or click to select files</p>
        {error && <p className="text-red-500">{error}</p>}
      </div>
      {imageSrc && (
        <div className="relative w-full h-64 mt-4">
          <Cropper
            image={imageSrc}
            crop={crop}
            zoom={zoom}
            aspect={4 / 3}
            onCropChange={setCrop}
            onZoomChange={setZoom}
            onCropComplete={onCropComplete}
          />
        </div>
      )}
      <button
        onClick={showCroppedImage}
        className="mt-4 bg-blue-500 text-white py-2 px-4 rounded">
        Upload Cropped Image
      </button>
    </div>
  );
};

export default ImageUploaderCropper;
