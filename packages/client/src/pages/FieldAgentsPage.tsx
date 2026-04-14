import { useEffect, useState } from "react";
import { Users, RefreshCw, Circle } from "lucide-react";
import { apiGet } from "@/api/client";

type Agent = {
  id: string | number;
  user_id?: number;
  full_name: string;
  email: string;
  emp_id: string | null;
  department: string | null;
  status: number;
  phone_number: string | null;
};

export default function FieldAgentsPage() {
  const [rows, setRows] = useState<Agent[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    setError(null);
    try {
      const res = await apiGet<{ data: Agent[] }>("/user/fetch-emp-users");
      const list =
        (res?.data as any)?.data ?? (Array.isArray(res?.data) ? (res.data as any) : []);
      setRows(list as Agent[]);
    } catch (e: any) {
      setError(e?.message || "Failed to load agents");
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
          <h1 className="text-2xl font-bold text-gray-900">Field Agents</h1>
          <p className="text-sm text-gray-500 mt-1">All employees registered in the field module</p>
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
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Agent</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Email</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Emp ID</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Department</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Phone</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {rows.length === 0 && !loading && (
              <tr>
                <td colSpan={6} className="px-4 py-8 text-center text-sm text-gray-500">
                  <Users className="w-8 h-8 mx-auto mb-2 text-gray-300" />
                  No field agents registered yet
                </td>
              </tr>
            )}
            {rows.map((u) => (
              <tr key={String(u.id)} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-sm font-medium">{u.full_name}</td>
                <td className="px-4 py-3 text-sm text-gray-600">{u.email}</td>
                <td className="px-4 py-3 text-sm font-mono text-gray-600">{u.emp_id || "—"}</td>
                <td className="px-4 py-3 text-sm text-gray-600">{u.department || "—"}</td>
                <td className="px-4 py-3 text-sm font-mono text-gray-600">{u.phone_number || "—"}</td>
                <td className="px-4 py-3 text-sm">
                  {u.status === 1 ? (
                    <span className="inline-flex items-center gap-1 text-green-700">
                      <Circle className="w-2 h-2 fill-current" /> Active
                    </span>
                  ) : (
                    <span className="inline-flex items-center gap-1 text-gray-400">
                      <Circle className="w-2 h-2 fill-current" /> Inactive
                    </span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
