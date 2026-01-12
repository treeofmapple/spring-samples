use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct ExamesMedicos {
    pub scheduled: String,
    pub completed: String,
    pub today: String,
}
