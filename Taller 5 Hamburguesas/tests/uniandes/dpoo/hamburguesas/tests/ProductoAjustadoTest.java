package uniandes.dpoo.hamburguesas.tests;
import java.util.ArrayList;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;

class ProductoAjustadoTest {
	
	private ProductoAjustado producto;

	@BeforeEach
	void setUp() throws Exception {
		ProductoMenu prodMenu = new ProductoMenu("Quesadillas con Guac", 24000);
		producto = new ProductoAjustado(prodMenu);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetNombre() {
		assertEquals("Quesadillas con Guac", producto.getNombre(), "El nombre del producto no es el esperado");
	}
	@Test
	void testGetPrecio() {
		assertEquals(24000, producto.getPrecio(), "El precio que retornó el producto no es el esperado");
	}
	@Test
	void generarTextoFactura() {
		String esperado = "Quesadillas con Guac"+"            "+"24000"+"\n";
		assertEquals(esperado, producto.generarTextoFactura(),"La impresión de la factura fue incorrecta");

	}
	

}
