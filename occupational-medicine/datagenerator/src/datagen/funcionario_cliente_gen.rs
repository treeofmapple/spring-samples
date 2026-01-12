use std::fs::File;

use super::*;

pub fn generate_funcionario_cliente<R: Rng + ?Sized>(
    rng: &mut R,
    today: chrono::NaiveDate,
    file: File
) -> Result<(), Box<dyn Error>> {
    let mut used_cpfs = HashSet::new();
    let mut wtr = Writer::from_writer(file);
    for i in 1..=NUM_FUNCIONARIOS_CLIENTE {
        let start_nascimento = today.with_year(today.year() - 65).unwrap();
        let end_nascimento = today.with_year(today.year() - 18).unwrap();
        let birth_date = random_date_between(start_nascimento, end_nascimento);

        let start_admission =
            birth_date
                .with_year(birth_date.year() + 18)
                .unwrap_or_else(|| {
                    NaiveDate::from_ymd_opt(birth_date.year() + 18, birth_date.month(), 28)
                        .unwrap()
                });

        let end_admission = today;

        let admission_date = if start_admission >= end_admission {
            end_admission
        } else {
            random_date_between(start_admission, end_admission)
        };

        let fc = FuncionarioCliente {
            id_funcionario: i,
            id_empresa: rng.gen_range(1..=NUM_EMPRESAS),
            nome: Name().fake::<String>(),
            cpf: gen_cpf(rng, &mut used_cpfs),
            email: format!("users{}@functionary.example", i),
            senha: "changeme123".into(),
            data_nascimento: birth_date,
            cargo: JobTitle().fake::<String>(),
            departamento: "Geral".into(),
            admission_date,
            status: if rng.gen_bool(0.97) {
                "active".into()
            } else {
                "inactive".into()
            },
            role: if rng.gen_bool(0.10) {
                "admin".into()
            } else {
                "employee".into()
            },
        };
        wtr.serialize(&fc)?;
    }
    wtr.flush()?;
    println!(
        "Generated {} funcionario_cliente records.",
        NUM_FUNCIONARIOS_CLIENTE
    );
    Ok(())
}
