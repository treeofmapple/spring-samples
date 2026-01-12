use super::{Deserialize, FromRow, NaiveDate, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct Empresa {
    pub id_empresa: Option<i64>,
    pub nome: String,
    pub cnpj: String,
    pub endereco: String,
    pub phone: String,
    pub registration_date: NaiveDate,
    pub status: String,
}
