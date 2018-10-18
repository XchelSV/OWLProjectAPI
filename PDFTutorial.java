package api;
import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Path("/pdf")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDFTutorial {

	OWLDataFactory factory = OWLManager.getOWLDataFactory();
	
  @POST
  @Path("/onthology/create/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response sayHello(@PathParam("name") String name) throws Exception {
	  
	  File f = new File("/Users/xchelsvz/"+name+".owl");
	  f.getParentFile().mkdirs(); 
	  f.createNewFile();
	  
	  OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	  OWLOntology localPizza = manager.loadOntologyFromOntologyDocument(f);
	  System.out.println("Loaded ontology: " + localPizza);
	  
	  /*String base = "http://example.com/owl/families/";
	  PrefixManager pm = new DefaultPrefixManager(base);
	  OWLClass person = df.getOWLClass(":Person", pm);
	  OWLNamedIndividual mary = df.getOWLNamedIndividual(":Mary", pm);
	  OWLClassAssertionAxiom classAssertion = df.getOWLClassAssertionAxiom(person, mary);
	  
	  AddAxiom addAxiom = new AddAxiom(localPizza, classAssertion);
	  // We now use the manager to apply the change
	  manager.applyChange(addAxiom);*/
	  
	  manager.saveOntology(localPizza, IRI.create(f.toURI()));
	  
	  
    return Response.ok("Hello World desde el API REST",MediaType.APPLICATION_JSON).build();

  }
  
  @POST
  @Path("/onthology/create/class/{id}/{class_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createClass(@PathParam("id") String id , @PathParam("class_id") String class_id ) throws Exception {
	  File f = new File("/Users/xchelsvz/"+id+".owl");
	  f.getParentFile().mkdirs(); 
	  f.createNewFile();
	  
	  OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	  OWLOntology ontho = manager.loadOntologyFromOntologyDocument(f);
	  
	  String base = "http://example.com/owl/families/";
	  PrefixManager pm = new DefaultPrefixManager(base);
	  
	  OWLClass person = factory.getOWLClass(":"+class_id, pm);
	  OWLDeclarationAxiom declarationAxiom = factory.getOWLDeclarationAxiom(person);
	  
	  manager.addAxiom(ontho, declarationAxiom);
	  
	  manager.saveOntology(ontho, IRI.create(f.toURI()));
	  return Response.ok("Clase creada",MediaType.APPLICATION_JSON).build();
  }
  
  @POST
  @Path("/onthology/create/subclass/{id}/{class_id_a}/{class_id_b}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createClass(@PathParam("id") String id , @PathParam("class_id_a") String class_id_a , @PathParam("class_id_b") String class_id_b ) throws Exception {
	  File f = new File("/Users/xchelsvz/"+id+".owl");
	  f.getParentFile().mkdirs(); 
	  f.createNewFile();
	  
	  OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	  OWLOntology ontho = manager.loadOntologyFromOntologyDocument(f);
	  
	  String base = "http://example.com/owl/families/";
	  PrefixManager pm = new DefaultPrefixManager(base);
	  
	  OWLClass class_a = factory.getOWLClass(":"+class_id_a, pm);
	  OWLClass class_b = factory.getOWLClass(":"+class_id_b, pm);
	  OWLAxiom axiom = factory.getOWLSubClassOfAxiom(class_a, class_b);
	  
	  AddAxiom addAxiom = new AddAxiom(ontho, axiom);
	  manager.applyChange(addAxiom);
	  
	  manager.saveOntology(ontho, IRI.create(f.toURI()));
	  
	  // The ontology will now contain references to class A and class B.
	  for (OWLClass cls : ontho.getClassesInSignature()) { 
		  System.out.println("Referenced class: " + cls);
	  }
	  return Response.ok("Clase creada",MediaType.APPLICATION_JSON).build();
  }
  
  /*@POST
  @Path("/onthology/create/property/{id}/{class_id_a}/{class_id_b}/{property_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createProperty(@PathParam("id") String id , @PathParam("class_id_a") String class_id_a, @PathParam("class_id_b") String class_id_b , @PathParam("property_id") String property_id ) throws Exception {
	  File f = new File("/Users/xchelsvz/"+id+".owl");
	  f.getParentFile().mkdirs(); 
	  f.createNewFile();
	  
	  OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	  OWLOntology ontho = manager.loadOntologyFromOntologyDocument(f);
	  
	  String base = "http://example.com/owl/families/";
	  PrefixManager pm = new DefaultPrefixManager(base);
	  
	  OWLClass class_a = factory.getOWLClass(":"+class_id_a, pm);
	  OWLClass class_b = factory.getOWLClass(":"+class_id_b, pm);
	  
	  OWLObjectProperty hasProp = factory.getOWLObjectProperty( ":"+property_id, pm);
	  OWLObjectPropertyAssertionAxiom assertion = factory.getOWLObjectPropertyAssertionAxiom(hasProp, class_a, class_b);
	  
	  manager.saveOntology(ontho, IRI.create(f.toURI()));
	  return Response.ok("Clase creada",MediaType.APPLICATION_JSON).build();
  }*/
  
  
  @POST
  @Path("/onthology/create/instance/{id}/{class_id}/{instance_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createInstance(@PathParam("id") String id , @PathParam("class_id") String class_id, @PathParam("instance_id") String instance_id ) throws Exception {
	  File f = new File("/Users/xchelsvz/"+id+".owl");
	  f.getParentFile().mkdirs(); 
	  f.createNewFile();
	  
	  OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	  OWLOntology ontho = manager.loadOntologyFromOntologyDocument(f);
	  
	  String base = "http://example.com/owl/families/";
	  PrefixManager pm = new DefaultPrefixManager(base);
	  
	  OWLClass class_a = factory.getOWLClass(":"+class_id, pm);
	  OWLNamedIndividual instance_a = factory.getOWLNamedIndividual(":"+instance_id, pm);
	  
	  OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(class_a, instance_a);
	  manager.addAxiom(ontho, classAssertion);
	  
	  manager.saveOntology(ontho, IRI.create(f.toURI()));
	  return Response.ok("Clase creada",MediaType.APPLICATION_JSON).build();
  }
}