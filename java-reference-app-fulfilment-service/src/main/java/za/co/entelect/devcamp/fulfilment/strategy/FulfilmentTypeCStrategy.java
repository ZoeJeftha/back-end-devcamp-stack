package za.co.entelect.devcamp.fulfilment.strategy;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import za.co.entelect.devcamp.fulfilment.enums.CustomerChecksEnum;
import za.co.entelect.devcamp.fulfilment.interfaces.ICreditCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaService;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;
import za.co.entelect.devcamp.fulfilment.requests.SaveCustomerChecksRequest;

@Slf4j
@Component("C")
public class FulfilmentTypeCStrategy implements FulfilmentStrategy {

    private final FulfilmentTypeBStrategy fulfilmentTypeBStrategy;
    private final ICreditCheckService creditCheckService;
    private final IDhaService dhaService;

    public FulfilmentTypeCStrategy(FulfilmentTypeBStrategy fulfilmentTypeBStrategy,
                                   ICreditCheckService creditCheckService,
                                   IDhaService dhaService)
    {
        this.fulfilmentTypeBStrategy = fulfilmentTypeBStrategy;
        this.creditCheckService = creditCheckService;
        this.dhaService = dhaService;
    }

    @Override
    public boolean processRequest(FulfilmentRequest fulfilmentRequest, List<SaveCustomerChecksRequest> saveCustomerChecksRequestList) throws Exception {

        try {
            boolean processBFlag = fulfilmentTypeBStrategy.processRequest(fulfilmentRequest,saveCustomerChecksRequestList);

            boolean maritalStatus = dhaService.DoMaritalCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));

            SaveCustomerChecksRequest saveCustomerChecksRequest = new SaveCustomerChecksRequest();
            saveCustomerChecksRequest.setCustomerCheck(CustomerChecksEnum.MARITAL_STATUS_CHECK);
            saveCustomerChecksRequest.setOrderId(fulfilmentRequest.getOrderId());
            saveCustomerChecksRequest.setHasPassed(maritalStatus);
            saveCustomerChecksRequestList.add(saveCustomerChecksRequest);

            log.info("Fulfilment marital statuses check: " + maritalStatus);

            boolean creditCheck = creditCheckService.DoCreditCheck(fulfilmentRequest.getId());

            SaveCustomerChecksRequest saveCustomerChecksRequest2 = new SaveCustomerChecksRequest();
            saveCustomerChecksRequest2.setCustomerCheck(CustomerChecksEnum.CREDIT_CHECK);
            saveCustomerChecksRequest2.setOrderId(fulfilmentRequest.getOrderId());
            saveCustomerChecksRequest2.setHasPassed(creditCheck);
            saveCustomerChecksRequestList.add(saveCustomerChecksRequest2);

            return processBFlag && maritalStatus && creditCheck;
        }
        catch (IOException e) {
            log.info("Fulfilment IOException type C: " + e.getMessage());
            throw new Exception("ProcessFulfilmentTypeC failed IOException: " + e.getMessage());
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type C: " + e.getMessage());
            throw new Exception("ProcessFulfilmentTypeC failed: " + e.getMessage());
        }
    }
}