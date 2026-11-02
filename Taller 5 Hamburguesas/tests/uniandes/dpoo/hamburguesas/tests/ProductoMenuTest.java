package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class ProductoMenuTest {
	/*
	De por sí en la clase no hay una excepción para quien intente
	poner un precio negativo a un producto. Pero eso es error de
	restricciones y los test se encargarían de ello si esa excepción 
	hubiera sido tenida en cuenta
	 * */ 

	private ProductoMenu producto;
	
	@BeforeEach
	void setUp() throws Exception {
		producto = new ProductoMenu("Quesadillas con Guac", 24000);
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
	void testGenerarTextoFactura() {
		assertEquals("Quesadillas con Guac"+"\n"+"            "+"24000"+"\n", producto.generarTextoFactura(), "El texto de la factura generada no fue el adecuado.");
	}

}
