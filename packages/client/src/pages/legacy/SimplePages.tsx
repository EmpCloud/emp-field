// ============================================================================
// SIMPLE LEGACY PAGES
// ============================================================================
//
// Compact CRUD pages for the smaller legacy modules. Each one mirrors the
// equivalent React JSX page from commit 3409d88 (Roles, Categories, Tags,
// Profile, Settings, GeoLocation, Reports, Dashboard, Tasks, Clients,
// Employees, Timeline, Stages).
// ============================================================================

import { useEffect, useState } from "react";
import {
  Plus,
  Trash2,
  Save,
  Tag as TagIcon,
  ShieldCheck,
  Layers,
  MapPin,
  BarChart3,
  Users,
  Building2,
  ListChecks,
  Activity,
} from "lucide-react";
import toast from "react-hot-toast";
import { legacyApi } from "@/lib/legacyApi";

// ============================================================================
// ROLES
// ============================================================================
export function RolesPage() {
  const [roles, setRoles] = useState<any[]>([]);
  const [name, setName] = useState("");
  async function load() {
    const r = await legacyApi.role.list();
    setRoles((r?.data as any[]) || []);
  }
  useEffect(() => {
    load();
  }, []);
  return (
    <PageShell title="Roles" subtitle="Org-level role definitions" Icon={ShieldCheck}>
      <CreateBar
        value={name}
        onChange={setName}
        placeholder="Role name"
        onSubmit={async () => {
          if (!name) return;
          await legacyApi.role.create(name);
          setName("");
          load();
        }}
      />
      <SimpleList
        items={roles.map((r) => ({ id: r.id, primary: r.role }))}
        onDelete={async (id) => {
          await legacyApi.role.remove(id);
          load();
        }}
      />
    </PageShell>
  );
}

// ============================================================================
// CATEGORIES
// ============================================================================
export function CategoriesPage() {
  const [items, setItems] = useState<any[]>([]);
  const [name, setName] = useState("");
  async function load() {
    const r = await legacyApi.category.list();
    setItems((r?.data as any[]) || []);
  }
  useEffect(() => {
    load();
  }, []);
  return (
    <PageShell title="Categories" subtitle="Task / client categories" Icon={Layers}>
      <CreateBar
        value={name}
        onChange={setName}
        placeholder="Category name"
        onSubmit={async () => {
          if (!name) return;
          await legacyApi.category.create(name);
          setName("");
          load();
        }}
      />
      <SimpleList
        items={items.map((c) => ({ id: c.id, primary: c.category_name }))}
        onDelete={async (id) => {
          await legacyApi.category.remove(id);
          load();
        }}
      />
    </PageShell>
  );
}

// ============================================================================
// TAGS
// ============================================================================
export function TagsPage() {
  const [items, setItems] = useState<any[]>([]);
  const [name, setName] = useState("");
  const [color, setColor] = useState("#6366f1");
  async function load() {
    const r = await legacyApi.tags.listAdmin();
    setItems((r?.data as any[]) || []);
  }
  useEffect(() => {
    load();
  }, []);
  return (
    <PageShell title="Tags" subtitle="Tag system for tasks and clients" Icon={TagIcon}>
      <form
        onSubmit={async (e) => {
          e.preventDefault();
          if (!name) return;
          await legacyApi.tags.create({ tag_name: name, color, order: 1 });
          setName("");
          load();
        }}
        className="flex gap-3 items-end rounded-xl bg-white p-4 border border-gray-200"
      >
        <div className="flex-1">
          <label className="text-xs font-medium text-gray-600">Tag name</label>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
          />
        </div>
        <div>
          <label className="text-xs font-medium text-gray-600">Colour</label>
          <input
            type="color"
            value={color}
            onChange={(e) => setColor(e.target.value)}
            className="mt-1 h-9 w-12 rounded-md border border-gray-300"
          />
        </div>
        <button className="inline-flex items-center gap-1 bg-brand-600 text-white px-4 py-2 rounded-md text-sm font-medium">
          <Plus className="w-4 h-4" /> Add
        </button>
      </form>
      <ul className="rounded-xl border border-gray-200 bg-white divide-y divide-gray-100">
        {items.map((t) => (
          <li key={t.id} className="flex items-center justify-between px-5 py-3">
            <div className="flex items-center gap-3">
              <span
                className="inline-block w-3 h-3 rounded-full"
                style={{ backgroundColor: t.color || "#999" }}
              />
              <p className="text-sm font-medium text-gray-900">{t.tag_name}</p>
            </div>
            <button
              onClick={async () => {
                await legacyApi.tags.remove(t.id);
                load();
              }}
              className="text-red-500 p-2 rounded-md hover:bg-red-50"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          </li>
        ))}
      </ul>
    </PageShell>
  );
}

