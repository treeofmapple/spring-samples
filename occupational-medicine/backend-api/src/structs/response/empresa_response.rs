use super::{Deserialize, NaiveDate, Serialize};

#[derive(Serialize, Deserialize, Debug, sqlx::FromRow)]
pub struct EmpresaResponse {
    pub id_empresa: i64,
    pub nome: String,
    pub cnpj: String,
    pub endereco: Option<String>,
    pub phone: Option<String>,
    pub quantidade_funcionarios_cliente: Option<i32>,
    pub quantidade_funcionarios_clinica: Option<i32>,
    pub registration_date: NaiveDate,
    pub status: String,
}
