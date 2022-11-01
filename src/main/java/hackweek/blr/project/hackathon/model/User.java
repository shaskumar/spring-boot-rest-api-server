package hackweek.blr.project.hackathon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String UserEmail;
    private String password;
    private Integer SharedAds;
    private Integer TotalAdCash;
}
