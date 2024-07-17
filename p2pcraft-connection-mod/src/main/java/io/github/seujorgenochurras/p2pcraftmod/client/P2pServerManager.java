package io.github.seujorgenochurras.p2pcraftmod.client;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.seujorgenochurras.p2pcraftmod.api.model.P2pServer;
import io.github.seujorgenochurras.p2pcraftmod.api.util.Terminal;
import net.minecraft.text.Text;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class P2pServerManager {

    private static final String RESOURCES_DIR = P2pCraftConnectModClient.getResourcesDirPath();
    private static final String P2PCRAFT_GITHUB_BOT_TOKEN = Dotenv.load().get("P2PCRAFT_GITHUB_BOT_TOKEN");
    private static Process serverProcess;
    private static Git git;

    private P2pServerManager() {
    }

    public static void startServer(P2pServer p2pServer, Consumer<Text> screenTextConsumer) throws IOException, GitAPIException, InterruptedException {
        P2pServer.P2pServerMap p2pServerMap = p2pServer.getMap();
        File mapDir = new File(RESOURCES_DIR + "/" + p2pServer.getName());

        if (mapDir.exists()) {
            git = Git.open(mapDir);
        } else {
            screenTextConsumer.accept(Text.translatable("connect.p2pcraftmod.clonning_git"));
            git = Git
                .cloneRepository()
                .setDirectory(mapDir)
                .setURI(p2pServerMap.getMapGithubURL())
                .call();
        }
        screenTextConsumer.accept(Text.translatable("connect.p2pcraftmod.fetching_git"));

        //TODO check for unsaved stuff and if there is do smth ;-;
        git.fetch().call();
        git.pull()
            .setRemote("origin")
            .setRemoteBranchName("main")
            .call();
        screenTextConsumer.accept(Text.translatable("connect.p2pcraftmod.starting_server_jar"));
        serverProcess = Terminal.execute(mapDir.getPath(), "java", "-Xmx2048M", "-jar", "server.jar", "nogui");


        //TODO properly wait for server to start
        int secondsInMillis = 1000;
        Thread.sleep(secondsInMillis * 14);

        Timer timer = new Timer("Server_Save_Timer");

        TimerTask saveServerTimerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    saveServer();
                } catch (GitAPIException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(saveServerTimerTask, 0, secondsInMillis * 600);
    }

    public static Process getServerProcess() {
        return serverProcess;
    }

    public static void saveServer() throws GitAPIException {
        git.add().addFilepattern(".")
            .setRenormalize(false)
            .call();

        String playerName = P2pCraftConnectModClient.getPlayerName();
        String message = playerName + " has saved the server data at " + OffsetDateTime.now();
        git.commit().setAuthor(playerName, playerName + "@player.com").setMessage(message).call();
        git.push()
            .setCredentialsProvider(new UsernamePasswordCredentialsProvider(P2PCRAFT_GITHUB_BOT_TOKEN, ""))
            .call();
    }


}
