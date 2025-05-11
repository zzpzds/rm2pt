package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class DesignImage implements Serializable {
	
	/* all primary attributes */
	private String DesignId;
	private String FileUrl;
	private DesignStatus DesignStatus;
	private String Id;
	
	/* all references */
	
	/* all get and set functions */
	public String getDesignId() {
		return DesignId;
	}	
	
	public void setDesignId(String designid) {
		this.DesignId = designid;
	}
	public String getFileUrl() {
		return FileUrl;
	}	
	
	public void setFileUrl(String fileurl) {
		this.FileUrl = fileurl;
	}
	public DesignStatus getDesignStatus() {
		return DesignStatus;
	}	
	
	public void setDesignStatus(DesignStatus designstatus) {
		this.DesignStatus = designstatus;
	}
	public String getId() {
		return Id;
	}	
	
	public void setId(String id) {
		this.Id = id;
	}
	
	/* all functions for reference*/
	


}
