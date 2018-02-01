# zeebe-camel

Camel routing for zeebe clients

## Use Cases

### Publish general events to route (wip)

A zeebe component registers to a topic and forwards all general events received to the route.

Applications:

* forward the events to a message broker (amqp, kafka, ...) and consume them remotely.


## Decisions

* The zeebe client is passed to the component directly, configuring the client is not part of the endpoint configuration.
* The `component#createEndpoint()` will be a factory that can create specific endpoints for each use case.
