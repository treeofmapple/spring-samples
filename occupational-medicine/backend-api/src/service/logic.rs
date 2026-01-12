use crate::{
    errorhandler::app_error::AppError,
    structs::logic::{
        aso_trend_report::AsoTrendReport, exams_by_month_report::ExamsByMonthReport,
        fitness_trend::FitnessTrend, quantity_funcionario::QuantityFuncionario,
        report_filter::ReportFilter, risco_categoria_report::RiscoCategoriaReport,
    },
};

use super::{PgPool, Row};
use axum::{
    Json,
    extract::{Query, State},
};

#[axum::debug_handler]
pub async fn relatorio_risco_categoria(
    State(pool): State<PgPool>,
    Query(filter): Query<ReportFilter>,
) -> Result<Json<Vec<RiscoCategoriaReport>>, AppError> {
    let is_yearly = filter
        .mode
        .as_deref()
        .map(|m| m.eq_ignore_ascii_case("yearly"))
        .unwrap_or(false);

    let query = if is_yearly {
        r#"
        SELECT
            r.risk_category AS categoria,
            COUNT(*)::BIGINT AS quantidade,
            (COUNT(*) * 100.0 / SUM(COUNT(*)) OVER ())::FLOAT8 AS porcentagem
        FROM risco_ocupacional r
        JOIN risco_ocupacional_exames re ON re.id_risco_ocupacional = r.id_risco_ocupacional
        JOIN exames e ON e.id_exames = re.id_exames
        WHERE ($1::INT IS NULL OR EXTRACT(YEAR FROM e.data_exame) = $1)
        GROUP BY r.risk_category
        ORDER BY quantidade DESC
        "#
    } else {
        r#"
        SELECT
            r.risk_category AS categoria,
            COUNT(*)::BIGINT AS quantidade,
            (COUNT(*) * 100.0 / SUM(COUNT(*)) OVER ())::FLOAT8 AS porcentagem
        FROM risco_ocupacional r
        JOIN risco_ocupacional_exames re ON re.id_risco_ocupacional = r.id_risco_ocupacional
        JOIN exames e ON e.id_exames = re.id_exames
        WHERE
            ($1::INT IS NULL OR EXTRACT(MONTH FROM e.data_exame) = $1)
            AND ($2::INT IS NULL OR EXTRACT(YEAR FROM e.data_exame) = $2)
        GROUP BY r.risk_category
        ORDER BY quantidade DESC
        "#
    };

    let mut q = sqlx::query(query);
    if is_yearly {
        q = q.bind(filter.year);
    } else {
        q = q.bind(filter.month).bind(filter.year);
    }

    let rows = q.fetch_all(&pool).await?;
    let result = rows
        .into_iter()
        .map(|r| RiscoCategoriaReport {
            categoria: r.get::<String, _>("categoria"),
            quantidade: r.get::<i64, _>("quantidade"),
            porcentagem: r.get::<f64, _>("porcentagem"),
        })
        .collect();

    Ok(Json(result))
}

