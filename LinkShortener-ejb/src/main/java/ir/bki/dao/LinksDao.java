package ir.bki.dao;

import ir.bki.entities.Links;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
@TransactionManagement
@TransactionAttribute
public class LinksDao extends AbstractDaoFacade<Links> {

    @PersistenceContext(name = "PU_ORACLE_LS")
    protected EntityManager em;

    public LinksDao() {
        super(Links.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

//    public Links create(Links links) {
//        getEm().persist(links);
//        getEm().flush();
//        getEm().refresh(links);
//        return links;
//    }

    public Map<String, Links> findAllToMap() {
        List<Links> list = findAll();
        return list.stream().collect(Collectors.toMap(links -> links.getShortLink(), Links -> Links));
    }
}
