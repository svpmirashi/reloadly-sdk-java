package software.reloadly.sdk.utilitypayment.enums;

import java.io.Serializable;

import lombok.Getter;

@SuppressWarnings("unused")
public enum UtilityBillPayTransactionStatus implements Serializable{
	PROCESSING, 
	SUCCESSFUL,
	FAILED,
	REFUNDED;
	
	@Getter
	private String status;
}
