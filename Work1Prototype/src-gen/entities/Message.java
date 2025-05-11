package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Message implements Serializable {
	
	/* all primary attributes */
	private String Title;
	private String Content;
	private String Id;
	
	/* all references */
	
	/* all get and set functions */
	public String getTitle() {
		return Title;
	}	
	
	public void setTitle(String title) {
		this.Title = title;
	}
	public String getContent() {
		return Content;
	}	
	
	public void setContent(String content) {
		this.Content = content;
	}
	public String getId() {
		return Id;
	}	
	
	public void setId(String id) {
		this.Id = id;
	}
	
	/* all functions for reference*/
	


}
