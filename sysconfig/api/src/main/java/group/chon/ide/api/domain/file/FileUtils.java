package group.chon.ide.api.domain.file;

import group.chon.ide.api.domain.file.exception.*;
import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.delete;

public class FileUtils {

    public static final String BREAK_LINE = "\n";

    public static final String COMPACTED_FILE_EXTENSION = ".zip";

    private static final int FILE_MAX_ATTEMPT_TO_CREATE = 3;

    private static final int FOLDER_MAX_ATTEMPT_TO_CREATE = 3;

    private static final int FILE_MAX_ATTEMPT_TO_DELETE = 3;

    public static void deleteAll(Path directory) {
        try {
            List<Path> directoryPaths = Files.list(directory).collect(Collectors.toList());
            for (Path directoryPath : directoryPaths) {
                if (Files.isDirectory(directoryPath)) {
                    deleteAll(directoryPath);
                } else {
                    delete(directoryPath);
                }
            }
            delete(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean createFolder(File folder) {
        int createFolderAttempt = 0;

        // Verifica se a pasta já existe.
        if (folder.exists()) {
            return true;
        }

        while (createFolderAttempt < FOLDER_MAX_ATTEMPT_TO_CREATE) {
            if (folder.mkdirs()) {
                return true;
            }
            createFolderAttempt++;
        }
        return false;
    }

    public static List<File> getAllFilesFromDirectory(File directory) {
        List<File> allFiles = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                allFiles.addAll(getAllFilesFromDirectory(file));
            } else {
                allFiles.add(file);
            }
        }
        return allFiles;
    }

    public static boolean createFile(File file, InputStream inputStream) {
        int createFileAttempt = 0;

        // Verifica se o arquivo já existe.
        if (file.exists()) {
            if (!file.delete()) {
                throw new ErrorDeletingFileException(file.getName());
            }
        }

        while (createFileAttempt < FILE_MAX_ATTEMPT_TO_CREATE) {
            try {
                if (file.createNewFile()) {
                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    return true;
                }
            } catch (IOException e) {
                throw new ErrorCreatingFileException(file.getName(), e);
            }
            createFileAttempt++;
        }
        return false;
    }

    public static boolean createFile(File file) {
        int createFileAttempt = 0;

        // Verifica se o arquivo já existe.
        if (file.exists()) {
            if (!file.delete()) {
                throw new ErrorDeletingFileException(file.getName());
            }
        }

        while (createFileAttempt < FILE_MAX_ATTEMPT_TO_CREATE) {
            try {
                if (file.createNewFile()) {
                    return true;
                }
            } catch (IOException e) {
                throw new ErrorCreatingFileException(file.getName(), e);
            }
            createFileAttempt++;
        }
        return false;
    }

    public static void write(Path path, String content) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean writeFileContent(File file, String content) {
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        try {
            fileWriter = new FileWriter(file, false);
            printWriter = new PrintWriter(fileWriter);
            printWriter.print(content);
            printWriter.flush();
            return true;
        } catch (IOException e) {
            throw new ErrorWritingFileContentException(file.getName(), e);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new ErrorClosingFileWriterException(file.getName(), e);
                }
            }
        }
    }

    public static boolean unzipFiles(File zipFile, String directoryToExtract) {
        try {
            ZipFile zippedFile = new ZipFile(zipFile);
            zippedFile.extractAll(directoryToExtract);
            zippedFile.close();
            return true;
        } catch (IOException e) {
            throw new ErrorZippingFileException(zipFile.getName(), e);
        }
    }

    public static File zipFilesByFolder(File folder) {
        try {
            Files.deleteIfExists(Paths.get(folder.getAbsolutePath() + COMPACTED_FILE_EXTENSION));
            ZipFile zipFile = new ZipFile(folder.getAbsoluteFile() + COMPACTED_FILE_EXTENSION);
            File[] files = folder.listFiles();
            if (files.length == 0) {
                throw new ErrorZippingFileException(folder.getName(), new Exception("Pasta vazia"));
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    zipFile.addFolder(file);
                } else {
                    zipFile.addFile(file);
                }
            }
            zipFile.close();
            return zipFile.getFile();
        } catch (IOException e) {
            throw new ErrorZippingFileException(folder.getName(), e);
        }
    }

    public static boolean deleteAllFilesExcept(String baseFolder, List<String> filesExcept) {
        boolean filesWasDeleted = true;
        File baseFolderFile = new File(baseFolder);
        if (baseFolderFile.exists()) {
            if (baseFolderFile.isDirectory()) {
                File[] files = baseFolderFile.listFiles();
                if (files != null && files.length > 1) {
                    for (File file : files) {
                        if (!filesExcept.contains(file.getName())) {
                            int attempt = 0;
                            while (attempt < FILE_MAX_ATTEMPT_TO_DELETE && file.exists()) {
                                deleteFile(file);
                                attempt++;
                            }
                            if (file.exists()) {
                                filesWasDeleted = false;
                            }
                        }
                    }
                }
            }
        }
        return filesWasDeleted;
    }

    public static boolean isFile(String path) {
        return path.lastIndexOf(".") > path.lastIndexOf(File.separator);
    }

    public static boolean deleteFolder(File folder) {
        File[] folderFiles = folder.listFiles();
        if (folderFiles != null && folderFiles.length >= 1) {
            for (File folderFile : folderFiles) {
                if (folderFile.isDirectory()) {
                    deleteFolder(folderFile);
                } else {
                    folderFile.delete();
                }
            }
        }
        folderFiles = folder.listFiles();
        if (folderFiles.length < 1) {
            folder.delete();
        }
        return !folder.exists();
    }

    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            return deleteFolder(file);
        } else {
            return file.delete();
        }
    }

    public static File getFirstFolderInside(File folder) {
        if (folder.exists() && folder.isDirectory() && folder.listFiles() != null && folder.listFiles().length > 1) {
            if (folder.isDirectory()) {
                for (File fileInsideFolder : folder.listFiles()) {
                    if (fileInsideFolder.isDirectory()) {
                        return fileInsideFolder;
                    }
                }

            }
        }
        return null;
    }

    public static File getFirstFileInside(File folder) {
        if (folder.exists() && folder.isDirectory() && folder.listFiles() != null && folder.listFiles().length > 1) {
            if (folder.isDirectory()) {
                for (File fileInsideFolder : folder.listFiles()) {
                    if (fileInsideFolder.isFile()) {
                        return fileInsideFolder;
                    }
                }
            }
        }
        return null;
    }

    public static String readFileContent(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append(BREAK_LINE);
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            throw new ErrorReadingFileException(file.getName(), e);
        } finally {
            Exception e = null;
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ioException) {
                    e = ioException;
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ioException) {
                    e = ioException;
                }
            }
            if (e != null) {
                throw new ErrorClosingFileReaderException(file.getName(), e);
            }
        }

        return stringBuilder.toString();
    }

}
