# REST Integration

This project is an example of how to integrate external REST APIs with the Genesis Platform.

We will cover the following sections:

- Integrating external APIs with Genesis components
- Generating type safe code from an Open API Specification
    - Using the generated code in Genesis components
        - REST grids with request reply server
        - How to handle pagination
        - Batch data-pipelines calling REST APIs

For organisation this how-to has been split into 3 files, including this one.

[REST Integration with Open API and using HTTP clients directly](open-api-rest.md)

[Integrating External APIs with Pipelines](pipelines.md)

Both of these guides demonstrate different ways to integrate REST into your application.

Each have their own server and UI setup, but both rely on the below to set up the application ready to demo and test:

## Running The Application

We have provided a Dockerfile for running the external API alongside the example application.

To run this, first build the docker image:

```shell
docker build --build-arg JAR_FILE=libs/accounts-0.0.5.jar -t genesis/accounts-example .
```

Once built, you can run the image:

```shell
docker run -p 8080:8080 genesis/accounts-example
```

If you do not have access to Docker, run the Jar directly:

```shell
nohup java -jar libs/accounts-0.0.5.jar &
```

Once you've started your docker container (or standalone application), set up the Genesis Application via the plugin (see [here](https://docs.genesis.global/docs/develop/development-environments/) for how to run this application). Once running, you'll have tha ability to interact directly with the REST API's.

> ⚠️
For convenience, an [`accounts.http`](accounts.http) file has been provided. Once the application has been started, you can run the requests in the file to
insert data and test the API.
# Test results
BDD test results can be found [here](https://genesiscommunitysuccess.github.io/howto-rest/test-results)
