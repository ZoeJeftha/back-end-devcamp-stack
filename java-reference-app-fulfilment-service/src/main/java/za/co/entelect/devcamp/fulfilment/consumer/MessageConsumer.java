package za.co.entelect.devcamp.fulfilment.consumer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.fulfilment.configuration.RabbitConfig;
import za.co.entelect.devcamp.fulfilment.enums.OrderStatusEnum;
import za.co.entelect.devcamp.fulfilment.interfaces.IProductService;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;
import za.co.entelect.devcamp.fulfilment.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.fulfilment.requests.SaveCustomerChecksRequest;
import za.co.entelect.devcamp.fulfilment.strategy.FulfilmentStrategy;

@Slf4j
@Service
public class MessageConsumer {

    public final IProductService productService;
    private final Map<String, FulfilmentStrategy> strategies;

    public MessageConsumer(IProductService productService,
                           List<FulfilmentStrategy> fulfilmentStrategyList)
    {
        this.productService = productService;
        this.strategies = fulfilmentStrategyList.stream()
                .collect(Collectors.toMap(
                        strategy -> getStrategyName(strategy),
                        Function.identity()
                ));
    }

    @RabbitListener(queues = RabbitConfig.QUEUE,   containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(FulfilmentRequest fulfilmentRequest) {
        int retryCount = RetrySynchronizationManager
                .getContext()
                .getRetryCount();
        try {

            log.info("-------------Processing FulfilmentRequest - Attempt: " + (retryCount + 1));

            List<SaveCustomerChecksRequest> saveCustomerChecksRequestList =
                    new java.util.ArrayList<>();

            FulfilmentStrategy strategy = strategies.get(fulfilmentRequest.getFulfilmentType());

            if (strategy == null) {
                throw new IllegalArgumentException(
                        "Unknown fulfilment type: "
                                + fulfilmentRequest.getFulfilmentType()
                );
            }


            boolean passed =
                    strategy.processRequest(
                            fulfilmentRequest,
                            saveCustomerChecksRequestList
                    );

            log.info("ALL CHECKS DONE, RESULT: "+ passed);
            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
            request.setOrderId(fulfilmentRequest.getOrderId());
            request.setSaveCustomerChecks(saveCustomerChecksRequestList);
            if(passed)
            {
                request.setStatus(OrderStatusEnum.ACCEPTED);
            }
            else
            {
                request.setStatus(OrderStatusEnum.REJECTED);
            }
            productService.UpdateOrder(request);
        }
        catch(Exception e)
        {
            log.info("-----------------Fulfilment processing failed --------------------------");
            throw new RuntimeException(e);
        }
    }

    private String getStrategyName(FulfilmentStrategy strategy) {

        if (strategy instanceof za.co.entelect.devcamp.fulfilment.strategy.FulfilmentTypeAStrategy) {
            return "A";
        }

        if (strategy instanceof za.co.entelect.devcamp.fulfilment.strategy.FulfilmentTypeBStrategy) {
            return "B";
        }

        if (strategy instanceof za.co.entelect.devcamp.fulfilment.strategy.FulfilmentTypeCStrategy) {
            return "C";
        }

        throw new IllegalArgumentException(
                "Unknown strategy: " + strategy.getClass().getSimpleName()
        );
    }
}
