use super::Deserialize;

#[derive(Deserialize)]
pub struct ReportFilter {
    pub month: Option<i32>,
    pub year: Option<i32>,
    pub mode: Option<String>,
}
