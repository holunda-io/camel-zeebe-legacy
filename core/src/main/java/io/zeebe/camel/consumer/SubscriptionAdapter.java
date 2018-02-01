package io.zeebe.camel.consumer;

import io.zeebe.client.event.TopicSubscription;
import io.zeebe.client.task.TaskSubscription;


public interface SubscriptionAdapter
{
    static SubscriptionAdapter of(final TopicSubscription subscription) {
        return new SubscriptionAdapter()
        {
            @Override
            public boolean isClosed()
            {
                return subscription.isClosed();
            }

            @Override
            public void close()
            {
                subscription.close();
            }
        };
    }

    static SubscriptionAdapter of(final TaskSubscription subscription) {
        return new SubscriptionAdapter()
        {
            @Override
            public boolean isClosed()
            {
                return subscription.isClosed();
            }

            @Override
            public void close()
            {
                subscription.close();
            }
        };
    }

    /**
     * @return true if this subscription is not open and is not in the process of opening or closing
     */
    boolean isClosed();

    /**
     * Closes the subscription. Blocks until all pending events have been handled.
     */
    void close();


    default boolean isNotClosed() {
        return !isClosed();
    }
}
