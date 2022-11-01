package hackweek.blr.project.hackathon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdData {
    private String adId;
    private Integer likes;
    private Integer shares;
    private Integer clicks;
}
