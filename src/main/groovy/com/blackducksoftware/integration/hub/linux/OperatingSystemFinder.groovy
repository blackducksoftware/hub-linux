package com.blackducksoftware.integration.hub.linux

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OperatingSystemFinder {
    @Value('${linux.distro}')
    String linuxDistro

    @Value('${command.timeout}')
    long commandTimeout

    String determineOperatingSystem() {
        if (linuxDistro) {
            return linuxDistro
        }

        def proc = 'lsb_release -a'.execute()
        proc.waitForOrKill(commandTimeout)

        String output = proc.inputStream.text
        output.tokenize(System.lineSeparator()).each {
            if (it.startsWith('Distributor ID:')) {
                return it.replace('Distributor ID:', '').trim()
            }
        }

        ''
    }
}