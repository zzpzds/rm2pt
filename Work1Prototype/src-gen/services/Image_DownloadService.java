package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface Image_DownloadService {

	/* all system operations of the use case*/
	float download_image(String design_id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
