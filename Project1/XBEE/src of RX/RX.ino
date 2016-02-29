#include <XBee.h>
//#define MAX_FRAME_DATA_SIZE 110

XBee xbee = XBee();


//ZBRxResponse rx64 = ZBRxResponse();
Rx16Response rx16 = Rx16Response();
//Rx64Response rx64 = Rx64Response();

unsigned long baud = 9600;
int TXDeviceID;
int RXDeviceID=87;
int rssi=0;
int randNumber=0;
const int ledPin = 13;
const int digitalPin= 4;



void setup() {
  // put your setup code here, to run once:

  Serial.begin(baud);
  Serial3.begin(baud);
  xbee.setSerial(Serial3);
  pinMode(ledPin, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  xbee.readPacket();

  int rd=1;

  rd=xbee.getResponse().isAvailable();
  xbee.getResponse().getRx16Response(rx16);
  //xbee.getResponse().getRx64Response(rx64);
  //xbee.getResponse().getZBRxResponse(rx64);

  randNumber = rx16.getData(0);
  TXDeviceID=rx16.getData(1);
  rssi=rx16.getRssi();
  
  Serial.print("Get package?  ");
  if(rd==1){
  Serial.println("--Yes!");}
  else {Serial.println("--No!");}
  Serial.print("random number:");
  Serial.println(randNumber);  
  Serial.print("RSSI: ");
  Serial.println(rssi,HEX);
  Serial.print("TX_Device_ID: ");
  Serial.println(TXDeviceID);
  Serial.print("RX_Device_ID: ");
  Serial.println(RXDeviceID);
  /*
  if (xbee.getResponse().isAvailable()) {
        if ( xbee.getResponse().getApiId() == RX_64_RESPONSE) {
          xbee.getResponse().getRx64Response(rx64);
          randNumber = rx64.getData(0);
          TXDeviceID=rx64.getData(1);
          rssi=rx64.getRssi();

        }else{ 
          
  digitalWrite(ledPin, HIGH);   // set the LED on
  delay(10);                  // wait for a second
  digitalWrite(ledPin, LOW);    // set the LED off
  delay(10);                  // wait for a second
  }
  }
  */
  
  digitalWrite(ledPin, HIGH);   // set the LED on
  delay(100);                  // wait for a second
  digitalWrite(ledPin, LOW);    // set the LED off
  delay(100);                  // wait for a second 

}
   
