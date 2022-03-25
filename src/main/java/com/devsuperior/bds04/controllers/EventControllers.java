package com.devsuperior.bds04.controllers;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.services.EventServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventControllers {

    @Autowired
    private EventServices services;

    @GetMapping
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("name"));
        Page<EventDTO> page = services.findAll(pageRequest);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EventDTO> findById(@PathVariable Long id){
        EventDTO  dto= services.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<EventDTO> insert(@Valid @RequestBody EventDTO dto){
        dto = services.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<EventDTO> update(@Valid @RequestBody EventDTO dto, @PathVariable Long id){
        dto= services.update(id,dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<EventDTO> delete(@PathVariable Long id){
        services.delete(id);
        return ResponseEntity.noContent().build();
    }
}
