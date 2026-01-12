import { MedicalExam } from "../model/MedicalExam";

const BASE_API_URL = "http://127.0.0.1:8080";
const MEDICAL_EXAMS_ENDPOINT = "/exames";

export const getMedicalExams = async (): Promise<MedicalExam[]> => {
  const response = await fetch(`${BASE_API_URL}${MEDICAL_EXAMS_ENDPOINT}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch medical exams: ${response.statusText}`);
  }

  return response.json();
};

export const createMedicalExam = async (examData: Omit<MedicalExam, 'id'>): Promise<MedicalExam> => {
  const response = await fetch(`${BASE_API_URL}${MEDICAL_EXAMS_ENDPOINT}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(examData),
  });

  if (!response.ok) {
    throw new Error(`Failed to create medical exam: ${response.statusText}`);
  }

  return response.json();
};