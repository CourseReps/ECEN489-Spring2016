#include <XBee.h>

//#define HWSERIAL Serial3
const int ledPin = 13;
unsigned long baud = 9600;
uint8_t randNumber;
XBee xbee = XBee();
uint8_t TXDeviceID=78;


void setup() {
  // put your setup code here, to run once:
  Serial3.begin(baud);
  Serial.begin(baud);  // USB, communication to PC or Mac
  //HWSERIAL.begin(baud); // communication to hardware serial
  xbee.setSerial(Serial3);
  pinMode(ledPin, OUTPUT);
  randomSeed(analogRead(0)); 
}

void loop() {

  // put your main code here, to run repeatedly:
  randNumber = random(254);
  Tx16Request Tx16;
  //ZBTxRequest Tx64;
  //Tx64Request Tx64;
  
  uint8_t payload[] = { randNumber, TXDeviceID };

  //XBeeAddress64 addr64 = XBeeAddress64(0x0013A200, 0x40C556C5);
  //XBeeAddress64 addr64 = XBeeAddress64(0x00000000, 0x0000ffff);

  Tx16 = Tx16Request(0x0817, payload, sizeof(payload));
  //Tx64 = ZBTxRequest(addr64, payload, sizeof(payload));
  //Tx64 = Tx64Request(addr64, payload, sizeof(payload));
  //TxStatusResponse txStatus = TxStatusResponse();

  //xbee.send(Tx64);
  xbee.send(Tx16);
  
 /*
  if (txStatus.getStatus() == SUCCESS) {
              // success.  time to celebrate
              Serial.println("sucessfully transfer");
              Serial.print("Random Number: ");
              Serial.println(randNumber);
           } else {
              // the remote XBee did not receive our packet. is it powered on?
             Serial.println("failed");
           }
 */

  digitalWrite(ledPin, HIGH);   // set the LED on
  delay(100);                  // wait for a second
  digitalWrite(ledPin, LOW);    // set the LED off
  delay(100);                  // wait for a second
  
}
