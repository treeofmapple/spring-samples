use chrono::Utc;
use datagenerator::{
    consts::{CSV_FILES, STORED_FOLDER},
    datagen::{
        generate_empresas, generate_exame, generate_funcionario_cliente,
        generate_funcionario_clinica, generate_risco_ocupacional, generate_risco_ocupacional_exame,
        generate_tipo_exame,
    },
};
use rand::thread_rng;
use rayon::{iter::{IndexedParallelIterator, IntoParallelRefIterator, ParallelIterator}, ThreadPoolBuilder};
use std::{
    fs::{self, OpenOptions},
    path::Path,
};

fn main() -> Result<(), Box<dyn std::error::Error>> {
    println!("Starting data generation...");

    ThreadPoolBuilder::new()
        .num_threads(6)
        .build_global()
        .expect("Failed to configure Rayon thread pool");

    fs::create_dir_all(STORED_FOLDER)?;

    for file in &CSV_FILES {
        let path = Path::new(STORED_FOLDER).join(file);
        if path.exists() {
            println!("Removing old file: {}", path.display());
            fs::remove_file(&path)?;
        }
    }

    CSV_FILES.par_iter().enumerate().for_each(|(i, csv_name)| {
        let mut rng = thread_rng();
        let today = Utc::now().date_naive();

        let path = Path::new(STORED_FOLDER).join(csv_name);
        println!("Generating file: {}", path.display());

        let file = OpenOptions::new()
            .write(true)
            .create(true)
            .truncate(true)
            .open(&path)
            .expect("Failed to open file");

        match i {
            0 => generate_empresas(&mut rng, today, file).expect("Failed empresas"),
            1 => generate_funcionario_cliente(&mut rng, today, file).expect("Failed func_cliente"),
            2 => generate_funcionario_clinica(&mut rng, today, file).expect("Failed func_clinica"),
            3 => generate_tipo_exame(&mut rng, file).expect("Failed tipo_exame"),
            4 => generate_risco_ocupacional(&mut rng, file).expect("Failed risco_ocupacional"),
            5 => generate_exame(&mut rng, today, file).expect("Failed exame"),
            6 => generate_risco_ocupacional_exame(&mut rng, file).expect("Failed risco_ocup_exame"),
            _ => unreachable!(),
        }
    });

    println!("\nData generation complete!");
    Ok(())
}
