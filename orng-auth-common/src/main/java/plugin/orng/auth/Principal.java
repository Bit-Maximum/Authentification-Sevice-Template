package plugin.orng.auth;

import lombok.*;

import static lombok.AccessLevel.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Principal {

    private long id;

}
