pub mod database;
pub mod options;
pub mod routes;

pub use axum::Router;
pub use axum::http::{HeaderValue, Method};
pub use axum::routing::get as Get;
pub use dotenvy::dotenv as Environment;
pub use sqlx::{PgPool, Result as ResultDB, pool as Pools, postgres::PgPoolOptions};
pub use std::env::var as Variable;
pub use tower_http::cors::{Any, CorsLayer};
