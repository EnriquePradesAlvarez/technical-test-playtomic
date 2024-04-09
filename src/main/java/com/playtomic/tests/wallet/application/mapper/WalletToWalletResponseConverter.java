package com.playtomic.tests.wallet.application.mapper;

import com.playtomic.tests.wallet.application.response.WalletResponse;
import com.playtomic.tests.wallet.domain.model.Wallet;
import com.playtomic.tests.wallet.domain.shared.GenericConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class WalletToWalletResponseConverter implements
    GenericConverter<WalletResponse, Wallet> {

    @Override
    public Wallet mapAtoB(final WalletResponse walletResponse) {
        return Wallet.builder()
            .id(walletResponse.getId())
            .balance(walletResponse.getBalance())
            .owner(walletResponse.getOwner())
            .build();
    }

    @Override
    public WalletResponse mapBtoA(final Wallet wallet) {
        return WalletResponse.builder()
            .id(wallet.getId())
            .balance(wallet.getBalance())
            .owner(wallet.getOwner())
            .payments(wallet.getPayments())
            .build();
    }
}
