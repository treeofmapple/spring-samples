use super::{Environment, PgPool, PgPoolOptions, ResultDB, Variable};
use crate::consts::{MAX_DB_CONNECTIONS, MIN_DB_CONNECTIONS};

pub async fn start_db_connection() -> ResultDB<PgPool> {
    Environment().ok();
    let database_url =
        Variable("DATABASE_URL").map_err(|e| sqlx::Error::Configuration(e.into()))?;

    let pool = PgPoolOptions::new()
        .max_connections(MAX_DB_CONNECTIONS)
        .min_connections(MIN_DB_CONNECTIONS)
        .connect(&database_url)
        .await?;
    Ok(pool)
}
