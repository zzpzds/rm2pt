package entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Map;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class EntityManager {

	private static Map<String, List> AllInstance = new HashMap<String, List>();
	
	private static List<Designer> DesignerInstances = new LinkedList<Designer>();
	private static List<DesignImage> DesignImageInstances = new LinkedList<DesignImage>();
	private static List<Developer> DeveloperInstances = new LinkedList<Developer>();
	private static List<Message> MessageInstances = new LinkedList<Message>();
	private static List<AIProcess> AIProcessInstances = new LinkedList<AIProcess>();
	private static List<DesignList> DesignListInstances = new LinkedList<DesignList>();

	
	/* Put instances list into Map */
	static {
		AllInstance.put("Designer", DesignerInstances);
		AllInstance.put("DesignImage", DesignImageInstances);
		AllInstance.put("Developer", DeveloperInstances);
		AllInstance.put("Message", MessageInstances);
		AllInstance.put("AIProcess", AIProcessInstances);
		AllInstance.put("DesignList", DesignListInstances);
	} 
		
	/* Save State */
	public static void save(File file) {
		
		try {
			
			ObjectOutputStream stateSave = new ObjectOutputStream(new FileOutputStream(file));
			
			stateSave.writeObject(DesignerInstances);
			stateSave.writeObject(DesignImageInstances);
			stateSave.writeObject(DeveloperInstances);
			stateSave.writeObject(MessageInstances);
			stateSave.writeObject(AIProcessInstances);
			stateSave.writeObject(DesignListInstances);
			
			stateSave.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* Load State */
	public static void load(File file) {
		
		try {
			
			ObjectInputStream stateLoad = new ObjectInputStream(new FileInputStream(file));
			
			try {
				
				DesignerInstances =  (List<Designer>) stateLoad.readObject();
				AllInstance.put("Designer", DesignerInstances);
				DesignImageInstances =  (List<DesignImage>) stateLoad.readObject();
				AllInstance.put("DesignImage", DesignImageInstances);
				DeveloperInstances =  (List<Developer>) stateLoad.readObject();
				AllInstance.put("Developer", DeveloperInstances);
				MessageInstances =  (List<Message>) stateLoad.readObject();
				AllInstance.put("Message", MessageInstances);
				AIProcessInstances =  (List<AIProcess>) stateLoad.readObject();
				AllInstance.put("AIProcess", AIProcessInstances);
				DesignListInstances =  (List<DesignList>) stateLoad.readObject();
				AllInstance.put("DesignList", DesignListInstances);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	/* create object */  
	public static Object createObject(String Classifer) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method createObjectMethod = c.getDeclaredMethod("create" + Classifer + "Object");
			return createObjectMethod.invoke(c);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* add object */  
	public static Object addObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectMethod = c.getDeclaredMethod("add" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) addObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	/* add objects */  
	public static Object addObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectsMethod = c.getDeclaredMethod("add" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) addObjectsMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* Release object */
	public static boolean deleteObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) deleteObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/* Release objects */
	public static boolean deleteObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) deleteObjectMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}		 	
	
	 /* Get all objects belongs to same class */
	public static List getAllInstancesOf(String ClassName) {
			 return AllInstance.get(ClassName);
	}	

   /* Sub-create object */
	public static Designer createDesignerObject() {
		Designer o = new Designer();
		return o;
	}
	
	public static boolean addDesignerObject(Designer o) {
		return DesignerInstances.add(o);
	}
	
	public static boolean addDesignerObjects(List<Designer> os) {
		return DesignerInstances.addAll(os);
	}
	
	public static boolean deleteDesignerObject(Designer o) {
		return DesignerInstances.remove(o);
	}
	
	public static boolean deleteDesignerObjects(List<Designer> os) {
		return DesignerInstances.removeAll(os);
	}
	public static DesignImage createDesignImageObject() {
		DesignImage o = new DesignImage();
		return o;
	}
	
	public static boolean addDesignImageObject(DesignImage o) {
		return DesignImageInstances.add(o);
	}
	
	public static boolean addDesignImageObjects(List<DesignImage> os) {
		return DesignImageInstances.addAll(os);
	}
	
	public static boolean deleteDesignImageObject(DesignImage o) {
		return DesignImageInstances.remove(o);
	}
	
	public static boolean deleteDesignImageObjects(List<DesignImage> os) {
		return DesignImageInstances.removeAll(os);
	}
	public static Developer createDeveloperObject() {
		Developer o = new Developer();
		return o;
	}
	
	public static boolean addDeveloperObject(Developer o) {
		return DeveloperInstances.add(o);
	}
	
	public static boolean addDeveloperObjects(List<Developer> os) {
		return DeveloperInstances.addAll(os);
	}
	
	public static boolean deleteDeveloperObject(Developer o) {
		return DeveloperInstances.remove(o);
	}
	
	public static boolean deleteDeveloperObjects(List<Developer> os) {
		return DeveloperInstances.removeAll(os);
	}
	public static Message createMessageObject() {
		Message o = new Message();
		return o;
	}
	
	public static boolean addMessageObject(Message o) {
		return MessageInstances.add(o);
	}
	
	public static boolean addMessageObjects(List<Message> os) {
		return MessageInstances.addAll(os);
	}
	
	public static boolean deleteMessageObject(Message o) {
		return MessageInstances.remove(o);
	}
	
	public static boolean deleteMessageObjects(List<Message> os) {
		return MessageInstances.removeAll(os);
	}
	public static AIProcess createAIProcessObject() {
		AIProcess o = new AIProcess();
		return o;
	}
	
	public static boolean addAIProcessObject(AIProcess o) {
		return AIProcessInstances.add(o);
	}
	
	public static boolean addAIProcessObjects(List<AIProcess> os) {
		return AIProcessInstances.addAll(os);
	}
	
	public static boolean deleteAIProcessObject(AIProcess o) {
		return AIProcessInstances.remove(o);
	}
	
	public static boolean deleteAIProcessObjects(List<AIProcess> os) {
		return AIProcessInstances.removeAll(os);
	}
	public static DesignList createDesignListObject() {
		DesignList o = new DesignList();
		return o;
	}
	
	public static boolean addDesignListObject(DesignList o) {
		return DesignListInstances.add(o);
	}
	
	public static boolean addDesignListObjects(List<DesignList> os) {
		return DesignListInstances.addAll(os);
	}
	
	public static boolean deleteDesignListObject(DesignList o) {
		return DesignListInstances.remove(o);
	}
	
	public static boolean deleteDesignListObjects(List<DesignList> os) {
		return DesignListInstances.removeAll(os);
	}
  
}

