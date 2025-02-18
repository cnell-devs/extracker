package com.extracker.backend.link;

import com.extracker.backend.db.items.ItemService;
import com.extracker.backend.db.users.UserEntity;
import com.extracker.backend.db.users.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class LinkController {
    private final CreateLinkToken linkToken;

    private ItemService itemService;
    private UserRepository userRepository;

    public LinkController(CreateLinkToken linkToken, ItemService itemService, ExchangeToken exchangeToken, UserRepository userRepository) {
        this.linkToken = linkToken;
        this.itemService = itemService;
        this.exchangeToken = exchangeToken;
        this.userRepository = userRepository;
    }

    private final ExchangeToken exchangeToken;

    public record LinkTokenResponse(String linkToken) {
    }

    public record AccessTokenResponse(String accessToken) {
    }

    public record PublicTokenRequest(String publicToken) {
    }


    @GetMapping("/create-link-token")
    public ResponseEntity<LinkTokenResponse> createLinkToken() {
        System.out.println("RECEIVED LINK");
        String getToken = linkToken.getToken();
        if (getToken == null) {
            return ResponseEntity.status(500).body(new LinkTokenResponse("Error: Link token is empty"));
        }
        return ResponseEntity.ok(new LinkTokenResponse(getToken));
    }

    @PostMapping(value = "/exchange-token", consumes = "application/json")
    public ResponseEntity<AccessTokenResponse> exchangeToken(@RequestBody PublicTokenRequest publicToken) {
        String accessToken = exchangeToken.exchange(publicToken.publicToken);

        System.out.println("accToken" + accessToken);
        if (accessToken == null) {
            return ResponseEntity.status(500).body(new AccessTokenResponse("Error: Link token is empty"));
        }

        try {
            itemService.saveItemFromPlaidResponse(userRepository.findByUsername("user").getId(), accessToken);
            System.out.println("SAVING");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(new AccessTokenResponse(accessToken));
    }


}
