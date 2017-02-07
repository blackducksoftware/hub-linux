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

import com.blackducksoftware.bdio.model.ExternalIdentifierBuilder
import com.blackducksoftware.integration.hub.linux.BdioComponentDetails

@Component
class AptExtractor extends Extractor {
    private final Logger logger = LoggerFactory.getLogger(AptExtractor.class)

    @Autowired
    ExternalIdentifierBuilder externalIdentifierBuilder

    @Override
    boolean shouldAttemptExtract(File file) {
        file.name.endsWith('_apt.txt')
    }

    @Override
    List<BdioComponentDetails> extract(String operatingSystem, File inputFile) {
        def components = []

        inputFile.eachLine { line ->
            def component = extract(operatingSystem, line)
            if (component != null) {
                components.add(component)
            }
        }

        components
    }

    BdioComponentDetails extract(String operatingSystem, String inputLine) {
        def bdioComponentDetails = null

        if (inputLine.contains(' ')) {
            def (packageName, version) = inputLine.split(" ")
            def index = packageName.indexOf("/")
            if(index > 0) {
                def component = packageName.substring(0,index)
                String externalId = "${component}/${version}"
                bdioComponentDetails = createBdioComponentDetails(operatingSystem, packageName, version, externalId)
            }
        }

        bdioComponentDetails
    }
}
