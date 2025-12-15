INSERT INTO project(name, description, start_date, end_date)
VALUES ('Demo Project', 'Alpha Solutions kalkulation demo', '2025-12-01', '2025-12-20');

INSERT INTO opgave(project_id, del_project_id, name, description, estimated_hours, deadline)
VALUES
    (1, NULL, 'Setup DB', 'Create schema + testdata', 6, '2025-12-03'),
    (1, NULL, 'CRUD UI', 'Thymeleaf pages', 10, '2025-12-10');

INSERT INTO time_entry(opgave_id, work_date, hours)
VALUES
    (1, '2025-12-01', 2),
    (1, '2025-12-02', 1),
    (2, '2025-12-05', 3);
