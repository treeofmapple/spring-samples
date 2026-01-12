"use client";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { LoginScreen } from "@/components/LoginScreen";
import { Dashboard } from "@/components/Dashboard";
import { CompanyRegistration } from "@/components/CompanyRegistration";
import { ClientEmployeeRegistration } from "@/components/ClientEmployeeRegistration";
import { ClinicStaffRegistration } from "@/components/ClinicStaffRegistration";
import { MedicalExams } from "@/components/MedicalExams";
import { ASOIssuance } from "@/components/ASOIssuance";
import { OccupationalRisks } from "@/components/OccupationalRisks";
import { Reports } from "@/components/Reports";
import { ProtectedRoute } from "@/functions/authentication/protectedRoute";
import { AuthProvider } from "@/functions/authentication/authProvider";
import { Unauthorized } from "@/components/Unauthorized";

export default function App() {
  return (
    <div className="min-h-screen bg-gray-50">
      <AuthProvider>
        <Router>
          <Routes>
            {/* Public Route */}
            <Route path="/login" element={<LoginScreen />} />

            {/* Protected Routes */}
            <Route element={<ProtectedRoute />}>
              <Route path="/" element={<Dashboard />} />
              <Route path="/dashboard" element={<Dashboard />} />
              <Route path="/companies" element={<CompanyRegistration />} />
              <Route path="/employees/client" element={<ClientEmployeeRegistration />} />
              <Route path="/employees/clinic" element={<ClinicStaffRegistration />} />
              <Route path="/exams" element={<MedicalExams />} />
              <Route path="/aso" element={<ASOIssuance />} />
              <Route path="/risks" element={<OccupationalRisks />} />
              <Route path="/reports" element={<Reports />} />
              <Route path="/unauthorized" element={<Unauthorized />} />
            </Route>

            {/* Catch all */}
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </Router>
      </AuthProvider>
    </div>
  );
}
