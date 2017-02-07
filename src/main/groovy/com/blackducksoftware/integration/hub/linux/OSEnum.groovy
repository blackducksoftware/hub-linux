package com.blackducksoftware.integration.hub.linux

import org.apache.commons.lang3.StringUtils

enum OSEnum {
    CENTOS ('centos'),
    DEBIAN('debian'),
    FEDORA('fedora'),
    UBUNTU('ubuntu'),
    REDHAT('redhat'),
    UNKNOWN('unknown')

    String forge

    private OSEnum(String forge) {
        this.forge = forge
    }

    static determineOperatingSystem(String operatingSystemName) {
        String toCheck = StringUtils.trimToEmpty(operatingSystemName).toLowerCase()
        if (toCheck.contains('centos')) {
            return CENTOS
        } else if (toCheck.contains('debian')) {
            return DEBIAN
        } else if (toCheck.contains('fedora')) {
            return FEDORA
        } else if (toCheck.contains('ubuntu')) {
            return UBUNTU
        } else if (toCheck.contains('redhat')) {
            return REDHAT
        } else {
            return UNKNOWN
        }
    }
}
