export interface CompanyRegistrationInterface {
  nome: string;
  cnpj: string;
  endereco: string;
  phone: string;
  registration_date: string;
  status: 'active' | 'inactive';
}
