DROP TABLE IF EXISTS project_files;
DROP TABLE IF EXISTS entries;
DROP TABLE IF EXISTS projects;

CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    title TEXT,
    language TEXT,
    description TEXT,
    url TEXT,
    local_file_dir TEXT,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE entries (
    id SERIAL PRIMARY KEY,
    notes TEXT NOT NULL,
    duration INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    parent_project_id INTEGER REFERENCES projects (id) ON DELETE CASCADE
);

CREATE  TABLE project_files (
    id SERIAL PRIMARY KEY,
    filename TEXT,
    filetype TEXT,
    hash TEXT,
    iterated_at TIMESTAMP DEFAULT NOW(),
    parent_project_id INTEGER REFERENCES projects (id) ON DELETE CASCADE,
    parent_entry_id INTEGER REFERENCES entries (id) ON DELETE CASCADE
);

