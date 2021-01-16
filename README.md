# BlunixTeleport
## Minecraft Paper Spigot Plugin for Teleportation


This minecraft plugin provides teleportation functionality using the `/tp` command. It is designed to be lightweight, do one thing and do it right as well as making it difficult for players to abuse the functionality.


# Functionality

This plugins functionality includes:

- `/tp help` Print the help text
- `/tp toa` Print Terms Of Agreement for teleportation operations
- `/tp bed` Teleport to bed or respawn anchor
- `/tp to <player>` Teleport to a player (will ask other player for permission if issued by non-admin)
- `/tp accept <player>` Accept another players teleportation request
- `/tp deny <player>` Deny another players teleportation request
- `/tp poi` Display all available Points Of Interest
- `/tp poi <poi>` Teleport to the given Point Of Interest
- `/tp setpoi <name>` Adds the current location to the list of Points Of Interest
` `/tp delpoi <name>` Deletes the given Point Of Interest from the list
- `/tp gps X Y Z <dimension>` Teleports to the given coordinates in the given dimension (current dimension if not specified)
- `/tp wild` Teleport to a random location in the current dimension
- `/tp cooldown` Display the remaining cooldown time until the next teleportation action can be performed
- `/tp generatelocations` Generate a new database of `/tp wild` locations for all dimensions
- `/tp reload` Reload the plugin


# Configuration

The documented configuration file can be found here: https://github.com/minecraft-anarchy/BlunixTeleport/blob/master/BlunixTeleport/src/config.yml


# Permissions

- `blunixteleport.request` - Allows `/tp to` command
- `blunixteleport.respond` - Allows `/tp accept` and `/tp deny` commands
- `blunixteleport.toa` - Allows `/tp toa` commands
- `blunixteleport.poi` - Allows `/tp poi` commands
- `blunixteleport.setpoi` - Allows `/tp setpoi` command
- `blunixteleport.delpoi` - Allows `/tp delpoi` command
- `blunixteleport.bed` - Allows `/tp bed` command
- `blunixteleport.wild` - Allows `/tp wild` command
- `blunixteleport.gps` - Allows `/tp gps` command
- `blunixteleport.cooldown` - Allows `/tp cooldown` command
- `blunixteleport.reload` - Allows `/tp reload` command
- `blunixteleport.generatelocations` - Allows `/tp generatelocations` command
- `blunixteleport.admin` - Allows admin features in some commands


# Author Information

Written by: https://github.com/Wmorales01  
Sponsored by: Blunix GmbH - Consulting for Linux Hosting 24/7

Blunix GmbH provides 24/7 support with Service Level Agreements for Debian Linux based hosting environments which are automated with Ansible.

```
Blunix GmbH
Glogauer Straße 21
10999 Berlin
Germany

Website: https://www.blunix.com
E-Mail:  contact@blunix.com
gpg key: https://www.blunix.com/contact-blunix-com-gpg-key
Signal:  +49 30 629 318 76
```

# License
Apache-2.0

Please refer to the LICENSE file in the root of this repository.
