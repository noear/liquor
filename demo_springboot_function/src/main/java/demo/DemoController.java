package demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author phanes
 */
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class DemoController {

    private final DemoService demoService;

    @GetMapping(path = {"", "/hello"})
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return demoService.sayHello(name);
    }

}