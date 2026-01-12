export interface Employee {
  id: string;
  nome: string;
  cpf: string;
  data_nascimento: string;
  id_empresa: string;
  cargo: string;
  department: string;
  admission_date: string; // automatic
  status: 'active' | 'inactive'; // automatic
}
