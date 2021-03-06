package tryakash;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.*;
import de.fhpotsdam.unfolding.providers.Google.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.llrp.ltk.generated.messages.RO_ACCESS_REPORT;
import org.llrp.ltk.generated.parameters.TagReportData;
import org.llrp.ltk.net.LLRPEndpoint;
import org.llrp.ltk.types.LLRPMessage;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
//import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

 
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import controlP5.*;

public class ECEN689Project1Main extends PApplet {//implements LLRPEndpoint {
	private static RFFieldSQLDatabase RFFieldDatabase = new RFFieldSQLDatabase();     
	
	public static HashMap<String, RFData> globalRFDataDump = new HashMap<String, RFData>();
	public static int recordID = 1;
	public static double distanceMatrix[][];
	ControlP5 controlP5;
	Textfield p5Lat;
	Textfield p5Lng;
	Textlabel rSSILabel;
	Button p5Button;
	//coordinates of the transmitter EIC by default
	public static float transLat = 30.6184134f;
	public static float transLng =-96.3437105f;
	
	public static int rssiCalcKey = 1234;
	public static float toBeCalculatedRSSI =0.0f;
    
	public static void main(String args[]) {
	    PApplet.main(new String[] { "--present", "tryakash.ECEN689Project1Main" });
		System.out.println("RFID Program");		
		 
	  }
	
	
	static void addData()
	{
		//add data to the HashMap from the SQL
		//http://lusherengineeringservices.com
		RFFieldDatabase.ConnectToDatabase("lusherengineeringservices.com");
		
		 Gson gson = new GsonBuilder().create();                         // Create Gson builder
	       
		String returnJSON = RFFieldDatabase.ListDataByGeoArea(0, 0, 0, 0);
		 ArrayList records = gson.fromJson(returnJSON, ArrayList.class); 
		  
		 // Convert from JSON to Array List
	     Iterator itr = records.iterator();                              // Build an Iterator for the Array List
	     
	     while(itr.hasNext())                                            // Loop through the list
	     {              
	    	 RFData tmp = new RFData();// Entries are JSON strings
	         Object element = itr.next();                                // Get the next object
	                                                                     // Convert from JSON to Array List
	         RFData RFMember = gson.fromJson((String)element, RFData.class);
	                                                                     // Print debug information to port
	         SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	         tmp.SampleNumber = RFMember.SampleNumber;
	                                                     // Default is -1 (undefined)
	         tmp.XbeeID = RFMember.XbeeID;                                                    // Default is -1 (undefined)
	         tmp.DeviceID = RFMember.DeviceID;                                                  // Default is -1 (undefined)
	         tmp.RSSI = RFMember.RSSI;
	         tmp.Latitude = RFMember.Latitude;
	         tmp.Longitude =RFMember.Longitude;
	         tmp.Yaw = RFMember.Yaw;
	         tmp.Pitch = RFMember.Pitch;
	         tmp.Roll = RFMember.Roll;
	         tmp.SampleDate = RFMember.SampleDate;
	         addToHashmap(tmp);
	        // System.out.println("Connection to dbase");
	         System.out.println("Got Record: # " + RFMember.SampleNumber + " - XbeeID: " + RFMember.XbeeID + ", RSSI: " + RFMember.RSSI + " Date/Time: " + ft.format(RFMember.SampleDate));
		       
	     }

	     
	     
	     
	     RFFieldDatabase.DisconnectDatabase();    
		
		
		
		
		//from John
		RFData testmember = new RFData();                               // Create test RF data
		testmember.SampleNumber =1;
        testmember.XbeeID = 456;                                        // Fill test with dummy data
        testmember.DeviceID = 1234;
        testmember.Latitude = 30.618227F;//30.618227, -96.342216
        testmember.Longitude = -96.342216F;
        testmember.RSSI = 90F;
        testmember.Yaw = 10;
        testmember.Pitch = 20;
        testmember.Roll = 30;
        testmember.SampleDate = new Date();
        
        RFData testmember2 = new RFData();                               // Create test RF data
		testmember2.SampleNumber =2;
        testmember2.XbeeID = 456;                                        // Fill test with dummy data
        testmember2.DeviceID = 1234;
        testmember2.Latitude = 30.619113F; 
        testmember2.Longitude = -96.340939F;
        testmember2.RSSI = 40F;
        testmember2.Yaw = 9;
        testmember2.Pitch = 21;
        testmember2.Roll = 32;
        testmember2.SampleDate = new Date();
        
        
        
        RFData testmember3 = new RFData();                               // Create test RF data
		testmember3.SampleNumber =3;
        testmember3.XbeeID = 456;                                        // Fill test with dummy data
        testmember3.DeviceID = 1234;
        testmember3.Latitude =30.620821F;
        testmember3.Longitude = -96.341304F;
        testmember3.RSSI = 20F;
        testmember3.Yaw = 9;
        testmember3.Pitch = 21;
        testmember3.Roll = 32;
        testmember3.SampleDate = new Date();
        
        
        RFData testmember4 = new RFData();                               // Create test RF data
		testmember4.SampleNumber =4;
        testmember4.XbeeID = 456;                                        // Fill test with dummy data
        testmember4.DeviceID = 1234;
        testmember4.Latitude = 30.619648F; 
        testmember4.Longitude = -96.342076F;
        testmember4.RSSI = 65F;
        testmember4.Yaw = 9;
        testmember4.Pitch = 21;
        testmember4.Roll = 32;
        testmember4.SampleDate = new Date();
        
        
        

                 // Convert to JSON and store in string
       // jsondata2 = gson.toJson(testmember2);
        //System.out.println(jsondata);
        //System.out.println(jsondata2);
      //  addToHashmap(testmember);
       // addToHashmap(testmember2);
        //addToHashmap(testmember3);
        //addToHashmap(testmember4);
        
        
        
        //push the data
        
	}
	
