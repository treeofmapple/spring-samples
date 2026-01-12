use super::{Serialize};

#[derive(Serialize, Debug)]
pub struct RiscoOcupacional {
    pub id_risco_ocupacional: u32,
    pub nome_risco: String,
    pub descricao: String,
    pub severity: String,
    pub preventive_measures: String,
    pub risk_category: String,
    pub exposure_level: String,
    pub status: String,
}
