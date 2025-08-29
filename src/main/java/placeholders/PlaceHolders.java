package placeholders;

public class PlaceHolders {

    public static void init() {
        new HintsPlaceholder().register();
        new EventStatusPlaceholder().register();
    }

}
