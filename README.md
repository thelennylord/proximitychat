# Proximity Chat

Makes chat in Minecraft proximity based.

## Features:

### Global Chat
Chat with everyone using the global chat.<br>
Command: `/globalchat <message>`<br>
Requires permission: `proximitychat.globalchat`<br>

### Global Chat Cooldown
A cooldown feature has been implemented for globalchat. Users can only use globalchat after their cooldown has expired. Default cooldown time is 60 seconds. Cooldown can be disabled by setting its value to 0.<br>
Permission to bypass cooldown: `proximitychat.bypassCooldown`

## Default Config
```
# Radius needed for the user to chat with others
radius:
  x: 100
  y: 100
  z: 100

# Global chat cooldown (in seconds)
global-chat-cooldown: 60

# Show proximity chat prefix. Visible when user is talking in proximity mode.
# Example:
# [PROXIMITY] <Steve> Hello world!
show-proximity-prefix: false
```
