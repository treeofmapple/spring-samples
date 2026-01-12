use super::*;

pub fn generate_risco_ocupacional_exame<R: Rng + ?Sized>(
    rng: &mut R,
    file: File
) -> Result<(), Box<dyn Error>> {
    let mut wtr = Writer::from_writer(file);
    let mut junction_id_counter: u32 = 1;

    for i in 1..=NUM_EXAMES {
        let num_riscos_for_this_exame = rng.gen_range(1..=MAX_RISCOS_PER_EXAME);
        for _ in 0..num_riscos_for_this_exame {
            let r = RiscoOcupacionalExame {
                id_risco_ocupacional_exames: junction_id_counter,
                id_exames: i,
                id_risco_ocupacional: rng.gen_range(1..=NUM_RISCOS_OCUPACIONAIS),
            };
            wtr.serialize(&r)?;
            junction_id_counter += 1;
        }
    }
    wtr.flush()?;
    println!(
        "Generated {} risco_ocupacional_exames records.",
        junction_id_counter - 1
    );
    Ok(())
}
