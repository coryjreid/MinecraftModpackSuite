# Minecraft Modpack Suite

This is a suite of tools which is used to help publish and distribute modpacks for private servers. It is made up of 
multiple components which work in tandem to accomplish the goal of publishing and distributing private modpacks.

## Components

### SyncClient

A simple application which runs on client computers to connect to a rsync server and pull down the served files. It
requires a simple config file to specify where to place the synced files/folders and the server connection information.
From there it should launch as a "prelaunch command" on the MultiMC instance which is managing that Minecraft
installation.
