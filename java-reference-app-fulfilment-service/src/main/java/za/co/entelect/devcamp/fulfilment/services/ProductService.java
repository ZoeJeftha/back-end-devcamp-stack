package za.co.entelect.devcamp.fulfilment.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import za.co.entelect.devcamp.fulfilment.dto.KycDto;
import za.co.entelect.devcamp.fulfilment.interfaces.IAuthService;
import za.co.entelect.devcamp.fulfilment.interfaces.IProductService;
import za.co.entelect.devcamp.fulfilment.interfaces.IProductServiceApiClient;
import za.co.entelect.devcamp.fulfilment.requests.OrderStatusUpdateRequest;
import za.co.entelect.devcamp.fulfilment.responses.OrderResponse;

@Slf4j
@Component
public class ProductService implements IProductService {

    public final IProductServiceApiClient productServiceApiClient;
    public final IAuthService authService;

    public ProductService(IProductServiceApiClient productServiceApiClient,
                      IAuthService authService)
    {
        this.productServiceApiClient = productServiceApiClient;
        this.authService = authService;
    }

    @Override
    public OrderResponse UpdateOrder(OrderStatusUpdateRequest request) throws Exception
    {
        try {
            String token = authService.GetSystemToken();
            OrderResponse response = productServiceApiClient.UpdateOrder(token, request);
            return response;
        }
        catch(Exception e)
        {
            System.out.println("------------------kyc check service" + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


//    private Boolean primaryIndicator;
//    private Boolean secondaryIndicator;
//    private String taxCompliance
}