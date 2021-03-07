package ir.bki.dao;

import ir.bki.entities.Links;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
@TransactionManagement
@TransactionAttribute
public class LinksDao implements Serializable {

    @PersistenceContext(name = "PU_ORACLE_LS")
    protected EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public Links create(Links links) {
        getEm().persist(links);
        getEm().flush();
        getEm().refresh(links);
        return links;
    }

}
