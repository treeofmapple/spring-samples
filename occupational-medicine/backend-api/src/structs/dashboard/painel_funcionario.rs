use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct PainelFuncionario {
    pub empregados_ativos: String,
    pub novos_mes_atual: String,
}
