use super::{Deserialize, FromRow, NaiveDate, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct Exame {
    pub id_exames: Option<i64>,
    pub id_funcionario: i64,
    pub id_funcionario_clinica: i64,
    pub id_tipos_exames: i64,
    pub data_exame: NaiveDate,
    pub type_exam: String,
    pub horario_exame: String,
    pub result: Option<String>,
    pub status: String,
    pub observations: String,
}