	public static void addToHashmap(RFData data) {
        
      
        
        globalRFDataDump.put(String.valueOf(data.SampleNumber), data);
        
        
    }
	
//30.623642, -96.347318
    UnfoldingMap map;
     
    public void setup() {
    	controlP5 = new ControlP5(this);
    	p5Button = controlP5.addButton("Calc RSSI").setValue(10).setPosition(20,20)
        .setSize(50,20).setId(1).addCallback(new CallbackListener() {
            public void controlEvent(CallbackEvent event) {
                if (event.getAction() == ControlP5.ACTION_RELEASED) {
                  System.out.println("button clicked.");
                  rSSILabel.setText(" ");
                 
                  rSSILabel.setText(Float.toString(toBeCalculatedRSSI));
                  float rssiCalc = rssiEstimation(Float.parseFloat(p5Lat.getText()),Float.parseFloat(p5Lng.getText()));
                  int sam[] = findNearestThreePoints(Float.parseFloat(p5Lat.getText()),Float.parseFloat(p5Lng.getText()));
                  rSSILabel.setText(Float.toString(toBeCalculatedRSSI));
                  //p5Button.setColorValue(color(255, 0, 0));
                  controlP5.remove(event.getController().getName());
                  System.out.println("akash ids: "+Arrays.toString(sam));
                  
                }
            }
        }
        );
    	p5Lat = controlP5.addTextfield("Lat",150,20,30,20);
    	p5Lng = controlP5.addTextfield("Long",300,20,30,20);
        rSSILabel = controlP5.addLabel("RSSI", 400, 25); //replace by ans when you click button
        
    	
    	
    	
    	
    	//controlp5 ends
        size(800, 600, OPENGL);
        map = new UnfoldingMap(this, 50, 50, 700, 500, new Google.GoogleMapProvider());
        MapUtils.createDefaultEventDispatcher(this, map);

        
        
    	addData();
    	addDistanceMatrix();
    	
		Iterator<Map.Entry<String, RFData>> i = globalRFDataDump.entrySet().iterator();
		 
		 //System.out.println("distance b/w the 2 points is "+getDistance(globalRFDataDump.get("1").Latitude, globalRFDataDump.get("1").Longitude, globalRFDataDump.get("2").Latitude, globalRFDataDump.get("2").Longitude));
		 // create a distance matrix
		 
		  
		 //
		 String key;
		 Location loc; 
		 SimplePointMarker pointMarker;
	       
	        while(i.hasNext()){
	        	 key = i.next().getKey();
	             System.out.println("SampleNumber:"+key+", loc: "+globalRFDataDump.get(key).Latitude+","+globalRFDataDump.get(key).Longitude+" ,RSSI:"+globalRFDataDump.get(key).RSSI);
	             loc = new Location(globalRFDataDump.get(key).Latitude, globalRFDataDump.get(key).Longitude);
	             pointMarker = new SimplePointMarker(loc);
	             
	            // if(globalRFDataDump.get(key).RSSI > 101) {
	             float maxRSSI = findMaxRSSI();
	            	 pointMarker.setColor(color((int)(255*globalRFDataDump.get(key).RSSI/maxRSSI), 255*(1-globalRFDataDump.get(key).RSSI/maxRSSI), 0, 150));
	            	// pointMarker.setStrokeWeight((int)5*globalRFDataDump.get(key).RSSI);
	            	 int stroke = (int) (10*globalRFDataDump.get(key).RSSI/maxRSSI);
	            	 pointMarker.setRadius(45*globalRFDataDump.get(key).RSSI/maxRSSI);
	            	// pointMarker.setStrokeColor(arg0);
	            	// pointMarker.setStrokeWeight(stroke);
	            	 map.addMarker(pointMarker);
	          //   }
	             
	            	
	            //  loc = new Location(lifeExpMap.get(key).x, lifeExpMap.get(key).y);
	             int zoomLevel = 15;
	     	    map.zoomAndPanTo(zoomLevel, new Location(globalRFDataDump.get(key).Latitude, globalRFDataDump.get(key).Longitude));
	     	   
	            
	        }
	        
	       
	       // System.exit(0);
	        
        	
        }
       
    

   


