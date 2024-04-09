package com.playtomic.tests.wallet.domain.usecase;

import com.playtomic.tests.wallet.domain.exception.AppException;
import com.playtomic.tests.wallet.domain.model.Wallet;
import com.playtomic.tests.wallet.domain.service.WalletService;
import io.vavr.Function1;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class GetWalletInfoUseCase implements
        Function1<String, Either<AppException, Wallet>> {

    private final transient WalletService walletService;

    @Override
    public Either<AppException, Wallet> apply(String walletId) {
        return walletService.getWalletInfo(walletId);
    }
}
