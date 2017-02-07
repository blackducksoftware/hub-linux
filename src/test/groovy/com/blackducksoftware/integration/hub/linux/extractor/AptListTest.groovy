package com.blackducksoftware.integration.hub.linux.extractor;

import org.junit.Test

class AptListTest {

    @Test
    void testExtractingApt() {
        AptExtractor extractor = new AptExtractor()
        URL url = this.getClass().getResource("/apt_package_list.txt")
        File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"))
        extractor.extract(file);
    }
}
