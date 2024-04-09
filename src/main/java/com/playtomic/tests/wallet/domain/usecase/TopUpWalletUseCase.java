package com.playtomic.tests.wallet.domain.usecase;


import com.playtomic.tests.wallet.domain.exception.AppException;
import com.playtomic.tests.wallet.domain.model.Payment;
import com.playtomic.tests.wallet.domain.service.StripeService;
import com.playtomic.tests.wallet.domain.service.WalletService;
import io.vavr.Function2;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class TopUpWalletUseCase implements
    Function2<String, BigDecimal, Either<AppException, Payment>> {

    private final transient StripeService stripeService;

    private final transient WalletService walletService;

    @Override
    public Either<AppException, Payment> apply(String walletId, BigDecimal amount) {
        return processPaymentInStripe(walletId, amount)
            .flatMap(payment -> walletService.topUpWallet(walletId, amount, payment))
            .mapLeft(AppException::new);
    }

    private Either<AppException, Payment> processPaymentInStripe(String walletId, BigDecimal amount) {
        return Try.of(() -> stripeService.charge(walletId, amount))
            .toEither()
            .mapLeft(AppException::new);
    }

}
