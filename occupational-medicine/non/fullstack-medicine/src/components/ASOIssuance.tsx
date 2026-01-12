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
import { Avatar, AvatarFallback } from "@/ui/avatar";
import {
  Plus,
  FileText,
  Download,
  Search,
  CheckCircle,
  Clock,
  AlertTriangle,
} from "lucide-react";
import { toast } from "sonner";
import { ASO } from "@/model/ASO";
import { createASO, getASOs } from "@/api/asoApi";
import { MedicalExam } from "@/model/MedicalExam";
import { getMedicalExams } from "@/api/medicalExamApi";

export function ASOIssuance() {
  const [asos, setAsos] = useState<ASO[]>([]);
  const [exams, setExams] = useState<MedicalExam[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [formData, setFormData] = useState({
    exam: "",
    observations: "",
    restrictions: "",
    validUntil: "",
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [asoData, examData] = await Promise.all([
          getASOs(),
          getMedicalExams(),
        ]);

        const completedExams = examData.filter(
          (e) => e.status === "completed" && e.result
        );

        setAsos(asoData);
        setExams(completedExams);
      } catch (err) {
        const message =
          err instanceof Error ? err.message : "Failed to fetch ASOs or exams";
        setError(message);
        toast.error(message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const selectedExam = exams.find((e) => e.id === formData.exam);
      if (!selectedExam) return;

      const newASO: Omit<ASO, "id"> = {
        employee: selectedExam.employee,
        company: selectedExam.company,
        examDate: selectedExam.date,
        examType: selectedExam.type,
        doctor: selectedExam.doctor,
        result: selectedExam.result!,
        issuanceDate: new Date().toISOString(),
        validUntil: formData.validUntil || "",
        asoNumber: `ASO-${new Date().getFullYear()}-${Math.floor(
          Math.random() * 1000
        )
          .toString()
          .padStart(3, "0")}`,
        status: "issued",
        observations: formData.observations,
        restrictions: formData.restrictions || "",
      };

      const created = await createASO(newASO);
      setAsos((prev) => [...prev, created]);

      toast.success("ASO issued successfully!");
      setIsDialogOpen(false);
      setFormData({
        exam: "",
        observations: "",
        restrictions: "",
        validUntil: "",
      });
    } catch (err) {
      const message =
        err instanceof Error ? err.message : "Failed to issue ASO";
      toast.error(message);
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

  const filteredASOs = asos.filter(
    (aso) =>
      aso.employee.toLowerCase().includes(searchTerm.toLowerCase()) ||
      aso.company.toLowerCase().includes(searchTerm.toLowerCase()) ||
      aso.asoNumber.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const asoCounts = {
    total: asos.length,
    issued: asos.filter((a) => a.status === "issued").length,
    pending: asos.filter((a) => a.status === "pending").length,
    thisMonth: asos.filter(
      (a) =>
        new Date(a.issuanceDate).getMonth() === new Date().getMonth() &&
        new Date(a.issuanceDate).getFullYear() === new Date().getFullYear()
    ).length,
  };

  if (loading)
    return <p className="text-center text-gray-600 py-10">Loading ASOs...</p>;
  if (error)
    return (
      <p className="text-center text-red-600 py-10">
        Failed to load ASOs: {error}
      </p>
    );

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-gray-900">ASO Issuance</h1>
          <p className="text-gray-600 mt-1">
            Issue and manage Occupational Health Certificates (ASO)
          </p>
        </div>
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
            <Button className="bg-blue-600 hover:bg-blue-700">
              <Plus className="w-4 h-4 mr-2" />
              Issue ASO
            </Button>
          </DialogTrigger>
          <DialogContent className="sm:max-w-[600px]">
            <DialogHeader>
              <DialogTitle>Issue New ASO</DialogTitle>
              <DialogDescription>
                Issue an Occupational Health Certificate based on a completed
                exam.
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
                    <SelectValue placeholder="Select an exam to issue ASO" />
                  </SelectTrigger>
                  <SelectContent>
                    {exams.map((exam) => (
                      <SelectItem key={exam.id} value={exam.id}>
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
                    {exams.find((e) => e.id === formData.exam) && (
                      <div className="grid grid-cols-2 gap-4 text-sm">
                        <div>
                          <span className="text-gray-500">Employee:</span>
                          <p className="font-medium">
                            {
                              exams.find((e) => e.id === formData.exam)
                                ?.employee
                            }
                          </p>
                        </div>
                        <div>
                          <span className="text-gray-500">Company:</span>
                          <p className="font-medium">
                            {exams.find((e) => e.id === formData.exam)?.company}
                          </p>
                        </div>
                        <div>
                          <span className="text-gray-500">Exam Type:</span>
                          <p className="font-medium">
                            {getExamTypeLabel(
                              exams.find((e) => e.id === formData.exam)?.type ||
                                ""
                            )}
                          </p>
                        </div>
                        <div>
                          <span className="text-gray-500">Result:</span>
                          <div className="mt-1">
                            {getResultBadge(
                              exams.find((e) => e.id === formData.exam)
                                ?.result || "fit"
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
                      placeholder="Enter medical observations for the ASO"
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
                      placeholder="Enter any work restrictions or limitations"
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
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                <FileText className="w-6 h-6 text-blue-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Total ASOs</p>
                <p className="text-2xl font-semibold text-gray-900">
                  {asoCounts.total}
                </p>
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
                <p className="text-sm font-medium text-gray-600">Issued</p>
                <p className="text-2xl font-semibold text-gray-900">
                  {asoCounts.issued}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
                <Clock className="w-6 h-6 text-orange-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">Pending</p>
                <p className="text-2xl font-semibold text-gray-900">
                  {asoCounts.pending}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center space-x-4">
              <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                <FileText className="w-6 h-6 text-purple-600" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-600">This Month</p>
                <p className="text-2xl font-semibold text-gray-900">
                  {asoCounts.thisMonth}
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
                placeholder="Search ASOs by employee, company, or ASO number..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* ASO Table */}
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
                  <TableHead className="text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredASOs.map((aso) => (
                  <TableRow key={aso.id}>
                    <TableCell>
                      <div className="flex items-center space-x-3">
                        <Avatar>
                          <AvatarFallback className="bg-blue-100 text-blue-700">
                            {aso.employee
                              .split(" ")
                              .map((n) => n[0])
                              .join("")
                              .substring(0, 2)}
                          </AvatarFallback>
                        </Avatar>
                        <div>
                          <p className="font-medium text-gray-900">
                            {aso.employee}
                          </p>
                          <p className="text-sm text-gray-500">{aso.company}</p>
                        </div>
                      </div>
                    </TableCell>
                    <TableCell className="font-mono text-sm">
                      {aso.asoNumber}
                    </TableCell>
                    <TableCell>
                      <Badge variant="outline">
                        {getExamTypeLabel(aso.examType)}
                      </Badge>
                    </TableCell>
                    <TableCell>{getResultBadge(aso.result)}</TableCell>
                    <TableCell>
                      {aso.issuanceDate
                        ? new Date(aso.issuanceDate).toLocaleDateString(
                            "en-US",
                            {
                              year: "numeric",
                              month: "short",
                              day: "numeric",
                            }
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
                    <TableCell className="text-right">
                      <div className="flex items-center justify-end space-x-2">
                        {aso.status === "issued" && (
                          <Button
                            variant="ghost"
                            size="sm"
                            className="text-blue-600"
                          >
                            <Download className="w-4 h-4 mr-1" />
                            Download
                          </Button>
                        )}
                        <Button variant="ghost" size="sm">
                          <FileText className="w-4 h-4 mr-1" />
                          Details
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}

                {filteredASOs.length === 0 && (
                  <TableRow>
                    <TableCell
                      colSpan={8}
                      className="text-center text-gray-500 py-6"
                    >
                      No ASO records found.
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
