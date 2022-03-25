package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.awt.print.Pageable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityServices {

    @Autowired
    private CityRepository repository;

    @Autowired
    private EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<CityDTO> findAll(){
        List<City> list = repository.findAll(Sort.by("name"));
        return list.stream().map( x-> new CityDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CityDTO findById(Long id){
        Optional<City> city = repository.findById(id);
        return new CityDTO(city.orElseThrow(()-> new EntityNotFoundException()));
    }

    @Transactional
    public CityDTO update(Long id, CityDTO dto){
        Optional<City> city = repository.findById(id);
        City newCity = city.orElseThrow(()-> new EntityNotFoundException());
        copyDTO(newCity,dto);
        for(Event event: newCity.getEvents()){
            Event eventAdd =eventRepository.getOne(event.getId());
            newCity.getEvents().add(eventAdd);
        }
        return new CityDTO(repository.save(newCity));

    }

    @Transactional
    public CityDTO insert( CityDTO dto){
        City newCity = new City();
        copyDTO(newCity,dto);
        return new CityDTO(repository.save(newCity));
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    private City copyDTO(City newCity, CityDTO dto){
        newCity.setName(dto.getName());
        return newCity;
    }
}
