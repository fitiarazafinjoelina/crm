package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.data.DataGenerationService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

@Controller
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataGenerationService dataGenerationService;
    @Autowired
    private AuthenticationUtils authenticationUtils;
    @Autowired
    private UserService userService;

    @GetMapping("/generate")
    public String generate(Authentication authentication) {
        try {
            int userId = authenticationUtils.getLoggedInUserId(authentication);
            User loggedInUser = userService.findById(userId);

            dataGenerationService.generateData(loggedInUser,10,10,20,20);

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "error/500";
        }
    }
}
