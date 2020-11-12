package com.crm.utils;

public class ServiceFactory {
	
	public static Object getService(Object service){
		
		return new com.crm.utils.TransactionInvocationHandler(service).getProxy();
		
	}
	
}
