package br.com.alura.school.support.validation;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.Assert;

public class ExistsEntityValidator implements ConstraintValidator<ExistsEntity, Object> {

	private final String path = "br.com.alura.school.";
	private String classpath;
	private String field;

	private final EntityManager entityManager;

	ExistsEntityValidator(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void initialize(ExistsEntity unique) {
		classpath = unique.classpath();
		field = unique.field();
	}

	@Override
	public boolean isValid(Object valueToBeValidated, ConstraintValidatorContext context) {
        try {
        	Assert.notNull(entityManager, "The entity manager should not be null");
			Class<?> entity = toEntity();
	        
			String jpql = String.format("select count(1) > 0 from %s where %s = :value", entity.getSimpleName(), field);
	       
	        boolean existsInDB = entityManager
	                .createQuery(jpql, Boolean.class)
	                .setParameter("value", valueToBeValidated)
	                .getSingleResult();

	        return existsInDB;
	        
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	private Class<?> toEntity() throws ClassNotFoundException {
		return Class.forName(path + classpath);
	}

}
