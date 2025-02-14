SELECT  projects.title,
        entries.notes,
        entries.duration,
        entries.created_at
  FROM  projects
 INNER  JOIN entries ON projects.id = entries.parent_project_id
 WHERE  projects.id = 1
 ORDER  BY entries.created_at DESC;
