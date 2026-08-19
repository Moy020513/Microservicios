package com.moises.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moises.auth.entities.Rol;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    boolean existsByNombre(String nombre);

    Optional<Rol> findByNombre(String nombre);
}
