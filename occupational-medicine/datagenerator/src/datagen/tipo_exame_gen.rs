use super::*;

pub fn generate_tipo_exame<R: Rng + ?Sized>(
    _rng: &mut R,
    file: File,
) -> Result<(), Box<dyn Error>> {
    let mut wtr = Writer::from_writer(file);
    for i in 1..=NUM_TIPOS_EXAMES {
        let te = TipoExame {
            id_tipos_exames: i,
            nome_tipo: EXAM_TYPES[((i - 1) as usize) % EXAM_TYPES.len()].to_string(),
            descricao: Sentence(5..8).fake::<String>(),
        };
        wtr.serialize(&te)?;
    }
    wtr.flush()?;
    println!("Generated {} tipos_exames.", NUM_TIPOS_EXAMES);
    Ok(())
}
