import React, { useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Button } from "./ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";
import { Badge } from "./ui/badge";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  LineChart,
  Line,
} from "recharts";
import {
  BarChart3,
  Download,
  Calendar,
  TrendingUp,
  AlertTriangle,
  Users,
  Building2,
  FileText,
  Loader2,
} from "lucide-react";
import { toast } from "sonner";
import { getSeverityRisks } from "../api/estatistics/severityRisk";
import { getMonthExams } from "../api/estatistics/monthExams";
import { getASOTrend } from "../api/estatistics/asoTrend";
import { getFitness } from "../api/estatistics/fitnessTrendApi";
import { getTotalFunc } from "../api/estatistics/asoTotalFuncApi";
import {
  exportAllReports,
  exportAsoResume,
  handleExportAll,
  handleExportAso,
} from "../api/tools/export_reports";
import { TotalFunc } from "../model/TotalFunc";

export function Reports() {
  const [risksByCategory, setRisksByCategory] = useState<any[]>([]);
  const [examsByMonth, setExamsByMonth] = useState<any[]>([]);
  const [asoTrend, setASOTrend] = useState<any[]>([]);
  const [fitnessTrend, setFitnessTrend] = useState<any[]>([]);
  const [companiesStats, setCompaniesStats] = useState<TotalFunc | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [companies, exams, aso, risks, fitness] = await Promise.all([
          getTotalFunc(),
          getMonthExams(),
          getASOTrend(),
          getSeverityRisks(),
          getFitness(),
        ]);

        setCompaniesStats(companies);

        setExamsByMonth(
          exams.map((e) => ({
            month: e.month,
            admission: e.admission,
            periodic: e.periodic,
            returnToWork: e.return_to_work,
            dismissal: e.dismissal,
          })),
        );

        setASOTrend(aso);
        setRisksByCategory(
          risks.map((r) => ({
            name: r.categoria,
            count: r.quantidade,
            percentage: r.porcentagem,
          })),
        );
        setFitnessTrend(fitness);
      } catch (err) {
        console.error(err);
        toast.error("Failed to fetch report data.");
      }
    };

    fetchData();
  }, []);

  const totalEmployees = Number(companiesStats?.quantity_funcionario ?? 0);

  const examsCount = Array.isArray(examsByMonth)
    ? examsByMonth.reduce(
        (sum, e) =>
          sum +
          Number(e.admission || 0) +
          Number(e.periodic || 0) +
          Number(e.returnToWork || 0) +
          Number(e.dismissal || 0),
        0,
      )
    : 0;

  const activeRisks = Array.isArray(risksByCategory)
    ? risksByCategory.length
    : 0;

  const asoIssued = Array.isArray(asoTrend) ? asoTrend.length : 0;

  const summaryCards = [
    {
      title: "Total Employees",
      value: totalEmployees.toLocaleString(),
      trend: "up",
      icon: Users,
      color: "bg-blue-500",
    },
    {
      title: "Exams This Month",
      value: examsCount.toLocaleString(),
      trend: "up",
      icon: FileText,
      color: "bg-green-500",
    },
    {
      title: "Active Risks",
      value: activeRisks.toLocaleString(),
      trend: "down",
      icon: AlertTriangle,
      color: "bg-orange-500",
    },
    {
      title: "ASOs Issued",
      value: asoIssued.toLocaleString(),
      trend: "up",
      icon: FileText,
      color: "bg-purple-500",
    },
  ];

  const [isDownloading, setIsDownloading] = useState(false);
  const [isDownloadingAso, setIsDownloadingAso] = useState(false);

  const handleDownloadAso = async () => {
    try {
      setIsDownloadingAso(true);
      await handleExportAso();
      toast.success("Resumo de ASO exported successfully!");
    } catch (err) {
      console.error(err);
      toast.error("Failed to export Resumo de ASO.");
    } finally {
      setIsDownloadingAso(false);
    }
  };

  const handleDownload1 = async () => {
    try {
      setIsDownloading(true);
      await handleExportAll();
      toast.success("Reports exported successfully!");
    } catch (err) {
      console.error(err);
      toast.error("Failed to export reports.");
    } finally {
      setIsDownloading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">
            Reports & Statistics
          </h1>
          <p className="text-gray-600 mt-1">
            Comprehensive analytics and reporting for occupational health data
          </p>
        </div>
        <div className="flex items-center space-x-4">
          <Button
            className="bg-blue-600 hover:bg-blue-700"
            onClick={handleDownload1}
            disabled={isDownloading}
          >
            {isDownloading ? (
              <>
                <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                Exporting...
              </>
            ) : (
              <>
                <Download className="w-4 h-4 mr-2" />
                Export Reports
              </>
            )}
          </Button>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        {summaryCards.map((card, index) => {
          const Icon = card.icon;
          return (
            <Card key={index}>
              <CardContent className="p-6">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-600 mb-1">
                      {card.title}
                    </p>
                    <p className="text-2xl font-semibold text-gray-900">
                      {card.value}
                    </p>
                    <div className="flex items-center mt-2">
                      <TrendingUp
                        className={`w-4 h-4 mr-1 ${
                          card.trend === "up"
                            ? "text-green-500"
                            : "text-red-500"
                        }`}
                      />
                    </div>
                  </div>
                  <div
                    className={`w-12 h-12 rounded-lg ${card.color} flex items-center justify-center`}
                  >
                    <Icon className="w-6 h-6 text-white" />
                  </div>
                </div>
              </CardContent>
            </Card>
          );
        })}
      </div>

      {/* Charts Section */}
      {/* Risks by Category */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <BarChart3 className="w-5 h-5" />
            <span>Riscos por Categoria</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={risksByCategory}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="count" fill="#3b82f6" />
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>

      {/* Exams by Month */}
      <Card className="lg:col-span-2">
        <CardHeader>
          <CardTitle>Tendência de Exames Médicos</CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={examsByMonth}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar
                dataKey="admission"
                stackId="a"
                fill="#3b82f6"
                name="Admission"
              />
              <Bar
                dataKey="periodic"
                stackId="a"
                fill="#10b981"
                name="Periodic"
              />
              <Bar
                dataKey="returnToWork"
                stackId="a"
                fill="#f59e0b"
                name="Return to Work"
              />
              <Bar
                dataKey="dismissal"
                stackId="a"
                fill="#ef4444"
                name="Dismissal"
              />
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>

      {/* ASO Issuance Trend */}
      <Card>
        <CardHeader>
          <CardTitle>Tendência de Emissão de ASO</CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={250}>
            <LineChart data={asoTrend}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line
                type="monotone"
                dataKey="issued"
                stroke="#10b981"
                strokeWidth={2}
                name="Issued"
              />
              <Line
                type="monotone"
                dataKey="pending"
                stroke="#f59e0b"
                strokeWidth={2}
                name="Pending"
              />
            </LineChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>

      {/* Fitness Results Trend */}
      <Card>
        <CardHeader>
          <CardTitle>Resultados de Aptidão para Trabalho</CardTitle>
        </CardHeader>
        <CardContent>
          <ResponsiveContainer width="100%" height={250}>
            <BarChart data={fitnessTrend}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="fit" fill="#10b981" name="Fit" />
              <Bar
                dataKey="fitWithRestrictions"
                fill="#f59e0b"
                name="Fit with Restrictions"
              />
              <Bar dataKey="unfit" fill="#ef4444" name="Unfit" />
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>

      {/* Quick Report Generation */}
      <Card>
        <CardHeader>
          <CardTitle>Geração Rápida de Relatórios</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-1 gap-4">
            <Button
              variant="outline"
              onClick={handleDownloadAso}
              className="h-16"
              disabled={isDownloadingAso}
            >
              <div className="text-center">
                {isDownloadingAso ? (
                  <>
                    <Loader2 className="w-6 h-6 mx-auto mb-2 animate-spin" />
                    Exporting...
                  </>
                ) : (
                  <>
                    <FileText className="w-6 h-6 mx-auto mb-2" />
                    <span>Resumo de ASO</span>
                  </>
                )}
              </div>
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
