package de.schroenser.timera;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(originPatterns = "http://localhost:3000")
public class HelloWorldController
{
    @GetMapping("hello")
    public String get()
    {
        return "Hello World";
    }
}
