package services.impl;

import services.*;
import entities.*;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.Arrays;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.SerializationUtils;
import java.util.Iterator;

public class Work1SystemImpl implements Work1System, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public Work1SystemImpl() {
		services = new ThirdPartyServicesImpl();
	}

	public void refresh() {
		Import_DesignService import_designservice_service = (Import_DesignService) ServiceManager
				.getAllInstancesOf("Import_DesignService").get(0);
		Preview_Design_DesignerService preview_design_designerservice_service = (Preview_Design_DesignerService) ServiceManager
				.getAllInstancesOf("Preview_Design_DesignerService").get(0);
		Preview_DesignService preview_designservice_service = (Preview_DesignService) ServiceManager
				.getAllInstancesOf("Preview_DesignService").get(0);
		Export_DesignService export_designservice_service = (Export_DesignService) ServiceManager
				.getAllInstancesOf("Export_DesignService").get(0);
		AI_ConvertService ai_convertservice_service = (AI_ConvertService) ServiceManager
				.getAllInstancesOf("AI_ConvertService").get(0);
		Image_DownloadService image_downloadservice_service = (Image_DownloadService) ServiceManager
				.getAllInstancesOf("Image_DownloadService").get(0);
		Preview_Design_DeveloperService preview_design_developerservice_service = (Preview_Design_DeveloperService) ServiceManager
				.getAllInstancesOf("Preview_Design_DeveloperService").get(0);
	}			
	
	/* Generate buiness logic according to functional requirement */
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
