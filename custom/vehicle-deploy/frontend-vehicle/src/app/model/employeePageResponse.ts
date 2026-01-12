import { Employee } from "./employee";

export interface PageEmployeeResponse {
  content: Employee[];
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
}