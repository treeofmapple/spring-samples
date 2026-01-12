import { RiscoSeveridade } from "../../model/RiscoSeveridade";

const BASE_API_URL = "http://127.0.0.1:8080";
const ASO_ENDPOINT = "/estatisticas/risco-severidade";

export const getSeverityRisks = async (): Promise<RiscoSeveridade[]> => {
  const response = await fetch(`${BASE_API_URL}${ASO_ENDPOINT}`);
  if (!response.ok) {
    throw new Error(`Failed to fetch ASOs: ${response.statusText}`);
  }
  return response.json();
};
