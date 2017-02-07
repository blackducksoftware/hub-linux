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

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

import org.junit.Test

class CreatorTest {

    @Test
    void testCommandAvailableFalse() {
        def creator = new Creator('a_bad_cmd_','' )
        assertFalse creator.isCommandAvailable()
    }

    @Test
    void testCommandAvailableTrue() {
        def creator = new Creator('ls -al','' )
        assertTrue creator.isCommandAvailable()
    }

    @Test
    void testCreateOutputInvalidCommand() {

        def creator = new Creator('','a_bad_cmd_ --version' )
        def directory = new File("./build/tmp")
        def fileName = "creator_test_file.txt"
        def file = creator.createOutputFile(directory, fileName)

        System.out.println("testCreateOutputInvalidCommand: file = "+ file.getCanonicalPath())
        def directoryPath = directory.getCanonicalPath()

        assertNotNull file
        assertEquals(directory.getCanonicalPath(), file.getParentFile().getCanonicalPath())
        assertEquals(fileName, file.getName())
        assertFalse file.exists()
        assertEquals(file.size(), 0)


        file.delete()
    }

    @Test
    void testCreateOutputCommand() {
        def creator = new Creator('ls -al','ls -al' )
        def directory = new File("./build/tmp")
        def fileName = "creator_test_file.txt"
        def file = creator.createOutputFile(directory, fileName)

        System.out.println("testCreateOutputCommand: file = "+ file.getCanonicalPath())
        def directoryPath = directory.getCanonicalPath()

        assertNotNull file
        assertTrue file.isFile()
        assertTrue(file.size() > 0)
        assertEquals(directory.getCanonicalPath(), file.getParentFile().getCanonicalPath())
        assertEquals(fileName, file.getName())

        file.delete()
    }
}
