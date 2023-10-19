package io.codertown.web.entity.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AddressThird {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ADDR_Third_NO")
    private Long addrThirdNo;
    private String addrName;
    private String addrCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ADDR_SECOND_NO")
    private AddressSecond addressSecond;

}
