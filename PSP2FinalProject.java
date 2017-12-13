import java.io.*;
import java.util.Scanner;
/**
 * @Author Steve Walsh
 *
 * Student Number: R00151053
 * Class         : COMP1CY
 * Module        : PSP2
 * Assignment    : Final Project
 * Created       : 30/03/2017
 * Last Update   : 28/04/2017
 *
 */
public class PSP2FinalProject {

    final static Scanner kb = new Scanner(System.in);   // allow for keyboard input
    /*
    ---------------------------------------------------------------
    Class Constants
    ---------------------------------------------------------------
     */
    final static String EURO_SYMBOL  = "\u20ac";         // Euro Symbol

    final static String GYM_NAME     = "The Goto Gym ";  // Gym name

    // holds title page message
    final static String optionList  = "1. Add a Class Session\n"                +
                                      "2. Show Times all Classes\n"             +
                                      "3. Show Instructor Payments Due\n"       +
                                      "4. Print TimeTable for Instructor\n"     +
                                      "5. Show Ordered TimeTable with Codes\n"  +
                                      "6. Exit";
    //titles for page selection
    final static String TITLE_1     = "Add a Class"      ;
    final static String TITLE_2     = "Classes"          ;
    final static String TITLE_3     = "Payments Due"     ;
    final static String TITLE_4     = "Timetables"       ;
    final static String TITLE_5     = "Ordered Timetable";

    // Option 1 - Add a Class messages
    final static String OPTION_1_TITLE_1     = "Enter the Class Type:"  ;
    final static String OPTION_1_TITLE_2     = "Enter the day:"         ;
    final static String OPTION_1_TITLE_3     = "Choose a time:"         ;
    final static String OPTION_1_TITLE_4     = "Choose a teacher:"      ;

    // Option 3 - Show Instructor Payments Due messages
    final static String OPTION_3_TITLE       = " Name     Classes   Pay(" + EURO_SYMBOL +")";
    final static String OPTION_3_LINE_BREAK  = "------    -------   ------"          ;
    final static String TOTAL_WAGES          = "Total wages for this week: ";

    // option 4 - Print TimeTable for Instructor messages
    final static String ASK_NAME             = "Please enter a name: "        ;
    final static String CREATED_TIMETABLE    = "sTimetable.txt created."      ;
    final static String WRITING_FILE         = "Writing to file..."           ;
    final static String NO_CLASSES           = "No classes assigned this week";

    // validation messages
    final static String INVALID_INTEGER = "Invalid input. Enter an integer: "            ;
    final static String INVALID_INPUT   = "Invalid input. "                              ;
    final static String INVALID_NAME    = "Instructor name not found.\n"                 ;
    final static String INVALID_RANGE   = "Invalid input. Enter an option in range 1-6 :";
    final static String FILE_ERROR      = "File missing"                                 ;
    final static String INSTRUCTOR_ALREADY_BOOKED = "That instructor already has a class at that time!\n";

    final static String PRESS_RETURN    = "\nPress Return to continue";        // return to continue

    final static String LINE_BREAK      = "-------------------------\n";       // line break

    final static int NUM_INSTRUCTORS    = 4;                                   // total number of instructors at 4

    final static int NUM_CLASSES        = 12;                                  // total number of classes per week at 12

    final static String[] CLASS_TIMES   = {"7","8","9"};                       // holds Class times

    final static String[] DAYS_OF_WEEK  = {"Mon", "Tue", "Wed"};               // holds days of week that classes are on

    static final String[] instructors   = new String[NUM_INSTRUCTORS];         // holds instructors

    static final String[] classTypes    = new String[3];                       // holds 3 class Types

    static final String[] classTypesAbb = new String[3];                       // hold class Types abbreviated

    final static int COST_FROM_7TO8     = 60;                                  // costs of classes at 7 or 8 of 60 euro

    final static int COST_AT_9          = 80;                                  // costs of classes at 9 of 80 euro

    final static int BONUS              = 10;                                  // bonus pay per class of 10 euro

