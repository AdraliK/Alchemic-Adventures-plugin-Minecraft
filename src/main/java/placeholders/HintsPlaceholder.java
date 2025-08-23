package placeholders;

import helpers.HintsManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class HintsPlaceholder extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "hints";
    }

    @Override
    public String getAuthor() {
        return "VanillaPlus";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) return "";

        if (params.equalsIgnoreCase("status")) {
            return HintsManager.hasHints(player.getUniqueId()) ? "true" : "false";
        }

        return null;
    }
}