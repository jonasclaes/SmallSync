package be.jonasclaes.smallsync.sync;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class FTPSyncClient extends SyncClient {
    private String server;
    private int port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    public FTPSyncClient(String localFolder, String remoteFolder, String server, int port, String user, String password) {
        super(localFolder, remoteFolder);
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void open() throws IOException, SyncException {
        ftpClient = new FTPClient();

        // Uncomment this to enable full view of FTP command output
//        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftpClient.connect(server, port);
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new SyncException("Exception in connecting to FTP server");
        }

        ftpClient.login(user, password);

        // Enter passive mode because most of us are behind firewalls these days.
        ftpClient.enterLocalPassiveMode();

        ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
    }

    public void close() throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
    }

    public void downloadFile(String source, String destination) throws IOException {
        FileOutputStream out = new FileOutputStream(destination);
        ftpClient.retrieveFile(source, out);
    }

    public void uploadFile(String source, String destination) throws IOException {
        File sourceFile = new File(source);
        InputStream inputStream = new FileInputStream(sourceFile);
        ftpClient.storeFile(destination, inputStream);
        inputStream.close();
    }

    public void makeDirectory(String path) throws IOException {
        ftpClient.makeDirectory(path);
    }

    public void uploadDirectory(String sourcePath, String destinationPath, String destinationParent) throws IOException {
        File sourceDir = new File(sourcePath);
        File[] subFiles = sourceDir.listFiles();

        if (subFiles != null && subFiles.length > 0) {
            for (File file : subFiles) {
                String destinationFilePath = destinationPath + "/" + destinationParent + "/" + file.getName();

                if (destinationParent.equals("")) {
                    destinationFilePath = destinationPath + "/" + file.getName();
                }

                if (file.isFile()) {
                    // Upload the file
                    System.out.printf("Uploading file %s...\r\n", file.getAbsolutePath());
                    String sourceFilePath = file.getAbsolutePath();
                    uploadFile(sourceFilePath, destinationFilePath);
                } else {
                    // Create directory on the server
                    makeDirectory(destinationFilePath);

                    String parent = destinationParent + "/" + file.getName();

                    if (destinationParent.equals("")) {
                        parent = file.getName();
                    }

                    sourcePath = file.getAbsolutePath();

                    // Upload the sub directory
                    uploadDirectory(sourcePath, destinationPath, parent);
                }
            }
        }
    }

    @Override
    public void sync() {
        System.out.printf("Starting synchronization from \"%s\" to \"%s\".\r\n", super.getSourceFolder().getAbsolutePath(), "ftp://" + user + "@" + server + ":" + port + super.getDestinationFolder().getAbsolutePath());
        try {
            uploadDirectory(super.getSourceFolder().getAbsolutePath(), super.getDestinationFolder().getAbsolutePath(), "");

            System.out.println("Synchronization done.");
        } catch (IOException e) {
            System.out.println("Synchronization stopped with following error:");
            e.printStackTrace();
        }
    }
}