    final static int MIN_NUM_CLASSES    = 2;                                   // minimum number classes to be eligible for bonus pay at 2

    final static int MAX_NUM_ITEMS      = 48;                                  // maximum number of items for classesBooked at 48
    /*
    ---------------------------------------------------------------
    Main method Start
    ---------------------------------------------------------------
    */
    /**
     * Programs main method
     *
     * @param args          - main method paramater
     * @throws IOException  - throws error if input files not found
     */
    public static void main(String[] args) throws IOException {
        /*
    ---------------------------------------------------------------
        Arrays
    ---------------------------------------------------------------
     */
        String[] classDetailsArray    = new String[MAX_NUM_ITEMS];   // holds all class details

        String[] instructorsBooked    = new String[NUM_CLASSES];     // holds instructors booked for timetable

        String[] classTypesBooked     = new String[NUM_CLASSES];     // holds class types booked abbreviated

        String[] classTypesFullBooked = new String[NUM_CLASSES];     // hold booked classes types in full

        String[] daysBooked           = new String[NUM_CLASSES];     // holds days booked

        String[] classTimesBooked     = new String[NUM_CLASSES];     // holds times of classes booked
     /*
    ---------------------------------------------------------------
     Program Start
    ---------------------------------------------------------------
     */
    loadArrays(classDetailsArray);                              // stores booked classes from file as classDetailsArray

    generateTimetableParallelArrays(classDetailsArray, instructorsBooked,classTypesBooked,
            classTypesFullBooked, daysBooked, classTimesBooked  );  // loads up parallel arrays needed for program

    boolean exit = false;                                       // loop checks when user types 6 to exit program
    while (!exit) {

        int choice = readInt(optionList);                       // prints options message to user and store next input as users choice

        switch (choice) {                                       // switch statement to select users keyboard choice
           /*
            ---------------------------------------------------------------
            Option 1 - Add a Class
            ---------------------------------------------------------------
            */
            case 1:
                printTitle(TITLE_1);                            // print page title

                printOptions(OPTION_1_TITLE_1 , classTypes);    // display class types to user

                int classTypeSelection = chooseOption();        // capture users choice and save to array index

                printOptions(OPTION_1_TITLE_2, DAYS_OF_WEEK);   // display choice of days to user

                int daySelection = chooseOption();              // capture users choice and save to array index

                printOptions(OPTION_1_TITLE_3, CLASS_TIMES);    // display time selection

                int timeSelection = chooseOption();             // capture users choice and save to array index

                printOptions(OPTION_1_TITLE_4, instructors);    // display instructor selection

                int instructorSelection = chooseOption();       // capture users choice and save to array index

                // checks if instructor already has a class at that time - returns 0 for no and 1 for yes.
                int valid = checkInstructorsBookedClasses(instructors[instructorSelection], CLASS_TIMES[timeSelection],
                        DAYS_OF_WEEK[daySelection],daysBooked,
                        classTimesBooked, instructorsBooked);

                // if 0 then instructor is not booked already so new class details are added to file and printer to user
                if ( valid == 0) {
                    // append added class to class details file
                    updateBookedClasses(classTypesAbb,classTypeSelection, daySelection,
                            timeSelection,instructorSelection );

                    // print the added class back to the user
                    printAddedClass(classTypesAbb,classTypeSelection,daySelection,
                            timeSelection,instructorSelection );

                    // updates class Details arrays with new added class
                    loadArrays(classDetailsArray);

                    // updates parallel arrays
                    generateTimetableParallelArrays(classDetailsArray, instructorsBooked,classTypesBooked,
                            classTypesFullBooked, daysBooked, classTimesBooked  );

                } else if (valid == 1 ) {
                    // prints message advising instructor already has a class booked
                    System.out.print(INSTRUCTOR_ALREADY_BOOKED);
                }
                break;
            /*
            ---------------------------------------------------------------
             Option 2 - Show Times all Classes
            ---------------------------------------------------------------
            */
            case 2:
                printTitle(TITLE_2);                                    // print page title

                printBookedClasses( instructorsBooked, classTypesFullBooked,
                                    daysBooked, classTimesBooked);       // prints booked classes to user
                break;
            /*
            ---------------------------------------------------------------
            Option 3 - Show Instructor Payments Due
            ---------------------------------------------------------------
            */
            case 3:
                printTitle(TITLE_3);                                    // print page title

                printPaymentsHeading();                                 // print payments heading

                int wagesDue =  calculateTotalWages(classDetailsArray); // calculate total wages due

                printTotalWages(wagesDue);                              // print total wages due for all instructors

                break;
            /*
            ---------------------------------------------------------------
            Option 4 - Print TimeTable for Instructor
            ---------------------------------------------------------------
            */
            case 4:
                printTitle(TITLE_4);                           // print page title

                String instructorNameEntered = storeName();    // asks for and stores instructor name

                //check if instructor has classes
                int results = sequentialSearch(classDetailsArray, instructorNameEntered);

                if (results == -1) {

                    // Create new file with instructors timetable saying no classes assigned
                    writeNoClassesTimetable(instructorNameEntered);

                    // print message to user saying no classes assigned to that instructor
                    System.out.print(NO_CLASSES);

                } else {

                    // Create new file with instructors timetable showing booked classes
                    writeTimetableFile(instructorNameEntered,instructorsBooked,daysBooked,
                            classTimesBooked,classTypesFullBooked);

                    // print full time table to user showing booked classes
                    printFullTimetable(instructorNameEntered,instructorsBooked,daysBooked,
                            classTimesBooked,classTypesFullBooked);
                }
                break;
            /*
            ---------------------------------------------------------------
            Option 5 - Show Ordered TimeTable with Codes
            ---------------------------------------------------------------
            */
            case 5:
                printTitle(TITLE_5);                           // print page title

                printOrderedTimetable(daysBooked,classTimesBooked,
                                        classTypesBooked);     // print ordered time table by day
                break;
                /*
            ---------------------------------------------------------------
            Option 6 - Exit Program
            ---------------------------------------------------------------
            */
            case 6:
                exit = true;                                   // Exits application
                break;
        }
        System.out.println(PRESS_RETURN);                      // prints message to press return to continue
        kb.nextLine();
      }
    }
    /*
    ---------------------------------------------------------------
    Main method End
    ---------------------------------------------------------------
    */
    /*
    ---------------------------------------------------------------
        Other Methods Used
    ---------------------------------------------------------------
     */



