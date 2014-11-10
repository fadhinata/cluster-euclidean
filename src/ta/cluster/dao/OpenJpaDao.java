/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ta.cluster.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import ta.cluster.tool.Constants;

/**
 *
 * @author Matt
 */
public abstract class OpenJpaDao<E> {

    protected Class<E> entityClass;
    protected EntityManager em;
    protected EntityManagerFactory factory;

    public OpenJpaDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
        factory = Persistence.createEntityManagerFactory(Constants.PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }

    public void beginTransaction() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
    }

    public void rollbackTransaction() {
        EntityTransaction tx = em.getTransaction();
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
    }

    public void setRollBackOnly() {
        EntityTransaction tx = em.getTransaction();
        if (tx != null && tx.isActive()) {
            tx.setRollbackOnly();
        }
    }

    public void commitTransaction() {
        EntityTransaction tx = em.getTransaction();
        if (tx != null && tx.isActive()) {
            try {
                tx.commit();
            } catch (RuntimeException e) {
            }
        }
    }

    public void insert(E entity) {
        em.persist(entity);
    }

    public void insertAll(List<E> list) {
        for (E entity : list) {
            insert(entity);
        }
    }

    public void update(E entity, Object id) {
        E e = findById(id);
        if (e == null) {
            throw new EntityNotFoundException();
        } else {
            em.merge(entity);
            em.flush();
            em.clear();
        }
    }

    public void delete(E entity) {
        em.remove(entity);
    }

    public E findById(Object id) {
        return em.find(entityClass, id);
    }

    public List<E> findAll() {
        List<E> allEntities = em.createQuery("SELECT e FROM " + entityClass.getName() + " e").getResultList();
        return allEntities;
    }
    
	public Query createNativeQuery(String sqlString) {
		return em.createNativeQuery(sqlString);
	}

	public Query createQuery(String query) {
		return em.createQuery(query);
	}

	public Query createNamedQuery(String queryName) {
		return em.createNamedQuery(queryName);
	}

	public void flush() {
		em.flush();
	}
    
}
