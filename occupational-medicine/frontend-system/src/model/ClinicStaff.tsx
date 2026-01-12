export interface ClinicStaff {
  id: string;
  nome: string;
  role: 'doctor' | 'employee' | 'admin';
  crm: string;
  especialidade?: string;
  email: string;
  phone: string;
  data_contratacao: string;
  status: 'active' | 'inactive';
};
