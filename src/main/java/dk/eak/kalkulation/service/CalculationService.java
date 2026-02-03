package dk.eak.kalkulation.service;

import dk.eak.kalkulation.model.Opgave;
import dk.eak.kalkulation.model.Projekt;
import dk.eak.kalkulation.repository.OpgaveRepository;
import dk.eak.kalkulation.repository.TimeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for project calculations.
 * Provides metrics and calculations for projects and tasks.
 */
@Service
public class CalculationService {

    private final OpgaveRepository opgaveRepo;
    private final TimeEntryRepository timeRepo;

    public CalculationService(OpgaveRepository opgaveRepo, TimeEntryRepository timeRepo) {
        this.opgaveRepo = opgaveRepo;
        this.timeRepo = timeRepo;
    }

    /**
     * Calculate total estimated hours for a project.
     *
     * @param projektId the project ID
     * @return total estimated hours
     */
    public int totalEstimatedHours(int projektId) {
        if (projektId <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        List<Opgave> tasks = opgaveRepo.findByProjectId(projektId);
        return tasks.stream()
                .mapToInt(o -> o.getEstimatedHours() != null ? o.getEstimatedHours() : 0)
                .sum();
    }

    /**
     * Calculate total registered (actual) hours for a project.
     *
     * @param projektId the project ID
     * @return total registered hours
     */
    public int totalRegisteredHours(int projektId) {
        if (projektId <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        return timeRepo.sumHoursByProjektId(projektId);
    }

    /**
     * Calculate remaining estimated hours for a project.
     *
     * @param projektId the project ID
     * @return remaining hours (estimated - registered, minimum 0)
     */
    public int remainingEstimatedHours(int projektId) {
        if (projektId <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        int est = totalEstimatedHours(projektId);
        int reg = totalRegisteredHours(projektId);
        return Math.max(est - reg, 0);
    }

    /**
     * Calculate number of working days between two dates (excludes weekends).
     *
     * @param start the start date (inclusive)
     * @param endInclusive the end date (inclusive)
     * @return number of working days
     */
    public int workingDaysBetween(LocalDate start, LocalDate endInclusive) {
        if (start == null || endInclusive == null) {
            return 0;
        }
        if (endInclusive.isBefore(start)) {
            return 0;
        }

        int count = 0;
        LocalDate d = start;
        while (!d.isAfter(endInclusive)) {
            DayOfWeek w = d.getDayOfWeek();
            if (w != DayOfWeek.SATURDAY && w != DayOfWeek.SUNDAY) {
                count++;
            }
            d = d.plusDays(1);
        }
        return count;
    }

    /**
     * Calculate required hours per working day to complete a project.
     *
     * @param p the project
     * @return hours per working day needed
     */
    public double requiredHoursPerWorkday(Projekt p) {
        if (p == null || p.getStartDate() == null || p.getEndDate() == null) {
            return 0.0;
        }
        int days = workingDaysBetween(p.getStartDate(), p.getEndDate());
        if (days == 0) {
            return 0.0;
        }
        int remaining = remainingEstimatedHours(p.getProjectId());
        return remaining / (double) days;
    }

    /**
     * Calculate progress percentage for a project.
     *
     * @param projektId the project ID
     * @return progress percentage (0-100)
     */
    public double getProgressPercentage(int projektId) {
        if (projektId <= 0) {
            throw new IllegalArgumentException("Project ID must be valid");
        }
        int estimated = totalEstimatedHours(projektId);
        if (estimated == 0) {
            return 0.0;
        }
        int registered = totalRegisteredHours(projektId);
        return Math.min((registered / (double) estimated) * 100, 100.0);
    }
}

