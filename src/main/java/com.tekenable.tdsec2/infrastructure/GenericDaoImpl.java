package com.tekenable.tdsec2.infrastructure;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by nbarrett on 22/06/2016.
 */
@Transactional
public class GenericDaoImpl <T> implements GenericDao<T> {

//    @PersistenceContext
//    protected EntityManager em;

    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> type;

    public GenericDaoImpl() {

        Type t = getClass().getGenericSuperclass();

        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return this.getSessionFactory().getCurrentSession();
    }

    @Override
    public long countAll(final Map<String, Object> params) {

        final StringBuffer queryString = new StringBuffer(
                "SELECT count(o) from ");

        queryString.append(type.getSimpleName()).append(" o ");


        /*
        On a sidenote, the GenericDaoImpl class uses a method getQueryClauses(params, null).
        This method is however not declared anywhere. It's not needed when you just want to retrieve a complete list of
        entities or when you want to count them. but it is needed when i want to apply certain filters.

         */
        //queryString.append(this.getQueryClauses(params, null));

        final Query query = this.getCurrentSession().createQuery(queryString.toString());

        return (Long) query.uniqueResult();

    }

    @Override
    public T create(final T t) {
        this.getCurrentSession().persist(t);
        return t;
    }

    @Override
    public void delete(final Object id) {
        this.getCurrentSession().delete(id);
    }

    @Override
    public T find(final Object id) {
        return (T) this.getCurrentSession().get(type, Long.valueOf(id.toString()));
    }

    @Override
    public T update(final T t) {

        //return this.getCurrentSession().merge(t);
        getCurrentSession().saveOrUpdate(t);

        return t;
    }

//    @Override
//    public T create(final T t) {
//        this.em.persist(t);
//        return t;
//    }
//
//    @Override
//    public void delete(final Object id) {
//        this.em.remove(this.em.getReference(type, id));
//    }
//
//    @Override
//    public T find(final Object id) {
//        return (T) this.em.find(type, id);
//    }
//
//    @Override
//    public T update(final T t) {
//        return this.em.merge(t);
//    }
}
