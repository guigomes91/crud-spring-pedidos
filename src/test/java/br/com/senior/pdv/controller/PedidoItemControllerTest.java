package br.com.senior.pdv.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import br.com.senior.pdv.dto.PedidoItemDTO;
import br.com.senior.pdv.repository.ItemRepository;
import br.com.senior.pdv.repository.PedidoItemRepository;
import br.com.senior.pdv.repository.PedidoRepository;
import br.com.senior.pdv.service.PedidoItemServiceImpl;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@WebMvcTest
public class PedidoItemControllerTest {

	@MockBean
	private PedidoItemController pedidoItemController;
	
	@MockBean
	private PedidoItemServiceImpl pedidoItemService;
	
	@MockBean
	private ItemRepository itemRepository;
	
	@MockBean
	private PedidoItemRepository pedidoItemRepository;
	
	@MockBean
	private PedidoRepository pedidoRepository;
	
	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.pedidoItemController);
	}
	
	@Test
	public void deveRetornarOk_QuandoBuscarItensDoPedido() throws Exception {
		UUID uuid = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		
		List<PedidoItemDTO> result = new ArrayList<>();
		result.add(new PedidoItemDTO());
		result.add(new PedidoItemDTO());
		result.add(new PedidoItemDTO());
		
		Mockito.when(pedidoItemService.getByPedido(uuid)).thenReturn(result);
		
		RestAssuredMockMvc.given()
			.accept(ContentType.JSON)
		.when()
			.get("/pedidoitem/{id}", uuid)
		.then() 
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveRetornarNotAllowed_QuandoBuscarItensDoPedido() throws Exception {
		RestAssuredMockMvc
		.given()
			.accept(ContentType.JSON)
		.when()
			.get("/pedidoitem/{id}", "")
		.then() 
			.statusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
	}
}
