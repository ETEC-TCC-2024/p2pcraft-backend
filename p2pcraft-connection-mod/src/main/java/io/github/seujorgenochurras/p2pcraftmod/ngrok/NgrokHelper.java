package io.github.seujorgenochurras.p2pcraftmod.ngrok;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Proto;
import com.github.alexdlaird.ngrok.protocol.Tunnel;

import java.util.logging.Logger;

public class NgrokHelper {

    private static final Logger logger = Logger.getLogger(NgrokHelper.class.getName());

    public static String openTunnel() {
        final NgrokClient ngrokClient = new NgrokClient.Builder().build();
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
