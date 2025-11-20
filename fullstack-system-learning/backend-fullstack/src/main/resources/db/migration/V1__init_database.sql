CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    phone VARCHAR(40),
    image_url VARCHAR(1024),
    employee_code VARCHAR(200)
    
);

CREATE INDEX IF NOT EXISTS idx_employee_email ON employee(email);
CREATE INDEX IF NOT EXISTS idx_employee_employee_code ON employee(employee_code);
