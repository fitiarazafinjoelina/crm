package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.easy.to.build.crm.service.cleanup.CleanUpService;

@Controller
@RequestMapping("/cleanup")
public class CleanUpController {
    @Autowired
    private CleanUpService cleanUpService;

    @GetMapping()
    public String cleanup() {
        cleanUpService.cleanupDatabase();
        return "redirect:/";
    }
}
