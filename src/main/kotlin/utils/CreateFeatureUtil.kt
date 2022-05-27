package utils

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.w3c.dom.Document
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths


class CreateFeatureUtil(private val project: Project) {
    private val packageName: String
    var featureName: String? = null

    init {
        packageName = getPackageName(project)
    }

    fun createFeature(featureName: String) {
        this.featureName = featureName
        val featureMapString = readFile("feature_map.txt")
        val paths = featureMapString.split("\n").toTypedArray()
        for (fullPath in paths) {
            val arrPath = fullPath.split("/").toTypedArray()
            if (arrPath.isNotEmpty()) {
                val fileName = arrPath[arrPath.size - 1]
                val content = readFile(fileName)
                writeToFile(generateContent(content), fullPath.replace(".txt", ".dart"))
            }
        }
    }

    fun refreshProject(e: AnActionEvent) {
        if (e.project != null) {
            e.project!!.baseDir.refresh(false, true)
        }
    }

    private fun readFile(path: String): String {
        var content = ""
        try {
            val path = "templates/$path"
            content = this.javaClass.classLoader?.getResource(path)!!.readText()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content
    }

    private fun writeToFile(value: String, path: String) {
        try {
            val file = File(project.basePath + "/" + generateContent(path))
            Files.createDirectories(Paths.get(file.parentFile.toString()))
            val fw = FileWriter(file.absoluteFile)
            val bw = BufferedWriter(fw)
            bw.write(value)
            bw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getPackageName(project: Project): String {
        var packageName = ""
        val docBuilderFactory = DocumentBuilderFactory.newInstance()
        try {
            val docBuilder = docBuilderFactory.newDocumentBuilder()
            val file = File(project.basePath + "/android/src/main/AndroidManifest.xml")
            val document: Document
            if(file.exists()) {
                document = docBuilder.parse(project.basePath + "/android/src/main/AndroidManifest.xml")
            } else {
                document = docBuilder.parse(project.basePath + "/android/app/src/main/AndroidManifest.xml")
            }
            val manifestElement = document.getElementsByTagName("manifest")
            for (index in 0 until manifestElement.length) {
                val element = manifestElement.item(index) as Element
                packageName = element.getAttribute("package")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val lastPartIdx = packageName.lastIndexOf('.')
        return packageName.substring(lastPartIdx + 1)
    }

    private fun generateContent(value: String): String {
        var content = value
        if (content.contains("\$\$\$package_name")) {
            content = content.replace("\$\$\$package_name", packageName)
        }

        if (content.contains("\$\$\$feature_name")) {
            content = content.replace("\$\$\$feature_name", featureName!!.toLowerCase())
        }

        var formattedFeatureName = StringBuilder(featureName!!.toLowerCase())
        if (content.contains("\$\$\$FeatureName")) {
            val parts = formattedFeatureName.toString().split("_").toTypedArray()
            formattedFeatureName = StringBuilder()
            for (part in parts) {
                if (part.length > 1) {
                    formattedFeatureName.append(part.substring(0, 1).toUpperCase()).append(part.substring(1))
                } else if (part.length == 1) {
                    formattedFeatureName.append(part.substring(0, 1).toUpperCase())
                }
            }
            content = content.replace("\$\$\$FeatureName", formattedFeatureName.toString())
        }
        if (formattedFeatureName.isNotEmpty()) {
            val featureName = formattedFeatureName.substring(0, 1).toLowerCase() + formattedFeatureName.substring(1)
            content = content.replace("\$\$\$featureName", featureName)
        }
        return content
    }
}
