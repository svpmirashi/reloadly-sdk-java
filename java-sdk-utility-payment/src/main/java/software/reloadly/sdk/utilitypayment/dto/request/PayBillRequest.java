package software.reloadly.sdk.utilitypayment.dto.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PayBillRequest  implements Serializable {

	private static final long serialVersionUID = -4180724800518802022L;

	/*
	 * subscriberAccountNumber indicates the account, reference, or card number of the subscriber or recipient of the bill payment.
	 * */
    private final String subscriberAccountNumber;
    
    /* Amount field Indicates the amount to be paid.
     * */
    private final Double amount;
    
    /*
     * The biller identification number you would have retrieved from the Get Billers endpoint.
     * */
    private final Long billerId;
    
    /*
     * Indicates if the amount being presented is in the local currency of the biller. If this parameter is not provided or is set to false, 
     * then the payment will be presented in the user's wallet currency.
     * */
    private final boolean useLocalAmount;
    
    /*
     * A unique string that you may choose to provide. If you provide one, it will be tied to the transaction. 
     * You can query back your transaction with this referenceId as well.
     * Ensure you change the sample custom identifier in the payload before making a request so as to avoid any errors.
     * */
    private final String referenceId;

    @Builder
	public PayBillRequest(String subscriberAccountNumber, Double amount, Long billerId, boolean useLocalAmount,
			String referenceId) {
		super();
		this.subscriberAccountNumber = subscriberAccountNumber;
		this.amount = amount;
		this.billerId = billerId;
		this.useLocalAmount = useLocalAmount;
		this.referenceId = referenceId;
	}
    
    
}
