package ls;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Lscommand {

	String currentFilePath;
	
	boolean l_option;
	boolean S_option;
	boolean r_option;
	boolean t_option;
	boolean a_option;
	
	String fileType;
	String permission;
	int count;
	String owner;
	long fileSize;
	String group;
	String time;
	String fileName;
	boolean hidden;
	
	ArrayList<FolderInfo> folderInfos = new ArrayList<FolderInfo>();
	FolderInfo folderInfo;
	
	public void run(String[] args) throws IOException {
		
		File dir = new File(System.getProperty("user.dir"));
		if(!args[args.length-1].substring(0,1).equals("-"))
		{
			dir = new File(args[args.length-1]);
		}
		/* 파일 경로가 입력되면 파일 경로 바꾸기*/
		File[] fileList = dir.listFiles();

		for(int i = 0 ; i < fileList.length ; i++){
			File file = fileList[i];
			Path path = file.toPath();
			PosixFileAttributes attr = Files.readAttributes(path, PosixFileAttributes.class);
			
			//find file type
			if(attr.isRegularFile())
				fileType = "-";
			else if(attr.isSymbolicLink())
				fileType = "l";
			else if(attr.isDirectory())
				fileType = "d";
			else fileType = "?";
			
			//find permission
			/*System.out.println("creationTime: " + attr.creationTime());
			System.out.println("lastAccessTime: " + attr.lastAccessTime());*/
			owner = attr.owner().getName();
		    group = attr.group().getName();
		    permission = PosixFilePermissions.toString(attr.permissions());
		    
		    //size
		    fileSize = attr.size();
		    FileTime fileTime = attr.creationTime();
		    
		    //time
			DateFormat simpleDateFormat = new SimpleDateFormat("MMM dd HH:mm");
			time = simpleDateFormat.format(fileTime.toMillis());
			//link count
			if(file.isDirectory())
			{
				count = file.list().length;
			}
			else count = 1;
			
			//file name
			fileName = file.getName();
			
			//file hidden
			hidden = file.isHidden();
			
			folderInfo = new FolderInfo(fileType,permission,count,owner,fileSize,group,time,fileName,hidden);
			folderInfos.add(folderInfo);
			//System.out.println(fileType+permission+count+owner+fileSize+group+time+fileName);
			 
		}
		
		Options options = createOptions();
		
		if(parseOptions(options,args)) {
			Comparator<FolderInfo> sortorder;
			if(r_option)
			{
				if(S_option)
				{
					sortorder = new reverseCompareSize();
				}
				else if(t_option)
				{
					sortorder = new reverseCompareTime();
				}
				else 
				{
					sortorder = new reverseCompareFileName();
				}
			}
			else
			{
				if(S_option)
				{
					sortorder = new CompareSize();
				}
				else if(t_option)
				{
					sortorder = new CompareTime();
				}
				else 
				{
					sortorder = new CompareFileName();
				}
			}
		    Collections.sort(folderInfos, sortorder);
		     
			if(l_option) {
				for(FolderInfo f : folderInfos)
				{
					if(a_option==false && f.hidden==true) continue;
					System.out.println(String.format("%s%s %4d %10s %10s %4d %10s %s", 
							f.fileType,f.permission,f.count,f.owner,
							f.group,f.fileSize,f.time,f.fileName));
				}
			}
			else
			{
				for(FolderInfo f : folderInfos)
				{
					if(a_option==false && f.hidden==true) continue;
					System.out.println(
							f.fileName
					);
				}
			}
			
	}
}

	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			CommandLine cmd = parser.parse(options, args);

			l_option = cmd.hasOption("l");
			S_option = cmd.hasOption("S");
			r_option = cmd.hasOption("r");
			t_option = cmd.hasOption("t");
			a_option = cmd.hasOption("a");
			
		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
		
	}
	
	private Options createOptions() {
		Options options = new Options();

		options.addOption(Option.builder("l").longOpt("long")
				.argName("long")
		        .desc("Show in long")
		        .build());
		options.addOption(Option.builder("S").longOpt("size")
				.argName("size")
		        .desc("List in size order")
		        .build());
		options.addOption(Option.builder("r").longOpt("reverse")
				.argName("reverse")
		        .desc("List in reverse order")
		        .build());
		options.addOption(Option.builder("t").longOpt("time")
				.argName("time")
		        .desc("List in time order")
		        .build());
		options.addOption(Option.builder("a").longOpt("all")
				.argName("all")
		        .desc("Show all file")
		        .build());
		
		return options;
	}
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "ls";
		String footer ="";
		formatter.printHelp("ls", header, options, footer, true);
	}
	
	class reverseCompareSize implements Comparator<FolderInfo> {
		 
	    @Override
	    public int compare(FolderInfo o1, FolderInfo o2) {
	        return (int) (o1.getFileSize() - o2.getFileSize());
	    }
	}
	
	class CompareSize implements Comparator<FolderInfo> {
		 
	    @Override
	    public int compare(FolderInfo o1, FolderInfo o2) {
	        return (int) (o2.getFileSize() - o1.getFileSize());
	    }
	}
	class CompareFileName implements Comparator<FolderInfo> {
		 
	    @Override
	    public int compare(FolderInfo o1, FolderInfo o2) {
		    return o1.getFileName().compareTo(o2.getFileName());
	    }
	}
	
	class reverseCompareFileName implements Comparator<FolderInfo> {
		 
	    @Override
	    public int compare(FolderInfo o1, FolderInfo o2) {
		    return o2.getFileName().compareTo(o1.getFileName());
	    }
	}
	
	class CompareTime implements Comparator<FolderInfo> {
		 
	    @Override
	    public int compare(FolderInfo o1, FolderInfo o2) {
		    return o1.getTime().compareTo(o2.getTime());
	    }
	}
	
	class reverseCompareTime implements Comparator<FolderInfo> {
		 
	    @Override
	    public int compare(FolderInfo o1, FolderInfo o2) {
		    return o2.getTime().compareTo(o1.getTime());
	    }
	}
	
	 
}
