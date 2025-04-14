package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @NotEmpty
    @Size(min=4,max=40)
    private String nom ;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date dateNaissance;
    private boolean malade ;
    @DecimalMin(("99"))
    private int score ;

}
