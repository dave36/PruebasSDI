package sdi.practica1.presentacion;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.*;

public class TestRegistroYNavegacion {
	private WebTester usuarioAnonimo;


	@Before
    public void prepare() {
    	usuarioAnonimo=new WebTester();
    	usuarioAnonimo.setBaseUrl("http://localhost:8280/UO244795");
    }
	
	@Test
	public void testRegistroYNavegacion(){
		usuarioAnonimo.beginAt("/");  
    	usuarioAnonimo.assertLinkPresent("registrarse_link_id"); 
    	usuarioAnonimo.clickLink("registrarse_link_id");
    	
    	usuarioAnonimo.assertTitleEquals("Registro");
    	usuarioAnonimo.setTextField("login", "usuarioPruebas");
    	usuarioAnonimo.setTextField("email", "uP@mail.es");
    	usuarioAnonimo.setTextField("password", "usuarioPruebas1");
    	usuarioAnonimo.setTextField("password-confirmacion", "usuarioPruebas1");
    	usuarioAnonimo.submit();
    	
    	usuarioAnonimo.assertTitleEquals("TaskManager - Inicie sesión");
    	
    	//Inicio sesion
    	usuarioAnonimo.beginAt("/");  
    	usuarioAnonimo.assertFormPresent("validarse_form_name");  
    	usuarioAnonimo.setTextField("nombreUsuario", "usuarioPruebas"); 
    	usuarioAnonimo.setTextField("password", "usuarioPruebas1");
    	usuarioAnonimo.submit(); 
    	
    	//Principal usuario
    	usuarioAnonimo.assertTitleEquals("TaskManager - Página principal del usuario");
    	usuarioAnonimo.assertLinkPresent("listarTareas_id");
    	usuarioAnonimo.clickLink("listarTareas_id");
    	
    	//Listar tareas
    	usuarioAnonimo.assertLinkPresent("gestionarCategorias_id");
    	usuarioAnonimo.clickLink("gestionarCategorias_id");
    	
    	//Gestion de categorias
    	usuarioAnonimo.assertLinkPresent("nuevaCategoria_id");
    	usuarioAnonimo.clickLink("nuevaCategoria_id");
    	
    	//Añadir categoria
    	usuarioAnonimo.assertTitleEquals("Añadir categoria");
    	usuarioAnonimo.setTextField("name", "Categoria Test");
    	usuarioAnonimo.submit();
    	
    	usuarioAnonimo.assertTextPresent("Categoria Test");
	}
}
