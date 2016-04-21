import java.util.ArrayList;
import java.io.*;

public class parse2
{
	public static void main(String [] args)
	{
		ArrayList<String> lines = new ArrayList<String>();
		File tcp_out = new File("snif2.txt");
		
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
				int rssi_end = tmp.indexOf("dB ");
				int MAC_index = tmp.indexOf("SA:");
				int MAC_end = MAC_index + 20;
				if(rssi_index == -1 || rssi_end > tmp.length() || MAC_index == -1 || MAC_end > tmp.length())
					continue;
				int rssi = Integer.parseInt(tmp.substring(rssi_index, rssi_end));
				String MAC_addr = tmp.substring(MAC_index+3,MAC_index+20);
				if(rssi < 35)
					System.out.println("Line match: RSSI = -" + rssi + "dB, MAC = " + MAC_addr);
			}
		}catch(Exception e){System.err.println(e);}
	}
}
