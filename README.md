# Camel Zeebe

[![Build Status](https://travis-ci.org/holunda-io/camel-zeebe.svg?branch=master)](https://travis-ci.org/holunda-io/camel-zeebe)
[![codecov](https://codecov.io/gh/holunda-io/camel-zeebe/branch/master/graph/badge.svg)](https://codecov.io/gh/holunda-io/camel-zeebe) 

Apache Camel routing for Zeebe clients.

![Overview](https://github.com/holunda-io/camel-zeebe/blob/master/docs/camel-zeebe-flow.png?raw=true)

Zeebe is a microservice orchestration platform, that supports a centralized
broker and distributed client-workers that handle actual tasks in the overall workflow. It provides clients in different
languages which allows for to integrate with it. 

The downfall is that Zeebe's broker/client communication requires direct access via TCP/IP host/port.
We noticed that this can become a problem when you have distributed (micro-)workers deployed in cloud scenarios,
because you rely on direct access to the broker.

For example, if you want to implement the worker as an AWS/lambda, you won't have 
that direct access available, you will have to work with some proprietary 
messaging (SNS in this case). A lot of other use cases are thinkable in which a lightweight 
integration of task workers without relying on direct access to the Zeebe broker is required.

Instead of implementation a set of adapters to different cloud and platform providers, we focus on implementation of an adapter to a 
famous and a well-established integration library Apache Camel. By doing so we provide access to a vast amount of protocols by adopting
the Zeebe's orchestration semantics to Apache Camel endpoint scheme. Apache Camel is a well-established integration library implementing 
dozens of Enterprise Integration Patterns.

Here are some key features for using camel-zeebe and Apache Camel for your application:

* you run a broker ring on some host / isolated LAN segment

* you configure Zeebe client adapters that are located close to the broker and have direct access to it via TCP/IP. Those 
adapters register and hold subscriptions on various Zeebe topics and tasks and forward the events emitted to a 
Apache Camel route (which might end with propagation of the event to an external messaging system)
* you implement a simple Apache Camel route, which holds the configuration of your adapters
* you implement a task worker that has no dependency on Zeebe, it just consumes a TaskEvent and produces a TaskCommand 
(currently, only the CompleteTaskCommand is supported)
* the entire protocol and data format conversion between your messaging and Zeebe is done by Apache Camel runtime.
* the command is published to a messaging system and consumed again by a Zeebe client adapter close to the broker.
* the camel-zeebe component receives the command and closes the task.


## Use Cases

### Publish general events to route

A Zeebe component registers to a topic and forwards all general events received to the route.

Syntax:

`from(zeebe://<TOPIC>(?name=<NAME>)).to(<messaging>:<someChannel>)`

`<NAME>` is the subscription name, if it is not set, a random UUID value is chosen.

Applications:

* forward the events to a message broker (amqp, kafka, ...) and consume them remotely for logging/monitoring/...

### Work on tasks 

Registers a subscription for a combination of topic and taskType. When a task is created, it
converts the internal taskEvent to an API TaskEvent that has no dependencies on 
Zeebe. 

**Syntax (start work):**

`from(zeebe://<TOPIC>/task/<TASK_TYPE>?owner=<LOCK_OWNER>).to(<messaging>:<someChannel>)`

This will inform external workers for the given `TASK_TYPE` that are subscribed to the messaging channel.

**Syntax (complete work):**

* Adapter side: `from(<messaging>:<someChannel>).to(zeebe://<TOPIC>/task/<TASK_TYPE>?owner=<LOCK_OWNER>)`

This will forward CompleteTaskCommands to the Zeebe client subscription and finish them.

* Worker side - TODO, see TaskEndpointTest for a Processor based example.

Applications:

* Implement the orchestration of microservices located in (one/several) clouds which are accessible using some specific protocols only.

## Decisions

* The Zeebe client is passed to the component directly, configuring the client is not part of the endpoint configuration.
* The `component#createEndpoint()` will be a factory that can create specific endpoints for each use case.


## Project setup

`camel-zeebe` is implemented as a Apache Maven multi-module project. Run `./mvnw clean install` to build it.

### extension

These are the only modules that a user would depend on to use the features.

**api** 

Commands and events that are used for remote communication. These don't not rely on Zeebe API and can be used as regular POJOs.
Use these to implement a remote worker that subscribes to a message-channel, works on `TaskEvent`s and sends back `TaskCommand`s.

**core**

This provides the Apache Camel component and allows implementing subscriptions that forward Zeebe events to any message channel 
supported by Apache Camel (so in fact: every one) and send the results back to Zeebe.

It was setup via Camel archetypes, using this syntax:

```
./mvnw archetype:generate -B -DarchetypeGroupId=org.apache.camel.archetypes -DarchetypeArtifactId=camel-archetype-api-component -DarchetypeVersion=2.20.2 -DgroupId=io.zeebe.camel -DartifactId=camel-zeebe-api -Dname=CamelZeebe -Dscheme=zeebe-api
```

**spring**

Uses core to support camel-zeebe setup via Spring. 

### test

Provides common test libraries and helpers that make it easier to test camel-zeebe integration.

### examples

These will not get deployed to the repo, they are playgrounds to better understand
integration and can be used as a reference how to work with camel-zeebe in different scenarios.

TODO: currently, this is messy, most of it can be kicked out.   

