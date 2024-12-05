package de.schroenser.timera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import de.schroenser.timera.jira.configuration.JiraProperties;

@SpringBootApplication
@EnableConfigurationProperties(JiraProperties.class)
public class TimeraApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(TimeraApplication.class, args);
    }
}
