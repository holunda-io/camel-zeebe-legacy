package io.zeebe.camel.fn;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.clustering.impl.TopicLeader;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * @deprecated not needed here?
 */
@Value
@Builder
@Slf4j
@Deprecated
public class CreateTopic
{
    private ZeebeClient client;

    private String name;

    private int partitions = 1;


    public void create()
    {
        if (new TopicDoesNotExist(client).test(name))
        {
            client.topics().create(name, partitions).execute();
            log.info("create topic: {}", this);
        }
    }
}
