package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

class RestauranteTest {

	    private static final File ARCHIVO_INGREDIENTES = new File( "./data/ingredientes.txt" );
	    private static final File ARCHIVO_MENU = new File( "./data/menu.txt" );
	    private static final File ARCHIVO_COMBOS = new File( "./data/combos.txt" );

	    private Restaurante restaurante;

	    @BeforeEach
	    void setUp( )
	    {
	        restaurante = new Restaurante( );
	    }

	    @Test
	    void testIniciarPedido( ) throws Exception
	    {
	        restaurante.iniciarPedido( "Ana", "Calle 1" );
	        assertNotNull( restaurante.getPedidoEnCurso( ) );
	        assertEquals( "Ana", restaurante.getPedidoEnCurso( ).getNombreCliente( ) );
	    }

	    @Test
	    void testIniciarPedidoYaHayUnoEnCurso( ) throws Exception
	    {
	        restaurante.iniciarPedido( "Ana", "Calle 1" );
	        assertThrows( YaHayUnPedidoEnCursoException.class, ( ) -> restaurante.iniciarPedido( "Luis", "Calle 2" ) );
	    }

	    @Test
	    void testCerrarSinPedidoEnCurso( )
	    {
	        assertThrows( NoHayPedidoEnCursoException.class, ( ) -> restaurante.cerrarYGuardarPedido( ) );
	    }

	    @Test
	    void testCerrarYGuardarPedido( ) throws Exception
	    {
	        new File( "./facturas/" ).mkdirs( );
	        restaurante.iniciarPedido( "Ana", "Calle 1" );
	        int id = restaurante.getPedidoEnCurso( ).getIdPedido( );

	        restaurante.cerrarYGuardarPedido( );

	        assertNull( restaurante.getPedidoEnCurso( ) );
	        assertEquals( 1, restaurante.getPedidos( ).size( ) );
	        assertTrue( new File( "./facturas/factura_" + id + ".txt" ).exists( ) );
	    }

	    @Test
	    void testCargarInformacionRestaurante( ) throws Exception
	    {
	        restaurante.cargarInformacionRestaurante( ARCHIVO_INGREDIENTES, ARCHIVO_MENU, ARCHIVO_COMBOS );

	        // Cambia estos por los números reales de tus archivos
	        assertFalse( restaurante.getIngredientes( ).isEmpty( ) );
	        assertFalse( restaurante.getMenuBase( ).isEmpty( ) );
	        assertFalse( restaurante.getMenuCombos( ).isEmpty( ) );
	    }
}
