package dk.eak.kalkulation.model;

import java.time.LocalDate;

public class Opgave {
    private Integer opgaveId;
    private Integer project_id;
    private Integer delProjektId; // nullable
    private String name;
    private String description;
    private Integer estimatedHours; // planlanan saat
    private LocalDate deadline;     // optional

    public Integer getOpgaveId() { return opgaveId; }
    public void setOpgaveId(Integer opgaveId) { this.opgaveId = opgaveId; }

    public Integer getProject_id() { return project_id; }
    public void setProject_id(Integer project_id) { this.project_id = project_id; }

    public Integer getDelProjektId() { return delProjektId; }
    public void setDelProjektId(Integer delProjektId) { this.delProjektId = delProjektId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(Integer estimatedHours) { this.estimatedHours = estimatedHours; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}
