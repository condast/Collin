package org.collin.authentication.services;

import org.collin.authentication.ds.Dispatcher;
import org.collin.authentication.model.Address;
import org.condast.commons.persistence.service.AbstractEntityService;

public class AddressService extends AbstractEntityService<Address>{

	private static Dispatcher dispatcher = Dispatcher.getInstance();
	
	public AddressService() {
		super( Address.class, dispatcher );
	}

	public Address create( String name, String password, String email ) {
		Address address = new Address();
		super.create(address);
		return address;
	}
}
