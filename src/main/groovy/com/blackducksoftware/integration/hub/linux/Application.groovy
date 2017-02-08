package com.blackducksoftware.integration.hub.linux

import javax.annotation.PostConstruct

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class Application {
    private final Logger logger = LoggerFactory.getLogger(Application.class)

    @Autowired
    HubClient hubClient

    @Autowired
    HubLinuxManager hubLinuxManager

    static void main(final String[] args) {
        new SpringApplicationBuilder(Application.class).logStartupInfo(false).run(args)
    }

    @PostConstruct
    void init() {
        println 'Hello Team!!!'

        try {
            hubClient.testHubConnection()
            println 'Your Hub configuration is valid and a successful connection to the Hub was established.'
        } catch (Exception e) {
            println("Your Hub configuration is not valid: ${e.message}")
        }

        hubLinuxManager.performInspection()
    }
}
