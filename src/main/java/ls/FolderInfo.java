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

}
