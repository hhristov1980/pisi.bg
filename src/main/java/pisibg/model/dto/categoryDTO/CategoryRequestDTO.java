package pisibg.model.dto.categoryDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@Component
public class CategoryRequestDTO {
    @NotBlank(message = "Name is mandatory!")
    private String name;
}