// ============================================================================
// PROFILE
// ============================================================================
export function ProfilePage() {
  const [profile, setProfile] = useState<any>(null);
  const [form, setForm] = useState<any>({});
  async function load() {
    const r = await legacyApi.profile.fetch();
    setProfile(r?.data);
    setForm(r?.data || {});
  }
  useEffect(() => {
    load();
  }, []);
  return (
    <PageShell title="My Profile" subtitle="Personal information and preferences" Icon={Users}>
      <div className="rounded-xl bg-white p-6 border border-gray-200 space-y-4">
        {profile == null && <p className="text-sm text-gray-500">Loading…</p>}
        {profile != null && (
          <>
            <FormField
              label="Full name"
              value={form.full_name || ""}
              onChange={(v) => setForm({ ...form, full_name: v })}
            />
            <FormField
              label="Email"
              value={form.email || ""}
              onChange={(v) => setForm({ ...form, email: v })}
            />
            <FormField
              label="Phone"
              value={form.phone_number || ""}
              onChange={(v) => setForm({ ...form, phone_number: v })}
            />
            <FormField
              label="Department"
              value={form.department || ""}
              onChange={(v) => setForm({ ...form, department: v })}
            />
            <FormField
              label="City"
              value={form.city || ""}
              onChange={(v) => setForm({ ...form, city: v })}
            />
            <button
              onClick={async () => {
                try {
                  await legacyApi.profile.update(form);
                  toast.success("Profile updated");
                  load();
                } catch (e: any) {
                  toast.error(e?.message || "Update failed");
                }
              }}
              className="inline-flex items-center gap-2 bg-brand-600 text-white px-4 py-2 rounded-md text-sm font-medium"
            >
              <Save className="w-4 h-4" /> Save
            </button>
          </>
        )}
      </div>
    </PageShell>
  );
}

// ============================================================================
// GEO LOCATION
// ============================================================================
export function GeoLocationPage() {
  const [items, setItems] = useState<any[]>([]);
  async function load() {
    const r = await legacyApi.hrmsAdmin.orgLocationList();
    setItems((r?.data as any[]) || []);
  }
  useEffect(() => {
    load();
  }, []);
  return (
    <PageShell title="Geo Locations" subtitle="Org-level geofence settings" Icon={MapPin}>
      <ul className="rounded-xl border border-gray-200 bg-white divide-y divide-gray-100">
        {items.length === 0 && <li className="p-6 text-sm text-gray-500">No locations configured</li>}
        {items.map((l) => (
          <li key={l.id} className="px-5 py-3">
            <p className="text-sm font-medium text-gray-900">{l.location_name || "Unnamed"}</p>
            <p className="text-xs text-gray-500 font-mono">
              {l.latitude}, {l.longitude} (range {l.range}m)
            </p>
          </li>
        ))}
      </ul>
    </PageShell>
  );
}

// ============================================================================
// REPORTS
// ============================================================================
export function ReportsPage() {
  const [data, setData] = useState<any>(null);
  async function run() {
    const today = new Date().toISOString().slice(0, 10);
    const r = await legacyApi.reports.consolidated({ start_date: today, end_date: today });
    setData(r?.data);
  }
  useEffect(() => {
    run();
  }, []);
  return (
    <PageShell title="Reports" subtitle="Consolidated daily reporting" Icon={BarChart3}>
      <div className="rounded-xl border border-gray-200 bg-white p-6">
        {data == null ? (
          <p className="text-sm text-gray-500">Loading…</p>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-5 gap-4">
            {[
              { k: "tasks", label: "Tasks" },
              { k: "attendance", label: "Attendance" },
              { k: "leaves", label: "Leaves" },
              { k: "mileage", label: "Mileage" },
              { k: "expenses", label: "Expenses" },
            ].map((card) => (
              <div key={card.k} className="rounded-lg border border-gray-100 bg-gray-50 p-4 text-center">
                <p className="text-2xl font-bold text-gray-900">{(data?.[card.k] || []).length}</p>
                <p className="text-xs text-gray-500 mt-1">{card.label}</p>
              </div>
            ))}
          </div>
        )}
      </div>
    </PageShell>
  );
}

// ============================================================================
// LEGACY CLIENTS
// ============================================================================
export function LegacyClientsPage() {
  const [items, setItems] = useState<any[]>([]);
  async function load() {
    const r = await legacyApi.legacyClient.list();
    setItems((r?.data as any[]) || []);
  }
  useEffect(() => {
    load();
  }, []);
  return (
    <PageShell title="Clients (legacy)" subtitle="Client directory" Icon={Building2}>
      <ul className="rounded-xl border border-gray-200 bg-white divide-y divide-gray-100">
        {items.length === 0 && <li className="p-6 text-sm text-gray-500">No clients yet</li>}
        {items.map((c) => (
          <li key={c.id} className="px-5 py-3">
            <p className="text-sm font-medium text-gray-900">{c.client_name}</p>
            <p className="text-xs text-gray-500">
              {c.city ? `${c.city}, ` : ""}
              {c.country}
            </p>
          </li>
        ))}
      </ul>
    </PageShell>
  );
}

