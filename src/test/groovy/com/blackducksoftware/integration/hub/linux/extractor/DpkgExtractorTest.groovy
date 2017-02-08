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

import org.junit.Test

import com.blackducksoftware.integration.hub.linux.BdioComponentDetails

class DpkgExtractorTest {
    @Test
    public void extractDpkgComponentsFile1(){
        extractDpkgComponentsFromFile("ubuntu_dpkg_output_1.txt")
    }

    @Test
    public void extractDpkgComponentsFile2(){
        extractDpkgComponentsFromFile("ubuntu_dpkg_output_2.txt")
    }


    public void extractDpkgComponentsFromFile(String fileName){
        URL url = this.getClass().getResource("/$fileName")
        File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"))

        DpkgExtractor extractor = new DpkgExtractor()
        List<BdioComponentDetails> componentDetails =  extractor.extract("CentOS", file)
        println(componentDetails.size())
    }
}
