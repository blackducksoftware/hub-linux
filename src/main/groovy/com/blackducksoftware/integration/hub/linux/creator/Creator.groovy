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

abstract class Creator {
    private final Logger logger = LoggerFactory.getLogger(getClass())

    String testCommand
    String executionCommand

    Creator(String testCommand, String executionCommand) {
        this.testCommand = testCommand
        this.executionCommand = executionCommand
    }

    abstract String getFilenameSuffix()

    boolean isCommandAvailable(long timeout) {
        def available = false;
        try {
            def stdOut = new StringBuilder();
            def stdErr = new StringBuilder();
            def proc = testCommand.execute()
            proc.consumeProcessOutput(stdOut, stdErr)
            proc.waitForOrKill(timeout)

            if (stdOut.length() > 0) {
                available = true
            }

            if (stdErr.length() > 0) {
                available = false
            }
        } catch(Exception e) {
            logger.error("Error executing test command {}",testCommand,e)
        }

        true
    }

    void writeOutputFile(File file, long timeout) {
        try {
            def stdOut = new StringBuilder();
            def stdErr = new StringBuilder();
            def proc = executionCommand.execute()
            proc.consumeProcessOutput(stdOut, stdErr)
            proc.waitForOrKill(timeout)

            file.withWriter { out ->
                out.println stdOut.toString()
            }
        } catch(Exception e) {
            logger.error("Error executing command {}",executionCommand,e)
        }
    }
}
