import { useEffect, useMemo, useState } from "react";
import { MapPin, Crosshair, LogIn, LogOut, Loader2, WifiOff, Wifi } from "lucide-react";
import toast from "react-hot-toast";
import { apiPost } from "@/api/client";
import { useGeolocation, type GeoPosition } from "@/lib/useGeolocation";
import { getSocket } from "@/lib/socket";
import { getUser } from "@/lib/auth-store";

type CheckInState = "out" | "in";

export default function FieldCheckInPage() {
  const user = getUser();
  const [state, setState] = useState<CheckInState>("out");
  const [submitting, setSubmitting] = useState(false);
  const [socketConnected, setSocketConnected] = useState(false);

  const { position, error, watching } = useGeolocation({
    enabled: true,
    highAccuracy: true,
    onPing: (p) => streamPing(p),
  });

  const socket = useMemo(() => getSocket(), []);

  useEffect(() => {
    const onConnect = () => setSocketConnected(true);
    const onDisconnect = () => setSocketConnected(false);
    socket.on("connect", onConnect);
    socket.on("disconnect", onDisconnect);
    if (socket.connected) setSocketConnected(true);
    return () => {
      socket.off("connect", onConnect);
      socket.off("disconnect", onDisconnect);
    };
  }, [socket]);

  function streamPing(p: GeoPosition) {
    if (state !== "in") return;
    if (!socket.connected) return;
    socket.emit("location:ping", p);
  }

  async function handleCheckIn() {
    if (!position) {
      toast.error("Waiting for GPS lock…");
      return;
    }
    setSubmitting(true);
    try {
      await apiPost("/checkins", {
        latitude: position.latitude,
        longitude: position.longitude,
        accuracy: position.accuracy,
      });
      setState("in");
      toast.success("Checked in");
    } catch (e: any) {
      toast.error(e?.message || "Check-in failed");
    } finally {
      setSubmitting(false);
    }
  }

  async function handleCheckOut() {
    if (!position) {
      toast.error("Waiting for GPS lock…");
      return;
    }
    setSubmitting(true);
    try {
      await apiPost("/checkins/checkout", {
        latitude: position.latitude,
        longitude: position.longitude,
        accuracy: position.accuracy,
      });
      setState("out");
      toast.success("Checked out");
    } catch (e: any) {
      toast.error(e?.message || "Check-out failed");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* Header — mobile first */}
      <header className="bg-white border-b border-gray-200 px-4 py-3 flex items-center justify-between">
        <div>
          <p className="text-xs text-gray-500">Hello,</p>
          <p className="text-base font-semibold text-gray-900">
            {user?.firstName || "Field agent"}
          </p>
        </div>
        <div className="flex items-center gap-1 text-xs">
          {socketConnected ? (
            <>
              <Wifi className="w-4 h-4 text-green-600" />
              <span className="text-green-700">Live</span>
            </>
          ) : (
            <>
              <WifiOff className="w-4 h-4 text-gray-400" />
              <span className="text-gray-500">Offline</span>
            </>
          )}
        </div>
      </header>

      {/* GPS status */}
      <section className="p-4">
        <div className="rounded-2xl bg-white border border-gray-200 p-4 shadow-sm">
          <div className="flex items-center gap-2 mb-3">
            <Crosshair
              className={`w-5 h-5 ${watching ? "text-brand-600 animate-pulse" : "text-gray-400"}`}
            />
            <h2 className="text-sm font-semibold text-gray-900">GPS position</h2>
          </div>

          {error && <p className="text-sm text-red-600">{error}</p>}

          {!error && !position && (
            <div className="flex items-center gap-2 text-sm text-gray-500">
              <Loader2 className="w-4 h-4 animate-spin" />
              Acquiring location…
            </div>
          )}

          {position && (
            <div className="grid grid-cols-2 gap-3 text-sm">
              <div>
                <p className="text-gray-500 text-xs">Latitude</p>
                <p className="font-mono text-gray-900">{position.latitude.toFixed(6)}</p>
              </div>
              <div>
                <p className="text-gray-500 text-xs">Longitude</p>
                <p className="font-mono text-gray-900">{position.longitude.toFixed(6)}</p>
              </div>
              <div>
                <p className="text-gray-500 text-xs">Accuracy</p>
                <p className="font-mono text-gray-900">{Math.round(position.accuracy)} m</p>
              </div>
              <div>
                <p className="text-gray-500 text-xs">Updated</p>
                <p className="font-mono text-gray-900">
                  {new Date(position.timestamp).toLocaleTimeString()}
                </p>
              </div>
            </div>
          )}
        </div>
      </section>

      {/* Current state badge */}
      <section className="px-4">
        <div
          className={`rounded-2xl p-4 text-center font-semibold ${
            state === "in"
              ? "bg-green-50 text-green-800 border border-green-200"
              : "bg-gray-50 text-gray-600 border border-gray-200"
          }`}
        >
          <MapPin className="inline w-5 h-5 mr-1" />
          {state === "in" ? "On field — location streaming" : "Not checked in"}
        </div>
      </section>

      {/* Action buttons — thumb friendly */}
      <section className="p-4 mt-auto">
        {state === "out" ? (
          <button
            onClick={handleCheckIn}
            disabled={submitting || !position}
            className="w-full flex items-center justify-center gap-2 bg-brand-600 text-white font-semibold text-lg py-4 rounded-2xl active:scale-[0.98] transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {submitting ? (
              <Loader2 className="w-6 h-6 animate-spin" />
            ) : (
              <>
                <LogIn className="w-6 h-6" /> Check in
              </>
            )}
          </button>
        ) : (
          <button
            onClick={handleCheckOut}
            disabled={submitting || !position}
            className="w-full flex items-center justify-center gap-2 bg-red-600 text-white font-semibold text-lg py-4 rounded-2xl active:scale-[0.98] transition disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {submitting ? (
              <Loader2 className="w-6 h-6 animate-spin" />
            ) : (
              <>
                <LogOut className="w-6 h-6" /> Check out
              </>
            )}
          </button>
        )}
        <p className="text-center text-xs text-gray-500 mt-3">
          Your location is only shared while checked in.
        </p>
      </section>
    </div>
  );
}
