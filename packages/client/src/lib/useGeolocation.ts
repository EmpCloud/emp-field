import { useEffect, useRef, useState } from "react";

export type GeoPosition = {
  latitude: number;
  longitude: number;
  accuracy: number;
  timestamp: string;
};

export type GeoState = {
  position: GeoPosition | null;
  error: string | null;
  watching: boolean;
};

/**
 * Watches the browser Geolocation API for position updates.
 * Emits through `onPing` on each update so callers can stream to sockets.
 */
export function useGeolocation(opts?: {
  enabled?: boolean;
  highAccuracy?: boolean;
  onPing?: (pos: GeoPosition) => void;
}): GeoState {
  const { enabled = true, highAccuracy = true, onPing } = opts ?? {};
  const [position, setPosition] = useState<GeoPosition | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [watching, setWatching] = useState(false);
  const pingRef = useRef(onPing);
  pingRef.current = onPing;

  useEffect(() => {
    if (!enabled) return;
    if (!("geolocation" in navigator)) {
      setError("Geolocation not supported on this device");
      return;
    }

    setWatching(true);
    const id = navigator.geolocation.watchPosition(
      (p) => {
        const next: GeoPosition = {
          latitude: p.coords.latitude,
          longitude: p.coords.longitude,
          accuracy: p.coords.accuracy,
          timestamp: new Date(p.timestamp).toISOString(),
        };
        setPosition(next);
        setError(null);
        pingRef.current?.(next);
      },
      (err) => {
        setError(err.message);
      },
      {
        enableHighAccuracy: highAccuracy,
        maximumAge: 10_000,
        timeout: 20_000,
      },
    );

    return () => {
      navigator.geolocation.clearWatch(id);
      setWatching(false);
    };
  }, [enabled, highAccuracy]);

  return { position, error, watching };
}
