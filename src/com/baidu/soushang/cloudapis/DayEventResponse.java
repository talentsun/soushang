package com.baidu.soushang.cloudapis;

import it.restrung.rest.annotations.JsonProperty;

public class DayEventResponse extends CommonResponse {
  private static final long serialVersionUID = 1L;
  
  @JsonProperty(value="event_finished")
  private int eventFinished = -1;
  
  public void setEventFinished(int finished) {
    eventFinished = finished;
  }
  
  public int getEventFinished() {
    return eventFinished;
  }
  
  @JsonProperty(value="start_id")
  private int startId = -1;
  
  public void setStartId(int startId) {
    this.startId = startId;
  }
  
  public int getStartId() {
    return startId;
  }
  
  public DayEventResponse() {
    
  }
}
