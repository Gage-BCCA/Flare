CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    title VARCHAR(500),
    language VARCHAR(100),
    desc TEXT,
    url VARCHAR(255),
    local_file_dir VARCHAR(255),
    created_at TIMESTAMP DEFAULT now()
);

CREATE  TABLE project_files (
    id SERIAL PRIMARY KEY,
    filename VARCHAR(255),
    filetype VARCHAR(25),
    hash VARCHAR(255),
    iterated_at TIMESTAMP DEFAULT now(),
    parent_project_id INTEGER REFERENCES project (id),
    parent_entry_id INTEGER REFERENCES entries (id)
);

CREATE TABLE entries (
    id SERIAL PRIMARY KEY,
    notes TEXT NOT NULL,
    duration INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT now(),
    parent_project_id INTEGER REFERENCES project (id)
);