package ir.bki.dao;

import ir.bki.entities.Role;
import ir.bki.entities.dto.RoleAggregatorDto;

import javax.ejb.*;
import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class RoleDao {

    @PersistenceContext(name = "PU_ORACLE_LS")
    private EntityManager em;

    public RoleDao() {
    }


    public Optional<RoleAggregatorDto> countRoleName(String roleName) {//https://vladmihalcea.com/jpa-sqlresultsetmapping/
        RoleAggregatorDto dto = null;
        try {
            dto = (RoleAggregatorDto) em
                    .createNamedQuery("Role.countRoleName")
                    .setParameter("roleName", roleName)
                    .getSingleResult();
        } catch (Exception ex) {
//            System.err.println("EException: " + ex.getMessage());
        }
        System.out.println("#dto: " + dto);
        return Optional.ofNullable(dto);
    }

    // Queries
    public List<Role> findAll() throws Exception {
        Query query = em.createNamedQuery(Role.FIND_ALL, Role.class);
        return query.getResultList();

    }

    public Map<String, Role> findAllMap() throws Exception {
        List<Role> list = findAll();
        return list.stream().collect(Collectors.toMap(input -> input.getId(), input -> input));
    }


    public Role findByName(String name) throws EntityNotFoundException {
        TypedQuery<Role> q = em.createNamedQuery(Role.FIND_BY_NAME, Role.class);
        q.setParameter("name", name);
        Role entity = q.getSingleResult();
        if (entity == null)
            throw new EntityNotFoundException("Entity Not Found! id: " + name);
        return entity;
    }

    public Role findById(String name) throws EntityNotFoundException {
        Role entity = em.find(Role.class, name);
        if (entity == null)
            throw new EntityNotFoundException("Entity Not Found! id: " + name);
        return entity;
    }

}
