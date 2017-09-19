package com.alpha.commons.enums;

public enum Gender {

	MALE(0),			//男性
	FEMALE(1);			//女性	
	
	private int value;
	
	private Gender(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}



	public void setValue(int value) {
		this.value = value;
	}



	public static Gender findByType(int value) {
		for(Gender item : values()) {
			if(item.value == value) {
				return item;
			}
		}
		throw new IllegalArgumentException("Cannot create enum from " + value);
	}
}
