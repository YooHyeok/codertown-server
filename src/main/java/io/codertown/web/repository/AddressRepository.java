package io.codertown.web.repository;

import io.codertown.web.entity.address.AddressFirst;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressFirst, Long> {

}