#[axum::debug_handler]
pub async fn exames_por_mes(
    State(pool): State<PgPool>,
    Query(filter): Query<ReportFilter>,
) -> Result<Json<Vec<ExamsByMonthReport>>, AppError> {
    let is_yearly = filter
        .mode
        .as_deref()
        .map(|m| m.eq_ignore_ascii_case("yearly"))
        .unwrap_or(false);

    let query = if is_yearly {
        r#"
        SELECT
            TO_CHAR(data_exame, 'YYYY') AS period,
            SUM(CASE WHEN type_exam = 'admission' THEN 1 ELSE 0 END)::BIGINT AS admission,
            SUM(CASE WHEN type_exam = 'periodic' THEN 1 ELSE 0 END)::BIGINT AS periodic,
            SUM(CASE WHEN type_exam = 'return_to_work' THEN 1 ELSE 0 END)::BIGINT AS return_to_work,
            SUM(CASE WHEN type_exam = 'dismissal' THEN 1 ELSE 0 END)::BIGINT AS dismissal
        FROM exames
        WHERE
            ($1::INT IS NULL OR EXTRACT(YEAR FROM data_exame) = $1)
        GROUP BY TO_CHAR(data_exame, 'YYYY')
        ORDER BY TO_CHAR(data_exame, 'YYYY')
        "#
    } else {
        r#"
        SELECT
            TO_CHAR(data_exame, 'Mon') AS period,
            SUM(CASE WHEN type_exam = 'admission' THEN 1 ELSE 0 END)::BIGINT AS admission,
            SUM(CASE WHEN type_exam = 'periodic' THEN 1 ELSE 0 END)::BIGINT AS periodic,
            SUM(CASE WHEN type_exam = 'return_to_work' THEN 1 ELSE 0 END)::BIGINT AS return_to_work,
            SUM(CASE WHEN type_exam = 'dismissal' THEN 1 ELSE 0 END)::BIGINT AS dismissal
        FROM exames
        WHERE
            ($1::INT IS NULL OR EXTRACT(MONTH FROM data_exame) = $1)
            AND ($2::INT IS NULL OR EXTRACT(YEAR FROM data_exame) = $2)
        GROUP BY TO_CHAR(data_exame, 'Mon'), EXTRACT(MONTH FROM data_exame)
        ORDER BY EXTRACT(MONTH FROM data_exame)
        "#
    };

    let mut q = sqlx::query(query);

    if is_yearly {
        q = q.bind(filter.year);
    } else {
        q = q.bind(filter.month).bind(filter.year);
    }

    let rows = q.fetch_all(&pool).await?;

    let result = rows
        .into_iter()
        .map(|r| ExamsByMonthReport {
            month: r.get::<String, _>("period"),
            admission: r.get::<i64, _>("admission"),
            periodic: r.get::<i64, _>("periodic"),
            return_to_work: r.get::<i64, _>("return_to_work"),
            dismissal: r.get::<i64, _>("dismissal"),
        })
        .collect();

    Ok(Json(result))
}

#[axum::debug_handler]
pub async fn aso_trend(
    State(pool): State<PgPool>,
    Query(filter): Query<ReportFilter>,
) -> Result<Json<Vec<AsoTrendReport>>, AppError> {
    let is_yearly = filter
        .mode
        .as_deref()
        .map(|m| m.eq_ignore_ascii_case("yearly"))
        .unwrap_or(false);

    let query = if is_yearly {
        r#"
        SELECT
            TO_CHAR(data_exame, 'YYYY') AS period,
            SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END)::BIGINT AS completed,
            SUM(CASE WHEN status = 'issued' THEN 1 ELSE 0 END)::BIGINT AS issued,
            SUM(CASE WHEN status IN ('scheduled', 'in_progress') THEN 1 ELSE 0 END)::BIGINT AS pending
        FROM exames
        WHERE ($1::INT IS NULL OR EXTRACT(YEAR FROM data_exame) = $1)
        GROUP BY TO_CHAR(data_exame, 'YYYY')
        ORDER BY TO_CHAR(data_exame, 'YYYY')
        "#
    } else {
        r#"
        SELECT
            TO_CHAR(data_exame, 'Mon') AS period,
            SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END)::BIGINT AS completed,
            SUM(CASE WHEN status = 'issued' THEN 1 ELSE 0 END)::BIGINT AS issued,
            SUM(CASE WHEN status IN ('scheduled', 'in_progress') THEN 1 ELSE 0 END)::BIGINT AS pending
        FROM exames
        WHERE
            ($1::INT IS NULL OR EXTRACT(MONTH FROM data_exame) = $1)
            AND ($2::INT IS NULL OR EXTRACT(YEAR FROM data_exame) = $2)
        GROUP BY TO_CHAR(data_exame, 'Mon'), EXTRACT(MONTH FROM data_exame)
        ORDER BY EXTRACT(MONTH FROM data_exame)
        "#
    };

    let mut q = sqlx::query(query);
    if is_yearly {
        q = q.bind(filter.year);
    } else {
        q = q.bind(filter.month).bind(filter.year);
    }

    let rows = q.fetch_all(&pool).await?;
    let result = rows
        .into_iter()
        .map(|r| AsoTrendReport {
            month: r.get::<String, _>("period"),
            complete: r.get::<i64, _>("completed"),
            issued: r.get::<i64, _>("issued"),
            pending: r.get::<i64, _>("pending"),
        })
        .collect();

    Ok(Json(result))
}

