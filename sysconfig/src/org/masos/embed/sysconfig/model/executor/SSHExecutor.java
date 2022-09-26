package org.masos.embed.sysconfig.model.executor;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.masos.embed.sysconfig.model.User;

import java.io.InputStream;
import java.util.Properties;

public class SSHExecutor implements Executor {

    /** Porta default para conexão SSH. */
    private static final int DEFAULT_PORT = 22;

    /** Usuário do receiver. */
    private String username;

    /** Senha do receiver. */
    private String password;

    /** Host para conexão SSH. */
    private String hostname;

    /** Porta para conexão SSH. */
    private int port;

    public SSHExecutor(String username, String password, String hostname) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = DEFAULT_PORT;
    }

    /**
     * Estabelece uma sessão SSH para transmissão de dados.
     *
     * @return Sessão SSH estabelecida.
     */
    public Session connectSession() {
        try {
            Session session = new JSch().getSession(this.username, this.hostname, this.port);
            session.setPassword(this.password);
            Properties sessionConfig = new Properties();
            sessionConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sessionConfig);
            session.connect();
            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Testa a conexão SSH.
     *
     * @return {@code True} se foi possível estabelecer e {@code false} se não.
     */
    public boolean test() {
        Session session = this.connectSession();
        if (session != null) {
            session.disconnect();
            return true;
        }
        return false;
    }

    @Override
    public String execute(String command) {
        Session session = this.connectSession();
        if (session == null) {
            return null;
        }

        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];

            String output = "";
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    output = output + (new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (channel.getExitStatus() != 0) {
                        return null;
                    }
                    break;
                }
            }

            channel.disconnect();
            return output.replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
        }

        return null;
    }
}
