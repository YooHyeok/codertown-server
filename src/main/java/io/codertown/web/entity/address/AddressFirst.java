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
public class AddressFirst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ADDR_FIRST_NO")
    private Long addrFirstNo;
    private String addrName;
    private String addrCode;
    @OneToMany(mappedBy = "addressFirst", cascade = CascadeType.ALL)
    private List<AddressSecond> addressSecondList = new ArrayList<>();

}