    /**
     * Load arrays needed for the program from text files
     *
     * Print message if any files are missing
     *
     * instructors[]
     * classTypes[]
     * classDetails[]
     * classDetailsAbb[]
     * @param classDetailsArray -- inputs classDetailsArray
     *
     * @throws FileNotFoundException   -- error if files not found
     */
    private static void loadArrays(String[] classDetailsArray) throws FileNotFoundException {

        File myFile = new File("Instructors");

        if(!myFile.exists()) {                             // checks file exits
            System.out.println(FILE_ERROR);                // prints error if not found
        }
        else {
            Scanner inputFile = new Scanner(myFile);       // Reading data from file

            for (int index = 0; index < NUM_INSTRUCTORS; index++) {

                instructors[index] = inputFile.nextLine(); // assign items in file to array
            }
            inputFile.close();                             // Close the file.
        }


        myFile = new File("ClassTypes");                // Reading data from file
        if(!myFile.exists()) {                             // checks file exits
            System.out.println(FILE_ERROR);                // prints error if not found
        }
        else {
            Scanner inputFile = new Scanner(myFile);

            for (int index = 0; index < 3; index++) {      // loop through file

                classTypes[index] = inputFile.nextLine();  // save to classTypes array
            }
            inputFile.close();                             // Close the file.
        }

        myFile = new File("ClassDetails");              // open and read info from text file
        if(!myFile.exists()) {                    // checks file exits
            System.out.println(FILE_ERROR);                // prints error if not found
        }
        else {
            Scanner inputFile = new Scanner(myFile);
            int index = 0;
            while (inputFile.hasNext()) {                  // read until end of file to see how many items

                classDetailsArray[index] = inputFile.nextLine(); // store items as array
                index++;
            }
            inputFile.close(); // Close the file.
        }

        for (int i = 0; i < classTypes.length; i++){       // abbreviated to show first two letters capitalised
            classTypesAbb[i] = classTypes[i].toUpperCase();
            classTypesAbb[i] = (String.valueOf(classTypesAbb[i].charAt(0)) +
                    String.valueOf(classTypesAbb[i].charAt(1)));
        }
    }
    /**
     * generates parallel arrays from the classDetailsArray
     * by splitting into classType, instructors, Times and Days
     *
     *
     * @param classDetailsArray        - inputs classDetailsArray
     * @param instructorsTimetable     - inputs instructorsTimetable
     * @param classTypeTimetable       - inputs classTypeTimetable
     * @param classTypesFullTimetable  - inputs classTypesFullTimetable
     * @param dayTimetable             - inputs dayTimetable
     * @param timeTimetable            - inputs timeTimetable
     */
    public static void generateTimetableParallelArrays(String[] classDetailsArray, String[] instructorsTimetable, String[] classTypeTimetable,
                                                       String[] classTypesFullTimetable, String[] dayTimetable, String[] timeTimetable) {
        int counter = 0 ;
        int numBookedClasses=0;
        for (int index = 0; index< classDetailsArray.length; index+=4) {
            classTypeTimetable[counter]   = classDetailsArray[index]  ;
            classTypesFullTimetable[counter]   = classDetailsArray[index];
            dayTimetable[counter]         = classDetailsArray[index+1];
            timeTimetable[counter]        = classDetailsArray[index+2];
            instructorsTimetable[counter] = classDetailsArray[index+3];
            counter++;
        }
        convertClassTypesFullTimetable(classTypesFullTimetable);
    }
    /**
     * converts booked time table to non abbreviated version
     * Yoga     becomes YO
     * Spinning becomes SP
     * Pilates  becomes PI
     *
     * @param classTypesFullTimetable -- full name of booked classes
     */
    public static void convertClassTypesFullTimetable(String[] classTypesFullTimetable) {

        for(int i = 0; i < classTypesFullTimetable.length; i++)     // incriments through the booked classes
        {
            if(classTypesFullTimetable[i] == null) {                   // checks to see next index is not null
                break;
            } else if (classTypesFullTimetable[i].equals(classTypesAbb[0])) {     // if index is same as classTypesAbb array

                classTypesFullTimetable[i] = classTypes[0];                       // copy in full name of class type

            } else if (classTypesFullTimetable[i].equals(classTypesAbb[1])) {     // if index is same as classTypesAbb array

                classTypesFullTimetable[i] = classTypes[1];                       // copy in full name of class type

            } else if (classTypesFullTimetable[i].equals(classTypesAbb[2])) {     // if index is same as classTypesAbb array

                classTypesFullTimetable[i] = classTypes[2];
            }
        }
    }
    /**
     * Asks question, reads and returns integer
     *
     * @param question - question asked
     * @return choice - option user picked
     */
    public static int readInt(String question) {
        int choice=0;
        int input;

        System.out.println(question);
        while (!kb.hasNextInt()) {                          // checks input is an integer
            kb.nextLine();
            System.out.println(INVALID_INTEGER + "\n");     // prints error message if input is not an int
            System.out.println(question);
        }
        input = kb.nextInt();                               // if integer stores as input
        kb.nextLine();                                      // consumes line

        if (input > 6 || input < 1) {                       // checks input is in valid range
            System.out.println(INVALID_RANGE + "\n");        // prints error message if inputted int is not in range 1-6
            System.out.println(question);
        } else {
            choice = input;                                 // if valid stores as choice
        }
        return choice;                                      // returns choice
    }
    /**
     * Prints Page title to the user
     *
     * @param  name  - title of specific page
     */
    private static void printTitle(String name) {
        System.out.println(GYM_NAME + name );              // prints tile of page
        System.out.println(LINE_BREAK);                    // prints line break
    }
    /** Checks if item is in array
     *
     * In this case checks if the name of an instructor was entered
     *
     * @return element - index item is found at
     */
    public static int sequentialSearch(String[] array, String name) {

        int index = 0;

        int element = -1;

        boolean found = false;
        // keeps looping through array until found
        while (!found && index < array.length && array[index]!= null) {

            if (array[index].equalsIgnoreCase(name)) {    // if found
                found = true;                             // set found to true to allow exit loop
                element = index;                          // store the index element was found as element
            }
            index++;                                      // keeps incrementing through array if not found
        }
        return element;                                   // returns the element
    }
     /*
    ---------------------------------------------------------------
        Methods for Option 1 - Add a Class
    ---------------------------------------------------------------
     */
    /**
     * Prints option types to the user
     *
     * @param title          - title of which option screen user is on
     * @param displayedArray - array of what question user is on
     */
    private static void printOptions(String title, String[] displayedArray) {
        System.out.println(title);                  // prints class options
        for (int index = 0; index < 3; index++){
            System.out.println("\t" + (index+1) + ": " + displayedArray[index]);
        }
    }
    /**
     *  Stores input and locates index in array
     *
     * @return indexInArray - variable that holds index of user choice
     */
    private static int chooseOption() {

        int userChoice;                             // hold users choice

        boolean valid = false;                      // validate user input is of correct data type and in range
        do
        {
            while (!kb.hasNextInt())                // validate data type to check for an int
            {
                kb.next();                          // clear invalid input
                System.out.println(INVALID_INTEGER);// print error if user types invalid input
            }
            userChoice = kb.nextInt();
            if (userChoice < 1 || userChoice > 3)   // validate range
                System.out.println(INVALID_INPUT);  // print error if user types invalid range for choices
            else
                valid = true;                       // sets valid to true when user types int of correct range
        } while(!valid);

        return userChoice -1;                       // adjust to fit array index and return the user choice as int
    }
    /**
     *  Checks parallel arrays instructorsBooked,classTimesBooked timesBooked to see if the instructor already
     *  has a class booked at the inputted time or not
     *
     * @param instructor        - instructor name inputted by user
     * @param days              - day inputted by user
     * @param time              - class time  name inputted by user
     * @param daysBooked        - array of days booked
     * @param classTimesBooked  - array of class times booked
     * @param instructorsBooked - array of instructors booked
     */
    private static int checkInstructorsBookedClasses(String instructor, String days, String time,
                                                     String[] daysBooked, String[] classTimesBooked,
                                                     String[] instructorsBooked) {
        int valid=0;
        int bookingIndex = 0;
        // compare the entered day and time
        // loop up to number of booked classes - prevents errors from null elements
        for (int index = 0; index < 5; index++) {
            // compares index in timeTimetable and DayTimetable with current day of week and class time
            if (classTimesBooked[index].equalsIgnoreCase(time) &&
                    daysBooked[index].equalsIgnoreCase(days) &&
                    instructor.equals(instructorsBooked[index])
                    ) {
                bookingIndex = index; // stores bookingIndex as index if classTimesBooked matches the time along with instructor name and day
            }
        }

        if (instructor.equals(instructorsBooked[bookingIndex])) { // compares instructor name entered with already booked instructor to see for already booked classes

            valid = 1;  // if true sets valid to 1
        }
        return valid;  // returns the choice to check if class time clash
    }
    /**
     *  Updates ClassDetails file with new booked class
     *
     * @param classTypesAbbrev - stores first 2 letters capitalised of class type
     * @param classTypeBooked  - stores type of class booked by user as integer
     * @param dayBooked        - stores day  of class booked by user as integer
     * @param timeBooked       - stores time of class booked by user as integer
     * @param instructorBooked - stores instructor for class by user as integer
     */
    private static void updateBookedClasses(String[] classTypesAbbrev, int classTypeBooked, int dayBooked,
                                            int timeBooked, int instructorBooked) throws IOException {
        // allow append to txt file
        FileWriter fw =  new FileWriter("ClassDetails", true);

        PrintWriter outputFile = new PrintWriter(fw);

        kb.nextLine();         // clear input

        // print the added class details to file
        outputFile.println(classTypesAbbrev[classTypeBooked]);
        outputFile.println(DAYS_OF_WEEK[dayBooked]);
        outputFile.println(CLASS_TIMES[timeBooked]);
        outputFile.println(instructors[instructorBooked]);

        outputFile.close();    // close file
        fw.close();            // close file
    }
    /**
     *  prints added class details back to user
     *
     * @param classType  - inputs classType
     * @param day        - days of the week
     * @param time       - times of classes
     * @param instructor - instructor
     */
    private static void printAddedClass(String[] classTypesAbbreviated, int classType, int day, int time, int instructor) {
        // prints added class details back to user
        System.out.println("Added " + classTypesAbbreviated[classType] +
                " on "   + DAYS_OF_WEEK[day]                +
                " at "   + CLASS_TIMES[time]                +
                " with " + instructors[instructor]);
    }
    /*
    ---------------------------------------------------------------
        Methods for Option 2 - Show Times all Classes
    ---------------------------------------------------------------
     */
     /**
     * prints booked classes to user
     *
     * @param instructorsBooked
     * @param classTypesFullBooked   - takes classTypeTimetable
     * @param dayTimetable         - takes dayTimetable
     * @param timeTimetable        - takes timeTimetable
     */
    private static void printBookedClasses(String[] instructorsBooked, String[] classTypesFullBooked,
                                           String[] dayTimetable, String[] timeTimetable) {

        int numBookedClasses =0;  // stores number of booked classes
        // loops through array to find number of booked classes
        for ( int k = 0; k< timeTimetable.length;k++) {
            if ( dayTimetable[k] != null)
                numBookedClasses++;
        }
    for( int index = 0 ; index < classTypes.length; index++) {
        // prints out the Class types
        System.out.println(classTypes[index]);
        // loops through booked classes
        for( int counter = 0 ; counter < numBookedClasses; counter++) {
            // checks name of class booked to see if name as class type index
        if ( classTypesFullBooked[counter].equals(classTypes[index]))
            // prints details from parallel arrays related to the counter found
            System.out.println("\n\t\t" +dayTimetable[counter] + " at " + timeTimetable[counter] +
                                        " with " + instructorsBooked[counter] );
           }
        }
    }
    /*
    ---------------------------------------------------------------
        Methods for Option 3 - Show Instructor Payments Due
    ---------------------------------------------------------------
     */
    /**
     * prints payment heading
     *
     */
    private static void printPaymentsHeading ( ) {
        System.out.println(OPTION_3_TITLE     );
        System.out.println(OPTION_3_LINE_BREAK);
    }
    /**
     * calculate number classes per instructor
     *
     * @param name - name of instructor
     */
    private static int getCount(String name) throws FileNotFoundException {

        int numberClassesTaught = 0;                    // hold number of classes taught

        File myFile = new File("ClassDetails");    // open and read info from text file
        Scanner inputFile = new Scanner(myFile);
        while (inputFile.hasNext()) {                 // read until end of file to see how many items

            if (inputFile.nextLine().equals(name)) {  // checks if next line is instructors name

                numberClassesTaught++;                // when found increments numberClassesTaught
            }
        }
        inputFile.close();                            //close file

        return numberClassesTaught;                   // returns numberClassesTaught
    }
    /**
     * calculate bonus pay based on number classes over minimum amount
     *
     * @param number - takes in the total number of classes taught by instructor
     *
     * @return bonusPayTotal - returns total amount of bonus pay
     */
    private static int extraPayPerClass(int number) {

        int bonusPayTotal = 0;                                  //variable to hold total amount of bonus pay
        if (number > MIN_NUM_CLASSES) {                         // checks if eligible for bonus pay
            bonusPayTotal = (number - MIN_NUM_CLASSES) * BONUS; // calculates bonus pay
        }
        return bonusPayTotal;                                   // returns bonusPayTotal
    }
    /**
     * Calculate  pay based on times classes are taught
     *
     * @param count  - increment through instructors
     *
     * @return payForClassTimes - returns total amount of bonus pay
     */
    private static int payPerClass(int count,String[] classDetailsArray)  {

        int numClasses7      = 0;   // hold number classes at 7
        int numClasses8      = 0;   // hold number classes at 8
        int numClasses9      = 0;   // hold number classes at 9
        // loop through new classDetailsArray
        for (int i = 0; i <classDetailsArray.length;i++){

            // Compares CLASS_TIMES to classDetailsArray and counts each instance
            if        (CLASS_TIMES[0].equals(classDetailsArray[i]) && instructors[count].equals(classDetailsArray[i+1])) {
                numClasses7++;
            } else if (CLASS_TIMES[1].equals(classDetailsArray[i]) && instructors[count].equals(classDetailsArray[i+1])) {
                numClasses8++;
            } else if (CLASS_TIMES[2].equals(classDetailsArray[i]) && instructors[count].equals(classDetailsArray[i+1])) {
                numClasses9++;
            }
        }
        int payClassesAt7 = numClasses7 * COST_FROM_7TO8 ;  //calculate wage for classes at 7
        int payClassesAt8 = numClasses8 * COST_FROM_7TO8 ;  //calculate wage for classes at 8
        int payClassesAt9 = numClasses9 * COST_AT_9      ;  //calculate wage for classes at 9

        return payClassesAt7 + payClassesAt8 + payClassesAt9;      // returns total pay
    }
    /**
     * Calculate  total wages
     *
     * Uses number of classes,times of classes and pay rates
     * to work out the total wages due for all instructors
     *
     * @return totalWages - returns total amount of wages due
     */
    private static int calculateTotalWages(String[]classDetailsArray) throws FileNotFoundException {

        int totalWages =0;
        // loop through instructors array
        for ( int counter = 0; counter< instructors.length; counter++) {

            int numberClassesTaught = getCount(instructors[counter]);  // calculate number of classes per instructor

            int bonusPay = extraPayPerClass(numberClassesTaught);      // calculate bonus pay

            int classPay = payPerClass(counter,classDetailsArray);     // calculate pay for times class taught

            int totalPay = bonusPay + classPay;                        // calculate total pay for instructor

            totalWages = totalWages + totalPay;                        // calculate totalWages

            // print instructor,numberClassesTaught and their totalPay to user
            System.out.println(instructors[counter] + " \t\t" + numberClassesTaught +
                    "\t\t" + totalPay);
        }
        return totalWages;    // returns totalWages
    }

