use crate::errorhandler::app_error::AppError;
use axum::{
    extract::State,
    http::{HeaderMap, HeaderValue, header},
    response::IntoResponse,
};
use sqlx::{PgPool, Row};

#[axum::debug_handler]
pub async fn export_aso_resume(State(pool): State<PgPool>) -> Result<impl IntoResponse, AppError> {
    let query = r#"
        SELECT
            TO_CHAR(data_exame, 'YYYY-MM') AS month,
            SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END)::BIGINT AS complete,
            SUM(CASE WHEN status = 'issued' THEN 1 ELSE 0 END)::BIGINT AS issued,
            SUM(CASE WHEN status IN ('scheduled', 'in_progress') THEN 1 ELSE 0 END)::BIGINT AS pending
        FROM exames
        GROUP BY TO_CHAR(data_exame, 'YYYY-MM'), EXTRACT(YEAR FROM data_exame), EXTRACT(MONTH FROM data_exame)
        ORDER BY EXTRACT(YEAR FROM data_exame), EXTRACT(MONTH FROM data_exame)
    "#;

    let rows = sqlx::query(query)
        .fetch_all(&pool)
        .await
        .map_err(AppError::from)?;

    let mut wtr = csv::Writer::from_writer(vec![]);
    wtr.write_record(&["month", "complete", "issued", "pending"])
        .map_err(|e| AppError::from(anyhow::anyhow!(e)))?;

    for r in rows {
        wtr.write_record(&[
            r.get::<String, _>("month"),
            r.get::<i64, _>("complete").to_string(),
            r.get::<i64, _>("issued").to_string(),
            r.get::<i64, _>("pending").to_string(),
        ])
        .map_err(|e| AppError::from(anyhow::anyhow!(e)))?;
    }

    let csv_bytes = wtr
        .into_inner()
        .map_err(|e| AppError::from(anyhow::anyhow!(e)))?;

    let mut headers = HeaderMap::new();
    headers.insert(
        header::CONTENT_TYPE,
        HeaderValue::from_static("text/csv; charset=utf-8"),
    );
    headers.insert(
        header::CONTENT_DISPOSITION,
        HeaderValue::from_static("attachment; filename=\"aso_resume_export.csv\""),
    );

    Ok((headers, csv_bytes).into_response())
}
#[axum::debug_handler]
pub async fn export_reports(State(pool): State<PgPool>) -> Result<impl IntoResponse, AppError> {
    let mut wtr = csv::WriterBuilder::new()
        .flexible(true)
        .from_writer(vec![]);
    let empty_record: &[&str] = &[];

    wtr.write_record(&["=== ASO Trend ==="]).unwrap();
    wtr.write_record(&["period", "completed", "issued", "pending"])
        .unwrap();

    let aso_rows = sqlx::query(
        r#"
        SELECT
            TO_CHAR(data_exame, 'YYYY-MM') AS period,
            SUM(CASE WHEN status = 'completed' THEN 1 ELSE 0 END)::BIGINT AS completed,
            SUM(CASE WHEN status = 'issued' THEN 1 ELSE 0 END)::BIGINT AS issued,
            SUM(CASE WHEN status IN ('scheduled', 'in_progress') THEN 1 ELSE 0 END)::BIGINT AS pending
        FROM exames
        GROUP BY TO_CHAR(data_exame, 'YYYY-MM')
        ORDER BY TO_CHAR(data_exame, 'YYYY-MM')
        "#,
    )
    .fetch_all(&pool)
    .await?;

    for r in aso_rows {
        let _ = wtr.write_record(&[
            r.get::<String, _>("period"),
            r.get::<i64, _>("completed").to_string(),
            r.get::<i64, _>("issued").to_string(),
            r.get::<i64, _>("pending").to_string(),
        ]);
    }

    wtr.write_record(empty_record).unwrap();
    wtr.write_record(&["=== Exams by Month ==="]).unwrap();
    wtr.write_record(&[
        "period",
        "admission",
        "periodic",
        "return_to_work",
        "dismissal",
    ])
    .unwrap();

    let exams_rows = sqlx::query(
        r#"
        SELECT
            TO_CHAR(data_exame, 'YYYY-MM') AS period,
            SUM(CASE WHEN type_exam = 'admission' THEN 1 ELSE 0 END)::BIGINT AS admission,
            SUM(CASE WHEN type_exam = 'periodic' THEN 1 ELSE 0 END)::BIGINT AS periodic,
            SUM(CASE WHEN type_exam = 'return_to_work' THEN 1 ELSE 0 END)::BIGINT AS return_to_work,
            SUM(CASE WHEN type_exam = 'dismissal' THEN 1 ELSE 0 END)::BIGINT AS dismissal
        FROM exames
        GROUP BY TO_CHAR(data_exame, 'YYYY-MM')
        ORDER BY TO_CHAR(data_exame, 'YYYY-MM')
        "#,
    )
    .fetch_all(&pool)
    .await?;

    for r in exams_rows {
        let _ = wtr.write_record(&[
            r.get::<String, _>("period"),
            r.get::<i64, _>("admission").to_string(),
            r.get::<i64, _>("periodic").to_string(),
            r.get::<i64, _>("return_to_work").to_string(),
            r.get::<i64, _>("dismissal").to_string(),
        ]);
    }

    wtr.write_record(empty_record).unwrap();
    wtr.write_record(&["=== Fitness Trend ==="]).unwrap();
    wtr.write_record(&["period", "fit", "fit_with_restrictions", "unfit"])
        .unwrap();

    let fitness_rows = sqlx::query(
        r#"
        SELECT
            TO_CHAR(data_exame, 'YYYY-MM') AS period,
            SUM(CASE WHEN result = 'fit' THEN 1 ELSE 0 END)::BIGINT AS fit,
            SUM(CASE WHEN result = 'fit_with_restrictions' THEN 1 ELSE 0 END)::BIGINT AS fit_with_restrictions,
            SUM(CASE WHEN result = 'unfit' THEN 1 ELSE 0 END)::BIGINT AS unfit
        FROM exames
        WHERE result IS NOT NULL
        GROUP BY TO_CHAR(data_exame, 'YYYY-MM')
        ORDER BY TO_CHAR(data_exame, 'YYYY-MM')
        "#,
    )
    .fetch_all(&pool)
    .await?;

    for r in fitness_rows {
        let _ = wtr.write_record(&[
            r.get::<String, _>("period"),
            r.get::<i64, _>("fit").to_string(),
            r.get::<i64, _>("fit_with_restrictions").to_string(),
            r.get::<i64, _>("unfit").to_string(),
        ]);
    }

    wtr.write_record(empty_record).unwrap();
    wtr.write_record(&["=== Risco Categoria ==="]).unwrap();
    wtr.write_record(&["categoria", "quantidade", "porcentagem"])
        .unwrap();

    let risco_rows = sqlx::query(
        r#"
        SELECT
            r.risk_category AS categoria,
            COUNT(*)::BIGINT AS quantidade,
            (COUNT(*) * 100.0 / SUM(COUNT(*)) OVER ())::FLOAT8 AS porcentagem
        FROM risco_ocupacional r
        JOIN risco_ocupacional_exames re ON re.id_risco_ocupacional = r.id_risco_ocupacional
        JOIN exames e ON e.id_exames = re.id_exames
        GROUP BY r.risk_category
        ORDER BY quantidade DESC
        "#,
    )
    .fetch_all(&pool)
    .await?;

    for r in risco_rows {
        let _ = wtr.write_record(&[
            r.get::<String, _>("categoria"),
            r.get::<i64, _>("quantidade").to_string(),
            format!("{:.2}", r.get::<f64, _>("porcentagem")),
        ]);
    }

    wtr.write_record(empty_record).unwrap();
    wtr.write_record(&["=== Total Funcionarios ==="]).unwrap();
    wtr.write_record(&["quantity_funcionario"]).unwrap();

    let func_row = sqlx::query(
        r#"
        SELECT (
            (SELECT COUNT(*) FROM funcionario_cliente)
            +
            (SELECT COUNT(*) FROM funcionario_clinica)
        )::BIGINT AS quantity_funcionario
        "#,
    )
    .fetch_one(&pool)
    .await?;

    wtr.write_record(&[func_row.get::<i64, _>("quantity_funcionario").to_string()])
        .unwrap();

    let csv_bytes = wtr.into_inner().unwrap();

    let mut headers = HeaderMap::new();
    headers.insert(
        header::CONTENT_TYPE,
        HeaderValue::from_static("text/csv; charset=utf-8"),
    );
    headers.insert(
        header::CONTENT_DISPOSITION,
        HeaderValue::from_static("attachment; filename=\"full_report_export.csv\""),
    );

    Ok((headers, csv_bytes).into_response())
}
