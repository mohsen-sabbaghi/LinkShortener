package ir.bki.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mahdi Sharifi
 * @version 1.0.0
 * https://www.linkedin.com/in/mahdisharifi/
 * @since ۶/۱۴/۲۰۲۰
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAggregatorDto {
    private String roleName;
    private Long count;
}
