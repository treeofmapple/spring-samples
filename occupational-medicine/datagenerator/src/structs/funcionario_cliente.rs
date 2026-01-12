use super::{NaiveDate, Serialize};

#[derive(Serialize, Debug)]
pub struct FuncionarioCliente {
    pub id_funcionario: u32,
    pub id_empresa: u32,
    pub nome: String,
    pub cpf: String,
    pub email: String,
    pub senha: String,
    pub data_nascimento: NaiveDate,
    pub cargo: String,
    pub departamento: String,
    pub admission_date: NaiveDate,
    pub status: String,
    pub role: String,
}
