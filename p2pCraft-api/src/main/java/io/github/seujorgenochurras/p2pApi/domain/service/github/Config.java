package io.github.seujorgenochurras.p2pApi.domain.service.github;

public class Config {
    protected static final String DEFAULT_PROPERTIES = """
        #Minecraft server properties
        #Mon Aug 19 11:08:17 BRT 2024
        allow-flight=false
        allow-nether=true
        broadcast-console-to-ops=true
        broadcast-rcon-to-ops=true
        difficulty=easy
        enable-command-block=false
        enable-jmx-monitoring=false
        enable-query=false
        enable-rcon=false
        enable-status=true
        enforce-secure-profile=true
        enforce-whitelist=false
        entity-broadcast-range-percentage=100
        force-gamemode=false
        function-permission-level=2
        gamemode=survival
        generate-structures=true
        generator-settings={}
        hardcore=false
        hide-online-players=false
        initial-disabled-packs=
        initial-enabled-packs=vanilla
        level-name=world
        level-seed=
        level-type=minecraft\\:normal
        log-ips=true
        max-chained-neighbor-updates=1000000
        max-players=20
        max-tick-time=60000
        max-world-size=29999984
        motd=A Minecraft Server
        network-compression-threshold=256
        online-mode=false
        op-permission-level=4
        player-idle-timeout=0
        prevent-proxy-connections=false
        pvp=true
        query.port=25565
        rate-limit=0
        rcon.password=
        rcon.port=25575
        require-resource-pack=false
        resource-pack=
        resource-pack-id=
        resource-pack-prompt=
        resource-pack-sha1=
        server-ip=
        server-port=25565
        simulation-distance=10
        spawn-animals=true
        spawn-monsters=true
        spawn-npcs=true
        spawn-protection=16
        sync-chunk-writes=true
        text-filtering-config=
        use-native-transport=true
        view-distance=10
        white-list=false
        """;
}