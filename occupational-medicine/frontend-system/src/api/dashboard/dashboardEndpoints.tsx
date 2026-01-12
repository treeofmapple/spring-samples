const BASE_API_URL = "http://127.0.0.1:8080";

const DASHBOARD_ENDPOINTS = {
  painel: "/dashboard/painel",
  empresas: "/dashboard/empresas",
  funcionarios: "/dashboard/funcionarios",
  clinica: "/dashboard/clinica",
  exames: "/dashboard/exames",
  riscos: "/dashboard/riscos",
  upcoming: "/dashboard/upcoming",
};

async function fetchJson<T>(url: string): Promise<T> {
  const response = await fetch(`${BASE_API_URL}${url}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch ${url}: ${response.statusText}`);
  }

  return response.json();
}

export const fetchPainel = () =>
  fetchJson<{
    total_companies: string;
    pending_exams: string;
    high_risk_cases: string;
  }>(DASHBOARD_ENDPOINTS.painel);

export const fetchEmpresas = () =>
  fetchJson<{ total_empresas: string; active_companies: string }>(
    DASHBOARD_ENDPOINTS.empresas,
  );

export const fetchFuncionarios = () =>
  fetchJson<{ empregados_ativos: string; novos_mes_atual: string }>(
    DASHBOARD_ENDPOINTS.funcionarios,
  );

export const fetchClinica = () =>
  fetchJson<{ doctors: string; employee: string; total_equipe: string }>(
    DASHBOARD_ENDPOINTS.clinica,
  );

export const fetchExames = () =>
  fetchJson<{ scheduled: string; completed: string; today: string }>(
    DASHBOARD_ENDPOINTS.exames,
  );

export const fetchRiscos = () =>
  fetchJson<{ total_risks: string; active_risks: string; critical: string }>(
    DASHBOARD_ENDPOINTS.riscos,
  );

export const fetchUpcoming = () =>
  fetchJson<
    {
      nome: string;
      nome_empresa: string;
      nome_exame: string;
      horario_exame: string;
      dia_exame: string;
    }[]
  >(DASHBOARD_ENDPOINTS.upcoming);
