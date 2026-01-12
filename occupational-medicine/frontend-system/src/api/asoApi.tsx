import { ASO } from "../model/ASO";

const BASE_API_URL = "http://127.0.0.1:8080";
const ASO_ENDPOINT = "/exames";

export const getASOs = async (): Promise<ASO[]> => {
  const response = await fetch(`${BASE_API_URL}${ASO_ENDPOINT}`);
  if (!response.ok) {
    throw new Error(`Failed to fetch ASOs: ${response.statusText}`);
  }
  return response.json();
};

export const createASO = async (asoData: Omit<ASO, "id">): Promise<ASO> => {
  const response = await fetch(`${BASE_API_URL}${ASO_ENDPOINT}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(asoData),
  });
  if (!response.ok) {
    throw new Error(`Failed to create ASO: ${response.statusText}`);
  }
  return response.json();
};
