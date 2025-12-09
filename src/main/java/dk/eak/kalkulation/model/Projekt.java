package dk.eak.kalkulation.model;

import java.time.LocalDate;

public class Projekt {
    private int id;
    private String navn;
    private String beskrivelse;
    private double budget;
    private double estimeredeTimer;
    private double timeRate;
    private LocalDate startDato;
    private LocalDate slutDato;
    private String status;

    public Projekt() {
    }

    public Projekt(int id, String navn, String beskrivelse, double budget, double estimeredeTimer,
                   double timeRate, LocalDate startDato, LocalDate slutDato, String status) {
        this.id = id;
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.budget = budget;
        this.estimeredeTimer = estimeredeTimer;
        this.timeRate = timeRate;
        this.startDato = startDato;
        this.slutDato = slutDato;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getEstimeredeTimer() {
        return estimeredeTimer;
    }

    public void setEstimeredeTimer(double estimeredeTimer) {
        this.estimeredeTimer = estimeredeTimer;
    }

    public double getTimeRate() {
        return timeRate;
    }

    public void setTimeRate(double timeRate) {
        this.timeRate = timeRate;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }

    public void setSlutDato(LocalDate slutDato) {
        this.slutDato = slutDato;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double beregnTotalPris() {
        return estimeredeTimer * timeRate;
    }

    public double beregnProfitMargin() {
        double totalPris = beregnTotalPris();
        if (totalPris > 0) {
            return ((budget - totalPris) / totalPris) * 100;
        }
        return 0;
    }
}
