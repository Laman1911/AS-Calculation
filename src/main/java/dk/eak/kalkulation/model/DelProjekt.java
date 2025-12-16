package dk.eak.kalkulation.model;

public class DelProjekt {

    private Integer delProjektId;
    private Integer projectId;
    private String name;
    private String description;

    public Integer getDelProjektId() {
        return delProjektId;
    }

    public void setDelProjektId(Integer delProjektId) {
        this.delProjektId = delProjektId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
