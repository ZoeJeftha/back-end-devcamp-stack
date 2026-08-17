using System.ServiceModel;

namespace SoapApi;

[ServiceContract(Namespace = "entelect:devcamp:creditcheckservice")]
public interface ICreditCheckService
{
    [OperationContract]
    string CreditCheck(int customerId);
}
