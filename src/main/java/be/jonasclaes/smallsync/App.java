package be.jonasclaes.smallsync;

import be.jonasclaes.smallsync.sync.FTPSyncClient;
import be.jonasclaes.smallsync.sync.LocalSyncClient;
import be.jonasclaes.smallsync.sync.SyncException;
import org.apache.commons.cli.*;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws SyncException, IOException {
        String syncType = null;
        String sourceDir = null;
        String destDir = null;
        String ftpHost = null;
        int ftpPort = 0;
        String ftpUser = null;
        String ftpPassword = null;

        System.out.println("#### ThinSync ####");

        Options options = new Options();

        Option syncTypeOption = new Option("type", "syncType", true, "sync type (local/ftp)");
        syncTypeOption.setRequired(true);
        options.addOption(syncTypeOption);

        Option sourceDirectoryOption = new Option("src", "source", true, "source directory");
        sourceDirectoryOption.setRequired(true);
        options.addOption(sourceDirectoryOption);

        Option destinationDirectoryOption = new Option("dest", "destination", true, "destination directory");
        destinationDirectoryOption.setRequired(true);
        options.addOption(destinationDirectoryOption);

        Option ftpHostOption = new Option("fhost", "ftphost", true, "ftp hostname");
        ftpHostOption.setRequired(false);
        options.addOption(ftpHostOption);

        Option ftpPortOption = new Option("fport", "ftpport", true, "ftp port");
        ftpPortOption.setRequired(false);
        options.addOption(ftpPortOption);

        Option ftpUsernameOption = new Option("fuser", "ftpusername", true, "ftp username");
        ftpUsernameOption.setRequired(false);
        options.addOption(ftpUsernameOption);

        Option ftpPasswordOption = new Option("fpass", "ftppassword", true, "ftp password");
        ftpPasswordOption.setRequired(false);
        options.addOption(ftpPasswordOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            syncType = cmd.getOptionValue("syncType");
            sourceDir = cmd.getOptionValue("source");
            destDir = cmd.getOptionValue("destination");

            if (syncType.equals("local")) {
                LocalSyncClient localSyncClient = new LocalSyncClient(sourceDir, destDir);
                localSyncClient.sync();
            } else if (syncType.equals("ftp")) {
                ftpHost = cmd.getOptionValue("ftphost");
                ftpPort = Integer.parseInt(cmd.getOptionValue("ftpport"));
                ftpUser = cmd.getOptionValue("ftpusername");
                ftpPassword = cmd.getOptionValue("ftppassword");

                FTPSyncClient ftpSyncClient =  new FTPSyncClient(sourceDir, destDir, ftpHost, ftpPort, ftpUser, ftpPassword);

                ftpSyncClient.open();
                ftpSyncClient.sync();
                ftpSyncClient.close();
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("smallsync", options);

            System.exit(1);
        }
    }
}
