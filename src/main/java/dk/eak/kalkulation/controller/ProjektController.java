package dk.eak.kalkulation.controller;

import dk.eak.kalkulation.model.Projekt;
import dk.eak.kalkulation.service.ProjektService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projekter")
public class ProjektController {
    private final ProjektService service;

    public ProjektController(ProjektService service){
        this.service = service;
    }
    @GetMapping
    public String getAllProjects(Model model) {
        model.addAttribute("projekts", service.getAll());
        return "projekts"; // Thymeleaf page (projekter.html)
    }
    @GetMapping("/opret")
    public String createProjectForm(Model model) {
        model.addAttribute("projekt", new Projekt());
        return "opret_projekt";
    }
    @PostMapping("/opret")
    public String createProjectSubmit(@ModelAttribute Projekt projekt) {
        service.create(projekt);
        return "redirect:/projekter";
    }
    @GetMapping("/slet/{id}")
    public String deleteProject(@PathVariable int id) {
        service.delete(id);
        return "redirect:/projekter";
    }
    @GetMapping("/rediger/{id}")
    public String editProjectForm(@PathVariable int id, Model model) {
        Projekt projekt = service.getById(id);
        model.addAttribute("projekt", projekt);
        return "rediger_projekt";
    }
    @PostMapping("/rediger")
    public String editProjectSubmit(@ModelAttribute Projekt projekt) {
        service.update(projekt);
        return "redirect:/projekter";
    }






}
