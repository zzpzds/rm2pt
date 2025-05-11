package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class AIProcess implements Serializable {
	
	/* all primary attributes */
	private String DesignId;
	private String Result;
	
	/* all references */
	
	/* all get and set functions */
	public String getDesignId() {
		return DesignId;
	}	
	
	public void setDesignId(String designid) {
		this.DesignId = designid;
	}
	public String getResult() {
		return Result;
	}	
	
	public void setResult(String result) {
		this.Result = result;
	}
	
	/* all functions for reference*/
	


}
