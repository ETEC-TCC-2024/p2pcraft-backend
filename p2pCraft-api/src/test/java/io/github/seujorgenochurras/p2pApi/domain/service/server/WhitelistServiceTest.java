package io.github.seujorgenochurras.p2pApi.domain.service.server;


import io.github.seujorgenochurras.p2pApi.domain.model.player.PlayerDataFactory;
import io.github.seujorgenochurras.p2pApi.domain.model.server.Server;
import io.github.seujorgenochurras.p2pApi.domain.model.server.ServerDataFactory;
import io.github.seujorgenochurras.p2pApi.domain.model.server.player.Player;
import io.github.seujorgenochurras.p2pApi.domain.service.MojangService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WhitelistServiceTest {


    @Mock
    private ServerFilesService serverFilesService;

    @Mock
    private ServerService serverService;

    @Mock
    private MojangService mojangService;

    @InjectMocks
    private WhitelistService whitelistService;

    private Player validMinecraftPlayer;
    private Server validServer;
    private ArrayList<Player> validWhitelistedPlayers;

    @BeforeEach
    void setup() {
        validServer = ServerDataFactory.createValidServer();
        validMinecraftPlayer = PlayerDataFactory.createValidPlayer();
        validWhitelistedPlayers = new ArrayList<>();
        validWhitelistedPlayers.add(validMinecraftPlayer);
        validWhitelistedPlayers.add(PlayerDataFactory.createValidPlayer());
    }

    @Test
    void add_ShouldReturnWhitelistedPlayers_WhenAddSuccessful() {
        when(mojangService.findPlayerByName(validMinecraftPlayer.getName())).thenReturn(validMinecraftPlayer);

        when(serverService.findByName(validServer.getName())).thenReturn(validServer);

        when(serverFilesService.addToWhitelist(any(), any())).thenReturn(validWhitelistedPlayers);

        ArrayList<Player> whiteListedPlayers = whitelistService.addToWhitelist(validMinecraftPlayer.getName(),
            validServer.getName());

        Assertions.assertEquals(validWhitelistedPlayers, whiteListedPlayers);
    }
}
