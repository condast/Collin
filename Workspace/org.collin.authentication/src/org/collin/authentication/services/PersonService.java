package org.collin.authentication.services;

import org.collin.authentication.ds.Dispatcher;
import org.collin.authentication.model.Name;
import org.collin.authentication.model.Person;
import org.condast.commons.na.model.IName;
import org.condast.commons.persistence.service.AbstractEntityService;

public class PersonService extends AbstractEntityService<Person>{

	private static Dispatcher dispatcher = Dispatcher.getInstance();
	
	public PersonService() {
		super( Person.class, dispatcher );
	}
	
	public Person create( String firstName, String prefix, String lastName ) {
		IName name=  new Name( firstName, prefix, lastName);
		Person person = new Person( name );
		super.create(person);
		return person;
	}
}
