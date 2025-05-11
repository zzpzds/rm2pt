package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Developer implements Serializable {
	
	/* all primary attributes */
	private String UserId;
	private String Name;
	private UserType Type;
	
	/* all references */
	private DesignList DevelopertoDesignList; 
	private AIProcess DevelopertoAIProcess; 
	
	/* all get and set functions */
	public String getUserId() {
		return UserId;
	}	
	
	public void setUserId(String userid) {
		this.UserId = userid;
	}
	public String getName() {
		return Name;
	}	
	
	public void setName(String name) {
		this.Name = name;
	}
	public UserType getType() {
		return Type;
	}	
	
	public void setType(UserType type) {
		this.Type = type;
	}
	
	/* all functions for reference*/
	public DesignList getDevelopertoDesignList() {
		return DevelopertoDesignList;
	}	
	
	public void setDevelopertoDesignList(DesignList designlist) {
		this.DevelopertoDesignList = designlist;
	}			
	public AIProcess getDevelopertoAIProcess() {
		return DevelopertoAIProcess;
	}	
	
	public void setDevelopertoAIProcess(AIProcess aiprocess) {
		this.DevelopertoAIProcess = aiprocess;
	}			
	


}
