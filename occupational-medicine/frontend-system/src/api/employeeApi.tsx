import { Employee } from "../model/Employee";

const BASE_API_URL = "http://127.0.0.1:8080";
const FUNCIONARIOS_ENDPOINT = "/funcionarios_cliente";
const COMPANIES_ENDPOINT = "/empresas";

export const getEmployees = async (): Promise<Employee[]> => {
    const response = await fetch(`${BASE_API_URL}${FUNCIONARIOS_ENDPOINT}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch employees: ${response.statusText}`);
  }

  return response.json();
};

export const getEmployeeById = async (id: string): Promise<Employee> => {
  const response = await fetch(`${BASE_API_URL}${FUNCIONARIOS_ENDPOINT}/${id}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch employee with id ${id}: ${response.statusText}`);
  }

  return response.json();
};

export const createEmployee = async (employeeData: Omit<Employee, 'id'>): Promise<Employee> => {
    const response = await fetch(`${BASE_API_URL}${FUNCIONARIOS_ENDPOINT}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(employeeData),
  });

  if (!response.ok) {
    throw new Error(`Failed to create employee: ${response.statusText}`);
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

export const getEmployeesWithCompanyName = async (): Promise<(Employee & { companyName: string })[]> => {
  const employees = await getEmployees();

  const employeesWithCompany = await Promise.all(
    employees.map(async (emp) => {
      try {
        const company = await getCompanyById(emp.id_empresa);
        return { ...emp, companyName: company.nome };
      } catch {
        return { ...emp, companyName: "Unknown" };
      }
    })
  );

  return employeesWithCompany;
};
