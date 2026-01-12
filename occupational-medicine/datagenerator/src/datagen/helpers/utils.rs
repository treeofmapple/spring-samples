use super::*;

pub fn generated_cpf<R: Rng + ?Sized>(rng: &mut R) -> String {
    let s: String = (0..11).map(|_| rng.gen_range(0..10).to_string()).collect();
    format!("{}.{}.{}-{}", &s[0..3], &s[3..6], &s[6..9], &s[9..11])
}

pub fn gen_cpf<R: Rng + ?Sized>(rng: &mut R, used: &mut HashSet<String>) -> String {
    loop {
        let cpf = generated_cpf(rng);
        if used.insert(cpf.clone()) {
            return cpf;
        }
    }
}

pub fn generated_cnpj<R: Rng + ?Sized>(rng: &mut R) -> String {
    let s: String = (0..14).map(|_| rng.gen_range(0..10).to_string()).collect();
    format!(
        "{}.{}.{}/{}-{}",
        &s[0..2],
        &s[2..5],
        &s[5..8],
        &s[8..12],
        &s[12..14]
    )
}

pub fn gen_cnpj<R: Rng + ?Sized>(rng: &mut R, used: &mut HashSet<String>) -> String {
    loop {
        let cnpj = generated_cnpj(rng);
        if used.insert(cnpj.clone()) {
            return cnpj;
        }
    }
}

pub fn random_date_between(start: NaiveDate, end: NaiveDate) -> NaiveDate {
    let days_diff = (end - start).num_days();
    let random_days: i64 = rand::thread_rng().gen_range(0..=days_diff);
    start + Duration::days(random_days)
}
