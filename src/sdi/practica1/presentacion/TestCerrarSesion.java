package sdi.practica1.presentacion;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.*;

public class TestCerrarSesion {
	
	 	private WebTester usuario1;

		@Before
	    public void prepare() {
	    	usuario1=new WebTester();
	    	usuario1.setBaseUrl("http://localhost:8280/UO244795");
	    }

	    @Test
	    public void testIniciarYCerrarSesionConExito() {
	    	usuario1.beginAt("/"); 
	    	usuario1.assertFormPresent("validarse_form_name");  
	    	usuario1.setTextField("nombreUsuario", "usuario1"); 
	    	usuario1.setTextField("password", "usuario1");
	    	usuario1.submit(); 
	    	usuario1.assertTitleEquals("TaskManager - Página principal del usuario");
	    	usuario1.assertTextInElement("login", "usuario1");  
	    	usuario1.assertTextInElement("id", "1");  
	    	usuario1.assertTextPresent("Iniciaste sesión el"); 
	    	usuario1.assertLinkPresent("cerrarSesion_link_id");
	    	usuario1.clickLink("cerrarSesion_link_id"); 
	    	
	    	WebTester anonimo = new WebTester();
	    	anonimo.setBaseUrl("http://localhost:8280/UO244795");
	    	anonimo.beginAt("/");
	    	anonimo.assertFormPresent("validarse_form_name");
	    	anonimo.assertTextPresent("Inicie sesión");
	    }
}
