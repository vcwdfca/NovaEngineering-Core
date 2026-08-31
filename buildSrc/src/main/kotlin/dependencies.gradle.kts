plugins {
    java
}

val enablelwjglx: String = project.property("enable_lwjglx") as String
val enableLwjglx = enablelwjglx.toBoolean()

repositories {
    flatDir {
        dirs(rootProject.projectDir.resolve("lib"))
    }
    // Other repositories described by default:
    // CleanroomMC: https://maven.cleanroommc.com
    mavenCentral()
    maven {
        name = "CurseMaven"
        setUrl("https://cursemaven.com")
    }
    maven {
        name = "CleanroomCurseMaven"
        setUrl("https://curse.cleanroommc.com")
    }
    maven {
        name = "Modrinth"
        setUrl("https://api.modrinth.com/maven")
    }
    maven {
        name = "CleanroomMaven"
        url = uri("https://maven.cleanroommc.com")
    }
    maven {
        name = "BlameJared"
        url = uri("https://maven.blamejared.com")
    }
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    mavenLocal() // Must be last for caching to work
}
dependencies {
    if (enableLwjglx) {
        compileOnly("com.cleanroommc:lwjglx:1.0.0")
    }

    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.4.0")
    annotationProcessor("com.google.code.gson:gson:2.8.9")
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
    compileOnly("io.github.chaosunity.forgelin:Forgelin-Continuous:2.4.0.0:dev")
    add("modRuntimeOnly", "io.github.chaosunity.forgelin:Forgelin-Continuous:2.4.0.0:dev")

    compileOnly("CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-4.1.20.711")
    add("modRuntimeOnly", "CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-4.1.20.711") {
        exclude(group = "org.ow2.asm", module = "asm-debug-all")
        exclude(group = "com.google.code.gson", module = "gson")
    }
    add("modRuntimeOnly", "CraftTweaker2:CraftTweaker2-API:4.1.20.711")
    add("modRuntimeOnly", "CraftTweaker2:ZenScript:4.1.20.711")
    //modImplementation ":resourceloader:1.5.3:main"

    add("modImplementation", ":MMCE-ComponentModelHider:1.1:dev")
    add("modImplementation", ":ModularMachinery-CE:2.3.3:dev")
    add("modImplementation", ":AppliedEnergistics2-Supergiant:1.0.13:dev")
    add("modImplementation", ":appcompat:0.0.0+d41e164:dev")

    add("modImplementation", "curse.maven:fugue-1005815:8230893")
    add("modImplementation", "mezz:jei:4.33.0:dev")
    add("modImplementation", "curse.maven:circulation-flow-networks-1519385:8699343")
    add("modCompileOnly", "curse.maven:electroblobs-wizardry-265642:5354477")
    add("modImplementation", "curse.maven:lumenized-1234162:6378222")
    add("modImplementation", "curse.maven:mantle-74924:2713386")
    add("modImplementation", "curse.maven:tinkers-construct-74072:2902483")
    //modImplementation 'curse.maven:not-enough-energistics-515565:5234732'
    add("modImplementation", "curse.maven:psi-241665:3085917")
    add("modImplementation", "curse.maven:psio-339394:3077697")
    add("modImplementation", "curse.maven:RandomTweaker-514170:5528753")
    add("modImplementation", "curse.maven:jetif-303122:2919936")
    add("modImplementation", "curse.maven:ctm-267602:2915363")
    add("modImplementation", "curse.maven:the-one-probe-245211:2667280")
    add("modImplementation", "curse.maven:FTB-Library-237167:2985811")
    add("modImplementation", "curse.maven:FTBU-237102:3157548")
    add("modImplementation", "curse.maven:guide-api-228832:2645992")
    add("modImplementation", "curse.maven:patchouli-306770:3162874")
    add("modCompileOnly", "com.github.FormlessDragon:Applied-Mekanistics:v1.1.1:dev")
    add("modImplementation", "curse.maven:codechicken-lib-cre-1328922:8553938") // CodeChicken Lib CRE 3.3.8
    add("modImplementation", "curse.maven:wanionlib-253043:4623135")
    add("modImplementation", "curse.maven:avaritia-1-1x-unofficial-1165010:7939693")
    add("modImplementation", "curse.maven:eternal-singularity-253077:2922583")
    add("modCompileOnly", "curse.maven:optifine-check-626981:3806565")
    add("modImplementation", "curse.maven:industrialcraft-2-242638:3078604")
    add("modImplementation", "curse.maven:mekanism-ce-unofficial-840735:5946841")
    add("modImplementation", "curse.maven:RedstoneFlux-270789:2920436")
    add("modImplementation", "software.bernie.geckolib:geckolib-forge-1.12.2:3.0.31")
    add("modImplementation", "curse.maven:botania-225643:3330934")
    add("modImplementation", "curse.maven:astral-sorcery-241721:3044416")
    add("modImplementation", "curse.maven:baubles-227083:2518667")
    add("modImplementation", "curse.maven:zenutil-401178:6895021")
    add("modImplementation", "curse.maven:scalingguis-319656:2716334")
    add("modCompileOnly", "curse.maven:matter-overdrive-community-edition-557428:4592069")
    add("modImplementation", "curse.maven:cofh-core-69162:2920433")
    add("modImplementation", "curse.maven:cofh-world-271384:2920434")
    add("modImplementation", "curse.maven:thermal-foundation-222880:2926428")
    add("modCompileOnly", "curse.maven:thermal-innovation-291737:2920441")
    add("modCompileOnly", "curse.maven:tesla-244651:2487959")
    add("modImplementation", "curse.maven:mcjtylib-233105:2745846")
    add("modImplementation", "curse.maven:rftools-224641:2861573")
    add("modImplementation", "curse.maven:thermal-expansion-69163:2926431")
    add("modCompileOnly", "curse.maven:athenaeum-284350:4633750")
    add("modCompileOnly", "curse.maven:artisan-worktables-284351:3205284")
    add("modCompileOnly", "curse.maven:endercore-231868:4671384")
    add("modCompileOnly", "curse.maven:ender-io-64578:4674244")
    add("modCompileOnly", "curse.maven:more-electric-tools-366298:3491973")
    add("modImplementation", "curse.maven:extrabotany-299086:3112313")
    add("modImplementation", "curse.maven:libnine-322344:3509087")
    add("modCompileOnly", "curse.maven:better-chat-363860:3048407")
    add("modCompileOnly", "curse.maven:lunatriuscore-225605:2489549")
    add("modCompileOnly", "curse.maven:immersive-engineering-231951:2974106")
    add("modCompileOnly", "curse.maven:immersive-petroleum-268250:3382321")
    add("modCompileOnly", "curse.maven:ingame-info-xml-225604:2489566")
    add("modCompileOnly", "curse.maven:ftbq-289412:3156637")
    add("modImplementation", "curse.maven:AutoRegLib-250363:2746011")
    add("modImplementation", "curse.maven:Cucumber-272335:2645867")
    add("modImplementation", "curse.maven:extended-crafting-nomifactory-edition-398267:5778512")
    add("modCompileOnly", "curse.maven:techguns-244201:2958103")
    add("modImplementation", "curse.maven:legendary-tooltips-532127:5734973")
    add("modImplementation", "curse.maven:nuclearcraft-overhauled-336895:6605808")
    add("modImplementation", "curse.maven:loot-overhaul-299389:2711740")
    add("modImplementation", "curse.maven:BloodMagic-224791:2822288")
    add("modImplementation", "curse.maven:dme-737252:5985530")
    add("modCompileOnly", "curse.maven:libvulpes-236541:3801015")
    add("modCompileOnly", "curse.maven:advanced-rocketry-236542:4671856")
    add("modImplementation", "curse.maven:random-complement-1198138:8700149")
    add("modImplementation", "curse.maven:modular-routers-250294:2954953")
    add("modImplementation", "curse.maven:actually-additions-228404:3117927")
    add("modImplementation", "curse.maven:brandons-core-231382:3408276")
    add("modImplementation", "curse.maven:draconic-evolution-223565:3431261")
    add("modImplementation", "curse.maven:packagedauto-308380:6932932")
    add("modImplementation", "curse.maven:packagedastral-811828:7063119")
}
