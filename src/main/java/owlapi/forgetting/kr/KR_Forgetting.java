package owlapi.forgetting.kr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class KR_Forgetting {


	public static void main(String[] args) throws OWLException, InstantiationException, IllegalAccessException,
	ClassNotFoundException, IOException {
		//String reasonerFactoryClassName = null;
		
		// We first need to obtain a copy of an OWLOntologyManager, which, as the name suggests, manages a set of ontologies.
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		// We load an ontology from the URI specified on the command line
		@Nonnull
		String ontologyPath = args[0];

		System.out.println("Starting Program...");
		System.out.println("--------------------");
		System.out.println("Loading ontology located at: " + ontologyPath);
		System.out.println("");

		// Now load the ontology.
		File ontologyDocument = new File(ontologyPath);
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyDocument);

		// Report information about the ontology
		System.out.println("Ontology Loaded!");
		System.out.println("Format : " + manager.getOntologyFormat(ontology));
		String parentDir = ontologyDocument.getParent();
		System.out.println("Parent Directory: " + parentDir);

		String input1 = args[1].toLowerCase();
		
		switch(input1) {
		  case "printallsubclasses":
			  getAllSubClasses(ontology);
		    break;
		  case "saveallsubclasses":
			  getAllSubClasses(ontology, parentDir);
		    break;
		  default:
			  System.out.println("Please give the program a valid second input");
		}	
	}
	
	
	@SuppressWarnings("deprecation")
	public static void getAllSubClasses(OWLOntology myOntology) {
    	for (final OWLSubClassOfAxiom subClass : myOntology.getAxioms(AxiomType.SUBCLASS_OF)){
    	    if (subClass.getSuperClass() instanceof OWLClass && subClass.getSubClass() instanceof OWLClass){       
    	    	System.out.println(subClass.getSubClass() + " <http://www.w3.org/2000/01/rdf-schema#subClassOf> " + subClass.getSuperClass());
    	    }
    	}
    }
	
	
	@SuppressWarnings("deprecation")
	public static void getAllSubClasses(OWLOntology myOntology, String parentDir) throws IOException {
		File subClassFile = new File(parentDir+"/subClasses.nt");
		subClassFile.createNewFile(); 
    	FileOutputStream fos = new FileOutputStream(subClassFile, false);   
    	
		for (final OWLSubClassOfAxiom subClass : myOntology.getAxioms(AxiomType.SUBCLASS_OF)){
    	    if (subClass.getSuperClass() instanceof OWLClass && subClass.getSubClass() instanceof OWLClass){	
    	    	String subClassStatement = subClass.getSubClass() + " <http://www.w3.org/2000/01/rdf-schema#subClassOf> " + subClass.getSuperClass() + " .";
                fos.write(subClassStatement.getBytes());
                fos.write(System.lineSeparator().getBytes());
    	    }
    	}
		System.out.println("");
		System.out.println("--------");
		System.out.println("Done! All subClass statements are saved at file: " + subClassFile);
		fos.close();
    }
	

}