package com.blackducksoftware.integration.hub.linux.extractor

import com.blackducksoftware.bdio.model.ExternalIdentifier
import com.blackducksoftware.integration.hub.linux.BdioComponentDetails
import com.blackducksoftware.integration.hub.linux.FileSuffixEnum
import com.blackducksoftware.integration.hub.linux.OSEnum

abstract class Extractor {
    FileSuffixEnum fileSuffixEnum

    abstract void init()
    abstract List<BdioComponentDetails> extract(String operatingSystem, File inputFile)

    void initValues(FileSuffixEnum fileSuffixEnum) {
        this.fileSuffixEnum = fileSuffixEnum
    }

    boolean shouldAttemptExtract(File file) {
        fileSuffixEnum.fileMatches(file)
    }

    BdioComponentDetails createBdioComponentDetails(String operatingSystem, String name, String version, String externalId) {
        def externalIdentifier = createLinuxIdentifier(operatingSystem, externalId)
        new BdioComponentDetails(name: name, version: version, externalIdentifier: externalIdentifier)
    }

    ExternalIdentifier createLinuxIdentifier(String operatingSystem, String externalId) {
        OSEnum os = OSEnum.determineOperatingSystem(operatingSystem)
        def externalIdentifier = new ExternalIdentifier()
        externalIdentifier.externalSystemTypeId = os.forge
        externalIdentifier.externalId = externalId

        externalIdentifier
    }
}
