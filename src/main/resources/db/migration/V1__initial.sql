CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE article (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    CONSTRAINT fk_article_author FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE audit_log (
    id SERIAL PRIMARY KEY,
    method VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    client_ip VARCHAR(255),
    user_agent VARCHAR(255),
    timestamp TIMESTAMPTZ NOT NULL,
    user_id INT,
    created_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ,
    CONSTRAINT fk_auditlog_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE auth_token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    user_id INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ,
    CONSTRAINT fk_authtoken_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE auth_otp (
    id SERIAL PRIMARY KEY,
    otp VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    verified_at TIMESTAMPTZ,
    user_id INT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ,
    CONSTRAINT fk_authotp_user FOREIGN KEY (user_id) REFERENCES users(id)
);
