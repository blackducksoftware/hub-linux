package com.blackducksoftware.integration.hub.linux.extractor

import static org.junit.Assert.*

import org.junit.Test

class ExtractorTest {
    @Test
    void testCreatingCentosExternalIdentifier() {
        String operatingSystem = 'CentOS Linux'
        def extractor = [init: {}] as Extractor
        def externalIdentifier = extractor.createLinuxIdentifier(operatingSystem, 'name/version')
        assertEquals('centos', externalIdentifier.externalSystemTypeId)
        assertEquals('name/version', externalIdentifier.externalId)
    }
}
