# 💎 DiamondWallet

![License](https://img.shields.io/github/license/shafiahaz2478/DiamondWallet)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![API](https://img.shields.io/badge/API-1.21-green)

**DiamondWallet** is a hybrid physical economy plugin for Minecraft 1.21. It bridges the gap between physical items (Diamonds) and virtual systems (Vault/Essentials), allowing players to use physical currency in GUI shops while keeping their chests organized.

## 🔥 Key Features

- **🏦 Physical Banking:** Players store Diamonds/Blocks in a virtual GUI (`/wallet`), acting as a secure "Enderchest for Currency."
- **🔄 Auto-Sorting:** Loose diamonds are automatically compressed into Blocks when deposited.
- **💾 Hybrid Storage:**
    - **Primary:** MySQL/MariaDB.
    - **Fallback:** Local YAML files (if the database goes offline).
- **🛡️ Raid-Proof:** On Lifesteal/Anarchy servers, wallet data is safe from griefing or theft (unless the player carries the items).

---

## 📥 Installation

1.  Download the latest `.jar` from the [Releases](https://builtbybit.com/your-link-here) page.
2.  Drop the file into your server's `plugins/` folder.
3.  Restart the server.
4.  (Optional) Configure MySQL in `config.yml` to enable cross-server syncing.

---

## 📜 Commands

| Command | Permission | Description |
| :--- | :--- | :--- |
| `/wallet` | `diamondwallet.wallet` | Open your personal diamond bank. |
| `/wallet [player]` | `diamondwallet.wallet-others` | **(Admin)** View or edit another player's wallet. |
| `/walletbal` | `diamondwallet.walletbal` | Check your current balance in chat. |
| `/walletbal [player]` | `diamondwallet.walletbal-others` | Check another player's balance. |
| `/wallettop` | `diamondwallet.wallettop` | View the top richest players. |

---

## 🛠️ Configuration

The `config.yml` handles storage settings and messages.

```yaml
SQL:
  use-mysql: false
  host: localhost
  port: '3306'
  database: "plugins"
  username: root
  password: 'password'
# this is the prefix that will display on player messages where its needed
prefix: "&#00fff2[DiamondWallet] "
messages:
  mysql-not-connected: "&cdatabase is not connected this feature is disabled"
  not-a-player: "&cYou are not a player"
  player-dont-exist: "&cPlayer Doesnt exist"
  #  %AMOUNT% gives the amount of diamonds that player have these placeholder are Case Sensitive
  walletbal-self: "&a You have %AMOUNT% Diamonds"
  #  %PLAYER% give the player display/nickname
  walletbal-other: "&a %PLAYER% have %AMOUNT% Diamonds"
  wallet-top:
    #   this message will show firsy and have %TOTAL_DIAMONDS% to display total diamonds
    header: "&#ff6f00Top Diamonds List Total diamonds: %TOTAL_DIAMONDS%"
    #    %RANK% is the rank of the player this will get formated as "[#1] shafiahaz2478: 64"
    list: "&#a020f0[#%RANK%] &b%PLAYER%: &e%AMOUNT%"
