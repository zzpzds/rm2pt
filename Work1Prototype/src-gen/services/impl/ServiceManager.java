package services.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import services.*;
	
public class ServiceManager {
	
	private static Map<String, List> AllServiceInstance = new HashMap<String, List>();
	
	private static List<Work1System> Work1SystemInstances = new LinkedList<Work1System>();
	private static List<ThirdPartyServices> ThirdPartyServicesInstances = new LinkedList<ThirdPartyServices>();
	private static List<Import_DesignService> Import_DesignServiceInstances = new LinkedList<Import_DesignService>();
	private static List<Preview_Design_DesignerService> Preview_Design_DesignerServiceInstances = new LinkedList<Preview_Design_DesignerService>();
	private static List<Preview_DesignService> Preview_DesignServiceInstances = new LinkedList<Preview_DesignService>();
	private static List<Export_DesignService> Export_DesignServiceInstances = new LinkedList<Export_DesignService>();
	private static List<AI_ConvertService> AI_ConvertServiceInstances = new LinkedList<AI_ConvertService>();
	private static List<Image_DownloadService> Image_DownloadServiceInstances = new LinkedList<Image_DownloadService>();
	private static List<Preview_Design_DeveloperService> Preview_Design_DeveloperServiceInstances = new LinkedList<Preview_Design_DeveloperService>();
	
	static {
		AllServiceInstance.put("Work1System", Work1SystemInstances);
		AllServiceInstance.put("ThirdPartyServices", ThirdPartyServicesInstances);
		AllServiceInstance.put("Import_DesignService", Import_DesignServiceInstances);
		AllServiceInstance.put("Preview_Design_DesignerService", Preview_Design_DesignerServiceInstances);
		AllServiceInstance.put("Preview_DesignService", Preview_DesignServiceInstances);
		AllServiceInstance.put("Export_DesignService", Export_DesignServiceInstances);
		AllServiceInstance.put("AI_ConvertService", AI_ConvertServiceInstances);
		AllServiceInstance.put("Image_DownloadService", Image_DownloadServiceInstances);
		AllServiceInstance.put("Preview_Design_DeveloperService", Preview_Design_DeveloperServiceInstances);
	} 
	
	public static List getAllInstancesOf(String ClassName) {
			 return AllServiceInstance.get(ClassName);
	}	
	
	public static Work1System createWork1System() {
		Work1System s = new Work1SystemImpl();
		Work1SystemInstances.add(s);
		return s;
	}
	public static ThirdPartyServices createThirdPartyServices() {
		ThirdPartyServices s = new ThirdPartyServicesImpl();
		ThirdPartyServicesInstances.add(s);
		return s;
	}
	public static Import_DesignService createImport_DesignService() {
		Import_DesignService s = new Import_DesignServiceImpl();
		Import_DesignServiceInstances.add(s);
		return s;
	}
	public static Preview_Design_DesignerService createPreview_Design_DesignerService() {
		Preview_Design_DesignerService s = new Preview_Design_DesignerServiceImpl();
		Preview_Design_DesignerServiceInstances.add(s);
		return s;
	}
	public static Preview_DesignService createPreview_DesignService() {
		Preview_DesignService s = new Preview_DesignServiceImpl();
		Preview_DesignServiceInstances.add(s);
		return s;
	}
	public static Export_DesignService createExport_DesignService() {
		Export_DesignService s = new Export_DesignServiceImpl();
		Export_DesignServiceInstances.add(s);
		return s;
	}
	public static AI_ConvertService createAI_ConvertService() {
		AI_ConvertService s = new AI_ConvertServiceImpl();
		AI_ConvertServiceInstances.add(s);
		return s;
	}
	public static Image_DownloadService createImage_DownloadService() {
		Image_DownloadService s = new Image_DownloadServiceImpl();
		Image_DownloadServiceInstances.add(s);
		return s;
	}
	public static Preview_Design_DeveloperService createPreview_Design_DeveloperService() {
		Preview_Design_DeveloperService s = new Preview_Design_DeveloperServiceImpl();
		Preview_Design_DeveloperServiceInstances.add(s);
		return s;
	}
}	
