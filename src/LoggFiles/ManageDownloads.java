package LoggFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ManageDownloads {
String staticPath = "/Users/antonekman/Documents/workspace/CentriAir/savefiles/static_subjects.txt";
String dynamicPath = "/Users/antonekman/Documents/workspace/CentriAir/savefiles/dynamic_subjects.txt";
List<String> staticList, dynamicList;
GetLoggFileV2 getLogg;

	public ManageDownloads(){
		staticList = new ArrayList<String>();
		dynamicList = new ArrayList<String>();
		staticList = fillList(staticPath);
		dynamicList = fillList(dynamicPath);
	}
	
	public void startStaticDownload() throws Exception{
		Iterator<String> staticIt = staticList.iterator();
		while(staticIt.hasNext()){
			getLogg = new GetLoggFileV2((String)staticIt.next());
			getLogg.comenceDownload();
		}
	}
	
	public void startDynamicDownload() throws Exception{
		Iterator<String> dynamicIt = dynamicList.iterator();
		while(dynamicIt.hasNext()){
			getLogg = new GetLoggFileV2((String)dynamicIt.next());
			getLogg.comenceDownload();
		}
	}
	
	public void startAllDownload() throws Exception{
		startStaticDownload();
		startDynamicDownload();
	}
	
	public void startSubjectDownload(String subjectToDownload) throws Exception{
		getLogg = new GetLoggFileV2(subjectToDownload);
		getLogg.comenceDownload();
	}
	
	@SuppressWarnings("resource")
	private List<String> fillList(String path){
		List<String> retList = new ArrayList<String>();
		File subPath = new File(path);
		BufferedReader br=null;
		Scanner scBR = null;
		
		try {
			br = new BufferedReader(new FileReader(subPath));
			scBR = new Scanner(br);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
		while(scBR.hasNextLine()){
			retList.add(scBR.nextLine());
		}
		
		return retList;
	}
}
