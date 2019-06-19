package ls;

public class FolderInfo {

	String fileType;
	String permission;
	int count;
	String owner;
	long fileSize;
	String group;
	String time;
	String fileName;
	boolean hidden;
	String timeForCompare;
	String temp;
	
	String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Agu","Sep","Oct","Nov","Dec"};

	public FolderInfo(String fileType, String permission, int count, String owner, long fileSize, String group,
			String time, String fileName, boolean hidden) {
		this.fileType = fileType;
		this.permission = permission;
		this.count = count;
		this.owner = owner;
		this.fileSize = fileSize;
		this.group = group;
		this.time = time;
		this.fileName = fileName;
		this.hidden = hidden;
		
		temp = time.substring(0,3);
		int i = 0;
		for(String month : months)
		{
			if(month.equals(temp)) timeForCompare = i + time.substring(3);
			i++;
		}
		
	}

	public FolderInfo() {
			fileType = null;
			permission = null;
			count = 0;
			owner = null;
			fileSize = 0;
			group = null;
			time = null;
			fileName = null;
			hidden = false;
		}
	
	public String getFileName() {
        return this.fileName;
    }
	
	public long getFileSize() {
		return fileSize;
	}

	public String getTime() {
		return timeForCompare;
	}

}
