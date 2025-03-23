package site.easy.to.build.crm.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.ErrorMessage;
import site.easy.to.build.crm.entity.LoginRequest;
import site.easy.to.build.crm.entity.Role;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.role.RoleService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationUtils authenticationUtils;
    private final RoleService roleService;

    public LoginController(PasswordEncoder passwordEncoder, UserService userService, AuthenticationManager authenticationManager
            , AuthenticationUtils authenticationUtils, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authenticationUtils = authenticationUtils;
        this.roleService = roleService;
    }

    @RequestMapping("/login")
    public String loginPage() {
        System.out.println("hereeee");
        return "login";
    }
    @PostMapping("/api/loginDotNet")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {
            User user = userService.findByUsername(loginRequest.getUsername()).get(0);

            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorMessage("Invalid credentials, please check your username and password"));
            }

            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Role role = roleService.findByName("ROLE_MANAGER");
            if (!user.getRoles().contains(role)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorMessage("Only managers can access this resource"));
            }

            return ResponseEntity.ok(user);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorMessage("Invalid credentials, please check your username and password"));

        }
         catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage("An unexpected error occurred, please try again later "+e.getMessage()));
        }
    }

    @GetMapping("/change-password")
    public String usernameConfirmationForm() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String confirmUsername(@RequestParam("username") @Nullable String username, @RequestParam("password") @Nullable String password, RedirectAttributes redirectAttributes, HttpSession session) {
        if(username == null || username.isEmpty()) {
            redirectAttributes.addFlashAttribute("usernameError", "Username is required");
            return "redirect:/password-changing";
        }
        List<User> currUser = userService.findByUsername(username);
        if(currUser == null || currUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("usernameError", "Incorrect username. Please provide a correct username");
            return "redirect:/password-changing";
        }
        if(password == null || password.isEmpty()) {
            redirectAttributes.addFlashAttribute("passwordError", "Password is required");
            return "redirect:/password-changing";
        }
        User user = currUser.get(0);
        String hashPassword = passwordEncoder.encode(password);
        user.setPassword(hashPassword);
        user.setPasswordSet(true);
        userService.save(user);
        redirectAttributes.addFlashAttribute("passwordSuccess", "You have successfully changed your password");
        return "redirect:/login";
    }

}
