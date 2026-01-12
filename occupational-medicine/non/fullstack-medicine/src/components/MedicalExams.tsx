"use client";
import React, { useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/ui/card";
import { Button } from "@/ui/button";
import { Input } from "@/ui/input";
import { Label } from "@/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/ui/select";
import { Textarea } from "@/ui/textarea";
import { Badge } from "@/ui/badge";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/ui/dialog";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/ui/table";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/ui/tabs";
import { Avatar, AvatarFallback } from "@/ui/avatar";
import {
  Plus,
  ClipboardList,
  Calendar,
  Search,
  FileText,
  Clock,
  CheckCircle,
} from "lucide-react";
import { toast } from "sonner";
import { MedicalExam } from "@/model/MedicalExam";
import { Employee } from "@/model/Employee";
import { ClinicStaff } from "@/model/ClinicStaff";
import { createMedicalExam, getMedicalExams } from "@/api/medicalExamApi";
import { getEmployees } from "@/api/employeeApi";
import { getClinicStaff } from "@/api/clinicApi";
import { StatCardProps } from "@/model/helper/StatCardProps";

function StatCard({ icon, label, value, bg }: StatCardProps) {
  return (
    <Card>
      <CardContent className="p-6">
        <div className="flex items-center space-x-4">
          <div
            className={`w-12 h-12 ${bg} rounded-lg flex items-center justify-center`}
          >
            {icon}
          </div>
          <div>
            <p className="text-sm font-medium text-gray-600">{label}</p>
            <p className="text-2xl font-semibold text-gray-900">{value}</p>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}

export function MedicalExams() {
  const [exams, setExams] = useState<MedicalExam[]>([]);
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [doctors, setDoctors] = useState<ClinicStaff[]>([]);

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedTab, setSelectedTab] = useState("all");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [formData, setFormData] = useState({
    employee: "",
    type: "",
    date: "",
    time: "",
    doctor: "",
    observations: "",
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [examData, employeeData, doctorData] = await Promise.all([
          getMedicalExams(),
          getEmployees(),
          getClinicStaff(),
        ]);
        setExams(examData);
        setEmployees(employeeData);
        setDoctors(doctorData.filter((d) => d.role === "doctor"));
      } catch (err: unknown) {
        console.error(err);
        setError("Failed to load data from the API");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const filteredExams = exams.filter((exam) => {
    const matchesSearch =
      exam.employee.toLowerCase().includes(searchTerm.toLowerCase()) ||
      exam.company.toLowerCase().includes(searchTerm.toLowerCase()) ||
      exam.doctor.toLowerCase().includes(searchTerm.toLowerCase());

    const matchesTab = selectedTab === "all" || exam.status === selectedTab;

    return matchesSearch && matchesTab;
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const selectedEmployee = employees.find(
        (emp) => emp.id === formData.employee
      );
      const selectedDoctor = doctors.find((doc) => doc.id === formData.doctor);

      if (!selectedEmployee || !selectedDoctor) {
        toast.error("Please select valid employee and doctor");
        return;
      }

      const newExam: Omit<MedicalExam, "id"> = {
        employee: selectedEmployee.name,
        company: selectedEmployee.company,
        type: formData.type as MedicalExam["type"],
        date: formData.date,
        time: formData.time,
        doctor: selectedDoctor.name,
        status: "scheduled",
        observations: formData.observations,
      };

      const createdExam = await createMedicalExam(newExam);
      setExams((prev) => [...prev, createdExam]);
      toast.success("Medical exam scheduled successfully!");
      setIsDialogOpen(false);
      setFormData({
        employee: "",
        type: "",
        date: "",
        time: "",
        doctor: "",
        observations: "",
      });
    } catch (err: unknown) {
      console.error(err);
      toast.error("Failed to schedule exam. Please try again.");
    }
  };

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const getTypeLabel = (type: string) => {
    switch (type) {
      case "admission":
        return "Admission";
      case "periodic":
        return "Periodic";
      case "return_to_work":
        return "Return to Work";
      case "dismissal":
        return "Dismissal";
      default:
        return type;
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "completed":
        return <CheckCircle className="w-4 h-4 text-green-500" />;
      case "in_progress":
        return <Clock className="w-4 h-4 text-blue-500" />;
      case "scheduled":
        return <Calendar className="w-4 h-4 text-orange-500" />;
      default:
        return <Clock className="w-4 h-4 text-gray-500" />;
    }
  };

  const getResultBadge = (result?: string) => {
    if (!result) return null;

    const colors: Record<string, string> = {
      fit: "bg-green-100 text-green-800",
      unfit: "bg-red-100 text-red-800",
      fit_with_restrictions: "bg-yellow-100 text-yellow-800",
    };

    const labels: Record<string, string> = {
      fit: "Fit for Work",
      unfit: "Unfit for Work",
      fit_with_restrictions: "Fit with Restrictions",
    };

    return <Badge className={colors[result]}>{labels[result]}</Badge>;
  };

  const examCounts = {
    total: exams.length,
    scheduled: exams.filter((e) => e.status === "scheduled").length,
    completed: exams.filter((e) => e.status === "completed").length,
    today: exams.filter(
      (e) => e.date === new Date().toISOString().split("T")[0]
    ).length,
  };

  if (loading) return <p>Loading exams...</p>;
  if (error) return <p className="text-red-600">{error}</p>;

  return (
    <div className="space-y-6">
      {/* HEADER */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">
            Medical Exams
          </h1>
          <p className="text-gray-600 mt-1">
            Schedule and manage occupational health examinations
          </p>
        </div>
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
            <Button className="bg-blue-600 hover:bg-blue-700">
              <Plus className="w-4 h-4 mr-2" />
              Schedule Exam
            </Button>
          </DialogTrigger>
          <DialogContent className="sm:max-w-[500px]">
            <DialogHeader>
              <DialogTitle>Schedule Medical Exam</DialogTitle>
              <DialogDescription>
                Schedule a new occupational health examination for an employee.
              </DialogDescription>
            </DialogHeader>
            <form onSubmit={handleSubmit} className="space-y-4 mt-4">
              {/* Employee */}
              <div className="space-y-2">
                <Label>Employee</Label>
                <Select
                  value={formData.employee}
                  onValueChange={(value) =>
                    handleInputChange("employee", value)
                  }
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Select employee" />
                  </SelectTrigger>
                  <SelectContent>
                    {employees.map((emp) => (
                      <SelectItem key={emp.id} value={emp.id}>
                        {emp.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              {/* Exam Type */}
              <div className="space-y-2">
                <Label>Exam Type</Label>
                <Select
                  value={formData.type}
                  onValueChange={(value) => handleInputChange("type", value)}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Select exam type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="admission">Admission</SelectItem>
                    <SelectItem value="periodic">Periodic</SelectItem>
                    <SelectItem value="return_to_work">
                      Return to Work
                    </SelectItem>
                    <SelectItem value="dismissal">Dismissal</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              {/* Date/Time */}
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>Date</Label>
                  <Input
                    type="date"
                    value={formData.date}
                    onChange={(e) => handleInputChange("date", e.target.value)}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label>Time</Label>
                  <Input
                    type="time"
                    value={formData.time}
                    onChange={(e) => handleInputChange("time", e.target.value)}
                    required
                  />
                </div>
              </div>

              {/* Doctor */}
              <div className="space-y-2">
                <Label>Doctor</Label>
                <Select
                  value={formData.doctor}
                  onValueChange={(value) => handleInputChange("doctor", value)}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Select doctor" />
                  </SelectTrigger>
                  <SelectContent>
                    {doctors.map((doc) => (
                      <SelectItem key={doc.id} value={doc.id}>
                        {doc.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              {/* Observations */}
              <div className="space-y-2">
                <Label>Observations (Optional)</Label>
                <Textarea
                  placeholder="Enter any special observations or instructions"
                  value={formData.observations}
                  onChange={(e) =>
                    handleInputChange("observations", e.target.value)
                  }
                />
              </div>

              <div className="flex justify-end space-x-2 pt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => setIsDialogOpen(false)}
                >
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

      {/* STATS */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <StatCard
          icon={<ClipboardList className="w-6 h-6 text-blue-600" />}
          label="Total Exams"
          value={examCounts.total}
          bg="bg-blue-100"
        />
        <StatCard
          icon={<Calendar className="w-6 h-6 text-orange-600" />}
          label="Scheduled"
          value={examCounts.scheduled}
          bg="bg-orange-100"
        />
        <StatCard
          icon={<CheckCircle className="w-6 h-6 text-green-600" />}
          label="Completed"
          value={examCounts.completed}
          bg="bg-green-100"
        />
        <StatCard
          icon={<Clock className="w-6 h-6 text-purple-600" />}
          label="Today"
          value={examCounts.today}
          bg="bg-purple-100"
        />
      </div>

      {/* Search */}
      <Card>
        <CardContent className="p-6">
          <div className="flex items-center space-x-4">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <Input
                placeholder="Search exams by employee, company, or doctor..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* TABLE */}
      <Card>
        <CardHeader>
          <CardTitle>Medical Examinations</CardTitle>
        </CardHeader>
        <CardContent>
          <Tabs
            value={selectedTab}
            onValueChange={setSelectedTab}
            className="space-y-4"
          >
            <TabsList>
              <TabsTrigger value="all">All Exams</TabsTrigger>
              <TabsTrigger value="scheduled">Scheduled</TabsTrigger>
              <TabsTrigger value="completed">Completed</TabsTrigger>
              <TabsTrigger value="in_progress">In Progress</TabsTrigger>
            </TabsList>

            <TabsContent value={selectedTab}>
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
                      <TableHead className="text-right">Actions</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredExams.map((exam) => (
                      <TableRow key={exam.id}>
                        <TableCell>
                          <div className="flex items-center space-x-3">
                            <Avatar>
                              <AvatarFallback className="bg-blue-100 text-blue-700">
                                {exam.employee
                                  .split(" ")
                                  .map((n) => n[0])
                                  .join("")
                                  .substring(0, 2)}
                              </AvatarFallback>
                            </Avatar>
                            <div>
                              <p className="font-medium text-gray-900">
                                {exam.employee}
                              </p>
                              <p className="text-sm text-gray-500">
                                {exam.company}
                              </p>
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
                              {new Date(exam.date).toLocaleDateString("en-US", {
                                month: "short",
                                day: "numeric",
                                year: "numeric",
                              })}
                            </p>
                            <p className="text-sm text-gray-500">{exam.time}</p>
                          </div>
                        </TableCell>
                        <TableCell>{exam.doctor}</TableCell>
                        <TableCell>
                          <div className="flex items-center space-x-2">
                            {getStatusIcon(exam.status)}
                            <span className="capitalize">
                              {exam.status.replace("_", " ")}
                            </span>
                          </div>
                        </TableCell>
                        <TableCell>{getResultBadge(exam.result)}</TableCell>
                        <TableCell className="text-right">
                          <Button variant="ghost" size="sm">
                            <FileText className="w-4 h-4" />
                          </Button>
                        </TableCell>
                      </TableRow>
                    ))}
                    {filteredExams.length === 0 && (
                      <TableRow>
                        <TableCell
                          colSpan={7}
                          className="text-center text-gray-500 py-6"
                        >
                          No exams found.
                        </TableCell>
                      </TableRow>
                    )}
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
