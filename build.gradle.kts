import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.compiler
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.taskTriggers

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.lombok") version "2.4.0"
    kotlin("kapt") version "2.4.0"
    id("com.gradleup.shadow") version "9.5.1"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.4.1"
    id("xyz.wagyourtail.unimined") version "1.4.36-kappa"
    id("net.kyori.blossom") version "2.2.0"
}

val modVersion: String = project.property("mod_version") as String
val rootPackage: String = project.property("root_package") as String
val modId: String = project.property("mod_id") as String
val modName: String = project.property("mod_name") as String

require(modVersion.isNotEmpty()) { "mod_version is empty!" }
require(rootPackage.isNotEmpty()) { "root_package is empty!" }
require(modId.isNotEmpty()) { "mod_id is empty!" }
require(modName.isNotEmpty()) { "mod_name is empty!" }

val generateSourcesJar = (project.property("generate_sources_jar") as String).toBoolean()
val generateJavadocsJar = (project.property("generate_javadocs_jar") as String).toBoolean()
val minecraftUsername: String = project.property("minecraft_username") as String
val extraJvmArgs: String = project.property("extra_jvm_args") as String
val enableShadow = (project.property("enable_shadow") as String).toBoolean()
val useAccessTransformer = (project.property("use_access_transformer") as String).toBoolean()
val isCoremod = (project.property("is_coremod") as String).toBoolean()
val coremodIncludesMod = (project.property("coremod_includes_mod") as String).toBoolean()
val coremodPluginClassName: String = project.property("coremod_plugin_class_name") as String
val useAssetMover = (project.property("use_asset_mover") as String).toBoolean()
val assetMoverVersion: String = project.property("asset_mover_version") as String
val enableJunitTesting = (project.property("enable_junit_testing") as String).toBoolean()
val showTestingOutput = (project.property("show_testing_output") as String).toBoolean()
val enableFoundationDebug = (project.property("enable_foundation_debug") as String).toBoolean()
val modDescription: String = project.property("mod_description") as String
val modAuthors: String = project.property("mod_authors") as String
val modCredits: String = project.property("mod_credits") as String
val modUrl: String = project.property("mod_url") as String
val modUpdateJson: String = project.property("mod_update_json") as String
val modLogoPath: String = project.property("mod_logo_path") as String
val modIssueTracker: String = project.property("mod_issue_tracker") as String

val accessTransformerLocations: String = "${modId}_at.cfg"

val gtceuMappingsJar = layout.projectDirectory.file(".gradle/unimined/local/mappings/srg2mcp.jar")
val gtceuMappingsSrg = layout.projectDirectory.file(".gradle/unimined/local/mappings/srg2mcp.tsrg")
val gtceuMappingsCsvDir = layout.buildDirectory.dir("gtceu/mcp-csv")
val prepareGtceuDevMappings = tasks.register<Copy>("prepareGtceuDevMappings") {
    description = "Prepare GTCEu development mappings"
    from({ zipTree(gtceuMappingsJar.asFile) }) {
        include("fields.csv", "methods.csv")
    }
    into(gtceuMappingsCsvDir)
}

tasks.matching { it.name == "runClient" || it.name == "runServer" }.configureEach {
    dependsOn(prepareGtceuDevMappings)
}

if (useAccessTransformer) {
    require(accessTransformerLocations.isNotEmpty()) { "access_transformer_locations is empty!" }
}
if (isCoremod) {
    require(coremodPluginClassName.isNotEmpty()) { "coremod_plugin_class_name is empty!" }
}
if (useAssetMover) {
    require(assetMoverVersion.isNotEmpty()) { "asset_mover_version is empty!" }
}

version = modVersion
group = rootPackage

base {
    archivesName.set(modId)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    if (generateSourcesJar) {
        withSourcesJar()
    }
    if (generateJavadocsJar) {
        withJavadocJar()
    }
}

kotlin {
    jvmToolchain(25)
}

configurations {
    val contain = create("contain")
    implementation { extendsFrom(contain) }
    val modCompileOnly = create("modCompileOnly")
    compileOnly { extendsFrom(modCompileOnly) }
    val modRuntimeOnly = create("modRuntimeOnly")
    runtimeOnly { extendsFrom(modRuntimeOnly) }
}

val remapTaskName = if (enableShadow) "remapShadowJar" else "remapJar"

unimined.minecraft {
    version("1.12.2")

    mappings {
        mcp("stable", "39-1.12")
    }

    cleanroom {
        if (useAccessTransformer) {
            accessTransformer("${rootProject.projectDir}/src/main/resources/$accessTransformerLocations")
        }
        loader("0.6.10-alpha")
        runs.all {
            systemProperty("crl.dev.mixin", "mixins.novaeng_core.json,mixins.novaeng_core.mod.json")
            systemProperty("net.minecraftforge.gradle.GradleStart.srg.notch-srg", gtceuMappingsSrg.asFile.absolutePath)
            systemProperty("net.minecraftforge.gradle.GradleStart.csvDir", gtceuMappingsCsvDir.get().asFile.absolutePath)
            args.addAll(listOf("--username", minecraftUsername))
            if (extraJvmArgs.isNotEmpty()) {
                jvmArgs(extraJvmArgs.split(("\\s+").toRegex()))
            }
            if (enableFoundationDebug) {
                systemProperties.apply {
                    set("foundation.dump", "true")
                    set("foundation.verbose", "true")
                }
            }
            if (isCoremod) {
                systemProperty("fml.coreMods.load", coremodPluginClassName)
            }
        }
    }

    defaultRemapJar = false

    val jarTaskName = if (enableShadow) "shadowJar" else "jar"

    remap(tasks.named(jarTaskName).get()) {
        mixinRemap {
            enableBaseMixin()
            enableMixinExtra()
            disableRefmap()
        }
    }

    mods {
        remap(configurations.getByName("modCompileOnly"))
        remap(configurations.getByName("modRuntimeOnly"))
    }
}

