Baking App
====================

The app shows a list of Recipes from URL.
On recipe click app will show recipe ingredients and steps, can be marked as favorite recipe.
On step click app will show guide on that step (Instructions + video if available).
Landscape has different Layout.
Tablets has different Layout (Master / detail flow)
Widget can be displayed on home screen, if no recipe is favored, it shows an image, else, it will show recipe name and ingredients.
Instrumented Tests are used to check UI.


Structure
=========
1-Main Activity: contains recycler view grid layout, implementing click listener, BroadCastReceiver implemented for network to start loading if network became.

2-Recipe Adapter: populating recipes

3-Recipe Activity: has a recipe fragment that shows Ingredients and steps of the selected recipe. (On Landscape mode on phone layout will change & on Tablets it will include Step fragment)

4-Step Adapter: populating steps in a recipe.

5-Step Activity: has a step fragment, show short description, video guide (if available), and instruction to the video with navigation buttons to other steps at bottom. (On landscape mode on phones video will go full screen).

6-Recipe Class: contains Recipe constructors, getter methods and setter methods, sub class for steps.

7-JsonUtils Class: Parses the recipe URL, ingredients, and steps.

8-Baking Widget Provider: Construct a widget for baking app.

9-ListWidgetAdapter: show a favorite recipe ingredients

10-Recipe Activity Test: instrumented tests for recipe activity

11-Step Activity Test: instrumented tests for step activity


