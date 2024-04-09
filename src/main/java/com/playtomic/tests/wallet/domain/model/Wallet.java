package com.playtomic.tests.wallet.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    private String id;
    private BigDecimal balance;
    private String owner;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> payments = new ArrayList<>();

}
