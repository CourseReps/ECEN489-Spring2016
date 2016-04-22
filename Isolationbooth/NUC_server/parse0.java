import java.util.ArrayList;
import java.io.*;
import java.util.Date;

public class parse0
{
	public static void main(String [] args)
	{
		ArrayList<String> lines = new ArrayList<String>();
		File tcp_out = new File("snif0.txt");
		RssiDbWriter dbHandle = new RssiDbWriter();

		try{
			BufferedReader br = new BufferedReader(new FileReader(tcp_out));
			String line = null;
			while((line = br.readLine()) != null)
				lines.add(line);
			br.close();
			tcp_out.delete();
		
			for(int i = 0; i < lines.size(); ++i)
			{
				String tmp = lines.get(i);
				if(tmp.length() == 0)
					continue;
				int rssi_index = tmp.indexOf("-") + 1;
				int rssi = Integer.parseInt(tmp.substring(rssi_index, tmp.indexOf("dB ")));
				int MAC_index = tmp.indexOf("SA:");
				if(MAC_index == -1)
					continue;
				String MAC_addr = tmp.substring(MAC_index+3,MAC_index+20);
				Date d = new Date();
				long unixTime =  d.getTime()/1000;
				if(rssi < 35)
					dbHandle.writeDB(rssi, MAC_addr, unixTime, 0);
					//System.out.println("Line match: RSSI = -" + rssi + "dB, MAC = " + MAC_addr);
			}
		}catch(Exception e){System.err.println(e);}
	}
}
