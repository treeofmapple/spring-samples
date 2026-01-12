use super::{NaiveDate, Serialize};

#[derive(Serialize, Debug)]
pub struct FuncionarioClinica {
    pub id_funcionario_clinica: u32,
    pub nome: String,
    pub crm: String,
    pub funcao: String,
    pub senha: String,
    pub especialidade: String,
    pub email: String,
    pub phone: String,
    pub data_contratacao: NaiveDate,
    pub status: String,
    pub role: String,
}
