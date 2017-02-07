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

import org.springframework.stereotype.Component

import com.blackducksoftware.integration.hub.linux.BdioComponentDetails

@Component
class YumExtractor extends Extractor {
    boolean shouldAttemptExtract(File file) {
        file.name.endsWith('_yum.txt')
    }

    List<BdioComponentDetails> extract(String operatingSystem, File yumOutput) {
        def components = []
        boolean startOfComponents = false

        def componentColumns = []
        yumOutput.eachLine { line ->
            if (line != null) {
                if ('Installed Packages' == line) {
                    startOfComponents = true
                } else if ('Available Packages' == line) {
                    return components
                } else if (startOfComponents) {
                    componentColumns.addAll(line.tokenize(' '))
                    if (componentColumns.size() == 3) {
                        String nameArch = componentColumns.get(0)
                        String version = componentColumns.get(1)
                        String name =nameArch.substring(0, nameArch.lastIndexOf("."))
                        String architecture = nameArch.substring(nameArch.lastIndexOf(".") + 1)

                        String externalId = "$name/$version/$architecture"
                        def bdioComponentDetails = createBdioComponentDetails(operatingSystem, name, version, externalId)

                        components.add(bdioComponentDetails)
                        componentColumns = []
                    } else  if (componentColumns.size() > 3) {
                        //FIXME
                        throw new RuntimeException ("Parsing multi-line components has failed.")
                    }
                }
            }
        }

        components
    }
}
