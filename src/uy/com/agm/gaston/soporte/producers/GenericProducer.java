package uy.com.agm.gaston.soporte.producers;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenericProducer {
	@Produces
	@PersistenceContext(unitName = "gaston-pu")
	private EntityManager em;

	@Produces
	private Logger createLogger(InjectionPoint injectionPoint) {
		return LogManager.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}
}
