package com.blackducksoftware.integration.hub.linux

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

    void performExtractFromRemoteInspection() {
    }

    void performExtractFromLocalInspection() {
        OperatingSystemEnum operatingSystemEnum = operatingSystemFinder.determineOperatingSystem()
        String projectName = operatingSystemEnum.forge + "-" + HostnameHelper.getMyHostname()
        String projectVersionName = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())

        logger.info("Operating System: {}",operatingSystemEnum.forge)
        logger.info("Project Name:     {}",projectName)
        logger.info("Project Version:  {}",projectVersionName)

        def workingDirectory = new File(workingDirectoryPath)
        workingDirectory.mkdirs()

        creators.each {
            logger.info "Starting file creation with ${it.getClass().name}"
            if (!it.isCommandAvailable(commandTimeout)) {
                logger.info("Can't create with ${it.getClass().name} - command is not available.")
            } else{
                String filename = it.filename(operatingSystemEnum.forge)
                File outputFile = new File(workingDirectory, filename)
                it.writeOutputFile(outputFile, commandTimeout)
                logger.info('Created file ${outputFile.canonicalPath}')
            }
        }

        def allExtractionResults = extractAllBdioComponentDetailsFromWorkingDirectory(workingDirectory)

        createAndUploadBdioFile(workingDirectory, projectName, projectVersionName, allExtractionResults)
    }

    private List<ExtractionResults> extractAllBdioComponentDetailsFromWorkingDirectory(File workingDirectory) {
        def allExtractionResults = []
        //        workingDirectory.eachFile(FileType.FILES) { file ->
        //            logger.info("Processing file ${file.name}")
        //            extractors.each { extractor ->
        //                if (extractor.shouldAttemptExtract(file)) {
        //                    logger.info("Extracting ${file.name} with ${extractor.getClass().name}")
        //                    def extractedComponents = extractor.extract(operatingSystemEnum.forge, file)
        //                    bdioComponentDetails.addAll(extractedComponents)
        //                }
        //            }
        //        }
        //logger.info "Found ${bdioComponentDetails.size()} components."
        allExtractionResults
    }

    private createAndUploadBdioFile(File workingDirectory, String projectName, String projectVersionName, List<ExtractionResults> allExtractionResults) {
        def outputFile = new File(workingDirectory, "${projectName}_bdio.jsonld")
        logger.info("Starting bdio creation using file: ${outputFile.canonicalPath}")
        //        new FileOutputStream(outputFile).withStream { outputStream ->
        //            def bdioWriter = bdioFileWriter.createBdioWriter(outputStream, projectName, projectVersionName)
        //            try {
        //                bdioComponentDetailsList.each { bdioComponentDetails ->
        //                    bdioFileWriter.writeComponent(bdioWriter, bdioComponentDetails)
        //                }
        //            } finally {
        //                bdioWriter.close()
        //            }
        //        }

        if (hubClient.isValid()) {
            hubClient.uploadBdioToHub(outputFile)
            logger.info("Uploaded bdio to ${hubClient.hubUrl}")
        }
    }
}