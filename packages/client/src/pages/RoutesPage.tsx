import { useEffect, useState } from "react";
import { Route as RouteIcon, RefreshCw, MapPin, CheckCircle2, Circle } from "lucide-react";
import { apiGet } from "@/api/client";

type DailyRoute = {
  id: string;
  user_id: number;
  date: string;
  status: string;
  total_distance_km: number;
  created_at: string;
};

type RouteStop = {
  id: string;
  daily_route_id: string;
  client_site_id: string | null;
  sequence_order: number;
  status: "pending" | "visited" | "skipped";
  planned_arrival: string | null;
  actual_arrival: string | null;
  notes: string | null;
};

export default function RoutesPage() {
  const [routes, setRoutes] = useState<DailyRoute[]>([]);
  const [selected, setSelected] = useState<DailyRoute | null>(null);
  const [stops, setStops] = useState<RouteStop[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function loadRoutes() {
    setLoading(true);
    setError(null);
    try {
      const res = await apiGet<{ data: DailyRoute[] }>("/routes");
      const list =
        (res?.data as any)?.data ?? (Array.isArray(res?.data) ? (res.data as any) : []);
      setRoutes(list as DailyRoute[]);
    } catch (e: any) {
      setError(e?.message || "Failed to load routes");
    } finally {
      setLoading(false);
    }
  }

  async function loadStops(route: DailyRoute) {
    setSelected(route);
    try {
      const res = await apiGet<{ stops: RouteStop[] }>(`/routes/${route.id}`);
      const payload = res?.data as any;
      setStops((payload?.stops || payload?.data?.stops || []) as RouteStop[]);
    } catch {
      setStops([]);
    }
  }

  useEffect(() => {
    loadRoutes();
  }, []);

  const statusColor: Record<string, string> = {
    pending: "bg-gray-100 text-gray-700",
    in_progress: "bg-blue-100 text-blue-700",
    completed: "bg-green-100 text-green-700",
    cancelled: "bg-red-100 text-red-700",
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Daily Routes</h1>
          <p className="text-sm text-gray-500 mt-1">Planned field routes with stops</p>
        </div>
        <button
          onClick={loadRoutes}
          className="flex items-center gap-2 px-3 py-2 text-sm rounded-lg border border-gray-200 bg-white hover:bg-gray-50"
        >
          <RefreshCw className={`w-4 h-4 ${loading ? "animate-spin" : ""}`} /> Refresh
        </button>
      </div>

      {error && (
        <div className="rounded-lg bg-red-50 border border-red-200 p-4 text-sm text-red-700">
          {error}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Routes list */}
        <section className="lg:col-span-2 rounded-xl border border-gray-200 bg-white">
          <header className="flex items-center gap-2 px-5 py-4 border-b border-gray-200">
            <RouteIcon className="w-4 h-4 text-brand-600" />
            <h2 className="text-sm font-semibold text-gray-900">Routes</h2>
            <span className="ml-auto text-xs text-gray-500">{routes.length}</span>
          </header>
          {routes.length === 0 ? (
            <p className="p-6 text-sm text-gray-500 text-center">No routes planned yet</p>
          ) : (
            <ul className="divide-y divide-gray-100">
              {routes.map((r) => (
                <li
                  key={r.id}
                  onClick={() => loadStops(r)}
                  className={`flex items-center justify-between px-5 py-3 cursor-pointer ${
                    selected?.id === r.id ? "bg-brand-50" : "hover:bg-gray-50"
                  }`}
                >
                  <div>
                    <p className="text-sm font-medium text-gray-900">Agent #{r.user_id}</p>
                    <p className="text-xs text-gray-500">
                      {r.date} • {Number(r.total_distance_km || 0).toFixed(1)} km
                    </p>
                  </div>
                  <span
                    className={`text-xs font-medium px-2 py-0.5 rounded-full ${
                      statusColor[r.status] || "bg-gray-100 text-gray-700"
                    }`}
                  >
                    {r.status}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </section>

        {/* Selected route stops */}
        <section className="rounded-xl border border-gray-200 bg-white">
          <header className="flex items-center gap-2 px-5 py-4 border-b border-gray-200">
            <MapPin className="w-4 h-4 text-amber-600" />
            <h2 className="text-sm font-semibold text-gray-900">Stops</h2>
          </header>
          {!selected ? (
            <p className="p-6 text-sm text-gray-500 text-center">Select a route to see stops</p>
          ) : stops.length === 0 ? (
            <p className="p-6 text-sm text-gray-500 text-center">No stops on this route</p>
          ) : (
            <ol className="divide-y divide-gray-100">
              {stops
                .sort((a, b) => a.sequence_order - b.sequence_order)
                .map((s) => (
                  <li key={s.id} className="px-5 py-3 flex items-start gap-3">
                    {s.status === "visited" ? (
                      <CheckCircle2 className="w-4 h-4 text-green-600 mt-0.5" />
                    ) : s.status === "skipped" ? (
                      <Circle className="w-4 h-4 text-red-400 mt-0.5" />
                    ) : (
                      <Circle className="w-4 h-4 text-gray-300 mt-0.5" />
                    )}
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium text-gray-900">
                        Stop #{s.sequence_order}
                      </p>
                      <p className="text-xs text-gray-500">
                        {s.planned_arrival
                          ? new Date(s.planned_arrival).toLocaleTimeString()
                          : "—"}
                        {" → "}
                        {s.actual_arrival
                          ? new Date(s.actual_arrival).toLocaleTimeString()
                          : "pending"}
                      </p>
                      {s.notes && <p className="text-xs text-gray-400 mt-1">{s.notes}</p>}
                    </div>
                  </li>
                ))}
            </ol>
          )}
        </section>
      </div>
    </div>
  );
}
