package dk.eak.kalkulation.model;

public class DelProjekt {
    private Integer delProjektId;
    private Integer projektId;
    private String name;
    private String description;

    public Integer getDelProjektId() { return delProjektId; }
    public void setDelProjektId(Integer delProjektId) { this.delProjektId = delProjektId; }

    public Integer getProjektId() { return projektId; }
    public void setProjektId(Integer projektId) { this.projektId = projektId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
