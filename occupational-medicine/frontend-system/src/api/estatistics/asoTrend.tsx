import { AsoTrend } from "../../model/AsoTrend";

const BASE_API_URL = "http://127.0.0.1:8080";
const ASO_ENDPOINT = "/estatisticas/aso-trend";

export const getASOTrend = async (): Promise<AsoTrend[]> => {
  const response = await fetch(`${BASE_API_URL}${ASO_ENDPOINT}`);
  if (!response.ok) {
    throw new Error(`Failed to fetch ASOs: ${response.statusText}`);
  }
  return response.json();
};
