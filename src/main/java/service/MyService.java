package service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.camel.Body;
import org.osgi.service.component.annotations.Component;

import model.Car;
import model.Person;

@Component
public class MyService {

	@PersistenceContext(unitName="myPU")
	private EntityManager entityManager;
	
	public Car searchCarByModel(@Body ArrayList<String> body){
		Car out = null;
		String[] split = body.iterator().next().split(";");
		String year = split[0];
		String model = split[1];
		
		
		TypedQuery<Car> query = entityManager.createNamedQuery("Car.findByModel", Car.class);
		query.setParameter("model", model);
		List<Car> list = query.getResultList();

		if (list==null || list.isEmpty()) {
			out = new Car();
			out.setModel(model);
			out.setYear(Integer.parseInt(year));
		} else {
			out = list.iterator().next();
			entityManager.detach(out);
			
			out.setYear(Integer.parseInt(year));
		}
		
		out.setDthChange(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		return out;
	}
	
	public Person createPerson(){
		Person p = new Person();
		p.setName("Bob");
		p.setDthChange(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		return p;
	}
}
