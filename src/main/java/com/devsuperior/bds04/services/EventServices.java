package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServices {

    @Autowired
    private EventRepository repository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional(readOnly = true)
    public Page<EventDTO> findAll(PageRequest pageRequest){
        Page<Event> page= repository.findAll(pageRequest);
        return page.map( x-> new EventDTO(x));
    }

    @Transactional(readOnly = true)
    public EventDTO findById(Long id){
        Optional<Event> event = repository.findById(id);
        return new EventDTO(event.orElseThrow(()-> new EntityNotFoundException()));
    }

    @Transactional
    public EventDTO update(Long id, EventDTO dto){
        Optional<Event> event = repository.findById(id);
        Event newEvent = event.orElseThrow(()-> new EntityNotFoundException());
        copyDTO(newEvent,dto);
        newEvent.setCity(cityRepository.getOne(dto.getCityId()));
        return new EventDTO(repository.save(newEvent));

    }

    @Transactional
    public EventDTO insert( EventDTO dto){
        Event newEvent = new Event();
        copyDTO(newEvent,dto);
        newEvent.setCity(cityRepository.getOne(dto.getCityId()));
        EventDTO newDTO = new EventDTO(repository.save(newEvent));
        return newDTO;
    }

   public void delete(Long id){
        repository.deleteById(id);
    }

    private Event copyDTO(Event newEvent, EventDTO dto){
        newEvent.setName(dto.getName());
        newEvent.setUrl(dto.getUrl());
        newEvent.setDate(dto.getDate());
        return newEvent;
    }
}
