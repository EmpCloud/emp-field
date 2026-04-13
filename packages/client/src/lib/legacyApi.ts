// ============================================================================
// LEGACY API CLIENT
// ============================================================================
//
// Thin wrapper over the existing axios client that maps directly onto the
// legacy backend routes ported from commit 3409d88. Exposed as a flat object
// so legacy pages can call e.g. `legacyApi.holiday.list()` without juggling
// URLs themselves.
// ============================================================================

import { apiGet, apiPost, apiPut, apiDelete } from "@/api/client";

type Q = Record<string, any>;

export const legacyApi = {
  // ---- HOLIDAY ----
  holiday: {
    list: () => apiGet("/holiday/get-holiday"),
    create: (body: Q) => apiPost("/holiday/create-holiday", body),
    update: (body: Q) => apiPut("/holiday/update-holiday", body),
    remove: (id: string) => apiDelete(`/holiday/delete-holiday?id=${id}`),
  },

  // ---- LEAVE ----
  leave: {
    listTypes: () => apiGet("/leave/get-leave-type"),
    createType: (body: Q) => apiPost("/leave/create-leave-type", body),
    updateType: (body: Q) => apiPut("/leave/update-leave-type", body),
    deleteType: (id: string) => apiDelete(`/leave/delete-leave-type?id=${id}`),
    listLeaves: (body: Q) => apiPost("/leave/get-leaves", body),
    createLeave: (body: Q) => apiPost("/leave/create-leave", body),
    typeOptions: () => apiPost("/leave/fetch-leave-type"),
    updateLeave: (body: Q) => apiPost("/leave/update-leaves", body),
    deleteLeave: (body: Q) => apiPost("/leave/delete-leaves", body),
  },

  // ---- ROLE ----
  role: {
    list: () => apiGet("/role/get"),
    create: (role: string) => apiPost("/role/create", { role }),
    update: (id: string, role: string) => apiPut("/role/update", { id, role }),
    remove: (id: string) => apiDelete(`/role/delete?id=${id}`),
  },

  // ---- PROFILE ----
  profile: {
    fetch: () => apiGet("/profile/fetchProfile"),
    update: (body: Q) => apiPost("/profile/updateProfile", body),
    uploadImage: (url: string) => apiPost("/profile/uploadProfileImage", { url }),
    updateSnap: (snap_points_limit: number, snap_duration_limit: number) =>
      apiPost("/profile/update-snap-details", { snap_points_limit, snap_duration_limit }),
  },

  // ---- CATEGORY ----
  category: {
    list: () => apiGet("/category/fetch"),
    create: (category_name: string) => apiPost("/category/create", { category_name }),
    update: (id: string, category_name: string) => apiPut("/category/update", { id, category_name }),
    remove: (id: string) => apiDelete(`/category/delete?id=${id}`),
  },

  // ---- TAGS ----
  tags: {
    list: () => apiGet("/tags/getTags"),
    listAdmin: () => apiGet("/tags/getAdminTags"),
    create: (body: Q) => apiPost("/tags/createTags", body),
    update: (body: Q) => apiPost("/tags/updateTags", body),
    updateOrder: (items: { id: string; order: number }[]) =>
      apiPost("/tags/updateTagsOrder", { items }),
    remove: (id: string) => apiDelete(`/tags/deleteTags?id=${id}`),
  },

  // ---- TRANSPORT ----
  transport: {
    updateMode: (emp_id: string, mode: string) =>
      apiPost("/transport/Update-Emp-mode-of-transport", { emp_id, mode }),
    updateFreqRadius: (emp_id: string, frequency: number, radius: number) =>
      apiPost("/transport/Update-Emp-Mot-Frequency-Radius", { emp_id, frequency, radius }),
  },

  // ---- TRACKING ----
  tracking: {
    getLocation: (emp_id: string, date?: string) =>
      apiPost("/track/get-location", { emp_id, date }),
  },

  // ---- HRMS ADMIN ----
  hrmsAdmin: {
    updateLocation: (body: Q) => apiPost("/hrmsAdmin/location-update", body),
    getLocationDetails: () => apiPost("/hrmsAdmin/get-location-details"),
    getEmployeeConf: (employee_id: number) =>
      apiPost("/hrmsAdmin/get-employee-conf", { employee_id }),
    updateEmployeeConf: (employee_id: number, body: Q) =>
      apiPost("/hrmsAdmin/update-employee-conf", { employee_id, ...body }),
    orgLocationList: () => apiGet("/hrmsAdmin/org-location-list"),
    updateEmployeeLocation: (employee_id: number, body: Q) =>
      apiPost("/hrmsAdmin/updateEmployeeLocation", { employee_id, ...body }),
    getEmployeeLocation: (employee_id: number) =>
      apiGet(`/hrmsAdmin/getEmployeeLocation?employee_id=${employee_id}`),
  },

  // ---- AUTO EMAIL REPORT ----
  autoEmail: {
    create: (body: Q) => apiPost("/autoEmailReport/createAutoEmailReport", body),
    list: () => apiPost("/autoEmailReport/get"),
    update: (body: Q) => apiPost("/autoEmailReport/Update", body),
    remove: (id: string) => apiPost("/autoEmailReport/delete", { id }),
  },

  // ---- LEGACY CLIENT ----
  legacyClient: {
    list: () => apiGet("/client/fetch"),
    create: (body: Q) => apiPost("/client/create", body),
    update: (body: Q) => apiPut("/client/update", body),
    remove: (id: string) => apiDelete(`/client/delete?id=${id}`),
    uploadImage: (id: string, url: string) => apiPost("/client/clientUploadProfileImage", { id, url }),
  },

  // ---- LEGACY USER ----
  legacyUser: {
    list: () => apiGet("/user/fetch"),
    create: (body: Q) => apiPost("/user/create", body),
    update: (body: Q) => apiPut("/user/update", body),
    remove: (user_id: number) => apiDelete(`/user/delete?user_id=${user_id}`),
    fetchEmpUsers: () => apiGet("/user/fetch-emp-users"),
    addEmpUsers: (users: any[]) => apiPost("/user/add-emp-users", { users }),
    setFreqGeo: (user_id: number, frequency: number, geo_on: number) =>
      apiGet(`/user/user-frequency-geolocation?user_id=${user_id}&frequency=${frequency}&geo_on=${geo_on}`),
    updateBiometric: (user_id: number, enabled: number) =>
      apiPut("/user/update_Biometric_config", { user_id, enabled }),
  },

  // ---- ATTENDANCE ----
  attendance: {
    fetch: (body: Q) => apiPost("/attendance/fetch-attendance", body),
    mark: (body: Q) => apiPost("/attendance/mark-attendance", body),
    history: () => apiGet("/attendance/attendance"),
    request: (body: Q) => apiPost("/attendance/attendance-request", body),
  },

  // ---- TASK ----
  task: {
    list: (params?: Q) => apiGet("/task/fetch", params),
    create: (body: Q) => apiPost("/task/create", body),
    update: (body: Q) => apiPut("/task/update", body),
    remove: (id: string) => apiDelete(`/task/delete?id=${id}`),
    approve: (id: string, status: number) =>
      apiPut("/task/update-approve", { id, task_approve_status: status }),
    updateStatus: (body: Q) => apiPost("/task/update-taskStatus", body),
    uploadFiles: (id: string, files: any[], images: any[]) =>
      apiPost("/task/uploadTask-files", { id, files, images }),
    deleteDoc: (id: string, url: string) =>
      apiDelete(`/task/deleteDocument?id=${encodeURIComponent(id)}&url=${encodeURIComponent(url)}`),
    filter: (body: Q) => apiPost("/task/filterTask", body),
    notifications: () => apiGet("/task/getNotification"),
  },

  // ---- REPORTS ----
  reports: {
    consolidated: (body: Q) => apiPost("/reports/get-Consolidated-Reports", body),
    updateGeoFencing: (body: Q) => apiPost("/reports/updateGeoFencing", body),
    getUserDetails: (user_id: number) => apiPost("/reports/getUserDetails", { user_id }),
    taskList: (body: Q) => apiPost("/reports/taskListDetails", body),
    userStats: (user_id: number) => apiPost("/reports/userStats", { user_id }),
    taskStatus: () => apiPost("/reports/taskStatus"),
    taskStages: () => apiPost("/reports/taskStages"),
    clientDetails: () => apiPost("/reports/clientDetails"),
    distance: (body: Q) => apiPost("/reports/distanceTraveled", body),
    individualAttendance: (body: Q) => apiPost("/reports/getIndividualAttendanceData", body),
  },

  // ---- ADMIN ----
  admin: {
    signUp: (body: Q) => apiPost("/admin/sign-up", body),
    signIn: (email: string, password: string) => apiPost("/admin/sign-in", { email, password }),
    dashboard: () => apiGet("/admin/get-dashboard-stats"),
    fetchTask: () => apiGet("/admin/fetchTask"),
    fetchClient: () => apiGet("/admin/fetchClient"),
    allEmployees: (body: Q) => apiPost("/admin/allEmployeesAttendance", body),
    averageWorkingHours: () => apiGet("/admin/average-working-hours"),
    timeline: (body: Q) => apiPost("/admin/getUserTimeLine", body),
    departments: () => apiGet("/admin/getDepartment"),
    locations: () => apiGet("/admin/getLocation"),
    languages: () => apiGet("/admin/get-languages"),
    allOrgEmployees: (body: Q) => apiPost("/admin/allOrgEmployee", body),
  },
};
