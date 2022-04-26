# Purplelimited

## Testing:

### Unit Tests:

ingredient_toString_test
- Description: tests that when creating a new ingredient the toString method returns the correct full ingredient name/description.
- Location: app/src/test/java/edu/sc/purplelimited/classes/IngredientTest.java
- Instructions: to run this test open android studio and go to the test directory. Open the classes folder then select the IngredientTest.java file. From the .java file, click the green arrow next to the ingredient_toString_test test method then run and the test will run and display the results.

pluralSingularTest
- Description: tests proper output from toString method based on whether the units should be plural or singular.
- Location: app/src/test/java/edu/sc/purplelimited/classes/IngredientTest.java
- Instructions: to run this test open android studio and go to the test directory. Open the classes folder then select the IngredientTest.java file. From the .java file, click the green arrow next to the pluralSingularTest test method then run and the test will run and display the results.

RecipeTest
- Description: tests the return values of the getters in the Recipe class. 
- Location: app/src/test/java/edu/sc/purplelimited/classes/RecipeTest.java
- Instructions: to run this test, open android studio and go to the test directory. Open the classes folder then select the RecipeTest.java file. From the .java file, click the green arrown next to RecipeTest (on line 14), then click the run button in the popup menu. Each of the individual tests will run and their results will be displayed. 



### Instrumented Tests:

signInTextIsDisplayed
- Description: tests that the Login page displays the correct text to the user.
- Location: app\src\androidTest\java\edu\sc\purplelimited\LoginActivityInstrumentedTest.java
- Instructions: to run this test, navigate to the androidTest directory in Android Studio and open the LoginActivityInstrumentedTest.java file. From within the .java file, click the green arrow next to the signInTextIsDisplayed method and wait for the test to run and for the results to be displayed. 

correctViewIsDisplayed
- Description: tests that all of the required elements of the login screen are displayed 
- Location: app\src\androidTest\java\edu\sc\purplelimited\LoginActivityInstrumentedTest.java
- Instructions: to run this test, navigate to the androidTest directory in Android Studio and open the LoginActivityInstrumentedTest.java file. From within the .java file, click the green arrow next to the correctViewIsDisplayed method, then click the run option and wait for the test to run and for the results to be displayed. 

registrationTextIsDisplayed
- Description: tests that the Registration page displays the correct text to the user.
- Location: app\src\androidTest\java\edu\sc\purplelimited\RegistrationActivityInstrumentedTest.java
- Instructions: to run this test, navigate to the androidTest directory in Android Studio and open the RegistrationActivityInstrumentedTest.java file. From within the .java file, click the green arrow next to the registrationTextIsDisplayed method and wait for the test to run and for the results to be displayed. 

MainActivityTest
- Description: tests that the Main Activity is properly displayed, including the navigation bar.
- Location: app\src\androidTest\java\edu\sc\purplelimited\MainActivityTest.java
- Instructions: to run this test, navigate to the androidTest directory in Android Studio and open the MainActivityTest.java file. From within the .java file, click the green arrow next to MainActivityTest (line 18), then select the run option. The main activity tests will run, and the results will be displayed.
