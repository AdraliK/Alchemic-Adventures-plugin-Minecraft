package placeholders;

import listeners.world.MoonPhaseChecker;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class EventStatusPlaceholder extends PlaceholderExpansion
{
    @Override
    public String getIdentifier() {
        return "event";
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
            return MoonPhaseChecker.isFullMoon(player.getWorld()) ? "fullMoon" : "false";
        }

        return null;
    }
}
