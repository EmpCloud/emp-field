import { useEffect, useState } from "react";
import { Settings as SettingsIcon, Save, Loader2 } from "lucide-react";
import toast from "react-hot-toast";
import { apiGet, apiPut } from "@/api/client";

type FieldSettings = {
  id?: string;
  check_in_radius_meters: number;
  photo_required: boolean;
  auto_checkout_hours: number;
  mileage_rate_per_km: number;
  tracking_interval_seconds: number;
};

const DEFAULT: FieldSettings = {
  check_in_radius_meters: 200,
  photo_required: false,
  auto_checkout_hours: 12,
  mileage_rate_per_km: 8,
  tracking_interval_seconds: 30,
};

export default function SettingsPage() {
  const [form, setForm] = useState<FieldSettings>(DEFAULT);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    setError(null);
    try {
      const res = await apiGet<FieldSettings>("/settings");
      const payload = (res?.data as any)?.data ?? res?.data ?? null;
      if (payload && typeof payload === "object") {
        setForm({ ...DEFAULT, ...payload });
      }
    } catch (e: any) {
      setError(e?.message || "Failed to load settings");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function save() {
    setSaving(true);
    try {
      await apiPut("/settings", form);
      toast.success("Settings saved");
    } catch (e: any) {
      toast.error(e?.message || "Failed to save");
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="space-y-6 max-w-2xl">
      <div className="flex items-center gap-3">
        <SettingsIcon className="w-7 h-7 text-brand-600" />
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Field Settings</h1>
          <p className="text-sm text-gray-500">Org-level field operation rules</p>
        </div>
      </div>

      {error && (
        <div className="rounded-lg bg-red-50 border border-red-200 p-4 text-sm text-red-700">
          {error}
        </div>
      )}

      {loading ? (
        <div className="flex items-center gap-2 text-gray-500">
          <Loader2 className="w-4 h-4 animate-spin" /> Loading…
        </div>
      ) : (
        <div className="rounded-xl border border-gray-200 bg-white p-6 space-y-5">
          <NumberField
            label="Check-in radius (meters)"
            hint="How close an agent must be to a client site to check in"
            value={form.check_in_radius_meters}
            onChange={(v) => setForm({ ...form, check_in_radius_meters: v })}
            min={10}
            max={50000}
          />

          <NumberField
            label="Auto check-out (hours)"
            hint="Active check-ins older than this are automatically closed"
            value={form.auto_checkout_hours}
            onChange={(v) => setForm({ ...form, auto_checkout_hours: v })}
            min={1}
            max={48}
          />

          <NumberField
            label="Mileage rate (per km)"
            hint="Reimbursement rate applied to tracked distance"
            value={form.mileage_rate_per_km}
            onChange={(v) => setForm({ ...form, mileage_rate_per_km: v })}
            min={0}
            step={0.1}
          />

          <NumberField
            label="GPS tracking interval (seconds)"
            hint="How often the mobile client emits a location ping while checked in"
            value={form.tracking_interval_seconds}
            onChange={(v) => setForm({ ...form, tracking_interval_seconds: v })}
            min={5}
            max={3600}
          />

          <label className="flex items-center gap-3">
            <input
              type="checkbox"
              checked={form.photo_required}
              onChange={(e) => setForm({ ...form, photo_required: e.target.checked })}
              className="h-4 w-4 rounded border-gray-300 text-brand-600"
            />
            <div>
              <p className="text-sm font-medium text-gray-900">Photo required at check-in</p>
              <p className="text-xs text-gray-500">Enforce a photo upload on every field check-in</p>
            </div>
          </label>

          <div className="pt-3 border-t border-gray-100">
            <button
              onClick={save}
              disabled={saving}
              className="inline-flex items-center gap-2 bg-brand-600 text-white px-4 py-2 rounded-md text-sm font-medium disabled:opacity-50"
            >
              {saving ? <Loader2 className="w-4 h-4 animate-spin" /> : <Save className="w-4 h-4" />}
              Save
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

function NumberField({
  label,
  hint,
  value,
  onChange,
  min,
  max,
  step,
}: {
  label: string;
  hint?: string;
  value: number;
  onChange: (v: number) => void;
  min?: number;
  max?: number;
  step?: number;
}) {
  return (
    <div>
      <label className="block text-sm font-medium text-gray-900">{label}</label>
      {hint && <p className="text-xs text-gray-500 mt-0.5">{hint}</p>}
      <input
        type="number"
        value={value}
        min={min}
        max={max}
        step={step}
        onChange={(e) => onChange(Number(e.target.value))}
        className="mt-2 w-full rounded-md border border-gray-300 px-3 py-2 text-sm font-mono"
      />
    </div>
  );
}
