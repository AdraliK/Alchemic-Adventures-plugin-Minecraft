package recipes;

import utils.Loadable;

import java.util.List;

public class CustomRecipes implements Loadable {

    List<Loadable> recipes = List.of(
            new ChangeHeadRecipe()
    );

    @Override
    public void register() {
        recipes.forEach(Loadable::register);
    }

    @Override
    public void shutdown() {

    }
}
