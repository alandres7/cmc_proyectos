package es.grupocmc.commons.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class StatusError {

    private int status;
    private String title;
    private String code;
    private String detail;
}
