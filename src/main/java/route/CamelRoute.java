package route;

import org.apache.camel.builder.RouteBuilder;

import service.MyService;

public class CamelRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		onException(Exception.class).to("direct:handleFail");
		
		from("direct:handleFail").log("ERROR => ${header.CamelExceptionCaught}").rollback();
		
		from("file:src/test/resources/?antInclude=**/*.txt&readLock=markerFile&delay=500&idempotent=true&noop=true")
		.setProperty("bkp").body()
		.transacted("PROPAGATION_REQUIRED")
		.log("Creating test person")
		.bean(MyService.class, "createPerson")
		.log("Bob created")
		.to("jpa://model.Person?persistenceUnit=myPU")
		.setBody().exchangeProperty("bkp")
		.log("Reading cars.txt")
		.unmarshal().csv().split().body()
			.log("Searching car [${body}]")
			.setProperty("atual").simple("${body}")
			.bean(MyService.class, "searchCarByModel") //lock when update.
			.log("Persist car [${body.model}]")
			.to("jpa://model.Car?persistenceUnit=myPU")
			.setBody().simple("${exchangeProperty.atual}")
			.bean(MyService.class, "searchCarByModel")
			.log("Car [${body.id}] merged with dtChange [${body.dthChange}]")
		.end();
	}

}
