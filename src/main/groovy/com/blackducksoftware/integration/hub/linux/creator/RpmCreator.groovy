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

import org.springframework.stereotype.Component

import com.blackducksoftware.integration.hub.linux.FileSuffixEnum

@Component
class RpmCreator extends Creator {
    @Override
    public void init() {
        initializeValues(FileSuffixEnum.RPM, 'rpm --version', 'rpm -qa')
    }
}
