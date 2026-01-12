// pub const NUM_EMPRESAS: u32 = 10000000;
// pub const NUM_FUNCIONARIOS_CLIENTE: u32 = 20000000;
// pub const NUM_FUNCIONARIOS_CLINICA: u32 = 2500000;
// pub const NUM_TIPOS_EXAMES: u32 = 1000000;
// pub const NUM_RISCOS_OCUPACIONAIS: u32 = 150000;
// pub const NUM_EXAMES: u32 = 5000000;
// pub const MAX_RISCOS_PER_EXAME: u32 = 8;

pub const NUM_EMPRESAS: u32 = 10000;
pub const NUM_FUNCIONARIOS_CLIENTE: u32 = 20000;
pub const NUM_FUNCIONARIOS_CLINICA: u32 = 2500;
pub const NUM_TIPOS_EXAMES: u32 = 1000;
pub const NUM_RISCOS_OCUPACIONAIS: u32 = 150;
pub const NUM_EXAMES: u32 = 5000;
pub const MAX_RISCOS_PER_EXAME: u32 = 8;
pub const STORED_FOLDER: &str = "database";

pub const CSV_FILES: [&str; 7] = [
    "empresas.csv",
    "funcionario_cliente.csv",
    "funcionario_clinica.csv",
    "tipos_exames.csv",
    "risco_ocupacional.csv",
    "exames.csv",
    "risco_ocupacional_exames.csv",
];

pub const RISKS: [&str; 15] = [
    "Físico",
    "Químico",
    "Biológico",
    "Ergonômico",
    "De Acidente",
    "Ruído",
    "Calor",
    "Poeira",
    "Névoa",
    "Vibração",
    "Radiação",
    "Vírus",
    "Bactérias",
    "Fungos",
    "Esforço Físico Intenso",
];

pub const RISK_CATEGORIES: [&str; 8] = [
    "Physical",
    "Chemical",
    "Ergonomic",
    "Accident",
    "Biological",
    "Noise",
    "Heat",
    "Radiation",
];

pub const JOB_FUNCTIONS: [&str; 4] = [
    "Médico do Trabalho",
    "Enfermeiro(a)",
    "Técnico de Enfermagem",
    "Fonoaudiólogo(a)",
];

pub const EXAM_TYPES: [&str; 10] = [
    "ASO Admissional",
    "ASO Periódico",
    "ASO Demissional",
    "ASO de Retorno ao Trabalho",
    "Audiometria",
    "Espirometria",
    "Acuidade Visual",
    "Hemograma Completo",
    "Glicemia de Jejum",
    "Raio-X de Tórax",
];
