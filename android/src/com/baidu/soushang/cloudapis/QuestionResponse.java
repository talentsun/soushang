package com.baidu.soushang.cloudapis;

import java.util.List;

import it.restrung.rest.annotations.JsonProperty;
import it.restrung.rest.marshalling.response.AbstractJSONResponse;

public class QuestionResponse extends CommonResponse {
  private static final long serialVersionUID = 1L;
  
  @JsonProperty(value="question")
  private Question question;

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public QuestionResponse() {

  }
  
  public static class Question extends AbstractJSONResponse {
    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value="event_title")
    private String eventTitle;
    
    @JsonProperty(value="question_id")
    private int id;
    
    @JsonProperty(value="right_answer")
    private int rightAnswer;
    
    @JsonProperty(value="search_recom")
    private String searchRecom;
    
    @JsonProperty(value="question_title")
    private String title;
    
    @JsonProperty(value="difficulty")
    private int difficulty;
    
    @JsonProperty(value="options")
    private List<String> options;
    
    @JsonProperty(value="index")
    private int index;
    
    @JsonProperty(value="total")
    private int total;
    
    @JsonProperty(value="answer_time")
    private int answerTime;

    public String getEventTitle() {
      return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
      this.eventTitle = eventTitle;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getRightAnswer() {
      return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
      this.rightAnswer = rightAnswer;
    }

    
    public String getSearchRecom() {
		return searchRecom;
	}

	public void setSearchRecom(String searchRecom) {
		this.searchRecom = searchRecom;
	}

	public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public int getDifficulty() {
      return difficulty;
    }

    public void setDifficulty(int difficulty) {
      this.difficulty = difficulty;
    }

    public List<String> getOptions() {
      return options;
    }

    public void setOptions(List<String> options) {
      this.options = options;
    }

    public int getIndex() {
      return index;
    }

    public void setIndex(int index) {
      this.index = index;
    }

    public int getTotal() {
      return total;
    }

    public void setTotal(int total) {
      this.total = total;
    }

    public int getAnswerTime() {
      return answerTime;
    }

    public void setAnswerTime(int answerTime) {
      this.answerTime = answerTime;
    }
    
    public Question() {
      
    }
  }
}
