use crate::{
    errorhandler::app_error::AppError,
    structs::dashboard::{
        equipe_clinica::EquipeClinica, exames_medicos::ExamesMedicos, painel::Painel,
        painel_empresas::PainelEmpresa, painel_funcionario::PainelFuncionario,
        riscos_ocupacionais::RiscosOcupacionais, upcoming_exams::UpcomingExams,
    },
};

use super::{PgPool, Row};
use axum::{Json, extract::State};

#[axum::debug_handler]
pub async fn dashboard_painel(State(pool): State<PgPool>) -> Result<Json<Painel>, AppError> {
    let total_companies_row = sqlx::query("SELECT COUNT(*)::BIGINT AS cnt FROM empresas")
        .fetch_one(&pool)
        .await?;
    let total_companies: i64 = total_companies_row.get::<i64, _>("cnt");

    let pending_row = sqlx::query(
        "SELECT COUNT(*)::BIGINT AS cnt FROM exames WHERE status IN ('scheduled', 'in_progress')",
    )
    .fetch_one(&pool)
    .await?;
    let pending_exams: i64 = pending_row.get::<i64, _>("cnt");

    let high_risk_row = sqlx::query(
        r#"
        SELECT COUNT(*)::BIGINT AS cnt
        FROM risco_ocupacional
        WHERE
            LOWER(severity) = 'high'
            OR LOWER(exposure_level) = 'high'
        "#,
    )
    .fetch_one(&pool)
    .await?;
    let high_risk_cases: i64 = high_risk_row.get::<i64, _>("cnt");

    let payload = Painel {
        total_companies: total_companies.to_string(),
        pending_exams: pending_exams.to_string(),
        high_risk_cases: high_risk_cases.to_string(),
    };

    Ok(Json(payload))
}

#[axum::debug_handler]
pub async fn dashboard_empresas(
    State(pool): State<PgPool>,
) -> Result<Json<PainelEmpresa>, AppError> {
    let total_row = sqlx::query("SELECT COUNT(*)::BIGINT AS cnt FROM empresas")
        .fetch_one(&pool)
        .await?;
    let total = total_row.get::<i64, _>("cnt");

    let active_row =
        sqlx::query("SELECT COUNT(*)::BIGINT AS cnt FROM empresas WHERE lower(status) = 'active'")
            .fetch_one(&pool)
            .await?;
    let active = active_row.get::<i64, _>("cnt");

    let payload = PainelEmpresa {
        total_empresas: total.to_string(),
        active_companies: active.to_string(),
    };

    Ok(Json(payload))
}

#[axum::debug_handler]
pub async fn dashboard_funcionarios(
    State(pool): State<PgPool>,
) -> Result<Json<PainelFuncionario>, AppError> {
    let active_row = sqlx::query(
        "SELECT
            ((SELECT COUNT(*) FROM funcionario_cliente WHERE lower(status) = 'active')
            +
            (SELECT COUNT(*) FROM funcionario_clinica WHERE lower(status) = 'active'))::BIGINT AS cnt"
    )
    .fetch_one(&pool)
    .await?;
    let empregados_ativos = active_row.get::<i64, _>("cnt");

    let novos_row = sqlx::query(
        r#"
        SELECT
            (
                (SELECT COUNT(*) FROM funcionario_cliente WHERE date_trunc('month', admission_date) = date_trunc('month', CURRENT_DATE))
                +
                (SELECT COUNT(*) FROM funcionario_clinica WHERE date_trunc('month', data_contratacao) = date_trunc('month', CURRENT_DATE))
            )::BIGINT AS cnt
        "#
    )
    .fetch_one(&pool)
    .await?;
    let novos_mes_atual = novos_row.get::<i64, _>("cnt");

    let payload = PainelFuncionario {
        empregados_ativos: empregados_ativos.to_string(),
        novos_mes_atual: novos_mes_atual.to_string(),
    };

    Ok(Json(payload))
}

