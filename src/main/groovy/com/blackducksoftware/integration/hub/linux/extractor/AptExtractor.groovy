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

import javax.annotation.PostConstruct

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import com.blackducksoftware.integration.hub.linux.BdioComponentDetails
import com.blackducksoftware.integration.hub.linux.FileSuffixEnum

@Component
class AptExtractor extends Extractor {
    private final Logger logger = LoggerFactory.getLogger(AptExtractor.class)

    @PostConstruct
    void init() {
        initValues(FileSuffixEnum.APT)
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
        if (inputLine.contains(' ')) {
            def (packageName, version) = inputLine.split(' ')
            def index = packageName.indexOf('/')
            if (index > 0) {
                def component = packageName.substring(0, index)
                String externalId = "${component}/${version}"
                return createBdioComponentDetails(operatingSystem, packageName, version, externalId)
            }
        }

        null
    }
}
