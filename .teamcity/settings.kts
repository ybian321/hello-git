import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.buildReportTab
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.2"

project {
    description = "Contains all other projects"

    params {
        // This makes it impossible to change the build settings through the UI
        param("teamcity.ui.settings.readOnly", "true")
    }

    features {
        buildReportTab {
            id = "PROJECT_EXT_1"
            title = "Code Coverage"
            startPage = "coverage.zip!index.html"
        }
    }

    cleanup {
        baseRule {
            preventDependencyCleanup = false
        }
    }

    vcsRoot(VCSExample)
    buildType(TeamCityPRExample)
    subProject(FirstChild)
}

object FirstChild : Project({
    name = "First Child"
    buildType(HelloWorld)
})

object HelloWorld: BuildType({
    name = "Hello Steve"
    steps {
        script {
            scriptContent = "echo 'Hello world!'"
        }
        script {
            scriptContent = "echo 'Hello Bian!'"
        }
        
    }
})

object TeamCityPRExample : BuildType({
    name = "TeamCityPRExample"

    vcs {
        root(VCSExample)
        cleanCheckout = true
    }

    steps {
        script {
            name = "Hello TeamCity"
            scriptContent = """
                echo "hello teamcity! I'm a build"
            """.trimIndent()
        }
    }

    triggers {
        vcs {
            branchFilter = """
               +:<default>
               +:pull/*
            """.trimIndent()
        }
    }

    features {
        commitStatusPublisher {
            vcsRootExtId = "${VCSExample.id}"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "zxx010cfab59641ee19092b8307a116427c6ae958a7948f59e971b95cf982c6f2c5d196fb90892f7a6f775d03cbe80d301b"
                }
            }
        }

        pullRequests {
            vcsRootExtId = "${VCSExample.id}"
            provider = github {
                authType = vcsRoot()
                filterAuthorRole = PullRequests.GitHubRoleFilter.EVERYBODY
            }
        }
    }
})

object VCSExample : GitVcsRoot({
    name = "VCSExample"
    url = "https://github.com/wellyfox/test_kotlin.git"
    branch = "refs/heads/main"
    authMethod = password {
        userName = "wellyfox"
        password = "zxx010cfab59641ee19092b8307a116427c6ae958a7948f59e971b95cf982c6f2c5d196fb90892f7a6f775d03cbe80d301b"
    }
})

