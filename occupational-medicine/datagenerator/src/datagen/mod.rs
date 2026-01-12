pub use crate::consts::*;
pub use crate::structs::*;
pub use chrono::{Datelike, NaiveDate};
pub use csv::Writer;
pub use fake::Fake;
pub use fake::faker::{
    company::en::CompanyName, job::en::Title as JobTitle, lorem::en::Sentence, name::en::Name,
};
pub use rand::Rng;
pub use rand::distributions::{Distribution, WeightedIndex};
pub use rand::seq::SliceRandom;
pub use std::collections::HashSet;
pub use std::error::Error;
pub use std::fs::{File, OpenOptions};

pub mod empresa_gen;
pub mod exame_gen;
pub mod funcionario_cliente_gen;
pub mod funcionario_clinica_gen;
pub mod helpers;
pub mod risco_ocupacional_exame_gen;
pub mod risco_ocupacional_gen;
pub mod tipo_exame_gen;

pub use self::empresa_gen::generate_empresas;
pub use self::exame_gen::generate_exame;
pub use self::funcionario_cliente_gen::generate_funcionario_cliente;
pub use self::funcionario_clinica_gen::generate_funcionario_clinica;
pub use self::helpers::utils::{gen_cnpj, gen_cpf, random_date_between};
pub use self::risco_ocupacional_exame_gen::generate_risco_ocupacional_exame;
pub use self::risco_ocupacional_gen::generate_risco_ocupacional;
pub use self::tipo_exame_gen::generate_tipo_exame;
