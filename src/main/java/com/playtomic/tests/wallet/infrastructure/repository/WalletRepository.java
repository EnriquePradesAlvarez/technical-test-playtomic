package com.playtomic.tests.wallet.infrastructure.repository;

import com.playtomic.tests.wallet.domain.exception.AppException;
import com.playtomic.tests.wallet.domain.model.Payment;
import com.playtomic.tests.wallet.domain.model.Wallet;
import io.vavr.control.Either;
import java.math.BigDecimal;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, String> {

    @Transactional
    default Either<AppException, Payment> topUpWalletById (String walletId, BigDecimal amount, Payment payment) {
        return findById(walletId)
            .map(wallet -> persist(wallet, amount, payment))
            .orElseGet(() -> Either.left(new AppException("Wallet not found with ID: ".concat(walletId))));
    }
    
    private Either<AppException, Payment> persist (Wallet wallet, BigDecimal amount, Payment payment) {
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.getPayments().add(payment.getId());
        save(wallet);
        return Either.right(payment);
    }
}
