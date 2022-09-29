package org.masos.embed.sysconfig.model;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.util.Properties;

public class SSHConnection {

    /** Host default para conexão SSH. */
    private static final String DEFAULT_HOST = "rpi06.bot.chon.group";

    /** Porta default para conexão SSH. */
    private static final int DEFAULT_PORT = 22;

    /** Sessão SSH. */
    Session session;

    /** Usuário do receiver. */
    private String username;

    /** Senha do receiver. */
    private String password;

    /** Host para conexão SSH. */
    private String hostname;

    /** Porta para conexão SSH. */
    private int port;

    /** Flag que indica se a sessão está ativa. */
    private boolean connected;

    public SSHConnection(String username, String password, String hostname, int port) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = port;

    }

    public static SSHConnection getDefault(User user) {
        return new SSHConnection(user.getUsername(), user.getPassword(), DEFAULT_HOST, DEFAULT_PORT);
    }

    /**
     * Estabelece uma sessão SSH para transmissão de dados.
     *
     * @return {@code True} se foi possível estabelecer a conexão e {@code false} se não.
     */
    public boolean connect() {
        try {
            this.session = new JSch().getSession(this.username, this.hostname, this.port);
            this.session.setPassword(this.password);
            Properties sessionConfig = new Properties();
            sessionConfig.put("StrictHostKeyChecking", "no");
            this.session.setConfig(sessionConfig);
            this.session.connect();
            this.connected = true;
        } catch (Exception e) {
            this.connected = false;
        }
        return this.connected;
    }

    /**
     * Testa a conexão SSH.
     *
     * @return {@code True} se foi possível estabelecer e {@code false} se não.
     */
    public boolean test() {
        if (this.connect() == true) {
            this.disconnect();
            return true;
        }
        return false;
    }

    /**
     * Finaliza a conexão SSH.
     */
    public void disconnect() {
        this.connected = false;
        this.session.disconnect();
    }

    /**
     * Executa um comando via sessão SSH.
     *
     * @param command Comando a ser executado.
     * @return Resposta do comando executado.
     */
    public String execute(String command) {
        if (!this.connected) {
            if (!this.connect()) {
                return null;
            }
        }

        try {
            Channel channel = this.session.openChannel("exec");
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
            this.disconnect();
            return output.replace("\n", "");
        } catch (Exception e) {
            this.disconnect();
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return {@link #username}
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username {@link #username}
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return {@link #password}
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password {@link #password}
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return {@link #hostname}
     */
    public String getHostname() {
        return this.hostname;
    }

    /**
     * @param hostname {@link #hostname}
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return {@link #port}
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @param port {@link #port}
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return {@link #connected}
     */
    public boolean isConnected() {
        return this.connected;
    }

    /**
     * @param connected {@link #connected}
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
