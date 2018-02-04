# zeebe-camel

Camel routing for zeebe clients

## Use Cases

### Publish general events to route

A zeebe component registers to a topic and forwards all general events received to the route.

Syntax

`from(zeebe://<TOPIC>(?name=<NAME>))`

if name is not set, a random Uuid value is chosen.

Applications:

* forward the events to a message broker (amqp, kafka, ...) and consume them remotely.


## Decisions

* The zeebe client is passed to the component directly, configuring the client is not part of the endpoint configuration.
* The `component#createEndpoint()` will be a factory that can create specific endpoints for each use case.


## Archetype configuration

```
./mvnw archetype:generate -B -DarchetypeGroupId=org.apache.camel.archetypes -DarchetypeArtifactId=camel-archetype-api-component -DarchetypeVersion=2.20.2 -DgroupId=io.zeebe.camel -DartifactId=camel-zeebe-api -Dname=CamelZeebe  -Dscheme=zeebe-api
```

