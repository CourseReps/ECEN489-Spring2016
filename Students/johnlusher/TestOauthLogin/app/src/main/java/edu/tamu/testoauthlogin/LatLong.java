// ---------------------------------------------------------------------------------------------------------------------
// ECEN689: Special Topics in Cloud-Enabled Mobile Sensing
//
// File Name: 		LatLong.java
// Version:			1.0.0
// Date:			February 25, 2016
// Description:	    Lat Long Member and Computation Function
//
// Author:          John Lusher II
// ---------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//  Revision(s):     Date:                       Description:
//  v1.0.0           February 25, 2016  	     Initial Release
//  --------------------------------------------------------------------------------------------------------------------
package edu.tamu.testoauthlogin;


//  --------------------------------------------------------------------------------------------------------------------
//  Imports
//  --------------------------------------------------------------------------------------------------------------------

//  --------------------------------------------------------------------------------------------------------------------
//        Class:    LatLong
//  Description:	LatLong class - LatLong Field Data
//  --------------------------------------------------------------------------------------------------------------------
public class LatLong
{
    public double Latitude;
    public double Longitude;

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     LatLong - Constructor
    //      Inputs:	    Azimuth (degrees), Distance (feet), Lat (degrees), Long (degrees)
    //     Outputs:	    none
    // Description:     LatLong, starting location bearing and distance to get new location
    //	----------------------------------------------------------------------------------------------------------------
    public LatLong(double azimuth, double distance, double ref_lat, double ref_long)
    {
        // Get radian versions
        double ref_lat_rad = GetRadianFromDegrees(ref_lat);
        double ref_long_rad = GetRadianFromDegrees(ref_long);
        double azimuth_rad = GetRadianFromDegrees(azimuth);

        // Get distance in meters
        double distance_m = distance * 0.3048;

        // Build Sine/Cosine values
        double cos_lat = Math.cos(ref_lat_rad);
        double sin_lat = Math.sin(ref_lat_rad);
        double cos_az = Math.cos(azimuth_rad);
        double sin_az = Math.sin(azimuth_rad);

        // See Mat Projections Document
        // parameters a & b
        double a = 6378137.0;
        double b = 6356752.3;

        // Ellipsoid Radius at Latitude
        double elp_rad = Math.sqrt((Math.pow((Math.pow(a, 2) * cos_lat), 2) + Math.pow((Math.pow(b, 2) * sin_lat), 2)) /
                (Math.pow(a * cos_lat, 2) + Math.pow(b * sin_lat, 2)));

        double c = distance_m / elp_rad;

        // Build Sine/Cosine values of "c"
        double cos_c = Math.cos(c);
        double sin_c = Math.sin(c);

        double y = sin_c * sin_az;
        double x = (cos_lat * cos_c) - (sin_lat * sin_c * cos_az);
        double at = (x == 0.0 && y == 0.0) ? 0.0 : Math.atan2(y, x);
        double new_lat_rad = Math.asin((sin_lat * cos_c) + (cos_lat * sin_c * cos_az));
        double new_lon_rad = ref_long_rad + at;

        // Covert back to degrees
        Latitude = GetDegreesFromRadian(new_lat_rad);
        Longitude = GetDegreesFromRadian(new_lon_rad);
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     GetRadianFromDegrees
    //      Inputs:	    Angle in Degrees
    //     Outputs:	    Angle in Radians
    // Description:     Radians from Degrees
    //	----------------------------------------------------------------------------------------------------------------
    private double GetRadianFromDegrees(double dblAngle)
    {
        return dblAngle / 180.0 * Math.PI;
    }

    //	----------------------------------------------------------------------------------------------------------------
    //      Method:     GetDegreesFromRadian
    //      Inputs:	    Angle in Radians
    //     Outputs:	    Angle in Degrees
    // Description:     Degrees from Radians
    //	----------------------------------------------------------------------------------------------------------------
    private double GetDegreesFromRadian(double dblAngle)
    {
        return dblAngle * 180.0 / Math.PI;
    }
}

