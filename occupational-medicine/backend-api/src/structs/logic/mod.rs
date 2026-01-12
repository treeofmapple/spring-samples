pub mod risco_categoria_report;
pub mod fitness_trend;
pub mod exams_by_month_report;
pub mod aso_trend_report;
pub mod quantity_funcionario;
pub mod report_filter;

pub use serde::{Serialize, Deserialize};
pub use chrono::{serde as Serde, naive::serde as NaiveSerde, NaiveDate};
pub use sqlx::FromRow;
