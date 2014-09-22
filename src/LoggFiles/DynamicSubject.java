package LoggFiles;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class DynamicSubject {
	//File pathMailSave = new File("/Users/antonekman/Documents/workspace/CentriAir/savefiles/mail_save.txt");
	File pathSubject;
	
	public DynamicSubject(String subName, String filePath){	// Add browsedpath
		pathSubject = new File(filePath + "/dynamic_subjects.txt");
		createNewDynamic(subName, filePath);
	}
	public DynamicSubject(){
		
	}
	
	public String[] getDynamicSubs(String filePathString) throws IOException{
		String[] retSubs = new String[fileCount(filePathString)];
		File filePath = new File(filePathString+"/dynamic_subjects.txt");
		BufferedReader br;
		int i=0;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			@SuppressWarnings("resource")
			Scanner sr = new Scanner(br);
			while(sr.hasNextLine()){
				retSubs[i]=sr.nextLine();
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return retSubs;
	}
	@SuppressWarnings("resource")
	private void createNewDynamic(String subName, String filePath){
		File pathSubject = new File(filePath + "/dynamic_subjects.txt");
		String s=null,ip;
		Scanner sc = new Scanner(subName);

		if(sc.hasNextLine()){
			s = sc.nextLine().toUpperCase().replace(" ", "_").trim();
			writeToFile(pathSubject, s, true);
			new File(filePath +"/"+s).mkdir();
			new File(filePath +"/"+s+"/logg_files").mkdir();
			new File(filePath +"/"+s+"IP.txt");
		}
		
	}
	public void addFileEnding(String subName, Enumeration<String> fileEnd, String filePath){
		File path = new File(filePath+"/"+subName.toUpperCase().replace(" ", "_").trim()+"/file_endings.txt");
		while(fileEnd.hasMoreElements()){
			writeToFile(path, fileEnd.nextElement(), false);
		}
		
}
	@SuppressWarnings("resource")
	private int fileCount(String s) throws IOException{
		int i=0;
		String path = s+"/dynamic_subjects.txt";
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();
		while(line != null){
			i=i+1;
			line = br.readLine();
		}
		return i;
	}
	
	@SuppressWarnings({ "resource", "unused" })
	private void writeToFile(File pathSubject, String toWrite, boolean append){
		BufferedWriter bw = null;
		BufferedReader br = null;

		try{
			bw = new BufferedWriter(new FileWriter(pathSubject,true));
			boolean b = true;
			br = new BufferedReader(new FileReader(pathSubject));
			Scanner sr = new Scanner(br);

			while (sr.hasNextLine()) {
				String line = sr.nextLine().trim();
				if(line.equals(toWrite)) { 
					b=false;
					break;
				}
			}
			if (b && append) {
				bw.append("\n");
				bw.append(toWrite);
			}else if(b && !append){
				bw.write(toWrite);
				bw.append("\n");
			}

		}catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		} finally{
			try {bw.close();} catch (IOException e) {e.printStackTrace();}
		}
	}
}
