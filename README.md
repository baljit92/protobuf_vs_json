# Protobuf / JSON protocol comparison

## Test Sample

Testing comparison using:
1. Springboot 1.4.3.RELEASE
2. Azure Service Bus Queue

## Size Performance
The data used to extract the results below were generated once during the startup process of the Spring Boot application.
The data generated submissions with "random data" and the following table exposes the data size that got sent through
the wire on each request to the submission queue.

|          | submission(s) total |   message size   |
|----------|--------------|------------------|
| protobuf |     1      |   0.316kb   |
| json     |     1      |   0.532kb   |
| protobuf |	100		|	31.05kb   |
| json 	   |	100		|	51.97kb   |

As we can observe, the Protobuf messages are **40%** smaller than the json formatted messages. With this size reduction,
we can hold that many more messages in in the queue.

## Average Data Performance

The following result is executing 100 submissions for (n) times/requests sequentially. Additionally, 5 runs were made in each case; and the timings were further averaged for a robust comparison and to remove the effect of caching due to Spring Boot.

Send 100 submissions for 100 times/requests:
|          |    data request | average time done |
|----------|-----------------|-------------------|
| protobuf |        100      |       225ms       |
| json     |        100      |       323ms       |

Send 100 submissions for 200 times/requests:
|          |    data request | average time done |
|----------|-----------------|-------------------|
| protobuf |        100      |       244ms       |
| json     |        100      |       326ms       |

As we can see, for 100 times. protobuf is faster at about 100ms. And at 200 times, protobuf protocol is faster at about 82ms.

With a service bus, performing an operation (send, receive, delete, etc.) takes some time. This time includes the processing of the operation by the Service Bus service in addition to the latency of the request and the response. 

P.S. All the print statements were removed when recording the time.

## Setting up

### Java class
1. **PerformanceTest.java** : 
  - DATA_PER_REQUEST (Amount of data (# of submissions) to be sent to queue in one request )
2. **QueueListener.java** :
  - PROTO_QUEUE (Queue for ProtoBuf on Azure Service Bus)
  - JSON_QUEUE (Queue for JSON on Azure Service Bus)
3. **ProtobufEndpoint.java** :
  - PROTO_QUEUE (Queue for ProtoBuf on Azure Service Bus)
  - JSON_QUEUE (Queue for JSON on Azure Service Bus)

### Properties (application.properties)
1. spring.jms.servicebus.connection-string=<YOUR-CONNECTION-STRING>
2. spring.jms.servicebus.idle-timeout=<TIMEOUT-SETTING> (recommend 1800000)



## Generating protobuf files

The command below generates protobuf files for Java. You will need to regenerate these files *only* if
you change the `./src/main/resources/submission.proto` file or if you add new messages descriptions (new `.proto` files).
Make sure that the Protobuf version on your machine and the version in the POM file dependency is the same.

```bash
protoc --java_out=./src/main/java/ \
       ./src/main/resources/submission.proto
```

