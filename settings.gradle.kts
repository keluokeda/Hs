pluginManagement {
    repositories {
        maven { url=uri ("https://www.jitpack.io")}
//        maven {
//            url = uri("https://developer.huawei.com/repo/")
//        }
//        maven { url=uri ("https://maven.aliyun.com/repository/releases")}
//        maven { url=uri ("https://maven.aliyun.com/repository/google")}
//        maven { url=uri ("https://maven.aliyun.com/repository/central")}
//        maven { url=uri ("https://maven.aliyun.com/repository/gradle-plugin")}
//        maven { url=uri ("https://maven.aliyun.com/repository/public")}
//        maven { url=uri ("https://tencent-tds-maven.pkg.coding.net/repository/shiply/repo")}

        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        maven { url=uri ("https://tencent-tds-maven.pkg.coding.net/repository/shiply/repo")}
        maven { url=uri ("https://www.jitpack.io")}
//        maven { url=uri ("https://maven.aliyun.com/repository/releases")}
//        maven { url=uri ("https://maven.aliyun.com/repository/google")}
//        maven { url=uri ("https://maven.aliyun.com/repository/central")}
//        maven { url=uri ("https://maven.aliyun.com/repository/gradle-plugin")}
//        maven { url=uri ("https://maven.aliyun.com/repository/public")}
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven {
            url = uri("https://developer.huawei.com/repo/")
        }
    }
}

rootProject.name = "Hs"
include(":app")
include(":lib")
include(":module")
include(":demo")
