//global to MainActivity
private int HTTP_SEND_STATUS = 0;

//...

//inside click listener for send button. JSONlist is a list containing all JSON lines desired to be sent
	Thread t = new Thread() {
		public void run() {
			for(int x = 0; x < JSONlist.size(); ++x)
			{
				HTTP_SEND_STATUS = 0;
				sendHTTPdata(JSONlist.get(x));
				if(HTTP_SEND_STATUS == -1)
					System.err.println("Error sending!"); //change later to toast message on phone screen
			}
			System.out.println("Send thread finished");
		}
	};
	t.start(); //start send thread

//...

//in MainActivity class
//pass this method a JSON object and the server address as a human readable string (i.e. "127.0.0.1")
protected void sendHTTPdata(JSONObject json, String serverAddress)
{
    final String data = json.toString();
    try{
        URL url = new URL(serverAddress);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(1000);
        conn.setConnectTimeout(1000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.connect();

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(data);
        writer.close();
        os.close();

        int result = conn.getResponseCode();
        HTTP_SEND_STATUS = 1;
    }catch(Exception e){
        System.err.print(e);
        HTTP_SEND_STATUS = -1;
    }
}
