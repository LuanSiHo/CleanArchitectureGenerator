package layout

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import java.awt.Dimension
import javax.swing.*

class FeatureInputLayout(
    val project: Project
) : DialogWrapper(project) {

    private val featureNameLabel: JLabel = JLabel().apply {
        name = "featureNameLabel"
    }
    private val featureNameTextField: JTextField = JTextField().apply {
        name = "featureNameTextField"
        minimumSize = Dimension(300, 30)
        preferredSize = Dimension(300, 30)
    }

    private fun oKAction() {}
    private fun cancelAction() {}

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
        oKAction()
        print("OK action")
    }

    override fun doCancelAction() {
        super.doCancelAction()
        cancelAction()
        print("cancel action")
    }
}