	private float findMaxRSSI() {
		// TODO Auto-generated method stub
		float max = 0.0f;
		float tmpArray[] = new float[globalRFDataDump.size()]; 
		 Set<String> keys = globalRFDataDump.keySet(); 
		 int i = 0;
		 for(String k: keys)
    	 {
    		tmpArray[i] = globalRFDataDump.get(k).RSSI;
    		i++;
    	 }
		  
		  
		 Arrays.sort(tmpArray);
		  max = tmpArray[tmpArray.length - 1];
		 
		 
		return max;
	}


	private void addDistanceMatrix() {
    	 int hSize = globalRFDataDump.size();
    	 System.out.println("Printing the distance Matrix of size "+hSize);
    	//String tmp;
    	 distanceMatrix = new double[hSize][hSize];
    	 
    	 int i=0,j = 0;
    	 
    	 Set<String> keys = globalRFDataDump.keySet();  //get all keys
    	 for(String k: keys)
    	 {
    		 j=0;
    		 for(String k2: keys)
        	 {
    			 if(k.equals(k2)){
    				 distanceMatrix[i][j] = 0;
    			 }
    			 else
    				 {
    				 	
    				 	distanceMatrix[i][j] = getDistance(globalRFDataDump.get(k).Latitude, globalRFDataDump.get(k).Longitude, globalRFDataDump.get(k2).Latitude, globalRFDataDump.get(k2).Longitude);
    				 }
    			 j++;
        	 
        	 }
    	     //System.out.println(globalRFDataDump.get(k));
    		 i++;
    	 }
    	 
    	 
    	/* Iterator it = globalRFDataDump.entrySet().iterator();
    	 Iterator it2 = globalRFDataDump.entrySet().iterator();
    	    while (it.hasNext()) {
    	        Map.Entry pair = (Map.Entry)it.next();
    	       // System.out.println(pair.getKey() + " = " + pair.getValue());
    	        for(int j=0;j<hSize;j++)
       		 	{
    	        	Map.Entry pair2 = (Map.Entry)it2.next();
    	        	  
       			 if(pair.getKey().toString().equals(pair2.getKey().toString())){
       				 distanceMatrix[i][j] = 0;
       			 }
       			 else
       				 {
       				 	
       				 	distanceMatrix[i][j] = getDistance(globalRFDataDump.get(pair.getKey().toString()).Latitude, globalRFDataDump.get(pair.getKey().toString()).Longitude, globalRFDataDump.get(pair2.getKey().toString()).Latitude, globalRFDataDump.get(pair2.getKey().toString()).Longitude);
       				 }
       			 i++;
       			 it2.remove();
       		 }
    	        it.remove(); // avoids a ConcurrentModificationException
    	    }*/
    	 
    	 
    	 
    	    
    	    /*
    	 for( i=0;i<hSize;i++)
    	 {
    		 for(int j=0;j<hSize;j++)
    		 {	
    			 if(i==j){
    				 distanceMatrix[i][j] = 0;
    			 }
    			 else
    				 {
    				 	
    				 	distanceMatrix[i][j] = getDistance(globalRFDataDump.get(Integer.toString(i+1)).Latitude, globalRFDataDump.get(Integer.toString(i+1)).Longitude, globalRFDataDump.get(Integer.toString(j+1)).Latitude, globalRFDataDump.get(Integer.toString(j+1)).Longitude);
    				 }
    		 }
    	 }
    	 */
    	 
    	 for( i=0;i<hSize;i++)
    	 {
    		 //System.out.println(i+": ");
    		 for( j=0;j<hSize;j++)
    		 {	
    			 //System.out.print(j+" ");
    			 System.out.format("%10f,%10s",distanceMatrix[i][j], "     ");
    			 
    		 }
    		 System.out.println("");
    	 }
    	 
		
	}


