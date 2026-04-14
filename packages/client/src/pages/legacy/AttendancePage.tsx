import { useEffect, useState } from "react";
import { Calendar, RefreshCw } from "lucide-react";
import { legacyApi } from "@/lib/legacyApi";

type AttendanceRow = {
  id: string;
  user_id: number;
  attendance_date: string;
  check_in: string | null;
  check_out: string | null;
  status: string;
};

export default function AttendancePage() {
  const [rows, setRows] = useState<AttendanceRow[]>([]);
  const [loading, setLoading] = useState(false);

  async function load() {
    setLoading(true);
    try {
      const res = await legacyApi.attendance.fetch({});
      setRows((res?.data as AttendanceRow[]) || []);
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
          <h1 className="text-2xl font-bold text-gray-900">Attendance</h1>
          <p className="text-sm text-gray-500 mt-1">Daily attendance records</p>
        </div>
        <button
          onClick={load}
          className="flex items-center gap-2 px-3 py-2 text-sm rounded-lg border border-gray-200 hover:bg-gray-50"
        >
          <RefreshCw className={`w-4 h-4 ${loading ? "animate-spin" : ""}`} /> Refresh
        </button>
      </div>

      <div className="rounded-xl border border-gray-200 bg-white overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">User</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Date</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Check In</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Check Out</th>
              <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {rows.length === 0 ? (
              <tr>
                <td colSpan={5} className="px-4 py-8 text-center text-sm text-gray-500">
                  <Calendar className="w-8 h-8 mx-auto mb-2 text-gray-300" />
                  No attendance records yet
                </td>
              </tr>
            ) : (
              rows.map((r) => (
                <tr key={r.id} className="hover:bg-gray-50">
                  <td className="px-4 py-3 text-sm">{r.user_id}</td>
                  <td className="px-4 py-3 text-sm">{r.attendance_date}</td>
                  <td className="px-4 py-3 text-sm font-mono">
                    {r.check_in ? new Date(r.check_in).toLocaleTimeString() : "—"}
                  </td>
                  <td className="px-4 py-3 text-sm font-mono">
                    {r.check_out ? new Date(r.check_out).toLocaleTimeString() : "—"}
                  </td>
                  <td className="px-4 py-3 text-sm">
                    <span className="inline-flex px-2 py-0.5 rounded-full bg-green-100 text-green-700 text-xs font-medium">
                      {r.status}
                    </span>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
