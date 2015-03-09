package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MonitoringClientApplication {
	  private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringClientApplication.class);
    
    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        LOGGER.info("responding with: "+name);
    	return "Hello "+name;
    }

    public static void main(String[] args) {
        SpringApplication.run(MonitoringClientApplication.class, args);
    }
}
