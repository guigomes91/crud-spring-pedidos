package br.com.senior.pdv.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import br.com.senior.pdv.dto.PedidoDTO;
import br.com.senior.pdv.dto.PedidoItemDTO;
import br.com.senior.pdv.form.AtualizacaoPedidoForm;
import br.com.senior.pdv.modelo.Item;
import br.com.senior.pdv.modelo.Pedido;
import br.com.senior.pdv.modelo.PedidoItem;
import br.com.senior.pdv.repository.ItemRepository;
import br.com.senior.pdv.repository.PedidoItemRepository;
import br.com.senior.pdv.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PedidoServiceImplTest {

	@InjectMocks
	private PedidoServiceImpl service;
	
	@Mock
	private PedidoRepository pedidoRepository;
	
	@Mock
	private PedidoItemRepository pedidoItemRepository;
	
	@Mock
	private ItemRepository itemRepository;
	
	private UUID uuidPedido;
	
	@BeforeEach
	public void init() {
		uuidPedido = UUID.fromString("0ef8a0ea-e716-41ef-8a7e-c256bde8468c");
		Mockito.when(pedidoRepository.findById(uuidPedido)).thenReturn(Optional.of(new Pedido()));
	}
	
	@Test
	@DisplayName("Não permiti atualizar desconto sem itens no pedido")
	public void naoAtualizarDescontoEmPedidoSemItens() {
		AtualizacaoPedidoForm form = new AtualizacaoPedidoForm(123456789L, 200d, 10, "ABERTO");
		List<PedidoItemDTO> itens = new ArrayList<>();
		
		Mockito.when(pedidoItemRepository.consultarPorPedido(uuidPedido)).thenReturn(itens);
		Mockito.when(service.atualizar(uuidPedido, form)).thenReturn(null);
		
		PedidoDTO pedidoDTO = service.atualizar(uuidPedido, form);
		assertNull(pedidoDTO);
	}
	
	@Test
	@DisplayName("Não permiti realizar desconto com pedido FECHADO")
	public void naoAtualizarDescontoComPedidoEncerrado() {
		AtualizacaoPedidoForm form = new AtualizacaoPedidoForm(123456789L, 200d, 10, "FECHADO");
		UUID uuid = UUID.fromString("7b5f26f4-28a6-40b1-bcf6-73d34d1b98ae");
		List<PedidoItemDTO> itens = new ArrayList<>();
		Item item = new Item("TESTE", 20d, 1, true);
		Pedido pedidoEntity = new Pedido();
		pedidoEntity.setId(uuidPedido);
		itens.add(new PedidoItemDTO(new PedidoItem(uuid, 2, 100d, 0.0d, item, pedidoEntity)));
		
		uuid = UUID.fromString("70698d96-3c1d-4724-b84e-352b8daf3c6d");
		item = new Item("TESTE 2", 10d, 1, true);
		itens.add(new PedidoItemDTO(new PedidoItem(uuid, 2, 100d, 0.0d, item, pedidoEntity)));
		
		Mockito.when(pedidoItemRepository.consultarPorPedido(uuidPedido)).thenReturn(itens);
		Mockito.when(service.atualizar(uuidPedido, form)).thenReturn(null);
		
		PedidoDTO pedidoDTO = service.atualizar(uuid, form);
		assertNull(pedidoDTO);
	}
	
	@Test
	@DisplayName("Permiti realizar desconto no pedido com situacao ABERTO")
	public void deveAtualizarDesconto() {
		//Valor inical sem o desconto aplicado: 200.00
		AtualizacaoPedidoForm form = new AtualizacaoPedidoForm(123456789L, 200d, 10, "ABERTO");
		
		List<PedidoItemDTO> itens = new ArrayList<>();
		Pedido pedidoEntity = new Pedido();
		pedidoEntity.setId(uuidPedido);
		UUID uuidItem = UUID.fromString("7b5f26f4-28a6-40b1-bcf6-73d34d1b98ae");
		Item itemA = new Item(uuidItem, "TESTE", 20d, 1, true);
		itens.add(new PedidoItemDTO(new PedidoItem(uuidPedido, 2, 100d, 0.0d, itemA, pedidoEntity)));
		
		uuidItem = UUID.fromString("70698d96-3c1d-4724-b84e-352b8daf3c6d");
		Item itemB = new Item(uuidItem, "TESTE 2", 10d, 1, true);
		itens.add(new PedidoItemDTO(new PedidoItem(uuidPedido, 2, 100d, 0.0d, itemB, pedidoEntity)));
		
		Pedido pedidoNew = new Pedido();
		pedidoNew.setId(uuidPedido);
		pedidoNew.setCpf(form.getCpf());
		pedidoNew.setDesconto(form.getDesconto());
		pedidoNew.setStatus(form.getStatus());
		pedidoNew.setTotal(form.getTotal());
		
		Mockito.when(pedidoRepository.getReferenceById(uuidPedido)).thenReturn(pedidoNew);
		Mockito.when(pedidoItemRepository.consultarPorPedido(uuidPedido)).thenReturn(itens);
		Mockito.when(itemRepository.getItem(UUID.fromString("7b5f26f4-28a6-40b1-bcf6-73d34d1b98ae"))).thenReturn(itemA);
		Mockito.when(itemRepository.getItem(UUID.fromString("70698d96-3c1d-4724-b84e-352b8daf3c6d"))).thenReturn(itemB);
		
		PedidoDTO pedidoDTO = service.atualizar(uuidPedido, form);
		
		assertNotNull(pedidoDTO);
		
		//Valor final com o desconto aplicado de 10%: 180.00
		assertEquals(180d, pedidoDTO.getTotal());
	}
}
