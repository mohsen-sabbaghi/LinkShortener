package ir.bki.dao;


import ir.bki.entities.User;
import ir.bki.util.PasswordUtils;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserDao {

    @PersistenceContext(name = "PU_ORACLE_LS")
    private EntityManager em;

    public UserDao() {

    }

    protected EntityManager getEntityManager() {
        return em;
    }

    public User findByUsername(String username) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put(User.PARAM_USER_NAME, username);
        return findSingleWithNamedQuery(User.FIND_BY_USERNAME, parameter, false);
    }

    public User findSingleWithNamedQuery(String namedQueryName, Map<String, Object> parameters, boolean cacheable) throws NoResultException {

        Query query = getEntityManager().createNamedQuery(namedQueryName);

        if (parameters != null) {
            Set<Map.Entry<String, Object>> rawParameters = parameters.entrySet();
            for (Map.Entry<String, Object> entry : rawParameters) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        User result = null;
        try {
            if (!cacheable)
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            result = (User) query.getSingleResult();

        } catch (NoResultException e) {
            throw new NoResultException("ERROR_MSG_CANT_FIND_ENTITY");
        }
        return result;
    }

    public User findByUsernameAndpassword(String username, String password) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put(User.PARAM_USER_NAME, username);
        parameter.put(User.PARAM_PASSWORD, PasswordUtils.digestPassword(password));
        //findSingleWithNamedQuery(User.FIND_BY_USERNAME_PASSWORD, parameter);
        return findSingleWithNamedQuery(User.FIND_BY_USERNAME_PASSWORD, parameter, false);
    }

    public Map<String, User> findAllToMap() throws Exception {
        List<User> list = findAll();
        return list.stream().collect(Collectors.toMap(user -> user.getUsername(), user -> user));
    }

    public List<User> findAll() {
//        if (getEntityManager() == null)
//            System.out.println("** EM ISNULL");
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public User edit(User entity, Object id) throws Exception {
        User entityReadFromDb = findById(id);
        convertToAnotherObject(entity, entityReadFromDb);
        entity = edit(entity);
        return entity;
    }

    public User edit(User entity) {
        entity = getEntityManager().merge(entity);
        return entity;
    }

    public User findById(Object id) {
        return getEntityManager().find(User.class, id);

    }

    public Object convertToAnotherObject(Object src, Object dsc) throws Exception {
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

}
