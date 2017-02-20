package sdi.practica1.presentacion;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestIniciarSesion.class,
	TestCerrarSesion.class,
	TestRegistroFallido.class,
	TestRegistroYNavegacion.class,
	TestListarCategorias.class,
	TestListarHoy.class,
	TestListarInbox.class,
	TestListarSemana.class,
	TestModificarTarea.class,
	TestFinalizarTarea.class
})
public class AllTest {

}
