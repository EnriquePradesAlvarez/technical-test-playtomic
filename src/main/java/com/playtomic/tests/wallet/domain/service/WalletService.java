package com.playtomic.tests.wallet.domain.service;

import com.playtomic.tests.wallet.domain.exception.AppException;
import com.playtomic.tests.wallet.domain.model.Payment;
import com.playtomic.tests.wallet.domain.model.Wallet;
import com.playtomic.tests.wallet.infrastructure.repository.WalletRepository;
import io.vavr.control.Either;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public Either<AppException, Wallet> getWalletInfo(String walletId) {
        return walletRepository.findById(walletId)
                .map(Either::<AppException, Wallet>right)
                .orElse(Either.left(new AppException("There is no wallet with the requested id ".concat(walletId))));
    }

    public Either<AppException, Payment> topUpWallet(String walletId, BigDecimal amount, Payment payment) {
        return walletRepository.topUpWalletById(walletId, amount, payment)
            .mapLeft(AppException::new);
    }
}
