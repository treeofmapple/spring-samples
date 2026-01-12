use super::Serialize;

#[derive(Serialize, Debug, sqlx::FromRow)]
pub struct ExamsByMonthReport {
    pub month: String,
    pub admission: i64,
    pub periodic: i64,
    pub return_to_work: i64,
    pub dismissal: i64,
}
