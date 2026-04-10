// ============================================================================
// EMPCLOUD DATABASE CONNECTION
// Separate Knex connection to the EmpCloud master database.
// Used for authentication, user lookups, and org data.
// ============================================================================

import knex from "knex";
import type { Knex } from "knex";
import { config } from "../config";
import { logger } from "../utils/logger";

let empcloudDb: Knex | null = null;

/**
 * Initialize the EmpCloud database connection.
 */
export async function initEmpCloudDB(): Promise<Knex> {
  if (empcloudDb) return empcloudDb;

  const { empcloudDb: dbConfig } = config;

  empcloudDb = knex({
    client: "mysql2",
    connection: {
      host: dbConfig.host,
      port: dbConfig.port,
      user: dbConfig.user,
      password: dbConfig.password,
      database: dbConfig.name,
    },
    pool: { min: 2, max: 10 },
  });

  await empcloudDb.raw("SELECT 1");
  logger.info(`EmpCloud database connected (${dbConfig.host}:${dbConfig.port}/${dbConfig.name})`);

  return empcloudDb;
}

/**
 * Get the EmpCloud Knex instance.
 */
export function getEmpCloudDB(): Knex {
  if (!empcloudDb) {
    throw new Error("EmpCloud database not initialized. Call initEmpCloudDB() first.");
  }
  return empcloudDb;
}

/**
 * Close the EmpCloud database connection.
 */
export async function closeEmpCloudDB(): Promise<void> {
  if (empcloudDb) {
    await empcloudDb.destroy();
    empcloudDb = null;
  }
}

// ---------------------------------------------------------------------------
// Query helpers for common EmpCloud lookups
// ---------------------------------------------------------------------------

export interface EmpCloudUser {
  id: number;
  organization_id: number;
  first_name: string;
  last_name: string;
  email: string;
  password: string | null;
  emp_code: string | null;
  contact_number: string | null;
  date_of_birth: string | null;
  gender: string | null;
  date_of_joining: string | null;
  date_of_exit: string | null;
  designation: string | null;
  department_id: number | null;
  location_id: number | null;
  reporting_manager_id: number | null;
  employment_type: string;
  role: string;
  status: number;
  created_at: Date;
  updated_at: Date;
}

export interface EmpCloudOrganization {
  id: number;
  name: string;
  legal_name: string | null;
  email: string | null;
  contact_number: string | null;
  timezone: string | null;
  country: string;
  state: string | null;
  city: string | null;
  is_active: boolean;
  created_at: Date;
  updated_at: Date;
}

export async function findUserByEmail(email: string): Promise<EmpCloudUser | null> {
  const db = getEmpCloudDB();
  const user = await db("users").where({ email, status: 1 }).first();
  return user || null;
}

export async function findUserById(id: number): Promise<EmpCloudUser | null> {
  const db = getEmpCloudDB();
  const user = await db("users").where({ id }).first();
  return user || null;
}

export async function findOrgById(id: number): Promise<EmpCloudOrganization | null> {
  const db = getEmpCloudDB();
  const org = await db("organizations").where({ id }).first();
  return org || null;
}

export async function findUsersByOrgId(
  orgId: number,
  options?: { limit?: number; offset?: number }
): Promise<EmpCloudUser[]> {
  const db = getEmpCloudDB();
  let query = db("users").where({ organization_id: orgId, status: 1 });
  if (options?.limit) query = query.limit(options.limit);
  if (options?.offset) query = query.offset(options.offset);
  return query;
}

export async function countUsersByOrgId(orgId: number): Promise<number> {
  const db = getEmpCloudDB();
  const [{ count }] = await db("users")
    .where({ organization_id: orgId, status: 1 })
    .count("* as count");
  return Number(count);
}
