package dk.eak.kalkulation.repository;

import dk.eak.kalkulation.model.TimeEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class TimeEntryRepository {
    private final JdbcTemplate jdbc;

    public TimeEntryRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<TimeEntry> findByOpgaveId(int opgaveId) {
        String sql = "SELECT * FROM time_entry WHERE opgave_id = ? ORDER BY work_date, time_entry_id";
        return jdbc.query(sql, (rs, rn) -> mapTimeEntry(rs), opgaveId);
    }

    public TimeEntry findById(int timeEntryId) {
        String sql = "SELECT * FROM time_entry WHERE time_entry_id = ?";
        return jdbc.queryForObject(sql, (rs, rn) -> mapTimeEntry(rs), timeEntryId);
    }

    public int sumHoursByProjektId(int projektId) {
        String sql = """
          SELECT COALESCE(SUM(te.hours),0) AS total
          FROM time_entry te
          JOIN opgave o ON o.opgave_id = te.opgave_id
          WHERE o.project_id = ?
        """;
        Integer v = jdbc.queryForObject(sql, Integer.class, projektId);
        return v == null ? 0 : v;
    }

    public void create(TimeEntry t) {
        String sql = "INSERT INTO time_entry (opgave_id, work_date, hours) VALUES (?,?,?)";
        jdbc.update(sql, t.getOpgaveId(), Date.valueOf(t.getWorkDate()), t.getHours());
    }

    public void update(TimeEntry t) {
        String sql = "UPDATE time_entry SET opgave_id = ?, work_date = ?, hours = ? WHERE time_entry_id = ?";
        jdbc.update(sql, t.getOpgaveId(), Date.valueOf(t.getWorkDate()), t.getHours(), t.getTimeEntryId());
    }

    public void delete(int timeEntryId) {
        jdbc.update("DELETE FROM time_entry WHERE time_entry_id = ?", timeEntryId);
    }

    // Helper method to map ResultSet to TimeEntry
    private TimeEntry mapTimeEntry(java.sql.ResultSet rs) throws java.sql.SQLException {
        TimeEntry t = new TimeEntry();
        t.setTimeEntryId(rs.getInt("time_entry_id"));
        t.setOpgaveId(rs.getInt("opgave_id"));
        t.setWorkDate(rs.getDate("work_date").toLocalDate());
        t.setHours(rs.getInt("hours"));
        return t;
    }
}

