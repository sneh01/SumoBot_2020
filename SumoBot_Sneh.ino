//********************************************************Sumobot 2020 Code Sneh's Strategy********************************************
//*******************************************************Initiataion Information****************************************
#include <Zumo32U4.h>

Zumo32U4LCD lcd;
Zumo32U4ProximitySensors proxsensors;
Zumo32U4LineSensors lineSensors;
Zumo32U4Motors motors;
Zumo32U4ButtonA bA;#
define RIGHT 1# define LEFT 0# define NUM_SENSORS 5
uint16_t lineSensorValues[NUM_SENSORS];
uint8_t selectedSensorIndex = 0;
void setup() {
  lcd.clear();
  lcd.print(F("Sneh")); //************************************(initial display)*****************************************************
  bA.waitForButton();
  delay(5000); //************************************(button delay time)***************************************************
  ledRed(1);
  ledYellow(0);
  ledGreen(0);
  lcd.clear();
  proxsensors.initThreeSensors();
  lineSensors.initFiveSensors();
  loadCustomCharacters();
}
void loadCustomCharacters() {
  static
  const char levels[] PROGMEM = {
    0,
    0,
    0,
    0,
    0,
    0,
    0,
    63,
    63,
    63,
    63,
    63,
    63,
    63
  };
  lcd.loadCustomCharacter(levels + 0, 0); // 1 bar
  lcd.loadCustomCharacter(levels + 1, 1); // 2 bars
  lcd.loadCustomCharacter(levels + 2, 2); // 3 bars
  lcd.loadCustomCharacter(levels + 3, 3); // 4 bars
  lcd.loadCustomCharacter(levels + 4, 4); // 5 bars
  lcd.loadCustomCharacter(levels + 5, 5); // 6 bars
  lcd.loadCustomCharacter(levels + 6, 6); // 7 bars
}
void printBar(uint8_t height) {
    if (height > 8 {
        height = 8;
      }
      const char barChars[] = {
        ' ',
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        255
      }; lcd.print(barChars[height]);
    }
    void printReadingsToLCD() {
      // On the first line of the LCD, display the bar graph.
      lcd.gotoXY(0, 0);
      for (uint8_t i = 0; i < 5; i++) {
        uint8_t barHeight = map(lineSensorValues[i], 0, 2000, 0, 8);
        printBar(barHeight);
      }
      // On the second line of the LCD, display one raw reading.
      lcd.gotoXY(0, 1);
      lcd.print(selectedSensorIndex);
      lcd.print(F(": "));
      lcd.print(lineSensorValues[selectedSensorIndex]);
      lcd.print(F("    "));
    }
    // Prints a line with all the sensor readings to the serial
    // monitor.
    void printReadingsToSerial() {
      char buffer[80];
      sprintf(buffer, "%4d %4d %4d %4d %4d %c\n",
        lineSensorValues[0],
        lineSensorValues[1],
        lineSensorValues[2],
        lineSensorValues[3],
        lineSensorValues[4]
      );
      Serial.print(buffer);
    }
    // *********************Above is all initiation stuff (initiation ends here)*************************************************
    // *****************read linesensors (may need to change based on table sumobot is on)***************************************
    // best way to change line sensor value is to use the line sensor test to get readings
    void linesensors() {
      if (lineSensorValues[selectedSensorIndex] >= 1000) { // change the 1900 based on value read of table (table must be SOLID COLOR TABLE)
        motors.setSpeeds(-200, -200);
        delay(1000);
        motors.setSpeeds(-200, 200); // go to middle and turn 180 degrees
        delay(800);
      }
    }
    //**************************main loop****************************************************************************************
    void loop() {
      //************************Get initial readings every time iterating through loop ******************************
      proxsensors.read();
      int left = proxsensors.countsLeftWithLeftLeds();
      int center_left = proxsensors.countsFrontWithLeftLeds();
      int center_right = proxsensors.countsFrontWithRightLeds();
      int right = proxsensors.countsRightWithRightLeds();
      // prints the values of the prox sensors on the LCD screen
      lcd.gotoXY(0, 1);
      lcd.print(left);
      lcd.print(" ");
      lcd.print(center_left);
      lcd.print(" ");
      lcd.print(center_right);
      lcd.print(" ");
      lcd.print(right);
      lcd.print(" ");
      static uint16_t lastSampleTime = 0;
      if ((uint16_t)(millis() - lastSampleTime) >= 100) {
        lastSampleTime = millis();
        // Read the line sensors.
        lineSensors.read(lineSensorValues);
        // Send the results to the LCD and to the serial monitor.
        printReadingsToLCD();
        printReadingsToSerial();
      }
      //***********************Call linesensors***********************************************************************************
      linesensors();
      
      
      //*********************Main Strategy Comments*******************************************************************************
      //Line sensors read from 0 to 2000 based of wavelength of colr
      // 2000 = black
      // 200 = white
      // 2000= "air"
      //Proximity sensors read from 0 to 6
      // 6 = extremely close
      // 0 = nothing in range
      // This is part of code to change for 2020 
      // incorporate line sensors and proximity sesnors to determine motor speeds
      // proximity sensors: seenleft, seenright,seenfront,attackfront return boolean value of True or False (look below for more info on this)
      // motors: motors.setSpeeds(left,right) is used to change speed and direction of motors (max speed is +/- 400)
      //         if you do motors.setSpeeds(left,0) or motors.setSpeeds(0,right) this will allow the sumobot to turn
      // line sensors: lineSensors.read(lineSensorValues) will read the linesensor values from 0 to 2000
      // watch syntax for if and for loops
      
      
      //******************************Booleans that could be used in code*********************************************************
      // proximity sensors
      // for these booleans change number values to what you want for closeness of opponent
      bool seenfront = center_left >= 4 || center_right >= 4; //this checks to see if the robot sees anyone in the front
      bool seenright = right >= 2; //this checks to see if the robot sees anyone in the right
      bool seenleft = left >= 2; //this checks to see if the robot sees anyone in the left
      bool attackfront = center_left == 5 || center_right == 5; //this checks to see if the robot sees anyone in the front, and attacks
      // line sensors 
      // can be used to reset motors if white line read
      // this can be a little confusing so ask Akash or Sneh if need help
      // write if statments based on the booleans above to make a strategy
      //example given below of strategy
      
      
      
      //***************************************Sneh's Strategy*********************************************************************
      //--------------------------------------------Scan first-----------------------------------------
      motors.setSpeeds(-150, 150); //scan until see other sumobot
      //--------------------------------------check in front for other sumobot-------------------------
      if (center_left >= 3 || center_right >= 3) {
        motors.setSpeeds(150, 150);
        if (center_left >= 4 || center_right >= 4) //increase speeds as the other sumobot is in the front of us get closer
        {
          motors.setSpeeds(250, 250);
          if (center_left >= 5 || center_right >= 5) {
            motors.setSpeeds(370, 370);
            linesensors();
            if (center_left >= 6 || center_right >= 6) {
              motors.setSpeeds(400, 400);
              linesensors();
            }
          }
        }
      }
      
      
      //-----------------------------------check on right for other sumobot----------------------------
      if (right >= 4) //turn our sumobot right if seen on the right and then increase speeds as the other sumobot is in the front of us
      {
        motors.setSpeeds(400, 0);
        linesensors();
        delay(200);
        if (center_left >= 3 || center_right >= 3) {
          motors.setSpeeds(150, 150);
          if (center_left >= 4 || center_right >= 4) //increase speeds as the other sumobot is in the front of us get closer
          {
            motors.setSpeeds(250, 250);
            if (center_left >= 5 || center_right >= 5) {
              motors.setSpeeds(350, 350);
              linesensors();
              if (center_left >= 6 || center_right >= 6) {
                motors.setSpeeds(400, 400);
                linesensors();
              }
            }
          }
        }
      }
      
      
      //-----------------------------------check on left for other sumobot----------------------------
      else if (left >= 4) //turn our sumobot left if seen on the left and then increase speeds as the other sumobot is in the front of us
      {
        motors.setSpeeds(0, 400);
        delay(200);
        if (center_left >= 3 || center_right >= 3) {
          motors.setSpeeds(150, 150);
          if (center_left >= 4 || center_right >= 4) //increase speeds as the other sumobot is in the front of us get closer
          {
            motors.setSpeeds(250, 250);
            if (center_left >= 5 || center_right >= 5) {
              motors.setSpeeds(350, 350);
              linesensors();
              if (center_left >= 6 || center_right >= 6) {
                motors.setSpeeds(400, 400);
                linesensors();
              }
            }
          }
        }
      }
      
      
      //-------------------if other sumobot not seen, then scan until detect other sumobot------------
      else // if nothing seen this is what the sumobot will do
      {
        motors.setSpeeds(70, 150); // to circle around to find opponent 
      }
    }
