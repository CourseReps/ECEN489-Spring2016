#include <ArduinoJson.h>

unsigned long baud = 9600;

long timeout=0, commtimeout=0;
const int ledPin = 13;
const int ioPinP = 11;
const int ioPinN = 12;
const int digitalPin= 4;


char rxbuffer[10];
int buffer_index = 0;
int wait_for_RSSI = 0;
int wait_for_ok = 0;
int prevByteA = 0;
int prevByteB = 0;
int data_count = 0;
int line_count = 0;

int rssi=0;
int TXDeviceID;
int RXDeviceID=91;
int randNumber=0;

void setup() {
  // put your setup code here, to run once:

  Serial.begin(baud);
  Serial3.begin(baud);
  pinMode(ledPin, OUTPUT);
  pinMode(ioPinP, OUTPUT);
  pinMode(ioPinN, OUTPUT);
  digitalWrite(ioPinN, HIGH);   // Set 10
  digitalWrite(ioPinP, LOW);    // Set 10

}

void loop() {
  if (Serial.available() > 0)
  {
      int incomingByte = Serial.read();
      if (incomingByte == '+')
      {
          digitalWrite(ioPinN, LOW);     // Set 01
          digitalWrite(ioPinP, HIGH);    // Set 01
      }
      else
      {
          digitalWrite(ioPinN, HIGH);   // Set 10
          digitalWrite(ioPinP, LOW);    // Set 10
      }
  }
  
  if (Serial3.available() > 0)
  {
    int incomingByte;
      // read the incoming byte and store into buffer
      incomingByte = Serial3.read();
      rxbuffer[buffer_index++] = incomingByte;
      if (buffer_index >= 9) buffer_index = 9;

      //Serial.write(incomingByte);
      //if (incomingByte == 0x0D) Serial.write(0x0A);
      
      // If CR process data, reset index
      if ((incomingByte == 0x0A) | (incomingByte == 0x0D))
      {
        if (wait_for_ok == 1)
        {
          timeout++;
          if ((prevByteA == 'O') & (prevByteB == 'K')) 
          {
            wait_for_RSSI = 1;
            wait_for_ok = 0;
            timeout = 0;
            buffer_index = 0;
            
            // RS for 900 MHz S3B
            // DB for 2.4 GHz S1
            //Serial.println("GOT OK - RS");
            Serial3.print("ATRS\r");
            //Serial3.print("ATDB\r");
          }
          if (timeout > 15)
          {
            //Serial.println("TIMEOUT");
            timeout = 0;
            buffer_index = 0;
            wait_for_ok = 0;
            wait_for_RSSI = 0;
          }
        }
        else if (wait_for_RSSI == 1)
        {
            StaticJsonBuffer<200> jsonBuffer;
            JsonObject& root = jsonBuffer.createObject();

            rssi = 0;
            if ((rxbuffer[0] >= 0x30) & (rxbuffer[0] <= 0x39)) rssi = (rxbuffer[0] - 0x30) << 4; 
            else if ((rxbuffer[0] >= 0x41) & (rxbuffer[0] <= 0x46))  rssi = (rxbuffer[0] - 0x37) << 4; 
            if ((rxbuffer[1] >= 0x30) & (rxbuffer[1] <= 0x39)) rssi |= (rxbuffer[1] - 0x30); 
            else if ((rxbuffer[1] >= 0x41) & (rxbuffer[1] <= 0x46))  rssi |= (rxbuffer[1] - 0x37); 
            
            rssi = (rxbuffer[0] - 0x30) << 4; 
            rssi |= (rxbuffer[1] - 0x30); 

            root["Transmit ID"] = TXDeviceID;
            root["RSSI"] = rssi;
            root["Receive ID"] = RXDeviceID;
            root["Rand"] = randNumber;
            root.prettyPrintTo(Serial);

            timeout = 0;
            data_count = 0;
            buffer_index = 0;
            wait_for_ok = 0;
            wait_for_RSSI = 0;
            //Serial.println("");
            //Serial.print("RSSI = ");
            //Serial.println(rssi, HEX);
            delay(100);
            Serial3.print("ATCN\r");
        }
        else if (rxbuffer[0] == '$')
        {
          TXDeviceID = 0;
          if ((rxbuffer[2] >= 0x30) & (rxbuffer[2] <= 0x39)) TXDeviceID = (rxbuffer[2] - 0x30) << 4; 
          else if ((rxbuffer[2] >= 0x41) & (rxbuffer[2] <= 0x46))  TXDeviceID = (rxbuffer[2] - 0x37) << 4; 
          if ((rxbuffer[3] >= 0x30) & (rxbuffer[3] <= 0x39)) TXDeviceID |= (rxbuffer[3] - 0x30); 
          else if ((rxbuffer[3] >= 0x41) & (rxbuffer[3] <= 0x46))  TXDeviceID |= (rxbuffer[3] - 0x37); 

          randNumber = 0;
          if ((rxbuffer[5] >= 0x30) & (rxbuffer[5] <= 0x39)) randNumber = (rxbuffer[5] - 0x30) << 4; 
          else if ((rxbuffer[5] >= 0x41) & (rxbuffer[5] <= 0x46))  randNumber = (rxbuffer[5] - 0x37) << 4; 
          if ((rxbuffer[6] >= 0x30) & (rxbuffer[6] <= 0x39)) randNumber |= (rxbuffer[6] - 0x30); 
          else if ((rxbuffer[6] >= 0x41) & (rxbuffer[6] <= 0x46))  randNumber |= (rxbuffer[6] - 0x37);

          timeout = 0;
          wait_for_RSSI = 0;
          data_count++;
          //Serial.println("DATA");
          if (data_count > 10)
          {
            wait_for_ok = 1;
            //Serial.println("***");
            Serial3.print("+++");
            data_count = 0;
          }
        }
        buffer_index = 0;
      }

      // Two byte fifo
      prevByteA = prevByteB;
      prevByteB = incomingByte;
      commtimeout = 0;
  }
  else
  {
      commtimeout++;
      if (commtimeout > 100000) 
      {
        StaticJsonBuffer<200> jsonBuffer;
        JsonObject& root = jsonBuffer.createObject();
        commtimeout = 0;
        rssi = 9999;
        root["Transmit ID"] = TXDeviceID;
        root["RSSI"] = rssi;
        root["Receive ID"] = RXDeviceID;
        root["Rand"] = randNumber;
        root.prettyPrintTo(Serial);
      }
  }


  //digitalWrite(ledPin, HIGH);   // set the LED on
  //delay(100);                  // wait for a second
  //digitalWrite(ledPin, LOW);    // set the LED off
  //delay(100);                  // wait for a second 
  //timeout++;

}

