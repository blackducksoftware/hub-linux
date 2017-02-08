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

import com.blackducksoftware.integration.hub.linux.BdioComponentDetails
import com.blackducksoftware.integration.hub.linux.OperatingSystemEnum
import com.blackducksoftware.integration.hub.linux.PackageManagerEnum

class DpkgExtractor extends Extractor {
    @PostConstruct
    void init() {
        initValues(PackageManagerEnum.DPKG)
    }

    List<BdioComponentDetails> extract(OperatingSystemEnum operatingSystemEnum, File yumOutput) {
        def components = []

        boolean startOfComponents = false
        yumOutput.eachLine { line ->
            if (line != null) {
                if (line.matches("\\+\\+\\+-=+-=+-=+-=+")) {
                    startOfComponents = true
                } else if (startOfComponents){
                    String componentInfo = line.substring(3)
                    def(name,version,architecture,description) = componentInfo.tokenize(" ")

                    String externalId = "$name/$version/$architecture"
                    def bdioComponentDetails = createBdioComponentDetails(operatingSystemEnum, name, version, externalId)
                    components.add(bdioComponentDetails)
                }
            }
        }
        components
    }
}
