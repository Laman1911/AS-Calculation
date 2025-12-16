package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.Opgave;
import dk.eak.kalkulation.model.Projekt;
import dk.eak.kalkulation.repository.OpgaveRepository;
import dk.eak.kalkulation.repository.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class CalculationService {

    private final OpgaveRepository opgaveRepo;
    private final TimeEntryRepository timeRepo;

    public CalculationService(OpgaveRepository opgaveRepo, TimeEntryRepository timeRepo) {
        this.opgaveRepo = opgaveRepo;
        this.timeRepo = timeRepo;
    }

    public int totalEstimatedHours(int projektId) {
        List<Opgave> tasks = opgaveRepo.findByProjektId(projektId);
        return tasks.stream().mapToInt(o -> o.getEstimatedHours() != null ? o.getEstimatedHours() : 0).sum();
    }

    public int totalRegisteredHours(int projektId) {
        return timeRepo.sumHoursByProjektId(projektId);
    }

    public int remainingEstimatedHours(int projektId) {
        int est = totalEstimatedHours(projektId);
        int reg = totalRegisteredHours(projektId);
        return Math.max(est - reg, 0);
    }

    public int workingDaysBetween(LocalDate start, LocalDate endInclusive) {
        if (start == null || endInclusive == null) return 0;
        if (endInclusive.isBefore(start)) return 0;

        int count = 0;
        LocalDate d = start;
        while (!d.isAfter(endInclusive)) {
            DayOfWeek w = d.getDayOfWeek();
            if (w != DayOfWeek.SATURDAY && w != DayOfWeek.SUNDAY) count++;
            d = d.plusDays(1);
        }
        return count;
    }

    public double requiredHoursPerWorkday(Projekt p) {
        if (p == null || p.getStartDate() == null || p.getEndDate() == null) return 0.0;
        int days = workingDaysBetween(p.getStartDate(), p.getEndDate());
        if (days == 0) return 0.0;
        int remaining = remainingEstimatedHours(p.getProjectid());
        return remaining / (double) days;
    }
}
