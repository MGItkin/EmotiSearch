package apples.spring.mobile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GreetingController
{
    @RequestMapping("/index")
    public String greeting() {
        return "greeting";
    }

}
