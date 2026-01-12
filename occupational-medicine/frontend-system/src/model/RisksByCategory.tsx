export interface OccupationalRisk {
  id: string;
  name_risco: string;
  risk_category: "physical" | "chemical" | "biological" | "ergonomic" | "accident";
  severity: "low" | "medium" | "high";
  descricao: string;
  preventive_measures: string;
  affectedEmployees: string[];
  status: "active" | "controlled" | "eliminated";
}
