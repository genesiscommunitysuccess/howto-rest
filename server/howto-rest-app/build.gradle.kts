plugins {
    // OPEN_API
    // Add the global.genesis.openapi plugin to your application's settings.gradle.kts file
    // This should be versioned the same as your GSF release and other plugins
    id("global.genesis.openapi") version "8.4.0"
    id("global.genesis.allure") version "1.0.0"
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
    implementation("com.fasterxml.jackson.core:jackson-databind:latest.release")
    modules {
        module("org.codehaus.groovy:groovy") {
            replacedBy("org.apache.groovy:groovy", "conflicts in current rest-assured version")
        }
        module("org.codehaus.groovy:groovy-xml") {
            replacedBy("org.apache.groovy:groovy-xml", "conflicts in current rest-assured version")
        }
        module("org.codehaus.groovy:groovy-json") {
            replacedBy("org.apache.groovy:groovy-json", "conflicts in current rest-assured version")
        }
    }
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
        // The package the code will be generated in
        packageName = "global.genesis.api.accounts"
        // Your OpenAPI / Swagger specification location
        specification = project.layout.projectDirectory.file("src/main/resources/accounts.json")
        // Optionally, the location of your pagination config, this configures which parameters of your API are for pagination
        paginationConfig = project.layout.projectDirectory.file("src/main/resources/pagination-config.yaml")
    }
}