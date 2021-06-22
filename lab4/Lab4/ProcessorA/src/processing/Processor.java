package processing;

import processing.StatusListener;

public interface Processor {
boolean submitTask(String task, StatusListener sl);
String getInfo();
String getResult();
}