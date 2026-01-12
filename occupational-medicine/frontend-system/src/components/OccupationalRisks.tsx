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
  AlertTriangle,
  Users,
  Building2,
  Search,
  Edit,
  Trash2,
  Shield,
  Zap,
  Droplets,
  Volume2,
} from "lucide-react";
import { toast } from "sonner";
import pt from "../i18n/pt-BR";
import { OccupationalRisk } from "../model/RisksByCategory";
import { RiskAssignment } from "../model/RiskAssignment";
import { getOccupationalRisks } from "../api/risksByCategoryApi";
import { getEmployeesWithCompanyName } from "../api/employeeApi";

//   riscos: "/dashboard/riscos",

export function OccupationalRisks() {
  const [risks] = useState<OccupationalRisk[]>([
    {
      id: "1",
      name: "Noise Exposure",
      category: "physical",
      severity: "high",
      description:
        "Continuous exposure to noise levels above 85 dB in production areas",
      preventiveMeasures:
        "Use of hearing protection equipment (earplugs/headphones), noise reduction engineering controls",
      affectedEmployees: [
        "João Silva Santos",
        "Pedro Henrique Lima",
        "Carlos Roberto Souza",
      ],
      companies: ["BuildCorp Construction", "TechCorp Solutions"],
      lastAssessment: "2023-12-15",
      nextAssessment: "2024-06-15",
      status: "active",
    },
    {
      id: "2",
      name: "Chemical Solvents",
      category: "chemical",
      severity: "medium",
      description:
        "Exposure to organic solvents in cleaning and maintenance operations",
      preventiveMeasures:
        "Adequate ventilation, chemical-resistant gloves, respiratory protection when necessary",
      affectedEmployees: ["Maria Oliveira Costa", "Ana Paula Ferreira"],
      companies: ["InnovaCorp Industries", "GreenCorp Sustentável"],
      lastAssessment: "2024-01-10",
      nextAssessment: "2024-07-10",
      status: "controlled",
    },
    {
      id: "3",
      name: "Repetitive Strain",
      category: "ergonomic",
      severity: "medium",
      description:
        "Repetitive movements and prolonged computer use causing musculoskeletal disorders",
      preventiveMeasures:
        "Ergonomic workstations, regular breaks, stretching exercises, posture training",
      affectedEmployees: [
        "João Silva Santos",
        "Ana Paula Ferreira",
        "Maria Oliveira Costa",
      ],
      companies: ["TechCorp Solutions", "InnovaCorp Industries"],
      lastAssessment: "2024-01-05",
      nextAssessment: "2024-07-05",
      status: "controlled",
    },
    {
      id: "4",
      name: "Height Work",
      category: "accident",
      severity: "critical",
      description:
        "Work performed at heights above 2 meters without adequate protection",
      preventiveMeasures:
        "Safety harnesses, guardrails, safety nets, fall protection training",
      affectedEmployees: ["Pedro Henrique Lima"],
      companies: ["BuildCorp Construction"],
      lastAssessment: "2023-11-20",
      nextAssessment: "2024-05-20",
      status: "active",
    },
    {
      id: "5",
      name: "Biological Agents",
      category: "biological",
      severity: "low",
      description:
        "Potential exposure to microorganisms in laboratory environments",
      preventiveMeasures:
        "Personal protective equipment, hand hygiene, proper waste disposal",
      affectedEmployees: ["Carlos Roberto Souza"],
      companies: ["GreenCorp Sustentável"],
      lastAssessment: "2024-01-08",
      nextAssessment: "2024-07-08",
      status: "controlled",
    },
  ]);

  const [assignments] = useState<RiskAssignment[]>([
    {
      id: "1",
      employee: "João Silva Santos",
      company: "TechCorp Solutions",
      riskName: "Repetitive Strain",
      riskCategory: "ergonomic",
      exposureLevel: "medium",
      assignmentDate: "2024-01-05",
      protectionMeasures: "Ergonomic chair and desk setup, hourly breaks",
      status: "monitored",
    },
    {
      id: "2",
      employee: "Pedro Henrique Lima",
      company: "BuildCorp Construction",
      riskName: "Height Work",
      riskCategory: "accident",
      exposureLevel: "high",
      assignmentDate: "2023-11-20",
      protectionMeasures:
        "Full body harness, safety lines, daily equipment inspection",
      status: "active",
    },
    {
      id: "3",
      employee: "Maria Oliveira Costa",
      company: "InnovaCorp Industries",
      riskName: "Chemical Solvents",
      riskCategory: "chemical",
      exposureLevel: "low",
      assignmentDate: "2024-01-10",
      protectionMeasures: "Chemical-resistant gloves, adequate ventilation",
      status: "monitored",
    },
  ]);

  const employees = [
    "João Silva Santos",
    "Maria Oliveira Costa",
    "Pedro Henrique Lima",
    "Ana Paula Ferreira",
    "Carlos Roberto Souza",
  ];

  const companies = [
    "TechCorp Solutions",
    "InnovaCorp Industries",
    "BuildCorp Construction",
    "GreenCorp Sustentável",
  ];

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

  const filteredRisks = risks.filter(
    (risk) =>
      risk.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      risk.category.toLowerCase().includes(searchTerm.toLowerCase()) ||
      risk.description.toLowerCase().includes(searchTerm.toLowerCase()),
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
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">
            {pt["nav.occupationalRisks"]}
          </h1>
          <p className="text-gray-600 mt-1">
            {pt["risks.description"] ||
              "Gerencie e monitore os riscos de saúde e segurança no trabalho"}
          </p>
        </div>
        <div className="flex space-x-2">
          <Dialog
            open={isAssignDialogOpen}
            onOpenChange={setIsAssignDialogOpen}
          >
            <DialogContent className="sm:max-w-[500px]">
              <DialogHeader>
                <DialogTitle>{pt["risks.assignTitle"]}</DialogTitle>
                <DialogDescription>
                  {pt["risks.assignDescription"] ||
                    "Associe um risco ocupacional a um funcionário específico."}
                </DialogDescription>
              </DialogHeader>
              <form onSubmit={handleAssignSubmit} className="space-y-4 mt-4">
                <div className="space-y-2">
                  <Label htmlFor="assign-employee">
                    {pt["clients.fields.name"]}
                  </Label>
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
                      <SelectValue
                        placeholder={
                          pt["risks.selectEmployee"] ||
                          "Selecione o funcionário"
                        }
                      />
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
                  <Label htmlFor="assign-risk">
                    {pt["risks.table.risk"] || "Risco"}
                  </Label>
                  <Select
                    value={assignFormData.risk}
                    onValueChange={(value: string) =>
                      setAssignFormData((prev) => ({ ...prev, risk: value }))
                    }
                  >
                    <SelectTrigger>
                      <SelectValue
                        placeholder={
                          pt["risks.selectRisk"] || "Selecione o risco"
                        }
                      />
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
                  <Label htmlFor="exposure-level">
                    {pt["risks.fields.exposureLevel"] || "Nível de exposição"}
                  </Label>
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
                      <SelectValue
                        placeholder={
                          pt["risks.selectExposure"] ||
                          "Selecione o nível de exposição"
                        }
                      />
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
                    {pt["risks.fields.protectionMeasures"] ||
                      "Medidas de proteção"}
                  </Label>
                  <Textarea
                    id="protection-measures"
                    placeholder={
                      pt["risks.protectionPlaceholder"] ||
                      "Descreva medidas de proteção específicas para este funcionário"
                    }
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
            <DialogContent className="sm:max-w-[600px]">
              <DialogHeader>
                <DialogTitle>{pt["risks.registerTitle"]}</DialogTitle>
                <DialogDescription>
                  {pt["risks.registerDescription"] ||
                    "Adicione um novo risco ocupacional ao sistema para monitoramento e gestão."}
                </DialogDescription>
              </DialogHeader>
              <form onSubmit={handleRiskSubmit} className="space-y-4 mt-4">
                <div className="space-y-2">
                  <Label htmlFor="risk-name">
                    {pt["risks.fields.name"] || "Nome do risco"}
                  </Label>
                  <Input
                    id="risk-name"
                    placeholder={
                      pt["risks.fields.namePlaceholder"] ||
                      "Insira o nome do risco"
                    }
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
                    <Label htmlFor="risk-category">
                      {pt["risks.fields.category"] || "Categoria"}
                    </Label>
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
                        <SelectValue
                          placeholder={
                            pt["risks.selectCategory"] ||
                            "Selecione a categoria"
                          }
                        />
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
                    <Label htmlFor="risk-severity">
                      {pt["risks.fields.severity"] || "Severidade"}
                    </Label>
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
                        <SelectValue
                          placeholder={
                            pt["risks.selectSeverity"] ||
                            "Selecione a severidade"
                          }
                        />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="low">
                          {pt["severity.low"] || "Baixa"}
                        </SelectItem>
                        <SelectItem value="medium">
                          {pt["severity.medium"] || "Média"}
                        </SelectItem>
                        <SelectItem value="high">
                          {pt["severity.high"] || "Alta"}
                        </SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="risk-description">
                    {pt["risks.fields.description"] || "Descrição"}
                  </Label>
                  <Textarea
                    id="risk-description"
                    placeholder={
                      pt["risks.fields.descriptionPlaceholder"] ||
                      "Descreva o risco ocupacional em detalhes"
                    }
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
                    {pt["risks.fields.preventiveMeasures"] ||
                      "Medidas preventivas"}
                  </Label>
                  <Textarea
                    id="preventive-measures"
                    placeholder={
                      pt["risks.fields.preventivePlaceholder"] ||
                      "Descreva medidas preventivas e controles"
                    }
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
                    {pt["common.cancel"]}
                  </Button>
                  <Button
                    type="submit"
                    className="bg-blue-600 hover:bg-blue-700"
                  >
                    {pt["risks.registerButton"]}
                  </Button>
                </div>
              </form>
            </DialogContent>
          </Dialog>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-3 md:grid-cols-3 gap-6">
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
        <Card></Card>
      </div>

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
                            },
                          )}
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
