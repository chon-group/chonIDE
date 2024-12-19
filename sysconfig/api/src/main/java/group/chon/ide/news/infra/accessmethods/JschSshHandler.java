package group.chon.ide.news.infra.accessmethods;

import com.jcraft.jsch.*;
import group.chon.ide.api.domain.file.FileUtils;
import group.chon.ide.news.app.accessmethods.SshHandler;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.Properties;

@RequiredArgsConstructor
public class JschSshHandler implements SshHandler {

    private static final String FILE_REMOTE_SEPARATOR = "/";

    private static final int DEFAULT_PORT = 22;

    private final String username;

    private final String password;

    private final String hostname;

    private Session session;

    @Override
    public boolean test() {
        this.connect();
        boolean isConnected = this.isConnect();
        if (isConnected) {
            this.disconnect();
        }
        return isConnected;
    }

    @Override
    public void connect() {
        try {
            Session session = new JSch().getSession(this.username, this.hostname, DEFAULT_PORT);
            session.setPassword(this.password);

            Properties sessionConfig = new Properties();
            sessionConfig.put("StrictHostKeyChecking", "no");

            session.setConfig(sessionConfig);
            session.connect();

            this.session = session;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnect() {
        return this.session.isConnected();
    }

    @Override
    public void disconnect() {
        this.session.disconnect();
    }


    @Override
    public boolean sftp_put(InputStream inputStream, String targetFile) {
        try {
            ChannelSftp sftp = (ChannelSftp) this.session.openChannel("sftp");
            sftp.connect();

            boolean hasToCreateSubdirectories = false;
            try {
                sftp.cd(targetFile.substring(0, targetFile.lastIndexOf(FILE_REMOTE_SEPARATOR)));
            } catch (SftpException e) {
                hasToCreateSubdirectories = true;
            }

            if (hasToCreateSubdirectories) {
                StringBuilder path = new StringBuilder();
                for (String folder : targetFile.split(FILE_REMOTE_SEPARATOR)) {
                    path.append(folder).append(FILE_REMOTE_SEPARATOR);
                    try {
                        sftp.cd(path.toString());
                    } catch (SftpException e) {
                        try {
                            sftp.mkdir(path.toString());
                        } catch (SftpException e2) {
                            return false;
                        }
                    }
                }
            }

            sftp.put(inputStream, targetFile, ChannelSftp.OVERWRITE);

            sftp.disconnect();
            return true;
        } catch (JSchException | SftpException ignored) {
            return false;
        } finally {
            this.session.disconnect();
        }
    }

    @Override
    public InputStream sftp_get(String targetFile) {
        try {
            ChannelSftp sftp = (ChannelSftp) this.session.openChannel("sftp");
            sftp.connect();

            InputStream inputStream = sftp.get(targetFile);
            sftp.disconnect();
            return inputStream;
        } catch (JSchException | SftpException ignored) {
            return null;
        } finally {
            this.session.disconnect();
        }
    }

    @Override
    public boolean sftp_rm(String target) {
        try {
            ChannelSftp sftp = (ChannelSftp) this.session.openChannel("sftp");
            sftp.connect();

            if (FileUtils.isFile(target)) {
                sftp.rm(target);
            } else {
                sftp.rmdir(target);
            }

            sftp.disconnect();
            return true;
        } catch (JSchException | SftpException ignored) {
            return false;
        } finally {
            this.session.disconnect();
        }
    }

    @Override
    public String execute(String command) {
        try {
            ChannelExec channel = (ChannelExec) this.session.openChannel("exec");
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
            return null;
        } finally {
            this.session.disconnect();
        }
    }
}