dependencies {
    if (useAssetMover) {
        implementation("com.cleanroommc:assetmover:$assetMoverVersion")
    }
    if (enableJunitTesting) {
        testImplementation("org.junit.jupiter:junit-jupiter:6.0.3")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}

pluginManager.apply("dependencies")

tasks.processResources {
    rename("(.+_at.cfg)", "META-INF/$1")
}

val generatedTagsDir = layout.buildDirectory.dir("generated/sources/tags/main/java")
val generateTags = tasks.register("generateTags") {
    description = "Generates the Tags class"
    outputs.dir(generatedTagsDir)
    inputs.property("packageName", rootPackage)
    inputs.property("modVersion", version.toString())
    doLast {
        val packageName = rootPackage
        val outputFile = generatedTagsDir.get().file("${packageName.replace('.', '/')}/Tags.java").asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            """package $packageName;

public final class Tags {
    public static final String VERSION = "$version";

    private Tags() {
    }
}
""",
        )
    }
}

sourceSets {
    val api = create("api") {
        java.srcDir("src/api/java")
        resources.setSrcDirs(emptyList<Any>())
    }
    main {
        java.srcDir(generateTags)
        compileClasspath += api.output
        runtimeClasspath += api.output
        blossom {
            kotlinSources {
                property("mod_id", modId)
                property("mod_name", modName)
                property("mod_version", modVersion)
                property("package", "$rootPackage.$modId")
            }
            resources {
                property("mod_id", modId)
                property("mod_name", modName)
                property("mod_version", modVersion)
                property("mod_description", modDescription)
                property(
                    "mod_authors",
                    modAuthors
                        .takeIf { it.isNotBlank() }
                        ?.split(",")
                        ?.filter { it.isNotBlank() }
                        ?.joinToString("\", \"") { it.trim() } ?: "",
                )
                property("mod_credits", modCredits)
                property("mod_url", modUrl)
                property("mod_update_json", modUpdateJson)
                property("mod_logo_path", modLogoPath)
                property("mod_issue_tracker", modIssueTracker)
            }
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn(generateTags)
    options.encoding = "UTF-8"
}
tasks.named("compileKotlin") {
    dependsOn(generateTags)
}

if (!enableShadow) {
    tasks.shadowJar { enabled = false }
}

idea {
    module {
        inheritOutputDirs = true
    }
    project {
        settings {
            runConfigurations {
                add(
                    Gradle("1. Build").apply {
                        setProperty("taskNames", listOf("build"))
                    },
                )
                add(
                    Gradle("2. Run Client").apply {
                        setProperty("taskNames", listOf("runClient"))
                    },
                )
                add(
                    Gradle("3. Run Server").apply {
                        setProperty("taskNames", listOf("runServer"))
                    },
                )
            }
            compiler.javac {
                afterEvaluate {
                    javacAdditionalOptions = "-encoding utf8"
                }
            }
            taskTriggers {
                beforeSync(tasks.named("genSources"))
            }
        }
    }
}

tasks.jar {
    archiveClassifier = "dev"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    val contain = configurations.getByName("contain")
    if (!contain.isEmpty) {
        into("/") {
            from(contain)
        }
    }
    doFirst {
        manifest {
            val attributeMap = mutableMapOf<String, Any>()
            attributeMap["ModType"] = "CRL"
            attributeMap["MixinConfigs"] = "mixins.novaeng_core.json,mixins.novaeng_core.mod.json"
            if (!contain.isEmpty) {
                attributeMap["ContainedDeps"] = contain.joinToString(" ") { it.name }
                attributeMap["NonModDeps"] = true
            }
            if (isCoremod) {
                attributeMap["FMLCorePlugin"] = coremodPluginClassName
                if (coremodIncludesMod) {
                    attributeMap["FMLCorePluginContainsFMLMod"] = true
                }
            }
            if (useAccessTransformer) {
                attributeMap["FMLAT"] = accessTransformerLocations
            }
            attributes(attributeMap)
        }
    }
    finalizedBy(tasks.named(remapTaskName).get())
}

tasks.shadowJar {
    configurations.add(project.configurations.shadow)
    archiveClassifier = "shadow"
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.test {
    useJUnitPlatform()
    javaLauncher =
        javaToolchains.launcherFor {
            languageVersion = JavaLanguageVersion.of(25)
        }

    if (showTestingOutput) {
        testLogging {
            showStandardStreams = true
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

kapt {
    keepJavacAnnotationProcessors = true
}

pluginManager.apply("publishing")
pluginManager.apply("extra")
