use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct RiscosOcupacionais {
    pub total_risks: String,
    pub active_risks: String,
    pub critical: String,
}
