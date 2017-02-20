package sdi.practica1.presentacion;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.Before;
import org.junit.Test;

public class TestListarHoy {

	
	private WebTester usuario;


	@Before
    public void prepare() {
    	usuario=new WebTester();
    	usuario.setBaseUrl("http://localhost:8280/UO244795");
    }
	
	
	@Test
	public void testListarHoy() {
		usuario.beginAt("/");
		usuario.setTextField("nombreUsuario", "usuario1");
		usuario.setTextField("password", "usuario1");
		usuario.submit();
		
		usuario.assertTitleEquals("TaskManager - Página principal del usuario");
		usuario.assertTextPresent("Tareas");
		
		usuario.clickLink("listarTareas_id");
		usuario.assertTextPresent("Listado de tareas");
		usuario.assertTextPresent("Título");
		usuario.assertTextPresent("Creada");		
		
		usuario.assertTitleEquals("Today Tasks");
		usuario.assertTextPresent("Listado de tareas");
		usuario.assertTextPresent("Título");
		usuario.assertTextPresent("Creada");
		
	}
	
	
}
