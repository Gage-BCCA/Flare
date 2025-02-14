SELECT  projects.title,
        entries.notes,
        entries.duration,
        entries.created_at
  FROM  projects
 INNER  JOIN entries ON projects.id = entries.id
 ORDER  BY projects.created_at DESC
 LIMIT  1;