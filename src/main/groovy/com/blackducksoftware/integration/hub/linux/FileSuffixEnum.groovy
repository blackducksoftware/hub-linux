package com.blackducksoftware.integration.hub.linux

enum FileSuffixEnum {
    APT('_apt.txt'),
    YUM('_yum.txt'),
    RPM('_rpm.txt'),
    DPKG('_dpkg.txt')

    String suffix

    private FileSuffixEnum(String suffix) {
        this.suffix = suffix
    }

    public boolean fileMatches(File file) {
        file.name.endsWith(suffix)
    }
}
