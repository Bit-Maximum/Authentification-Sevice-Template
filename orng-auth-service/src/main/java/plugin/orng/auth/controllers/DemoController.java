package plugin.orng.auth.controllers;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        return ResponseEntity.ok("Hello User");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> helloA() {

        return ResponseEntity.ok("Hello Admin");
    }
}
