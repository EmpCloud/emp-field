import { Building2, Plus } from "lucide-react";

const SAMPLE_SITES = [
  { id: 1, name: "Acme Corp HQ", address: "123 Business Park, Mumbai", status: "active", agents: 3 },
  { id: 2, name: "TechStart Office", address: "456 Innovation Hub, Pune", status: "active", agents: 2 },
  { id: 3, name: "Global Services", address: "789 Commerce Ave, Delhi", status: "inactive", agents: 0 },
];

export default function ClientSitesPage() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Client Sites</h1>
          <p className="text-sm text-gray-500 mt-1">Manage client locations and site assignments</p>
        </div>
        <button className="flex items-center gap-2 rounded-lg bg-brand-600 px-4 py-2 text-sm font-medium text-white hover:bg-brand-700 transition-colors">
          <Plus className="h-4 w-4" />
          Add Site
        </button>
      </div>

      <div className="overflow-hidden rounded-xl border border-gray-200 bg-white">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Site Name
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Address
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Status
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider text-gray-500">
                Agents
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {SAMPLE_SITES.map((site) => (
              <tr key={site.id} className="hover:bg-gray-50">
                <td className="whitespace-nowrap px-6 py-4">
                  <div className="flex items-center gap-3">
                    <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-brand-50">
                      <Building2 className="h-4 w-4 text-brand-600" />
                    </div>
                    <span className="text-sm font-medium text-gray-900">{site.name}</span>
                  </div>
                </td>
                <td className="whitespace-nowrap px-6 py-4 text-sm text-gray-500">
                  {site.address}
                </td>
                <td className="whitespace-nowrap px-6 py-4">
                  <span
                    className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${
                      site.status === "active"
                        ? "bg-green-100 text-green-700"
                        : "bg-gray-100 text-gray-600"
                    }`}
                  >
                    {site.status}
                  </span>
                </td>
                <td className="whitespace-nowrap px-6 py-4 text-sm text-gray-500">
                  {site.agents}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
