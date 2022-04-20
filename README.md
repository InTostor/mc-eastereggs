
# Easter eggs (secrets) counter #

This plugin will help players with collecting secrets
`<enter text>`

# Installing & usage #

## Requirements ##

Minecraft 1.13+ (this tested on 1.18.2 but using compatible api)
mysql server (in future there will be SQLITE, MSSQL, and other)
sense of adventure

## Commands ##

`/secc purgeplayer <player>` - deleting all eggs, found by `player`
`/secc addegg <displayname> <groupname> [message]` - adds egg, you are looking at with name `displayname` and relates to the `groupname`. Sending message `[message]`
(required for rewards and statistics)
`/secc delegg` - deletes an egg, you are looking at

examples

`/secc addegg Lenins-essay Communists very_large_book_with_idealogy`

## Warning (you really need to know this) ##

Because this plugin is my first try of java programming (before i knew py,lua,js)
there is some very weird solutions (for example: in messages and cmds you should replace all spaces with `_` symbol,  etc)
you should strictly follow the command layout, experiment ant try to work with this plugin in an unexpected way (whatever that means)

# Build yourself #

this is maven project, so you have to use maven to build this.
cmd: `mvn package -DskipTests`

### Note ###

Please, if you know how to improve this, contact me or make changes and create a pull request
