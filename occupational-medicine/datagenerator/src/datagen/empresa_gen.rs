use super::*;

pub fn generate_empresas<R: Rng + ?Sized>(
    rng: &mut R,
    today: chrono::NaiveDate,
    file: File
) -> Result<(), Box<dyn Error>> {
    let mut used_cnpj = HashSet::new();
    let mut wtr = Writer::from_writer(file);
    let start_date = today.with_year(today.year() - 30).unwrap();
    let end_date = today;

    println!("Generating {} empresas...", NUM_EMPRESAS);
    for i in 1..=NUM_EMPRESAS {
        let empresa = Empresa {
            id_empresa: i,
            nome: CompanyName().fake::<String>(),
            cnpj: gen_cnpj(rng, &mut used_cnpj),
            endereco: format!(
                "Rua {}, N {}",
                Name().fake::<String>(),
                rng.gen_range(1..=9999)
            ),
            phone: format!(
                "+55 (11) 9{:04}-{:04}",
                rng.gen_range(1000..=9999),
                rng.gen_range(1000..=9999)
            ),
            quantidade_funcionarios_cliente: rng.gen_range(10..=5000),
            quantidade_funcionarios_clinica: rng.gen_range(1..=50),
            registration_date: random_date_between(start_date, end_date),
            status: if rng.gen_bool(0.95) {
                "active".into()
            } else {
                "inactive".into()
            },
        };
        wtr.serialize(&empresa)?;
    }
    wtr.flush()?;
    println!("Generated {} empresas.", NUM_EMPRESAS);
    Ok(())
}
