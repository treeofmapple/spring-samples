use super::{NaiveDate, Serialize};

#[derive(Serialize, Debug)]
pub struct Exame {
    pub id_exames: u32,
    pub id_funcionario: u32,
    pub id_funcionario_clinica: u32,
    pub id_tipos_exames: u32,
    pub data_exame: NaiveDate,
    pub type_exam: String,
    pub horario_exame: String,
    pub result: Option<String>,
    pub status: String,
    pub observations: String,
}
