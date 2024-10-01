# Open API REST Integration with Code Generation

### Introduction

We will take an Open API / Swagger specification, generate type-safe code from it,
and integrate this with Genesis components such as the request server

The steps for this are listed below, all relevant code is commented with `// OPEN_API`
so you can quickly navigate to relevant areas.

*Filenames within the README link to the actual files in the project, click them to navigate.*

### Adding the necessary dependencies

In the [`build.gradle.kts`](server/howto-rest-app/build.gradle.kts) file, add the `global.genesis.openapi` plugin.

```kotlin
plugins {
    // Add the global.genesis.openapi plugin to your application's settings.gradle.kts file
    // This should be versioned the same as your GSF release and other plugins
    id("global.genesis.openapi") version "8.4.0-SNAPSHOT"
}
```

Then add a dependency on the `genesis-openapi-codegen` module:

```kotlin
dependencies {
    implementation("global.genesis:genesis-openapi-codegen")
    ...
}
```

### Configuring the Open API plugin

Once the plugin has been added, you can configure it in your [`build.gradle.kts`](server/howto-rest-app/build.gradle.kts) file.

```kotlin
tasks {
    genesisOpenApi {
        // The package name for the generated code
        packageName = "global.genesis.api.accounts"
        // The location of your OpenAPI spec
        specification = project.layout.projectDirectory.file("src/main/resources/accounts.json")
        // Optionally, the location of your pagination config, this configures which parameters of your API are for pagination
        paginationConfig = project.layout.projectDirectory.file("src/main/resources/pagination-config.yaml")
    }
}
```
## Pagination

If your API has pagination enabled, you can provide config to map these parameters to Genesis' paginated parameters.
This allows the platform to handle the pagination and provides certain benefits:
- Standardises the requests to the router by having generic paginated parameters that map to your API
- In cases where the UI requests max-rows and the server filters out the response, the platform will request the next page from the API and return the correct number of rows
- The server will indicate the next logical page to request from the API, taking into account pages that have been partially read

To configure pagination, add a YAML file with the following:

```yaml
paginated_requests:
  - operation_id: findAllUsingGET # Which request is paginated (from OpenAPI spec)
    index_param: pageIndex # The parameter which tracks page index / number
    limit_param: limit # The parameter which tracks the number of rows per page
```
Example from [`pagination-config.yaml`](server/howto-rest-app/src/main/resources/pagination-config.yaml)

### Using the code in a request reply server

With our generated code we can now integrate the API with other Genesis components.


In order to display the data on a grid, we can use a request reply definition:

See [`howto-rest-reqrep.kts`](server/howto-rest-app/src/main/genesis/scripts/howto-rest-reqrep.kts)

### Scheduling a call to the API

You can also use the Genesis Evaluator to schedule eventhandler calls, which in turn can call your API:

```kotlin
    // OPEN_API: You can also use the Genesis Evaluator to schedule eventhandler calls to your API
    // See https://docs.genesis.global/docs/develop/server-capabilities/real-time-triggers-evaluator/ for details.
    eventHandler<Unit>("CRON_REST") {
        onCommit {
            val accounts = accountsApi.findAllUsingGET(
                FindAllUsingGETRequest(
                    _limit = 100,
                    _pageIndex = 0
                )
            ).accounts

            LOG.info("Retrieved accounts {}", accounts.joinToString(","))
            ack()
        }
    }
```

# Using Http Clients Directly

If you do not have a specification to generate code from, you can instead define your own data
classes and use them with the Genesis HTTP Client, or work with the raw response, it's up to you.

The Genesis HTTP Client is a powerful and flexible tool designed to simplify integration with external REST services
in the Genesis platform. It provides an intuitive DSL for making HTTP requests, allowing developers to easily integrate Request Servers and Event Handlers with external applications.

If you have not already added the `genesis-openapi-codegen` dependency you will have to add a dependency
on the client directly in your [`build.gradle.kts`](server/howto-rest-app/build.gradle.kts)

```kotlin
dependencies {
    implementation("global.genesis:genesis-http-client")
}
```

