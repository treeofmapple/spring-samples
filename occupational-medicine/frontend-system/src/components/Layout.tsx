import React from "react";
import { Link, useLocation } from "react-router-dom";
import { Button } from "./ui/button";
import { Avatar, AvatarFallback } from "./ui/avatar";
import { useAuth } from "../App";
import {
  LayoutDashboard,
  Building2,
  Users,
  UserCheck,
  ClipboardList,
  FileText,
  AlertTriangle,
  BarChart3,
  LogOut,
  Stethoscope,
} from "lucide-react";
import pt from "../i18n/pt-BR";

export function Layout({ children }: { children: React.ReactNode }) {
  const { user, logout } = useAuth();
  const location = useLocation();

  const navigationItems: Array<{
    path: string;
    label: string;
    icon: any;
    allowedRoles?: string[];
  }> = [
    { path: "/dashboard", label: pt["nav.dashboard"], icon: LayoutDashboard },
    {
      path: "/companies",
      label: pt["nav.companies"],
      icon: Building2,
      allowedRoles: ["admin", "employee"],
    },
    {
      path: "/employees/client",
      label: pt["nav.clientEmployees"],
      icon: Users,
      allowedRoles: ["admin", "employee"],
    },
    {
      path: "/employees/clinic",
      label: pt["nav.clinicStaff"],
      icon: UserCheck,
      allowedRoles: ["admin"],
    },
    {
      path: "/exams",
      label: pt["nav.medicalExams"],
      icon: ClipboardList,
      allowedRoles: ["admin", "doctor"],
    },
    {
      path: "/aso",
      label: pt["nav.asoIssuance"],
      icon: FileText,
      allowedRoles: ["admin", "doctor"],
    },
    {
      path: "/risks",
      label: pt["nav.occupationalRisks"],
      icon: AlertTriangle,
      allowedRoles: ["admin", "doctor"],
    },
    {
      path: "/reports",
      label: pt["nav.reports"],
      icon: BarChart3,
      allowedRoles: ["admin", "doctor", "employee"],
    },
  ];

  const filteredNavItems = navigationItems.filter((item) => {
    if (!item.allowedRoles || item.allowedRoles.length === 0) return true;
    return !!user && item.allowedRoles.includes(user.role);
  });

  return (
    <div className="h-screen flex bg-gray-50">
      {/* Sidebar */}
      <div className="w-64 bg-white border-r border-gray-200 flex flex-col">
        {/* Logo */}
        <div className="h-16 flex items-center px-6 border-b border-gray-200">
          <div className="flex items-center space-x-3">
            <div className="w-8 h-8 bg-blue-600 rounded-md flex items-center justify-center">
              <Stethoscope className="w-5 h-5 text-white" />
            </div>
            <div>
              <h1 className="text-lg font-semibold text-gray-900">
                {pt["app.title"]}
              </h1>
              <p className="text-xs text-gray-500">{pt["app.subtitle"]}</p>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <nav className="flex-1 px-4 py-6 space-y-2">
          {filteredNavItems.map((item) => {
            const Icon = item.icon;
            const isActive = location.pathname === item.path;

            return (
              <Link key={item.path} to={item.path}>
                <div
                  className={`flex items-center space-x-3 px-3 py-2 rounded-md transition-colors ${
                    isActive
                      ? "bg-blue-50 text-blue-700 border border-blue-200"
                      : "text-gray-700 hover:bg-gray-100"
                  }`}
                >
                  <Icon className="w-5 h-5" />
                  <span className="text-sm font-medium">{item.label}</span>
                </div>
              </Link>
            );
          })}
        </nav>

        {/* User Profile */}
        <div className="border-t border-gray-200 p-4">
          <div className="flex items-center space-x-3 mb-3">
            <Avatar>
              <AvatarFallback className="bg-blue-100 text-blue-700">
                {user?.name
                  ?.split(" ")
                  .map((n: string) => n[0])
                  .join("") || "U"}
              </AvatarFallback>
            </Avatar>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 truncate">
                {user?.name}
              </p>
              <p className="text-xs text-gray-500 truncate capitalize">
                {user?.role}
              </p>
            </div>
          </div>
          <Button
            variant="outline"
            size="sm"
            className="w-full"
            onClick={logout}
          >
            <LogOut className="w-4 h-4 mr-2" />
            {pt["nav.signOut"]}
          </Button>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col min-w-0">
        {/* Header */}
        <header className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6">
          <div>
            <h2 className="text-xl font-semibold text-gray-900">
              {navigationItems.find((item) => item.path === location.pathname)
                ?.label || "Dashboard"}
            </h2>
          </div>
          <div className="flex items-center space-x-4">
            <div className="text-sm text-gray-500">
              {new Date().toLocaleDateString("en-US", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric",
              })}
            </div>
          </div>
        </header>

        {/* Page Content */}
        <main className="flex-1 overflow-auto p-6">{children}</main>
      </div>
    </div>
  );
}
