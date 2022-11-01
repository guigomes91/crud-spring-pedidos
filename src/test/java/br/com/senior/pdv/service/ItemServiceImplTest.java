package br.com.senior.pdv.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.senior.pdv.dto.ItemDTO;
import br.com.senior.pdv.form.ItemForm;
import br.com.senior.pdv.modelo.Item;
import br.com.senior.pdv.repository.ItemRepository;
import br.com.senior.pdv.repository.PedidoItemRepository;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

	@InjectMocks
	private ItemServiceImpl service;
	
	@Mock
	private ItemRepository repository;
	
	@Mock
	private PedidoItemRepository pedidoItemRepository;
	
	@Test
	void deveriaRetornarUmItemComIdCadastrado() {
		UUID uuid = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		
		Optional<Item> opcional = Optional.of(new Item());
		Mockito.when(repository.findById(uuid)).thenReturn(opcional);
		Optional<Item> it = service.getById(uuid);
	
		assertNotNull(it);
	} 
	
	@Test
	void naoDeveriaRetornarUmItem() {
		UUID uuid = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		
		Optional<Item> opcional = null;
		Mockito.when(repository.findById(uuid)).thenReturn(opcional);
		Optional<Item> it = service.getById(uuid);
		
		assertNull(it);
	}
	
	@Test
	void deveriaAtualizarUmItem() {
		UUID uuid = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		Optional<Item> opcional = Optional.of(new Item());
		Mockito.when(repository.findById(uuid)).thenReturn(opcional);
		
		ItemForm form = new ItemForm("TESTE", 1, 2.99d, true);
		
		Mockito.when(repository.getReferenceById(uuid)).thenReturn(new Item("TESTE", 2.99d, 1, true));
		
		ItemDTO itemDTO = service.atualizar(uuid, form);
		assertNotNull(itemDTO);
		assertEquals(itemDTO.getDescricao(), form.getDescricao());
	}
	
	@Test
	@DisplayName("Não deleta o produto caso esteja em um pedido")
	void naoDeveriaDeletarItemNoPedido() {
		UUID uuid = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		Optional<Item> opcional = Optional.of(new Item());
		Mockito.when(repository.findById(uuid)).thenReturn(opcional);
		
		Mockito.when(pedidoItemRepository.itemEmPedido(uuid)).thenReturn("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		
		assertFalse(service.deletar(uuid));
	}
	
	@Test
	@DisplayName("Deleta o produto caso não esteja em nenhum pedido")
	void deveriaDeletarItemSemPedido() {
		UUID uuid = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		Optional<Item> opcional = Optional.of(new Item());
		Mockito.when(repository.findById(uuid)).thenReturn(opcional);
		
		Mockito.when(pedidoItemRepository.itemEmPedido(uuid)).thenReturn("");
		
		assertTrue(service.deletar(uuid));
	}
}
