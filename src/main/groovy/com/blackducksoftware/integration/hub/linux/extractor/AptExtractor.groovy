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
class AptExtractor {
    private final Logger logger = LoggerFactory.getLogger(AptExtractor.class)

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

        if (inputLine.contains(' ')) {
            def (packageName,version) = inputLine.split(" ")
            def externalIdentifier = createExternalIdentifier(packageName, version)
            details = new BdioComponentDetails(name: packageName, version: version, externalIdentifier: externalIdentifier)
        }

        details
    }

    ExternalIdentifier createExternalIdentifier(String packageName, String version) {
        def externalIdentifier = new ExternalIdentifier();
        externalIdentifier.setExternalSystemTypeId('ubuntu');
        externalIdentifier.setExternalId("${packageName}/${version}");

        externalIdentifier
    }
}
