package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

class RestauranteTest
{
    // Archivos reales de la aplicación
    private static final File ARCHIVO_INGREDIENTES = new File( "./data/ingredientes.txt" );
    private static final File ARCHIVO_MENU = new File( "./data/menu.txt" );
    private static final File ARCHIVO_COMBOS = new File( "./data/combos.txt" );

    // Archivos creados para las pruebas (con errores a propósito o con líneas vacías)
    private static final File INGREDIENTES_REPETIDOS = new File( "./data/pruebas/ingredientes_repetidos.txt" );
    private static final File MENU_REPETIDO = new File( "./data/pruebas/menu_repetido.txt" );
    private static final File COMBOS_REPETIDOS = new File( "./data/pruebas/combos_repetidos.txt" );
    private static final File COMBOS_PRODUCTO_FALTANTE = new File( "./data/pruebas/combos_producto_faltante.txt" );
    private static final File INGREDIENTES_LINEAS_VACIAS = new File( "./data/pruebas/ingredientes_lineas_vacias.txt" );
    private static final File MENU_LINEAS_VACIAS = new File( "./data/pruebas/menu_lineas_vacias.txt" );
    private static final File COMBOS_LINEAS_VACIAS = new File( "./data/pruebas/combos_lineas_vacias.txt" );
    private static final File NO_EXISTE = new File( "./data/pruebas/noExiste.txt" );

    private Restaurante restaurante;

    @BeforeEach
    void setUp( )
    {
        restaurante = new Restaurante( );
    }

    // ***************************************
    // Estado inicial
    // ***************************************

    @Test
    void testRestauranteNuevoEstaVacio( )
    {
        assertNull( restaurante.getPedidoEnCurso( ) );
        assertTrue( restaurante.getPedidos( ).isEmpty( ) );
        assertTrue( restaurante.getIngredientes( ).isEmpty( ) );
        assertTrue( restaurante.getMenuBase( ).isEmpty( ) );
        assertTrue( restaurante.getMenuCombos( ).isEmpty( ) );
    }

    // ***************************************
    // Pedidos
    // ***************************************

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
        assertEquals( "Ana", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "El pedido en curso no debe cambiar" );
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
        Pedido pedido = restaurante.getPedidoEnCurso( );
        pedido.agregarProducto( new ProductoMenu( "corral", 14000 ) );
        int id = pedido.getIdPedido( );
        String textoEsperado = pedido.generarTextoFactura( );

        restaurante.cerrarYGuardarPedido( );

        assertNull( restaurante.getPedidoEnCurso( ) );
        assertEquals( 1, restaurante.getPedidos( ).size( ) );
        assertSame( pedido, restaurante.getPedidos( ).get( 0 ) );

        File factura = new File( "./facturas/factura_" + id + ".txt" );
        assertTrue( factura.exists( ) );
        assertEquals( textoEsperado, Files.readString( factura.toPath( ), Charset.defaultCharset( ) ) );
        factura.delete( );
    }

    @Test
    void testIniciarOtroPedidoDespuesDeCerrar( ) throws Exception
    {
        new File( "./facturas/" ).mkdirs( );
        restaurante.iniciarPedido( "Ana", "Calle 1" );
        int id = restaurante.getPedidoEnCurso( ).getIdPedido( );
        restaurante.cerrarYGuardarPedido( );

        restaurante.iniciarPedido( "Luis", "Calle 2" );

        assertEquals( "Luis", restaurante.getPedidoEnCurso( ).getNombreCliente( ) );
        new File( "./facturas/factura_" + id + ".txt" ).delete( );
    }

    // ***************************************
    // Carga de información
    // ***************************************

    @Test
    void testCargarInformacionRestaurante( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( ARCHIVO_INGREDIENTES, ARCHIVO_MENU, ARCHIVO_COMBOS );

        assertEquals( 15, restaurante.getIngredientes( ).size( ) );
        assertEquals( "lechuga", restaurante.getIngredientes( ).get( 0 ).getNombre( ) );
        assertEquals( 1000, restaurante.getIngredientes( ).get( 0 ).getCostoAdicional( ) );

        assertEquals( 22, restaurante.getMenuBase( ).size( ) );
        assertEquals( "corral", restaurante.getMenuBase( ).get( 0 ).getNombre( ) );
        assertEquals( 14000, restaurante.getMenuBase( ).get( 0 ).getPrecio( ) );

        assertEquals( 4, restaurante.getMenuCombos( ).size( ) );
        assertEquals( "combo corral", restaurante.getMenuCombos( ).get( 0 ).getNombre( ) );
        // corral 14000 + papas medianas 5500 + gaseosa 5000 = 24500, con 10% de descuento
        assertEquals( 22050, restaurante.getMenuCombos( ).get( 0 ).getPrecio( ) );
        // especial 24000 + papas medianas 5500 + gaseosa 5000 = 34500, con 9.5% de descuento
        assertEquals( "combo especial", restaurante.getMenuCombos( ).get( 3 ).getNombre( ) );
        assertEquals( 31222, restaurante.getMenuCombos( ).get( 3 ).getPrecio( ) );
    }

    @Test
    void testCargarArchivosConLineasVacias( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( INGREDIENTES_LINEAS_VACIAS, MENU_LINEAS_VACIAS, COMBOS_LINEAS_VACIAS );

        assertEquals( 2, restaurante.getIngredientes( ).size( ) );
        assertEquals( 2, restaurante.getMenuBase( ).size( ) );
        assertEquals( 1, restaurante.getMenuCombos( ).size( ) );
        // (14000 + 5500) con 10% de descuento
        assertEquals( 17550, restaurante.getMenuCombos( ).get( 0 ).getPrecio( ) );
    }

    @Test
    void testIngredienteRepetido( )
    {
        assertThrows( IngredienteRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( INGREDIENTES_REPETIDOS, ARCHIVO_MENU, ARCHIVO_COMBOS ) );
    }

    @Test
    void testProductoRepetidoEnMenu( )
    {
        assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ARCHIVO_INGREDIENTES, MENU_REPETIDO, ARCHIVO_COMBOS ) );
    }

    @Test
    void testComboRepetido( )
    {
        assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ARCHIVO_INGREDIENTES, ARCHIVO_MENU, COMBOS_REPETIDOS ) );
    }

    @Test
    void testComboConProductoFaltante( )
    {
        assertThrows( ProductoFaltanteException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ARCHIVO_INGREDIENTES, ARCHIVO_MENU, COMBOS_PRODUCTO_FALTANTE ) );
    }

    @Test
    void testArchivoInexistente( )
    {
        assertThrows( IOException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( NO_EXISTE, ARCHIVO_MENU, ARCHIVO_COMBOS ) );
    }

}
