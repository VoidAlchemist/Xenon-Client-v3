# Xenon-Client-v3
A toy 1.8.9 PvP client for people to study

Compared to v2, the followings were added:

## Hypixel-related

- Hypixel Skyblock Dungeon QOL options (*dungeon helper* setting):
  - Auto secret chest: Automatically steals the content of a secret chest if it is either a talisman, a potion, a tripwire or an ender pearl, then automatically closes the chest. If the secret is garbage, automatically closes the chest without stealing the content.
  - Dungeon trivia puzzle solver for trivia (except for fairy souls questions) (blaze and livid solver doesn't work yet).
  - Dungeon three weirdo solver. Requires ESP being enabled. Once the correct answer has been detected, the weirdo which has the correct chest will be highlighted in green.
  - Dungeon F7 QOL features (*F7++* setting): Cancels wrong lever flips for light device, Cancels wrong item frame interaction for arrow device, solver for all terminals.
  - Dungeon iron sword right click duplication: Programmatically creates a right click out of thin air every N ticks when holding right click with an iron sword. Lifesaver for hype mages overall.
- Hide lightning: Useful for F7 boss 2nd phase
- Map overlay: Takes the first map in your hotbar, starting from the left, and draws it as overlay. Useful for stuff like hypixel skyblock dungeons.
- No sword slow: Voids mobility reduction when blocking with a sword. Useful for 1.8 hype mages to go as fast as 1.9 hype mages in dungeons.
- Murder Mystery Helper: When ESP is enabled, highlights the murderer in red and the players that have bows in blue.

## Other

- ESP: Exact 1.9 glowing effect brought in 1.8.9. Currently only available by pressing the corresponding key (in vanilla *controls* settings) for toggling it. Will interact with murder mystery helper and with hypixel skyblock dungeons (will highlight murderer and players with bows in murder mystery and will only highlights npc and bats while playing dungeons)
- Barrier block revealer
- Single player better fly: Makes creative fly not drift and hitting the ground upon sneaking not undo fly.
- Block placement delay: Reduces the delay between an action repetition when holding right click (e.g. when placing blocks or throwing snowballs).
