use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct RiscoOcupacionalExame {
    pub id_risco_ocupacional_exames: Option<i64>,
    pub id_exames: i64,
    pub id_risco_ocupacional: i64,
}
