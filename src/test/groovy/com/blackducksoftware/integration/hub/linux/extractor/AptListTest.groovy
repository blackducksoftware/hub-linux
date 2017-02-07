package com.blackducksoftware.integration.hub.linux.extractor;

import org.junit.Test

import com.blackducksoftware.integration.hub.linux.BdioComponentDetails

class AptListTest {

    @Test
    void testExtractingAptFile1() {
        AptExtractor extractor = new AptExtractor()
        URL url = this.getClass().getResource("/ubuntu_apt_package_list_1.txt")
        File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"))
        List<BdioComponentDetails> componentDetails =extractor.extract(file);
        println(componentDetails.size())
    }

    @Test
    void testExtractingAptFile2() {
        AptExtractor extractor = new AptExtractor()
        URL url = this.getClass().getResource("/ubuntu_apt_package_list_2.txt")
        File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"))
        List<BdioComponentDetails> componentDetails =extractor.extract(file);
        println(componentDetails.size())
    }
}
