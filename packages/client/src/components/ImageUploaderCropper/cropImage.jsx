// cropImage.js
export const getCroppedImg = (imageSrc, pixelCrop) => {
  const image = new Image();
  image.src = imageSrc;

  const canvas = document.createElement('canvas');
  const ctx = canvas.getContext('2d');

  canvas.width = pixelCrop.width;
  canvas.height = pixelCrop.height;

  return new Promise((resolve, reject) => {
    image.onload = () => {
      ctx.drawImage(
        image,
        pixelCrop.x,
        pixelCrop.y,
        pixelCrop.width,
        pixelCrop.height,
        0,
        0,
        pixelCrop.width,
        pixelCrop.height
      );

      canvas.toBlob(blob => {
        if (blob) {
          resolve(URL.createObjectURL(blob));
        } else {
          reject(new Error('Canvas is empty'));
        }
      }, 'image/jpeg');
    };
    image.onerror = () => {
      reject(new Error('Failed to load image'));
    };
  });
};
