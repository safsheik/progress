package com.progrexion.bcm.model.utils;

import java.util.Comparator;

import com.progrexion.bcm.model.common.entities.AbstractEntity;



public class CreatedDTComparatorDesc implements Comparator<AbstractEntity> {

	@Override
	public int compare(AbstractEntity o1, AbstractEntity o2) {
		if(o1 != null) {
			if(o2 != null) { 
				if(o2.getCreatedDate()!= null) {
					if(o1.getCreatedDate()!= null) {
						return o2.getCreatedDate().compareTo(o1.getCreatedDate());
					} else {
						return -1;
					}
				} else {
					if(o1.getCreatedDate() != null) {
						return 1;
					} else {
						return 0;
					}
				}
			} else {
				return -1;
			}
		} else {
			if(o2 != null) { 
				return 1;
			} else {
				return 0;
			}
		}
	}
}