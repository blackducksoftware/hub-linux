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
package com.blackducksoftware.integration.hub.linux.creator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.blackducksoftware.integration.hub.linux.FileSuffixEnum

abstract class Creator {
    private final Logger logger = LoggerFactory.getLogger(getClass())

    FileSuffixEnum fileSuffixEnum
    String testCommand
    String executionCommand

    abstract void init()

    void initializeValues(FileSuffixEnum fileSuffixEnum, String testCommand, String executionCommand) {
        this.fileSuffixEnum = fileSuffixEnum
        this.testCommand = testCommand
        this.executionCommand = executionCommand
    }

    String filename(String forge) {
        "${forge}${fileSuffixEnum.suffix}"
    }

    boolean isCommandAvailable(long timeout) {
        try {
            def proc = testCommand.execute()
            proc.waitForOrKill(timeout)

            return proc.exitValue() == 0
        } catch(Exception e) {
            logger.error("Error executing test command {}",testCommand,e)
        }

        false
    }

    void writeOutputFile(File file, long timeout) {
        try {
            def standardOut = new StringBuilder()
            def standardError = new StringBuilder()
            def process = executionCommand.execute()
            process.consumeProcessOutput(standardOut, standardError)
            process.waitForOrKill(timeout)

            file << standardOut
        } catch(Exception e) {
            logger.error("Error executing command {}",executionCommand,e)
        }
    }
}
