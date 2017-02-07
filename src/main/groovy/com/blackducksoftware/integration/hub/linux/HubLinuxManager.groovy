package com.blackducksoftware.integration.hub.linux

import groovy.io.FileType

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import com.blackducksoftware.integration.hub.linux.creator.Creator
import com.blackducksoftware.integration.hub.linux.extractor.Extractor
import com.blackducksoftware.integration.hub.util.HostnameHelper

@Component
class HubLinuxManager {
    private final Logger logger = LoggerFactory.getLogger(HubLinuxManager.class)

    @Value('${working.directory}')
    String workingDirectoryPath

    @Value('${command.timeout}')
    long commandTimeout

    @Autowired
    OperatingSystemFinder operatingSystemFinder

    @Autowired
    HubClient hubClient

    @Autowired
    BdioFileWriter bdioFileWriter

    @Autowired
    List<Creator> creators

    @Autowired
    List<Extractor> extractors

    void performInspection() {
        String operatingSystemName = operatingSystemFinder.determineOperatingSystem()
        OSEnum os = OSEnum.determineOperatingSystem(operatingSystemName)
        String projectName = os.forge + "-" + HostnameHelper.getMyHostname()
        String projectVersionName = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())

        def workingDirectory = new File(workingDirectoryPath)
        workingDirectory.mkdirs()

        creators.each {
            println it.filenameSuffix
            if (it.isCommandAvailable(commandTimeout)) {
                println 'trying to create file'
                String filename = "${os.forge}${it.filenameSuffix}"
                File outputFile = new File(workingDirectory, filename)
                it.writeOutputFile(outputFile, commandTimeout)
                println 'created file'
            } else {
                println 'command not available'
            }
        }

        def bdioComponentDetails = []
        workingDirectory.eachFile(FileType.FILES) { file ->
            println file.name
            extractors.each { extractor ->
                if (extractor.shouldAttemptExtract(file)) {
                    def extractedComponents = extractor.extract(os.forge, file)
                    bdioComponentDetails.addAll(extractedComponents)
                }
            }
        }

        def outputFile = new File(workingDirectory, "${projectName}_bdio.jsonld")
        logger.info("Starting bdio creation using file: ${outputFile.canonicalPath}")
        new FileOutputStream(outputFile).withStream { outputStream ->
            def bdioWriter = bdioFileWriter.createBdioWriter(outputStream, projectName, projectVersionName)
            try {
                bdioComponentDetails.each { component ->
                    bdioFileWriter.writeComponent(bdioWriter, component)
                }
            } finally {
                bdioWriter.close()
            }
        }

        if (hubClient.isValid()) {
            hubClient.uploadBdioToHub(outputFile)
            logger.info("Uploaded bdio to ${hubClient.hubUrl}")
        }
    }
}
