-- Insert dummy data into 'projects' table
INSERT INTO projects (title, language, description, url, local_file_dir)
VALUES
    ('Project Alpha', 'Python', 'This is a description of Project Alpha. It involves data analysis and machine learning.', 'https://example.com/project-alpha', '/path/to/project-alpha'),
    ('Project Beta', 'JavaScript', 'Project Beta is a web development project focused on building dynamic front-end features.', 'https://example.com/project-beta', '/path/to/project-beta'),
    ('Project Gamma', 'Java', 'This project is focused on backend services and microservices architecture.', 'https://example.com/project-gamma', '/path/to/project-gamma');

-- Insert dummy data into 'entries' table
INSERT INTO entries (notes, duration, parent_project_id)
VALUES
    ('Entry 1 for Project Alpha: initial setup and research.', 120, 1),
    ('Entry 2 for Project Alpha: implemented first machine learning model.', 240, 1),
    ('Entry 1 for Project Beta: created the homepage and initial components.', 180, 2),
    ('Entry 1 for Project Gamma: created basic service skeleton and endpoints.', 150, 3);

-- Insert dummy data into 'project_files' table
INSERT INTO project_files (filename, filetype, hash, iterated_at, parent_project_id, parent_entry_id)
VALUES
    ('alpha_research_notes.pdf', 'pdf', 'a1b2c3d4e5f6789g0', '2025-02-13 10:00:00', 1, 1),
    ('alpha_ml_model.py', 'python', 'z9y8x7w6v5u4t3s2r1', '2025-02-13 12:00:00', 1, 2),
    ('beta_homepage.html', 'html', 's1r2t3u4v5w6x7y8z9', '2025-02-13 09:30:00', 2, 1),
    ('gamma_service_code.java', 'java', 'h8g9f0e1d2c3b4a5', '2025-02-13 11:15:00', 3, 1);
