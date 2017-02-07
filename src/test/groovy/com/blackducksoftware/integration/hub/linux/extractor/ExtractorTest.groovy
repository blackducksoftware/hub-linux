package com.blackducksoftware.integration.hub.linux.extractor

import org.junit.Assert
import org.junit.Test

class ExtractorTest {
    @Test
    void testCreatingCentosExternalIdentifier() {
        String operatingSystem = 'CentOS Linux'
        def extractor = [extract: {File file -> null}, extract: {String s -> null}] as Extractor
        def externalIdentifier = extractor.createLinuxIdentifier(operatingSystem, 'name/version')
        Assert.assertEquals('centos', externalIdentifier.externalSystemTypeId)
        Assert.assertEquals('name/version', externalIdentifier.externalId)
    }
}
