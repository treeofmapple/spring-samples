use super::{Serialize};

#[derive(Serialize, Debug)]
pub struct TipoExame {
    pub id_tipos_exames: u32,
    pub nome_tipo: String,
    pub descricao: String,
}
