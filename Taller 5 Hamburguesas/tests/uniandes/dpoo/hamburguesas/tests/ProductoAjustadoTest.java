package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class ProductoAjustadoTest
{
    private ProductoAjustado producto;

    private Ingrediente queso;

    private Ingrediente tomate;

    @BeforeEach
    void setUp( ) throws Exception
    {
        ProductoMenu prodMenu = new ProductoMenu( "Quesadillas con Guac", 24000 );
        producto = new ProductoAjustado( prodMenu );
        queso = new Ingrediente( "queso mozzarella", 2500 );
        tomate = new Ingrediente( "tomate", 1000 );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "Quesadillas con Guac", producto.getNombre( ), "El nombre del producto no es el esperado" );
    }

    @Test
    void testGetPrecioSinAjustes( )
    {
        assertEquals( 24000, producto.getPrecio( ), "El precio que retornó el producto no es el esperado" );
    }

    @Test
    void testGetPrecioConAgregados( )
    {
        producto.agregarIngrediente( queso );
        producto.agregarIngrediente( tomate );
        assertEquals( 27500, producto.getPrecio( ), "El precio debe sumar el costo de los ingredientes agregados" );
    }

    @Test
    void testMismoIngredienteAgregadoVariasVeces( )
    {
        producto.agregarIngrediente( queso );
        producto.agregarIngrediente( queso );
        assertEquals( 29000, producto.getPrecio( ), "Un mismo ingrediente puede agregarse varias veces y cada una cuesta" );
    }

    @Test
    void testEliminarIngredienteNoCambiaElPrecio( )
    {
        producto.eliminarIngrediente( tomate );
        assertEquals( 24000, producto.getPrecio( ), "Quitar ingredientes no debe reducir el precio" );
    }

    @Test
    void testGenerarTextoFacturaSinAjustes( )
    {
        String esperado = "Quesadillas con Guac" + "            " + "24000" + "\n";
        assertEquals( esperado, producto.generarTextoFactura( ), "La impresión de la factura fue incorrecta" );
    }

    @Test
    void testGenerarTextoFacturaConAjustes( )
    {
        producto.agregarIngrediente( queso );
        producto.eliminarIngrediente( tomate );

        String esperado = "Quesadillas con Guac" + "    +queso mozzarella" + "                2500" + "    -tomate" + "            26500" + "\n";
        assertEquals( esperado, producto.generarTextoFactura( ), "La impresión de la factura con ajustes fue incorrecta" );
    }

}
