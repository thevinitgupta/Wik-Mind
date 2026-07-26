CREATE TABLE users
(
    id UUID PRIMARY KEY,

    email VARCHAR(255) NOT NULL UNIQUE,

    first_name VARCHAR(255),

    last_name VARCHAR(255),

    profile_image_url TEXT,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE connected_accounts
(
    id UUID PRIMARY KEY,

    provider VARCHAR(50) NOT NULL,

    provider_user_id VARCHAR(255) NOT NULL,

    connected_at TIMESTAMP,

    refresh_token TEXT,

    user_id UUID NOT NULL,

    CONSTRAINT fk_connected_account_user
        FOREIGN KEY (user_id)
            REFERENCES users(id),

    CONSTRAINT uq_provider_account
        UNIQUE(provider, provider_user_id)
);

CREATE TABLE refresh_tokens
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    token_hash VARCHAR(255) NOT NULL UNIQUE,

    expires_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP NOT NULL,

    revoked_at TIMESTAMP,

    revoked BOOLEAN NOT NULL,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY(user_id)
            REFERENCES users(id)
);

CREATE TABLE workspaces
(
    id UUID PRIMARY KEY,

    owner_id UUID NOT NULL,

    name VARCHAR(200) NOT NULL,

    visibility VARCHAR(20) NOT NULL,

    clone_policy VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_workspace_owner
        FOREIGN KEY(owner_id)
            REFERENCES users(id)
);

CREATE INDEX idx_connected_accounts_user
    ON connected_accounts(user_id);

CREATE INDEX idx_refresh_tokens_user
    ON refresh_tokens(user_id);

CREATE INDEX idx_workspaces_owner
    ON workspaces(owner_id);

