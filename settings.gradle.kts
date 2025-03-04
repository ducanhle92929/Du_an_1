pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
//        mavenCentral()
        jcenter() // Chỉ nên sử dụng nếu cần thiết (đã deprecated)
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://jitpack.io")
        google()
//        mavenCentral()
        jcenter() // Có thể bỏ dòng này nếu không cần
    }
}

rootProject.name = "Duan"
include(":app")


 