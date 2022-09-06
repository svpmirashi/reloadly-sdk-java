package software.reloadly.sdk.utilitypayment.dto.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import software.reloadly.sdk.core.internal.adapter.JackSonDateDeserializer;
import software.reloadly.sdk.utilitypayment.enums.UtilityBillPayTransactionStatus;


@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayBillResponse implements Serializable{

	private static final long serialVersionUID = 6318148407399964533L;
	
	private Long transactionId;
	
	private UtilityBillPayTransactionStatus status;
	/*
	 * The reference identification used to uniquley identify each utility payment request.
	 * */
	private String referenceId;
	
	/*
	 * A message code if available. A message code is related to the current processing conditions the transaction is going through.
	 * */
	private String code;
	
	/*
	 * A human friendly message associated with the code if available.
	 * */
	private String message;
	
    /**
     * submittedAt: The time instant at which the payment was submitted to Reloadly.
     */
    @JsonDeserialize(using = JackSonDateDeserializer.class)
    private Date submittedAt;
    
    /**
     * finalStatusAvailabilityAt: A SLA promised by Reloadly. 
     * A final status for the transaction will be provided at or before this time instant. Most utility payments are successful within seconds.
     */
    @JsonDeserialize(using = JackSonDateDeserializer.class)
    private Date finalStatusAvailabilityAt;
}