// ============================================================================
// LEGACY USERS / EMPLOYEES
// ============================================================================
export function EmployeesPage() {
  const [items, setItems] = useState<any[]>([]);
  async function load() {
    const r = await legacyApi.legacyUser.fetchEmpUsers();
    setItems((r?.data as any[]) || []);
  }
  useEffect(() => {
    load();
  }, []);
  return (
    <PageShell title="Employees" subtitle="Field employees directory" Icon={Users}>
      <ul className="rounded-xl border border-gray-200 bg-white divide-y divide-gray-100">
        {items.length === 0 && <li className="p-6 text-sm text-gray-500">No employees yet</li>}
        {items.map((u) => (
          <li key={u.id} className="px-5 py-3">
            <p className="text-sm font-medium text-gray-900">{u.full_name}</p>
            <p className="text-xs text-gray-500">{u.email}</p>
          </li>
        ))}
      </ul>
    </PageShell>
  );
}

// ============================================================================
// LEGACY TASKS
// ============================================================================
export function LegacyTasksPage() {
  const [items, setItems] = useState<any[]>([]);
  async function load() {
    const r = await legacyApi.task.list();
    setItems((r?.data as any[]) || []);
  }
  useEffect(() => {
    load();
  }, []);
  return (
    <PageShell title="Tasks" subtitle="Field task assignments" Icon={ListChecks}>
      <ul className="rounded-xl border border-gray-200 bg-white divide-y divide-gray-100">
        {items.length === 0 && <li className="p-6 text-sm text-gray-500">No tasks yet</li>}
        {items.map((t) => (
          <li key={t.id} className="px-5 py-3">
            <p className="text-sm font-medium text-gray-900">{t.task_name}</p>
            <p className="text-xs text-gray-500">
              Emp #{t.emp_id} • {t.date || "no date"}
            </p>
          </li>
        ))}
      </ul>
    </PageShell>
  );
}

// ============================================================================
// TIMELINE
// ============================================================================
export function TimelinePage() {
  const [events, setEvents] = useState<any[]>([]);
  useEffect(() => {
    legacyApi.admin.timeline({ userId: 0, date: new Date().toISOString().slice(0, 10) }).then((r) => {
      setEvents((r?.data as any[]) || []);
    });
  }, []);
  return (
    <PageShell title="User Timeline" subtitle="Daily user activity timeline" Icon={Activity}>
      <ul className="rounded-xl border border-gray-200 bg-white divide-y divide-gray-100">
        {events.length === 0 && <li className="p-6 text-sm text-gray-500">No events for today</li>}
        {events.map((e, idx) => (
          <li key={idx} className="px-5 py-3">
            <p className="text-sm font-medium text-gray-900">{e.tracking_type}</p>
            <p className="text-xs text-gray-500 font-mono">
              {e.latitude}, {e.longitude} • {new Date(e.date).toLocaleTimeString()}
            </p>
          </li>
        ))}
      </ul>
    </PageShell>
  );
}

// ============================================================================
// SHARED COMPONENTS
// ============================================================================
function PageShell({
  title,
  subtitle,
  Icon,
  children,
}: {
  title: string;
  subtitle: string;
  Icon: any;
  children: React.ReactNode;
}) {
  return (
    <div className="space-y-6">
      <div className="flex items-center gap-3">
        <Icon className="w-7 h-7 text-brand-600" />
        <div>
          <h1 className="text-2xl font-bold text-gray-900">{title}</h1>
          <p className="text-sm text-gray-500">{subtitle}</p>
        </div>
      </div>
      {children}
    </div>
  );
}

function CreateBar({
  value,
  onChange,
  placeholder,
  onSubmit,
}: {
  value: string;
  onChange: (v: string) => void;
  placeholder: string;
  onSubmit: () => void;
}) {
  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        onSubmit();
      }}
      className="flex gap-3 items-end rounded-xl bg-white p-4 border border-gray-200"
    >
      <input
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder}
        className="flex-1 rounded-md border border-gray-300 px-3 py-2 text-sm"
      />
      <button className="inline-flex items-center gap-1 bg-brand-600 text-white px-4 py-2 rounded-md text-sm font-medium">
        <Plus className="w-4 h-4" /> Add
      </button>
    </form>
  );
}

function SimpleList({
  items,
  onDelete,
}: {
  items: { id: string; primary: string }[];
  onDelete: (id: string) => void;
}) {
  return (
    <ul className="rounded-xl border border-gray-200 bg-white divide-y divide-gray-100">
      {items.length === 0 && <li className="p-6 text-sm text-gray-500">No items yet</li>}
      {items.map((it) => (
        <li key={it.id} className="flex items-center justify-between px-5 py-3">
          <p className="text-sm font-medium text-gray-900">{it.primary}</p>
          <button
            onClick={() => onDelete(it.id)}
            className="text-red-500 p-2 rounded-md hover:bg-red-50"
          >
            <Trash2 className="w-4 h-4" />
          </button>
        </li>
      ))}
    </ul>
  );
}

function FormField({
  label,
  value,
  onChange,
}: {
  label: string;
  value: string;
  onChange: (v: string) => void;
}) {
  return (
    <div>
      <label className="text-xs font-medium text-gray-600">{label}</label>
      <input
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="mt-1 w-full rounded-md border border-gray-300 px-3 py-2 text-sm"
      />
    </div>
  );
}
