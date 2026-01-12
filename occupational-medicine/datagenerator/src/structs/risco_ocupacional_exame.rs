use super::{Serialize};

#[derive(Serialize, Debug)]
pub struct RiscoOcupacionalExame {
    pub id_risco_ocupacional_exames: u32,
    pub id_exames: u32,
    pub id_risco_ocupacional: u32,
}