#[axum::debug_handler]
pub async fn dashboard_clinica(
    State(pool): State<PgPool>,
) -> Result<Json<EquipeClinica>, AppError> {
    let total_row = sqlx::query("SELECT COUNT(*)::BIGINT AS cnt FROM funcionario_clinica")
        .fetch_one(&pool)
        .await?;
    let total = total_row.get::<i64, _>("cnt");

    let doctors_row = sqlx::query(
        r#"
        SELECT COUNT(*)::BIGINT AS cnt
        FROM funcionario_clinica
        WHERE LOWER(role) = 'doctor'
        "#,
    )
    .fetch_one(&pool)
    .await?;
    let doctors = doctors_row.get::<i64, _>("cnt");

    let employee_row = sqlx::query(
        r#"
        SELECT COUNT(*)::BIGINT AS cnt
        FROM funcionario_clinica
        WHERE LOWER(role) = 'employee'
        "#,
    )
    .fetch_one(&pool)
    .await?;
    let employees = employee_row.get::<i64, _>("cnt");

    let payload = EquipeClinica {
        doctors: doctors.to_string(),
        employee: employees.to_string(),
        total_equipe: total.to_string(),
    };

    Ok(Json(payload))
}

#[axum::debug_handler]
pub async fn dashboard_exames(State(pool): State<PgPool>) -> Result<Json<ExamesMedicos>, AppError> {
    let scheduled_row = sqlx::query("SELECT COUNT(*)::BIGINT AS cnt FROM exames WHERE lower(status) IN ('scheduled', 'in_progress')")
        .fetch_one(&pool)
        .await?;
    let scheduled = scheduled_row.get::<i64, _>("cnt");

    let completed_row =
        sqlx::query("SELECT COUNT(*)::BIGINT AS cnt FROM exames WHERE lower(status) = 'completed'")
            .fetch_one(&pool)
            .await?;
    let completed = completed_row.get::<i64, _>("cnt");

    let today_row =
        sqlx::query("SELECT COUNT(*)::BIGINT AS cnt FROM exames WHERE data_exame = CURRENT_DATE")
            .fetch_one(&pool)
            .await?;
    let today = today_row.get::<i64, _>("cnt");

    let payload = ExamesMedicos {
        scheduled: scheduled.to_string(),
        completed: completed.to_string(),
        today: today.to_string(),
    };

    Ok(Json(payload))
}

#[axum::debug_handler]
pub async fn dashboard_riscos(
    State(pool): State<PgPool>,
) -> Result<Json<RiscosOcupacionais>, AppError> {
    let total_row = sqlx::query("SELECT COUNT(*)::BIGINT AS cnt FROM risco_ocupacional")
        .fetch_one(&pool)
        .await?;
    let total = total_row.get::<i64, _>("cnt");

    let active_row = sqlx::query(
        "SELECT COUNT(*)::BIGINT AS cnt FROM risco_ocupacional WHERE lower(status) = 'active'",
    )
    .fetch_one(&pool)
    .await?;
    let active = active_row.get::<i64, _>("cnt");

    let critical_row = sqlx::query(
        "SELECT COUNT(*)::BIGINT AS cnt FROM risco_ocupacional WHERE lower(severity) = 'high'",
    )
    .fetch_one(&pool)
    .await?;
    let critical = critical_row.get::<i64, _>("cnt");

    let payload = RiscosOcupacionais {
        total_risks: total.to_string(),
        active_risks: active.to_string(),
        critical: critical.to_string(),
    };

    Ok(Json(payload))
}

#[axum::debug_handler]
pub async fn dashboard_upcoming(
    State(pool): State<PgPool>,
) -> Result<Json<Vec<UpcomingExams>>, AppError> {
    let query = r#"
        SELECT
            fc.nome,
            e.nome AS nome_empresa,
            te.nome_tipo AS nome_exame,
            ex.horario_exame,
            TO_CHAR(ex.data_exame, 'YYYY-MM-DD') AS dia_exame
        FROM exames ex
        JOIN funcionario_cliente fc ON ex.id_funcionario = fc.id_funcionario
        JOIN empresas e ON fc.id_empresa = e.id_empresa
        JOIN tipos_exames te ON ex.id_tipos_exames = te.id_tipos_exames
        ORDER BY ex.data_exame DESC
        LIMIT 10;
    "#;

    let upcoming_exams = sqlx::query_as::<_, UpcomingExams>(query)
        .fetch_all(&pool)
        .await?;

    Ok(Json(upcoming_exams))
}
