// ============================================================================
// GEO UTILITIES
// Haversine distance, point-in-circle, point-in-polygon
// ============================================================================

const EARTH_RADIUS_KM = 6371;

function toRad(deg: number): number {
  return (deg * Math.PI) / 180;
}

/**
 * Calculate the Haversine distance between two points in kilometers.
 */
export function haversineDistance(
  lat1: number,
  lng1: number,
  lat2: number,
  lng2: number
): number {
  const dLat = toRad(lat2 - lat1);
  const dLng = toRad(lng2 - lng1);
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
    Math.sin(dLng / 2) * Math.sin(dLng / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return EARTH_RADIUS_KM * c;
}

/**
 * Calculate the Haversine distance in meters.
 */
export function haversineDistanceMeters(
  lat1: number,
  lng1: number,
  lat2: number,
  lng2: number
): number {
  return haversineDistance(lat1, lng1, lat2, lng2) * 1000;
}

/**
 * Check if a point is within a circle defined by center and radius (meters).
 */
export function isPointInCircle(
  pointLat: number,
  pointLng: number,
  centerLat: number,
  centerLng: number,
  radiusMeters: number
): boolean {
  const distance = haversineDistanceMeters(pointLat, pointLng, centerLat, centerLng);
  return distance <= radiusMeters;
}

/**
 * Check if a point is inside a polygon using ray casting algorithm.
 * Polygon is an array of {lat, lng} vertices.
 */
export function isPointInPolygon(
  pointLat: number,
  pointLng: number,
  polygon: Array<{ lat: number; lng: number }>
): boolean {
  let inside = false;
  const n = polygon.length;

  for (let i = 0, j = n - 1; i < n; j = i++) {
    const yi = polygon[i].lat;
    const xi = polygon[i].lng;
    const yj = polygon[j].lat;
    const xj = polygon[j].lng;

    const intersect =
      yi > pointLat !== yj > pointLat &&
      pointLng < ((xj - xi) * (pointLat - yi)) / (yj - yi) + xi;

    if (intersect) inside = !inside;
  }

  return inside;
}
