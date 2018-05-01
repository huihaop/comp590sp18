package apps;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import models.Unsigned8BitModel;
import codec.HuffmanEncoder;
import codec.SymbolDecoder;
import codec.SymbolEncoder;
import models.Symbol;
import models.SymbolModel;
import models.Unsigned8BitModel.Unsigned8BitSymbol;
import io.InsufficientBitsLeftException;
import io.BitSink;
import io.BitSource;
import codec.ArithmeticDecoder;
import codec.ArithmeticEncoder;
import codec.HuffmanDecoder;
import io.InputStreamBitSource;
import io.OutputStreamBitSink;
import java.util.*;

public class LZWVideoApp {
	public static void main(String[] args) throws IOException, InsufficientBitsLeftException {
		
		String[] bases = {"candle","tractor","pinwheel"};
		
		//"bunny","jellyfish",
		
	    int width = 800;
		int height = 450;
		int num_frames = 150;
		int dic_size = 256;
		int temp = 0;

	/**for (int k = 0;k<bases.length;k++) {
		String filename="C:\\Users\\Hao\\Desktop\\" + bases[k] + ".450p.yuv";
		File file = new File(filename);
		InputStream train = new FileInputStream(file);
		File out_file = new File("C:\\Users\\Hao\\Desktop\\" + bases[k] + "onefilelzw-compressed.dat");
		OutputStream out_stream = new FileOutputStream(out_file);
		BitSink bit_sink = new OutputStreamBitSink(out_stream);
		int EOF = -1;
		int[][][]wholefile = new int[num_frames][width][height];
		List<Integer> CList= new ArrayList<Integer>();
	
		for(int i = 0;i<num_frames;i++) {
			int[][] current_frame = new int[width][height];
			
			
			current_frame =readFrame(train,width,height);
			wholefile[i]=current_frame;
			System.out.println("Reading frame No."+ i);
			
		//	out_stream.write(EOF);
		}
		CList=lzwcompression(wholefile,width,height,dic_size,num_frames);
		
		
		for (int j = 0;j<CList.size();j++) {
			if(CList.get(j)>temp) {
				temp = CList.get(j);
			}
			out_stream.write(CList.get(j));
		}
	}
	//	out_stream.write(EOF);
		System.out.println(temp);**/
		for(int i = 0; i<bases.length;i++) {
			String rfilename = "C:\\Users\\Hao\\Desktop\\" + bases[i] + "onefilelzw-compressed.dat";
			File rfile = new File(rfilename);
			File reconstructed = new File("C:\\Users\\Hao\\Desktop\\" + bases[i] + "onefilelzw-decompressed.dat");
			InputStream decom_in = new FileInputStream(rfile);
			OutputStream decom_out = new FileOutputStream(reconstructed);
			BitSource decom_inS = new InputStreamBitSource(decom_in);
			List<Integer> decom = new ArrayList<Integer>();
			while(true) {
				int j = 0;
			try {
				j = decom_inS.next(32);
				decom.add(j);
			}catch(InsufficientBitsLeftException insuff){
				break;
			}
			String s = lzwdecompression(decom,dic_size);
			for(int k = 0;k<s.length();k++) {
				decom_out.write(s.indexOf(k));
			}
			
		}
	}
		
	}	
	public static List<Integer> lzwcompression(int[][][]frame,  int width, int height,int dic_size,int num_frames) {
		HashMap<String, Integer> dict = new HashMap<String,Integer>();
		List<Integer> result = new ArrayList<Integer>();
		int size = dic_size;
		for(int i =0;i<size;i++) {
			dict.put(Character.toString((char)i), i);
		}
		String w = "";
		
		for(int x = 0;x<num_frames;x++) {
			for(int y = 0;y<width;y++) {
				for(int z = 0;z<height;z++) {
					
				
				String c = Character.toString((char)frame[x][y][z]);
				String wc = w+c;
				if(dict.containsKey(wc)) {
					w =wc;
				}else {
					result.add(dict.get(w));
					dict.put(wc,size++);
					w =""+c;
				}
			}
			}
		}
		if(!w.equals("")) {
			result.add(dict.get(w));
		}
		return result;
	}
	private static int[][] readFrame(InputStream src, int width, int height) 
			throws IOException {
		int[][] frame_data = new int[width][height];
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				frame_data[x][y] = src.read();
			}
		}
		return frame_data;
	}
	
	
	public static String lzwdecompression(List<Integer> compressed, int dict_size) {
		int dictSize = dict_size;
		
	
		Map<Integer,String> dict= new HashMap<Integer,String>();
		for(int i = 0;i<256;i++) {
			dict.put(i, Character.toString((char)i));
		}
			String w = ""+(char)(int)compressed.remove(0);
			StringBuffer result =new StringBuffer(w);
			for(int k:compressed) {
				String entry;
				if(dict.containsKey(k)) {
					entry = dict.get(k);
				}else if(k==dict_size) {
					entry = w+w.charAt(0);
				}else{
					throw new IllegalArgumentException("Bad compressed");
			}
				result.append(entry);
				dict.put(dictSize++,w+entry.charAt(0));
				
				w = entry;
				
				
				
			}
			return result.toString();
		
	}
	}
	
