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

import com.blackducksoftware.bdio.model.ExternalIdentifier
import com.blackducksoftware.integration.hub.linux.BdioComponentDetails

class YumExtractor {


    List<BdioComponentDetails> extract(File yumOutput) {
        def components = []
        boolean startOfComponents = false;
        yumOutput.eachLine { line ->
            if(line != null){
                if('Installed Packages' == line){
                    startOfComponents = true;
                } else if('Available Packages' == line){
                    return components
                } else if(startOfComponents){
                    BdioComponentDetails componentDetails = extract(line.trim())
                    if(componentDetails != null){
                        components.add(componentDetails)
                    }
                }
            }
        }
        components
    }

    BdioComponentDetails extract(String yumOutputLine) {
        def (nameArch, version, info) = yumOutputLine.tokenize(" ")
        if(nameArch != null && version != null){
            String name =nameArch.substring(0, nameArch.lastIndexOf("."));
            String architecture = nameArch.substring(nameArch.lastIndexOf(".") + 1);

            return buildComponent(name,  version,  architecture)
        }
    }

    private BdioComponentDetails buildComponent(String name, String version, String architecture) {
        def component = new BdioComponentDetails();
        component.id = UUID.randomUUID()
        component.name = name
        component.version = version

        def ExternalIdentifier externalId = new ExternalIdentifier();
        externalId.setExternalId("$name/$version/$architecture");
        component.setExternalIdentifier(externalId)

        component
    }
}
