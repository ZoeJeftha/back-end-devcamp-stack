package za.co.entelect.devcamp.fulfilment.strategy;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import za.co.entelect.devcamp.fulfilment.enums.CustomerChecksEnum;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycCheckService;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;
import za.co.entelect.devcamp.fulfilment.requests.SaveCustomerChecksRequest;

@Slf4j
@Component("A")
public class FulfilmentTypeAStrategy implements FulfilmentStrategy {

    private final IKycCheckService kycCheckService;

    public FulfilmentTypeAStrategy(IKycCheckService kycCheckService) {
        this.kycCheckService = kycCheckService;
    }

    @Override
    public boolean processRequest(FulfilmentRequest fulfilmentRequest, List<SaveCustomerChecksRequest> saveCustomerChecksRequestList) throws Exception{

        try {
            boolean kycCheck = kycCheckService.DoKycCheck(fulfilmentRequest.getId());
            log.info("Fulfilment kyc check: " + kycCheck);

            SaveCustomerChecksRequest saveCustomerChecksRequest = new SaveCustomerChecksRequest();
            saveCustomerChecksRequest.setCustomerCheck(CustomerChecksEnum.KYC_CHECK);
            saveCustomerChecksRequest.setOrderId(fulfilmentRequest.getOrderId());
            saveCustomerChecksRequest.setHasPassed(kycCheck);
            saveCustomerChecksRequestList.add(saveCustomerChecksRequest);

            return kycCheck;
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type A: " + e.getMessage());
            throw new Exception("ProcessFulfilmentTypeA failed: " + e.getMessage());
        }
    }
}
