package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MonitoringClientApplication {
    public static final String CLIENT_NAME = "monitorClientApp";
    
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
    	return "Hello "+name;
    }

    public static void main(String[] args) {
        SpringApplication.run(MonitoringClientApplication.class, args);
    }
}
