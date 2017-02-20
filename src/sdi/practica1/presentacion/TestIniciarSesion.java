package sdi.practica1.presentacion;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.*;

public class TestIniciarSesion {

    private WebTester mary;


	@Before
    public void prepare() {
    	mary=new WebTester();
    	mary.setBaseUrl("http://localhost:8280/sesion3.MVCCasero");
    }

    @Test
    public void testIniciarSesionConExito() {
    	mary.beginAt("/");  // Navegar a la URL
    	mary.assertFormPresent("validarse_form_name");  // Comprobar formulario está presente
    	mary.setTextField("nombreUsuario", "john"); // Rellenar primer campo de formulario
    	mary.submit(); // Enviar formulario
    	mary.assertTitleEquals("TaskManager - Página principal del usuario");  // Comprobar título de la página
    	mary.assertTextInElement("login", "john");  // Comprobar cierto elemento contiene cierto texto
    	mary.assertTextInElement("id", "2");  // Comprobar cierto elemento contiene cierto texto
    	mary.assertTextPresent("Iniciaste sesión el"); // Comprobar cierto texto está presente
    }

    
    @Test
    public void testIniciarSesionSinExito() {
    	WebTester browser=new WebTester();
    	browser.setBaseUrl("http://localhost:8280/sesion3.MVCCasero");        
    	browser.beginAt("/");  // Navegar a la URL
    	browser.setTextField("nombreUsuario", "yoNoExisto"); // Rellenar primer campo de formulario
    	browser.submit(); // Enviar formulario
    	browser.assertTitleEquals("TaskManager - Inicie sesión");  // Comprobar título de la página
    }

}