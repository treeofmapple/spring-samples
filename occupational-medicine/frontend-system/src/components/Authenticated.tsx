import React from "react";
import { Card, CardContent } from "./ui/card";
import { UserCheck } from "lucide-react";
import { User } from "../model/User";

interface AdminRouteGuardProps {
  user: User | null | undefined;
  children: React.ReactNode;
}

const pt = {
  "access.restrictedTitle": "Acesso Restrito",
  "access.restrictedMessage":
    "Apenas administradores podem acessar o registro de equipe da clínica.",
};

const AdminRouteGuard: React.FC<AdminRouteGuardProps> = ({
  user,
  children,
}) => {
  if (user?.role !== "admin") {
    return (
      <div className="flex items-center justify-center h-96">
        <Card className="p-6 text-center">
          <CardContent>
            <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <UserCheck className="w-8 h-8 text-red-600" />
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">
              {pt["access.restrictedTitle"] || "Acesso restrito"}
            </h3>
            <p className="text-gray-600">
              {pt["access.restrictedMessage"] ||
                "Apenas administradores podem acessar o registro de equipe da clínica."}
            </p>
          </CardContent>
        </Card>
      </div>
    );
  }
  return <>{children}</>;
};

export default AdminRouteGuard;
