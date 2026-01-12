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
import { Avatar, AvatarFallback } from "./ui/avatar";
import { Plus, Users, Building2, Edit, Trash2, Search } from "lucide-react";
import { toast } from "sonner";
import pt from "../i18n/pt-BR";
import { Employee } from "../model/Employee";
import { getEmployeesWithCompanyName } from "../api/employeeApi";
import { getCompanies } from "../api/companyApi";

export function ClientEmployeeRegistration() {
  const [employees, setEmployees] = useState<
    (Employee & { companyName: string })[]
  >([]);
  const [companies, setCompanies] = useState<{ id: string; nome: string }[]>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [formData, setFormData] = useState({
    nome: "",
    cpf: "",
    data_nascimento: "",
    companyName: "",
    cargo: "",
    department: "",
  });

  useEffect(() => {
    async function fetchEmployees() {
      try {
        const data = await getEmployeesWithCompanyName();
        setEmployees(data);
      } catch (error) {
        toast.error("Failed to load employees.");
        console.error(error);
      }
    }
    fetchEmployees();
  }, []);

  useEffect(() => {
    async function fetchCompanies() {
      try {
        const data = await getCompanies();
        setCompanies(data);
      } catch (error) {
        console.error(error);
        toast.error("Failed to load companies.");
      }
    }
    fetchCompanies();
  }, []);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    toast.success("Employee registered successfully!");
    setIsDialogOpen(false);
    setFormData({
      nome: "",
      cpf: "",
      data_nascimento: "",
      companyName: "",
      cargo: "",
      department: "",
    });
  };

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">
            {pt["nav.clientEmployees"]}
          </h1>
          <p className="text-gray-600 mt-1">
            {pt["clients.description"] ||
              "Gerencie funcionários de empresas clientes"}
          </p>
        </div>
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
          </DialogTrigger>
          <DialogContent className="sm:max-w-[500px]">
            <DialogHeader>
              <DialogTitle>
                {pt["clients.registerTitle"] || "Registrar novo funcionário"}
              </DialogTitle>
              <DialogDescription>
                {pt["clients.registerDescription"] ||
                  "Adicione um novo funcionário de uma empresa cliente ao sistema."}
              </DialogDescription>
            </DialogHeader>
            <form onSubmit={handleSubmit} className="space-y-4 mt-4">
              <div className="space-y-2">
                <Label htmlFor="employee-name">
                  {pt["clients.fields.name"] || "Nome completo"}
                </Label>
                <Input
                  id="employee-name"
                  placeholder={pt["clients.fields.name"]}
                  value={formData.nome}
                  onChange={(e) => handleInputChange("name", e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="cpf">CPF</Label>
                <Input
                  id="cpf"
                  placeholder={pt["clients.fields.cpf"] || "000.000.000-00"}
                  value={formData.cpf}
                  onChange={(e) => handleInputChange("cpf", e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="date-birth">
                  {pt["clients.fields.dob"] || "Data de nascimento"}
                </Label>
                <Input
                  id="date-birth"
                  type="date"
                  value={formData.data_nascimento}
                  onChange={(e) =>
                    handleInputChange("dateOfBirth", e.target.value)
                  }
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="company">Company</Label>
                <Select
                  value={formData.companyName}
                  onValueChange={(value: string) =>
                    handleInputChange("company", value)
                  }
                >
                  <SelectTrigger>
                    <SelectValue
                      placeholder={
                        pt["clients.selectCompanyPlaceholder"] ||
                        "Selecione a empresa"
                      }
                    />
                  </SelectTrigger>
                  <SelectContent>
                    {companies.map((company) => (
                      <SelectItem key={company} value={company}>
                        {company}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="position">
                  {pt["clients.fields.position"] || "Cargo"}
                </Label>
                <Input
                  id="position"
                  placeholder={pt["clients.fields.position"]}
                  value={formData.cargo}
                  onChange={(e) =>
                    handleInputChange("position", e.target.value)
                  }
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="department">
                  {pt["clients.fields.department"] || "Departamento"}
                </Label>
                <Input
                  id="department"
                  placeholder={pt["clients.fields.department"]}
                  value={formData.department}
                  onChange={(e) =>
                    handleInputChange("department", e.target.value)
                  }
                  required
                />
              </div>
              <div className="flex justify-end space-x-2 pt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => setIsDialogOpen(false)}
                >
                  {pt["common.cancel"] || "Cancelar"}
                </Button>
                <Button type="submit" className="bg-blue-600 hover:bg-blue-700">
                  {pt["clients.registerButton"] || "Registrar Funcionário"}
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-3 md:grid-cols-3 gap-6">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                <Users className="w-6 h-6 text-green-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">
                  Active Employees
                </p>
                <p className="text-2xl font-semibold text-gray-900">
                  {employees.filter((e) => e.status === "active").length}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
                <Building2 className="w-6 h-6 text-orange-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Companies</p>
                <p className="text-2xl font-semibold text-gray-900">
                  {new Set(employees.map((e) => e.companyName)).size}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                <Users className="w-6 h-6 text-purple-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">
                  {pt["clients.newThisMonth"] || "Novos este mês"}
                </p>
                <p className="text-2xl font-semibold text-gray-900">3</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Employees Table */}
      <Card>
        <CardHeader>
          <CardTitle>Client Employees</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Employee</TableHead>
                  <TableHead>CPF</TableHead>
                  <TableHead>Company</TableHead>
                  <TableHead>Position</TableHead>
                  <TableHead>Admission Date</TableHead>
                  <TableHead>Status</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {employees.map((employee) => (
                  <TableRow key={employee.id}>
                    <TableCell>
                      <div className="flex items-center space-x-3">
                        <Avatar>
                          <AvatarFallback className="bg-blue-100 text-blue-700">
                            {employee.nome
                              .split(" ")
                              .map((n) => n[0])
                              .join("")
                              .substring(0, 2)}
                          </AvatarFallback>
                        </Avatar>
                        <div>
                          <p className="font-medium text-gray-900">
                            {employee.nome}
                          </p>
                        </div>
                      </div>
                    </TableCell>
                    <TableCell className="font-mono text-sm">
                      {employee.cpf}
                    </TableCell>
                    <TableCell>{employee.companyName}</TableCell>
                    <TableCell>{employee.cargo}</TableCell>
                    <TableCell>
                      {new Date(employee.admission_date).toLocaleDateString(
                        "en-US",
                        { year: "numeric", month: "short", day: "numeric" },
                      )}
                    </TableCell>
                    <TableCell>
                      <Badge
                        variant={
                          employee.status === "active" ? "default" : "secondary"
                        }
                        className={
                          employee.status === "active"
                            ? "bg-green-100 text-green-800"
                            : ""
                        }
                      >
                        {employee.status}
                      </Badge>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
