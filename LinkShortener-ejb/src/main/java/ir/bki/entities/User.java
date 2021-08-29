package ir.bki.entities;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ir.bki.util.PasswordUtils;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static ir.bki.app.AppConstants.DATASOURCE_SCHEMA;

/**
 * @author Antonio Goncalves
 * http://www.antoniogoncalves.org
 * --
 */

@Entity
@Cacheable(false)
@Table(name = DATASOURCE_SCHEMA + "TB_USER")
@NamedQueries({
        @NamedQuery(name = User.FIND_ALL, query = "SELECT u FROM User u ORDER BY u.lastName DESC"),
        @NamedQuery(name = User.FIND_BY_USERNAME, query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = User.FIND_BY_USERNAME_PASSWORD, query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"),
        @NamedQuery(name = User.COUNT_ALL, query = "SELECT COUNT(u) FROM User u")
})
@Data
public class User {//TODO ADD TRY COUNT

    // ======================================
    // =             Constants              =
    // ======================================

    public static final String PARAM_USER_NAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String FIND_ALL = "User.findAll";
    public static final String COUNT_ALL = "User.countAll";
    public static final String FIND_BY_USERNAME = "User.findByUsername";
    public static final String FIND_BY_ROLE = "User.findByRole";
    public static final String FIND_BY_USERNAME_PASSWORD = "User.findByUsernameAndPassword";

    // ======================================
    // =             Attributes             =
    // ======================================

    @ManyToMany(fetch = FetchType.EAGER)//,  mappedBy = "role",orphanRemoval = true
    @JoinTable(
            name = DATASOURCE_SCHEMA+"JND_USER_ROLE",
            joinColumns = @JoinColumn(name = "FK_USER", referencedColumnName = "PK_USER_NAME"),//
            inverseJoinColumns = @JoinColumn(name = "FK_ROLE", referencedColumnName = "PK_NAME"))//
    private Collection<Role> roles = new ArrayList<>();

    @Id
    @NotNull
    @Size(max = 29)
    @Column(name = "PK_USER_NAME", length = 29, nullable = false)
    private String username;

    @Column(name = "LAST_NAME", length = 100)
    private String lastName;

    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @NotNull
    @Size(max = 250)
    @Column(name = "PASSWORD", nullable = false, length = 250)
    private String password;

    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    @SerializedName("create-time")
    private Date createTime;

    @Column(name = "UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @SerializedName("update-time")
    @Expose
    private Date updateTime;

    @Size(max = 15)
    @Column(name = "NATIONAL_CODE", length = 15)
    private String nationalCode;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "PERSONAL_ID")
    private Long personalId;

    @Column(name = "AVATAR_URL")
    private String avatarUrl;

    @Column(name = "COMPANY", length = 100)
    private String company;

    @Size(max = 15, message = "#telphone must be mor than 15 number")
    @Column(name = "TEL", length = 15)
    private String tel;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "RTY_COUNT")
    private Integer tryCount;

    @Size(max = 300)
    @Column(name = "STORAGE_KEY", length = 300)
    private String storageKey;
    @Size(max = 500)
    @Column(name = "URL", length = 500)
    private String url;


    @Column(name = "ENABLED")
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;

    @Column(name = "STATUS")
    @Expose
    private Integer status;

    @Column(name = "VERSION")
    @SerializedName("version")
    @Expose
    private Long version;

    public User() {
        this("", "", Boolean.TRUE);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String firstName, String lastName, String username, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Boolean enabled) {
        this.username = username;
        this.password = password;
        setEnabled(enabled);
    }

    @PrePersist
    private void onPrePersist() {
        password = PasswordUtils.digestPassword(password);
        setCreateTime(new Date());
        setEnabled(true);
        setUpdateTime(new Date());
        if (getVersion() == null)
            setVersion(0L);
        setVersion(1 + getVersion());

    }

}
