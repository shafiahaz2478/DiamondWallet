package me.shafi.diamondwallet.util;

import me.shafi.diamondwallet.DiamondWallet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChatUtil {

    public static Component format(String message) {
        if (message == null) return Component.empty();

        String hexParsed = hexColor(message);

        String legacyCode = hexParsed.replace('&', '§');

        return LegacyComponentSerializer.legacySection().deserialize(legacyCode);
    }

    public static Component formatWithPrefix(String message) {

        return DiamondWallet.prefix.append(format(message));
    }

    private static String hexColor(String message) {
        final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, "§x"
                    + "§" + group.charAt(0) + "§" + group.charAt(1)
                    + "§" + group.charAt(2) + "§" + group.charAt(3)
                    + "§" + group.charAt(4) + "§" + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    public static Component walletbalformat(String playername , int amount, String message){
         String result = message;

         result = result.replace("%PLAYER%" , playername);
         result = result.replace("%AMOUNT%" , String.valueOf(amount));

         return formatWithPrefix(result);
    }

    public static Component walletTopListFormat(String playername, int rank, int amount, String message){
        String result = message;

        result = result.replace("%PLAYER%" , playername);
        result = result.replace("%RANK%" , String.valueOf(rank));
        result = result.replace("%AMOUNT%" , String.valueOf(amount));

        return format(result);
    }

}
