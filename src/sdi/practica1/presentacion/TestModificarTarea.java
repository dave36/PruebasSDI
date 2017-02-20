package sdi.practica1.presentacion;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.Before;
import org.junit.Test;

public class TestModificarTarea {

	
	private WebTester usuario;


	@Before
    public void prepare() {
    	usuario=new WebTester();
    	usuario.setBaseUrl("http://localhost:8280/UO244795");
    }
	
	
	@Test
	public void testModificarTarea() {
		usuario.beginAt("/");
		usuario.setTextField("nombreUsuario", "usuario1");
		usuario.setTextField("password", "usuario1");
		usuario.submit();
		usuario.clickLink("listarTareas_id");
		usuario.assertTextPresent("Modificar");	
		
		usuario.assertTitleEquals("Today Tasks");
		usuario.assertTextPresent("Listado de tareas");
		usuario.assertTextPresent("Título");
		usuario.assertTextPresent("Creada");	
	}
}
