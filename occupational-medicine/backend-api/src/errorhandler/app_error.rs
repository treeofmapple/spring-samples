use axum::{
    http::StatusCode,
    response::{IntoResponse, Response},
};
use std::{env, fmt};

#[derive(Debug)]
pub struct AppError(anyhow::Error);

impl fmt::Display for AppError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "{}", self.0)
    }
}

impl std::error::Error for AppError {
    fn source(&self) -> Option<&(dyn std::error::Error + 'static)> {
        Some(self.0.as_ref())
    }
}

impl IntoResponse for AppError {
    fn into_response(self) -> Response {
        (
            StatusCode::INTERNAL_SERVER_ERROR,
            format!("Something went wrong: {}", self.0),
        )
            .into_response()
    }
}

impl From<sqlx::Error> for AppError {
    fn from(err: sqlx::Error) -> Self {
        Self(anyhow::Error::new(err))
    }
}

impl From<serde_json::Error> for AppError {
    fn from(err: serde_json::Error) -> Self {
        Self(anyhow::Error::new(err))
    }
}

impl From<env::VarError> for AppError {
    fn from(err: env::VarError) -> Self {
        Self(anyhow::Error::new(err))
    }
}

impl From<axum_core::Error> for AppError {
    fn from(err: axum_core::Error) -> Self {
        Self(anyhow::Error::new(err))
    }
}

impl From<anyhow::Error> for AppError {
    fn from(err: anyhow::Error) -> Self {
        Self(err)
    }
}
