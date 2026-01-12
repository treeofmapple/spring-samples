export interface Company {
  id: string;
  nome: string;
  cnpj: string;
  endereco: string;
  phone: string;
  quantidade_funcionarios_cliente: string,
  quantidade_funcionarios_clinica: string,
  registration_date: string;
  status: 'active' | 'inactive';
}
