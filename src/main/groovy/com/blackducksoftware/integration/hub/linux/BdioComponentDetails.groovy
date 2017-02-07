package com.blackducksoftware.integration.hub.linux

import com.blackducksoftware.bdio.model.Component
import com.blackducksoftware.bdio.model.ExternalIdentifier

class BdioComponentDetails {
    String name
    String version
    ExternalIdentifier externalIdentifier

    Component createBdioComponent() {
        def component = new Component()
        component.id = "uuid:${UUID.randomUUID()}"
        component.name = name
        component.version = version
        component.addExternalIdentifier(externalIdentifier)

        component
    }
}
