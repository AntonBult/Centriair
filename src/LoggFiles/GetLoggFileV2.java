package LoggFiles;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public class GetLoggFileV2{
	private String subjectPath = null;
	private String subjectName = null;
	
	public GetLoggFileV2(String subName){
		subjectName = subName.toUpperCase().trim();
		subjectPath = "/Users/antonekman/Documents/workspace/CentriAir/savefiles/" + subjectName;
	}
	
	public void comenceDownload() throws Exception {
		System.out.println("Download jobs started");				//For trouble shooting
		downloadLIB();												//Starts a download preperation
	}

		// Add so files from static_subjects get downloaded to!
	public void downloadLIB(){
		String libURL;
		File libFile; 
		BufferedReader brEndings;
		
		try {
				brEndings = new BufferedReader(new FileReader(subjectPath + "/file_endings.txt"));
				char c = 'A';
				
				for(int j=0;j<fileCount(subjectName);j++){
					libURL = createSubURL(brEndings.readLine());
					libFile = createFilePath(subjectName,c);
					c=(char)(c+1);
					downloadFile(libURL, libFile, subjectName);		//Start the download
					//Anropa Bash
				}
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void downloadFile(String url, File path, String fileName){
		InputStream in = null;
		FileOutputStream fout = null;
		
		try
		{
			in = URI.create(url).toURL().openStream();				//Creates stream to read file from URL
			fout = new FileOutputStream(path);						//Creates stream to write file from URL
			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1)			//Loop to read the whole file
			{
				fout.write(data, 0, count);
			}
			
		}
		catch(Exception e){
			System.out.println("Cannot download file : " + fileName);	//For trouble shooting
		}
		finally{
			if (in != null)
				try{
					in.close();										//Close read stream
				}
			catch (IOException e){
				e.printStackTrace();
			}
			if(fout != null)
				try{
					fout.close();									//Close write stream
				}
			catch (IOException e){
				e.printStackTrace();
			}
			System.out.println("File " + fileName + " downloaded successfully");	//For trouble shooting
		}

	}
	
	@SuppressWarnings("resource")
	private int fileCount(String subj) throws IOException{
		int i=0;
		String s = subj.replaceAll("\\s","_");
		String path = "/Users/antonekman/Documents/workspace/CentriAir/savefiles/"+s+"/file_endings.txt";
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();
		while(line != null){
			i=i+1;
			line = br.readLine();
		}
		return i;
	}
	
	private File createFilePath(String sub,char c){
		String s = "/Users/antonekman/Documents/workspace/CentriAir/savefiles/"+sub+"/logg_files/"+sub+"_"+c+".csv";
		File ret = new File(s);
		return ret;
	}
	
	@SuppressWarnings("resource")
	private String createSubURL(String endOfURL){
		BufferedReader brSubLine;
		String ip=null;
		try {
			brSubLine = new BufferedReader(new FileReader(subjectPath + "/IP.txt"));
			ip = brSubLine.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "http://"+ip.trim()+"/"+endOfURL;
	}
	
}

