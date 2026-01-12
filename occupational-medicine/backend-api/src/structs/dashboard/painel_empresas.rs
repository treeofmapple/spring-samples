use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct PainelEmpresa {
    pub total_empresas: String,
    pub active_companies: String,
}
