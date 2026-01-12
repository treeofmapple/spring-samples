use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct EquipeClinica {
    pub doctors: String,
    pub employee: String,
    pub total_equipe: String,
}
