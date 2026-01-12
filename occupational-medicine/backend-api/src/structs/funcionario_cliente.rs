use super::{Deserialize, FromRow, NaiveDate, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct FuncionarioCliente {
    #[sqlx(rename = "id_funcionario")]
    pub id_funcionario: Option<i64>,
    #[sqlx(rename = "id_empresa")]
    pub id_empresa: i64,
    pub nome: String,
    pub cpf: String,
    pub email: Option<String>,
    pub senha: String,
    #[sqlx(rename = "data_nascimento")]
    pub data_nascimento: NaiveDate,
    pub cargo: String,
    pub departamento: String,
    #[sqlx(rename = "admission_date")]
    pub admission_date: NaiveDate,
    pub status: String,
    #[serde(default)]
    pub role: Option<String>,
}
