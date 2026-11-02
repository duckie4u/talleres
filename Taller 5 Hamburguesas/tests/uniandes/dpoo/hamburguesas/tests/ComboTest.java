package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class ComboTest {

    private Combo combo;

    @BeforeEach
    void setUp( )
    {
        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>( );
        items.add( new ProductoMenu( "corral", 10000 ) );
        items.add( new ProductoMenu( "papas", 5000 ) );
        combo = new Combo( "combo corral", 0.1, items );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "combo corral", combo.getNombre( ) );
    }

    @Test
    void testGetPrecio( )
    {
        // (10000 + 5000) con 10% de descuento = 13500
        assertEquals( 13500, combo.getPrecio( ) );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "Combo combo corral\n" + " Descuento: 0.1\n" + "            13500\n";
        assertEquals( esperado, combo.generarTextoFactura( ) );
    }

}
