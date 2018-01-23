package io.zeebe.camel.fn;

import java.util.function.Predicate;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.clustering.impl.TopicLeader;
import lombok.Value;

@Value
public class TopicDoesNotExist implements Predicate<String>
{

    private final ZeebeClient client;

    @Override
    public boolean test(String name)
    {
        return !client.requestTopology().execute().getTopicLeaders().stream().map(TopicLeader::getTopicName).anyMatch(t -> name.equals(t));
    }
}
