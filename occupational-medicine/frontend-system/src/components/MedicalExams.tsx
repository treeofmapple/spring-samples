import React, { useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";
import { Textarea } from "./ui/textarea";
import { Badge } from "./ui/badge";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "./ui/dialog";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "./ui/table";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import { Avatar, AvatarFallback } from "./ui/avatar";
import {
  Plus,
  ClipboardList,
  Calendar,
  User,
  Building2,
  Search,
  FileText,
  Clock,
  CheckCircle,
} from "lucide-react";
import { toast } from "sonner";
import pt from "../i18n/pt-BR";
import { MedicalExam } from "../model/MedicalExam";
import { fetchExames } from "../api/dashboard/dashboardEndpoints";
import { getMedicalExams } from "../api/medicalExamApi";
import { getEmployeeById, getEmployees } from "../api/employeeApi";

export function MedicalExams() {
  const [exams] = useState<MedicalExam[]>([
    {
      id: '1',
      employee: 'João Silva Santos',
      company: 'TechCorp Solutions',
      type: 'periodic',
      date: '2024-01-15',
      time: '09:00',
      doctor: 'Dr. Maria Silva',
      status: 'completed',
      result: 'fit',
      observations: 'Employee in good health condition. All parameters within normal limits.',
      nextExamDate: '2025-01-15'
    },
    {
      id: '2',
      employee: 'Maria Oliveira Costa',
      company: 'InnovaCorp Industries',
      type: 'admission',
      date: '2024-01-16',
      time: '10:30',
      doctor: 'Dr. João Santos',
      status: 'completed',
      result: 'fit',
      observations: 'Admission exam completed successfully. No restrictions identified.'
    },
    {
      id: '3',
      employee: 'Pedro Henrique Lima',
      company: 'BuildCorp Construction',
      type: 'return_to_work',
      date: '2024-01-17',
      time: '14:00',
      doctor: 'Dr. Maria Silva',
      status: 'scheduled'
    },
    {
      id: '4',
      employee: 'Ana Paula Ferreira',
      company: 'TechCorp Solutions',
      type: 'periodic',
      date: '2024-01-18',
      time: '15:30',
      doctor: 'Dr. João Santos',
      status: 'scheduled'
    },
    {
      id: '5',
      employee: 'Carlos Roberto Souza',
      company: 'GreenCorp Sustentável',
      type: 'dismissal',
      date: '2024-01-12',
      time: '11:00',
      doctor: 'Dr. Maria Silva',
      status: 'completed',
      result: 'fit',
      observations: 'Dismissal exam completed. Employee cleared for exit.'
    }
  ]);

  const employees = [
    'João Silva Santos', 'Maria Oliveira Costa', 'Pedro Henrique Lima',
    'Ana Paula Ferreira', 'Carlos Roberto Souza'
  ];

  const doctors = ['Dr. Maria Silva', 'Dr. João Santos'];

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedTab, setSelectedTab] = useState('all');
  const [formData, setFormData] = useState({
    employee: '',
    type: '',
    date: '',
    time: '',
    doctor: '',
    observations: ''
  });

  const filteredExams = exams.filter(exam => {
    const matchesSearch = exam.employee.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         exam.company.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         exam.doctor.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesTab = selectedTab === 'all' || exam.status === selectedTab;

    return matchesSearch && matchesTab;
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    toast.success('Medical exam scheduled successfully!');
    setIsDialogOpen(false);
    setFormData({
      employee: '',
      type: '',
      date: '',
      time: '',
      doctor: '',
      observations: ''
    });
  };

  const handleInputChange = (field: string, value: string) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const getTypeLabel = (type: string) => {
    switch (type) {
      case 'admission': return 'Admission';
      case 'periodic': return 'Periodic';
      case 'return_to_work': return 'Return to Work';
      case 'dismissal': return 'Dismissal';
      default: return type;
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'completed':
        return <CheckCircle className="w-4 h-4 text-green-500" />;
      case 'in_progress':
        return <Clock className="w-4 h-4 text-blue-500" />;
      case 'scheduled':
        return <Calendar className="w-4 h-4 text-orange-500" />;
      default:
        return <Clock className="w-4 h-4 text-gray-500" />;
    }
  };

  const getResultBadge = (result: string | undefined) => {
    if (!result) return null;

    const colors = {
      fit: 'bg-green-100 text-green-800',
      unfit: 'bg-red-100 text-red-800',
      fit_with_restrictions: 'bg-yellow-100 text-yellow-800'
    };

    const labels = {
      fit: 'Fit for Work',
      unfit: 'Unfit for Work',
      fit_with_restrictions: 'Fit with Restrictions'
    };

    return (
      <Badge className={colors[result as keyof typeof colors]}>
        {labels[result as keyof typeof labels]}
      </Badge>
    );
  };

  const examCounts = {
    total: exams.length,
    scheduled: exams.filter(e => e.status === 'scheduled').length,
    completed: exams.filter(e => e.status === 'completed').length,
    today: exams.filter(e => e.date === new Date().toISOString().split('T')[0]).length
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">{pt['nav.medicalExams']}</h1>
          <p className="text-gray-600 mt-1">{pt['exams.description'] || 'Agende e gerencie exames ocupacionais'}</p>
        </div>
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
          </DialogTrigger>
          <DialogContent className="sm:max-w-[500px]">
            <DialogHeader>
              <DialogTitle>{pt['exams.scheduleTitle'] || 'Agendar Exame Médico'}</DialogTitle>
              <DialogDescription>
                {pt['exams.scheduleDescription'] || 'Agende um novo exame de saúde ocupacional para um funcionário.'}
              </DialogDescription>
            </DialogHeader>
            <form onSubmit={handleSubmit} className="space-y-4 mt-4">
              <div className="space-y-2">
                <Label htmlFor="exam-employee">Employee</Label>
                <Select value={formData.employee} onValueChange={(value: string) => handleInputChange('employee', value)}>
                  <SelectTrigger>
                    <SelectValue placeholder={pt['clients.selectCompanyPlaceholder'] || 'Selecione o funcionário'} />
                  </SelectTrigger>
                  <SelectContent>
                    {employees.map((employee) => (
                      <SelectItem key={employee} value={employee}>
                        {employee}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="exam-type">Exam Type</Label>
                <Select value={formData.type} onValueChange={(value: string) => handleInputChange('type', value)}>
                  <SelectTrigger>
                    <SelectValue placeholder={pt['exams.selectType'] || 'Selecione o tipo de exame'} />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="admission">Admission</SelectItem>
                    <SelectItem value="periodic">Periodic</SelectItem>
                    <SelectItem value="return_to_work">Return to Work</SelectItem>
                    <SelectItem value="dismissal">Dismissal</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="exam-date">Date</Label>
                  <Input
                    id="exam-date"
                    type="date"
                    value={formData.date}
                    onChange={(e) => handleInputChange('date', e.target.value)}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="exam-time">Time</Label>
                  <Input
                    id="exam-time"
                    type="time"
                    value={formData.time}
                    onChange={(e) => handleInputChange('time', e.target.value)}
                    required
                  />
                </div>
              </div>
              <div className="space-y-2">
                <Label htmlFor="exam-doctor">Doctor</Label>
                <Select value={formData.doctor} onValueChange={(value: string) => handleInputChange('doctor', value)}>
                  <SelectTrigger>
                    <SelectValue placeholder={pt['clinic.fields.name'] || 'Selecione o médico'} />
                  </SelectTrigger>
                  <SelectContent>
                    {doctors.map((doctor) => (
                      <SelectItem key={doctor} value={doctor}>
                        {doctor}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="exam-observations">Observations (Optional)</Label>
                <Textarea
                  id="exam-observations"
                  placeholder={pt['exams.observationsPlaceholder'] || 'Insira observações ou instruções especiais'}
                  value={formData.observations}
                  onChange={(e) => handleInputChange('observations', e.target.value)}
                />
              </div>
              <div className="flex justify-end space-x-2 pt-4">
                <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
                  Cancel
                </Button>
                <Button type="submit" className="bg-blue-600 hover:bg-blue-700">
                  Schedule Exam
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                <ClipboardList className="w-6 h-6 text-blue-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">{pt['exams.description']}</p>
                <p className="text-2xl font-semibold text-gray-900">{examCounts.total}</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
                <Calendar className="w-6 h-6 text-orange-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Scheduled</p>
                <p className="text-2xl font-semibold text-gray-900">{examCounts.scheduled}</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                <CheckCircle className="w-6 h-6 text-green-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Completed</p>
                <p className="text-2xl font-semibold text-gray-900">{examCounts.completed}</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                <Clock className="w-6 h-6 text-purple-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Today</p>
                <p className="text-2xl font-semibold text-gray-900">{examCounts.today}</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Exams Table with Tabs */}
      <Card>
        <CardHeader>
          <CardTitle>Medical Examinations</CardTitle>
        </CardHeader>
        <CardContent>
          <Tabs value={selectedTab} onValueChange={setSelectedTab} className="space-y-4">

            <TabsContent value={selectedTab} className="space-y-4">
              <div className="overflow-x-auto">
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Employee</TableHead>
                      <TableHead>Type</TableHead>
                      <TableHead>Date & Time</TableHead>
                      <TableHead>Doctor</TableHead>
                      <TableHead>Status</TableHead>
                      <TableHead>Result</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredExams.map((exam) => (
                      <TableRow key={exam.id}>
                        <TableCell>
                          <div className="flex items-center space-x-3">
                            <Avatar>
                              <AvatarFallback className="bg-blue-100 text-blue-700">
                                {exam.employee.split(' ').map(n => n[0]).join('').substring(0, 2)}
                              </AvatarFallback>
                            </Avatar>
                            <div>
                              <p className="font-medium text-gray-900">{exam.employee}</p>
                              <p className="text-sm text-gray-500">{exam.company}</p>
                            </div>
                          </div>
                        </TableCell>
                        <TableCell>
                          <Badge variant="outline">
                            {getTypeLabel(exam.type)}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <div>
                            <p className="font-medium">
                              {new Date(exam.date).toLocaleDateString('en-US', {
                                month: 'short',
                                day: 'numeric',
                                year: 'numeric'
                              })}
                            </p>
                            <p className="text-sm text-gray-500">{exam.time}</p>
                          </div>
                        </TableCell>
                        <TableCell>{exam.doctor}</TableCell>
                        <TableCell>
                          <div className="flex items-center space-x-2">
                            {getStatusIcon(exam.status)}
                            <span className="capitalize">{exam.status.replace('_', ' ')}</span>
                          </div>
                        </TableCell>
                        <TableCell>
                          {getResultBadge(exam.result)}
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </TabsContent>
          </Tabs>
        </CardContent>
      </Card>
    </div>
  );
}
