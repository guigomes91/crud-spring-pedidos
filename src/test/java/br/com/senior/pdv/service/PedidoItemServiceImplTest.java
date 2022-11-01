package br.com.senior.pdv.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.senior.pdv.modelo.Item;
import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.modelo.PedidoItem;
import br.com.senior.pdv.repository.ItemRepository;
import br.com.senior.pdv.repository.PedidoItemRepository;
import br.com.senior.pdv.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
public class PedidoItemServiceImplTest {

	@InjectMocks
	private PedidoItemServiceImpl service;
	
	@Mock
	private PedidoItemRepository repository;
	
	@Mock
	private PedidoRepository pedidoRepository;
	
	@Mock
	private ItemRepository itemRepository;
	
	@Test
	@DisplayName("Não insere o item no pedido caso esteja desativado")
	void naoDeveriaInserirItemDesativado() {
		UUID uuidPedido = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		Optional<Pedido> opcionalPedido = Optional.of(new Pedido());
		
		UUID uuidItem = UUID.fromString("0e3bd77b-8932-4afd-8eb4-77fd16496eb6");
		Optional<Item> opcionalItem = Optional.of(new Item("TESTE", 6.99d, 1, false));
		
		Mockito.when(pedidoRepository.findById(uuidPedido)).thenReturn(opcionalPedido);
		Mockito.when(itemRepository.findById(uuidItem)).thenReturn(opcionalItem);
		
		PedidoItem pedidoItem = new PedidoItem();
		Pedido pedido = new Pedido();
		pedido.setId(uuidPedido);
		pedidoItem.setPedido(pedido);
		Item item = new Item();
		item.setId(uuidItem);
		pedidoItem.setItem(item);		
		
		assertNull(service.cadastrar(pedidoItem));
	}
	
	@Test
	@DisplayName("Insere o item no pedido caso esteja ativo")
	void deveriaInserirItemAtivo() {
		UUID uuidPedido = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		Optional<Pedido> opcionalPedido = Optional.of(new Pedido());
		
		UUID uuidItem = UUID.fromString("0e3bd77b-8932-4afd-8eb4-77fd16496eb6");
		Optional<Item> opcionalItem = Optional.of(new Item("TESTE", 6.99d, 1, true));
		
		Mockito.when(pedidoRepository.findById(uuidPedido)).thenReturn(opcionalPedido);
		Mockito.when(itemRepository.findById(uuidItem)).thenReturn(opcionalItem);
		
		PedidoItem pedidoItem = new PedidoItem();
		Pedido pedido = new Pedido();
		pedido.setId(uuidPedido);
		pedidoItem.setPedido(pedido);
		Item item = new Item();
		item.setId(uuidItem);
		pedidoItem.setItem(item);		
		
		assertNotNull(service.cadastrar(pedidoItem));
	}
}
