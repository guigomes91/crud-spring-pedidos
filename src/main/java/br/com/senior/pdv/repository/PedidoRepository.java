package br.com.senior.pdv.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.senior.pdv.modelo.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, UUID>{
	
	@Query("select p from Pedido p where p.id = :id")
	Page<Pedido> findByIdPaginacao(UUID id, Pageable paginacao);
}
