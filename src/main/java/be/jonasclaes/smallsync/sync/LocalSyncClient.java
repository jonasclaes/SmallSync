package be.jonasclaes.smallsync.sync;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalSyncClient extends SyncClient {

    public LocalSyncClient(String localFolder, String remoteFolder) {
        super(localFolder, remoteFolder);
    }

    @Override
    public void sync() {
        System.out.printf("Starting synchronization from \"%s\" to \"%s\".\r\n", super.getSourceFolder().getAbsolutePath(), super.getDestinationFolder().getAbsolutePath());
        try {
            // Get a list of source files and destination files
            List<File> sourceFiles = (List<File>) FileUtils.listFilesAndDirs(super.getSourceFolder(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            List<File> destinationFiles = (List<File>) FileUtils.listFilesAndDirs(super.getDestinationFolder(), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

            List<String> destinationFileNames = new ArrayList<String>();

            // Get the destination file paths of existing files
            for (File file : destinationFiles) {
                if (file.isFile()) {
                    destinationFileNames.add(file.getAbsolutePath().replace(super.getDestinationFolder().getAbsolutePath(), ""));
                }
            }

            for (File file : sourceFiles) {
                if (file.isFile()) {
                    // Get the source file path
                    String filePath = file.getAbsolutePath().replace(super.getSourceFolder().getAbsolutePath(), "");

                    // Check if file already exists
                    if (destinationFileNames.contains(filePath) && !super.isOverwrite()) {
                        System.out.printf("File \"%s\" already exists.\r\n", file.getName());
                    } else {
                        // Copy the file to the exact path in the destination directory
                        FileUtils.copyFile(file, new File(super.getDestinationFolder().getAbsolutePath() + filePath));

                        System.out.printf("Copying file \"%s\" to \"%s\"...\r\n", file.getName(), super.getDestinationFolder().getAbsolutePath() + filePath);
                    }
                }
            }

            System.out.println("Synchronization done.");
        } catch (IOException e) {
            System.out.println("Synchronization stopped with following error:");
            e.printStackTrace();
        }
    }
}
