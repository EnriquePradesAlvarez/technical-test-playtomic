package com.playtomic.tests.wallet.application.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;


/**
 * This class is not really necessary, I just wanted to include to clarify that I can have different objects in different layers.
 * In the case that the Object Wallet could have more attributes, this class and the WalletToWalletResponseConverter could be used to map it out.
 */
@Data
@Builder
public class WalletResponse {

    private String id;
    private BigDecimal balance;
    private String owner;
    private List<String> payments;

}
