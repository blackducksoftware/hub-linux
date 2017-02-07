package com.blackducksoftware.integration.hub.linux

import com.blackducksoftware.bdio.model.ExternalIdentifier
import com.blackducksoftware.integration.hub.api.component.Component

class BdioComponentDetails {
    String id
    String name
    String version
    ExternalIdentifier externalIdentifier

    Component createBdioComponent() {
        def component = new com.blackducksoftware.bdio.model.Component()
        component.id = id
        component.name = name
        component.version = version
        component.addExternalIdentifier(externalIdentifier)

        component
    }
}
