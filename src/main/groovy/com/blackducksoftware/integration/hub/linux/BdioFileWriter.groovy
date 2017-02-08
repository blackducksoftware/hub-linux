package com.blackducksoftware.integration.hub.linux

import org.springframework.stereotype.Component

import com.blackducksoftware.bdio.io.BdioWriter
import com.blackducksoftware.bdio.io.LinkedDataContext
import com.blackducksoftware.bdio.model.BillOfMaterials
import com.blackducksoftware.bdio.model.CreationInfo
import com.blackducksoftware.bdio.model.Project

@Component
class BdioFileWriter {
    BdioWriter createBdioWriter(final OutputStream outputStream, final String projectName, final String projectVersion) {
        def linkedDataContext = new LinkedDataContext()
        def bdioWriter = new BdioWriter(linkedDataContext, outputStream)

        def bom = new BillOfMaterials()
        bom.id = "uuid:${UUID.randomUUID()}"
        bom.name = "${projectName} Black Duck I/O Export"
        bom.specVersion = linkedDataContext.getSpecVersion()
        bom.creationInfo = CreationInfo.currentTool()
        bdioWriter.write(bom)

        def project = new Project()
        project.id = "uuid:${UUID.randomUUID()}"
        project.name = projectName
        project.version = projectVersion
        bdioWriter.write(project)

        bdioWriter
    }

    void writeComponent(BdioWriter bdioWriter, BdioComponentDetails bdioComponentDetails) {
        def component = bdioComponentDetails.createBdioComponent()
        bdioWriter.write(component)
    }
}