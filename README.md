# SmallSync

It's a small and simple file sync application.
You can use it to sync locally between folders or over the network to an FTP server.
You can find the latest releases by clicking [here](https://github.com/jonasclaes/SmallSync/releases).

# Basic usage

* Bring up the help: `java -jar SmallSync.jar`
* Sync locally: `java -jar SmallSync.jar -type local -src "full-source-path" -dest "full-destination-path"`
* Sync to FTP: `java -jar SmallSync.jar -type ftp -src "full-source-path" -dest "full-destination-path" -fhost "ftp-hostname" -fport ftp-port -fuser "ftp-username" -fpass "ftp-password"`

# Progress
- [x] Syncing to local folders
- [x] Syncing to FTP servers
- [ ] Syncing FTP servers to local folders
