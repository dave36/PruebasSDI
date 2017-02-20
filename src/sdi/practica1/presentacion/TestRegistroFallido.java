package sdi.practica1.presentacion;

import net.sourceforge.jwebunit.junit.WebTester;

import org.junit.*;

public class TestRegistroFallido {
	private WebTester usuarioAnonimo;


	@Before
    public void prepare() {
    	usuarioAnonimo=new WebTester();
    	usuarioAnonimo.setBaseUrl("http://localhost:8280/UO244795");
    }
	
	@Test
	public void testFallidoRegistro(){
		usuarioAnonimo.beginAt("/");  
    	usuarioAnonimo.assertLinkPresent("registrarse_link_id"); 
    	usuarioAnonimo.clickLink("registrarse_link_id");
    	
    	usuarioAnonimo.assertTitleEquals("Registro");
    	
    	try{
    		usuarioAnonimo.submit();
    	}
    	catch(Exception e){
    		System.out.println(e.getMessage());
    	}
    	
    	usuarioAnonimo.assertTextPresent("error");
	}
}
