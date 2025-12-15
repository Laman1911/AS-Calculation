package dk.eak.kalkulation.controller;

import dk.eak.kalkulation.model.Opgave;
import dk.eak.kalkulation.repository.OpgaveRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projektId}/tasks")
public class OpgaveController {

    private final OpgaveRepository repo;

    public OpgaveController(OpgaveRepository repo) { this.repo = repo; }

    @GetMapping
    public String list(@PathVariable int projektId, Model model) {
        model.addAttribute("projektId", projektId);
        model.addAttribute("tasks", repo.findByProjektId(projektId));
        model.addAttribute("taskForm", new Opgave());
        return "tasks";
    }

    @PostMapping
    public String create(@PathVariable int projektId, @ModelAttribute("taskForm") Opgave o) {
        o.setProjektId(projektId);
        if (o.getEstimatedHours() == null) o.setEstimatedHours(0);
        repo.create(o);
        return "redirect:/projects/" + projektId + "/tasks";
    }

    @PostMapping("/{opgaveId}/delete")
    public String delete(@PathVariable int projektId, @PathVariable int opgaveId) {
        repo.delete(opgaveId);
        return "redirect:/projects/" + projektId + "/tasks";
    }
}