	public void draw() {
        // Draw map tiles and country markers
		background(0);
        map.draw();
        
        
        String p5StringLat = p5Lat.getText(); 
        String p5StringLng = p5Lng.getText(); 
        text(p5StringLat,400,20);
        text(", "+p5StringLng, 480,20);
       // rSSILabel
    	//System.out.println("draw called");
       
      /*  do{
        	System.out.println("Enter lat long to find RSSI ");
        	lat = sc.nextFloat();
        	lng = sc.nextFloat();
        	System.out.println("The RSSI value is "+calcDistance(lat,lng));
        }while(lat != -1);
        */
        
    }
    
    
    
    
    


	double rad( double x) {
    	  return (double) (x * Math.PI / 180);
    	}
    
    
   /* class location{
    	public double lat, lng;
    	 
    	public location(double lat, double lng){
    	this.lat = lat;
    	this.lng = lng;
    		
    	}
    }
    */
	
	

    double getDistance (float lat1, float lng1, float lat2, float lng2) {
    	  int R = 6378137; // Earth’s mean radius in meter
    	  double dLat = rad(lat2 - lat1);
    	  double dLong = rad(lng2 - lng1);
    	  double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    	    Math.cos(rad(lat1)) * Math.cos(rad(lat2)) *
    	    Math.sin(dLong / 2) * Math.sin(dLong / 2);
    	  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    	  double d = R * c;
    	  return d; // returns the distance in meter
    	}
    
    
    
    
    int[] findNearestThreePoints(float nLat, float nLng){
    	int ids[] ={0, 0, 0};
    	
    if( globalRFDataDump.size()>3){
    	RFData threePoints = new RFData();
    	threePoints.SampleNumber = rssiCalcKey;
    	threePoints.Latitude = nLat;
    	threePoints.Longitude = nLng;
    	
    			
    	globalRFDataDump.put(Integer.toString(rssiCalcKey), threePoints);
    	System.out.println("The nearest 3 points from the coordinates are ");
    	
    	
    	addDistanceMatrix(); //updated with the new coordinate
    	int hmSize = globalRFDataDump.size();
    	double sortDistance[] = new double[hmSize]; 
    	    	
    	HashMap<Double, Integer> matrixThreeMin = new HashMap<Double, Integer>();  
    	Set<String> keys = globalRFDataDump.keySet();
    	String Stmp = keys.toString();
    	Stmp = Stmp.replace("[", "");
    	Stmp = Stmp.replace("]", "");
    	Stmp = Stmp.replace(" ", "");
    	String Skeys[] = (Stmp).split(",");
    	
    	Double[] transmitDistance =  new Double[hmSize];
    	
    	for(int i = 0;i<hmSize;i++)
    	{
    		transmitDistance [i] = getDistance(transLat,transLng,globalRFDataDump.get(Skeys[i]).Latitude, globalRFDataDump.get(Skeys[i]).Longitude);
    	}
    	for(int i = 1; i<=hmSize;i++)
    	{
    		 //float tempFormat = 2.345343f;
    		 //float formatted =Float.parseFloat(String.format("%.2f", tempFormat));
    		// matrixThreeMin.put(distanceMatrix[hmSize-1][i-1], i); //akash change to distance from transmitter getDistance
    		 
    		matrixThreeMin.put(transmitDistance[i-1], i);
    		
    		sortDistance[i-1] = transmitDistance[i-1];
    		 
    		 	
    	}
    	//sort
    	double tmpDistance[] = new double[hmSize];
    	tmpDistance = sortDistance;
    	Arrays.sort(sortDistance);
    	ids[0] = matrixThreeMin.get(sortDistance[1]); // ids give nearest points to the location
    	ids[1] = matrixThreeMin.get(sortDistance[2]);
    	ids[2] = matrixThreeMin.get(sortDistance[3]);
    	sortDistance = tmpDistance;
    	
    	
    	//solve the 1,2 equations
    	//eq is RSSI = alpha *   *  (distance) ^ -n // solve n and aplha
    // n = log r2 - log r1 / (log d1 + log d2)
    	
    	
    	//System.out.println("akash skeys"+keys.toString());
    	//System.out.println("akash double  distance"+Array.toString() );
    	double n = (Math.log((globalRFDataDump.get(Skeys[ids[1]-1])).RSSI/ (globalRFDataDump.get(Skeys[ids[0]-1]).RSSI))) / (Math.log(sortDistance[ids[0]-1] * sortDistance[ids[1]-1])) ;
    	double alpha = Math.pow(sortDistance[ids[0]], n)*globalRFDataDump.get(Skeys[ids[0]-1]).RSSI;
    	 
    	//match error with 3rd eq
    	double error = globalRFDataDump.get(Skeys[ids[2]-1]).RSSI - (alpha / Math.pow(sortDistance[ids[2]], n));
    	
    	System.out.println("Error from formula is " + error);
    	System.out.println("alpha is  " + alpha + "n is "+n);
        
    
    	//find rssi from the equation
    	double distanceofCalculatedRssiFromCenter =  transmitDistance[hmSize-1];
    	toBeCalculatedRSSI = (float) (alpha / Math.pow(distanceofCalculatedRssiFromCenter, n));
    	
    	System.out.println("distance trans is " + Arrays.toString(transmitDistance));
    	
    	
    	}
    	
    
    
    	return ids;
    }
    
