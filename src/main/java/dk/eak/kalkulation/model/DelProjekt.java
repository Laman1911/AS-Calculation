package dk.eak.kalkulation.model;

public class DelProjekt {
    private Integer delProjektId;
    private Integer project_id;
    private String name;
    private String description;

    public Integer getDelProjektId() { return delProjektId; }
    public void setDelProjektId(Integer delProjektId) { this.delProjektId = delProjektId; }

    public Integer getProject_id() { return project_id; }
    public void setProject_id(Integer project_id) { this.project_id = project_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
