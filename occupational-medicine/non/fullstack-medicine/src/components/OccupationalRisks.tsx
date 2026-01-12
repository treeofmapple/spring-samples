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
  AlertTriangle,
  Users,
  Search,
  Edit,
  Trash2,
  Shield,
  Zap,
  Droplets,
  Volume2,
} from "lucide-react";
import { toast } from "sonner";
import { OccupationalRisk } from "@/model/RisksByCategory";
import { RiskAssignment } from "@/model/RiskAssignment";
import { getOccupationalRisks } from "@/api/risksByCategoryApi";
import { getRiskAssignments } from "@/api/riskAssignmentApi";
import { getEmployees } from "@/api/employeeApi";
import { getCompanies } from "@/api/companyApi";

export function OccupationalRisks() {
  const [risks, setRisks] = useState<OccupationalRisk[]>([]);
  const [assignments, setAssignments] = useState<RiskAssignment[]>([]);
  const [employees, setEmployees] = useState<string[]>([]);
  const [companies, setCompanies] = useState<string[]>([]);
  const [isRiskDialogOpen, setIsRiskDialogOpen] = useState(false);
  const [isAssignDialogOpen, setIsAssignDialogOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedTab, setSelectedTab] = useState("risks");

  const [riskFormData, setRiskFormData] = useState({
    name: "",
    category: "",
    severity: "",
    description: "",
    preventiveMeasures: "",
  });

  const [assignFormData, setAssignFormData] = useState({
    employee: "",
    risk: "",
    exposureLevel: "",
    protectionMeasures: "",
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [risksData, assignmentsData, employeesData, companiesData] =
          await Promise.all([
            getOccupationalRisks(),
            getRiskAssignments(),
            getEmployees(),
            getCompanies(),
          ]);

        setRisks(risksData);
        setAssignments(assignmentsData);
        setEmployees(employeesData.map((e) => e.name));
        setCompanies(companiesData.map((c) => c.name));
      } catch (error) {
        console.error(error);
        toast.error("Failed to fetch data from server.");
      }
    };

    fetchData();
  }, []);

  const filteredRisks = risks.filter(
    (risk) =>
      risk.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      risk.category.toLowerCase().includes(searchTerm.toLowerCase()) ||
      risk.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredAssignments = assignments.filter(
    (assignment) =>
      assignment.employee.toLowerCase().includes(searchTerm.toLowerCase()) ||
      assignment.company.toLowerCase().includes(searchTerm.toLowerCase()) ||
      assignment.riskName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleRiskSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    toast.success("Occupational risk registered successfully!");
    setIsRiskDialogOpen(false);
    setRiskFormData({
      name: "",
      category: "",
      severity: "",
      description: "",
      preventiveMeasures: "",
    });
  };

  const handleAssignSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    toast.success("Risk assigned to employee successfully!");
    setIsAssignDialogOpen(false);
    setAssignFormData({
      employee: "",
      risk: "",
      exposureLevel: "",
      protectionMeasures: "",
    });
  };

  const getCategoryIcon = (category: string) => {
    switch (category) {
      case "physical":
        return <Volume2 className="w-4 h-4" />;
      case "chemical":
        return <Droplets className="w-4 h-4" />;
      case "biological":
        return <Shield className="w-4 h-4" />;
      case "ergonomic":
        return <Users className="w-4 h-4" />;
      case "accident":
        return <Zap className="w-4 h-4" />;
      default:
        return <AlertTriangle className="w-4 h-4" />;
    }
  };

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case "critical":
        return "bg-red-100 text-red-800 border-red-200";
      case "high":
        return "bg-orange-100 text-orange-800 border-orange-200";
      case "medium":
        return "bg-yellow-100 text-yellow-800 border-yellow-200";
      case "low":
        return "bg-green-100 text-green-800 border-green-200";
      default:
        return "bg-gray-100 text-gray-800 border-gray-200";
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "active":
        return "bg-red-100 text-red-800";
      case "controlled":
        return "bg-yellow-100 text-yellow-800";
      case "eliminated":
        return "bg-green-100 text-green-800";
      case "monitored":
        return "bg-blue-100 text-blue-800";
      case "resolved":
        return "bg-green-100 text-green-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const riskCounts = {
    total: risks.length,
    active: risks.filter((r) => r.status === "active").length,
    controlled: risks.filter((r) => r.status === "controlled").length,
    critical: risks.filter((r) => r.severity === "critical").length,
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">
            Occupational Risks
          </h1>
          <p className="text-gray-600 mt-1">
            Manage and monitor workplace health and safety risks
          </p>
        </div>
        <div className="flex space-x-2">
          <Dialog
            open={isAssignDialogOpen}
            onOpenChange={setIsAssignDialogOpen}
          >
            <DialogTrigger asChild>
              <Button variant="outline">
                <Users className="w-4 h-4 mr-2" />
                Assign Risk
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[500px]">
              <DialogHeader>
                <DialogTitle>Assign Risk to Employee</DialogTitle>
                <DialogDescription>
                  Associate an occupational risk with a specific employee.
                </DialogDescription>
              </DialogHeader>
              <form onSubmit={handleAssignSubmit} className="space-y-4 mt-4">
                <div className="space-y-2">
                  <Label htmlFor="assign-employee">Employee</Label>
                  <Select
                    value={assignFormData.employee}
                    onValueChange={(value: string) =>
                      setAssignFormData((prev) => ({
                        ...prev,
                        employee: value,
                      }))
                    }
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Select employee" />
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
                  <Label htmlFor="assign-risk">Risk</Label>
                  <Select
                    value={assignFormData.risk}
                    onValueChange={(value: string) =>
                      setAssignFormData((prev) => ({ ...prev, risk: value }))
                    }
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Select risk" />
                    </SelectTrigger>
                    <SelectContent>
                      {risks.map((risk) => (
                        <SelectItem key={risk.id} value={risk.name}>
                          {risk.name} ({risk.category})
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="exposure-level">Exposure Level</Label>
                  <Select
                    value={assignFormData.exposureLevel}
                    onValueChange={(value: string) =>
                      setAssignFormData((prev) => ({
                        ...prev,
                        exposureLevel: value,
                      }))
                    }
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Select exposure level" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="low">Low</SelectItem>
                      <SelectItem value="medium">Medium</SelectItem>
                      <SelectItem value="high">High</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="protection-measures">
                    Protection Measures
                  </Label>
                  <Textarea
                    id="protection-measures"
                    placeholder="Describe specific protection measures for this employee"
                    value={assignFormData.protectionMeasures}
                    onChange={(e) =>
                      setAssignFormData((prev) => ({
                        ...prev,
                        protectionMeasures: e.target.value,
                      }))
                    }
                    required
                  />
                </div>
                <div className="flex justify-end space-x-2 pt-4">
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => setIsAssignDialogOpen(false)}
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    className="bg-blue-600 hover:bg-blue-700"
                  >
                    Assign Risk
                  </Button>
                </div>
              </form>
            </DialogContent>
          </Dialog>

          <Dialog open={isRiskDialogOpen} onOpenChange={setIsRiskDialogOpen}>
            <DialogTrigger asChild>
              <Button className="bg-blue-600 hover:bg-blue-700">
                <Plus className="w-4 h-4 mr-2" />
                New Risk
              </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[600px]">
              <DialogHeader>
                <DialogTitle>Register New Occupational Risk</DialogTitle>
                <DialogDescription>
                  Add a new occupational risk to the system for monitoring and
                  management.
                </DialogDescription>
              </DialogHeader>
              <form onSubmit={handleRiskSubmit} className="space-y-4 mt-4">
                <div className="space-y-2">
                  <Label htmlFor="risk-name">Risk Name</Label>
                  <Input
                    id="risk-name"
                    placeholder="Enter risk name"
                    value={riskFormData.name}
                    onChange={(e) =>
                      setRiskFormData((prev) => ({
                        ...prev,
                        name: e.target.value,
                      }))
                    }
                    required
                  />
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="risk-category">Category</Label>
                    <Select
                      value={riskFormData.category}
                      onValueChange={(value: string) =>
                        setRiskFormData((prev) => ({
                          ...prev,
                          category: value,
                        }))
                      }
                    >
                      <SelectTrigger>
                        <SelectValue placeholder="Select category" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="physical">Physical</SelectItem>
                        <SelectItem value="chemical">Chemical</SelectItem>
                        <SelectItem value="biological">Biological</SelectItem>
                        <SelectItem value="ergonomic">Ergonomic</SelectItem>
                        <SelectItem value="accident">Accident</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="risk-severity">Severity</Label>
                    <Select
                      value={riskFormData.severity}
                      onValueChange={(value: string) =>
                        setRiskFormData((prev) => ({
                          ...prev,
                          severity: value,
                        }))
                      }
                    >
                      <SelectTrigger>
                        <SelectValue placeholder="Select severity" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="low">Low</SelectItem>
                        <SelectItem value="medium">Medium</SelectItem>
                        <SelectItem value="high">High</SelectItem>
                        <SelectItem value="critical">Critical</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="risk-description">Description</Label>
                  <Textarea
                    id="risk-description"
                    placeholder="Describe the occupational risk in detail"
                    value={riskFormData.description}
                    onChange={(e) =>
                      setRiskFormData((prev) => ({
                        ...prev,
                        description: e.target.value,
                      }))
                    }
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="preventive-measures">
                    Preventive Measures
                  </Label>
                  <Textarea
                    id="preventive-measures"
                    placeholder="Describe preventive measures and controls"
                    value={riskFormData.preventiveMeasures}
                    onChange={(e) =>
                      setRiskFormData((prev) => ({
                        ...prev,
                        preventiveMeasures: e.target.value,
                      }))
                    }
                    required
                  />
                </div>
                <div className="flex justify-end space-x-2 pt-4">
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => setIsRiskDialogOpen(false)}
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    className="bg-blue-600 hover:bg-blue-700"
                  >
                    Register Risk
                  </Button>
                </div>
              </form>
            </DialogContent>
          </Dialog>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                <AlertTriangle className="w-6 h-6 text-blue-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Total Risks</p>
                <p className="text-2xl font-semibold text-gray-900">
                  {riskCounts.total}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-red-100 rounded-lg flex items-center justify-center">
                <AlertTriangle className="w-6 h-6 text-red-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">
                  Active Risks
                </p>
                <p className="text-2xl font-semibold text-gray-900">
                  {riskCounts.active}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center">
                <Shield className="w-6 h-6 text-yellow-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Controlled</p>
                <p className="text-2xl font-semibold text-gray-900">
                  {riskCounts.controlled}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
                <Zap className="w-6 h-6 text-orange-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Critical</p>
                <p className="text-2xl font-semibold text-gray-900">
                  {riskCounts.critical}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Search */}
      <Card>
        <CardContent className="p-6">
          <div className="flex items-center space-x-4">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <Input
                placeholder="Search risks or assignments..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Content Tabs */}
      <Card>
        <CardHeader>
          <CardTitle>Risk Management</CardTitle>
        </CardHeader>
        <CardContent>
          <Tabs
            value={selectedTab}
            onValueChange={setSelectedTab}
            className="space-y-4"
          >
            <TabsList>
              <TabsTrigger value="risks">Risk Registry</TabsTrigger>
              <TabsTrigger value="assignments">
                Employee Assignments
              </TabsTrigger>
            </TabsList>

            <TabsContent value="risks" className="space-y-4">
              <div className="overflow-x-auto">
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Risk</TableHead>
                      <TableHead>Category</TableHead>
                      <TableHead>Severity</TableHead>
                      <TableHead>Affected Employees</TableHead>
                      <TableHead>Status</TableHead>
                      <TableHead>Next Assessment</TableHead>
                      <TableHead className="text-right">Actions</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredRisks.map((risk) => (
                      <TableRow key={risk.id}>
                        <TableCell>
                          <div>
                            <p className="font-medium text-gray-900">
                              {risk.name}
                            </p>
                            <p className="text-sm text-gray-500 line-clamp-2">
                              {risk.description}
                            </p>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center space-x-2">
                            {getCategoryIcon(risk.category)}
                            <span className="capitalize">{risk.category}</span>
                          </div>
                        </TableCell>
                        <TableCell>
                          <Badge className={getSeverityColor(risk.severity)}>
                            {risk.severity.toUpperCase()}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center space-x-1">
                            <Users className="w-4 h-4 text-gray-500" />
                            <span>{risk.affectedEmployees.length}</span>
                          </div>
                        </TableCell>
                        <TableCell>
                          <Badge className={getStatusColor(risk.status)}>
                            {risk.status}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          {new Date(risk.nextAssessment).toLocaleDateString(
                            "en-US",
                            {
                              year: "numeric",
                              month: "short",
                              day: "numeric",
                            }
                          )}
                        </TableCell>
                        <TableCell className="text-right">
                          <div className="flex items-center justify-end space-x-2">
                            <Button variant="ghost" size="sm">
                              <Edit className="w-4 h-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              className="text-red-600 hover:text-red-700"
                            >
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            </TabsContent>

            <TabsContent value="assignments" className="space-y-4">
              <div className="overflow-x-auto">
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Employee</TableHead>
                      <TableHead>Risk</TableHead>
                      <TableHead>Exposure Level</TableHead>
                      <TableHead>Assignment Date</TableHead>
                      <TableHead>Protection Measures</TableHead>
                      <TableHead>Status</TableHead>
                      <TableHead className="text-right">Actions</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredAssignments.map((assignment) => (
                      <TableRow key={assignment.id}>
                        <TableCell>
                          <div className="flex items-center space-x-3">
                            <Avatar>
                              <AvatarFallback className="bg-blue-100 text-blue-700">
                                {assignment.employee
                                  .split(" ")
                                  .map((n) => n[0])
                                  .join("")
                                  .substring(0, 2)}
                              </AvatarFallback>
                            </Avatar>
                            <div>
                              <p className="font-medium text-gray-900">
                                {assignment.employee}
                              </p>
                              <p className="text-sm text-gray-500">
                                {assignment.company}
                              </p>
                            </div>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center space-x-2">
                            {getCategoryIcon(assignment.riskCategory)}
                            <span>{assignment.riskName}</span>
                          </div>
                        </TableCell>
                        <TableCell>
                          <Badge
                            className={getSeverityColor(
                              assignment.exposureLevel
                            )}
                          >
                            {assignment.exposureLevel}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          {new Date(
                            assignment.assignmentDate
                          ).toLocaleDateString("en-US", {
                            year: "numeric",
                            month: "short",
                            day: "numeric",
                          })}
                        </TableCell>
                        <TableCell>
                          <p className="text-sm text-gray-600 max-w-xs truncate">
                            {assignment.protectionMeasures}
                          </p>
                        </TableCell>
                        <TableCell>
                          <Badge className={getStatusColor(assignment.status)}>
                            {assignment.status}
                          </Badge>
                        </TableCell>
                        <TableCell className="text-right">
                          <div className="flex items-center justify-end space-x-2">
                            <Button variant="ghost" size="sm">
                              <Edit className="w-4 h-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              className="text-red-600 hover:text-red-700"
                            >
                              <Trash2 className="w-4 h-4" />
                            </Button>
                          </div>
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
