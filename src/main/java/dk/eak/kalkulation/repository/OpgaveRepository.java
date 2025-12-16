package dk.eak.kalkulation.repository;

import dk.eak.kalkulation.model.Opgave;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Repository
public class OpgaveRepository {

    private final JdbcTemplate jdbc;

    public OpgaveRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Opgave> findByProjectId(int projectId) {
        String sql = "SELECT * FROM opgave WHERE project_id = ? ORDER BY opgave_id";
        return mapList(sql, projectId);
    }

    public List<Opgave> findByDelProjektId(int delProjektId) {
        String sql = "SELECT * FROM opgave WHERE delprojekt_id = ? ORDER BY opgave_id";
        return mapList(sql, delProjektId);
    }

    public Opgave findById(int id) {
        String sql = "SELECT * FROM opgave WHERE opgave_id = ?";
        return jdbc.queryForObject(sql, (rs, rn) -> map(rs));
    }

    public void create(Opgave o) {
        jdbc.update("""
            INSERT INTO opgave
            (project_id, delprojekt_id, name, description, estimated_hours, deadline)
            VALUES (?,?,?,?,?,?)
        """,
                o.getProject_id(),
                o.getDelProjektId(),
                o.getName(),
                o.getDescription(),
                o.getEstimatedHours(),
                o.getDeadline() != null ? Date.valueOf(o.getDeadline()) : null
        );
    }

    public void delete(int id) {
        jdbc.update("DELETE FROM opgave WHERE opgave_id = ?", id);
    }

    // ===== helpers =====
    private List<Opgave> mapList(String sql, int id) {
        return jdbc.query(sql, (rs, rn) -> map(rs), id);
    }

    private Opgave map(ResultSet rs) throws SQLException {
        Opgave o = new Opgave();
        o.setOpgaveId(rs.getInt("opgave_id"));
        o.setProject_id(rs.getInt("project_id"));
        int dp = rs.getInt("delprojekt_id");
        o.setDelProjektId(rs.wasNull() ? null : dp);
        o.setName(rs.getString("name"));
        o.setDescription(rs.getString("description"));
        o.setEstimatedHours(rs.getInt("estimated_hours"));
        Date d = rs.getDate("deadline");
        o.setDeadline(d != null ? d.toLocalDate() : null);
        return o;
    }
}
