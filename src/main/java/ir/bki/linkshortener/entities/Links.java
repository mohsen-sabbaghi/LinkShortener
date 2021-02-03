package ir.bki.linkshortener.entities;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TB_Links")
@Cacheable(false)
@Data
@NamedQueries({
        @NamedQuery(name = Links.FIND_ALL, query = "SELECT u FROM Links u ORDER BY u.id DESC"),
        @NamedQuery(name = Links.FIND_BY_ID, query = "SELECT u FROM Links u WHERE u.id = :id"),
        @NamedQuery(name = Links.COUNT_ALL, query = "SELECT COUNT(u) FROM Links u")
})
public class Links implements Serializable {
    public static final String FIND_ALL = "Links.findAll";
    public static final String COUNT_ALL = "Links.countAll";
    public static final String FIND_BY_ID = "Links.findById";

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
//    @SequenceGenerator(name = "TB_LINKS_SEQ_GENERATOR", sequenceName = "LINKS_SEQ", initialValue = 10000, allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_LINKS_SEQ_GENERATOR")
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresDate;

    @Column(name = "ACTIVATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activatedDate;

    @Size(max = 4000)
    @Column(name = "DESCRIPTION", length = 4000)
    private String description;
}
