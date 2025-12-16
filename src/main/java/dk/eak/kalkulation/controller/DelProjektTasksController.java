package dk.eak.kalkulation.controller;

import dk.eak.kalkulation.repository.OpgaveRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/delprojekter/{delProjektId}/tasks")
public class DelProjektTasksController {

    private final OpgaveRepository repo;

    public DelProjektTasksController(OpgaveRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String tasksForDelprojekt(
            @PathVariable int delProjektId,
            Model model
    ) {
        model.addAttribute("opgaver", repo.findByDelProjektId(delProjektId));
        model.addAttribute("delProjektId", delProjektId);
        return "tasks";
    }
}
