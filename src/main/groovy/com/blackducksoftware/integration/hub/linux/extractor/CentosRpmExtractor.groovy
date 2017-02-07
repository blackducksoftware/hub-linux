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
package com.blackducksoftware.integration.hub.linux.extractor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.blackducksoftware.bdio.model.ExternalIdentifier
import com.blackducksoftware.bdio.model.ExternalIdentifierBuilder
import com.blackducksoftware.integration.hub.linux.BdioComponentDetails

@Component
class CentosRpmExtractor {
    private final Logger logger = LoggerFactory.getLogger(CentosRpmExtractor.class)

    @Autowired
    ExternalIdentifierBuilder externalIdentifierBuilder

    List<BdioComponentDetails> extract(File inputFile) {
        def components = []
        inputFile.eachLine { line ->
            components.add(extract(line))
        }

        components
    }

    BdioComponentDetails extract(String inputLine) {
        def details = null

        if (valid(inputLine)) {

            final int lastDotIndex = inputLine.lastIndexOf('.')
            final String arch = inputLine.substring(lastDotIndex + 1)
            final int lastDashIndex = inputLine.lastIndexOf('-')
            final String nameVersion = inputLine.substring(0, lastDashIndex)
            final int secondToLastDashIndex = nameVersion.lastIndexOf('-')

            final String versionRelease = inputLine.substring(secondToLastDashIndex + 1, lastDotIndex)
            final String artifact = inputLine.substring(0, secondToLastDashIndex)

            def externalIdentifier = createExternalIdentifier(artifact, versionRelease, arch)
            details = new BdioComponentDetails(name: artifact, version: versionRelease, externalIdentifier: externalIdentifier)
        }

        details
    }



    ExternalIdentifier createExternalIdentifier(String packageName, String version, String arch) {
        def externalIdentifier = new ExternalIdentifier();
        externalIdentifier.setExternalSystemTypeId('centos');
        externalIdentifier.setExternalId("${packageName}/${version}/${arch}");

        externalIdentifier
    }

    boolean valid(String inputLine) {
        if (inputLine.matches(".+-.+-.+\\..*")) {
            true
        } else {
            false
        }
    }
}
