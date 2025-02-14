SELECT  entries.id,
        entries.notes,
        entries.duration,
        entries.created_at,
        projects.title,
        projects.language,
        projects.description,
        projects.local_file_dir
  FROM  entries
 INNER  JOIN projects on entries.parent_project_id = projects.id
 ORDER  BY DESC
 LIMIT  5;