package com.blackducksoftware.integration.hub.linux

import org.junit.Assert
import org.junit.Test

class BdioFileWriterTest {
    @Test
    void testCreatingCentosExternalIdentifier() {
        String operatingSystem = 'CentOS Linux'
        def bdioFileWriter = new BdioFileWriter()
        def externalIdentifier = bdioFileWriter.createLinuxIdentifier(operatingSystem)
        Assert.assertEquals('centos', externalIdentifier.externalSystemTypeId)
    }
}
