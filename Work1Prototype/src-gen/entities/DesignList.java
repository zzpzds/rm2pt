package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class DesignList implements Serializable {
	
	/* all primary attributes */
	private String List;
	private DesignListType Type;
	
	/* all references */
	private List<DesignImage> DesignListtoDesignImage = new LinkedList<DesignImage>(); 
	
	/* all get and set functions */
	public String getList() {
		return List;
	}	
	
	public void setList(String list) {
		this.List = list;
	}
	public DesignListType getType() {
		return Type;
	}	
	
	public void setType(DesignListType type) {
		this.Type = type;
	}
	
	/* all functions for reference*/
	public List<DesignImage> getDesignListtoDesignImage() {
		return DesignListtoDesignImage;
	}	
	
	public void addDesignListtoDesignImage(DesignImage designimage) {
		this.DesignListtoDesignImage.add(designimage);
	}
	
	public void deleteDesignListtoDesignImage(DesignImage designimage) {
		this.DesignListtoDesignImage.remove(designimage);
	}
	


}
