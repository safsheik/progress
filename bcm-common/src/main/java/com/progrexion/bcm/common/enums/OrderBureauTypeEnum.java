package com.progrexion.bcm.common.enums;

public enum OrderBureauTypeEnum {
	
	TRANS_UNION("TRANS_UNION"),	
	EQUIFAX("EQUIFAX"),	
	EXPERIAN("EXPERIAN"),
	;

	private String bureauType;

	OrderBureauTypeEnum(String bureauType) {
		this.bureauType = bureauType;

	}

	public String getBureauType() {
		return bureauType;
	}
	
}
