package ma.enset.hospitalapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
    @GetMapping("/notauthorized")
    public String notAuthorized() {
        return "notauthorized";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
