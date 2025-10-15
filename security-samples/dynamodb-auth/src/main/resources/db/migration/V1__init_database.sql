CREATE TABLE auditable (
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    created_by UUID,
    modified_by UUID,

    version BIGINT NOT NULL
);

CREATE TABLE login_history (

	id UUID PRIMARY KEY,
	user_id UUID NOT NULL,
	
	login_time TIMESTAMP WITH TIME ZONE NOT NULL,
	ip_address VARCHAR(255)
	
);

CREATE TABLE users (
	
	id UUID PRIMARY KEY,
	
    username VARCHAR(40) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    age INTEGER,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,

    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    account_enabled BOOLEAN NOT NULL DEFAULT TRUE,
	
	CONSTRAINT fk_users_created_by FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT fk_users_modified_by FOREIGN KEY (modified_by) REFERENCES users (id)
	
) INHERITS (auditable);

create TABLE tokens (

	id BIGSERIAL PRIMARY KEY,
	
	token VARCHAR(1024) NOT NULL UNIQUE,
	token_type VARCHAR(255) NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    expired BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID NOT NULL,

	CONSTRAINT fk_tokens_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_tokens_created_by FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT fk_tokens_modified_by FOREIGN KEY (modified_by) REFERENCES users (id)
	
) INHERITS (auditable);


-- Comments 

COMMENT ON TABLE users IS 'The user table';
COMMENT ON TABLE tokens IS 'The token table';
COMMENT ON TABLE login_history IS 'The login history table';
COMMENT ON TABLE auditable IS 'The auditable table';

