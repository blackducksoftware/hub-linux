/*
 * Copyright (C) 2017 Black Duck Software Inc.
 * http://www.blackducksoftware.com/
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of
 * Black Duck Software ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Black Duck Software.
 */
package com.blackducksoftware.integration.hub.linux.extractor;

import static org.junit.Assert.*

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import com.blackducksoftware.integration.hub.linux.BdioComponentDetails

class CentosRpmExtractorTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    void testExtractingCentosRpm() {
        final CentosRpmExtractor extractor = new CentosRpmExtractor()
        final URL url = this.getClass().getResource("/centos_rpm_output.txt")
        final File file = new File(url.getFile())

        List<BdioComponentDetails> bdioEntries = extractor.extract(file)
        assertEquals(635, bdioEntries.size())
        boolean foundTargetEntry = false
        int validEntryCount = 0
        for (final BdioComponentDetails bdioEntry : bdioEntries) {
            if (bdioEntry != null) {
                validEntryCount++
                System.out.println(bdioEntry.getExternalIdentifier())
                if ("perl-Data-Dumper/2.145-3.el7/x86_64".contentEquals(bdioEntry.getExternalIdentifier().getExternalId())) {
                    foundTargetEntry = true
                    assertEquals("perl-Data-Dumper", bdioEntry.getName())
                    assertEquals("2.145-3.el7", bdioEntry.getVersion())
                }
            }
        }
        assertTrue(foundTargetEntry)
    }
}
