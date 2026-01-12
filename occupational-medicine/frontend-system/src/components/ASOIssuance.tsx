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
import { Avatar, AvatarFallback } from "./ui/avatar";
import {
  Plus,
  FileText,
  Download,
  Search,
  CheckCircle,
  Clock,
  AlertTriangle,
  User,
  Building2,
} from "lucide-react";
import { toast } from "sonner";
import pt from "../i18n/pt-BR";
import { ASO } from "../model/ASO";
import { MedicalExam } from "../model/MedicalExam";
import { getMedicalExams } from "../api/medicalExamApi";
import { getClinicStaff } from "../api/clinicApi";
import { createASO, getASOs } from "../api/asoApi";
import { getEmployeeById, getEmployeeNameMap, getEmployees } from "../api/employeeApi";
import { Employee } from "../model/Employee";

export function ASOIssuance() {
  const [asos, setAsos] = useState<ASO[]>([]);
  const [pendingExams, setPendingExams] = useState<MedicalExam[]>([]);
  const [doctors, setDoctors] = useState<string[]>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [formData, setFormData] = useState({
    exam: "",
    observations: "",
    restrictions: "",
    validUntil: "",
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  interface RawASO {
    id_exames: number;
    id_funcionario: number;
    data_exame: string;
    type_exam: string;
    result: string;
    status: string;
    observations?: string;
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        const [asoData, examData, staffData] = await Promise.all([
          getASOs(),
          getMedicalExams(),
          getClinicStaff(),
        ]);

        const normalizedASOs = await Promise.all(
          (asoData as unknown as RawASO[]).map(async (a) => {
            let employeeName = "Unknown";

            try {
              const employee = await getEmployeeById(a.id_funcionario.toString());
              employeeName = employee.nome;
            } catch (err) {
              console.warn(`Failed to fetch employee with id ${a.id_funcionario}`);
            }

            return {
              id: a.id_exames?.toString() ?? Math.random().toString(),
              employee: employeeName,
              examDate: a.data_exame,
              examType: a.type_exam as ASO["examType"],
              result: a.result as ASO["result"],
              status: a.status as ASO["status"],
              observations: a.observations ?? "",
              restrictions: "",
              validUntil: "",
              asoNumber: `ASO-${new Date().getFullYear()}-${Math.floor(Math.random() * 1000)}`,
              issuanceDate: new Date().toISOString(),
            };
          })
        );

        setAsos(normalizedASOs);
      } catch (err: any) {
        console.error(err);
        setError("Failed to load ASO data.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const selectedExam = pendingExams.find((e) => e.id === formData.exam);
      if (!selectedExam) return;

      const newASO: Omit<ASO, "id"> = {
        employee: selectedExam.employee,
        company: selectedExam.company,
        examDate: selectedExam.date,
        examType: selectedExam.type,
        doctor: selectedExam.doctor,
        result: selectedExam.result || "fit",
        issuanceDate: new Date().toISOString(),
        validUntil: formData.validUntil || "",
        asoNumber: `ASO-${new Date().getFullYear()}-${Math.floor(
          Math.random() * 1000,
        )}`,
        status: "issued",
        observations: formData.observations,
        restrictions: formData.restrictions,
      };

      const createdASO = await createASO(newASO);
      setAsos((prev) => [...prev, createdASO]);

      toast.success("ASO issued successfully!");
      setIsDialogOpen(false);
      setFormData({
        exam: "",
        observations: "",
        restrictions: "",
        validUntil: "",
      });
    } catch (err) {
      console.error(err);
      toast.error("Failed to issue ASO.");
    }
  };

  const handleInputChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const getResultBadge = (result: string) => {
    const colors = {
      fit: "bg-green-100 text-green-800",
      unfit: "bg-red-100 text-red-800",
      fit_with_restrictions: "bg-yellow-100 text-yellow-800",
    };
    const labels = {
      fit: "Fit for Work",
      unfit: "Unfit for Work",
      fit_with_restrictions: "Fit with Restrictions",
    };
    return (
      <Badge className={colors[result as keyof typeof colors]}>
        {labels[result as keyof typeof labels]}
      </Badge>
    );
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "issued":
        return <CheckCircle className="w-4 h-4 text-green-500" />;
      case "pending":
        return <Clock className="w-4 h-4 text-orange-500" />;
      case "cancelled":
        return <AlertTriangle className="w-4 h-4 text-red-500" />;
      default:
        return <Clock className="w-4 h-4 text-gray-500" />;
    }
  };

  const getExamTypeLabel = (type: string) => {
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

  const asoCounts = {
    total: asos.length,
    issued: asos.filter((a) => a.status === "issued").length,
    pending: asos.filter((a) => a.status === "pending").length,
    thisMonth: asos.filter((a) => {
      const date = new Date(a.issuanceDate);
      return (
        date.getMonth() === new Date().getMonth() &&
        date.getFullYear() === new Date().getFullYear()
      );
    }).length,
  };

  if (loading)
    return <div className="p-6 text-gray-500">Loading ASO data...</div>;
  if (error) return <div className="p-6 text-red-600">{error}</div>;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">
            {pt["nav.asoIssuance"]}
          </h1>
          <p className="text-gray-600 mt-1">
            {pt["aso.description"] ||
              "Emitir e gerenciar Atestados de Saúde Ocupacional (ASO)"}
          </p>
        </div>
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogContent className="sm:max-w-[600px]">
            <DialogHeader>
              <DialogTitle>
                {pt["aso.newTitle"] || "Emitir novo ASO"}
              </DialogTitle>
              <DialogDescription>
                {pt["aso.newDescription"] ||
                  "Emita um Atestado de Saúde Ocupacional baseado em um exame concluído."}
              </DialogDescription>
            </DialogHeader>
            <form onSubmit={handleSubmit} className="space-y-4 mt-4">
              <div className="space-y-2">
                <Label htmlFor="exam-select">Select Completed Exam</Label>
                <Select
                  value={formData.exam}
                  onValueChange={(value: string) =>
                    handleInputChange("exam", value)
                  }
                >
                  <SelectTrigger>
                    <SelectValue
                      placeholder={
                        pt["aso.selectExamPlaceholder"] ||
                        "Selecione um exame para emitir ASO"
                      }
                    />
                  </SelectTrigger>
                  <SelectContent>
                    {pendingExams.map((exam, idx) => (
                      <SelectItem
                        key={exam.id ?? `exam-${idx}`}
                        value={String(exam.id ?? idx)}
                      >
                        {exam.employee} - {getExamTypeLabel(exam.type)} (
                        {new Date(exam.date).toLocaleDateString()})
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              {formData.exam && (
                <>
                  <div className="p-4 bg-gray-50 rounded-md">
                    <h4 className="font-medium text-gray-900 mb-2">
                      Exam Details
                    </h4>
                    {pendingExams.find((e) => e.id === formData.exam) && (
                      <div className="grid grid-cols-2 gap-4 text-sm">
                        <div>
                          <span className="text-gray-500">Employee:</span>
                          <p className="font-medium">
                            {
                              pendingExams.find((e) => e.id === formData.exam)
                                ?.employee
                            }
                          </p>
                        </div>
                        <div>
                          <span className="text-gray-500">Company:</span>
                          <p className="font-medium">
                            {
                              pendingExams.find((e) => e.id === formData.exam)
                                ?.company
                            }
                          </p>
                        </div>
                        <div>
                          <span className="text-gray-500">Exam Type:</span>
                          <p className="font-medium">
                            {getExamTypeLabel(
                              pendingExams.find((e) => e.id === formData.exam)
                                ?.type || "",
                            )}
                          </p>
                        </div>
                        <div>
                          <span className="text-gray-500">Result:</span>
                          <div className="mt-1">
                            {getResultBadge(
                              pendingExams.find((e) => e.id === formData.exam)
                                ?.result || "fit",
                            )}
                          </div>
                        </div>
                      </div>
                    )}
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="valid-until">Valid Until (Optional)</Label>
                    <Input
                      id="valid-until"
                      type="date"
                      value={formData.validUntil}
                      onChange={(e) =>
                        handleInputChange("validUntil", e.target.value)
                      }
                    />
                    <p className="text-xs text-gray-500">
                      Leave empty for one-time certificates
                      (admission/dismissal)
                    </p>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="aso-observations">
                      Medical Observations
                    </Label>
                    <Textarea
                      id="aso-observations"
                      placeholder={
                        pt["aso.medicalObservationsPlaceholder"] ||
                        "Insira observações médicas para o ASO"
                      }
                      value={formData.observations}
                      onChange={(e) =>
                        handleInputChange("observations", e.target.value)
                      }
                      required
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="restrictions">
                      Work Restrictions (If applicable)
                    </Label>
                    <Textarea
                      id="restrictions"
                      placeholder={
                        pt["aso.workRestrictionsPlaceholder"] ||
                        "Insira quaisquer restrições ou limitações de trabalho"
                      }
                      value={formData.restrictions}
                      onChange={(e) =>
                        handleInputChange("restrictions", e.target.value)
                      }
                    />
                  </div>
                </>
              )}

              <div className="flex justify-end space-x-2 pt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => setIsDialogOpen(false)}
                >
                  Cancel
                </Button>
                <Button
                  type="submit"
                  className="bg-blue-600 hover:bg-blue-700"
                  disabled={!formData.exam}
                >
                  Issue ASO
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        {/* ... your stats cards unchanged ... */}
      </div>

      {/* Table */}
      <Card>
        <CardHeader>
          <CardTitle>ASO Records</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Employee</TableHead>
                  <TableHead>ASO Number</TableHead>
                  <TableHead>Exam Type</TableHead>
                  <TableHead>Result</TableHead>
                  <TableHead>Issuance Date</TableHead>
                  <TableHead>Valid Until</TableHead>
                  <TableHead>Status</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {asos.map((aso) => (
                  <TableRow key={aso.id ?? Math.random()}>
                    <TableCell>
                      <div className="flex items-center space-x-3">
                        <Avatar>
                          <AvatarFallback className="bg-blue-100 text-blue-700">
                            {(aso.employee ?? "")
                              .split(" ")
                              .map((n) => n[0])
                              .join("")
                              .substring(0, 2) || "?"}
                          </AvatarFallback>
                        </Avatar>
                        <div>
                          <p className="font-medium text-gray-900">
                            {aso.employee || "Unknown"}
                          </p>
                        </div>
                      </div>
                    </TableCell>
                    <TableCell className="font-mono text-sm">
                      {aso.asoNumber || "-"}
                    </TableCell>
                    <TableCell>
                      <Badge variant="outline">
                        {getExamTypeLabel(aso.examType || "-")}
                      </Badge>
                    </TableCell>
                    <TableCell>{getResultBadge(aso.result || "fit")}</TableCell>
                    <TableCell>
                      {aso.issuanceDate
                        ? new Date(aso.issuanceDate).toLocaleDateString(
                            "en-US",
                            {
                              year: "numeric",
                              month: "short",
                              day: "numeric",
                            },
                          )
                        : "-"}
                    </TableCell>
                    <TableCell>
                      {aso.validUntil
                        ? new Date(aso.validUntil).toLocaleDateString("en-US", {
                            year: "numeric",
                            month: "short",
                            day: "numeric",
                          })
                        : "N/A"}
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center space-x-2">
                        {getStatusIcon(aso.status)}
                        <span className="capitalize">{aso.status}</span>
                      </div>
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
