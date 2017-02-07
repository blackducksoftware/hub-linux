package com.blackducksoftware.integration.hub.linux.extractor;

import org.junit.Test

import com.blackducksoftware.integration.hub.linux.BdioComponentDetails

class AptListTest {

    @Test
    void testExtractingAptFile1() {
        extractAptComponentsFromFile("ubuntu_apt_package_list_1.txt");
    }

    @Test
    void testExtractingAptFile2() {
        extractAptComponentsFromFile("ubuntu_apt_package_list_2.txt");
    }

    @Test
    void testExtractingAptFile3() {
        extractAptComponentsFromFile("ubuntu_apt_package_list_3.txt");
    }

    public void extractAptComponentsFromFile(String fileName){
        AptExtractor extractor = new AptExtractor()
        URL url = this.getClass().getResource("/$fileName")
        File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"))
        List<BdioComponentDetails> componentDetails =extractor.extract(file);
        println(componentDetails.size())
    }
}
