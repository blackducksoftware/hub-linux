package com.blackducksoftware.integration.hub.linux

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import com.blackducksoftware.integration.hub.linux.extractor.Extractor
import com.blackducksoftware.integration.hub.util.HostnameHelper

import groovy.io.FileType

@Component
class HubLinuxManager {
    private final Logger logger = LoggerFactory.getLogger(HubLinuxManager.class)

    @Value('${working.directory}')
    String workingDirectoryPath

    @Autowired
    HubClient hubClient

    @Autowired
    BdioFileWriter bdioFileWriter

    @Autowired
    List<Extractor> extractors

    void performInspection() {
        String operatingSystem = "CentOS"
        String projectName = operatingSystem + "-" + HostnameHelper.getMyHostname()
        String projectVersionName = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())

        def filenameFilter = [accept: {File dir, String name -> name.endsWith(".txt")}] as FilenameFilter

        def workingDirectory = new File(workingDirectoryPath)
        def bdioComponentDetails = []
        File[] files = workingDirectory.listFiles(filenameFilter)
        workingDirectory.eachFile(FileType.FILES) { file ->
            println file.name
            extractors.each { extractor ->
                if (extractor.shouldAttemptExtract(file)) {
                    def extractedComponents = extractor.extract(operatingSystem, file)
                    bdioComponentDetails.addAll(extractedComponents)
                }
            }
        }

        def outputFile = new File(workingDirectory, "${operatingSystem}_bdio.jsonld")
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
