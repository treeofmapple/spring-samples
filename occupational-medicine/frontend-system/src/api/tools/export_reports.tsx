import { toast } from "sonner";

const BASE_API_URL = "http://127.0.0.1:8080";
const ASO_ENDPOINT = "/tooling/resumo/aso";
const EXPORT_ALL_ENDPOINT = "/tooling/reports";

export const exportAsoResume = async (): Promise<void> => {
  const response = await fetch(`${BASE_API_URL}${ASO_ENDPOINT}`, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch ASO CSV: ${response.statusText}`);
  }

  const blob = await response.blob();

  const url = window.URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = "aso_resume_export.csv";
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(url);
};

export const exportAllReports = async (): Promise<void> => {
  const response = await fetch(`${BASE_API_URL}${EXPORT_ALL_ENDPOINT}`, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error(`Failed to fetch all reports: ${response.statusText}`);
  }

  const blob = await response.blob();

  const url = window.URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = "full_report_export.csv";
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(url);
};

export const handleExportAso = async () => {
  try {
    await exportAsoResume();
    toast.success("ASO resume exported successfully!");
  } catch (err) {
    toast.error("Failed to export ASO resume.");
    console.error(err);
  }
};

export const handleExportAll = async () => {
  try {
    await exportAllReports();
    toast.success("All reports exported successfully!");
  } catch (err) {
    toast.error("Failed to export reports.");
    console.error(err);
  }
};
