use super::{NaiveDate, Serialize};

#[derive(Serialize, Debug)]
pub struct Empresa {
    pub id_empresa: u32,
    pub nome: String,
    pub cnpj: String,
    pub endereco: String,
    pub phone: String,
    pub quantidade_funcionarios_cliente: u32,
    pub quantidade_funcionarios_clinica: u32,
    pub registration_date: NaiveDate,
    pub status: String,
}
