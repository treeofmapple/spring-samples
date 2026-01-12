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
import {
  Plus,
  UserCheck,
  Users,
  Stethoscope,
  Edit,
  Trash2,
  Search,
  Award,
} from "lucide-react";
import { toast } from "sonner";
import { useAuth } from "../App";
import pt from "../i18n/pt-BR";
import { ClinicStaff } from "../model/ClinicStaff";
import AdminRouteGuard from "./Authenticated";
import { createClinicStaff, getClinicStaff } from "../api/clinicApi";

export function ClinicStaffRegistration() {
  const { user } = useAuth();

  const [staff, setStaff] = useState<ClinicStaff[]>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    role: "",
    registrationNumber: "",
    specialty: "",
    email: "",
    phone: "",
  });

  useEffect(() => {
    const loadStaff = async () => {
      try {
        const staffList = await getClinicStaff();
        setStaff(staffList);
      } catch (error) {
        console.error(error);
        toast.error("Erro ao carregar equipe da clínica");
      }
    };
    loadStaff();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const newStaffData = {
        nome: formData.name,
        role: formData.role as "doctor" | "employee" | "admin",
        crm: formData.registrationNumber,
        especialidade: formData.specialty || undefined,
        email: formData.email,
        phone: formData.phone,
        data_contratacao: new Date().toISOString(),
        status: "active",
      };

      const createdStaff = await createClinicStaff(newStaffData);
      setStaff((prev) => [...prev, createdStaff]);

      toast.success("Membro da equipe registrado com sucesso!");
      setIsDialogOpen(false);
      setFormData({
        name: "",
        role: "",
        registrationNumber: "",
        specialty: "",
        email: "",
        phone: "",
      });
    } catch (error) {
      console.error(error);
      toast.error("Falha ao registrar membro da equipe");
    }
  };

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const getRoleIcon = (role: string) => {
    switch (role) {
      case "doctor":
        return <Stethoscope className="w-4 h-4" />;
      case "employee":
        return <UserCheck className="w-4 h-4" />;
      case "admin":
        return <Award className="w-4 h-4" />;
      default:
        return <Users className="w-4 h-4" />;
    }
  };

  const getRoleBadgeColor = (role: string) => {
    switch (role) {
      case "doctor":
        return "bg-blue-100 text-blue-800";
      case "employee":
        return "bg-green-100 text-green-800";
      case "admin":
        return "bg-purple-100 text-purple-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  return (
    <div className="space-y-6">
      <AdminRouteGuard user={user}>
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-semibold text-gray-900">
              {pt["nav.clinicStaff"]}
            </h1>
            <p className="text-gray-600 mt-1">
              {pt["clinic.description"] ||
                "Gerencie profissionais e membros da equipe da clínica"}
            </p>
          </div>
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogTrigger asChild>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[500px]">
              <DialogHeader>
                <DialogTitle>
                  {pt["clinic.registerTitle"] ||
                    "Registrar novo membro da equipe"}
                </DialogTitle>
                <DialogDescription>
                  {pt["clinic.registerDescription"] ||
                    "Adicione um novo profissional à equipe da clínica."}
                </DialogDescription>
              </DialogHeader>
              <form onSubmit={handleSubmit} className="space-y-4 mt-4">
                <div className="space-y-2">
                  <Label htmlFor="staff-name">
                    {pt["clinic.fields.name"] || "Nome completo"}
                  </Label>
                  <Input
                    id="staff-name"
                    placeholder={pt["clinic.fields.name"]}
                    value={formData.name}
                    onChange={(e) => handleInputChange("name", e.target.value)}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="role">
                    {pt["clinic.fields.role"] || "Função"}
                  </Label>
                  <Select
                    value={formData.role}
                    onValueChange={(value: string) =>
                      handleInputChange("role", value)
                    }
                  >
                    <SelectTrigger>
                      <SelectValue
                        placeholder={
                          pt["clinic.fields.role"] || "Selecione a função"
                        }
                      />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="doctor">Doctor</SelectItem>
                      <SelectItem value="nurse">Nurse</SelectItem>
                      <SelectItem value="technician">Technician</SelectItem>
                      <SelectItem value="administrator">
                        Administrator
                      </SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="registration">
                    {pt["clinic.fields.registration"] ||
                      "Registro profissional"}
                  </Label>
                  <Input
                    id="registration"
                    placeholder={pt["clinic.fields.registration"]}
                    value={formData.registrationNumber}
                    onChange={(e) =>
                      handleInputChange("registrationNumber", e.target.value)
                    }
                    required
                  />
                </div>
                {formData.role === "doctor" && (
                  <div className="space-y-2">
                    <Label htmlFor="specialty">
                      {pt["clinic.fields.specialty"] || "Especialidade médica"}
                    </Label>
                    <Input
                      id="specialty"
                      placeholder={pt["clinic.fields.specialty"]}
                      value={formData.specialty}
                      onChange={(e) =>
                        handleInputChange("specialty", e.target.value)
                      }
                    />
                  </div>
                )}
                <div className="space-y-2">
                  <Label htmlFor="staff-email">Email</Label>
                  <Input
                    id="staff-email"
                    type="email"
                    placeholder={pt["login.placeholderEmail"]}
                    value={formData.email}
                    onChange={(e) => handleInputChange("email", e.target.value)}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="staff-phone">
                    {pt["clinic.fields.phone"] || "Telefone"}
                  </Label>
                  <Input
                    id="staff-phone"
                    placeholder={pt["clinic.fields.phone"]}
                    value={formData.phone}
                    onChange={(e) => handleInputChange("phone", e.target.value)}
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
                  <Button
                    type="submit"
                    className="bg-blue-600 hover:bg-blue-700"
                  >
                    {pt["clinic.registerButton"] || "Registrar Membro"}
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
                  <Stethoscope className="w-6 h-6 text-blue-600" />
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-600">Doctors</p>
                  <p className="text-2xl font-semibold text-gray-900">
                    {staff.filter((s) => s.role === "doctor").length}
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center space-x-4">
                <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                  <UserCheck className="w-6 h-6 text-green-600" />
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-600">Employee</p>
                  <p className="text-2xl font-semibold text-gray-900">
                    {staff.filter((s) => s.role === "employee").length}
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center space-x-4">
                <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                  <Award className="w-6 h-6 text-purple-600" />
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-600">
                    Administrator
                  </p>
                  <p className="text-2xl font-semibold text-gray-900">
                    {staff.filter((s) => s.role === "admin").length}
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center space-x-4">
                <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
                  <Users className="w-6 h-6 text-orange-600" />
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-600">
                    {pt["clinic.totalStaff"] || "Total de Equipe"}
                  </p>
                  <p className="text-2xl font-semibold text-gray-900">
                    {staff.length}
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Staff Table */}
        <Card>
          <CardHeader>
            <CardTitle>Clinic Staff Members</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Staff Member</TableHead>
                    <TableHead>Role</TableHead>
                    <TableHead>Registration</TableHead>
                    <TableHead>Contact</TableHead>
                    <TableHead>Hire Date</TableHead>
                    <TableHead>Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {staff.map((member) => (
                    <TableRow key={member.id}>
                      <TableCell>
                        <div className="flex items-center space-x-3">
                          <Avatar>
                            <AvatarFallback className="bg-blue-100 text-blue-700">
                              {member.nome
                                .split(" ")
                                .map((n) => n[0])
                                .join("")
                                .substring(0, 2)}
                            </AvatarFallback>
                          </Avatar>
                          <div>
                            <p className="font-medium text-gray-900">
                              {member.nome}
                            </p>
                            {member.especialidade && (
                              <p className="text-sm text-gray-500">
                                {member.especialidade}
                              </p>
                            )}
                          </div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge className={getRoleBadgeColor(member.role)}>
                          <span className="flex items-center space-x-1">
                            {getRoleIcon(member.role)}
                            <span className="capitalize">{member.role}</span>
                          </span>
                        </Badge>
                      </TableCell>
                      <TableCell className="font-mono text-sm">
                        {member.crm}
                      </TableCell>
                      <TableCell>
                        <div className="space-y-1">
                          <p className="text-sm">{member.email}</p>
                          <p className="text-sm text-gray-500">
                            {member.phone}
                          </p>
                        </div>
                      </TableCell>
                      <TableCell>
                        {new Date(member.data_contratacao).toLocaleDateString(
                          "en-US",
                          {
                            year: "numeric",
                            month: "short",
                            day: "numeric",
                          },
                        )}
                      </TableCell>
                      <TableCell>
                        <Badge
                          variant={
                            member.status === "active" ? "default" : "secondary"
                          }
                          className={
                            member.status === "active"
                              ? "bg-green-100 text-green-800"
                              : ""
                          }
                        >
                          {member.status}
                        </Badge>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </CardContent>
        </Card>
      </AdminRouteGuard>
    </div>
  );
}
