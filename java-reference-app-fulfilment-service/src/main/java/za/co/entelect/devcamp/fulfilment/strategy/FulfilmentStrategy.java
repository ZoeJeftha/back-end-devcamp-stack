package za.co.entelect.devcamp.fulfilment.strategy;

import java.util.List;
import za.co.entelect.devcamp.fulfilment.requests.FulfilmentRequest;
import za.co.entelect.devcamp.fulfilment.requests.SaveCustomerChecksRequest;

public interface FulfilmentStrategy {

    boolean processRequest(FulfilmentRequest fulfilmentRequest, List<SaveCustomerChecksRequest> saveCustomerChecksRequestList) throws Exception;

}