SELECT  entries.notes,
        entries.duration,
        entries.created_at,
        project_files.filename,
        project_files.filetype,
        projects.title
  FROM  project_files
 INNER  JOIN entries ON project_files.parent_entry_id = entries.id
 INNER  JOIN projects ON project_files.parent_project_id = projects.id
 WHERE  filetype = 'java'
 ORDER  BY entries.created_at DESC;