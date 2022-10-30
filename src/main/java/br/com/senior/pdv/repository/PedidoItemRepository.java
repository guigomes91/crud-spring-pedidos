package br.com.senior.pdv.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.senior.pdv.dto.PedidoItemDTO;
import br.com.senior.pdv.modelo.PedidoItem;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, UUID>{

	@Query("select pi from PedidoItem pi where pi.pedido = :id")
	Page<PedidoItem> findByIdPaginacao(UUID id, Pageable paginacao);

	@Query("select pi from PedidoItem pi where pedido_id = :id")
	List<PedidoItemDTO> consultarPorPedido(@Param("id") UUID id);
	
	@Modifying
	@Query("update PedidoItem pi set pi.quantidade = ?1, pi.desconto = ?2, pi.total = ?3 where pi.id = ?4")
	void atualizarDescontoItem(int quantidade, double desconto, double total, UUID id);
}
