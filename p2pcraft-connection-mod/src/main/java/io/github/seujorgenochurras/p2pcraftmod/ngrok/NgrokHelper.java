package io.github.seujorgenochurras.p2pcraftmod.ngrok;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Proto;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import io.github.seujorgenochurras.p2pcraftmod.client.config.ConfigFile;

import java.util.logging.Logger;

public class NgrokHelper {

    private static final Logger logger = Logger.getLogger(NgrokHelper.class.getName());
    private final static String NGROK_TOKEN = ConfigFile.get("NGROK_TOKEN");

    public static String openTunnel() {

        final NgrokClient ngrokClient = new NgrokClient
            .Builder()
            .withJavaNgrokConfig(new JavaNgrokConfig.Builder().withAuthToken(NGROK_TOKEN).build())
            .build();
        final CreateTunnel sshCreateTunnel = new CreateTunnel.Builder()
            .withProto(Proto.TCP)
            .withAddr(25565)
            .build();

        final Tunnel namedTunnel = ngrokClient.connect(sshCreateTunnel);
        logger.info("Openned tcp tunnel on address: '"
            + namedTunnel.getPublicUrl() + "'");

        return namedTunnel.getPublicUrl().replace("tcp://", "");
    }

}
