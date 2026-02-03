-- Test data for integration tests

-- Projects
INSERT INTO project (project_id, name, description, start_date, end_date) VALUES
(1, 'Test Project 1', 'First test project', '2026-02-01', '2026-02-28'),
(2, 'Test Project 2', 'Second test project', '2026-03-01', '2026-03-31');

-- SubProjects
INSERT INTO delprojekt (delprojekt_id, project_id, name, description) VALUES
(1, 1, 'Test SubProject 1', 'Subproject for project 1'),
(2, 1, 'Test SubProject 2', 'Another subproject for project 1'),
(3, 2, 'Test SubProject 3', 'Subproject for project 2');

-- Tasks
INSERT INTO opgave (opgave_id, project_id, delprojekt_id, name, description, estimated_hours, deadline) VALUES
(1, 1, 1, 'Task 1-1', 'Task in subproject 1', 10, '2026-02-10'),
(2, 1, 1, 'Task 1-2', 'Another task in subproject 1', 15, '2026-02-15'),
(3, 1, 2, 'Task 1-3', 'Task in subproject 2', 20, '2026-02-20'),
(4, 1, NULL, 'Task 1-4', 'Task without subproject', 5, '2026-02-05'),
(5, 2, 3, 'Task 2-1', 'Task in project 2', 25, '2026-03-15');

-- Time Entries
INSERT INTO time_entry (time_entry_id, opgave_id, work_date, hours) VALUES
(1, 1, '2026-02-03', 3),
(2, 1, '2026-02-04', 2),
(3, 2, '2026-02-03', 5),
(4, 3, '2026-02-05', 8),
(5, 4, '2026-02-02', 2),
(6, 5, '2026-03-03', 10);
