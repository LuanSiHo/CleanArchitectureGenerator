package action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import layout.FeatureInputLayout

class GenerateFeatureAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val dialog = FeatureInputLayout(e.project!!, e)
        dialog.show()
    }
}