use crate::structs::{
    exame::Exame, funcionario_cliente::FuncionarioCliente, funcionario_clinica::FuncionarioClinica,
    response::empresa_response::EmpresaResponse, risco_ocupacional::RiscoOcupacional,
    risco_ocupacional_exame::RiscoOcupacionalExame, tipo_exame::TipoExame,
};

use super::{Json, Path, PgPool, State};

pub async fn get_empresas(State(pool): State<PgPool>) -> Json<Vec<EmpresaResponse>> {
    let result = sqlx::query_as::<_, EmpresaResponse>(
        r#"
        SELECT * FROM empresas ORDER BY id_empresa ASC LIMIT 100
        "#,
    )
    .fetch_all(&pool)
    .await
    .unwrap();

    Json(result)
}

pub async fn get_empresa_by_id(
    State(pool): State<PgPool>,
    Path(id): Path<i32>,
) -> Json<Option<EmpresaResponse>> {
    let result = sqlx::query_as::<_, EmpresaResponse>(
        r#"
        SELECT * FROM empresas WHERE id_empresa = $1
        "#,
    )
    .bind(id)
    .fetch_optional(&pool)
    .await
    .unwrap();

    Json(result)
}

pub async fn get_funcionarios_cliente(State(pool): State<PgPool>) -> Json<Vec<FuncionarioCliente>> {
    let result = sqlx::query_as::<_, FuncionarioCliente>(
        "SELECT * FROM public.funcionario_cliente ORDER BY id_funcionario ASC LIMIT 100",
    )
    .fetch_all(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn get_funcionario_cliente_by_id(
    State(pool): State<PgPool>,
    Path(id): Path<i32>,
) -> Json<Option<FuncionarioCliente>> {
    let result = sqlx::query_as::<_, FuncionarioCliente>(
        "SELECT * FROM public.funcionario_cliente WHERE id_funcionario = $1",
    )
    .bind(id)
    .fetch_optional(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn get_funcionarios_clinica(State(pool): State<PgPool>) -> Json<Vec<FuncionarioClinica>> {
    let result = sqlx::query_as::<_, FuncionarioClinica>(
        "SELECT * FROM funcionario_clinica ORDER BY id_funcionario_clinica ASC LIMIT 100",
    )
    .fetch_all(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn get_funcionario_clinica_by_id(
    State(pool): State<PgPool>,
    Path(id): Path<i32>,
) -> Json<Option<FuncionarioClinica>> {
    let result = sqlx::query_as::<_, FuncionarioClinica>(
        "SELECT * FROM funcionario_clinica WHERE id_funcionario_clinica = $1",
    )
    .bind(id)
    .fetch_optional(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn get_tipos_exame(State(pool): State<PgPool>) -> Json<Vec<TipoExame>> {
    let result = sqlx::query_as::<_, TipoExame>(
        "SELECT * FROM public.tipos_exames ORDER BY id_tipos_exames ASC LIMIT 100",
    )
    .fetch_all(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn get_tipo_exame_by_id(
    State(pool): State<PgPool>,
    Path(id): Path<i64>,
) -> Json<Option<TipoExame>> {
    let result = sqlx::query_as::<_, TipoExame>(
        "SELECT * FROM public.tipos_exames WHERE id_tipos_exames = $1",
    )
    .bind(id)
    .fetch_optional(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn get_riscos_ocupacionais(State(pool): State<PgPool>) -> Json<Vec<RiscoOcupacional>> {
    let result = sqlx::query_as::<_, RiscoOcupacional>(
        "SELECT * FROM public.risco_ocupacional ORDER BY id_risco_ocupacional ASC LIMIT 100",
    )
    .fetch_all(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn get_risco_ocupacional_by_id(
    State(pool): State<PgPool>,
    Path(id): Path<i32>,
) -> Json<Option<RiscoOcupacional>> {
    let result = sqlx::query_as::<_, RiscoOcupacional>(
        "SELECT * FROM public.risco_ocupacional WHERE id_risco_ocupacional = $1",
    )
    .bind(id)
    .fetch_optional(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn get_exames(State(pool): State<PgPool>) -> Json<Vec<Exame>> {
    let result =
        sqlx::query_as::<_, Exame>("SELECT * FROM public.exames ORDER BY id_exames ASC LIMIT 100")
            .fetch_all(&pool)
            .await
            .unwrap();
    Json(result)
}

pub async fn get_exame_by_id(
    State(pool): State<PgPool>,
    Path(id): Path<i32>,
) -> Json<Option<Exame>> {
    let result = sqlx::query_as::<_, Exame>("SELECT * FROM public.exames WHERE id_exames = $1")
        .bind(id)
        .fetch_optional(&pool)
        .await
        .unwrap();
    Json(result)
}

pub async fn get_riscos_exames(State(pool): State<PgPool>) -> Json<Vec<RiscoOcupacionalExame>> {
    let result = sqlx::query_as::<_, RiscoOcupacionalExame>(
        "SELECT * FROM public.risco_ocupacional_exames ORDER BY id_risco_ocupacional_exames ASC LIMIT 100"
    )
    .fetch_all(&pool)
    .await
    .unwrap();
    Json(result)
}

pub async fn get_risco_exame_by_id(
    State(pool): State<PgPool>,
    Path(id): Path<i64>,
) -> Json<Option<RiscoOcupacionalExame>> {
    let result = sqlx::query_as::<_, RiscoOcupacionalExame>(
        "SELECT * FROM public.risco_ocupacional_exames WHERE id_risco_ocupacional_exames = $1",
    )
    .bind(id)
    .fetch_optional(&pool)
    .await
    .unwrap();
    Json(result)
}
