import { ClinicStaff } from "../model/ClinicStaff";

const BASE_API_URL = "http://127.0.0.1:8080";
const CLINIC_STAFF_ENDPOINT = "/funcionarios_clinica";

export const getClinicStaff = async (): Promise<ClinicStaff[]> => {
  const response = await fetch(`${BASE_API_URL}${CLINIC_STAFF_ENDPOINT}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch clinic staff: ${response.statusText}`);
  }

  return response.json();
};

export const createClinicStaff = async (staffData: Omit<ClinicStaff, 'id'>): Promise<ClinicStaff> => {
  const response = await fetch(`${BASE_API_URL}${CLINIC_STAFF_ENDPOINT}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(staffData),
  });

  if (!response.ok) {
    throw new Error(`Failed to create clinic staff: ${response.statusText}`);
  }

  return response.json();
};