plugins {
    kotlin("plugin.power-assert")
    // OPEN_API
    // Add the global.genesis.openapi plugin to your application's settings.gradle.kts file
    // This should be versioned the same as your GSF release and other plugins
    id("global.genesis.openapi")
    id("global.genesis.bdd_automation") version "3.5.76"
}

dependencies {
    // OPEN_API
    // Add genesis-openapi-codegen as a dependency
    implementation("global.genesis:genesis-openapi-codegen")

    // PIPELINES
    // Add "genesis-pal-datapipeline" as a dependency
    implementation(genesis("pal-datapipeline"))

    compileOnly(genesis("script-dependencies"))
    genesisGeneratedCode(withTestDependency = true)
    testImplementation(genesis("dbtest"))
    testImplementation(genesis("testsupport"))
    testImplementation(genesis("pal-eventhandler"))
    testImplementation("io.rest-assured:rest-assured:5.5.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.4")
}


description = "howto-rest-app"

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources", "src/main/genesis")
        }
    }
}

tasks {
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    // OPEN_API
    // Configure the code generation
    genesisOpenApi {
        // The package the code will be generated in, and your OpenAPI / Swagger specification location
        addSpec("global.genesis.api.accounts", project.layout.projectDirectory.file("src/main/resources/accounts.json"))
        // Optionally, the location of your pagination config, this configures which parameters of your API are for pagination
        paginationConfig = project.layout.projectDirectory.file("src/main/resources/pagination-config.yaml")
    }
}

// Genesis's Groovy 3 clashes with the BDD library's Groovy 4
configurations {
    testCompileClasspath { exclude(group = "org.codehaus.groovy") }
    testRuntimeClasspath { exclude(group = "org.codehaus.groovy") }
}
