import React, { useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
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
import {
  Plus,
  Building2,
  Users,
  Phone,
  MapPin,
  Edit,
  Trash2,
} from "lucide-react";
import { toast } from "sonner";
import pt from "../i18n/pt-BR";
import { Company } from "../model/Company";
import { createCompany, getCompanies } from "../api/companyApi";
import { fetchEmpresas } from "../api/dashboard/dashboardEndpoints";
import { CompanyRegistrationInterface } from "../model/CompanyRegistration";

export function CompanyRegistration() {
  const [companies, setCompanies] = useState<Company[]>([]);
  const [setStats] = useState({
    total_empresas: "0",
    active_companies: "0",
  });

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [formData, setFormData] = useState({
    nome: "",
    cnpj: "",
    endereco: "",
    phone: "",
  });

  useEffect(() => {
    const loadData = async () => {
      try {
        const [companyList, empresaStats] = await Promise.all([
          getCompanies(),
          fetchEmpresas(),
        ]);
        setCompanies(companyList);
      } catch (error) {
        console.error(error);
        toast.error("Erro ao carregar dados das empresas");
      }
    };
    loadData();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const newCompany: CompanyRegistrationInterface = {
        nome: formData.nome,
        cnpj: formData.cnpj,
        endereco: formData.endereco,
        phone: formData.phone,
        registration_date: new Date().toISOString().split("T")[0],
        status: "active",
      };

      const createdCompany = await createCompany(newCompany);
      setCompanies((prev) => [...prev, createdCompany]);
      toast.success("Empresa registrada com sucesso!");
      setIsDialogOpen(false);
      setFormData({ nome: "", cnpj: "", endereco: "", phone: "" });
    } catch (error) {
      console.error(error);
      toast.error("Falha ao registrar empresa");
    }
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
            {pt["nav.companies"]}
          </h1>
          <p className="text-gray-600 mt-1">
            {pt["companies.description"] ||
              "Gerencie empresas clientes e suas informações"}
          </p>
        </div>
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
            <Button className="bg-blue-600 hover:bg-blue-700">
              <Plus className="w-4 h-4 mr-2" />
              {pt["companies.new"] || "Nova Empresa"}
            </Button>
          </DialogTrigger>
          <DialogContent className="sm:max-w-[500px]">
            <DialogHeader>
              <DialogTitle>
                {pt["companies.registerTitle"] || "Registrar nova empresa"}
              </DialogTitle>
              <DialogDescription>
                {pt["companies.registerDescription"] ||
                  "Adicione uma nova empresa cliente ao sistema. Todos os campos são obrigatórios."}
              </DialogDescription>
            </DialogHeader>
            <form onSubmit={handleSubmit} className="space-y-4 mt-4">
              <div className="space-y-2">
                <Label htmlFor="company-name">
                  {pt["companies.fields.name"] || "Razão Social"}
                </Label>
                <Input
                  id="company-name"
                  placeholder={pt["companies.fields.name"]}
                  value={formData.nome}
                  onChange={(e) => handleInputChange("nome", e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="cnpj">CNPJ</Label>
                <Input
                  id="cnpj"
                  placeholder={
                    pt["companies.fields.cnpj"] || "00.000.000/0000-00"
                  }
                  value={formData.cnpj}
                  onChange={(e) => handleInputChange("cnpj", e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="address">
                  {pt["companies.fields.address"] || "Endereço"}
                </Label>
                <Input
                  id="address"
                  placeholder={pt["companies.fields.address"]}
                  value={formData.endereco}
                  onChange={(e) =>
                    handleInputChange("endereco", e.target.value)
                  }
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="phone">
                  {pt["companies.fields.phone"] || "Telefone"}
                </Label>
                <Input
                  id="phone"
                  placeholder={pt["companies.fields.phone"]}
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
                  onClick={handleSubmit}
                >
                  {pt["companies.registerButton"] || "Registrar Empresa"}
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-2 md:grid-cols-2 gap-6">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                <Building2 className="w-6 h-6 text-blue-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">
                  {pt["stats.totalCompanies"]}
                </p>
                <p className="text-2xl font-semibold text-gray-900">
                  {companies.length}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-emerald-100 rounded-lg flex items-center justify-center">
                <Building2 className="w-6 h-6 text-emerald-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">
                  Active Companies
                </p>
                <p className="text-2xl font-semibold text-gray-900">
                  {companies.filter((c) => c.status === "active").length}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Companies Table */}
      <Card>
        <CardHeader>
          <CardTitle>{pt["companies.registerTitle"]}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Company</TableHead>
                  <TableHead>CNPJ</TableHead>
                  <TableHead>Contact</TableHead>
                  <TableHead>Employees</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead>Registration Date</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {companies.map((company) => (
                  <TableRow key={company.id}>
                    <TableCell>
                      <div>
                        <p className="font-medium text-gray-900">
                          {company.nome}
                        </p>
                        <div className="flex items-center text-sm text-gray-500 mt-1">
                          <MapPin className="w-3 h-3 mr-1" />
                          {company.endereco}
                        </div>
                      </div>
                    </TableCell>
                    <TableCell className="font-mono text-sm">
                      {company.cnpj}
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center text-sm text-gray-600">
                        <Phone className="w-3 h-3 mr-1" />
                        {company.phone}
                      </div>
                    </TableCell>
                    <TableCell>
                      <span className="font-medium">
                        {Number(company.quantidade_funcionarios_cliente || 0) +
                          Number(company.quantidade_funcionarios_clinica || 0)}
                      </span>
                    </TableCell>
                    <TableCell>
                      <Badge
                        variant={
                          company.status === "active" ? "default" : "secondary"
                        }
                        className={
                          company.status === "active"
                            ? "bg-green-100 text-green-800"
                            : ""
                        }
                      >
                        {company.status}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      {new Date(company.registration_date).toLocaleDateString(
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
        </CardContent>
      </Card>
    </div>
  );
}
