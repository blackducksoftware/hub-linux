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
    void testExtractingCentosRpmFile1() {
        extractingCentosRpm("centos_rpm_output_1.txt");
    }

    @Test
    void testExtractingCentosRpmFile2() {
        extractingCentosRpm("centos_rpm_output_2.txt");
    }

    @Test
    void testExtractingCentosRpmFile3() {
        extractingCentosRpm("centos_rpm_output_3.txt");
    }

    void extractingCentosRpm(String fileName) {
        final CentosRpmExtractor extractor = new CentosRpmExtractor()
        final URL url = this.getClass().getResource("/$fileName")
        final File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"))

        List<BdioComponentDetails> bdioEntries = extractor.extract(file)

        for (final BdioComponentDetails bdioEntry : bdioEntries) {
            if (bdioEntry != null) {
                System.out.println(bdioEntry.getExternalIdentifier())
            }
        }
        println(bdioEntries.size())
    }
}
