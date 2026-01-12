import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from './ui/card';
import { Badge } from './ui/badge';
import { useAuth } from '../App';
import pt from '../i18n/pt-BR';
import {
  Building2,
  Users,
  ClipboardList,
  AlertTriangle,
  Calendar,
} from 'lucide-react';
import { fetchPainel, fetchUpcoming } from '../api/dashboard/dashboardEndpoints';

export function Dashboard() {
  const { user } = useAuth();

  const [painelData, setPainelData] = useState<{
    total_companies: string;
    pending_exams: string;
    high_risk_cases: string;
  } | null>(null);

  const [upcomingExams, setUpcomingExams] = useState<
    {
      nome: string;
      nome_empresa: string;
      nome_exame: string;
      horario_exame: string;
      dia_exame: string;
    }[]
  >([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function loadData() {
      try {
        const [painel, upcoming] = await Promise.all([
          fetchPainel(),
          fetchUpcoming(),
        ]);
        setPainelData(painel);
        setUpcomingExams(upcoming);
      } catch (err) {
        console.error(err);
        setError('Failed to load dashboard data.');
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64 text-gray-500">
        Loading dashboard...
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center h-64 text-red-500">
        {error}
      </div>
    );
  }

  if (!painelData) return null;

  const statsCards = [
    {
      title: 'Total Companies',
      value: painelData.total_companies,
      change: '',
      icon: Building2,
      color: 'bg-blue-500',
    },
    {
      title: 'Pending Exams',
      value: painelData.pending_exams,
      change: '',
      icon: ClipboardList,
      color: 'bg-orange-500',
    },
    {
      title: 'High Risk Cases',
      value: painelData.high_risk_cases,
      change: '',
      icon: AlertTriangle,
      color: 'bg-red-500',
    },
  ];

  return (
    <div className="space-y-6">
      {/* Welcome Section */}
      <div className="bg-gradient-to-r from-blue-500 to-blue-600 rounded-lg p-6 text-white">
        <h1 className="text-2xl font-semibold mb-2">
          {pt['dashboard.welcome'].replace('{name}', user?.name || '')}
        </h1>
        <p className="text-blue-100">
          {pt['dashboard.subtitle']}
        </p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-3 gap-6">
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
                    <p className="text-xs text-gray-500 mt-1">
                      {stat.change}
                    </p>
                  </div>
                  <div className={`w-12 h-12 rounded-lg ${stat.color} flex items-center justify-center`}>
                    <Icon className="w-6 h-6 text-white" />
                  </div>
                </div>
              </CardContent>
            </Card>
          );
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-1 gap-6">

        {/* Upcoming Exams */}
        <Card>
          <CardHeader className="pb-4">
            <CardTitle className="flex items-center space-x-2">
              <Calendar className="w-5 h-5" />
              <span>Upcoming Exams</span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {upcomingExams.map((exam) => (
                <div key={exam.nome_exame} className="flex items-center justify-between p-3 border border-gray-200 rounded-md">
                  <div className="flex-1">
                    <p className="font-medium text-gray-900 text-sm">
                      {exam.nome}
                    </p>
                    <p className="text-xs text-gray-500">
                      {exam.nome_empresa}
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-medium text-gray-900">
                      {exam.horario_exame || "--:--"}
                    </p>
                    <p className="text-xs text-gray-500">
                      {exam.dia_exame}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
