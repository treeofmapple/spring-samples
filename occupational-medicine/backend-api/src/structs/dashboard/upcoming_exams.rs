use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct UpcomingExams {
    pub nome: String,
    pub nome_empresa: String,
    pub nome_exame: String,
    pub horario_exame: String,
    pub dia_exame: String,
}
