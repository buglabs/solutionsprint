#include <Servo.h> 
#include <stdlib.h>
#include <stdio.h>
#define MAX_DIGITS 5

char buff[MAX_DIGITS];
int pos = 0, num;


Servo myservo;
char ch0, ch1, ch2;

void setup()    {  

    myservo.attach(9);
    Serial.begin(115200);
    memset(buff, 0, MAX_DIGITS);
    Serial.println("Ready");
    myservo.write(0); // set servo to 0 (range 0-180)
    
}

void loop() 
{ 
  
   if (Serial.available()){
      buff[pos++] = Serial.read();
    
   }
   
    if (pos > MAX_DIGITS-1){
     
      Serial.println("Must be a number between 0-180");
      pos = 0;
    }
      
    if (buff[0] == '\r'){
      pos=0;
      }
      
     else if ((buff[pos-1] == '\r')||(buff[pos-1] == '\n')||(buff[pos-1] == '.')){
      //We have hit a return!
      
        int num = atoi(buff);
      
        if ((num > 180 )||(num < 0)){
        
          Serial.println("Must be a number between 0-180");
          pos=0;
        }
      
        else {
      
          myservo.write(num);
          Serial.println(num);
          memset(buff, 0, MAX_DIGITS);
          pos=0;
        }
      }
}
  

 
   



