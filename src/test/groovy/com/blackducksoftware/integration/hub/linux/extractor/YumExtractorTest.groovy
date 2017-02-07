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

class YumExtractorTest {

    @Test
    public void extractYumComponentsFile1(){
        extractYumComponentsFromFile("centos_yum_output_1.txt")
    }

    @Test
    public void extractYumComponentsFile2(){
        extractYumComponentsFromFile("centos_yum_output_2.txt")
    }

    @Test
    public void extractYumComponentsFile3(){
        extractYumComponentsFromFile("centos_yum_output_3.txt")
    }

    public void extractYumComponentsFromFile(String fileName){
        URL url = this.getClass().getResource("/$fileName")
        File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));

        YumExtractor extractor = new YumExtractor()
        List<BdioComponentDetails> componentDetails =  extractor.extract(file)
        println(componentDetails.size())
    }
}
