package com.app.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.app.android.bakingapp.activity.StepActivity;
import com.app.android.bakingapp.model.Recipe;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;

/**
 * This test demos clicks on Recipe Activity UI components
 */

@RunWith(AndroidJUnit4.class)
public class StepActivityTest {

    // These values to mimic extras sent with Intent extra to open Step Activity
    private final static String name = "Nutella Pie";
    private final static String steps = "[{\"id\":0,\"shortDescription\":\"Recipe Introduction\",\"description\":\"Recipe Introduction\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffd974_-intro-creampie\\/-intro-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":1,\"shortDescription\":\"Starting prep\",\"description\":\"1. Preheat the oven to 350°F. Butter a 9\\\" deep dish pie pan.\",\"videoURL\":\"\",\"thumbnailURL\":\"\"},{\"id\":2,\"shortDescription\":\"Prep the cookie crust.\",\"description\":\"2. Whisk the graham cracker crumbs, 50 grams (1\\/4 cup) of sugar, and 1\\/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffd9a6_2-mix-sugar-crackers-creampie\\/2-mix-sugar-crackers-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":3,\"shortDescription\":\"Press the crust into baking form.\",\"description\":\"3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffd9cb_4-press-crumbs-in-pie-plate-creampie\\/4-press-crumbs-in-pie-plate-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":4,\"shortDescription\":\"Start filling prep\",\"description\":\"4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffd97a_1-mix-marscapone-nutella-creampie\\/1-mix-marscapone-nutella-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":5,\"shortDescription\":\"Finish filling prep\",\"description\":\"5. Beat the cream cheese and 50 grams (1\\/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.\",\"videoURL\":\"\",\"thumbnailURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffda20_7-add-cream-mix-creampie\\/7-add-cream-mix-creampie.mp4\"},{\"id\":6,\"shortDescription\":\"Finishing Steps\",\"description\":\"6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. Then it's ready to serve!\",\"videoURL\":\"https:\\/\\/d17h27t6h515a5.cloudfront.net\\/topher\\/2017\\/April\\/58ffda45_9-add-mixed-nutella-to-crust-creampie\\/9-add-mixed-nutella-to-crust-creampie.mp4\",\"thumbnailURL\":\"\"}]";
    private int stepID = 2;

    @Rule
    public ActivityTestRule<StepActivity> mActivityTestRule = new ActivityTestRule<StepActivity>(StepActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(context, StepActivity.class);
            intent.putExtra(Recipe.RECIPE_NAME, name);
            intent.putExtra(Recipe.Step.STEP_ID, stepID);
            intent.putExtra(Recipe.RECIPE_STEPS, steps);
            return intent;
        }
    };

    @Test
    public void checkShortDescriptionText() {
        onView(withId(R.id.short_description_tv)).check(matches(withText("Prep the cookie crust.")));
    }

    @Test
    public void checkInstructionText() {
        onView(withId(R.id.instruction_tv)).check(matches(withText(containsString("graham cracker"))));
    }

    @Test
    public void checkTotalStepsTextView() {
        onView(withId(R.id.total_steps)).check(matches(withText("3/7")));
    }

    @Test
    public void checkViewsVisibility() {        // Small screen Portrait Mode
        onView(withId(R.id.short_description_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.instruction_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.total_steps)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.next_iv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.next_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.previous_iv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.previous_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void checkViewsAfterRotation() {     // Only for Portrait
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Configuration configuration = context.getResources().getConfiguration();
        int width = configuration.screenWidthDp;
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (width >= 500) {    //if tablet
            onView(withId(R.id.short_description_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withId(R.id.instruction_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withId(R.id.total_steps)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withId(R.id.next_iv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withId(R.id.next_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withId(R.id.previous_iv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            onView(withId(R.id.previous_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        } else {                //if phone (Portrait)
            onView(withId(R.id.short_description_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.instruction_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.total_steps)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.next_iv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.next_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.previous_iv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
            onView(withId(R.id.previous_tv)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        }
    }
}