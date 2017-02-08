package com.blackducksoftware.integration.hub.linux.extractor

import com.blackducksoftware.bdio.model.ExternalIdentifier
import com.blackducksoftware.integration.hub.linux.BdioComponentDetails
import com.blackducksoftware.integration.hub.linux.OperatingSystemEnum
import com.blackducksoftware.integration.hub.linux.PackageManagerEnum

abstract class Extractor {
    PackageManagerEnum packageManagerEnum

    abstract void init()
    abstract List<BdioComponentDetails> extract(OperatingSystemEnum operatingSystemEnum, File inputFile)

    void initValues(PackageManagerEnum packageManagerEnum) {
        this.packageManagerEnum = packageManagerEnum
    }

    boolean shouldAttemptExtract(File file) {
        packageManagerEnum.fileMatches(file)
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
