# camel-zeebe

Camel routing for zeebe clients.

Zeebe is a microservice orchestration platform, that supports a centralized
broker and distributed client-workers that handle actual tasks in the overall workflow.

Downfall: Zeebes broker/client communication requires direct access via tcp host/port.
We noticed that this can become a problem when you have distributed (micro-)workers,
because you rely on direct access to the broker.

For example: when you want to implement the worker as an AWS/lambda, you wont have 
that direct access available, you will have to work with some proprietary 
messaging (SNS in this case). A lot of other use cases are thinkable where would like to have a lightweight 
integration of job workers without relying on direct access to the zeebe broker.

This is the motivation for the camel-zeebe integration:

* you run a broker ring on some host
* you implement zeebe-client adapters that are located close to the broker and have direct access via tcp.
* those adapters register and hold subscriptions on various zeebe topics and tasks and forward the events emitted to a camel route (which might end with proagation of the event to an external messaging system)
* you implement a job worker that has no dependency on zeebe, it just consumes a TaskEvent and produces a job command (currently, only the CompleteTaskCommand is supported)
* the command is published to a messaging system and consumed again by a zeebe-client adapter close to the broker.
* the camel-zeebe component receives the command and closes the job.

## Use Cases

### Publish general events to route

A zeebe component registers to a topic and forwards all general events received to the route.

Syntax

`from(zeebe://<TOPIC>(?name=<NAME>)).to(<messaging>:<someChannel>)`

"name" is the subscription name, if it is not set, a random Uuid value is chosen.

Applications:

* forward the events to a message broker (amqp, kafka, ...) and consume them remotely for logging/monitoring/...

### Work on tasks 

Registers a subscription for a combination of topic and taskType. When a job is created, it
converts the internal taskEvent to an API TaskEvent that has no dependencies on 
zeebe. 

**Syntax (start work):**

`from(zeebe://<TOPIC>/job/<TASK_TYPE>?owner=<LOCK_OWNER>).to(<messaging>:<someChannel>)`

This will inform external workers for the given taskType that are subscribed to the messaging channel.

**Syntax (complete work):**

* Adapter side: `from(<messaging>:<someChannel>).to(zeebe://<TOPIC>/job/<TASK_TYPE>?owner=<LOCK_OWNER>)`

This will forward CompleteTaskCommands to the zeebe client subscription and finish them.

* Worker side - TODO, see TaskEndpointTest for a Processor based example.



## Decisions

* The zeebe client is passed to the component directly, configuring the client is not part of the endpoint configuration.
* The `component#createEndpoint()` will be a factory that can create specific endpoints for each use case.



## Project setup

`camel-zeebe` is implemented as a maven multi-module project.

### extension

These are the only modules that a user would depend on to use the features.

**api** 

Commands and events that are used for remote communication. These will not rely on zeebe, they can be used as regular POJOs.
Use these to implement a remote worker that subscribes to a message-channel, works on TaskEvents and sends back TaskCommands.

**core**

This provides the neede camel component and allows implementing subsriptions that forward events to some message channel 
supported by camel (so in fact: every one).

It was setup via camel archetypes, using this syntacx:

```
./mvnw archetype:generate -B -DarchetypeGroupId=org.apache.camel.archetypes -DarchetypeArtifactId=camel-archetype-api-component -DarchetypeVersion=2.20.2 -DgroupId=io.zeebe.camel -DartifactId=camel-zeebe-api -Dname=CamelZeebe  -Dscheme=zeebe-api
```

**spring**

Uses core to support camel-zeebe setup via spring

### test

Provides common test libraries and helpers that make it easier to test camel-zeebe integration.

### examples

These will not get deployed to the repo, they are playgrounds to better understand
itegration and can be used as a reference how to work with camel-zeebe in different scenarios.

TODO: currently, this is messy, most of it can be kicked out.   


