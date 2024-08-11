package group.chon.ide.api.domain.model;

import com.jcraft.jsch.*;
import group.chon.ide.api.domain.file.FileUtils;
import group.chon.ide.api.domain.file.exception.ErrorSettingResourceInRemoteException;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class SSHExecutor implements Executor {

    private static final String FILE_REMOTE_SEPARATOR = "/";

    private static final String WINDOWS_PARTITION_SEPARATOR = ":";

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

    private static String getRemoteResourcePath(String resourcePath) {
        String path = resourcePath.replace(File.separator, FILE_REMOTE_SEPARATOR).substring(
                resourcePath.indexOf(WINDOWS_PARTITION_SEPARATOR) + 1);
        if (!String.valueOf(path.charAt(0)).equals(FILE_REMOTE_SEPARATOR)) {
            path = FILE_REMOTE_SEPARATOR + path;
        }
        return path;
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
     * Inicia a conexão SSH.
     *
     * @return {@code True} se foi possível estabelecer e {@code false} se não.
     */
    public boolean test() {
        Session session = this.connectSession();
        if (session == null) {
            return false;
        }
        if (session.isConnected()) {
            session.disconnect();
            return true;
        } else {
            return false;
        }
    }

    public String setResourceInRemote(File resource) {
        Session session = this.connectSession();
        try {
            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            String absoluteResourcePath = resource.getAbsolutePath();
            String remoteResourcePath = getRemoteResourcePath(absoluteResourcePath);
            if (FileUtils.isFile(absoluteResourcePath)) {
                sftp.put(resource.getAbsolutePath(), remoteResourcePath, ChannelSftp.OVERWRITE);
            } else {
                List<String> folders = Arrays.stream(remoteResourcePath.split(FILE_REMOTE_SEPARATOR)).filter(
                        folder -> !folder.isEmpty()).collect(Collectors.toList());
                String resultPath = FILE_REMOTE_SEPARATOR;
                for (int i = 0; i < folders.size(); i++) {
                    resultPath += folders.get(i) + FILE_REMOTE_SEPARATOR;
                    try {
                        sftp.cd(resultPath);
                    } catch (SftpException e) {
                        sftp.mkdir(resultPath);
                    }
                }
            }
            sftp.disconnect();
            return remoteResourcePath;
        } catch (JSchException | SftpException e) {
            throw new ErrorSettingResourceInRemoteException(resource.getName(), e);
        } finally {
            session.disconnect();
        }
    }

    private synchronized String executeInRemote(String command) {
        Session session = this.connectSession();
        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.setInputStream(null);
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];

            StringBuilder output = new StringBuilder();
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    output.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (channel.getExitStatus() != 0) {
                        return null;
                    }
                    break;
                }
            }
            channel.disconnect();
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
        }
        return null;
    }

    @Override
    public String execute(String command, boolean mantainLineBreak) {
        String output = this.executeInRemote(command);
        if (!mantainLineBreak && output != null) {
            return output.replace(FileUtils.BREAK_LINE, "");
        }
        return output;
    }
}
