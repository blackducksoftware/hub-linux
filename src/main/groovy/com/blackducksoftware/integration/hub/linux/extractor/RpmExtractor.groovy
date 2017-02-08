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
import com.blackducksoftware.integration.hub.linux.OperatingSystemEnum
import com.blackducksoftware.integration.hub.linux.PackageManagerEnum

@Component
class RpmExtractor extends Extractor {
    private final Logger logger = LoggerFactory.getLogger(RpmExtractor.class)

    @PostConstruct
    void init() {
        initValues(PackageManagerEnum.RPM)
    }

    @Override
    List<BdioComponentDetails> extract(OperatingSystemEnum operatingSystemEnum, File inputFile) {
        def components = []
        inputFile.eachLine { line ->
            components.add(extract(operatingSystemEnum, line))
        }

        components
    }

    BdioComponentDetails extract(OperatingSystemEnum operatingSystemEnum, String inputLine) {
        if (valid(inputLine)) {
            def lastDotIndex = inputLine.lastIndexOf('.')
            def arch = inputLine.substring(lastDotIndex + 1)
            def lastDashIndex = inputLine.lastIndexOf('-')
            def nameVersion = inputLine.substring(0, lastDashIndex)
            def secondToLastDashIndex = nameVersion.lastIndexOf('-')

            def versionRelease = inputLine.substring(secondToLastDashIndex + 1, lastDotIndex)
            def artifact = inputLine.substring(0, secondToLastDashIndex)

            String externalId = "${artifact}/${versionRelease}/${arch}"
            return createBdioComponentDetails(operatingSystemEnum, artifact, versionRelease, externalId)
        }

        null
    }

    boolean valid(String inputLine) {
        inputLine.matches(".+-.+-.+\\..*")
    }
}
