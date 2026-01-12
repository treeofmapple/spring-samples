use serde::Serialize;

#[derive(Serialize, Debug, sqlx::FromRow)]
pub struct AsoResumeExport {
    pub month: String,
    pub complete: i64,
    pub issued: i64,
    pub pending: i64,
}
