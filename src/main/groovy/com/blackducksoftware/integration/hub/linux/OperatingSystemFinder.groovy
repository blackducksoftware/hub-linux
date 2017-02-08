package com.blackducksoftware.integration.hub.linux

import javax.annotation.PostConstruct

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OperatingSystemFinder {
    private final Logger logger = LoggerFactory.getLogger(OperatingSystemFinder.class)
    @Value('${linux.distro:""}')
    String linuxDistro

    @Value('${command.timeout}')
    long commandTimeout

    def commandCheckList = []

    @PostConstruct
    void init() {
        def cmdObject = [command: 'lsb_release -a', prefixMatch:'Distributor ID:', delimeter:':']
        commandCheckList.add(cmdObject)
    }

    String determineOperatingSystem() {
        //        if (!StringUtils.isBlank(linuxDistro)) {
        //            // make sure it isn't empty
        //            return linuxDistro
        //        }

        def osName = commandCheckList.findResult {
            check(it.command,it.prefixMatch,it.delimeter)
        }

        osName
    }

    private String check(String command, String prefixMatch, String delimeter) {
        def osName = null
        try {
            def proc = command.execute()
            proc.waitForOrKill(commandTimeout)

            if(proc.exitValue() == 0) {
                String output = proc.inputStream.text
                logger.debug("Distribution Check Command Output: \n {}",output)
                osName = output.tokenize(System.lineSeparator()).find {
                    it.trim().startsWith(prefixMatch)
                }

                def (description, value) = osName.split(delimeter)
                def replacedString = value.trim()
                osName = replacedString
            }
        } catch(Exception e) {
            logger.error("Error executing command {}", command,e)
            osName = null
        }

        osName
    }

    private String lsbCommandCheck() {
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

    private String lsbReleaseFileCheck() {
        def proc = 'cat /etc/lsb-release'.execute()
        proc.waitForOrKill(commandTimeout)

        String output = proc.inputStream.text
        output.tokenize(System.lineSeparator()).each {
            if (it.startsWith('DISTRIB_ID=')) {
                return it.replace('DISTRIB_ID=', '').trim()
            }
        }

        ''
    }

    private String issueFileCheck() {
        def proc = 'cat /etc/issue.net'.execute()
        proc.waitForOrKill(commandTimeout)

        String output = proc.inputStream.text
        output.tokenize(System.lineSeparator()).each {
            if (it.startsWith('DISTRIB_ID=')) {
                return it.replace('DISTRIB_ID=', '').trim()
            }
        }

        ''
    }

    private String issueNetFileCheck() {
        def proc = 'cat /etc/issue'.execute()
        proc.waitForOrKill(commandTimeout)

        String output = proc.inputStream.text
        output.tokenize(System.lineSeparator()).each {
            if (it.startsWith('DISTRIB_ID=')) {
                return it.replace('DISTRIB_ID=', '').trim()
            }
        }

        ''
    }

    private String debianVersionCheck() {
        def proc = 'cat /etc/debian_version'.execute()
        proc.waitForOrKill(commandTimeout)

        String output = proc.inputStream.text
        output.tokenize(System.lineSeparator()).each {
            if (it.startsWith('DISTRIB_ID=')) {
                return it.replace('DISTRIB_ID=', '').trim()
            }
        }

        ''
    }
}
