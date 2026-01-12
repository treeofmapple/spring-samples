use axum::http::StatusCode;
use rand::Rng;
use serde_json::json;

use super::{Json, PgPool, State};

use crate::structs::{
    empresa::Empresa, exame::Exame, funcionario_cliente::FuncionarioCliente,
    funcionario_clinica::FuncionarioClinica, risco_ocupacional::RiscoOcupacional,
    risco_ocupacional_exame::RiscoOcupacionalExame, tipo_exame::TipoExame,
};

pub async fn create_empresa(
    State(pool): State<PgPool>,
    Json(data): Json<Empresa>,
) -> Json<Empresa> {
    let result = sqlx::query_as::<_, Empresa>(
        "INSERT INTO empresas (nome, cnpj, endereco, phone, registration_date, status)
         VALUES ($1, $2, $3, $4, $5, $6)
         RETURNING *",
    )
    .bind(data.nome)
    .bind(data.cnpj)
    .bind(data.endereco)
    .bind(data.phone)
    .bind(data.registration_date)
    .bind(data.status)
    .fetch_one(&pool)
    .await
    .unwrap();

    Json(result)
}

pub async fn create_funcionario_cliente(
    State(pool): State<PgPool>,
    Json(mut data): Json<FuncionarioCliente>,
) -> Result<Json<FuncionarioCliente>, (StatusCode, Json<serde_json::Value>)> {
    if data.role.is_none() {
        let mut rng = rand::thread_rng();
        let r = rng.gen_range(0..100);
        data.role = Some(if r < 10 {
            "admin".to_string()
        } else if r < 55 {
            "doctor".to_string()
        } else {
            "employee".to_string()
        });
    }

    let exists = sqlx::query_scalar::<_, bool>(
        "SELECT EXISTS(SELECT 1 FROM funcionario_cliente WHERE cpf = $1)",
    )
    .bind(&data.cpf)
    .fetch_one(&pool)
    .await
    .map_err(|e| {
        (
            StatusCode::INTERNAL_SERVER_ERROR,
            Json(json!({ "error": "Database check failed", "detail": e.to_string() })),
        )
    })?;

    if exists {
        return Err((
            StatusCode::CONFLICT,
            Json(json!({ "error": "CPF already registered" })),
        ));
    }

    let result = sqlx::query_as::<_, FuncionarioCliente>(
        "INSERT INTO funcionario_cliente
        (id_empresa, nome, cpf, senha, data_nascimento, cargo, departamento, admission_date, status, role)
        VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10)
        RETURNING *",
    )
    .bind(&data.id_empresa)
    .bind(&data.nome)
    .bind(&data.cpf)
    .bind(&data.senha)
    .bind(&data.data_nascimento)
    .bind(&data.cargo)
    .bind(&data.departamento)
    .bind(&data.admission_date)
    .bind(&data.status)
    .bind(&data.role)
    .fetch_one(&pool)
    .await
    .map_err(|e| {
        (
            StatusCode::INTERNAL_SERVER_ERROR,
            Json(json!({ "error": "Failed to insert record", "detail": e.to_string() })),
        )
    })?;

    Ok(Json(result))
}

pub async fn create_funcionario_clinica(
    State(pool): State<PgPool>,
    Json(mut data): Json<FuncionarioClinica>,
) -> Json<FuncionarioClinica> {
    if data.role.is_none() {
        let mut rng = rand::thread_rng();
        let r = rng.gen_range(0..100);
        data.role = Some(if r < 10 {
            "admin".to_string()
        } else if r < 55 {
            "doctor".to_string()
        } else {
            "employee".to_string()
        });
    }

    let result = sqlx::query_as::<_, FuncionarioClinica>(
        "INSERT INTO funcionario_clinica (nome, crm, funcao, senha, especialidade, email, phone, data_contratacao, status, role)
         VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10)
         RETURNING *",
    )
    .bind(&data.nome)
    .bind(&data.crm)
    .bind(&data.funcao)
    .bind(&data.senha)
    .bind(&data.especialidade)
    .bind(&data.email)
    .bind(&data.phone)
    .bind(&data.data_contratacao)
    .bind(&data.status)
    .bind(&data.role)
    .fetch_one(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn create_tipo_exame(
    State(pool): State<PgPool>,
    Json(data): Json<TipoExame>,
) -> Json<TipoExame> {
    let result = sqlx::query_as::<_, TipoExame>(
        "INSERT INTO tipos_exames (nome_tipo, descricao) VALUES ($1, $2) RETURNING *",
    )
    .bind(data.nome_tipo)
    .bind(data.descricao)
    .fetch_one(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn create_risco_ocupacional(
    State(pool): State<PgPool>,
    Json(data): Json<RiscoOcupacional>,
) -> Json<RiscoOcupacional> {
    let result = sqlx::query_as::<_, RiscoOcupacional>(
        "INSERT INTO risco_ocupacional (nome_risco, descricao, severity, preventive_measures, risk_category, exposure_level, status)
         VALUES ($1,$2,$3,$4,$5,$6,$7)
         RETURNING *",
    )
    .bind(data.nome_risco)
    .bind(data.descricao)
    .bind(data.severity)
    .bind(data.preventive_measures)
    .bind(data.risk_category)
    .bind(data.exposure_level)
    .bind(data.status)
    .fetch_one(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn create_exame(State(pool): State<PgPool>, Json(data): Json<Exame>) -> Json<Exame> {
    let result = sqlx::query_as::<_, Exame>(
        "INSERT INTO exames (id_funcionario, id_funcionario_clinica, id_tipos_exames, data_exame, type_exam, horario_exame, result, status, observations)
         VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9)
         RETURNING *",
    )
    .bind(data.id_funcionario)
    .bind(data.id_funcionario_clinica)
    .bind(data.id_tipos_exames)
    .bind(data.data_exame)
    .bind(data.type_exam)
    .bind(data.horario_exame)
    .bind(data.result)
    .bind(data.status)
    .bind(data.observations)
    .fetch_one(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn create_risco_exame(
    State(pool): State<PgPool>,
    Json(data): Json<RiscoOcupacionalExame>,
) -> Json<RiscoOcupacionalExame> {
    let result = sqlx::query_as::<_, RiscoOcupacionalExame>(
        "INSERT INTO risco_ocupacional_exames (id_exames, id_risco_ocupacional)
         VALUES ($1,$2)
         RETURNING *",
    )
    .bind(data.id_exames)
    .bind(data.id_risco_ocupacional)
    .fetch_one(&pool)
    .await
    .unwrap();
    Json(result)
}
