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
        URL url = this.getClass().getResource("/centos_yum_output_1.txt")
        File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));

        YumExtractor extractor = new YumExtractor()
        List<BdioComponentDetails> componentDetails =  extractor.extract(file)
        println(componentDetails.size())
    }

    @Test
    public void extractYumComponentsFile2(){
        URL url = this.getClass().getResource("/centos_yum_output_2.txt")
        File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"));

        YumExtractor extractor = new YumExtractor()
        List<BdioComponentDetails> componentDetails =  extractor.extract(file)
        println(componentDetails.size())
    }
}
