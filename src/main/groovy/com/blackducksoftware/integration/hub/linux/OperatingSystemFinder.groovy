package com.blackducksoftware.integration.hub.linux

import org.springframework.stereotype.Component

@Component
class OperatingSystemFinder {
    String determineOperatingSystem() {
        def linuxFlavorFile = new File('/etc/*-release')
        linuxFlavorFile.eachLine {
            println it
            if (it.startsWith('ID=')) {
                return it.replace('ID=').trim()
            }
        }

        ''
    }
}
