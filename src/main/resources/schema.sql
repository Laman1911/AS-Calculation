DROP TABLE IF EXISTS time_entry;
DROP TABLE IF EXISTS opgave;
DROP TABLE IF EXISTS delprojekt;
DROP TABLE IF EXISTS project;

CREATE TABLE project (
                         project_id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description VARCHAR(500),
                         start_date DATE,
                         end_date DATE
);

CREATE TABLE delprojekt (
                            delprojekt_id INT AUTO_INCREMENT PRIMARY KEY,
                            project_id INT NOT NULL,
                            name VARCHAR(255),
                            description VARCHAR(255),
                            CONSTRAINT fk_delprojekt_project
                                FOREIGN KEY (project_id)
                                    REFERENCES project(project_id)
                                    ON DELETE CASCADE
);

CREATE TABLE opgave (
                        opgave_id INT AUTO_INCREMENT PRIMARY KEY,
                        project_id INT NOT NULL,
                        delprojekt_id INT NULL,
                        name VARCHAR(120) NOT NULL,
                        description VARCHAR(500),
                        estimated_hours INT DEFAULT 0,
                        deadline DATE NULL,
                        CONSTRAINT fk_opgave_project
                            FOREIGN KEY (project_id)
                                REFERENCES project(project_id)
                                ON DELETE CASCADE,
                        CONSTRAINT fk_opgave_delprojekt
                            FOREIGN KEY (delprojekt_id)
                                REFERENCES delprojekt(delprojekt_id)
                                ON DELETE SET NULL
);

CREATE TABLE time_entry (
                            time_entry_id INT AUTO_INCREMENT PRIMARY KEY,
                            opgave_id INT NOT NULL,
                            work_date DATE NOT NULL,
                            hours INT NOT NULL,
                            CONSTRAINT fk_time_opgave
                                FOREIGN KEY (opgave_id)
                                    REFERENCES opgave(opgave_id)
                                    ON DELETE CASCADE
);
