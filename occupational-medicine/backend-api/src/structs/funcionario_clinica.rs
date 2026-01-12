use super::{Deserialize, FromRow, NaiveDate, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct FuncionarioClinica {
    pub id_funcionario_clinica: Option<i64>,
    pub nome: String,
    pub crm: String,
    pub funcao: String,
    pub senha: String,
    pub especialidade: Option<String>,
    pub email: String,
    pub phone: String,
    pub data_contratacao: NaiveDate,
    pub status: String,
    #[serde(default)]
    pub role: Option<String>,
}
