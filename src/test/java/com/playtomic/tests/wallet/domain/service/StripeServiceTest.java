package com.playtomic.tests.wallet.domain.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.playtomic.tests.wallet.AbstractTestBase;
import com.playtomic.tests.wallet.domain.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.domain.exception.StripeServiceException;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */
@Slf4j
@DisplayName("Stripe Service Test")
public class StripeServiceTest extends AbstractTestBase {

    @Autowired
    StripeService stripeService;

    @Test
    public void test_exception() {
        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            stripeService.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        assertThat(stripeService.charge("4242 4242 4242 4242", new BigDecimal(15))).isNotNull();
    }
}
