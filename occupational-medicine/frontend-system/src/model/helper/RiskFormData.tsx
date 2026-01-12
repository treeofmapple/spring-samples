export interface RiskFormData {
  name: string;
  category:
    | "physical"
    | "chemical"
    | "biological"
    | "ergonomic"
    | "accident"
    | "";
  severity: "low" | "medium" | "high" | "critical" | "";
  description: string;
  preventiveMeasures: string;
}
