package za.co.entelect.devcamp.fulfilment.strategy;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import za.co.entelect.devcamp.fulfilment.enums.CustomerChecksEnum;
import za.co.entelect.devcamp.fulfilment.interfaces.IDhaService;
import za.co.entelect.devcamp.fulfilment.interfaces.IFraudCheckService;
import za.co.entelect.devcamp.fulfilment.interfaces.IKycCheckService;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;
import za.co.entelect.devcamp.fulfilment.requests.SaveCustomerChecksRequest;

@Slf4j
@Component("B")
public class FulfilmentTypeBStrategy implements FulfilmentStrategy {

    private final IKycCheckService kycCheckService;
    private final IFraudCheckService fraudCheckService;
    private final IDhaService dhaService;

    public FulfilmentTypeBStrategy(IKycCheckService kycCheckService,
                                   IFraudCheckService fraudCheckService,
                                   IDhaService dhaService){

        this.kycCheckService = kycCheckService;
        this.fraudCheckService = fraudCheckService;
        this.dhaService = dhaService;
    }

    @Override
    public boolean processRequest(FulfilmentRequest fulfilmentRequest, List<SaveCustomerChecksRequest> saveCustomerChecksRequestList) throws Exception {
        try
        {
            log.info("---------------Processing Fulfilment process B--------------------");

            boolean kycCheck = kycCheckService.DoKycCheck(fulfilmentRequest.getId());
            log.info("Fulfilment kyc check: " + kycCheck);

            SaveCustomerChecksRequest saveCustomerChecksRequest = new SaveCustomerChecksRequest();
            saveCustomerChecksRequest.setCustomerCheck(CustomerChecksEnum.KYC_CHECK);
            saveCustomerChecksRequest.setOrderId(fulfilmentRequest.getOrderId());
            saveCustomerChecksRequest.setHasPassed(kycCheck);
            saveCustomerChecksRequestList.add(saveCustomerChecksRequest);

            boolean fraudCheck = fraudCheckService.DoFraudCheck(fulfilmentRequest.getId(),fulfilmentRequest.getIdNumber());
            log.info("Fulfilment fraud check: " + fraudCheck);

            SaveCustomerChecksRequest saveCustomerChecksRequest2 = new SaveCustomerChecksRequest();
            saveCustomerChecksRequest2.setCustomerCheck(CustomerChecksEnum.FRAUD_CHECK);
            saveCustomerChecksRequest2.setOrderId(fulfilmentRequest.getOrderId());
            saveCustomerChecksRequest2.setHasPassed(fraudCheck);
            saveCustomerChecksRequestList.add(saveCustomerChecksRequest2);

            boolean livingStatus = dhaService.DoLivingStatusCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));
            log.info("Fulfilment living status check: " + livingStatus);

            SaveCustomerChecksRequest saveCustomerChecksRequest3 = new SaveCustomerChecksRequest();
            saveCustomerChecksRequest3.setCustomerCheck(CustomerChecksEnum.LIVING_STATUS_CHECK);
            saveCustomerChecksRequest3.setOrderId(fulfilmentRequest.getOrderId());
            saveCustomerChecksRequest3.setHasPassed(livingStatus);
            saveCustomerChecksRequestList.add(saveCustomerChecksRequest3);

            boolean duplicateIdStatus = dhaService.DoDuplicateIdCheck(Long.parseLong(fulfilmentRequest.getIdNumber()));
            log.info("Fulfilment duplicate id status check: " + duplicateIdStatus);

            SaveCustomerChecksRequest saveCustomerChecksRequest4 = new SaveCustomerChecksRequest();
            saveCustomerChecksRequest4.setCustomerCheck(CustomerChecksEnum.DUPLICATE_ID_STATUS_CHECK);
            saveCustomerChecksRequest4.setOrderId(fulfilmentRequest.getOrderId());
            saveCustomerChecksRequest4.setHasPassed(duplicateIdStatus);
            saveCustomerChecksRequestList.add(saveCustomerChecksRequest4);

            log.info("Process Fulfilment Type B: "+ (kycCheck && fraudCheck && livingStatus && duplicateIdStatus));

            return kycCheck && fraudCheck && livingStatus && duplicateIdStatus;
        }
        catch(Exception e)
        {
            log.info("Fulfilment Exception type B: " + e.getMessage());
            throw new Exception("ProcessFulfilmentTypeB failed: " + e.getMessage());
        }
    }
}