package dk.eak.kalkulation.model;
import java.time.LocalDate;
//i denne model/boks gemmer jeg data
public class Projekt {
    private String name;
    private int projectid;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getName(){
        return name;
    }
    public int getProjectid(){
        return projectid;
    }
    public String getDescription(){
        return description;
    }
    public LocalDate getStartDate(){
        return startDate;
    }
    public LocalDate getEndDate(){
        return  endDate;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setProjectid(int projektid){
        this.projectid = projektid;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }


}
