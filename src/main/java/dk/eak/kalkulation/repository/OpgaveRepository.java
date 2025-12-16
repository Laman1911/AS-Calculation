package dk.eak.kalkulation.repository;

import dk.eak.kalkulation.model.Opgave;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class OpgaveRepository {

    private final JdbcTemplate jdbc;

    public OpgaveRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Opgave> findByProjectId(int projectId) {
        String sql = "SELECT * FROM opgave WHERE project_id = ? ORDER BY opgave_id";
        return jdbc.query(sql, (rs, rn) -> {
            Opgave o = new Opgave();
            o.setOpgaveId(rs.getInt("opgave_id"));
            o.setProject_id(rs.getInt("project_id"));          // ✅ FIX
            int dp = rs.getInt("del_project_id");
            o.setDelProjektId(rs.wasNull() ? null : dp);
            o.setName(rs.getString("name"));
            o.setDescription(rs.getString("description"));
            o.setEstimatedHours(rs.getInt("estimated_hours"));
            Date dl = rs.getDate("deadline");
            o.setDeadline(dl != null ? dl.toLocalDate() : null);
            return o;
        }, projectId);
    }

    public Opgave findById(int opgaveId) {
        String sql = "SELECT * FROM opgave WHERE opgave_id = ?";
        return jdbc.queryForObject(sql, (rs, rn) -> {
            Opgave o = new Opgave();
            o.setOpgaveId(rs.getInt("opgave_id"));
            o.setProject_id(rs.getInt("project_id"));          // ✅ FIX
            int dp = rs.getInt("del_project_id");
            o.setDelProjektId(rs.wasNull() ? null : dp);
            o.setName(rs.getString("name"));
            o.setDescription(rs.getString("description"));
            o.setEstimatedHours(rs.getInt("estimated_hours"));
            Date dl = rs.getDate("deadline");
            o.setDeadline(dl != null ? dl.toLocalDate() : null);
            return o;
        }, opgaveId);
    }

    public void create(Opgave o) {
        String sql = """
            INSERT INTO opgave
            (project_id, del_project_id, name, description, estimated_hours, deadline)
            VALUES (?,?,?,?,?,?)
        """;
        jdbc.update(
                sql,
                o.getProject_id(),
                o.getDelProjektId(),
                o.getName(),
                o.getDescription(),
                o.getEstimatedHours(),
                o.getDeadline() != null ? Date.valueOf(o.getDeadline()) : null
        );
    }

    public void update(Opgave o) {
        String sql = """
            UPDATE opgave
            SET del_project_id=?, name=?, description=?, estimated_hours=?, deadline=?
            WHERE opgave_id=?
        """;
        jdbc.update(
                sql,
                o.getDelProjektId(),
                o.getName(),
                o.getDescription(),
                o.getEstimatedHours(),
                o.getDeadline() != null ? Date.valueOf(o.getDeadline()) : null,
                o.getOpgaveId()
        );
    }

    public void delete(int opgaveId) {
        jdbc.update("DELETE FROM opgave WHERE opgave_id = ?", opgaveId);
    }
}
