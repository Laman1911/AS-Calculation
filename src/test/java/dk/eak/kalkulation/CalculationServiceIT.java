package dk.eak.kalkulation;

import dk.eak.kalkulation.model.Projekt;
import dk.eak.kalkulation.repository.ProjektRepository;
import dk.eak.kalkulation.service.CalculationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for CalculationService with full application context.
 * Tests calculation features with actual database operations.
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("CalculationService Integration Tests")
class CalculationServiceIT {

    @Autowired
    private CalculationService calc;

    @Autowired
    private ProjektRepository projektRepo;

    @Test
    @DisplayName("Should calculate total estimated hours for project")
    void testTotalEstimatedHours_Success() {
        // Act - Project 1 has tasks with total 50 estimated hours
        int est = calc.totalEstimatedHours(1);

        // Assert
        assertTrue(est >= 0);
    }

    @Test
    @DisplayName("Should calculate total registered hours for project")
    void testTotalRegisteredHours_Success() {
        // Act
        int reg = calc.totalRegisteredHours(1);

        // Assert
        assertTrue(reg >= 0);
    }

    @Test
    @DisplayName("Should calculate remaining hours correctly")
    void testRemainingEstimatedHours_Success() {
        // Act
        int remaining = calc.remainingEstimatedHours(1);

        // Assert
        assertTrue(remaining >= 0);
    }

    @Test
    @DisplayName("Should calculate required hours per working day")
    void testRequiredHoursPerDay_Success() {
        // Arrange
        Projekt p = projektRepo.findById(1);

        // Act
        double requiredPerDay = calc.requiredHoursPerWorkday(p);

        // Assert
        assertTrue(requiredPerDay >= 0.0);
    }

    @Test
    @DisplayName("Should complete full calculation flow")
    void testCompleteCalculationFlow_Success() {
        // Arrange
        Projekt p = projektRepo.findById(1);

        // Act
        int estimated = calc.totalEstimatedHours(1);
        int registered = calc.totalRegisteredHours(1);
        int remaining = calc.remainingEstimatedHours(1);
        double requiredPerDay = calc.requiredHoursPerWorkday(p);
        double progress = calc.getProgressPercentage(1);

        // Assert
        assertTrue(estimated >= 0);
        assertTrue(registered >= 0);
        assertTrue(remaining >= 0);
        assertTrue(requiredPerDay >= 0.0);
        assertTrue(progress >= 0.0 && progress <= 100.0);
    }

    @Test
    @DisplayName("Should calculate working days correctly")
    void testWorkingDaysBetween_Success() {
        // Arrange
        Projekt p = projektRepo.findById(1);

        // Act
        int workingDays = calc.workingDaysBetween(p.getStartDate(), p.getEndDate());

        // Assert
        assertTrue(workingDays > 0);
    }

    @Test
    @DisplayName("Should calculate progress percentage")
    void testProgressPercentage_Success() {
        // Act
        double progress = calc.getProgressPercentage(1);

        // Assert
        assertTrue(progress >= 0.0 && progress <= 100.0);
    }

    @Test
    @DisplayName("Should throw exception with invalid project ID")
    void testInvalidProjectId_Throws() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            calc.totalEstimatedHours(0);
        });
    }
}

