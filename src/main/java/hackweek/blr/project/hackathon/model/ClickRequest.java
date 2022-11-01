package hackweek.blr.project.hackathon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClickRequest {
    private String advertiseId;
    private String userId;
}
