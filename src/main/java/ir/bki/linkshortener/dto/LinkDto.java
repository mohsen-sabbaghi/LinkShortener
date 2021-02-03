package ir.bki.linkshortener.dto;
import lombok.Data;

@Data
public class LinkDto {

    private String longLink;
    private String expiresDate;

}
