"use client";
import React, { useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/ui/card';
import { Button } from '@/ui/button';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/ui/select';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line } from 'recharts';
import { BarChart3, Download, Calendar, AlertTriangle, FileText } from 'lucide-react';
import { toast } from 'sonner';

export function Reports() {
  const [selectedPeriod, setSelectedPeriod] = useState('monthly');
  
  const risksByCategory = [
    { name: 'Physical', count: 8, percentage: 32 },
    { name: 'Chemical', count: 6, percentage: 24 },
    { name: 'Ergonomic', count: 5, percentage: 20 },
    { name: 'Accident', count: 4, percentage: 16 },
    { name: 'Biological', count: 2, percentage: 8 }
  ];

  const examsByMonth = [
    { month: 'Jan', admission: 12, periodic: 18, returnToWork: 5, dismissal: 8 },
    { month: 'Feb', admission: 15, periodic: 22, returnToWork: 7, dismissal: 6 },
    { month: 'Mar', admission: 18, periodic: 25, returnToWork: 9, dismissal: 12 },
    { month: 'Apr', admission: 14, periodic: 28, returnToWork: 6, dismissal: 9 },
    { month: 'May', admission: 20, periodic: 30, returnToWork: 8, dismissal: 11 },
    { month: 'Jun', admission: 16, periodic: 26, returnToWork: 4, dismissal: 7 }
  ];

  const asoTrend = [
    { month: 'Jan', issued: 32, pending: 8 },
    { month: 'Feb', issued: 38, pending: 6 },
    { month: 'Mar', issued: 45, pending: 12 },
    { month: 'Apr', issued: 42, pending: 9 },
    { month: 'May', issued: 51, pending: 7 },
    { month: 'Jun', issued: 38, pending: 5 }
  ];

  const fitnessTrend = [
    { month: 'Jan', fit: 28, fitWithRestrictions: 8, unfit: 2 },
    { month: 'Feb', fit: 32, fitWithRestrictions: 6, unfit: 1 },
    { month: 'Mar', fit: 35, fitWithRestrictions: 9, unfit: 3 },
    { month: 'Apr', fit: 30, fitWithRestrictions: 7, unfit: 2 },
    { month: 'May', fit: 38, fitWithRestrictions: 8, unfit: 1 },
    { month: 'Jun', fit: 29, fitWithRestrictions: 5, unfit: 2 }
  ];

  const generateReport = (type: string) => {
    toast.success(`${type} report generated successfully!`);
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">Reports & Statistics</h1>
          <p className="text-gray-600 mt-1">Comprehensive analytics and reporting for occupational health data</p>
        </div>
        <div className="flex items-center space-x-4">
          <Select value={selectedPeriod} onValueChange={setSelectedPeriod}>
            <SelectTrigger className="w-32">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="daily">Daily</SelectItem>
              <SelectItem value="weekly">Weekly</SelectItem>
              <SelectItem value="monthly">Monthly</SelectItem>
              <SelectItem value="yearly">Yearly</SelectItem>
            </SelectContent>
          </Select>
          <Button className="bg-blue-600 hover:bg-blue-700">
            <Download className="w-4 h-4 mr-2" />
            Export Reports
          </Button>
        </div>
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Risks by Category */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <BarChart3 className="w-5 h-5" />
              <span>Occupational Risks by Category</span>
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
            <CardTitle>Medical Exams Trend</CardTitle>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={examsByMonth}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="admission" stackId="a" fill="#3b82f6" name="Admission" />
                <Bar dataKey="periodic" stackId="a" fill="#10b981" name="Periodic" />
                <Bar dataKey="returnToWork" stackId="a" fill="#f59e0b" name="Return to Work" />
                <Bar dataKey="dismissal" stackId="a" fill="#ef4444" name="Dismissal" />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* ASO Issuance Trend */}
        <Card>
          <CardHeader>
            <CardTitle>ASO Issuance Trend</CardTitle>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={asoTrend}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="issued" stroke="#10b981" strokeWidth={2} name="Issued" />
                <Line type="monotone" dataKey="pending" stroke="#f59e0b" strokeWidth={2} name="Pending" />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Fitness Results Trend */}
        <Card>
          <CardHeader>
            <CardTitle>Fitness for Work Results</CardTitle>
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
                <Bar dataKey="fitWithRestrictions" fill="#f59e0b" name="Fit with Restrictions" />
                <Bar dataKey="unfit" fill="#ef4444" name="Unfit" />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>

      {/* Quick Report Generation */}
      <Card>
        <CardHeader>
          <CardTitle>Quick Report Generation</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Button variant="outline" onClick={() => generateReport('Monthly Compliance')} className="h-16">
              <div className="text-center">
                <Calendar className="w-6 h-6 mx-auto mb-2" />
                <span>Monthly Compliance Report</span>
              </div>
            </Button>
            <Button variant="outline" onClick={() => generateReport('Risk Assessment')} className="h-16">
              <div className="text-center">
                <AlertTriangle className="w-6 h-6 mx-auto mb-2" />
                <span>Risk Assessment Report</span>
              </div>
            </Button>
            <Button variant="outline" onClick={() => generateReport('ASO Summary')} className="h-16">
              <div className="text-center">
                <FileText className="w-6 h-6 mx-auto mb-2" />
                <span>ASO Summary Report</span>
              </div>
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}