export function DecodeJWTToken(token) {
  if (!token) return null;
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join('')
    );
    const decodeJwt = JSON.parse(jsonPayload);
    return decodeJwt?.userData?.orgId;
  } catch (error) {
    console.error('Invalid token:', error);
    return null;
  }
}
