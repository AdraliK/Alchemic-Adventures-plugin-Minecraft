package placeholders;

import listeners.world.MoonPhaseChecker;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.World;
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
            return player.getWorld().getEnvironment() == World.Environment.NORMAL
                    && MoonPhaseChecker.isFullMoon(player.getWorld())
                    ? "fullMoon" : "false";
        }

        return null;
    }
}
