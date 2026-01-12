use super::*;

pub fn generate_funcionario_clinica<R: Rng + ?Sized>(
    rng: &mut R,
    today: chrono::NaiveDate,
    file: File
) -> Result<(), Box<dyn Error>> {
    let mut wtr = Writer::from_writer(file);
    let start_date = today.with_year(today.year() - 20).unwrap();
    let end_date = today;

    for i in 1..=NUM_FUNCIONARIOS_CLINICA {
        let fc = FuncionarioClinica {
            id_funcionario_clinica: i,
            nome: Name().fake::<String>(),
            crm: format!("CRM/SP {}", rng.gen_range(10_000..=99_999)),
            funcao: JOB_FUNCTIONS[rng.gen_range(0..JOB_FUNCTIONS.len())].to_string(),
            senha: "clinicpass".into(),
            especialidade: "General".into(),
            email: format!("user{}@clinic.example", i),
            phone: format!(
                "+55 (11) 3{:03}-{:04}",
                rng.gen_range(100..=999),
                rng.gen_range(1000..=9999)
            ),
            data_contratacao: random_date_between(start_date, end_date),
            status: if rng.gen_bool(0.98) {
                "active".into()
            } else {
                "inactive".into()
            },
            role: {
                let r = rng.gen_range(0..100);
                if r < 10 {
                    "admin".into()
                } else if r < 55 {
                    "doctor".into()
                } else {
                    "employee".into()
                }
            },
        };
        wtr.serialize(&fc)?;
    }
    wtr.flush()?;
    println!(
        "Generated {} funcionario_clinica records.",
        NUM_FUNCIONARIOS_CLINICA
    );
    Ok(())
}
