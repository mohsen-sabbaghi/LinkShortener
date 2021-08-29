package ir.bki.dao;


import ir.bki.entities.User;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserDao extends AbstractDaoFacade<User> {

    @PersistenceContext(name = "PU_ORACLE_LS")
    private EntityManager em;

    public UserDao() {
        super(User.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Map<String, User> findAllToMap() throws Exception {
        List<User> list = findAll();
        return list.stream().collect(Collectors.toMap(user -> user.getUsername(), user -> user));
    }
}
