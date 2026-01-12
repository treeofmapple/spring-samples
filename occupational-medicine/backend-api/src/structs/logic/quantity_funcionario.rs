use super::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug, sqlx::FromRow)]
pub struct QuantityFuncionario {
    pub quantity_funcionario: i64,
}
