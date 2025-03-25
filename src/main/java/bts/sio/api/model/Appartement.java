package bts.sio.api.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "appartement")
public class Appartement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="numero")
    private String numero;

    @Column(name="surface")
    private Float surface;

    @Column(name="nbPiece")
    private Integer nbPiece;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "batiment_id")
    private Batiment batiment;
}
