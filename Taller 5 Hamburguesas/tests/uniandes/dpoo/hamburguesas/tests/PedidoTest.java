package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class PedidoTest {

    private Pedido pedido;

    @BeforeEach
    void setUp( )
    {
        pedido = new Pedido( "Ana", "Calle 1" );
    }

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Ana", pedido.getNombreCliente( ) );
    }

    @Test
    void testGetIdPedido( )
    {
        Pedido otro = new Pedido( "Luis", "Calle 2" );
        assertEquals( pedido.getIdPedido( ) + 1, otro.getIdPedido( ) );
    }

    @Test
    void testPrecioPedidoVacio( )
    {
        assertEquals( 0, pedido.getPrecioTotalPedido( ) );
    }

    @Test
    void testAgregarProductoYPrecioTotal( )
    {
        pedido.agregarProducto( new ProductoMenu( "corral", 10000 ) );
        pedido.agregarProducto( new ProductoMenu( "papas", 5000 ) );
        // neto 15000 + IVA 2850 (19%)
        assertEquals( 17850, pedido.getPrecioTotalPedido( ) );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>( );
        items.add( new ProductoMenu( "corral", 10000 ) );
        pedido.agregarProducto( new Combo( "combo corral", 0.1, items ) );

        String esperado = "Cliente: Ana\n" + "Dirección: Calle 1\n" + "----------------\n"
                + "Combo combo corral\n" + " Descuento: 0.1\n" + "            9000\n"
                + "----------------\n" + "Precio Neto:  9000\n" + "IVA:          1710\n"
                + "Precio Total: 10710\n";

        assertEquals( esperado, pedido.generarTextoFactura( ) );
    }

    @Test
    void testGuardarFactura( ) throws Exception
    {
        File archivo = File.createTempFile( "factura", ".txt" );
        archivo.deleteOnExit( );

        pedido.guardarFactura( archivo );

        String contenido = Files.readString( archivo.toPath( ), Charset.defaultCharset( ) );
        assertEquals( pedido.generarTextoFactura( ), contenido );
    }

}
