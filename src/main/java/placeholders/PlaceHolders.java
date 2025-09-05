package placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import utils.Loadable;

import java.util.List;

public class PlaceHolders implements Loadable {

    private static final List<PlaceholderExpansion> placeholders = List.of(
            new HintsPlaceholder(),
            new EventStatusPlaceholder()
    );

    @Override
    public void register() {
        placeholders.forEach(PlaceholderExpansion::register);
    }

    @Override
    public void shutdown() {

    }
}
