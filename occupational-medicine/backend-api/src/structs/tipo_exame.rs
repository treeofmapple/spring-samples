use super::{Deserialize, FromRow, Serialize};

#[derive(Serialize, Deserialize, Debug, FromRow)]
pub struct TipoExame {
    pub id_tipos_exames: Option<i64>,
    pub nome_tipo: String,
    pub descricao: Option<String>,
}
