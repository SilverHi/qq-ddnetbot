create table ddrtools.ddr_mapinfo
(
    id         bigint auto_increment
        primary key,
    mapname    varchar(200)  null,
    website    varchar(2000) null,
    thumbnail  varchar(2000) null,
    webpreview varchar(2000) null,
    maptype    varchar(200)  null,
    points     int           null,
    difficulty int           null,
    mapper     varchar(2000) null,
    maprelease varchar(200)  null,
    width      int           null,
    height     int           null,
    goodnum    int           null,
    badnum     int           null
)
    engine = InnoDB;

create table ddrtools.sys_cmd
(
    id         bigint auto_increment
        primary key,
    cmd_name   varchar(200) null,
    class_name varchar(500) null
)
    engine = InnoDB;

create table ddrtools.sys_enums
(
    id    bigint auto_increment
        primary key,
    type  varchar(200) null,
    name  varchar(200) null,
    value varchar(200) null
)
    engine = InnoDB;

create table ddrtools.sys_setting
(
    id    bigint auto_increment
        primary key,
    type  varchar(1000) null,
    name  varchar(1000) null,
    value varchar(5000) null,
    bief  varchar(1000) null
)
    engine = InnoDB;

为了测试 请你告诉我怎么写一个按下鼠标左键实现 分身和本体同时锤击 的bind    控制分身是否锤击的变量 在如下的可用参数文档中，请你自行查找判断：! Command
! Arguments
! Description
|-
| echo
| r[text]
| Echo the text
|-
| exec
| r[file]
| Execute the specified file
|-
| reset
| s[config-name]
| Reset a config to its default value
|-
| toggle
| s[config-option] i[value 1] i[value 2]
| Toggle config value
|-
| access_level
| s[command] ?i[accesslevel]
| Specify command accessibility (admin = 0, moderator = 1, helper = 2, all = 3)
|-
| access_status
| i[accesslevel]
| List all commands which are accessible for admin = 0, moderator = 1, helper = 2, all = 3
|-
| cmdlist
|
| List all commands which are accessible for users
|-
| ban
| s[ip|id] ?i[minutes] r[reason]
| Ban ip for x minutes for any reason
|-
| ban_range
| s[first ip] s[last ip] ?i[minutes] r[reason]
| Ban ip range for x minutes for any reason
|-
| unban
| s[ip|entry]
| Unban ip/banlist entry
|-
| unban_range
| s[first ip] s[last ip]
| Unban ip range
|-
| unban_all
|
| Unban all entries
|-
| bans
| ?i[page]
| Show banlist (page 0 by default, 20 entries per page)
|-
| bans_save
| s[file]
| Save banlist in a file
|-
| ban
| s[ip|id] ?i[minutes] r[reason]
| Ban player with ip/client id for x minutes for any reason
|-
| ban_region
| s[region] s[ip|id] ?i[minutes] r[reason]
| Ban player in a region
|-
| ban_region_range
| s[region] s[first ip] s[last ip] ?i[minutes] r[reason]
| Ban range in a region
|-
| kick
| i[id] ?r[reason]
| Kick player with specified id for any reason
|-
| status
| ?r[name]
| List players containing name or all players
|-
| shutdown
| ?r[reason]
| Shut down
|-
| logout
|
| Logout of rcon
|-
| show_ips
| ?i[show]
| Show IP addresses in rcon commands (1 = on, 0 = off)
|-
| record
| ?s[file]
| Record to a file
|-
| stoprecord
|
| Stop recording
|-
| reload
|
| Reload the map
|-
| add_sqlserver
| s[&#x27;r&#x27;|&#x27;w&#x27;] s[Database] s[Prefix] s[User] s[Password] s[IP] i[Port] ?i[SetUpDatabase ?]
| add a sqlserver
|-
| dump_sqlservers
| s[&#x27;r&#x27;|&#x27;w&#x27;]
| dumps all sqlservers readservers = r, writeservers = w
|-
| auth_add
| s[ident] s[level] r[pw]
| Add a rcon key
|-
| auth_add_p
| s[ident] s[level] s[hash] s[salt]
| Add a prehashed rcon key
|-
| auth_change
| s[ident] s[level] r[pw]
| Update a rcon key
                                                                                                                                                                                                             |-
                                                                                                                                                                                                             | auth_change_p
                                                                                                                                                                                                             | s[ident] s[level] s[hash] s[salt]
                                                                                                                                                                                                             | Update a rcon key with prehashed data
    |-
    | auth_remove
    | s[ident]
    | Remove a rcon key
    |-
    | auth_list
    |
    | List all rcon keys
    |-
    | name_ban
    | s[name] ?i[distance] ?i[is_substring] ?r[reason]
    | Ban a certain nickname
    |-
    | name_unban
    | s[name]
    | Unban a certain nickname
    |-
    | name_bans
    |
    | List all name bans
    |-
    | tune
    | s[tuning] ?i[value]
    | Tune variable to value or show current value
    |-
    | toggle_tune
    | s[tuning] i[value 1] i[value 2]
    | Toggle tune variable
    |-
    | tune_reset
    | ?s[tuning]
    | Reset all or one tuning variable to default
    |-
    | tunes
    |
    | List all tuning variables and their values
                                                                                                                                                                                                                   |-
                                                                                                                                                                                                                   | tune_zone
                                                                                                                                                                                                                   | i[zone] s[tuning] i[value]
                                                                                                                                                                                                                   | Tune in zone a variable to value
                                                                                                                                                                                                                   |-
                                                                                                                                                                                                                   | tune_zone_dump
                                                                                                                                                                                                                   | i[zone]
                                                                                                                                                                                                                   | Dump zone tuning in zone x
                                                                                                                                                                                                                   |-
                                                                                                                                                                                                                   | tune_zone_reset
                                                                                                                                                                                                                   | ?i[zone]
                                                                                                                                                                                                                   | reset zone tuning in zone x or in all zones
                                                                                                                                                                                                                   |-
                                                                                                                                                                                                                   | tune_zone_enter
                                                                                                                                                                                                                   | i[zone] r[message]
                                                                                                                                                                                                                   | which message to display on zone enter; use 0 for normal area
|-
| tune_zone_leave
| i[zone] r[message]
| which message to display on zone leave; use 0 for normal area
|-
| mapbug
| s[mapbug]
| Enable map compatibility mode using the specified bug (example: grenade-doubleexplosion@ddnet.tw)
|-
| switch_open
| i[switch]
| Whether a switch is deactivated by default (otherwise activated)
|-
| pause_game
|
| Pause/unpause game
|-
| change_map
| ?r[map]
| Change map
|-
| random_map
| ?i[stars]
| Random map
|-
| random_unfinished_map
| ?i[stars]
| Random unfinished map
|-
| restart
| ?i[seconds]
| Restart in x seconds (0 = abort)
|-
| broadcast
| r[message]
| Broadcast message
|-
| say
| r[message]
| Say in chat
|-
| set_team
| i[id] i[team-id] ?i[delay in minutes]
| Set team of player to team
|-
| set_team_all
| i[team-id]
| Set team of all players to team
|-
| add_vote
| s[name] r[command]
| Add a voting option
|-
| remove_vote
| r[name]
| remove a voting option
|-
| force_vote
| s[name] s[command] ?r[reason]
| Force a voting option
|-
| clear_votes
|
| Clears the voting options
|-
| add_map_votes
|
| Automatically adds voting options for all maps
|-
| vote
| r[&#x27;yes&#x27;|&#x27;no&#x27;]
| Force a vote to yes/no
|-
| dump_antibot
|
| Dumps the antibot status
|-
| dbg_lognetwork
|
| Log the network
|-
| kill_pl
| v[id]
| Kills player v and announces the kill
|-
| totele
| i[number]
| Teleports you to teleporter v
|-
| totelecp
| i[number]
| Teleports you to checkpoint teleporter v
|-
| tele
| ?i[id] ?i[id]
| Teleports player i (or you) to player i (or you to where you look at)
|-
| addweapon
| i[weapon-id]
| Gives weapon with id i to you (all = -1, hammer = 0, gun = 1, shotgun = 2, grenade = 3, laser = 4, ninja = 5)
|-
| removeweapon
| i[weapon-id]
| removes weapon with id i from you (all = -1, hammer = 0, gun = 1, shotgun = 2, grenade = 3, laser = 4, ninja = 5)
|-
| shotgun
|
| Gives a shotgun to you
|-
| grenade
|
| Gives a grenade launcher to you
|-
| laser
|
| Gives a laser to you
|-
| rifle
|
| Gives a laser to you
|-
| jetpack
|
| Gives jetpack to you
|-
| weapons
|
| Gives all weapons to you
|-
| unshotgun
|
| Removes the shotgun from you
|-
| ungrenade
|
| Removes the grenade launcher from you
|-
| unlaser
|
| Removes the laser from you
|-
| unrifle
|
| Removes the laser from you
|-
| unjetpack
|
| Removes the jetpack from you
|-
| unweapons
|
| Removes all weapons from you
|-
| ninja
|
| Makes you a ninja
|-
| super
|
| Makes you super
|-
| unsuper
|
| Removes super from you
|-
| endless_hook
|
| Gives you endless hook
|-
| unendless_hook
|
| Removes endless hook from you
|-
| unsolo
|
| Puts you out of solo part
|-
| undeep
|
| Puts you out of deep freeze
|-
| livefreeze
|
| Makes you live frozen
|-
| unlivefreeze
|
| Puts you out of live freeze
|-
| left
|
| Makes you move 1 tile left
|-
| right
|
| Makes you move 1 tile right
|-
| up
|
| Makes you move 1 tile up
|-
| down
|
| Makes you move 1 tile down
|-
| move
| i[x] i[y]
| Moves to the tile with x/y-number ii
|-
| move_raw
| i[x] i[y]
| Moves to the point with x/y-coordinates ii
|-
| force_pause
| v[id] i[seconds]
| Force i to pause for i seconds
|-
| force_unpause
| v[id]
| Set force-pause timer of i to 0.
|-
| set_team_ddr
| v[id] i[team]
| Set ddrace team of a player
|-
| uninvite
| v[id] i[team]
| Uninvite player from team
|-
| vote_mute
| v[id] i[seconds] ?r[reason]
| Remove v&#x27;s right to vote for i seconds
|-
| vote_unmute
| v[id]
| Give back v&#x27;s right to vote.
|-
| vote_mutes
|
| List the current active vote mutes.
|-
| mute
|
|
|-
| muteid
| v[id] i[seconds] ?r[reason]
| Mute player with id
|-
| muteip
| s[ip] i[seconds] ?r[reason]
| Mute player with IP address
|-
| unmute
| i[muteid]
|
|-
| unmuteid
| v[id]
| Unmute player with id
|-
| mutes
|
|
|-
| moderate
|
| Enables/disables active moderator mode for the player
|-
| vote_no
|
|
|-
| save_dry
|
| Dump the current savestring
|-
| dump_log
| ?i[seconds]
| Show logs of the last i seconds
|-
| freezehammer
| v[id]
| Gives a player Freeze Hammer
|-
| unfreezehammer
| v[id]
| Removes Freeze Hammer from a player
|}! Setting
! Description
! Default
! Min
! Max
|-
| cl_predict
| Predict client movements
| 1
| 0
| 1
|-
| cl_predict_dummy
| Predict dummy movements
| 1
| 0
| 1
|-
| cl_antiping_limit
| Antiping limit (0 to disable)
| 0
| 0
| 200
|-
| cl_antiping
| Enable antiping, i. e. more aggressive prediction.
| 0
| 0
| 1
|-
| cl_antiping_players
| Predict other player&#x27;s movement more aggressively (only enabled if cl_antiping is set to 1)
| 1
| 0
| 1
|-
| cl_antiping_grenade
| Predict grenades (only enabled if cl_antiping is set to 1)
| 1
| 0
| 1
|-
| cl_antiping_weapons
| Predict weapon projectiles (only enabled if cl_antiping is set to 1)
| 1
| 0
| 1
|-
| cl_antiping_smooth
| Make the prediction of other player&#x27;s movement smoother
| 0
| 0
| 1
|-
| cl_antiping_gunfire
| Predict gunfire and show predicted weapon physics (with cl_antiping_grenade 1 and cl_antiping_weapons 1)
| 1
| 0
| 1
|-
| cl_prediction_margin
| Prediction margin in ms (adds latency, can reduce lag from ping jumps)
| 10
| 1
| 2000
|-
| cl_nameplates
| Show name plates
| 1
| 0
| 1
|-
| cl_afk_emote
| Show zzz emote next to afk players
| 1
| 0
| 1
|-
| cl_nameplates_always
| Always show name plates disregarding of distance
| 1
| 0
| 1
|-
| cl_nameplates_teamcolors
| Use team colors for name plates
| 1
| 0
| 1
|-
| cl_nameplates_size
| Size of the name plates from 0 to 100%
| 50
| 0
| 100
|-
| cl_nameplates_clan
| Show clan in name plates
| 0
| 0
| 1
|-
| cl_nameplates_clan_size
| Size of the clan plates from 0 to 100%
| 30
| 0
| 100
|-
| cl_nameplates_ids
| Show IDs in name plates
| 0
| 0
| 1
|-
| cl_nameplates_own
| Show own name plate (useful for demo recording)
| 0
| 0
| 1
|-
| cl_nameplates_friendmark
| Show friend mark (♥) in name plates
| 0
| 0
| 1
|-
| cl_nameplates_strong
| Show strong/weak in name plates (0 - off, 1 - icons, 2 - icons + numbers)
| 0
| 0
| 2
|-
| cl_text_entities
| Render textual entity data
| 1
| 0
| 1
|-
| cl_text_entities_size
| Size of textual entity data from 1 to 100%
| 100
| 1
| 100
|-
| cl_authed_player_color
| Color of name of authenticated player in scoreboard
| 5898211
|
|
|-
| cl_same_clan_color
| Clan color of players with the same clan as you in scoreboard.
| 5898211
|
|
|-
| cl_enable_ping_color
| Whether ping is colored in scoreboard.
| 1
| 0
| 1
|-
| cl_autoswitch_weapons
| Auto switch weapon on pickup
| 1
| 0
| 1
|-
| cl_autoswitch_weapons_out_of_ammo
| Auto switch weapon when out of ammo
| 0
| 0
| 1
|-
| cl_showhud
| Show ingame HUD
| 1
| 0
| 1
|-
| cl_showhud_healthammo
| Show ingame HUD (Health + Ammo)
| 1
| 0
| 1
|-
| cl_showhud_score
| Show ingame HUD (Score)
| 1
| 0
| 1
|-
| cl_showhud_timer
| Show ingame HUD (Timer)
| 1
| 0
| 1
|-
| cl_showhud_dummy_actions
| Show ingame HUD (Dummy Actions)
| 1
| 0
| 1
|-
| cl_showhud_player_position
| Show ingame HUD (Player Position)
| 0
| 0
| 1
|-
| cl_showhud_player_speed
| Show ingame HUD (Player Speed)
| 0
| 0
| 1
|-
| cl_showhud_player_angle
| Show ingame HUD (Player Aim Angle)
| 0
| 0
| 1
|-
| cl_showhud_ddrace
| Show ingame HUD (DDRace HUD)
| 1
| 0
| 1
|-
| cl_showhud_jumps_indicator
| Show ingame HUD (Jumps you have and have used)
| 1
| 0
| 1
|-
| cl_show_freeze_bars
| Whether to show a freeze bar under frozen players to indicate the thaw time
| 1
| 0
| 1
|-
| cl_freezebars_alpha_inside_freeze
| Opacity of freeze bars inside freeze (0 invisible, 100 fully visible)
| 0
| 0
| 100
|-
| cl_showrecord
| Show old style DDRace client records
| 0
| 0
| 1
|-
| cl_shownotifications
| Make the client notify when someone highlights you
| 1
| 0
| 1
|-
| cl_showemotes
| Show tee emotes
| 1
| 0
| 1
|-
| cl_showchat
| Show chat (2 to always show large chat area)
| 1
| 0
| 2
|-
| cl_show_chat_friends
| Show only chat messages from friends
| 0
| 0
| 1
|-
| cl_show_chat_system
| Show chat messages from the server
| 1
| 0
| 1
|-
| cl_showkillmessages
| Show kill messages
| 1
| 0
| 1
|-
| cl_show_votes_after_voting
| Show votes window after voting
| 0
| 0
| 1
|-
| cl_show_local_time_always
| Always show local time
| 0
| 0
| 1
|-
| cl_showfps
| Show ingame FPS counter
| 0
| 0
| 1
|-
| cl_showpred
| Show ingame prediction time in milliseconds
| 0
| 0
| 1
|-
| cl_eye_wheel
| Show eye wheel along together with emotes
| 1
| 0
| 1
|-
| cl_eye_duration
| How long the eyes emotes last
| 999999
| 1
| 999999
|-
| cl_airjumpindicator
|
| 1
| 0
| 1
|-
| cl_threadsoundloading
| Load sound files threaded
| 0
| 0
| 1
|-
| cl_warning_teambalance
| Warn about team balance
| 1
| 0
| 1
|-
| cl_mouse_deadzone
| Deadzone for the camera to follow the cursor
| 0
| 0
| 3000
|-
| cl_mouse_followfactor
| Factor for the camera to follow the cursor
| 0
| 0
| 200
|-
| cl_mouse_max_distance
| Maximum cursor distance
| 400
| 0
| 5000
|-
| cl_mouse_min_distance
| Minimum cursor distance
| 0
| 0
| 5000
|-
| cl_dyncam
| Enable dyncam
| 0
| 0
| 1
|-
| cl_dyncam_max_distance
| Maximum dynamic camera cursor distance
| 1000
| 0
| 2000
|-
| cl_dyncam_min_distance
| Minimum dynamic camera cursor distance
| 0
| 0
| 2000
|-
| cl_dyncam_mousesens
| Mouse sens used when dyncam is toggled on
| 0
| 0
| 100000
|-
| cl_dyncam_deadzone
| Deadzone for the dynamic camera to follow the cursor
| 300
| 1
| 1300
|-
| cl_dyncam_follow_factor
| Factor for the dynamic camera to follow the cursor
| 60
| 0
| 200
|-
| cl_dyncam_smoothness
| Transition amount of the camera movement, 0=instant, 100=slow and smooth
| 0
| 0
| 100
|-
| cl_dyncam_stabilizing
| Amount of camera slowdown during fast cursor movement. High value can cause delay in camera movement
| 0
| 0
| 100
|-
| ed_smooth_zoom_time
| Time of smooth zoom animation in the editor in ms (0 for off)
| 250
| 0
| 5000
|-
| ed_limit_max_zoom_level
| Specifies, if zooming in the editor should be limited or not (0 = no limit)
| 1
| 0
| 1
|-
| ed_zoom_target
| Zoom to the current mouse target
| 0
| 0
| 1
|-
| ed_showkeys
|
| 0
| 0
| 1
|-
| cl_show_welcome
|
| 1
| 0
| 1
|-
| cl_motd_time
| How long to show the server message of the day
| 10
| 0
| 100
|-
| cl_map_download_url
| URL used to download maps (can start with http:// or https://)
| &quot;https://maps.ddnet.org&quot;
|
|
|-
| cl_map_download_connect_timeout_ms
| HTTP map downloads: timeout for the connect phase in milliseconds (0 to disable)
| 2000
| 0
| 100000
|-
| cl_map_download_low_speed_limit
| HTTP map downloads: Set low speed limit in bytes per second (0 to disable)
| 4000
| 0
| 100000
|-
| cl_map_download_low_speed_time
| HTTP map downloads: Set low speed limit time period (0 to disable)
| 3
| 0
| 100000
|-
| cl_languagefile
| What language file to use
| &quot;&quot;
|
|
|-
| cl_skin_download_url
| URL used to download skins
| &quot;https://skins.ddnet.org/skin/&quot;
|
|
|-
| cl_skin_community_download_url
| URL used to download community skins
| &quot;https://skins.ddnet.org/skin/community/&quot;
|
|
|-
| cl_vanilla_skins_only
| Only show skins available in Vanilla Teeworlds
| 0
| 0
| 1
|-
| cl_download_skins
| Download skins from cl_skin_download_url on-the-fly
| 1
| 0
| 1
|-
| cl_download_community_skins
| Allow to download skins created by the community. Uses cl_skin_community_download_url instead of cl_skin_download_url for the download
| 0
| 0
| 1
|-
| cl_auto_statboard_screenshot
| Automatically take game over statboard screenshot
| 0
| 0
| 1
|-
| cl_auto_statboard_screenshot_max
| Maximum number of automatically created statboard screenshots (0 = no limit)
| 10
| 0
| 1000
|-
| cl_default_zoom
| Default zoom level
| 10
| 0
| 20
|-
| cl_smooth_zoom_time
| Time of smooth zoom animation ingame in ms (0 for off)
| 250
| 0
| 5000
|-
| cl_limit_max_zoom_level
| Specifies, if zooming ingame should be limited or not (0 = no limit)
| 1
| 0
| 1
|-
| player_use_custom_color
| Toggles usage of custom colors
| 0
| 0
| 1
|-
| player_color_body
| Player body color
| 65408
|
|
|-
| player_color_feet
| Player feet color
| 65408
|
|
|-
| player_skin
| Player skin
| &quot;default&quot;
|
|
|-
| player_default_eyes
| Player eyes when joining server. 0 = normal, 1 = pain, 2 = happy, 3 = surprise, 4 = angry, 5 = blink
| 0
| 0
| 5
|-
| cl_skin_prefix
| Replace the skins by skins with this prefix (e.g. kitty, santa)
| &quot;&quot;
|
|
|-
| cl_fat_skins
| Enable fat skins
| 0
| 0
| 1
|-
| ui_page
| Interface page
| 9
| 6
| 10
|-
| ui_settings_page
| Interface settings page
| 0
| 0
| 9
|-
| ui_toolbox_page
| Toolbox page
| 0
| 0
| 2
|-
| ui_server_address
| Interface server address
| &quot;localhost:8303&quot;
|
|
|-
| ui_mousesens
| Mouse sensitivity for menus/editor
| 200
| 1
| 100000
|-
| ui_controller_sens
| Controller sensitivity for menus/editor
| 100
| 1
| 100000
|-
| ui_smooth_scroll_time
| Time of smooth scrolling animation in menus/editor in ms (0 for off)
| 500
| 0
| 5000
|-
| ui_color
| Interface color
| 0xE4A046AF
|
|
|-
| ui_colorize_ping
| Highlight ping
| 1
| 0
| 1
|-
| ui_colorize_gametype
| Highlight gametype
| 1
| 0
| 1
|-
| ui_demo_selected
| Selected demo file
| &quot;&quot;
|
|
|-
| ui_close_window_after_changing_setting
| Close window after changing setting
| 1
| 0
| 1
|-
| ui_unread_news
| Whether there is unread news
| 0
| 0
| 1
|-
| gfx_noclip
| Disable clipping
| 0
| 0
| 1
|-
| dummy_name
| Name of the dummy
| &quot;&quot;
|
|
|-
| dummy_clan
| Clan of the dummy
| &quot;&quot;
|
|
|-
| dummy_country
| Country of the Dummy
| -1
| -1
| 1000
|-
| dummy_use_custom_color
| Toggles usage of custom colors
| 0
| 0
| 1
|-
| dummy_color_body
| Dummy body color
| 65408
|
|
|-
| dummy_color_feet
| Dummy feet color
| 65408
|
|
|-
| dummy_skin
| Dummy skin
| &quot;default&quot;
|
|
|-
| dummy_default_eyes
| Dummy eyes when joining server. 0 = normal, 1 = pain, 2 = happy, 3 = surprise, 4 = angry, 5 = blink
| 0
| 0
| 5
|-
| cl_dummy
| 0 - player / 1 - dummy
| 0
| 0
| 1
|-
| cl_dummy_hammer
| Whether dummy is hammering for a hammerfly
| 0
| 0
| 1
|-
| cl_dummy_resetonswitch
| Whether dummy or player should stop pressing keys when you switch. 0 = off, 1 = dummy, 2 = player
| 0
| 0
| 2
|-
| cl_dummy_restore_weapon
| Whether dummy should switch to last weapon after hammerfly
| 1
| 0
| 1
|-
| cl_dummy_copy_moves
| Whether dummy should copy your moves
| 0
| 0
| 1
|-
| cl_dummy_control
| Whether can you control dummy at the same time (cl_dummy_jump, cl_dummy_fire, cl_dummy_hook)
| 0
| 0
| 1
|-
| cl_dummy_jump
| Whether dummy is jumping (requires cl_dummy_control 1)
| 0
| 0
| 1
|-
| cl_dummy_fire
| Whether dummy is firing (requires cl_dummy_control 1)
| 0
| 0
| 1
|-
| cl_dummy_hook
| Whether dummy is hooking (requires cl_dummy_control 1)
| 0
| 0
| 1
|-
| cl_show_start_menu_images
| Show start menu images
| 1
| 0
| 1
|-
| cl_skip_start_menu
| Skip the start menu
| 0
| 0
| 1
|-
| cl_video_pausewithdemo
| Pause video rendering when demo playing pause
| 1
| 0
| 1
|-
| cl_video_showhud
| Show ingame HUD when rendering video
| 0
| 0
| 1
|-
| cl_video_showchat
| Show chat when rendering video
| 1
| 0
| 1
|-
| cl_video_sound_enable
| Use sound when rendering video
| 1
| 0
| 1
|-
| cl_video_show_hook_coll_other
| Show other players&#x27; hook collision lines when rendering video
| 0
| 0
| 1
|-
| cl_video_show_direction
| Show players&#x27; key presses when rendering video (1 = other players&#x27;, 2 = also your own)
| 0
| 0
| 2
|-
| cl_video_crf
| Set crf when encode video with libx264 (0 for highest quality, 51 for lowest)
| 18
| 0
| 51
|-
| cl_video_preset
| Set preset when encode video with libx264, default is 5 (medium), 0 is ultrafast, 9 is placebo (the slowest, not recommend)
| 5
| 0
| 9
|-
| dbg_focus
|
| 0
| 0
| 1
|-
| dbg_tuning
| Display information about the tuning parameters that affect the own player
| 0
| 0
| 1
|-
| player_name
| Name of the player
| &quot;&quot;
|
|
|-
| player_clan
| Clan of the player
| &quot;&quot;
|
|
|-
| player_country
| Country of the player
| -1
| -1
| 1000
|-
| password
| Password to the server
| &quot;&quot;
|
|
|-
| logfile
| Filename to log all output to
| &quot;&quot;
|
|
|-
| loglevel
| Log level (0 = Error, 1 = Warn, 2 = Info, 3 = Debug, 4 = Trace)
| 2
| 0
| 4
|-
| console_output_level
| Adjusts the amount of information in the console
| 0
| 0
| 2
|-
| console_enable_colors
| Enable colors in console output
| 1
| 0
| 1
|-
| events
| Enable triggering of events, (eye emotes on some holidays in server, christmas skins in client).
| 1
| 0
| 1
|-
| steam_name
| Last seen name of the Steam profile
| &quot;&quot;
|
|
|-
| cl_save_settings
| Write the settings file on exit
| 1
| 0
| 1
|-
| cl_refresh_rate
| Refresh rate for updating the game (in Hz)
| 0
| 0
| 10000
|-
| cl_refresh_rate_inactive
| Refresh rate for updating the game when the window is inactive (in Hz)
| 120
| 0
| 10000
|-
| cl_editor
|
| 0
| 0
| 1
|-
| cl_editor_dilate
| Automatically dilates embedded images
| 1
| 0
| 1
|-
| cl_skin_filter_string
| Skin filtering string
| &quot;&quot;
|
|
|-
| cl_auto_demo_record
| Automatically record demos
| 1
| 0
| 1
|-
| cl_auto_demo_on_connect
| Only start a new demo when connect while automatically record demos
| 0
| 0
| 1
|-
| cl_auto_demo_max
| Maximum number of automatically recorded demos (0 = no limit)
| 10
| 0
| 1000
|-
| cl_auto_screenshot
| Automatically take game over screenshot
| 0
| 0
| 1
|-
| cl_auto_screenshot_max
| Maximum number of automatically created screenshots (0 = no limit)
| 10
| 0
| 1000
|-
| cl_auto_csv
| Automatically create game over csv
| 0
| 0
| 1
|-
| cl_auto_csv_max
| Maximum number of automatically created csvs (0 = no limit)
| 10
| 0
| 1000
|-
| cl_show_broadcasts
| Show broadcasts ingame
| 1
| 0
| 1
|-
| cl_print_broadcasts
| Print broadcasts to console
| 1
| 0
| 1
|-
| cl_print_motd
| Print motd to console
| 1
| 0
| 1
|-
| cl_friends_ignore_clan
| Ignore clan tag when searching for friends
| 1
| 0
| 1
|-
| cl_assets_entities
| The asset/assets for entities
| &quot;default&quot;
|
|
|-
| cl_asset_game
| The asset for game
| &quot;default&quot;
|
|
|-
| cl_asset_emoticons
| The asset for emoticons
| &quot;default&quot;
|
|
|-
| cl_asset_particles
| The asset for particles
| &quot;default&quot;
|
|
|-
| cl_asset_hud
| The asset for HUD
| &quot;default&quot;
|
|
|-
| cl_asset_extras
| The asset for the game graphics that do not come from Teeworlds
| &quot;default&quot;
|
|
|-
| br_filter_string
| Server browser filtering string
| &quot;Novice&quot;
|
|
|-
| br_exclude_string
| Server browser exclusion string
| &quot;&quot;
|
|
|-
| br_filter_full
| Filter out full server in browser
| 0
| 0
| 1
|-
| br_filter_empty
| Filter out empty server in browser
| 0
| 0
| 1
|-
| br_filter_spectators
| Filter out spectators from player numbers
| 0
| 0
| 1
|-
| br_filter_friends
| Filter out servers with no friends
| 0
| 0
| 1
|-
| br_filter_country
| Filter out servers with non-matching player country
| 0
| 0
| 1
|-
| br_filter_country_index
| Player country to filter by in the server browser
| -1
| -1
| 999
|-
| br_filter_pw
| Filter out password protected servers in browser
| 0
| 0
| 1
|-
| br_filter_gametype
| Game types to filter
| &quot;&quot;
|
|
|-
| br_filter_gametype_strict
| Strict gametype filter
| 0
| 0
| 1
|-
| br_filter_connecting_players
| Filter connecting players
| 1
| 0
| 1
|-
| br_filter_serveraddress
| Server address to filter
| &quot;&quot;
|
|
|-
| br_filter_unfinished_map
| Show only servers with unfinished maps
| 0
| 0
| 1
|-
| br_filter_exclude_countries
| Filter out DDNet servers by country
| &quot;&quot;
|
|
|-
| br_filter_exclude_types
| Filter out DDNet servers by type (mod)
| &quot;&quot;
|
|
|-
| br_indicate_finished
| Show whether you have finished a DDNet map (transmits your player name to info.ddnet.org/info)
| 1
| 0
| 1
|-
| br_location
| Override location for ping estimation, available: auto, af, as, as:cn, eu, na, oc, sa (Automatic, Africa, Asia, China, Europe, North America, Oceania/Australia, South America
| &quot;auto&quot;
|
|
|-
| br_cached_best_serverinfo_url
| Do not set this variable, instead create a ddnet-serverlist-urls.cfg next to settings_ddnet.cfg to specify all possible serverlist URLs
| &quot;&quot;
|
|
|-
| br_filter_exclude_countries_kog
| Filter out kog servers by country
| &quot;&quot;
|
|
|-
| br_filter_exclude_types_kog
| Filter out kog servers by type (mod)
| &quot;&quot;
|
|
|-
| br_sort
| Sorting column in server browser
| 4
| 0
| 256
|-
| br_sort_order
| Sorting order in server browser
| 2
| 0
| 2
|-
| br_max_requests
| Number of concurrent requests to use when refreshing server browser
| 100
| 0
| 1000
|-
| br_demo_sort
| Sorting column in demo browser
| 0
| 0
| 3
|-
| br_demo_sort_order
| Sorting order in demo browser
| 0
| 0
| 1
|-
| br_demo_fetch_info
| Whether to auto fetch demo infos on refresh
| 0
| 0
| 1
|-
| snd_buffer_size
| Sound buffer size
| 512
| 128
| 32768
|-
| snd_rate
| Sound mixing rate
| 48000
| 0
| 0
|-
| snd_enable
| Sound enable
| 1
| 0
| 1
|-
| snd_enable_music
| Play background music
| 0
| 0
| 1
|-
| snd_volume
| Sound volume
| 30
| 0
| 100
|-
| snd_device
| (deprecated) Sound device to use
| -1
| 0
| 0
|-
| snd_chat_volume
| Chat sound volume
| 30
| 0
| 100
|-
| snd_game_volume
| Game sound volume
| 30
| 0
| 100
|-
| snd_ambient_volume
| Map Sound sound volume
| 30
| 0
| 100
|-
| snd_background_music_volume
| Background music sound volume
| 30
| 0
| 100
|-
| snd_nonactive_mute
|
| 0
| 0
| 1
|-
| snd_game
| Enable game sounds
| 1
| 0
| 1
|-
| snd_gun
| Enable gun sound
| 1
| 0
| 1
|-
| snd_long_pain
| Enable long pain sound (used when shooting in freeze)
| 1
| 0
| 1
|-
| snd_chat
| Enable regular chat sound
| 1
| 0
| 1
|-
| snd_team_chat
| Enable team chat sound
| 1
| 0
| 1
|-
| snd_servermessage
| Enable server message sound
| 1
| 0
| 1
|-
| snd_highlight
| Enable highlighted chat sound
| 1
| 0
| 1
|-
| gfx_screen
| Screen index
| 0
| 0
| 15
|-
| gfx_screen_width
| Screen resolution width
| 0
| 0
| 0
|-
| gfx_screen_height
| Screen resolution height
| 0
| 0
| 0
|-
| gfx_screen_refresh_rate
| Screen refresh rate
| 0
| 0
| 0
|-
| gfx_desktop_width
| Desktop resolution width for detecting display changes (not recommended to change manually)
| 0
| 0
| 0
|-
| gfx_desktop_height
| Desktop resolution height for detecting display changes (not recommended to change manually)
| 0
| 0
| 0
|-
| gfx_borderless
| Borderless window (not to be used with fullscreen)
| 1
| 0
| 1
|-
| gfx_fullscreen
| Set fullscreen mode: 0=no fullscreen, 1=pure fullscreen, 2=desktop fullscreen, 3=windowed fullscreen
| 0
| 0
| 3
|-
| gfx_highdpi
| Enable high-dpi
| 1
| 0
| 1
|-
| gfx_color_depth
| Colors bits for framebuffer (fullscreen only)
| 24
| 16
| 24
|-
| gfx_vsync
| Vertical sync (may cause delay)
| 0
| 0
| 1
|-
| gfx_display_all_video_modes
| Show all video modes
| 0
| 0
| 1
|-
| gfx_high_detail
| High detail
| 1
| 0
| 1
|-
| gfx_fsaa_samples
| FSAA Samples
| 0
| 0
| 64
|-
| gfx_refresh_rate
| Screen refresh rate
| 0
| 0
| 10000
|-
| gfx_finish
|
| 0
| 0
| 1
|-
| gfx_backgroundrender
| Render graphics when window is in background
| 1
| 0
| 1
|-
| gfx_text_overlay
| Stop rendering textoverlay in editor or with entities: high value = less details = more speed
| 10
| 1
| 100
|-
| gfx_asyncrender_old
| Do rendering async from the the update
| 1
| 0
| 1
|-
| gfx_tune_overlay
| Stop rendering text overlay in tuning zone in editor: high value = less details = more speed
| 20
| 1
| 100
|-
| gfx_quad_as_triangle
| Render quads as triangles (fixes quad coloring on some GPUs)
| 0
| 0
| 1
|-
| inp_mousesens
| Mouse sensitivity
| 200
| 1
| 100000
|-
| inp_mouseold
| Use old mouse mode (warp mouse instead of raw input)
| 0
| 0
| 1
|-
| inp_translated_keys
| Translate keys before interpreting them, respects keyboard layouts
| 0
| 0
| 1
|-
| inp_ignored_modifiers
| Ignored keyboard modifier mask
| 0
| 0
| 65536
|-
| inp_controller_enable
| Enable controller
| 0
| 0
| 1
|-
| inp_controller_guid
| Controller GUID which uniquely identifies the active controller
| &quot;&quot;
|
|
|-
| inp_controller_absolute
| Enable absolute controller aiming ingame
| 0
| 0
| 1
|-
| inp_controller_sens
| Ingame controller sensitivity
| 100
| 1
| 100000
|-
| inp_controller_x
| Controller axis that controls X axis of cursor
| 0
| 0
| 12
|-
| inp_controller_y
| Controller axis that controls Y axis of cursor
| 1
| 0
| 12
|-
| inp_controller_tolerance
| Controller axis tolerance to account for jitter
| 5
| 0
| 50
|-
| cl_port
| Port to use for client connections to server (0 to choose a random port, 1024 or higher to set a manual port, requires a restart)
| 0
| 0
| 65535
|-
| cl_dummy_port
| Port to use for dummy connections to server (0 to choose a random port, 1024 or higher to set a manual port, requires a restart)
| 0
| 0
| 65535
|-
| cl_contact_port
| Port to use for serverinfo connections to server (0 to choose a random port, 1024 or higher to set a manual port, requires a restart)
| 0
| 0
| 65535
|-
| bindaddr
| Address to bind the client/server to
| &quot;&quot;
|
|
|-
| debug
| Debug mode
| 0
| 0
| 1
|-
| dbg_curl
| Debug curl
| 0
| 0
| 1
|-
| dbg_graphs
| Performance graphs
| 0
| 0
| 1
|-
| dbg_gfx
| Show graphic library warnings and errors, if the GPU supports it (0: none, 1: minimal, 2: affects performance, 3: verbose, 4: all)
| 0
| 0
| 4
|-
| dbg_stress
| Stress systems (Debug build only)
| 0
| 0
| 0
|-
| dbg_stress_network
| Stress network (Debug build only)
| 0
| 0
| 0
|-
| dbg_stress_server
| Server to stress (Debug build only)
| &quot;localhost&quot;
|
|
|-
| http_allow_insecure
| Allow insecure HTTP protocol in addition to the secure HTTPS one. Mostly useful for testing.
| 0
| 0
| 1
|-
| cl_race_binds_set
| What level the DDRace binds are set to (this is automated, you don&#x27;t need to use this)
| 0
| 0
| 1
|-
| cl_reconnect_timeout
| How many seconds to wait before reconnecting (after timeout, 0 for off)
| 120
| 0
| 600
|-
| cl_reconnect_full
| How many seconds to wait before reconnecting (when server is full, 0 for off)
| 5
| 0
| 600
|-
| cl_message_system_color
| System message color
| 2817983
|
|
|-
| cl_message_client_color
| Client message color
| 9633471
|
|
|-
| cl_message_highlight_color
| Highlighted message color
| 65471
|
|
|-
| cl_message_team_color
| Team message color
| 5636050
|
|
|-
| cl_message_color
| Message color
| 255
|
|
|-
| cl_laser_rifle_inner_color
| Laser inner color for Rifle
| 11206591
|
|
|-
| cl_laser_rifle_outline_color
| Laser outline color for Rifle
| 11176233
|
|
|-
| cl_laser_sg_inner_color
| Laser inner color for Shotgun
| 1467241
|
|
|-
| cl_laser_sg_outline_color
| Laser outline color for Shotgun
| 1866773
|
|
|-
| cl_laser_door_inner_color
| Laser inner color for doors
| 7701379
|
|
|-
| cl_laser_door_outline_color
| Laser outline color for doors
| 7667473
|
|
|-
| cl_laser_freeze_inner_color
| Laser inner color for freezes
| 12001153
|
|
|-
| cl_laser_freeze_outline_color
| Laser outline color for freezes
| 11613223
|
|
|-
| cl_kill_message_normal_color
| Kill message normal color
| 255
|
|
|-
| cl_kill_message_highlight_color
| Kill message highlight color
| 255
|
|
|-
| cl_message_friend
| Enable coloring and the heart for friends
| 1
| 0
| 1
|-
| cl_message_friend_color
| Friend message color
| 65425
|
|
|-
| conn_timeout
| Network timeout
| 100
| 5
| 1000
|-
| cl_show_ids
| Whether to show client ids in scoreboard
| 0
| 0
| 1
|-
| cl_scoreboard_on_death
| Whether to show scoreboard after death or not
| 1
| 0
| 1
|-
| cl_auto_race_record
| Save the best demo of each race
| 1
| 0
| 1
|-
| cl_replays
| Enable/disable replays
| 0
| 0
| 1
|-
| cl_replay_length
| Set the default length of the replays
| 30
| 10
| 0
|-
| cl_race_record_server_control
| Let the server start the race recorder
| 1
| 0
| 1
|-
| cl_demo_name
| Save the player name within the demo
| 1
| 0
| 1
|-
| cl_demo_assume_race
| Assume that demos are race demos
| 1
| 0
| 1
|-
| cl_race_ghost
| Enable ghost
| 1
| 0
| 1
|-
| cl_race_ghost_server_control
| Let the server start the ghost
| 1
| 0
| 1
|-
| cl_race_show_ghost
| Show ghost
| 1
| 0
| 1
|-
| cl_race_save_ghost
| Save ghost
| 1
| 0
| 1
|-
| cl_ddrace_scoreboard
| Enable DDRace Scoreboard
| 1
| 0
| 1
|-
| cl_show_others
| Show players in other teams (2 to show own team only)
| 0
| 0
| 2
|-
| cl_show_others_alpha
| Show players in other teams (alpha value, 0 invisible, 100 fully visible)
| 40
| 0
| 100
|-
| cl_overlay_entities
| Overlay game tiles with a percentage of opacity
| 0
| 0
| 100
|-
| cl_showquads
| Show quads (only interesting for mappers, or if your system has extremely bad performance)
| 1
| 0
| 1
|-
| cl_background_color
| Background color
| 128
|
|
|-
| cl_background_entities_color
| Background (entities) color
| 128
|
|
|-
| cl_background_entities
| Background (entities)
| &quot;&quot;
|
|
|-
| cl_run_on_join
| Command to run when joining a server
| &quot;&quot;
|
|
|-
| cl_menu_map
| Background map in the menu
| &quot;auto&quot;
|
|
|-
| cl_rotation_radius
| Menu camera rotation radius
| 30
| 1
| 500
|-
| cl_rotation_speed
| Menu camera rotations in seconds
| 40
| 1
| 120
|-
| cl_camera_speed
| Menu camera speed
| 5
| 1
| 40
|-
| cl_background_show_tiles_layers
| Whether draw tiles layers when using custom background (entities)
| 0
| 0
| 1
|-
| cl_unpredicted_shadow
| Show unpredicted shadow tee (0 = off, 1 = on, -1 = don&#x27;t even show in debug mode)
| 0
| -1
| 1
|-
| cl_predict_freeze
| Predict freeze tiles (0 = off, 1 = on, 2 = partial (allow a small amount of movement in freeze)
| 1
| 0
| 2
|-
| cl_show_ninja
| Show ninja skin
| 1
| 0
| 1
|-
| cl_show_hook_coll_other
| Show other players&#x27; hook collision line (2 to always show)
| 1
| 0
| 2
|-
| cl_show_hook_coll_own
| Show own players&#x27; hook collision line (2 to always show)
| 1
| 0
| 2
|-
| cl_hook_coll_size
| Size of hook collision line
| 0
| 0
| 20
|-
| cl_hook_coll_alpha
| Alpha of hook collision line (0 invisible, 100 fully visible)
| 100
| 0
| 100
|-
| cl_hook_coll_color_no_coll
| Specifies the color of a hookline that hits nothing.
| 65407
|
|
|-
| cl_hook_coll_color_hookable_coll
| Specifies the color of a hookline that hits hookable tiles.
| 6401973
|
|
|-
| cl_hook_coll_color_tee_coll
| Specifies the color of a hookline that hits tees.
| 2817919
|
|
|-
| cl_chat_teamcolors
| Show names in chat in team colors
| 0
| 0
| 1
|-
| cl_chat_reset
| Reset chat when pressing escape
| 1
| 0
| 1
|-
| cl_chat_old
| Old chat style: No tee, no background
| 0
| 0
| 1
|-
| cl_show_direction
| Show key presses (1 = other players&#x27;, 2 = also your own)
| 1
| 0
| 2
|-
| cl_old_gun_position
| Tees hold gun a bit higher like in TW 0.6.1 and older
| 0
| 0
| 1
|-
| cl_confirm_disconnect_time
| Confirmation popup before disconnecting after game time (in minutes, -1 to turn off, 0 to always turn on)
| 20
| -1
| 1440
|-
| cl_confirm_quit_time
| Confirmation popup before quitting after game time (in minutes, -1 to turn off, 0 to always turn on)
| 20
| -1
| 1440
|-
| cl_timeout_code
| Timeout code to use
| &quot;&quot;
|
|
|-
| cl_dummy_timeout_code
| Dummy Timeout code to use
| &quot;&quot;
|
|
|-
| cl_timeout_seed
| Timeout seed
| &quot;&quot;
|
|
|-
| cl_input_fifo
| Fifo file (non-Windows) or Named Pipe (Windows) to use as input for client console
| &quot;&quot;
|
|
|-
| cl_config_version
| The config version. Helps newer clients fix bugs with older configs.
| 0
| 0
| 0
|-
| cl_demo_slice_begin
| Begin marker for demo slice
| -1
| 0
| 0
|-
| cl_demo_slice_end
| End marker for demo slice
| -1
| 0
| 0
|-
| cl_demo_show_speed
| Show speed meter on change
| 0
| 0
| 1
|-
| cl_demo_keyboard_shortcuts
| Enable keyboard shortcuts in demo player
| 1
| 0
| 1
|-
| gfx_gl_major
| Graphic library major version
| 1
| 1
| 10
|-
| gfx_gl_minor
| Graphic library minor version
| 3
| 0
| 10
|-
| gfx_gl_patch
| Graphic library patch version
| 0
| 0
| 10
|-
| gfx_gl_texture_lod_bias
| The lod bias for graphic library texture sampling multiplied by 1000
| -500
| -15000
| 15000
|-
| gfx_3d_texture_analysis_ran
| Ran an analyzer to check if sampling 3D/2D array textures works correctly
| 0
| 0
| 1
|-
| gfx_3d_texture_analysis_renderer
| The renderer on which the analysis was performed
| &quot;&quot;
|
|
|-
| gfx_3d_texture_analysis_version
| The version on which the analysis was performed
| &quot;&quot;
|
|
|-
| gfx_gpu_name
| The GPU&#x27;s name, which will be selected by the backend. (if supported by the backend)
| &quot;auto&quot;
|
|
|-
| gfx_backend
| The backend to use (e.g. OpenGL or Vulkan)
| &quot;OpenGL&quot;
|
|
|-
| gfx_render_thread_count
| Number of threads the backend can use for rendering. (note: the value can be ignored by the backend)
| 3
| 0
| 0
|-
| gfx_driver_is_blocked
| If 1, the current driver is in a blocked error state.
| 0
| 0
| 1
|-
| cl_video_recorder_fps
| At which FPS the videorecorder should record demos.
| 60
| 1
| 1000
|}