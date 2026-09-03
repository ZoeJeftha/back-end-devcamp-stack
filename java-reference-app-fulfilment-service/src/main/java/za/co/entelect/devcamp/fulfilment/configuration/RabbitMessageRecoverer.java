package za.co.entelect.devcamp.fulfilment.configuration;

import org.springframework.retry.interceptor.MethodInvocationRecoverer;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessageRecoverer implements MethodInvocationRecoverer<Object> {

    @Override
    public Object recover(Object[] args, Throwable cause) {
        throw new RuntimeException(
                "Message processing failed after all retry attempts",
                cause
        );
    }
}