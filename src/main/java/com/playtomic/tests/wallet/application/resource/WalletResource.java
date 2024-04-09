package com.playtomic.tests.wallet.application.resource;

import com.playtomic.tests.wallet.application.mapper.WalletToWalletResponseConverter;
import com.playtomic.tests.wallet.application.response.WalletResponse;
import com.playtomic.tests.wallet.domain.model.Payment;
import com.playtomic.tests.wallet.domain.usecase.GetWalletInfoUseCase;
import com.playtomic.tests.wallet.domain.usecase.TopUpWalletUseCase;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.container.TimeoutHandler;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Component
public class WalletResource {

    private final GetWalletInfoUseCase getWalletInfoUseCase;
    private final TopUpWalletUseCase topUpWalletUseCase;
    private final WalletToWalletResponseConverter converter;
    private final TimeoutHandler defaultTimeHandler;

    @GetMapping(value = "/wallets/{walletId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WalletResponse> getWalletInfo(@NonNull @PathVariable("walletId") String walletId) {
        log.info("getWallet info: walletId {}", walletId);
        return ResponseEntity.ok(getWalletInfoUseCase.apply(walletId)
            .map(converter::mapBtoA)
            .getOrElseThrow(e -> e));
    }

    @PutMapping(value = "/wallets/{walletId}/topup/{amount}")
    public ResponseEntity<Payment> topUpWallet(@NonNull @PathVariable("walletId") String walletId,
        @NonNull @PathVariable("amount") BigDecimal amount) {
        log.info("top up Wallet: walletId {} amount {}", walletId, amount);
        return ResponseEntity.ok(topUpWalletUseCase.apply(walletId, amount)
            .getOrElseThrow(e -> e));
    }

    /**
     * This endpoint does not work because there are problems within the versions of springboot, jersey and java11,
     * I wanted to keep that because this is the way that I would have handled High Availability and concurrency, including a timeoutHandler
     * Also i searched that there is an annotation @Async to do that but i didnt know how to properly implement it in this example, maybe in the topup endpoint but
     * it would depend on if the other part expect response or not
     */
    @GET
    @Path("/ha/wallets/{walletId}")
    @Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public void getHAWalletInfo(@Suspended AsyncResponse asyncResponse,
        @NonNull @PathParam("walletId") String walletId) {
        log.info("getWallet info: walletId {}", walletId);
        asyncResponse.setTimeout(5l, TimeUnit.MILLISECONDS);
        asyncResponse.setTimeoutHandler(defaultTimeHandler);

        getWalletInfoUseCase.apply(walletId)
            .map(converter::mapBtoA)
            .peek(walletInfo -> asyncResponse.resume(buildResponse(walletInfo)))
            .getOrElseThrow(e -> e);
    }

    private Response buildResponse(WalletResponse walletResponse) {
        return Response.status(Status.OK).entity(walletResponse).build();
    }

}
