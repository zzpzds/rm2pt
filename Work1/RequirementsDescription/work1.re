As a designer, I want to select and upload design drafts to platform, so that all design images are centralized
{
	Basic Flow {
		(User)1. the designer shall opens a file dialog and selects design image.
		(System)2. the platform shall validates file format.
		(System)3. the platform shall uploads image to database and generates thumbnail previews and stores metadata.
	}
	Alternative Flow {
		A. At any time, Invalid file format detected :
			1. Displays an error message like Unsupported file format and Please upload PNG JPG SVG.
		B. At any time, Upload failure :
			1. Notify user like Upload failed and Please try again.
		C. At any time, File exceeds a size limit :
			1. Shows like File size exceeds limit and Maximum allowed xMB.
	}
}
As a developer, I want to review all design images and select a method to convert images to code
{
	Basic Flow {
		(System)1. the platform shall displays a paginated list of design images with thumbnails and metadata and tags.
		(System)2. the platform shall enables filtering and searching by tags and upload date or designer ID.
		(User)3. the developer shall selects a design image.
		(System)4. the platform shall provides zoomable high resolution previews for detailed inspection.
	}
	Alternative Flow {
		A. At any time, No design images available :
			1. Shows a placeholder message like No designs found and Upload a design first.
	}
}
As a developer, I want to choose between AI generated code or manual development for selected design
{
	Basic Flow {
		(User)1. the developer shall selects a design image.
		(System)2. the Prompts developer shall to choose a conversion method between AI Auto Convert and Manual Development.
		(System)3. the AI mode platform shall allows specifying code preferences.
		(User)4. the developer shall selects desired option.
		(System)5. the platform shall mark status of selected design image to doing.
	}
	Alternative Flow {
		A. At any time, AI service unavailable :
			1. notify AI service is temporarily unavailable and please try manual mode.
	}
}
As a developer, I want to platform to auto convert design into code via AI and provide downloadable results
{
	Basic Flow {
		(User)1. the developer shall selects AI Auto Convert.
		(System)2. the platform shall calls an AI conversion service and generates clean and annotated code.
		(System)3. the platform shall validates code integrity and packages it into a ZIP file.
		(System)4. the platform shall triggers a download and logs activity.
		(User)5. the developer shall select a file position for download result ZIP file.
	}
	Alternative Flow {
		A. At any time, AI confidence score lower than ninty percent :
			1. Warns Conversion may require manual adjustments and Proceed.
		B. At any time, Code validation errors :
			2. Highlights errors and offers retry options.
	}
}
As a developer, I want to download design image for manual development
{
	Basic Flow {
		(User)1. the developer shall selects Manual Development.
		(System)2. the platform shall provides original design image as a downloadable file.
		(System)3. the platform shall extracts design assets into a folder.
	}
	Alternative Flow {
		A. At any time, Image corruption detected :
			1. Notify Image file is corrupted and Please reupload design.
	}
}
As a designer, I want to know whether any developer had accepted my design image
{
	Basic Flow {
		(System)1. the platform shall send a message to designer after any developer accepted design image.
		(User)2. the user shall receive palaform messages in person message center.
	}
}