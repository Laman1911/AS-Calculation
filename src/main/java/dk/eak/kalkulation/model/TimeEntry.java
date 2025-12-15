package dk.eak.kalkulation.model;

import java.time.LocalDate;

public class TimeEntry {
    private Integer timeEntryId;
    private Integer opgaveId;
    private LocalDate workDate;
    private Integer hours; // real işlənən saat

    public Integer getTimeEntryId() { return timeEntryId; }
    public void setTimeEntryId(Integer timeEntryId) { this.timeEntryId = timeEntryId; }

    public Integer getOpgaveId() { return opgaveId; }
    public void setOpgaveId(Integer opgaveId) { this.opgaveId = opgaveId; }

    public LocalDate getWorkDate() { return workDate; }
    public void setWorkDate(LocalDate workDate) { this.workDate = workDate; }

    public Integer getHours() { return hours; }
    public void setHours(Integer hours) { this.hours = hours; }
}
