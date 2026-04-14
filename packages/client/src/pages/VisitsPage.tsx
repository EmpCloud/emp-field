import { useEffect, useState } from "react";
import { ClipboardList, RefreshCw, MapPin } from "lucide-react";
import { apiGet } from "@/api/client";

type VisitLog = {
  id: string;
  user_id: number;
  client_site_id: string | null;
  checkin_id: string | null;
  purpose: string | null;
  outcome: string | null;
  notes: string | null;
  duration_minutes: number | null;
  created_at: string;
};

export default function VisitsPage() {
  const [rows, setRows] = useState<VisitLog[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    setError(null);
    try {
      const res = await apiGet<{ data: VisitLog[] }>("/visits");
      const list =
        (res?.data as any)?.data ?? (Array.isArray(res?.data) ? (res.data as any) : []);
      setRows(list as VisitLog[]);
    } catch (e: any) {
      setError(e?.message || "Failed to load visits");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Visit Logs</h1>
          <p className="text-sm text-gray-500 mt-1">All field visits with purpose and outcome</p>
        </div>
        <button
          onClick={load}
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

      <div className="rounded-xl border border-gray-200 bg-white overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">
                Agent
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">
                Purpose
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">
                Outcome
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">
                Duration
              </th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">
                Logged
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {rows.length === 0 && !loading && (
              <tr>
                <td colSpan={5} className="px-4 py-8 text-center text-sm text-gray-500">
                  <ClipboardList className="w-8 h-8 mx-auto mb-2 text-gray-300" />
                  No visits logged yet
                </td>
              </tr>
            )}
            {rows.map((v) => (
              <tr key={v.id} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-sm">Agent #{v.user_id}</td>
                <td className="px-4 py-3 text-sm">{v.purpose || "—"}</td>
                <td className="px-4 py-3 text-sm">{v.outcome || "—"}</td>
                <td className="px-4 py-3 text-sm font-mono">
                  {v.duration_minutes != null ? `${v.duration_minutes} min` : "—"}
                </td>
                <td className="px-4 py-3 text-sm text-gray-500">
                  <MapPin className="inline w-3 h-3 mr-1" />
                  {new Date(v.created_at).toLocaleString()}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
