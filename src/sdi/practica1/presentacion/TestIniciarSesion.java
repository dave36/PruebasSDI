package sdi.practica1.presentacion;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.*;

public class TestIniciarSesion {

    private WebTester usuario1;


	@Before
    public void prepare() {
    	usuario1=new WebTester();
    	usuario1.setBaseUrl("http://localhost:8280/UO244795");
    }

    @Test
    public void testIniciarSesionConExito() {
    	usuario1.beginAt("/");  
    	usuario1.assertFormPresent("validarse_form_name");  
    	usuario1.setTextField("nombreUsuario", "usuario1"); 
    	usuario1.setTextField("password", "usuario1");
    	usuario1.submit(); 
    	usuario1.assertTitleEquals("TaskManager - Página principal del usuario");  
    	usuario1.assertTextInElement("login", "usuario1");  
    	usuario1.assertTextInElement("id", "1");  
    	usuario1.assertTextPresent("Iniciaste sesión el"); 
    }

    
    @Test
    public void testIniciarSesionSinExito() {
    	WebTester browser=new WebTester();
    	browser.setBaseUrl("http://localhost:8280/UO244795");        
    	browser.beginAt("/");  
    	browser.setTextField("nombreUsuario", "yoNoExisto"); 
    	browser.setTextField("password", "nohaypassword");
    	browser.submit(); 
    	browser.assertTitleEquals("TaskManager - Inicie sesión");
    }

}