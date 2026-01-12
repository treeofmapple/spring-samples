use super::Serialize;

#[derive(Serialize, Debug, sqlx::FromRow, Clone)]
pub struct RiscoCategoriaReport {
    pub categoria: String,
    pub quantidade: i64,
    pub porcentagem: f64,
}
