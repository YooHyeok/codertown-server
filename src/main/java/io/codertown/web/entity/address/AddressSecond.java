package io.codertown.web.entity.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AddressSecond {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ADDR_SECOND_NO")
    private Long addrSecondNo;
    private String addrName;
    private String addrCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ADDR_FIRST_NO")
    private AddressFirst addressFirst;
    @OneToMany(mappedBy = "addressSecond", cascade = CascadeType.ALL)
    private List<AddressThird> addressThirdList = new ArrayList<>();

}
