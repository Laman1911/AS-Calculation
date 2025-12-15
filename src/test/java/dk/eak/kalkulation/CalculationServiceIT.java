package dk.eak.kalkulation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;


import dk.eak.kalkulation.repository.ProjektRepository;
import dk.eak.kalkulation.service.CalculationService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CalculationServiceIT {

    @Autowired
    CalculationService calc;

    @Autowired
    ProjektRepository projektRepo;

    @Test
    void totals_should_work() {
        int est = calc.totalEstimatedHours(1);
        int reg = calc.totalRegisteredHours(1);

        assertTrue(est >= 0);
        assertTrue(reg >= 0);
    }

    @Test
    void required_hours_per_day_should_not_crash() {
        var p = projektRepo.findById(1);
        double v = calc.requiredHoursPerWorkday(p);
        assertTrue(v >= 0.0);
    }
}
