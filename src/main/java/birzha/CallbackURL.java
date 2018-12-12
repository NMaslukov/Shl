package birzha;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CallbackURL {
    private String callbackURL;
    private Integer pairId;
}