#[axum::debug_handler]
pub async fn fitness_trend(
    State(pool): State<PgPool>,
    Query(filter): Query<ReportFilter>,
) -> Result<Json<Vec<FitnessTrend>>, AppError> {
    let is_yearly = filter
        .mode
        .as_deref()
        .map(|m| m.eq_ignore_ascii_case("yearly"))
        .unwrap_or(false);

    let query = if is_yearly {
        r#"
        SELECT
            TO_CHAR(data_exame, 'YYYY') AS period,
            SUM(CASE WHEN result = 'fit' THEN 1 ELSE 0 END)::BIGINT AS fit,
            SUM(CASE WHEN result = 'fit_with_restrictions' THEN 1 ELSE 0 END)::BIGINT AS fit_with_restrictions,
            SUM(CASE WHEN result = 'unfit' THEN 1 ELSE 0 END)::BIGINT AS unfit
        FROM exames
        WHERE
            result IS NOT NULL
            AND ($1::INT IS NULL OR EXTRACT(YEAR FROM data_exame) = $1)
        GROUP BY TO_CHAR(data_exame, 'YYYY')
        ORDER BY TO_CHAR(data_exame, 'YYYY')
        "#
    } else {
        r#"
        SELECT
            TO_CHAR(data_exame, 'Mon') AS period,
            SUM(CASE WHEN result = 'fit' THEN 1 ELSE 0 END)::BIGINT AS fit,
            SUM(CASE WHEN result = 'fit_with_restrictions' THEN 1 ELSE 0 END)::BIGINT AS fit_with_restrictions,
            SUM(CASE WHEN result = 'unfit' THEN 1 ELSE 0 END)::BIGINT AS unfit
        FROM exames
        WHERE
            result IS NOT NULL
            AND ($1::INT IS NULL OR EXTRACT(MONTH FROM data_exame) = $1)
            AND ($2::INT IS NULL OR EXTRACT(YEAR FROM data_exame) = $2)
        GROUP BY TO_CHAR(data_exame, 'Mon'), EXTRACT(MONTH FROM data_exame)
        ORDER BY EXTRACT(MONTH FROM data_exame)
        "#
    };

    let mut q = sqlx::query(query);
    if is_yearly {
        q = q.bind(filter.year);
    } else {
        q = q.bind(filter.month).bind(filter.year);
    }

    let rows = q.fetch_all(&pool).await?;
    let result = rows
        .into_iter()
        .map(|r| FitnessTrend {
            month: r.get::<String, _>("period"),
            fit: r.get::<i64, _>("fit"),
            fit_with_restrictions: r.get::<i64, _>("fit_with_restrictions"),
            unfit: r.get::<i64, _>("unfit"),
        })
        .collect();

    Ok(Json(result))
}

#[axum::debug_handler]
pub async fn get_total_funcionarios(
    State(pool): State<PgPool>,
    Query(filter): Query<ReportFilter>,
) -> Result<Json<QuantityFuncionario>, AppError> {
    let is_yearly = filter
        .mode
        .as_deref()
        .map(|m| m.eq_ignore_ascii_case("yearly"))
        .unwrap_or(false);

    let query = if is_yearly {
        r#"
        SELECT
            (
                (SELECT COUNT(*) FROM funcionario_cliente WHERE ($1::INT IS NULL OR EXTRACT(YEAR FROM admission_date) = $1))
                +
                (SELECT COUNT(*) FROM funcionario_clinica WHERE ($1::INT IS NULL OR EXTRACT(YEAR FROM data_contratacao) = $1))
            )::BIGINT AS quantity_funcionario
        "#
    } else {
        r#"
        SELECT
            (
                (SELECT COUNT(*) FROM funcionario_cliente
                    WHERE ($1::INT IS NULL OR EXTRACT(MONTH FROM admission_date) = $1)
                    AND ($2::INT IS NULL OR EXTRACT(YEAR FROM admission_date) = $2))
                +
                (SELECT COUNT(*) FROM funcionario_clinica
                    WHERE ($1::INT IS NULL OR EXTRACT(MONTH FROM data_contratacao) = $1)
                    AND ($2::INT IS NULL OR EXTRACT(YEAR FROM data_contratacao) = $2))
            )::BIGINT AS quantity_funcionario
        "#
    };

    let mut q = sqlx::query(query);
    if is_yearly {
        q = q.bind(filter.year);
    } else {
        q = q.bind(filter.month).bind(filter.year);
    }

    let row = q.fetch_one(&pool).await?;
    let result = QuantityFuncionario {
        quantity_funcionario: row.get::<i64, _>("quantity_funcionario"),
    };

    Ok(Json(result))
}
