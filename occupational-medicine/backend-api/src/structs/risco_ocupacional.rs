use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct RiscoOcupacional {
    pub id_risco_ocupacional: Option<i64>,
    pub nome_risco: String,
    pub descricao: String,
    pub severity: String,
    pub preventive_measures: String,
    pub risk_category: String,
    pub exposure_level: String,
    pub status: String,
}
