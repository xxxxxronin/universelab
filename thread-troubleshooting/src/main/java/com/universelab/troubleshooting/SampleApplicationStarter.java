package com.universelab.troubleshooting;

import com.uranus.common.MySpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
public class SampleApplicationStarter {

    public static void main(String[] args) throws Exception{
        MySpringApplication.build()
                .disableES()
                .disableEurekaClient()
                .disableMongoDB().disableMySQL()
                .run(SampleApplicationStarter.class,args);
    }
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(2)).build();
    }

    @GetMapping("/getMsg")
    public String getMsg() {
        try {
            String demo = restTemplate.getForObject("http://localhost:8080/slowApi", String.class);
            return ">>>" + demo;

        } catch (Exception e) {
            return "failed!";
        }
    }

    @GetMapping("/slowApi")
    public String slowApi() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return "ok!";
    }
}
