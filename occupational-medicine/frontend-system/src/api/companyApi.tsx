import { Company } from "../model/Company";
import { CompanyRegistrationInterface } from "../model/CompanyRegistration";

const BASE_API_URL = "http://127.0.0.1:8080";
const COMPANIES_ENDPOINT = "/empresas";

export const getCompanies = async (): Promise<Company[]> => {
  const response = await fetch(`${BASE_API_URL}${COMPANIES_ENDPOINT}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch companies: ${response.statusText}`);
  }

  return response.json();
};

export const getCompanyById = async (id: string): Promise<Company> => {
  const response = await fetch(`${BASE_API_URL}${COMPANIES_ENDPOINT}/${id}`);
  if (!response.ok) {
    throw new Error(`Failed to fetch company with id ${id}: ${response.statusText}`);
  }
  return response.json();
};

export const createCompany = async (
  companyData: CompanyRegistrationInterface,
): Promise<Company> => {
  const response = await fetch(`${BASE_API_URL}${COMPANIES_ENDPOINT}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(companyData),
  });

  if (!response.ok) {
    throw new Error(`Failed to create company: ${response.statusText}`);
  }

  return response.json();
};
