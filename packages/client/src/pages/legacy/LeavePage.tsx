import { useEffect, useState } from "react";
import { Plus, Trash2, Briefcase } from "lucide-react";
import toast from "react-hot-toast";
import { legacyApi } from "@/lib/legacyApi";

type LeaveType = { id: string; name: string; duration: number; no_of_days: number };
type LeaveRequest = {
  id: string;
  user_id: number;
  start_date: string;
  end_date: string;
  days: number;
  reason: string | null;
  status: string;
};

export default function LeavePage() {
  const [tab, setTab] = useState<"types" | "requests">("requests");
  const [types, setTypes] = useState<LeaveType[]>([]);
  const [requests, setRequests] = useState<LeaveRequest[]>([]);
  const [name, setName] = useState("");
  const [days, setDays] = useState(1);

  async function load() {
    const [t, r] = await Promise.all([legacyApi.leave.listTypes(), legacyApi.leave.listLeaves({})]);
    setTypes((t?.data as LeaveType[]) || []);
    setRequests((r?.data as LeaveRequest[]) || []);
  }
  useEffect(() => {
    load();
  }, []);

  async function addType(e: React.FormEvent) {
    e.preventDefault();
    if (!name) return;
    await legacyApi.leave.createType({ name, duration: 1, no_of_days: days, carry_forward: 0 });
    setName("");
    setDays(1);
    toast.success("Leave type added");
    load();
  }

  async function removeType(id: string) {
    if (!confirm("Delete this leave type?")) return;
    await legacyApi.leave.deleteType(id);
    load();
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Leave Management</h1>
        <p className="text-sm text-gray-500 mt-1">Leave types and requests</p>
      </div>

      <div className="flex gap-2 border-b border-gray-200">
        {(["requests", "types"] as const).map((t) => (
          <button
            key={t}
            onClick={() => setTab(t)}
            className={`px-4 py-2 text-sm font-medium border-b-2 ${
              tab === t ? "border-brand-600 text-brand-600" : "border-transparent text-gray-500"
            }`}
          >
            {t === "requests" ? "Leave Requests" : "Leave Types"}
          </button>
        ))}
      </div>

      {tab === "types" ? (
        <>
          <form onSubmit={addType} className="flex gap-3 items-end rounded-xl bg-white p-4 border border-gray-200">
            <div className="flex-1">
              <label className="text-xs font-medium text-gray-600">Type Name</label>
              <input
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
                placeholder="e.g. Sick Leave"
              />
            </div>
            <div>
              <label className="text-xs font-medium text-gray-600">Days</label>
              <input
                type="number"
                value={days}
                onChange={(e) => setDays(Number(e.target.value))}
                className="mt-1 w-20 rounded-md border border-gray-300 px-3 py-2 text-sm"
              />
            </div>
            <button className="inline-flex items-center gap-1 bg-brand-600 text-white px-4 py-2 rounded-md text-sm font-medium">
              <Plus className="w-4 h-4" /> Add
            </button>
          </form>
          <ul className="rounded-xl border border-gray-200 bg-white divide-y divide-gray-100">
            {types.map((lt) => (
              <li key={lt.id} className="flex items-center justify-between px-5 py-3">
                <div>
                  <p className="text-sm font-medium text-gray-900">{lt.name}</p>
                  <p className="text-xs text-gray-500">{lt.no_of_days} days/year</p>
                </div>
                <button
                  onClick={() => removeType(lt.id)}
                  className="text-red-500 hover:bg-red-50 p-2 rounded-md"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </li>
            ))}
          </ul>
        </>
      ) : (
        <div className="rounded-xl border border-gray-200 bg-white overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">User</th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Range</th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Days</th>
                <th className="px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {requests.length === 0 && (
                <tr>
                  <td colSpan={4} className="px-4 py-8 text-center text-sm text-gray-500">
                    <Briefcase className="w-8 h-8 mx-auto mb-2 text-gray-300" />
                    No leave requests
                  </td>
                </tr>
              )}
              {requests.map((r) => (
                <tr key={r.id}>
                  <td className="px-4 py-3 text-sm">{r.user_id}</td>
                  <td className="px-4 py-3 text-sm">
                    {r.start_date} → {r.end_date}
                  </td>
                  <td className="px-4 py-3 text-sm">{r.days}</td>
                  <td className="px-4 py-3 text-sm capitalize">{r.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
