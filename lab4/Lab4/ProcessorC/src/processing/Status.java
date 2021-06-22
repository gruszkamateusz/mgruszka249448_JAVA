package processing;


public class Status {
private String taskName;

private int progress;

public int getProgress(){
	return progress;
}

public String getTaskName() {
	return taskName;
}
public Status(String taskName, int progress) {
	this.taskName = taskName;
	this.progress = progress;
}
}