If you already have a client you are familiar with then you can use that too.
For example, you could use a library like [Ktor](https://ktor.io/docs/welcome.html)

Alongside the examples using generated code you will find equivalents using the Genesis HTTP Client.

Relevant examples are commented with `//REST`

- See [`howto-rest-reqrep.kts`](server/howto-rest-app/src/main/genesis/scripts/howto-rest-reqrep.kts) for request reply definitions

# Integrating with UI Grids

To display the data from the external API on a grid we will need to add a `grid-pro-server-side-datasource` component
to the UI.

You can navigate to the relevant code using the below links, or by searching for `// GRID:` comments.

### Adding dependencies

The first step is adding the necessary dependencies in your [package.json](client/package.json):

```json
{
  "dependencies": {
    "@ag-grid-community/core": "29.2.0",
    "@ag-grid-enterprise/server-side-row-model": "29.2.0",
    "@genesislcap/grid-pro": "14.202.0"
  } 
}
```
### Registering components

Next you will need to register the relevant modules in your [components.ts](client/src/components/components.ts):

- AG Grid `ServerSideRowModelModule`
- Genesis `rapidGridComponents`
- Genesis `rapidDesignSystem.baseComponents`

```typescript
// ...
// GRID:
// Import the relevant modules
import { ModuleRegistry } from '@ag-grid-community/core';
import * as rapidDesignSystem from '@genesislcap/rapid-design-system';
import { rapidGridComponents } from '@genesislcap/rapid-grid-pro';
import { ServerSideRowModelModule } from '@ag-grid-enterprise/server-side-row-model';

// GRID:
// Register the AG Grid module in the AG grid module registry
ModuleRegistry.registerModules([ServerSideRowModelModule]);

// ...

export async function registerComponents() {

    // ...
    // GRID:
    rapidDesignSystem
        .provideDesignSystem()
        .register(
            rapidDesignSystem.baseComponents, // Register the rapidDesignSystem base components
            rapidGridComponents, // Register the rapid grid components
            g2plotChartsComponents,
            foundationLayoutComponents,
            [ServerSideRowModelModule], // Also register the AG grid module in the design system
        );
    // ...
}
// ...
```
### Defining the grid

The next step is to define your grid, for this how to guide we'll use the home page, but you can place this in any of your
pages within the `client/src/routes/` folder.

In your [home.template.ts](client/src/routes/home/home.template.ts):

```typescript jsx
import {html} from '@genesislcap/web-core';
import type {Home} from './home';
// GRID:
// Write the definition within the html block of the template
export const HomeTemplate = html<Home>`
    [Grid definition here]
`;
```
Then define the grids (you will find the grids in the file wrapped in layout elements, omitted here)

Infinite scrolling:
```jsx
<!-- Wrap the grid-pro-server-side-datasource in a rapid-grid-pro element -->
<!-- Make sure the persist-column-state-key is set to the ssrm (server side row model) column state -->
<rapid-grid-pro
    persist-column-state-key="grid-pro-ssrm-column-state"
    enable-row-flashing
    enable-cell-flashing
>
    <!-- Add the grid pro server side component -->
    <!-- resource-name - This name should match your request reply name -->
    <!-- max-rows - Set your desired max rows for the grid -->
    <!-- pagination - Enables pagination, each page with display max-rows number of rows -->
    <!-- zero-based-view-number - If your API's page number starts from zero, add this attribute -->
    <!-- This grid has infinite scrolling -->
    <grid-pro-server-side-datasource
        resource-name="ACCOUNTS_API"
        row-id="ACCOUNT_NUMBER"
        max-rows="30"
    ></grid-pro-server-side-datasource>
```

Pagination:
```jsx
          <rapid-grid-pro
            persist-column-state-key="grid-pro-ssrm-column-state"
            enable-row-flashing
            enable-cell-flashing
          >
            <!-- This grid uses pagination -->
            <grid-pro-server-side-datasource
              resource-name="ACCOUNTS_API"
              row-id="ACCOUNT_NUMBER"
              max-rows="25"
              pagination
            ></grid-pro-server-side-datasource>
          </rapid-grid-pro>
```

That's it, go back to the [README](README.md) to see how to run your application.