package com.blackducksoftware.integration.hub.linux

enum PackageManagerEnum {
    APT('_apt.txt'),
    YUM('_yum.txt'),
    RPM('_rpm.txt'),
    DPKG('_dpkg.txt')

    String filenameSuffix

    private FileSuffixEnum(String filenameSuffix) {
        this.filenameSuffix = filenameSuffix
    }

    public boolean fileMatches(File file) {
        file.name.endsWith(filenameSuffix)
    }
}
