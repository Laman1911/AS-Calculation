package dk.eak.kalkulation.repository;

import dk.eak.kalkulation.model.Projekt;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class ProjektRepository {
    private final JdbcTemplate jdbc;

    public ProjektRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // 1. CREATE
    public void create(Projekt projekt) {
        String sql = "INSERT INTO project (name, description, start_date, end_date) VALUES (?, ?, ?, ?)";
        jdbc.update(sql,
                projekt.getName(),
                projekt.getDescription(),
                projekt.getStartDate(),
                projekt.getEndDate()
        );
    }

    // 2. GET ALL
    public List<Projekt> getAll() {
        String sql = "SELECT * FROM project";
        return jdbc.query(sql, (rs, rowNum) -> {
            Projekt p = new Projekt();
            p.setProjectid(rs.getInt("project_id"));
            p.setName(rs.getString("name"));
            p.setDescription(rs.getString("description"));
            p.setStartDate(rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null);
            p.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
            return p;
        });
    }

    // 3. GET BY ID
    public Projekt getById(int id) {
        String sql = "SELECT * FROM project WHERE project_id = ?";
        return jdbc.queryForObject(sql, (rs, rowNum) -> {
            Projekt p = new Projekt();
            p.setProjectid(rs.getInt("project_id"));
            p.setName(rs.getString("name"));
            p.setDescription(rs.getString("description"));
            p.setStartDate(rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null);
            p.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
            return p;
        }, id);
    }

    // 4. UPDATE
    public void update(Projekt p) {
        String sql = "UPDATE project SET name = ?, description = ?, start_date = ?, end_date = ? WHERE project_id = ?";
        jdbc.update(sql,
                p.getName(),
                p.getDescription(),
                p.getStartDate(),
                p.getEndDate(),
                p.getProjectid());
    }

    // 5. DELETE
    public void delete(int id) {
        String sql = "DELETE FROM project WHERE project_id = ?";
        jdbc.update(sql, id);
    }
    public Projekt findById(int projectId) {
        String sql = "SELECT * FROM project WHERE project_id = ?";
        return jdbc.queryForObject(sql, (rs, rowNum) -> {
            Projekt p = new Projekt();
            p.setProjectid(rs.getInt("project_id"));
            p.setName(rs.getString("name"));
            p.setDescription(rs.getString("description"));
            p.setStartDate(rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null);
            p.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
            return p;
        }, projectId);
    }




}
