package ir.bki.dao;


import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @param <T>
 * @author me-sharifi
 */

public abstract class AbstractDaoFacade<T> implements Serializable {


    private final static int pageSize = 10;
    private final String ERROR_MSG_CANT_FIND_ENTITY = "##Cant find Entity!";

    // ======================================
    // =             Attributes             =
    // ======================================

    //    private Logger LOGGER=Logger.getLogger(AbstractDaoFacade.class);
    private Class<T> entityClass;

    //    public AbstractDaoFacade() {// IMPORTANT DONT USED THIS LINE
//    }
    public AbstractDaoFacade(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    //-----------------------------------
    public static Object convertToAnotherObject(Object src, Object dsc) throws Exception {
        BeanInfo infoSrc = Introspector.getBeanInfo(src.getClass());
        BeanInfo infoDsc = Introspector.getBeanInfo(dsc.getClass());
        Object temp;
        for (PropertyDescriptor pdDsc : infoDsc.getPropertyDescriptors()) {
            Method writerDsc = pdDsc.getWriteMethod();
            String name = pdDsc.getName().toLowerCase();
            for (PropertyDescriptor pdSrc : infoSrc.getPropertyDescriptors()) {
                Method readerSrc = pdSrc.getReadMethod();
                if (readerSrc != null && writerDsc != null) {
                    if (name.equalsIgnoreCase(pdSrc.getName())) {
                        if (pdDsc.getPropertyType().getName().equals(pdSrc.getPropertyType().getName())) {
                            Object[] paramesReader = new Object[]{};
                            temp = readerSrc.invoke(src, paramesReader);
                            if (temp != null) {
                                Object[] paramesWriter = new Object[]{temp};
                                writerDsc.invoke(dsc, paramesWriter);
                            }
                        }
                    }
                }
            }
        }
        return dsc;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    // ======================================
    // =         Constructor Methods         =
    // ======================================


    // ======================================
    // =           Abstract Methods           =
    // ======================================

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // ======================================
    // =           Public Methods           =
    // ======================================

//    public SessionFactory getSessionFactory(){
//        Session session = getEntityManager().unwrap(Session.class);
//        sessionFactory = session.getS
//    }

    // ======================================
    // =          Business methods          =
    // ======================================

    abstract protected EntityManager getEntityManager();

    public void batchUpdate() {
        // http://stackoverflow.com/questions/4148231/how-can-i-get-the-session-object-if-i-have-the-entitymanager
        // Retrieve DB connection
        // http://tonaconsulting.com/batch-update-with-jpa/
        // connect = sfi.getConnectionProvider().getConnection();
        // PreparedStatement ps = connect.prepareStatement("INSERT INTO temp_table values(?,?)");
        //
        // for (Data p : insertCommands) {//  ps.setInt(1, p.getId());
        //    if (p.isWorking() != null) {
        //     ps.setInt(2, p.isWorking() ? 1 : 0);
        // } else {
//                ps.setNull(2, Types.INTEGER);
//            }
//            ps.addBatch();
//  }
//        ps.executeBatch();
    }

    //    public void create(T entity) throws Exception {
//        getEntityManager().persist(entity);
//    }
    public T create(T t) {
        getEntityManager().persist(t);
        getEntityManager().flush();
        getEntityManager().refresh(t);
        return t;
    }

    public T edit(T entity) {
        entity = getEntityManager().merge(entity);
        return entity;
    }

    public T edit(T entity, Object id) throws Exception {
        T entityReadFromDb = findById(id);
        convertToAnotherObject(entity, entityReadFromDb);
        entity = edit(entity);
        return entity;
    }

    public void remove(T entity) throws Exception {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * find record then remove it
     *
     * @param id
     */
    public void deleteById(Object id) {
//    public void deleteById(Object id) throws EntityNotFoundException,NoResultException {
        T entity = findById(id);
        if (entity == null) {
            throw new EntityNotFoundException(ERROR_MSG_CANT_FIND_ENTITY + " Id: " + id);
        }
        try {
            remove(entity);
        } catch (Exception ex) {
            throw new NoResultException("## cant remove entity! id ##" + id);
        }
    }

    public T findById(Object id) {

        T entity = getEntityManager().find(entityClass, id);

        return entity;

    }


    public List<T> findAll() {
        if (getEntityManager() == null)
            System.out.println("** EM ISNULL");
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public T findOne() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return (T) getEntityManager().createQuery(cq).getSingleResult();
    }

    /**
     * @param offset
     * @param limit
     * @return
     */
    public List<T> findRange(Integer offset, Integer limit) {

        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        Query q = getEntityManager().createQuery(cq);
        if (offset != null)
            q.setFirstResult(offset);
        if (limit != null)

            q.setMaxResults(limit);
        return q.getResultList();
    }

    public long count() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<T> findWithNamedQuery(String namedQueryName) {
        return getEntityManager().createNamedQuery(namedQueryName).getResultList();
    }

    /**
     * Cachable
     *
     * @param namedQueryName
     * @param parameters
     * @param cacheable
     * @return
     */
    public List findWithNamedQuery(String namedQueryName, Map parameters, boolean cacheable) {
        return findWithNamedQuery(namedQueryName, parameters, 0, cacheable);
    }

    public List findWithNamedQuery(String namedQueryName, Map parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0, false);
    }

    public List findWithNamedQuery(String queryName, int resultLimit) {
        return getEntityManager().createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    public List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit) {
        return findWithNamedQuery(namedQueryName, parameters, resultLimit, true);
    }

    //http://www.adam-bien.com/roller/abien/entry/generic_crud_service_aka_dao
    public List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit, boolean cacheable) {

        Query query = getEntityManager().createNamedQuery(namedQueryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }

        if (parameters != null) {
            Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
            for (Map.Entry<String, Object> entry : rawParameters) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        if (!cacheable)
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return query.getResultList();
    }

    public List<T> findWithQuery(String queryName) {
        return getEntityManager().createQuery(queryName).getResultList();
    }

    public List<T> findByNativeQuery(String sql) {
        return getEntityManager().createNativeQuery(sql, entityClass).getResultList();
    }

    public T findSingleWithNamedQuery(String namedQueryName) {
        T result = null;
        try {
            result = (T) getEntityManager().createNamedQuery(namedQueryName).getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("Cant re find " + entityClass.getSimpleName());
        }
        return result;
    }

    public T findSingleWithNamedQuery(String namedQueryName, Map<String, Object> parameters) throws NoResultException {
        return findSingleWithNamedQuery(namedQueryName, parameters, false);
    }

    /**
     * Ability Cachable
     *
     * @param namedQueryName
     * @param parameters
     * @param cacheable
     * @return
     * @throws NoResultException
     */
    public T findSingleWithNamedQuery(String namedQueryName, Map<String, Object> parameters, boolean cacheable) throws NoResultException {

        Query query = getEntityManager().createNamedQuery(namedQueryName);

        if (parameters != null) {
            Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
            for (Map.Entry<String, Object> entry : rawParameters) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        T result = null;
        try {
            if (!cacheable)
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            result = (T) query.getSingleResult();

        } catch (NoResultException e) {
            throw new NoResultException(ERROR_MSG_CANT_FIND_ENTITY);
        }
        return result;
    }

    public long countBetweenDate(String namedQueryName, Date startDate, Date endDate) throws Exception {
        if (!endDate.after(startDate)) {
            throw new Exception("Start Date must be before end date! start Date is:" + startDate + " ,end Date: " + endDate);
        }
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("startDate", startDate);
        parameter.put("endDate", endDate);
        return countBetweenDateWithNamedQuery(namedQueryName, parameter);
    }

    public long countBetweenDateAndHost(String namedQueryName, Date startDate, Date endDate, long hostId) throws Exception {
        if (!endDate.after(startDate)) {
            throw new Exception("Start Date must be before end date! start Date is:" + startDate + " ,end Date: " + endDate);
        }
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("startDate", startDate);
        parameter.put("endDate", endDate);
        parameter.put("host", hostId);
        return countBetweenDateWithNamedQuery(namedQueryName, parameter);
    }


    public long countBetweenDateWithNamedQuery(String namedQueryName, Map<String, Object> parameters) throws Exception {
        return countBetweenDateWithNamedQuery(namedQueryName, parameters, false);
    }

    public long countBetweenDateWithNamedQuery(String namedQueryName, Map<String, Object> parameters, boolean cacheable) {
        Query query = getEntityManager().createNamedQuery(namedQueryName);

        if (parameters != null) {
            Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
            for (Map.Entry<String, Object> entry : rawParameters) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        long result;
        try {
            if (!cacheable)
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            result = (long) query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoResultException("Cant count Entity!");
        }
        return result;
    }

    //---------------------------950916---------------------------------
//    http://stackoverflow.com/questions/4050111/best-practice-to-generate-a-jpa-dynamic-typed-query
    public <T> List<T> findAllEntitiesOrderedBy(String orderByColumn, boolean ascending) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteria = builder.createQuery();
        Root<T> entityRoot = criteria.from(entityClass);
        criteria.select(entityRoot);
        javax.persistence.criteria.Order order = ascending ? builder.asc(entityRoot.get(orderByColumn))
                : builder.desc(entityRoot.get(orderByColumn));
        criteria.orderBy(order);
        return getEntityManager().createQuery(criteria).getResultList();
    }

    /**  public List<T> findWithNamedQuery(String queryName, T entity, int maxResult) throws Exception {//"StatusRepository.findByAppId"
     List<T> result = null;
     try {
     TypedQuery<T> query = getQueryFindWithNamedQuery(queryName, entity);
     result = query.setMaxResults(maxResult).getResultList();
     } catch (NoResultException e) {
     throw new NoResultException(ERROR_MSG_CANT_FIND_ENTITY);
     }
     return result;
     }

     public T findSingleWithNamedQuery(String queryName, T entity) throws Exception {//"StatusRepository.findByAppId"
     T result = null;
     TypedQuery<T> query = getQueryFindWithNamedQuery(queryName, entity);
     try {
     result = (T) query.getSingleResult();
     } catch (NoResultException e) {
     throw new NoResultException(ERROR_MSG_CANT_FIND_ENTITY);
     }

     return result;
     }
     **/
    /**
     * private TypedQuery<T> getQueryFindWithNamedQuery(String queryName, T entity) throws Exception {
     * TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, entityClass);
     * Map<String, Object> map = getQueryParameters(query, entity);
     * for (Map.Entry<String, Object> entry : map.entrySet()) {
     * query.setParameter(entry.getKey(), entry.getValue());
     * }
     * return query;
     * }
     **/

    public String createWhereClause(Object object) throws Exception {
        final StringBuilder sb = new StringBuilder(" WHERE 1 = 1");
        BeanInfo infoDsc = Introspector.getBeanInfo(object.getClass());
        for (PropertyDescriptor pdDsc : infoDsc.getPropertyDescriptors()) {
            Method readerSrc = pdDsc.getReadMethod();
            String fieldName = pdDsc.getName();
            if (readerSrc != null && !fieldName.equalsIgnoreCase("class") && !"".equals(readerSrc)) {
                Object[] paramesReader = new Object[]{};
                Object value = readerSrc.invoke(object, paramesReader);
                if (value != null && !"".equals(value)) {

                    System.out.println("****name: " + fieldName + " ,readerSrc: " + readerSrc + " ,invok: " + value);
                    if (pdDsc.getPropertyType().getName().toLowerCase().contains("boolean")) {
                        sb.append(" AND c." + fieldName + " = " + value + "");
                    } else
                        sb.append(" AND LOWER(c." + fieldName + ") LIKE '%" + value + "%'");
                }
            }
        }
        return sb.toString();
    }

    public TypedQuery<T> createQueryFormObject(Object object) throws Exception {
        StringBuilder sb;// Populate count
        sb = new StringBuilder("SELECT c FROM " + entityClass.getSimpleName() + " c");// Populate pageItems
        sb.append(createWhereClause(object));
        System.out.println("***create Query Form Object:*** " + sb);
        return getEntityManager().createQuery(sb.toString(), entityClass);
    }

    public List<T> paginate(int page, Object object) throws Exception {
        TypedQuery<T> query = createQueryFormObject(object);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        List<T> list = query.getResultList();
        return list;
    }

    public List<T> createQueryFormObjectNew(Object object) throws Exception {
        TypedQuery<T> query = createQueryFormObject(object);
        System.out.println("***query:**** " + query);
        List<T> list = query.getResultList();
        return list;
    }

    public long paginateCount(Object object) throws Exception {
        StringBuilder sb;// Populate count
//        System.out.println("***object: "+object +" ,entityClass: "+entityClass);
        sb = new StringBuilder("SELECT count(c) FROM " + entityClass.getSimpleName() + " c");// Populate pageItems
        sb.append(createWhereClause(object));
//        System.out.println("***Query Count: "+sb);
        TypedQuery<Long> countCriteria = getEntityManager().createQuery(sb.toString(), Long.class);
        return countCriteria.getSingleResult();
    }

    public List<T> paginate(int page, String whereClause) {
        StringBuilder sb;// Populate count
        sb = new StringBuilder("SELECT c FROM " + entityClass.getSimpleName() + " c");// Populate pageItems
        sb.append(whereClause);
        TypedQuery<T> query = getEntityManager().createQuery(sb.toString(), entityClass);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    //--------
    public List<T> paginate3(int page) throws Exception {
        TypedQuery<T> query = createQueryFromPaginate();
        query.setFirstResult(page).setMaxResults(3);
        List<T> list = query.getResultList();
        return list;
    }

    public List<T> paginate(int page) throws Exception {
        TypedQuery<T> query = createQueryFromPaginate();
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        List<T> list = query.getResultList();
        return list;
    }

    private TypedQuery<T> createQueryFromPaginate() {
        StringBuilder sb;// Populate count
        sb = new StringBuilder("SELECT c FROM " + entityClass.getSimpleName() + " c");// Populate pageItems
//        LOGGER.debug("***Query: " + sb);
        return getEntityManager().createQuery(sb.toString(), entityClass);
    }

}