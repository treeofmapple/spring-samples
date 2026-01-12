import { OccupationalRisk } from "../model/RisksByCategory";

const BASE_API_URL = "http://127.0.0.1:8080";
const RISKS_ENDPOINT = "/riscos_ocupacionais";

export const getOccupationalRisks = async (): Promise<OccupationalRisk[]> => {
  const response = await fetch(`${BASE_API_URL}${RISKS_ENDPOINT}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch occupational risks: ${response.statusText}`);
  }

  return response.json();
};

export const createOccupationalRisk = async (riskData: Omit<OccupationalRisk, 'id'>): Promise<OccupationalRisk> => {
  const response = await fetch(`${BASE_API_URL}${RISKS_ENDPOINT}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(riskData),
  });

  if (!response.ok) {
    throw new Error(`Failed to create occupational risk: ${response.statusText}`);
  }

  return response.json();
};
