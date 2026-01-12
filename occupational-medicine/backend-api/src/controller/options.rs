use super::{Any, CorsLayer, HeaderValue, Method};

use crate::consts::CORS_AUTHORIZED;

pub fn insert_cors_config() -> CorsLayer {
    CorsLayer::new()
        .allow_origin(HeaderValue::from_static(CORS_AUTHORIZED))
        .allow_methods([Method::GET, Method::POST, Method::PUT, Method::DELETE])
        .allow_headers(Any)
}
