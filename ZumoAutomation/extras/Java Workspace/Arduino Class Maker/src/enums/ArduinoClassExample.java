package enums;
import parsing.ArduinoParser;

/** Name: Jacob Smith
 *  Email:jsmith2021@brandeis.edu
 *  Assignment: Personal Study, an enum class of example fields used to generate the
 *  hardCoded correct examples of cpp,h, and keywords files
 *  Date: May 16, 2019
 *  Sources: https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
 *  Bugs:
 *  notes: enum has nice automatic toString, naem field
 *  Need to have variable enforceMent for if supportedBoards is all, 
 *  and if a field is null,and ask for variables,privateMethods,and public methods one
 *  at a time
 */


public enum ArduinoClassExample {
	
    CLASSNAME 		("Timer",true,true,true,false,null),
    AUTHOR   		("Jacob Smith",true,true,true,false,null),
    ORGANIZATION   	("Brandeis Robotics Club",false,true,true,false,null),
    HEADERCOMMENTS 	("A timer class to allow the user to create loops and maintain program control",false,true,true,true,null),
    SUPPORTEDBOARDS	("ARDUINO_AVR_UNO ESP8266_WEMOSD1R1",false,true,true,true,"ALL"),
    VARIABLES  		(
    		"long|initTime|the beginning time of the interval\n" + 
    		"Apple|test|a test varible for the parser",false,true,true,true,"null"),
    PRIVATEMETHODS  (null,false,true,true,true,"null"),
    PUBLICMETHODS 	(
    		"initTime=millis();\n\n"+
    		"long|resetTime|resets the Initial Time|\n"+
    		"initTime=millis();\nreturn getTime();\n\n"+
    		"long|getTime|returns the current time|\n"+
    		"return millis()-initTime;\n\n"+
    		"long|getAndResetTime|returns the current time and the initial time|\n"+
    		"long curTime=getTime();\nresetTime();\nreturn curTime;\n",false,true,true,true,null);
    
    //the hardcoded example of the field
    private String example;
    //validation information
    private boolean enforceSpaces;
    private boolean enforceBars;
    private boolean enforceNewLines;
    private int numSpaces;
    private int numNewLines;
    private int numBars;
    //sepcail forms information, like how board defines can either be specific forms or ALL
    private boolean multiplePrompt;
    private String alternateForm;
    
    /** A private constructor to create an enum with prompt and format
     * */
    ArduinoClassExample(String example,boolean enforceSpaces,boolean enforceBars,boolean enforceNewLines,boolean multiplePrompt,String alternateForm) {
    	//load the example string
    	this.example=example;
    	//load whether it should enforce certain special characters
        this.enforceSpaces=enforceSpaces;
        this.enforceBars=enforceBars;
        this.enforceNewLines=enforceNewLines;
        //count the number of special characters in the sample string for input validation
        this.numSpaces=countCharacter(example,' ');
        this.numNewLines=countCharacter(example,'\n');
        this.numBars=countCharacter(example,'|');
        
        //set multiple prompt and alternate form fields
        this.multiplePrompt=multiplePrompt;
        this.alternateForm=alternateForm;
        
    }
    
    /**
     * validates user input for one of the ArduinoFields
     * returns null if input is correct and error message if it is not
     */
    public String validate(String input){
    	//check the number of special characters
    	String specialCharsCheck=enforceSpecialCharacters(input);
    	//if there is an alternate form, check to see if the input matches it
    	if(alternateForm!=null && input.equals(alternateForm)){
    		//if true, then re
    		return null;
    	}
    	//if the user entered null but there is no allowed alternate form, return error message
    	if(input.equals("null") && alternateForm==null){
    		return "This field cannot be null";
    	//if user entered null and the alternate form isn't null, return error message
    	}else if(input.equals("null") && !alternateForm.equals("null")){
    		return "This field cannot be null";
    	}
    	//otherwise, just check special characters
    	return specialCharsCheck;
    	
    }
    
    /**
     * returns whether this field needs to be done in multiple stages
     */
    public boolean getMultiplePrompt(){
    	return multiplePrompt;
    }
    
    
    /**
     * enforces the number of special characters in an input string
     * @ return null if input is valid and an error message if not
     */
    private String enforceSpecialCharacters(String input){
    	//count up the number of special characters
    	int inputSpaces=countCharacter(input,' ');
    	int inputBars=countCharacter(input,'|');
    	int inputNewLines=countCharacter(input,'\n');
    	//check if number of special characters in input matches number of special characters in correct example
    	if(enforceSpaces && inputSpaces !=numSpaces){
    		return "Error, Expected "+numSpaces+" spaces and you entered "+inputSpaces;
    	}
    	if(enforceBars && inputBars!=numBars){
    		return "Error, Expected "+numBars+" bars and you entered "+inputBars;
    	}
    	if(enforceNewLines && inputNewLines!=numNewLines){
    		return "Error, Expected "+numNewLines+" newlines and you entered "+inputNewLines;
    	}
    	return null;
    }

    /**Displays capabilities of the enum
     * */
    public static void main(String[] args) {
        System.out.println("Prints out the different enums, prompts, and formatting contained in this class");
        String field;
        for (ArduinoClassExample a : ArduinoClassExample.values()){
        	//print name of enum
        	System.out.println(a.name()+":");
        	//print field of enum with tabs for nice printing
        	field=a.toString();
        	field=ArduinoParser.insertTabs(field, 1, true);
        	System.out.println(field);
        }
        System.out.println("Shows how enum can be used to validate user input");
        System.out.println("INPUT\tVALIDATION RESPONCE");
        //too many newlines
        String className="Timer\n";
        String result=ArduinoClassExample.CLASSNAME.validate(className);
        System.out.print(className+":"+result);
        //too many spaces
        className="Timer ";
        result=ArduinoClassExample.CLASSNAME.validate(className);
        System.out.print(className+":"+result);
        //too many bars
        className="Timer|";
        result=ArduinoClassExample.CLASSNAME.validate(className);
        System.out.print(className+":"+result);
        
        
    }
    
    /**
     * returns the number of a character in the base string
     * useful for determining how many special characters to enforce
     */
    private static int countCharacter(String base,char c){	
    	//if the string is null, there are no instances of the character
    	if(base==null){
    		return 0;
    	}
    	//otherwise, return the number of character cs in the string
    	int seqCount=0; 
    	for(int i=0;i<base.length();i++){
    		if(base.charAt(i)==c){
    			seqCount++;
    		}
    	}
    	return seqCount;
    }
    
    /**returns a string representation of the enum, with name, prompt, and format
     * */
    public String toString(){
    	return example;
    }
}