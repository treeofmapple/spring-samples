"use client";

import React, { useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/ui/card";
import { Building2, Calendar } from "lucide-react";
import { useAuth } from "@/functions/authentication/useAuth";
import { Company } from "@/model/Company";
import { MedicalExam } from "@/model/MedicalExam";
import { getCompanies } from "@/api/companyApi";
import { getMedicalExams } from "@/api/medicalExamApi";

export const Dashboard = () => {
  const { user } = useAuth();

  const [companies, setCompanies] = useState<Company[]>([]);
  const [exams, setExams] = useState<MedicalExam[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadData = async (): Promise<void> => {
      try {
        setLoading(true);
        setError(null);

        const [companiesData, examsData] = await Promise.all([
          getCompanies(),
          getMedicalExams(),
        ]);

        setCompanies(companiesData);
        setExams(examsData);
      } catch (err) {
        if (err instanceof Error) {
          setError(err.message);
        } else {
          setError("Failed to load data.");
        }
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  if (loading) {
    return <div className="p-6 text-gray-600">Loading data...</div>;
  }

  if (error) {
    return (
      <div className="p-6 text-red-500">
        ⚠️ Error loading dashboard: {error}
      </div>
    );
  }

  const statsCards = [
    {
      title: "Registered Companies",
      value: companies.length,
      icon: Building2,
      color: "bg-blue-500",
    },
    {
      title: "Total Exams",
      value: exams.length,
      icon: Calendar,
      color: "bg-green-500",
    },
    {
      title: "Completed Exams",
      value: exams.filter((e) => e.status === "completed").length,
      icon: Calendar,
      color: "bg-emerald-500",
    },
    {
      title: "Pending Exams",
      value: exams.filter((e) => e.status === "scheduled").length,
      icon: Calendar,
      color: "bg-yellow-500",
    },
  ];

  const upcomingExams = exams
    .filter((e) => new Date(e.date) >= new Date() && e.status === "scheduled")
    .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
    .slice(0, 5); // limit to 5

  return (
    <div className="space-y-6">
      {/* Welcome Section */}
      <div className="bg-gradient-to-r from-blue-500 to-blue-600 rounded-lg p-6 text-white">
        <h1 className="text-2xl font-semibold mb-2">
          Welcome back, {user?.name}!
        </h1>
        <p className="text-blue-100">
          {"Here's what's happening at your clinic today"}
        </p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {statsCards.map((stat, index) => {
          const Icon = stat.icon;
          return (
            <Card key={index} className="hover:shadow-md transition-shadow">
              <CardContent className="p-6">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-600 mb-1">
                      {stat.title}
                    </p>
                    <p className="text-2xl font-semibold text-gray-900">
                      {stat.value}
                    </p>
                  </div>
                  <div
                    className={`w-12 h-12 rounded-lg ${stat.color} flex items-center justify-center`}
                  >
                    <Icon className="w-6 h-6 text-white" />
                  </div>
                </div>
              </CardContent>
            </Card>
          );
        })}
      </div>

      {/* Upcoming Exams */}
      <Card>
        <CardHeader className="pb-4">
          <CardTitle className="flex items-center space-x-2">
            <Calendar className="w-5 h-5" />
            <span>Upcoming Exams</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          {upcomingExams.length === 0 ? (
            <p className="text-gray-500 text-sm">No upcoming exams.</p>
          ) : (
            <div className="space-y-3">
              {upcomingExams.map((exam) => (
                <div
                  key={exam.id}
                  className="flex items-center justify-between p-3 border border-gray-200 rounded-md"
                >
                  <div className="flex-1">
                    <p className="font-medium text-gray-900 text-sm">
                      {exam.employee}
                    </p>
                    <p className="text-xs text-gray-500">
                      {exam.company} • {exam.type.replace(/_/g, " ")}
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-medium text-gray-900">
                      {new Date(exam.date).toLocaleDateString("en-US", {
                        month: "short",
                        day: "numeric",
                      })}
                    </p>
                    <p className="text-xs text-gray-500">{exam.time}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
};
