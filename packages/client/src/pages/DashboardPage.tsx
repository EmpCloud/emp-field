import { MapPin, Building2, Users, ClipboardList } from "lucide-react";
import { getUser } from "@/lib/auth-store";

const stats = [
  { label: "Active Agents", value: "0", icon: Users, color: "bg-blue-500" },
  { label: "Client Sites", value: "0", icon: Building2, color: "bg-green-500" },
  { label: "Today's Visits", value: "0", icon: ClipboardList, color: "bg-amber-500" },
  { label: "Tracked Routes", value: "0", icon: MapPin, color: "bg-purple-500" },
];

export default function DashboardPage() {
  const user = getUser();

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">
          Welcome back, {user?.firstName || "User"}
        </h1>
        <p className="text-sm text-gray-500 mt-1">Field Force Management Dashboard</p>
      </div>

      {/* Stat cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {stats.map((stat) => (
          <div
            key={stat.label}
            className="flex items-center gap-4 rounded-xl border border-gray-200 bg-white p-5"
          >
            <div
              className={`flex h-12 w-12 items-center justify-center rounded-lg ${stat.color}`}
            >
              <stat.icon className="h-6 w-6 text-white" />
            </div>
            <div>
              <p className="text-2xl font-bold text-gray-900">{stat.value}</p>
              <p className="text-sm text-gray-500">{stat.label}</p>
            </div>
          </div>
        ))}
      </div>

      {/* Map placeholder */}
      <div className="rounded-xl border border-gray-200 bg-white p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Live Map</h2>
        <div className="flex h-64 items-center justify-center rounded-lg bg-gray-100 border-2 border-dashed border-gray-300">
          <div className="text-center text-gray-400">
            <MapPin className="h-10 w-10 mx-auto mb-2" />
            <p className="text-sm font-medium">Map view will appear here</p>
            <p className="text-xs mt-1">Real-time agent locations and routes</p>
          </div>
        </div>
      </div>
    </div>
  );
}
