package ir.bki.entities;

import ir.bki.entities.dto.RoleAggregatorDto;
import ir.bki.util.serializer.GsonExcludeField;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static ir.bki.app.AppConstants.DATASOURCE_SCHEMA;

/**
 * @author Mahdi Sharifi
 * @version 1.0.0
 * https://www.linkedin.com/in/mahdisharifi/
 * @since 20/01/2020
 */

@Entity
@Table(name = DATASOURCE_SCHEMA +"TB_ROLE")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Role.countRoleName",//"Post.findByIdWithCommentCount",
                query = "SELECT	FK_ROLE AS ROLE_NAME,	COUNT( * ) AS ROLE_COUNTER FROM	LINKSHORTENER.JND_USER_ROLE WHERE FK_ROLE =:roleName GROUP BY FK_ROLE",
                resultSetMapping = "Role.countRoleNameMapping")
})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "Role.countRoleNameMapping",
                classes = {
                        @ConstructorResult(
                                columns = {
                                        @ColumnResult(name = "ROLE_NAME", type = String.class),
                                        @ColumnResult(name = "ROLE_COUNTER", type = long.class)
                                },
                                targetClass = RoleAggregatorDto.class
                        )}
        )
})
@NamedQueries({
        @NamedQuery(name = Role.FIND_ALL, query = "SELECT m FROM Role m"),
        @NamedQuery(name = Role.FIND_BY_NAME, query = "SELECT m FROM Role m WHERE m.name = :name")
})
@Getter
public class Role {

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String FIND_ALL = "Role.findAll";
    public static final String FIND_BY_NAME = "Role.findById";

    // ======================================
    // =             Attributes             =
    // ======================================
    @Column(name = "CREATE_AT")
    @Temporal(TemporalType.TIMESTAMP)
    @GsonExcludeField
    protected Date createAt;
    @Column(name = "UPDATE_AT")
    @GsonExcludeField
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updateAt;
    @Column(name = "ENABLED")
    @GsonExcludeField
    protected Boolean enabled;
    @Id
    @Column(name = "PK_NAME", length = 30, nullable = false)
    private String name; //Parent, Child, Consultant, Guest, Admin
    @ManyToMany(mappedBy = "roles")
    @GsonExcludeField
    private List<User> userList;

    // ======================================
    // =            Constructors            =
    // ======================================
    public Role() {
        setEnabled(true);
    }

    // ======================================
    // =         Lifecycle methods          =
    // ======================================
    public Role(String name) {
        this.name = name;
        setEnabled(true);
    }

    public String getId() {
        return getName();
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Role: ");
        sb.append(name);
        return sb.toString();
    }
}
