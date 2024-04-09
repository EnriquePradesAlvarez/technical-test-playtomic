package com.playtomic.tests.wallet.application;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import com.playtomic.tests.wallet.AbstractTestBase;
import com.playtomic.tests.wallet.application.response.WalletResponse;
import com.playtomic.tests.wallet.domain.model.Payment;
import com.playtomic.tests.wallet.domain.model.Wallet;
import com.playtomic.tests.wallet.infrastructure.repository.WalletRepository;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@Slf4j
@DisplayName("Wallet resource Test")
public class WalletResourceTest extends AbstractTestBase {

    @LocalServerPort
    private Integer serverPort;

    @Autowired
    WalletRepository walletRepository;

    private static final String ROOT_URI = "http://localhost:%s/%s";
    private static final String PATH_WALLET_INFO = "wallets/";
    private static final String PATH_WALLET_TOPUP = "wallets/%s/topup/";

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
    @DisplayName("Should Return 400 error because balance parameter format is incorrect")
    void shouldReturnBadRequestDueAmountIsInvalid() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .put(String.format(ROOT_URI, serverPort, String.format(PATH_WALLET_TOPUP, "XXX").concat("XXX")))
            .andReturn()
            .then()
            .assertThat()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should return wallet info")
    void shouldReturnWalletInfo() {

        String walletId = "13250326-e9b5-49ad-a8f5-3d0268746856";
        String owner = "Enrique Prades Alvarez";
        BigDecimal balance = BigDecimal.TEN.setScale(2);
        List<String> payments = Arrays.asList("8b97c318-2df2-4353-83cd-9e34f6e921ed", "06ec291b-ccf5-4fd2-ab79-5420666e6adc");

        walletRepository.save(Wallet.builder().id(walletId).owner(owner).balance(balance).payments(payments).build());

        final WalletResponse response = executeHTTPGetRequestToWalletInfo(walletId);

        assertThatAPIResponseContainsTheExpectedValuesForGetWalletInfo(response, walletId, owner, balance, payments);
    }

    private WalletResponse executeHTTPGetRequestToWalletInfo(String walletId) {
        return given()
            .contentType(ContentType.JSON)
            .when()
            .get(String.format(ROOT_URI, serverPort, PATH_WALLET_INFO.concat(walletId)))
            .andReturn().getBody()
            .as(WalletResponse.class);
    }

    public void assertThatAPIResponseContainsTheExpectedValuesForGetWalletInfo(WalletResponse response, String walletId, String owner, BigDecimal balance, List<String> payments) {
        assertThat(response.getId()).isEqualTo(walletId);
        assertThat(response.getOwner()).isEqualTo(owner);
        assertThat(response.getBalance()).isEqualTo(balance);
        assertThat(response.getPayments()).containsAll(payments);
    }

    @Test
    @DisplayName("Should return wallet info")
    void shouldTopUpWalletAndCheckIfBalanceHasChanged() {

        String walletId = "13250326-e9b5-49ad-a8f5-3d0268746856";
        String owner = "Enrique Prades Alvarez";
        BigDecimal initialBalance = BigDecimal.TEN.setScale(2);
        List<String> payments = Arrays.asList("8b97c318-2df2-4353-83cd-9e34f6e921ed", "06ec291b-ccf5-4fd2-ab79-5420666e6adc");

        walletRepository.save(Wallet.builder().id(walletId).owner(owner).balance(initialBalance).payments(payments).build());

        Optional<Wallet> wallet = walletRepository.findById(walletId);
        assertThat(wallet).isPresent();
        assertThat(wallet.get().getId()).isEqualTo(walletId);

        BigDecimal amount = BigDecimal.valueOf(200l);
        final Payment response = executeHTTPPutRequestToTopUpWallet(walletId, amount);
        assertThat(response.getId()).isNotNull();

        assertThatAPIResponseContainsTheExpectedValuesForTopUpWallet(walletRepository.findById(walletId).get(), initialBalance, amount, response.getId());
    }

    private Payment executeHTTPPutRequestToTopUpWallet(String walletId, BigDecimal amount) {
        return given()
            .contentType(ContentType.JSON)
            .when()
            .put(String.format(ROOT_URI, serverPort, String.format(PATH_WALLET_TOPUP, walletId).concat(amount.toString())))
            .andReturn().getBody()
            .as(Payment.class);
    }

    public void assertThatAPIResponseContainsTheExpectedValuesForTopUpWallet(Wallet wallet, BigDecimal initialBalance, BigDecimal amount, String paymentId) {
        assertThat(wallet.getBalance()).isEqualTo(initialBalance.add(amount));
        assertThat(wallet.getPayments()).contains(paymentId);
    }
}
