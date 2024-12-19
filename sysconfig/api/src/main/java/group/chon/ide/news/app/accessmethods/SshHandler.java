package group.chon.ide.news.app.accessmethods;

import java.io.InputStream;

public interface SshHandler extends CommandPromptRunner {

    boolean test();

    void connect();

    boolean isConnect();

    void disconnect();

    boolean sftp_put(InputStream inputStream, String targetFile);

    InputStream sftp_get(String targetFile);

    boolean sftp_rm(String target);

}
