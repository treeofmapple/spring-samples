use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct Painel {
    pub total_companies: String,
    pub pending_exams: String,
    pub high_risk_cases: String,
}
