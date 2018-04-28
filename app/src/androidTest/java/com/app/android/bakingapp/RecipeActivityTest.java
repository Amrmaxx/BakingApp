package com.app.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.app.android.bakingapp.Activity.RecipeActivity;
import com.app.android.bakingapp.Model.Recipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * This test demos clicks on Recipe Activity UI components
 */

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    // These 4 Strings to mimic extras sent with Intent extra to open Recipe Activity;
    private final static String name = "Nutella Pie";
    private final static String ingredients = "2 CUP of Graham Cracker crumbs.\n" + "6 TBLSP of unsalted butter, melted.\n" + "0.5 CUP of granulated sugar.\n" + "1.5 TSP of salt.\n" + "5 TBLSP of vanilla.\n" + "1 K of Nutella or other chocolate-hazelnut spread.\n" + "500 G of Mascapone Cheese(room temperature).\n" + "1 CUP of heavy cream(cold).\n" + "4 OZ of cream cheese(softened).";
    private final static String steps = "[{\"id\":0,\"shortDescription\":\"Recipe Introduction\",\"description\":\"Recipe Introduction\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffd974_-intro-creampie\\/-intro-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":1,\"shortDescription\":\"Starting prep\",\"description\":\"1. Preheat the oven to 350°F. Butter a 9\\\" deep dish pie pan.\",\"videoURL\":\"\",\"thumbnailURL\":\"\"},{\"id\":2,\"shortDescription\":\"Prep the cookie crust.\",\"description\":\"2. Whisk the graham cracker crumbs, 50 grams (1\\/4 cup) of sugar, and 1\\/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffd9a6_2-mix-sugar-crackers-creampie\\/2-mix-sugar-crackers-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":3,\"shortDescription\":\"Press the crust into baking form.\",\"description\":\"3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffd9cb_4-press-crumbs-in-pie-plate-creampie\\/4-press-crumbs-in-pie-plate-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":4,\"shortDescription\":\"Start filling prep\",\"description\":\"4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffd97a_1-mix-marscapone-nutella-creampie\\/1-mix-marscapone-nutella-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":5,\"shortDescription\":\"Finish filling prep\",\"description\":\"5. Beat the cream cheese and 50 grams (1\\/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.\",\"videoURL\":\"\",\"thumbnailURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffda20_7-add-cream-mix-creampie\\/7-add-cream-mix-creampie.mp4\"},{\"id\":6,\"shortDescription\":\"Finishing Steps\",\"description\":\"6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. Then it's ready to serve!\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffda45_9-add-mixed-nutella-to-crust-creampie\\/9-add-mixed-nutella-to-crust-creampie.mp4\",\"thumbnailURL\":\"\"}]";
    private final static String serving = "8";

    // Launching required activity for testing before each test while passing sample intent extras for test
    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule = new ActivityTestRule<RecipeActivity>(RecipeActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra(Recipe.RECIPE_NAME, name);
            intent.putExtra(Recipe.RECIPE_INGREDIENTS_STRING, ingredients);
            intent.putExtra(Recipe.RECIPE_STEPS, steps);
            intent.putExtra(Recipe.RECIPE_SERVINGS, serving);
            return intent;
        }
    };

    @Test
    public void checkIngredientsText() {
        onView(withId(R.id.ingredients_list_tv)).check(matches(withText(ingredients + "\n\nSuitable for " + serving + " persons.")));
    }

    @Test
    public void checkIngredientsTitle() {
        onView(withId(R.id.ingredients_title)).check(matches(withText("Ingredients")));
    }

    @Test
    public void checkStepsTitle() {
        onView(withId(R.id.steps_title)).check(matches(withText("Steps")));
    }

    @Test
    public void checkStepsRecyclerView_has_sample_step() {
        onView(withId(R.id.steps_rv)).check(matches(hasDescendant(withText("Recipe Introduction"))));
        onView(withId(R.id.steps_rv)).check(matches(hasDescendant(withText("Start filling prep"))));
    }

    @Test
    public void clickExpandIcon_hides_ingredients() {
        onView(withId(R.id.expand_icon)).perform(click());
        onView(withId(R.id.expand_icon)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.ingredients_list_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}