package dk.eak.kalkulation.controller;

import dk.eak.kalkulation.repository.ProjektRepository;
import dk.eak.kalkulation.service.CalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projektId}/calc")
public class CalculationController {

    private final ProjektRepository projektRepo;
    private final CalculationService calc;

    public CalculationController(ProjektRepository projektRepo, CalculationService calc) {
        this.projektRepo = projektRepo;
        this.calc = calc;
    }

    @GetMapping
    public String view(@PathVariable int projektId, Model model) {
        var p = projektRepo.findById(projektId);

        model.addAttribute("projekt", p);
        model.addAttribute("estimatedTotal", calc.totalEstimatedHours(projektId));
        model.addAttribute("registeredTotal", calc.totalRegisteredHours(projektId));
        model.addAttribute("remaining", calc.remainingEstimatedHours(projektId));
        model.addAttribute("requiredPerDay", calc.requiredHoursPerWorkday(p));
        return "calc";
    }
}

