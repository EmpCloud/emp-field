import { useEffect, useMemo, useState } from "react";
import { MapPin, Radio, Users, Clock } from "lucide-react";
import { getSocket } from "@/lib/socket";

type AgentFix = {
  userId: number;
  orgId: number;
  latitude: number;
  longitude: number;
  accuracy?: number;
  timestamp: string;
};

type GeofenceActivity = {
  userId: number;
  geoFenceId: string;
  event: "enter" | "exit";
  latitude: number;
  longitude: number;
  occurredAt?: string;
};

/**
 * Manager-facing live tracking board.
 * Subscribes to the /field socket namespace and listens for:
 *   - location:update  (agent GPS pings)
 *   - geofence:activity (enter/exit)
 *
 * This is intentionally map-less (no Leaflet/Mapbox) to keep the dep graph
 * small. Coordinates can be opened in the user's map app via the MapPin link.
 */
export default function LiveTrackingPage() {
  const [agents, setAgents] = useState<Map<number, AgentFix>>(new Map());
  const [events, setEvents] = useState<GeofenceActivity[]>([]);
  const [connected, setConnected] = useState(false);
  const socket = useMemo(() => getSocket(), []);

  useEffect(() => {
    function onConnect() {
      setConnected(true);
    }
    function onDisconnect() {
      setConnected(false);
    }
    function onLocation(fix: AgentFix) {
      setAgents((prev) => {
        const next = new Map(prev);
        next.set(fix.userId, fix);
        return next;
      });
    }
    function onGeofence(ev: GeofenceActivity) {
      setEvents((prev) => [ev, ...prev].slice(0, 50));
    }

    socket.on("connect", onConnect);
    socket.on("disconnect", onDisconnect);
    socket.on("location:update", onLocation);
    socket.on("geofence:activity", onGeofence);
    if (socket.connected) setConnected(true);

    return () => {
      socket.off("connect", onConnect);
      socket.off("disconnect", onDisconnect);
      socket.off("location:update", onLocation);
      socket.off("geofence:activity", onGeofence);
    };
  }, [socket]);

  const agentList = useMemo(
    () =>
      Array.from(agents.values()).sort(
        (a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime(),
      ),
    [agents],
  );

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Live Tracking</h1>
          <p className="text-sm text-gray-500 mt-1">Real-time field agent positions</p>
        </div>
        <div
          className={`flex items-center gap-2 rounded-full px-3 py-1 text-xs font-medium ${
            connected
              ? "bg-green-100 text-green-700"
              : "bg-gray-100 text-gray-500"
          }`}
        >
          <Radio className={`w-3 h-3 ${connected ? "animate-pulse" : ""}`} />
          {connected ? "Live" : "Disconnected"}
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Active agents */}
        <section className="lg:col-span-2 rounded-xl border border-gray-200 bg-white">
          <header className="flex items-center gap-2 px-5 py-4 border-b border-gray-200">
            <Users className="w-4 h-4 text-brand-600" />
            <h2 className="text-sm font-semibold text-gray-900">Active agents</h2>
            <span className="ml-auto text-xs text-gray-500">{agentList.length}</span>
          </header>
          {agentList.length === 0 ? (
            <p className="p-6 text-sm text-gray-500 text-center">
              No agents streaming yet.
            </p>
          ) : (
            <ul className="divide-y divide-gray-100">
              {agentList.map((a) => (
                <li key={a.userId} className="flex items-center gap-4 px-5 py-3">
                  <div className="w-9 h-9 rounded-full bg-brand-50 text-brand-700 flex items-center justify-center text-sm font-semibold">
                    {a.userId}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-gray-900">
                      Agent #{a.userId}
                    </p>
                    <p className="text-xs text-gray-500 font-mono">
                      {a.latitude.toFixed(5)}, {a.longitude.toFixed(5)}
                      {a.accuracy ? ` (±${Math.round(a.accuracy)}m)` : ""}
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="text-xs text-gray-500 flex items-center gap-1">
                      <Clock className="w-3 h-3" />
                      {new Date(a.timestamp).toLocaleTimeString()}
                    </p>
                    <a
                      href={`https://www.google.com/maps?q=${a.latitude},${a.longitude}`}
                      target="_blank"
                      rel="noreferrer"
                      className="text-xs text-brand-600 inline-flex items-center gap-1 mt-1"
                    >
                      <MapPin className="w-3 h-3" /> Open
                    </a>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </section>

        {/* Geofence events */}
        <section className="rounded-xl border border-gray-200 bg-white">
          <header className="flex items-center gap-2 px-5 py-4 border-b border-gray-200">
            <MapPin className="w-4 h-4 text-amber-600" />
            <h2 className="text-sm font-semibold text-gray-900">Geofence activity</h2>
          </header>
          {events.length === 0 ? (
            <p className="p-6 text-sm text-gray-500 text-center">
              No recent events.
            </p>
          ) : (
            <ul className="divide-y divide-gray-100 max-h-96 overflow-auto">
              {events.map((e, idx) => (
                <li key={`${e.userId}-${idx}`} className="px-5 py-3">
                  <div className="flex items-center justify-between">
                    <p className="text-sm font-medium text-gray-900">
                      Agent #{e.userId}
                    </p>
                    <span
                      className={`text-xs font-semibold uppercase ${
                        e.event === "enter" ? "text-green-600" : "text-red-600"
                      }`}
                    >
                      {e.event}
                    </span>
                  </div>
                  <p className="text-xs text-gray-500 mt-1">
                    Fence {e.geoFenceId.slice(0, 8)} •{" "}
                    {e.occurredAt ? new Date(e.occurredAt).toLocaleTimeString() : "now"}
                  </p>
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </div>
  );
}
