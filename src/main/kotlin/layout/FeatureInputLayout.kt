package layout

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import utils.CreateFeatureUtil
import java.awt.Dimension
import javax.swing.*
import com.intellij.openapi.ui.Messages

class FeatureInputLayout(
    private val project: Project,
    private val event: AnActionEvent
) : DialogWrapper(project) {

    private val featureNameLabel: JLabel = JLabel().apply {
        name = "featureNameLabel"
    }

    private val featureNameTextField: JTextField = JTextField().apply {
        name = "featureNameTextField"
        minimumSize = Dimension(300, 30)
        preferredSize = Dimension(300, 30)
        requestFocus()
    }

    init {
        init()
        title = "Feature Generator"
    }

    override fun createCenterPanel(): JComponent = panel {
        row("Feature Name: ") {
            featureNameLabel()
            featureNameTextField()
        }
    }.apply {
        minimumSize = Dimension(450, 60)
        preferredSize = Dimension(450, 60)
    }

    override fun doOKAction() {
        super.doOKAction()
        val featureName = featureNameTextField.text
        if (featureName.isNotEmpty()) {
            val util = CreateFeatureUtil(project)
            util.createFeature(featureName)
            Messages.showInfoMessage(project, "Feature $featureName created successfully", "Feature Generator")
            util.refreshProject(event)
        }
    }

    override fun doCancelAction() {
        super.doCancelAction()
    }
}