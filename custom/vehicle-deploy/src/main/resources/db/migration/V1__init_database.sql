CREATE TABLE brand (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE model (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand_id BIGINT NOT NULL,
    
    CONSTRAINT fk_model_brand FOREIGN KEY(brand_id) REFERENCES brand(id),
    CONSTRAINT uk_model_name_brand UNIQUE (name, brand_id)
);

CREATE TABLE vehicle (
    id BIGSERIAL PRIMARY KEY,
    brand_id BIGINT NOT NULL,
    model_id BIGINT NOT NULL,
    color VARCHAR(255),
    plate VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL CHECK (type IN ('CAR', 'MOTORCYCLE')),
    
    CONSTRAINT fk_vehicle_brand FOREIGN KEY(brand_id) REFERENCES brand(id),
    CONSTRAINT fk_vehicle_model FOREIGN KEY(model_id) REFERENCES model(id)
);

COMMENT ON TABLE brand IS 'Stores vehicle brand information';
COMMENT ON TABLE model IS 'Stores vehicle model information, linked to a brand';
COMMENT ON TABLE vehicle IS 'Stores individual vehicle data';

CREATE INDEX idx_brand_name ON brand(name);
CREATE INDEX idx_model_name ON model(name);
CREATE INDEX idx_vehicle_brand_id ON vehicle(brand_id);
CREATE INDEX idx_vehicle_model_id ON vehicle(model_id);