    float rssiEstimation(float rlat, float rlng){
    	//transLat, transLng are the lat and log of the transmitters
    	
    	float distRssi = 0;
    	int earthR = 6371;
    	float LatA = 37.418436F;
    	float LonA = -121.963477F;
    	float DistA = 0.265710701754f;
    	float LatB = 37.417243f;
    	float LonB = -121.961889f;
    	float DistB = 0.234592423446f;
    	float LatC = 37.418692f;
    	float LonC = -121.960194f;
    	float DistC = 0.0548954278262f;
    	float xA = (float) (earthR *(Math.cos(Math.toRadians(LatA)) * Math.cos(Math.toRadians(LonA))));
    	float yA = (float) (earthR *(Math.cos(Math.toRadians(LatA)) * Math.sin(Math.toRadians(LonA))));
    	float zA = (float) (earthR *(Math.sin(Math.toRadians(LatA))));
    	
    	float xB = (float) (earthR *(Math.cos(Math.toRadians(LatB)) * Math.cos(Math.toRadians(LonB))));
    	float yB = (float) (earthR *(Math.cos(Math.toRadians(LatB)) * Math.sin(Math.toRadians(LonB))));
    	float zB = (float) (earthR *(Math.sin(Math.toRadians(LatB))));

    	float xC = (float) (earthR *(Math.cos(Math.toRadians(LatC)) * Math.cos(Math.toRadians(LonC))));
    	float yC = (float) (earthR *(Math.cos(Math.toRadians(LatC)) * Math.sin(Math.toRadians(LonC))));
    	float zC = (float) (earthR *(Math.sin(Math.toRadians(LatC))));
    	
    	float P1[] = {xA, yA, zA};
    	float P2[] = {xB, yB, zB};
    	float P3[] = {xC, yC, zC};

 		/*ex = (P2 - P1)/(numpy.linalg.norm(P2 - P1))
 		i = numpy.dot(ex, P3 - P1)
   		ey = (P3 - P1 - i*ex)/(numpy.linalg.norm(P3 - P1 - i*ex))
  		ez = numpy.cross(ex,ey)
  		d = numpy.linalg.norm(P2 - P1)
  		j = numpy.dot(ey, P3 - P1)

   		x = (pow(DistA,2) - pow(DistB,2) + pow(d,2))/(2*d)
		y = ((pow(DistA,2) - pow(DistC,2) + pow(i,2) + pow(j,2))/(2*j)) - ((i/j)*x)

   		z = numpy.sqrt(pow(DistA,2) - pow(x,2) - pow(y,2))

    //	#triPt is an array with ECEF x,y,z of trilateration point
    	triPt = P1 + x*ex + y*ey + z*ez

 //   			#convert back to lat/long from ECEF
   // 			#convert to degrees
 		lat = math.degrees(math.asin(triPt[2] / earthR))
  		lon = math.degrees(math.atan2(triPt[1],triPt[0]))

    	*/
    	
    	
    	return distRssi;
    }

 
    

    // Helper method to color each country based on life expectancy
    // Red-orange indicates low (near 40)
    // Blue indicates high (near 100)
}
