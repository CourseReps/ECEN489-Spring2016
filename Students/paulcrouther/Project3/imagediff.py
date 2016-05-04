from PIL import Image
import cStringIO as StringIO
import binascii
import io

import socket
# tablet
#UDP_IP_DEST = '10.202.97.214'
# phone 4g from tablet
####UDP_IP_DEST = '192.168.43.220'
#UDP_IP_DEST = '192.168.42.22'
#UDP_IP_DEST = '192.168.42.23'
# Mobile Data
#UDP_IP_DEST = '192.0.0.4'
#UDP_IP_DEST = '172.31.99.132'
#UDP_IP_DEST = '172.31.99.46'
#UDP_IP_DEST = '10.202.96.229'
UDP_IP_DEST = '10.202.96.166'


UDP_IP = '0.0.0.0'
UDP_PORT = 8000
imageList = []


def dhash(image, hash_size = 8):
    # Grayscale and shrink the image.
    image = image.convert('L').resize(
                                      (hash_size + 1, hash_size),
                                      Image.ANTIALIAS,
                                      )
        
    pixels = list(image.getdata())
        
    # Compare adjacent pixels.
    difference = []
    for row in xrange(hash_size):
        for col in xrange(hash_size):
            pixel_left = image.getpixel((col, row))
            pixel_right = image.getpixel((col + 1, row))
            difference.append(pixel_left > pixel_right)
    
    # Convert the binary array to a hexadecimal string.
    decimal_value = 0
    hex_string = []
    for index, value in enumerate(difference):
        if value:
            decimal_value += 2**(index % 8)
        if (index % 8) == 7:
            hex_string.append(hex(decimal_value)[2:].rjust(2, '0'))
            decimal_value = 0

    return ''.join(hex_string)


sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP
sock.bind((socket.gethostbyname(socket.gethostname()), UDP_PORT))
print "Open"
print str(socket.gethostbyname(socket.gethostname()))
while True:
    for i in range(0,180):
        data, addr = sock.recvfrom(62000) # buffer size is 1024 bytes
        if data:
        
            jpgFile = Image.open(io.BytesIO(data))
            #import StringIO
            print "received: " + str(jpgFile.bits) + " " +  str(jpgFile.size) + " " +  str(jpgFile.format) + " " + dhash(jpgFile) + "\n"
            #print "count: " + str(i)
            #jpgFile.show()
            #print "received message:\n", str(data.strip())
            sock.sendto("I got the message", (UDP_IP_DEST, UDP_PORT))
            imageList.append(dhash(jpgFile))
            print "count: " + str(i) + " current image hash: " + str(imageList[i])
            if(i > 0):
                print "count: " + str(i-1) + " previous image hash: " + str(imageList[i-1])
                currvalue = int(imageList[i],32)
                prevalue = int(imageList[i-1],32)
                print "Difference between the two hex values: " + str(abs(currvalue - prevalue))
                if (abs(currvalue - prevalue) < (264452523040700131969024)/2):
                    print "Same image."
                elif (((264452523040700131969024)/2 + 1) < abs(currvalue - prevalue) < (2644525230407001319690240)/6):
                    print "Some motion detected"
                elif (((2644525230407001319690240)/6 + 1) < abs(currvalue - prevalue)):
                    print "A lot of motion detected!!!"





