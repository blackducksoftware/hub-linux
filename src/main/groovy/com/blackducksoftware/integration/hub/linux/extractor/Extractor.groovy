package com.blackducksoftware.integration.hub.linux.extractor

import org.springframework.beans.factory.annotation.Value

import com.blackducksoftware.bdio.model.ExternalIdentifier
import com.blackducksoftware.integration.hub.linux.BdioComponentDetails
import com.blackducksoftware.integration.hub.linux.ExtractionResults
import com.blackducksoftware.integration.hub.linux.OperatingSystemEnum
import com.blackducksoftware.integration.hub.linux.PackageManagerEnum

abstract class Extractor {
    @Value('${filename.separator}')
    String filenameSeparator

    PackageManagerEnum packageManagerEnum

    abstract void init()
    //abstract List<BdioComponentDetails> extractComponents(OperatingSystemEnum operatingSystemEnum, File inputFile)

    abstract List<BdioComponentDetails> extractComponents(File inputFile)

    void initValues(PackageManagerEnum packageManagerEnum) {
        this.packageManagerEnum = packageManagerEnum
    }

    boolean shouldAttemptExtract(File file) {
        packageManagerEnum.fileMatches(file)
    }

    ExtractionResults extract(File inputFile) {
        def (hubProjectName, hubProjectVersionName, forge, packageManager) = inputFile.name.split(filenameSeparator)
        OperatingSystemEnum operatingSystemEnum = OperatingSystemEnum.determineOperatingSystem(forge)

        def components = extractComponents(inputFile)

        new ExtractionResults(hubProjectName: hubProjectName, hubProjectVersionName: hubProjectVersionName, operatingSystemEnum: operatingSystemEnum, bdioComponentDetailsList: components)
    }

    void addToBdioComponentDetails(List<BdioComponentDetails> componentDetails, String name, String version, String externalId) {
        OperatingSystemEnum.each { operatingSystem ->
            createBdioComponentDetails(operatingSystem, name, version,externalId)
            componentDetails.add(createBdioComponentDetails(operatingSystem, name, version, externalId))
        }
    }

    BdioComponentDetails createBdioComponentDetails(OperatingSystemEnum operatingSystemEnum, String name, String version, String externalId) {
        def externalIdentifier = createLinuxIdentifier(operatingSystemEnum, externalId)
        new BdioComponentDetails(name: name, version: version, externalIdentifier: externalIdentifier)
    }

    ExternalIdentifier createLinuxIdentifier(OperatingSystemEnum operatingSystemEnum, String externalId) {
        def externalIdentifier = new ExternalIdentifier()
        externalIdentifier.externalSystemTypeId = operatingSystemEnum.forge
        externalIdentifier.externalId = externalId

        externalIdentifier
    }
}
