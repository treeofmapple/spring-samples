use crate::{
    controller::{database::start_db_connection, options::insert_cors_config},
    service::{
        create::{
            create_empresa, create_exame, create_funcionario_cliente, create_funcionario_clinica,
            create_risco_exame, create_risco_ocupacional, create_tipo_exame,
        },
        dashboard::{
            dashboard_clinica, dashboard_empresas, dashboard_exames, dashboard_funcionarios,
            dashboard_painel, dashboard_riscos, dashboard_upcoming,
        },
        get::{
            get_empresa_by_id, get_empresas, get_exame_by_id, get_exames,
            get_funcionario_cliente_by_id, get_funcionario_clinica_by_id, get_funcionarios_cliente,
            get_funcionarios_clinica, get_risco_exame_by_id, get_risco_ocupacional_by_id,
            get_riscos_exames, get_riscos_ocupacionais, get_tipo_exame_by_id, get_tipos_exame,
        },
        logic::{
            aso_trend, exames_por_mes, fitness_trend, get_total_funcionarios,
            relatorio_risco_categoria,
        },
        tooling::{export_aso_resume, export_reports},
    },
};

use super::{Get, Router};

pub async fn define_access_routes() -> Router {
    let state = start_db_connection()
        .await
        .expect("Failed to connect to database");
    Router::new()
        // ---------- EMPRESA ----------
        .route("/empresas", Get(get_empresas).post(create_empresa))
        .route("/empresas/:id", Get(get_empresa_by_id))
        // ---------- FUNCIONARIO CLIENTE ----------
        .route(
            "/funcionarios_cliente",
            Get(get_funcionarios_cliente).post(create_funcionario_cliente),
        )
        .route(
            "/funcionarios_cliente/:id",
            Get(get_funcionario_cliente_by_id),
        )
        // ---------- FUNCIONARIO CLINICA ----------
        .route(
            "/funcionarios_clinica",
            Get(get_funcionarios_clinica).post(create_funcionario_clinica),
        )
        .route(
            "/funcionarios_clinica/:id",
            Get(get_funcionario_clinica_by_id),
        )
        // ---------- TIPO EXAME ----------
        .route("/tipos_exame", Get(get_tipos_exame).post(create_tipo_exame))
        .route("/tipos_exame/:id", Get(get_tipo_exame_by_id))
        // ---------- RISCO OCUPACIONAL ----------
        .route(
            "/riscos_ocupacionais",
            Get(get_riscos_ocupacionais).post(create_risco_ocupacional),
        )
        .route("/riscos_ocupacionais/:id", Get(get_risco_ocupacional_by_id))
        // ---------- EXAMES ----------
        .route("/exames", Get(get_exames).post(create_exame))
        .route("/exames/:id", Get(get_exame_by_id))
        // ---------- RISCO OCUPACIONAL EXAMES ----------
        .route(
            "/riscos_exames",
            Get(get_riscos_exames).post(create_risco_exame),
        )
        .route("/riscos_exames/:id", Get(get_risco_exame_by_id))
        // ---------- RELATÓRIOS E ESTATÍSTICAS ----------
        .route("/estatisticas/fitness-trend", Get(fitness_trend))
        .route(
            "/estatisticas/risco-severidade",
            Get(relatorio_risco_categoria),
        )
        .route("/estatisticas/exames-por-mes", Get(exames_por_mes))
        .route("/estatisticas/aso-trend", Get(aso_trend))
        .route("/estatisticas/total-func", Get(get_total_funcionarios))
        // -- Dashboards --
        .route("/dashboard/painel", Get(dashboard_painel))
        .route("/dashboard/empresas", Get(dashboard_empresas))
        .route("/dashboard/funcionarios", Get(dashboard_funcionarios))
        .route("/dashboard/clinica", Get(dashboard_clinica))
        .route("/dashboard/exames", Get(dashboard_exames))
        .route("/dashboard/riscos", Get(dashboard_riscos))
        .route("/dashboard/upcoming", Get(dashboard_upcoming))
        // -- Tooling --
        .route("/tooling/resumo/aso", Get(export_aso_resume))
        .route("/tooling/reports", Get(export_reports))
        .with_state(state)
        .layer(insert_cors_config())
}
