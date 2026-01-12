use super::Serialize;

#[derive(Serialize, Debug, sqlx::FromRow, Clone)]
#[serde(rename_all = "camelCase")]
pub struct FitnessTrend {
    pub month: String,
    pub fit: i64,
    pub fit_with_restrictions: i64,
    pub unfit: i64,
}
