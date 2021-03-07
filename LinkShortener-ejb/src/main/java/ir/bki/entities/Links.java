package ir.bki.entities;


import ir.bki.util.serializer.GsonModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

import static ir.bki.app.AppConstants.DATASOURCE_SCHEMA;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = DATASOURCE_SCHEMA + "TB_Links", uniqueConstraints = {@UniqueConstraint(columnNames = {"REDIRECT_LINK"})})
@Cacheable(false)
@Data
@NamedQueries({
        @NamedQuery(name = Links.FIND_ALL, query = "SELECT u FROM Links u ORDER BY u.id DESC"),
        @NamedQuery(name = Links.FIND_BY_ID, query = "SELECT u FROM Links u WHERE u.id = :id"),
        @NamedQuery(name = Links.FIND_BY_LONG_URL, query = "SELECT u FROM Links u WHERE u.redirectLink = :redirectLink"),
        @NamedQuery(name = Links.FIND_BY_SHORT_URL, query = "SELECT u FROM Links u WHERE u.shortLink = :shortLink"),
        @NamedQuery(name = Links.COUNT_ALL, query = "SELECT COUNT(u) FROM Links u")
})
public class Links extends GsonModel implements Serializable {
    public static final String FIND_ALL = "Links.findAll";
    public static final String COUNT_ALL = "Links.countAll";
    public static final String FIND_BY_ID = "Links.findById";
    public static final String FIND_BY_LONG_URL = "Links.findByLongUrl";
    public static final String FIND_BY_SHORT_URL = "Links.findByShortUrl";

    @Id
    @SequenceGenerator(name = "T_LINKS_SEQ_GENERATOR", sequenceName = DATASOURCE_SCHEMA + "SQ_LINK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_LINKS_SEQ_GENERATOR")
    @Column(name = "PK_ID", nullable = false)
    private long id;

    @Column(name = "SHORT_LINK")
    private String shortLink;

    @Column(name = "REDIRECT_LINK", nullable = false, length = 2000)
    private String redirectLink;

    @Column(name = "HTTP_STATUS_CODE", length = 3)
    private int httpStatusCode;

    @Column(name = "ENABLED")
    private boolean enabled;

    @Column(name = "CREATED_AT", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "EXPIRED_AT")
    @Temporal(TemporalType.DATE)
    private Date expiresDate;

    @Column(name = "ACTIVATED_AT")
    @Temporal(TemporalType.DATE)
    private Date activatedDate;

    @Size(max = 400)
    @Column(name = "DESCRIPTION", length = 400)
    private String description;

    @PrePersist
    public void prePersist() {
        setCreatedDate(new Date());
    }
}
