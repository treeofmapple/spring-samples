use super::*;

pub fn generate_exame<R: Rng + ?Sized>(
    rng: &mut R,
    today: chrono::NaiveDate,
    file: File,
) -> Result<(), Box<dyn Error>> {
    let mut wtr = Writer::from_writer(file);
    let choices = ["fit", "fit_with_restrictions", "unfit"];
    let weights = [70, 20, 10];
    let dist = WeightedIndex::new(&weights).unwrap();
    let exam_types = ["admission", "periodic", "return_to_work", "dismissal"];
    let statuses = ["completed", "scheduled", "issued"];
    let start_date = today.with_year(today.year() - 5).unwrap();
    let end_date = today;

    for i in 1..=NUM_EXAMES {
        let result_value = choices[dist.sample(rng)];
        let result = Some(result_value.to_string());

        let ex = Exame {
            id_exames: i,
            id_funcionario: rng.gen_range(1..=NUM_FUNCIONARIOS_CLIENTE),
            id_funcionario_clinica: rng.gen_range(1..=NUM_FUNCIONARIOS_CLINICA),
            id_tipos_exames: rng.gen_range(1..=NUM_TIPOS_EXAMES),
            data_exame: random_date_between(start_date, end_date),
            type_exam: exam_types[rng.gen_range(0..exam_types.len())].to_string(),
            horario_exame: format!("{:02}:{:02}", rng.gen_range(8..=17), rng.gen_range(0..=59)),
            result,
            status: statuses.choose(rng).unwrap().to_string(),
            observations: Sentence(2..4).fake::<String>(),
        };
        wtr.serialize(&ex)?;
    }
    wtr.flush()?;
    println!("Generated {} exames.", NUM_EXAMES);
    Ok(())
}
