package helpers;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class SkullCreator {

	public static ItemStack itemFromBase64(String base64) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		if (skullMeta != null) {
			PlayerProfile profile = Bukkit.createProfile("CustomHead");
			profile.setProperty(new ProfileProperty("textures", base64));
			skullMeta.setOwnerProfile(profile);
			skull.setItemMeta(skullMeta);
		}
		return skull;
	}
}