    /**
     * Prints total wages to user
     *
     * @param wages  - total wages due
     */
    private static void printTotalWages(int wages) {

        System.out.println("\n\n" + TOTAL_WAGES + EURO_SYMBOL + wages);
    }
    /*
    ---------------------------------------------------------------
    Methods for Option 4 - Print TimeTable for Instructor
    ---------------------------------------------------------------
    */
    /**
     * asks for and stores name entered by user
     *
     * @return name - name entered by user
     */
    private static String storeName() {
        String name = "";                     //variable to hold name

        boolean validName = false;
        // keeps asking for name if invalid

        while (!validName) {

            System.out.print(ASK_NAME);           // ask for name

            String enteredName = kb.nextLine();   // store name entered by user

            // Searching for name in an Array
            int results = sequentialSearch(instructors, enteredName);

            if (results == -1) {                  //Determine whether name was found
                System.out.println(INVALID_NAME); //displays message if invalid
            } else {
                name = enteredName;
                printMessageSavingFile(name);     // print message writing to file
                validName = true;
            }
        }
        return name;                          // returns name
    }
    /**
     * Prints message saying saving instructor time table
     *
     *@param name  - name of instructor
     */
    public static void printMessageSavingFile(String name){
        System.out.println("\n\n" + name + CREATED_TIMETABLE);       // print message with file name
        System.out.println(WRITING_FILE + "\n\n");
    }
    /**
     * Write timetable showing no classes assigned
     *
     * @param name  - name of instructor
     *
     */
    public static void writeNoClassesTimetable(String name) throws FileNotFoundException {

        PrintWriter outputFile = new PrintWriter(name + ".txt"); // Create the filename.

        outputFile.println(NO_CLASSES); // print message saying no classes assigned

        outputFile.close();             // Close the file.
    }
    /**
     * Write timetable to file in the name of instructor
     *
     * @param name                  - name of instructor
     * @param instructorsTimetable  - booked instructors
     * @param dayTimetable          - booked days
     * @param timeTimetable         - booked times
     * @param classTypeTimetable    - booked class types
     */
    public static void writeTimetableFile(String name, String[] instructorsTimetable, String[] dayTimetable, String[] timeTimetable, String[] classTypeTimetable) throws FileNotFoundException {

        PrintWriter outputFile = new PrintWriter(name + ".txt"); // Create the filename.
        for (int index = 0; index < instructorsTimetable.length; index++) {
            if (name.equals(instructorsTimetable[index]) ) {        // compares name with instructorsTimetable array
                outputFile.println(classTypeTimetable[index] +
                        "\t" + dayTimetable[index] +                // outputs time table if name matches
                        " at " + timeTimetable[index]);
            }
        }
        outputFile.close(); // Close the file.
    }
    /**
     * Prints instructors time table to screen
     *
     * @param name                 - name entered
     * @param instructorsTimetable - booked instructors
     * @param dayTimetable         - booked days
     * @param timeTimetable        - booked times
     * @param classTypeTimetable   - booked class types
     */
    public static void printFullTimetable(String name, String[] instructorsTimetable, String[] dayTimetable, String[] timeTimetable, String[] classTypeTimetable){
        for (int index = 0; index < instructorsTimetable.length; index++) {
            if (name.equals(instructorsTimetable[index]) )           // compares name with instructorsTimetable array
                System.out.println(classTypeTimetable[index] +
                        "\t\t" + dayTimetable[index] +       // prints time table if name matches
                        " at " + timeTimetable[index]);
        }
    }
    /*
     ---------------------------------------------------------------
     Methods for Option 5 - Show Ordered TimeTable with Codes
     ---------------------------------------------------------------
     */
    /**
     * Prints time table ordered by day and class type to screen
     *
     * @param dayTimetable       - booked days
     * @param timeTimetable      - booked times
     * @param classTypeTimetable - booked class types
     */
    public static void printOrderedTimetable(String[] dayTimetable, String[] timeTimetable, String[] classTypeTimetable){
    int numBookedClasses =0;  // stores number of booked classes
        // loops through array to find number of booked classes
        for ( int index = 0; index< timeTimetable.length;index++) {
            if ( dayTimetable[index] != null)
                numBookedClasses++;
        }
            // loop through days
            for (int daysIndex = 0; daysIndex < DAYS_OF_WEEK.length; daysIndex++) {
                // Print day
                System.out.println(DAYS_OF_WEEK[daysIndex]);

                // loop through times of classes
                for (int timeIndex = 0; timeIndex < CLASS_TIMES.length; timeIndex++) {

                    // loop up to number of booked classes - prevents errors from null elements
                    for (int index = 0; index < numBookedClasses; index++) {
                        // compares index in timeTimetable and DayTimetable with current day of week and class time
                        if (timeTimetable[index].equalsIgnoreCase(CLASS_TIMES[timeIndex]) &&
                                dayTimetable[index].equalsIgnoreCase(DAYS_OF_WEEK[daysIndex])
                                ) {
                            // prints time with booked class if found
                            System.out.println("\t\t" + CLASS_TIMES[timeIndex] + " "+ classTypeTimetable[index] );
                        }
                    }
                }
            }
    }
} // close class

