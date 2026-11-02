package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class PedidoTest
{
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
        // numeroPedidos es estático, así que solo se verifica que cada pedido nuevo tenga el id siguiente
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
    void testPrecioTotalConProductoAjustado( )
    {
        ProductoAjustado ajustado = new ProductoAjustado( new ProductoMenu( "corral", 10000 ) );
        ajustado.agregarIngrediente( new Ingrediente( "huevo", 2000 ) );
        pedido.agregarProducto( ajustado );
        // neto 12000 + IVA 2280
        assertEquals( 14280, pedido.getPrecioTotalPedido( ) );
    }

    @Test
    void testGenerarTextoFacturaVacia( )
    {
        String esperado = "Cliente: Ana\n" + "Dirección: Calle 1\n" + "----------------\n" + "----------------\n"
                + "Precio Neto:  0\n" + "IVA:          0\n" + "Precio Total: 0\n";
        assertEquals( esperado, pedido.generarTextoFactura( ) );
    }

    @Test
    void testGenerarTextoFacturaConCombo( )
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
    void testGenerarTextoFacturaMezclandoProductos( )
    {
        pedido.agregarProducto( new ProductoMenu( "corral", 10000 ) );
        ProductoAjustado ajustado = new ProductoAjustado( new ProductoMenu( "papas", 5000 ) );
        ajustado.agregarIngrediente( new Ingrediente( "queso mozzarella", 2500 ) );
        pedido.agregarProducto( ajustado );

        String esperado = "Cliente: Ana\n" + "Dirección: Calle 1\n" + "----------------\n"
                + "corral\n" + "            10000\n"
                + "papas" + "    +queso mozzarella" + "                2500" + "            7500\n"
                + "----------------\n" + "Precio Neto:  17500\n" + "IVA:          3325\n"
                + "Precio Total: 20825\n";

        assertEquals( esperado, pedido.generarTextoFactura( ) );
    }

    @Test
    void testGuardarFactura( ) throws Exception
    {
        // La aplicación guarda las facturas en ./facturas/, así que la prueba usa esa misma carpeta
        File carpeta = new File( "./facturas/" );
        carpeta.mkdirs( );
        File archivo = new File( carpeta, "factura_prueba.txt" );

        pedido.agregarProducto( new ProductoMenu( "corral", 10000 ) );
        pedido.guardarFactura( archivo );

        String contenido = Files.readString( archivo.toPath( ), Charset.defaultCharset( ) );
        assertEquals( pedido.generarTextoFactura( ), contenido );

        archivo.delete( );
    }

    @Test
    void testGuardarFacturaEnCarpetaInexistente( )
    {
        File archivo = new File( "./carpetaQueNoExiste/factura.txt" );
        assertThrows( FileNotFoundException.class, ( ) -> pedido.guardarFactura( archivo ) );
    }

}
