package group.chon.ide.news.app.service.chonos.firmware;

import group.chon.ide.news.app.model.Board;
import group.chon.ide.news.infra.repository.LocalFileRepository;
import group.chon.ide.news.app.accessmethods.SshHandler;

import java.io.InputStream;

public class RemoteChonosFirmwareService extends ChonosFirmwareService {

    private static final String LIBRARY_PREFIX_PATH = "/tmp/lib_";

    private static final String SKETCH_BUILD_FILE = "/tmp/sketch.ino";

    private final SshHandler sshHandler;

    public RemoteChonosFirmwareService(SshHandler sshHandler) {
        super(sshHandler);
        this.sshHandler = sshHandler;
    }

    @Override
    public void compileSketch(String sketchFilePath, Board board) {
        LocalFileRepository localFileRepository = new LocalFileRepository();
        boolean fileExists = localFileRepository.exists(sketchFilePath);
        if (fileExists) {
            InputStream inputStream = localFileRepository.readInputStream(sketchFilePath);

            this.sshHandler.sftp_put(inputStream, SKETCH_BUILD_FILE);

            super.compileSketch(SKETCH_BUILD_FILE, board);
        }
    }

    @Override
    public void importLibrary(String libraryFilePath) {
        LocalFileRepository localFileRepository = new LocalFileRepository();
        boolean fileExists = localFileRepository.exists(libraryFilePath);
        if (fileExists) {
            InputStream inputStream = localFileRepository.readInputStream(libraryFilePath);
            String libraryName = libraryFilePath.substring(libraryFilePath.lastIndexOf("/"));

            String targetFile = LIBRARY_PREFIX_PATH + libraryName;
            this.sshHandler.sftp_put(inputStream, targetFile);

            super.importLibrary(targetFile);
        }
    }
}
