package ir.bki.dto;

import com.google.gson.annotations.SerializedName;
import ir.bki.util.serializer.GsonModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.ejb.Stateless;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Stateless
public class LinkDto extends GsonModel implements Serializable {

    @SerializedName("long_link")
    @XmlElement(name = "long_link")
    private String longLink;

    @SerializedName("short_link")
    @XmlElement(name = "short_link")
    private String shortLink;

    @SerializedName("created_date")
    @XmlElement(name = "created_date")
    private Date createdDate;

    @SerializedName("activated_date")
    @XmlElement(name = "activated_date")
    private Date activatedDate;

    @SerializedName("expires_date")
    @XmlElement(name = "expires_ate")
    private String expiresDate;

    @SerializedName("desired_link")
    @XmlElement(name = "desired_link")
    private String desiredlink;

    @SerializedName("description")
    @XmlElement(name = "description")
    private String description;

    @SerializedName("enabled")
    @XmlElement(name = "enabled")
    private boolean enabled;

    @SerializedName("http_status_code")
    @XmlElement(name = "http_status_code")
    private int httpStatusCode;

}
