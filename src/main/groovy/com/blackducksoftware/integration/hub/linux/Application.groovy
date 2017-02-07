package com.blackducksoftware.integration.hub.linux

import javax.annotation.PostConstruct

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean

import com.blackducksoftware.bdio.model.ExternalIdentifierBuilder
import com.blackducksoftware.integration.hub.linux.creator.AptCreator

@SpringBootApplication
class Application {
    private final Logger logger = LoggerFactory.getLogger(Application.class)

    @Autowired
    HubClient hubClient

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

        try {
            def directory = new File(".")
            def creator = new AptCreator()

            if(creator.isCommandAvailable()) {
                def file = creator.createOutputFile(directory,"apt_extractor_input.txt")
                if(file.exists()) {
                    logger.info("File exists!!!!")
                } else {
                    logger.info("File does not exist!")
                }
            }
        } catch(Exception e) {
            println("Error creating output file ${e.message}")
        }
    }

    @Bean
    ExternalIdentifierBuilder externalIdentifierBuilder() {
        ExternalIdentifierBuilder.create()
    }
}
