import { RiskAssignment } from "../model/RiskAssignment";

const BASE_API_URL = "http://127.0.0.1:8080";
const OCUPACIONAIS_ENDPOINT = "/riscos_ocupacionais";

export const getRiskAssignments = async (): Promise<RiskAssignment[]> => {
    const response = await fetch(`${BASE_API_URL}${OCUPACIONAIS_ENDPOINT}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch risk assignments: ${response.statusText}`);
  }

  return response.json();
};

export const createRiskAssignment = async (assignmentData: Omit<RiskAssignment, 'id'>): Promise<RiskAssignment> => {
      const response = await fetch(`${BASE_API_URL}${OCUPACIONAIS_ENDPOINT}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(assignmentData),
  });

  if (!response.ok) {
    throw new Error(`Failed to create risk assignment: ${response.statusText}`);
  }

  return response.json();
};