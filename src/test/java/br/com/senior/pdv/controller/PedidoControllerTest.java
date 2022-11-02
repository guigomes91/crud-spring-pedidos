package br.com.senior.pdv.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.senior.pdv.dto.PedidoDTO;
import br.com.senior.pdv.form.PedidoForm;
import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.repository.ItemRepository;
import br.com.senior.pdv.repository.PedidoItemRepository;
import br.com.senior.pdv.repository.PedidoRepository;
import br.com.senior.pdv.service.PedidoServiceImpl;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@WebMvcTest(controllers = PedidoController.class)
public class PedidoControllerTest {

	@MockBean
	private PedidoController pedidoController;
	
	@MockBean
	private PedidoServiceImpl service;
	
	@MockBean
	private ItemRepository itemRepository;
	
	@MockBean
	private PedidoItemRepository pedidoItemRepository;
	
	@MockBean
	private PedidoRepository pedidoRepository;
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.pedidoController);
	}
	
	@Test
	void deveListarPedidoPorUUID() {
		
		PedidoDTO pedido = new PedidoDTO();
		UUID uuid = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		
		Mockito.when(service.getById(uuid)).thenReturn(pedido);
		
		RestAssuredMockMvc.given()
		.accept(ContentType.JSON)
		.when()
			.get("/pedido/{id}", uuid)
		.then() 
			.statusCode(HttpStatus.OK.value());
		
		assertNotNull(pedido);
	}
	
	@Test
	void deveCadastrarUmPedido() throws Exception {
		Pedido pedido = new Pedido();
		pedido.setStatus("ABERTO");
		pedido.setTotal(200d);
		
		Date dateEmissao = new Date();
		PedidoForm pedidoForm = new PedidoForm(12345678945L, dateEmissao, 200d, 0, "ABERTO");
		
		mockMvc.perform(post("/pedido")
	            .contentType("application/json")
	            .content(objectMapper.writeValueAsString(pedidoForm)))
	            .andExpect(status().isOk());
		
		Mockito.when(service.cadastrar(pedidoForm)).thenReturn(pedido);
		assertEquals("ABERTO", pedido.getStatus());
		assertEquals(200d, pedido.getTotal());
	}
}
