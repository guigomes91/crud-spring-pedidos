package br.com.senior.pdv.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.senior.pdv.modelo.Item;

public interface ItemRepository extends JpaRepository<Item, UUID> {
	
	@Query("select i from Item i where i.id = :id")
	Page<Item> findByIdPaginacao(UUID id, Pageable paginacao);
	
	@Query("select i from Item i where i.id = ?1")
	Item getItem(UUID id);
}
