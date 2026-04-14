import { useEffect, useState } from "react";
import { Plus, Trash2, CalendarDays } from "lucide-react";
import toast from "react-hot-toast";
import { legacyApi } from "@/lib/legacyApi";

type Holiday = { id: string; name: string; date: string };

export default function HolidayPage() {
  const [items, setItems] = useState<Holiday[]>([]);
  const [name, setName] = useState("");
  const [date, setDate] = useState("");

  async function load() {
    const res = await legacyApi.holiday.list();
    setItems((res?.data as Holiday[]) || []);
  }
  useEffect(() => {
    load();
  }, []);

  async function add(e: React.FormEvent) {
    e.preventDefault();
    if (!name || !date) return;
    try {
      await legacyApi.holiday.create({ name, date });
      setName("");
      setDate("");
      toast.success("Holiday added");
      load();
    } catch (e: any) {
      toast.error(e?.message || "Failed");
    }
  }

  async function remove(id: string) {
    if (!confirm("Delete this holiday?")) return;
    await legacyApi.holiday.remove(id);
    load();
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Holidays</h1>
        <p className="text-sm text-gray-500 mt-1">Manage organisation-wide holiday calendar</p>
      </div>

      <form onSubmit={add} className="flex flex-wrap gap-3 items-end rounded-xl bg-white p-4 border border-gray-200">
        <div className="flex-1 min-w-[200px]">
          <label className="text-xs font-medium text-gray-600">Name</label>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
            placeholder="e.g. Republic Day"
          />
        </div>
        <div>
          <label className="text-xs font-medium text-gray-600">Date</label>
          <input
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            className="mt-1 rounded-md border border-gray-300 px-3 py-2 text-sm"
          />
        </div>
        <button
          type="submit"
          className="inline-flex items-center gap-1 bg-brand-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-brand-700"
        >
          <Plus className="w-4 h-4" /> Add
        </button>
      </form>

      <ul className="rounded-xl border border-gray-200 bg-white divide-y divide-gray-100">
        {items.length === 0 && (
          <li className="p-8 text-center text-sm text-gray-500">
            <CalendarDays className="w-8 h-8 mx-auto mb-2 text-gray-300" />
            No holidays added yet
          </li>
        )}
        {items.map((h) => (
          <li key={h.id} className="flex items-center justify-between px-5 py-3">
            <div>
              <p className="text-sm font-medium text-gray-900">{h.name}</p>
              <p className="text-xs text-gray-500">{new Date(h.date).toDateString()}</p>
            </div>
            <button
              onClick={() => remove(h.id)}
              className="text-red-500 hover:bg-red-50 p-2 rounded-md"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
