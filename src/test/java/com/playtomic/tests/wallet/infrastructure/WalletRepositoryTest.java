package com.playtomic.tests.wallet.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.playtomic.tests.wallet.domain.exception.AppException;
import com.playtomic.tests.wallet.domain.model.Payment;
import com.playtomic.tests.wallet.domain.model.Wallet;
import com.playtomic.tests.wallet.domain.service.WalletService;
import io.vavr.control.Either;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@DisplayName("Wallet repository test")
@ExtendWith(MockitoExtension.class)
public class WalletRepositoryTest {

    @Mock
    private WalletService walletService;

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        String displayName = testInfo.getDisplayName();
        log.info("Before start test:  " + displayName);
    }

    @AfterEach
    void afterEach(TestInfo testInfo) {
        String displayName = testInfo.getDisplayName();
        log.info("After end test:  " + displayName);
    }

    @Test
    @DisplayName("Should Return Wallet Info Mocked")
    void shouldReturnPaymentMocked() {
        String mockPaymentId = "6baa97ee-8e67-4c3b-af07-974f5a55a881";

        doReturn(Either.right(new Payment(mockPaymentId))).when(walletService).topUpWallet(any(), any(), any());

        Either<AppException, Payment> payment = walletService.topUpWallet(any(), any(), any());

        verify(walletService, times(1)).topUpWallet(any(), any(), any());
        verify(walletService, times(1)).topUpWallet(any(), any(), any());
        assertThat(payment.isRight()).isTrue();
        assertThat(payment.get().getId()).isEqualTo(mockPaymentId);
    }
}
